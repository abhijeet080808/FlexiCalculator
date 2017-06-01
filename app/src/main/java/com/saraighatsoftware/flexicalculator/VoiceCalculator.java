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
import java.util.Arrays;

import static android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL;
import static android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;
import static android.speech.SpeechRecognizer.RESULTS_RECOGNITION;

class VoiceCalculator implements RecognitionListener {

    // http://www.truiton.com/2014/06/android-speech-recognition-without-dialog-custom-activity/

    private static final String TAG = "VoiceCalculator";

    private final SpeechRecognizer mRecognizer;
    private final VoiceResultListener mResultListener;

    private final Calculator mCalculator;

    VoiceCalculator(Context context, VoiceResultListener resultListener) {
        mResultListener = resultListener;
        mCalculator = new Calculator();

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
        mResultListener.IsListening(VoiceResultListener.ListenState.NOT_LISTENING);
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
        mResultListener.IsListening(VoiceResultListener.ListenState.NOT_LISTENING);
        ArrayList<String> result = results.getStringArrayList(RESULTS_RECOGNITION);
        if (result == null) {
            mResultListener.Error(-1, "Failed to Parse Voice Input");
            return;
        }
        process(result);
    }

    public void onRmsChanged(float rmsDb) {

    }

    // ---------------------------------------------

    private void process(ArrayList<String> inputList) {
        // TODO use last result in parsing, add more phrases, negative numbers
        for (String input: inputList) {
            // http://par.cse.nsysu.edu.tw/link/math-pronunciation.pdf
            // strings can be in form oneplus 2, one plus 2, 1 + 2, one plus two etc
            // locale affects recognition of words like 1 million vs 10 lakhs
            String values[] = new String[] {
                    "plus",
                    "minus",
                    "multiply", "multiplied by", "into", "times",
                    "/", "by", "divided by", "over",
                    "open bracket",
                    "close bracket",
                    "% of",
                    "factorial",
                    "square",
                    "to the power", "to the power of",
                    "modulus", "modulo", "mod"
            };
            String replacements[] = new String[] {
                    Calculator.ADD,
                    Calculator.SUBTRACT,
                    Calculator.MULTIPLY, Calculator.MULTIPLY, Calculator.MULTIPLY, Calculator.MULTIPLY,
                    Calculator.DIVIDE, Calculator.DIVIDE, Calculator.DIVIDE, Calculator.DIVIDE,
                    Calculator.OPEN_BRACKET,
                    Calculator.CLOSE_BRACKET,
                    " " + Calculator.PERCENTAGE + " " + Calculator.MULTIPLY,
                    Calculator.FACTORIAL,
                    Calculator.SQUARE,
                    Calculator.POWER, Calculator.POWER,
                    Calculator.MODULUS, Calculator.MODULUS, Calculator.MODULUS
            };
            String processed_input = StringUtils.replaceEach(input, values, replacements);
            // try to break down to infix expression tokens
            // TODO drop all non numbers and non operators
            ArrayList<String> infix_expression = new ArrayList<>(Arrays.asList(StringUtils.split(processed_input)));
            try {
                String result = mCalculator.Evaluate(infix_expression, Calculator.Base.DEC, Calculator.AngularUnit.DEGREE);
                mResultListener.Result(StringUtils.deleteWhitespace(processed_input));
                mResultListener.Result(result);
                return;
            } catch (Exception e) {
                Log.v(TAG, "Failed to parse " + input);
                // do nothing
            }
        }
        mResultListener.Error(-1, "Failed to Process Voice Input - " + inputList.get(0));
    }
}
