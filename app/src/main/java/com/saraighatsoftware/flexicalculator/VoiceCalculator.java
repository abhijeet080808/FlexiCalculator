package com.saraighatsoftware.flexicalculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL;
import static android.speech.RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS;
import static android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;
import static android.speech.SpeechRecognizer.RESULTS_RECOGNITION;

class VoiceCalculator implements RecognitionListener {

    // http://www.truiton.com/2014/06/android-speech-recognition-without-dialog-custom-activity/

    private static final String TAG = "VoiceCalculator";

    private final SpeechRecognizer mRecognizer;
    private final VoiceResultListener mResultListener;

    private long mListenStartTimeMillis;

    private final String[] mOperatorKeywords;
    private final String[] mOperatorReplacements;

    private final Calculator mCalculator;

    private final Converter[] mConverters;

    // map of unit keyword vs unit
    private HashMap<String, Converter.Unit> mConverterKeywords;
    private Pattern mConverterPattern;

    private String mLastResult;

    VoiceCalculator(Context context, VoiceResultListener resultListener) {
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

        // TODO improve keywords
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
        mConverterPattern = Pattern.compile("([0-9]+.*[0-9]*) ([a-z]+) to ([a-z]+)");

        mLastResult = "";
    }

    void Start() {
        if (mRecognizer == null) {
            mResultListener.Error(-1, "Speech Recognition Service Is Not Available on This Phone");
            return;
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(EXTRA_LANGUAGE_MODEL, LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 5000);

        mListenStartTimeMillis = System.currentTimeMillis();
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

    public void onBeginningOfSpeech() {
        // user has started to speak
    }

    public void onBufferReceived(byte[] buffer) {
        // more sound has been received
    }

    public void onEndOfSpeech() {
        // called after the user stops speaking
        if (mListenStartTimeMillis != 0) {
            mResultListener.IsListening(VoiceResultListener.ListenState.PROCESSING);
        } else {
            // this is a forced stop
            mResultListener.IsListening(VoiceResultListener.ListenState.IDLE);
        }
    }

    public void onError(int error) {
        // network or recognition error occurred
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                mResultListener.Error(error, "Failed to Record Audio while Performing Speech Recognition");
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                mResultListener.Error(error, "Speech Recognition Client Error");
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                mResultListener.Error(error, "Insufficient Permission to Record Audio for Speech Recognition");
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                mResultListener.Error(error, "Network Failed while Performing Speech Recognition");
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                mResultListener.Error(error, "Network Timed Out while Performing Speech Recognition");
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                if (System.currentTimeMillis() - mListenStartTimeMillis > 4000 && mListenStartTimeMillis != 0) {
                    // this bypasses the error message in case of -
                    // - android bug when it stops listening on its own just after starting
                    // - user stops the listening
                    mResultListener.Error(error, "Failed to Recognise Voice while Performing Speech Recognition");
                }
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                mResultListener.Error(error, "Speech Recognition Service Busy");
                break;
            case SpeechRecognizer.ERROR_SERVER:
                mResultListener.Error(error, "Speech Recognition Server Error");
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                if (mListenStartTimeMillis != 0) {
                    // this bypasses the error message in case of -
                    // - user stops the listening
                    mResultListener.Error(error, "Failed to Hear Voice while Performing Speech Recognition");
                }
                break;
            default:
                break;
        }
        mResultListener.IsListening(VoiceResultListener.ListenState.IDLE);
    }

    public void onEvent(int eventType, Bundle params) {
        // reserved for adding future events
    }

    public void onPartialResults(Bundle partialResults) {
        // called when partial recognition results are available
    }

    public void onReadyForSpeech(Bundle params) {
        // called when the endpoint is ready for the user to start speaking
        mResultListener.IsListening(VoiceResultListener.ListenState.LISTENING);
    }

    public void onResults(Bundle results) {
        // called when recognition results are ready
        mResultListener.IsListening(VoiceResultListener.ListenState.IDLE);
        ArrayList<String> result = results.getStringArrayList(RESULTS_RECOGNITION);
        if (result == null || result.isEmpty()) {
            mResultListener.Error(-1, "Failed to Process Voice Input");
            return;
        }

        if (!convert(result)) {
            if (!calculate(result)) {
                mResultListener.Error(-1, "Failed to Process Voice Input - " + result.get(0));
            }
        }
    }

    public void onRmsChanged(float rmsDb) {

    }

    // ---------------------------------------------

    private boolean calculate(final ArrayList<String> inputList) {
        Log.v(TAG, "Parsing " + inputList);
        for (String input: inputList) {
            // replace recognized operator keywords
            String replaced_input =
                    StringUtils.replaceEach(input, mOperatorKeywords, mOperatorReplacements);
            // TODO negative numbers
            // try to break down to infix expression tokens
            String[] split_input = StringUtils.split(replaced_input);
            // drop all non numbers and non operators
            ArrayList<String> infix_expression = new ArrayList<>();
            for (String s : split_input) {
                if (mCalculator.IsOperator(s) || mCalculator.IsOperand(s, Calculator.Base.DEC)) {
                    infix_expression.add(s);
                }
            }
            if (infix_expression.isEmpty() || infix_expression.size() == 1) {
                // nothing to evaluate
                continue;
            }
            // use previous result if applicable
            if (mCalculator.IsBinaryOperator(infix_expression.get(0)) && !mLastResult.isEmpty()) {
                infix_expression.add(0, mLastResult);
            }

            try {
                String infix_string = StringUtils.join(infix_expression, "");
                Log.v(TAG, "Parsing " + infix_string);
                String result = mCalculator.Evaluate(
                        infix_expression,
                        Calculator.Base.DEC,
                        Calculator.AngularUnit.DEGREE);

                mLastResult = result;

                mResultListener.Result(infix_string);
                mResultListener.Result(result);
                Log.v(TAG, "Parsed " + infix_string + " as " + result);

                return true;
            } catch (Exception e) {
                // do nothing
            }
        }
        return false;
    }

    private boolean convert(final ArrayList<String> inputList) {
        Log.v(TAG, "Converting " + inputList);
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
                    Converter converter;
                    // as per mConverters initialization above
                    try {
                        if (input_unit instanceof ConverterVolume.VolumeUnit &&
                                output_unit instanceof ConverterVolume.VolumeUnit) {
                            converter = mConverters[0];
                        } else if (input_unit instanceof ConverterWeight.WeightUnit &&
                                output_unit instanceof ConverterWeight.WeightUnit) {
                            converter = mConverters[1];
                        } else if (input_unit instanceof ConverterLength.LengthUnit &&
                                output_unit instanceof ConverterLength.LengthUnit) {
                            converter = mConverters[2];
                        } else if (input_unit instanceof ConverterArea.AreaUnit &&
                                output_unit instanceof ConverterArea.AreaUnit) {
                            converter = mConverters[3];
                        } else if (input_unit instanceof ConverterFuelEconomy.FuelEconomyUnit &&
                                output_unit instanceof ConverterFuelEconomy.FuelEconomyUnit) {
                            converter = mConverters[4];
                        } else if (input_unit instanceof ConverterTemperature.TemperatureUnit &&
                                output_unit instanceof ConverterTemperature.TemperatureUnit) {
                            converter = mConverters[5];
                        } else if (input_unit instanceof ConverterPressure.PressureUnit &&
                                output_unit instanceof ConverterPressure.PressureUnit) {
                            converter = mConverters[6];
                        } else if (input_unit instanceof ConverterEnergy.EnergyUnit &&
                                output_unit instanceof ConverterEnergy.EnergyUnit) {
                            converter = mConverters[7];
                        } else if (input_unit instanceof ConverterPower.PowerUnit &&
                                output_unit instanceof ConverterPower.PowerUnit) {
                            converter = mConverters[8];
                        } else if (input_unit instanceof ConverterTorque.TorqueUnit &&
                                output_unit instanceof ConverterTorque.TorqueUnit) {
                            converter = mConverters[9];
                        } else if (input_unit instanceof ConverterSpeed.SpeedUnit &&
                                output_unit instanceof ConverterSpeed.SpeedUnit) {
                            converter = mConverters[10];
                        } else if (input_unit instanceof ConverterTime.TimeUnit &&
                                output_unit instanceof ConverterTime.TimeUnit) {
                            converter = mConverters[11];
                        } else if (input_unit instanceof ConverterData.DataUnit &&
                                output_unit instanceof ConverterData.DataUnit) {
                            converter = mConverters[12];
                        } else if (input_unit instanceof ConverterAngle.AngleUnit &&
                                output_unit instanceof ConverterAngle.AngleUnit) {
                            converter = mConverters[13];
                        } else {
                            // unknown units or mismatched units
                            continue;
                        }

                        result = converter.Convert(input_value, input_unit, output_unit);
                        mResultListener.Result(input_value + " " + input_unit);
                        mResultListener.Result(result + " " + output_unit);
                        Log.v(TAG, "Converted " + input_value + " " + input_unit +
                                " to " + result + " " + output_unit);
                        return true;
                    } catch (Exception e) {
                        // do nothing
                    }
                } else {
                    Log.v(TAG, "Unknown units " + input_unit_string + " " + output_unit_string);
                }
            } else {
                Log.v(TAG, "Unknown conversion command " + input);
            }
        }
        return false;
    }
}
