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

import static android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL;
import static android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;
import static android.speech.SpeechRecognizer.RESULTS_RECOGNITION;

class VoiceCalculator implements RecognitionListener {

    // http://www.truiton.com/2014/06/android-speech-recognition-without-dialog-custom-activity/

    private static final String TAG = "VoiceCalculator";

    private final SpeechRecognizer mRecognizer;
    private final VoiceResultListener mResultListener;

    private final String[] mOperatorKeywords;
    private final String[] mOperatorReplacements;

    private final Calculator mCalculator;
    private String mLastResult;

    VoiceCalculator(Context context, VoiceResultListener resultListener) {
        mResultListener = resultListener;
        mCalculator = new Calculator();
        mLastResult = "";

        // http://par.cse.nsysu.edu.tw/link/math-pronunciation.pdf
        // strings can be in form oneplus 2, one plus 2, 1 + 2, one plus two etc
        // locale affects recognition of words like 1 million vs 10 lakhs
        HashMap<String, String[]> operator_map = new HashMap<>();
        operator_map.put(Calculator.OPEN_BRACKET, new String[]{ "open bracket" });
        operator_map.put(Calculator.CLOSE_BRACKET, new String[]{ "close bracket" });
        operator_map.put(" " + Calculator.PERCENTAGE + " " + Calculator.MULTIPLY, new String[]{ "% of" });
        operator_map.put(Calculator.FACTORIAL, new String[]{ "factorial" });
        operator_map.put(Calculator.SQUARE_ROOT, new String[]{ "square root of", "square root", "root" });
        operator_map.put(Calculator.SQUARE, new String[]{ "square", "squared" });
        operator_map.put(Calculator.POWER, new String[]{ "to the power", "to the power of" });
        operator_map.put(Calculator.DIVIDE, new String[]{ "/", "by", "divided by", "over" });
        operator_map.put(Calculator.MULTIPLY, new String[]{ "x", "multiplied by", "into", "times" });
        operator_map.put(Calculator.MODULUS, new String[]{ "modulus", "modulo", "mod" });
        operator_map.put(Calculator.SUBTRACT, new String[]{ "minus" });
        operator_map.put(Calculator.ADD, new String[]{ "plus" });

        ArrayList<String> operator_keywords = new ArrayList<>();
        ArrayList<String> operator_replacements = new ArrayList<>();
        for (String key : operator_map.keySet()) {
            String[] values = operator_map.get(key);
            for (String value : values) {
                operator_keywords.add(value);
                operator_replacements.add(key);
            }
        }

        mOperatorKeywords = operator_keywords.toArray(new String[operator_keywords.size()]);
        mOperatorReplacements = operator_replacements.toArray(new String[operator_replacements.size()]);

        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            mRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
            mRecognizer.setRecognitionListener(this);
        } else {
            mRecognizer = null;
        }
    }

    void Start() {
        if (mRecognizer == null) {
            mResultListener.Error(-1, "Speech Recognition Service Is Not Available on This Phone");
            return;
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(EXTRA_LANGUAGE_MODEL, LANGUAGE_MODEL_FREE_FORM);

        mRecognizer.startListening(intent);
    }

    void Stop() {
        if (mRecognizer != null) {
            mRecognizer.stopListening();
        }
    }

    void Cancel() {
        if (mRecognizer != null) {
            mRecognizer.cancel();
        }
    }

    void Destroy() {
        if (mRecognizer != null) {
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
        mResultListener.IsListening(VoiceResultListener.ListenState.PROCESSING);
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
                mResultListener.Error(error, "Failed to Recognise Voice while Performing Speech Recognition");
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                mResultListener.Error(error, "Speech Recognition Service Busy");
                break;
            case SpeechRecognizer.ERROR_SERVER:
                mResultListener.Error(error, "Speech Recognition Server Error");
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                mResultListener.Error(error, "Failed to Hear Voice while Performing Speech Recognition");
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
        process(result);
    }

    public void onRmsChanged(float rmsDb) {

    }

    // ---------------------------------------------

    private void process(ArrayList<String> inputList) {
        Log.v(TAG, "Parsing " + inputList);
        for (String input: inputList) {
            // replace recognized operator keywords
            String replaced_input =
                    StringUtils.replaceEach(input, mOperatorKeywords, mOperatorReplacements);
            // TODO negative numbers, conversion
            // try to break down to infix expression tokens
            String[] split_input = StringUtils.split(replaced_input);
            // drop all non numbers and non operators
            ArrayList<String> infix_expression = new ArrayList<>();
            for (String s : split_input) {
                if (mCalculator.IsOperator(s) || mCalculator.IsOperand(s, Calculator.Base.DEC)) {
                    infix_expression.add(s);
                }
            }
            if (infix_expression.isEmpty()) {
                // nothing to evaluate
                continue;
            }
            // use previous result if applicable
            if (mCalculator.IsOperator(infix_expression.get(0)) && !mLastResult.isEmpty()) {
                infix_expression.add(0, mLastResult);
            }

            try {
                String result = mCalculator.Evaluate(
                        infix_expression,
                        Calculator.Base.DEC,
                        Calculator.AngularUnit.DEGREE);

                mLastResult = result;
                String infix_string = StringUtils.join(infix_expression, "");
                mResultListener.Result(infix_string);
                mResultListener.Result(result);
                Log.v(TAG, "Parsed " + infix_string + " as " + result);

                return;
            } catch (Exception e) {
                // do nothing
            }
        }
        mResultListener.Error(-1, "Failed to Process Voice Input - " + inputList.get(0));
    }

    private void convert(ArrayList<String> inputList) {
        // process strings like convert 1 ml to litre

    }
}
