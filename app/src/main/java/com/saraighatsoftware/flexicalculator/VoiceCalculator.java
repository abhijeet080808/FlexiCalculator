package com.saraighatsoftware.flexicalculator;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL;
import static android.speech.RecognizerIntent.EXTRA_MAX_RESULTS;
import static android.speech.RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS;
import static android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;
import static android.speech.SpeechRecognizer.RESULTS_RECOGNITION;

class VoiceCalculator implements RecognitionListener {

    // http://www.truiton.com/2014/06/android-speech-recognition-without-dialog-custom-activity/

    private static final String TAG = "VoiceCalculator";

    private static final int AUDIO_UNMUTE_DELAY_MILLIS = 1000;

    private final Context mContext;

    private final SpeechRecognizer mRecognizer;
    private final VoiceResultListener mResultListener;

    private long mListenStartTimeMillis;

    private final String[] mOperatorKeywords;
    private final String[] mOperatorReplacements;

    private final Calculator mCalculator;

    private final Converter[] mConverters;

    // map of unit keyword vs unit
    private final HashMap<String, Converter.Unit> mConverterKeywords;
    private final Pattern mConverterPattern;

    private String mLastResult;

    private final AudioManager mAudioManager;
    private final Handler mAudioHandler;
    private final Runnable mAudioUnmuteRunnable;

    VoiceCalculator(Context context, VoiceResultListener resultListener) {
        mContext = context;

        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            mRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
            mRecognizer.setRecognitionListener(this);
        } else {
            mRecognizer = null;
        }

        mResultListener = resultListener;

        // http://par.cse.nsysu.edu.tw/link/math-pronunciation.pdf
        // strings can be in form oneplus 2, one plus 2, 1 + 2, one plus two etc
        // locale affects recognition of words like 1 million vs 10 lakhs
        ArrayList<String> operator_keywords = new ArrayList<>();
        ArrayList<String> operator_replacements = new ArrayList<>();
        for (Operator operator : OperatorFinder.GetAll()) {
            String keywords[] = operator.Keywords();
            for (String keyword : keywords) {
                operator_keywords.add(keyword);
                operator_replacements.add(operator.Symbol());
            }
        }
        // special keyword that maps to more than 1 operator
        operator_keywords.add("% of");
        operator_replacements.add(" " + Operator.PERCENTAGE.Symbol() + " " + Operator.MULTIPLY.Symbol());

        mOperatorKeywords = operator_keywords.toArray(new String[operator_keywords.size()]);
        mOperatorReplacements = operator_replacements.toArray(new String[operator_replacements.size()]);

        mCalculator = new Calculator();

        mConverters = new Converter[] {
                new ConverterVolume(context),
                new ConverterWeight(context),
                new ConverterLength(context),
                new ConverterArea(context),
                new ConverterFuelEconomy(context),
                new ConverterTemperature(context),
                new ConverterPressure(context),
                new ConverterEnergy(context),
                new ConverterPower(context),
                new ConverterTorque(context),
                new ConverterSpeed(context),
                new ConverterTime(context),
                new ConverterData(context),
                new ConverterAngle(context)
        };

        // mapping of converter unit vs keywords
        mConverterKeywords = new HashMap<>();
        for (Converter converter : mConverters) {
            for (Converter.Unit unit : converter.GetAllUnits()) {
                String[] keywords = unit.GetKeywords();
                for (String keyword : keywords) {
                    mConverterKeywords.put(keyword, unit);
                }
            }
        }

        // matches - 1.5 litre to ml
        mConverterPattern = Pattern.compile("([0-9]+\\.*[0-9]*) ([a-z ]+) to ([a-z ]+)");

        mLastResult = "";

        // use this to mute audio before listening on user speech to avoid beep
        // also use this to un-mute audio after user speech ends
        //
        // - this fixes bug when recogniser stops listening on its own just after starting recognition
        //   this happens because speech recognizer identifies the beep as start of speech and
        //   ending of the beep as end of speech
        // - it is less irritating this way
        mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        mAudioHandler = new Handler();
        mAudioUnmuteRunnable = new Runnable() {
            @Override
            @SuppressWarnings("deprecation")
            public void run() {
                mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            }
        };
    }

    void Start() {
        if (mRecognizer == null) {
            mResultListener.OnListenError(-1, "Speech Recognition Service Is Not Available on This Phone");
            return;
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(EXTRA_LANGUAGE_MODEL, LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 5000);
        intent.putExtra(EXTRA_MAX_RESULTS, 50);

        mListenStartTimeMillis = System.currentTimeMillis();
        muteAudio();
        mRecognizer.startListening(intent);
    }

    void Stop() {
        if (mRecognizer != null) {
            mListenStartTimeMillis = 0;
            mRecognizer.stopListening();
        }
    }

    void Cancel() {
        if (mRecognizer != null) {
            mListenStartTimeMillis = 0;
            mRecognizer.cancel();
        }
    }

    void Destroy() {
        if (mRecognizer != null) {
            mListenStartTimeMillis = 0;
            mRecognizer.destroy();
        }
    }

    // -------- RecognitionListener methods --------

    // User does not speak - onEndOfSpeech, onError
    // User cancels soon - onError
    // User speaks - onEndOfSpeech, onResults

    public void onBeginningOfSpeech() {
        // user has started to speak
    }

    public void onBufferReceived(byte[] buffer) {
        // more sound has been received
    }

    public void onEndOfSpeech() {
        mResultListener.OnListenStateChange(VoiceResultListener.ListenState.PROCESSING);
    }

    public void onError(int error) {
        // network or recognition error occurred
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                mResultListener.OnListenError(error, "Failed to Record Audio while Performing Speech Recognition");
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                mResultListener.OnListenError(error, "Speech Recognition Client Error");
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                mResultListener.OnListenError(error, "Insufficient Permission to Record Audio for Speech Recognition");
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                mResultListener.OnListenError(error, "Network Failed while Performing Speech Recognition");
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                mResultListener.OnListenError(error, "Network Timed Out while Performing Speech Recognition");
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                if (System.currentTimeMillis() - mListenStartTimeMillis > 4000 && mListenStartTimeMillis != 0) {
                    // this bypasses the error message in case of -
                    // - android bug when it stops listening on its own just after starting
                    // - user stops the listening
                    mResultListener.OnListenError(error, "Failed to Recognise Voice while Performing Speech Recognition");
                }
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                mResultListener.OnListenError(error, "Speech Recognition Service Busy");
                break;
            case SpeechRecognizer.ERROR_SERVER:
                mResultListener.OnListenError(error, "Speech Recognition Server Error");
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                if (mListenStartTimeMillis != 0) {
                    // this bypasses the error message in case of -
                    // - user stops the listening
                    mResultListener.OnListenError(error, "Failed to Hear Voice while Performing Speech Recognition");
                }
                break;
            default:
                break;
        }
        delayedUnmuteAudio();
        mResultListener.OnListenStateChange(VoiceResultListener.ListenState.IDLE);
    }

    public void onEvent(int eventType, Bundle params) {
        // reserved for adding future events
    }

    public void onPartialResults(Bundle partialResults) {
        // called when partial recognition results are available
    }

    public void onReadyForSpeech(Bundle params) {
        // called when the endpoint is ready for the user to start speaking
        mResultListener.OnListenStateChange(VoiceResultListener.ListenState.LISTENING);
    }

    public void onResults(Bundle results) {
        // called when recognition results are ready
        ArrayList<String> result = results.getStringArrayList(RESULTS_RECOGNITION);
        if (result == null || result.isEmpty()) {
            mResultListener.OnListenError(-1, "Failed to Process Voice Input");
            return;
        }

        if (!convert(result)) {
            if (!calculate(result)) {
                // TODO process words like 1 billion as google doesn't always give those as numbers
                mResultListener.OnListenError(-1, "Failed to Process Voice Input - " + result.get(0));
            }
        }
        delayedUnmuteAudio();
        mResultListener.OnListenStateChange(VoiceResultListener.ListenState.IDLE);
    }

    public void onRmsChanged(float rmsDb) {

    }

    // ---------------------------------------------

    @SuppressWarnings("deprecation")
    private void muteAudio() {
        mAudioHandler.removeCallbacks(mAudioUnmuteRunnable);
        mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
    }

    private void delayedUnmuteAudio() {
        mAudioHandler.postDelayed(mAudioUnmuteRunnable, AUDIO_UNMUTE_DELAY_MILLIS);
    }

    private boolean calculate(final ArrayList<String> inputList) {
        Logger.v(TAG, "Parsing " + inputList);
        for (String input: inputList) {
            // replace recognized operator keywords
            String replaced_input =
                    StringUtils.replaceEach(input, mOperatorKeywords, mOperatorReplacements);
            // try to break down to infix expression tokens
            String[] split_input = StringUtils.split(replaced_input);
            ArrayList<String> infix_expression = new ArrayList<>(Arrays.asList(split_input));
            if (infix_expression.isEmpty()) {
                // nothing to evaluate
                continue;
            }
            // use previous result if applicable
            if (mCalculator.IsBinaryOperator(infix_expression.get(0)) && !mLastResult.isEmpty()) {
                infix_expression.add(0, mLastResult);
            }

            try {
                String infix_string = StringUtils.join(infix_expression, "");
                Logger.v(TAG, "Parsing " + infix_string);
                String result = mCalculator.Evaluate(
                        infix_expression,
                        Calculator.Base.DEC,
                        Calculator.AngularUnit.DEGREE);

                mLastResult = result;

                mResultListener.OnListenResult(
                        infix_string + mContext.getString(R.string.equal) + result);
                Logger.v(TAG, "Parsed " + infix_string + " as " + result);

                return true;
            } catch (Exception e) {
                // do nothing
            }
        }
        return false;
    }

    private boolean convert(final ArrayList<String> inputList) {
        Logger.v(TAG, "Converting " + inputList);
        // process strings like convert 1 ml to litre
        for (String input : inputList) {
            input = input.toLowerCase();
            Matcher m = mConverterPattern.matcher(input);
            if (m.matches()) {
                String input_value = m.group(1);
                String input_unit_string = m.group(2);
                String output_unit_string = m.group(3);

                Converter.Unit input_unit = mConverterKeywords.get(input_unit_string);
                Converter.Unit output_unit = mConverterKeywords.get(output_unit_string);

                if (input_unit != null && output_unit != null) {
                    String result;
                    Converter converter = getConverter(input_unit, output_unit);
                    // as per mConverters initialization above
                    try {
                        result = converter.Convert(input_value, input_unit, output_unit);
                        mResultListener.OnListenResult(input_value
                                        + " " + input_unit
                                        + " " + mContext.getString(R.string.is)
                                        + " " + result
                                        + " " + output_unit);
                        Logger.v(TAG, "Converted " + input_value + " " + input_unit +
                                " to " + result + " " + output_unit);
                        return true;
                    } catch (Exception e) {
                        // do nothing
                    }
                } else {
                    Logger.v(TAG, "Unknown units " + input_unit_string + " " + output_unit_string);
                }
            } else {
                Logger.v(TAG, "Unknown conversion command " + input);
            }
        }
        return false;
    }

    private Converter getConverter(Converter.Unit inputUnit, Converter.Unit outputUnit) {
        if (inputUnit instanceof ConverterVolume.VolumeUnit &&
                outputUnit instanceof ConverterVolume.VolumeUnit) {
            return mConverters[0];
        }
        if (inputUnit instanceof ConverterWeight.WeightUnit &&
                outputUnit instanceof ConverterWeight.WeightUnit) {
            return mConverters[1];
        }
        if (inputUnit instanceof ConverterLength.LengthUnit &&
                outputUnit instanceof ConverterLength.LengthUnit) {
            return mConverters[2];
        }
        if (inputUnit instanceof ConverterArea.AreaUnit &&
                outputUnit instanceof ConverterArea.AreaUnit) {
            return mConverters[3];
        }
        if (inputUnit instanceof ConverterFuelEconomy.FuelEconomyUnit &&
                outputUnit instanceof ConverterFuelEconomy.FuelEconomyUnit) {
            return mConverters[4];
        }
        if (inputUnit instanceof ConverterTemperature.TemperatureUnit &&
                outputUnit instanceof ConverterTemperature.TemperatureUnit) {
            return mConverters[5];
        }
        if (inputUnit instanceof ConverterPressure.PressureUnit &&
                outputUnit instanceof ConverterPressure.PressureUnit) {
            return mConverters[6];
        }
        if (inputUnit instanceof ConverterEnergy.EnergyUnit &&
                outputUnit instanceof ConverterEnergy.EnergyUnit) {
            return mConverters[7];
        }
        if (inputUnit instanceof ConverterPower.PowerUnit &&
                outputUnit instanceof ConverterPower.PowerUnit) {
            return mConverters[8];
        }
        if (inputUnit instanceof ConverterTorque.TorqueUnit &&
                outputUnit instanceof ConverterTorque.TorqueUnit) {
            return mConverters[9];
        }
        if (inputUnit instanceof ConverterSpeed.SpeedUnit &&
                outputUnit instanceof ConverterSpeed.SpeedUnit) {
            return mConverters[10];
        }
        if (inputUnit instanceof ConverterTime.TimeUnit &&
                outputUnit instanceof ConverterTime.TimeUnit) {
            return mConverters[11];
        }
        if (inputUnit instanceof ConverterData.DataUnit &&
                outputUnit instanceof ConverterData.DataUnit) {
            return mConverters[12];
        }
        if (inputUnit instanceof ConverterAngle.AngleUnit &&
                outputUnit instanceof ConverterAngle.AngleUnit) {
            return mConverters[13];
        }
        // unknown units or mismatched units
        return null;
    }
}
