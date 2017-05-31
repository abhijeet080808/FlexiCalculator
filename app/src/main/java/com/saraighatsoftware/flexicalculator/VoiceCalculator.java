package com.saraighatsoftware.flexicalculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

import static android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL;
import static android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;
import static android.speech.SpeechRecognizer.RESULTS_RECOGNITION;

class VoiceCalculator implements RecognitionListener {

    // http://www.truiton.com/2014/06/android-speech-recognition-without-dialog-custom-activity/

    private static final String TAG = "VoiceCalculator";

    private SpeechRecognizer mRecognizer;
    private VoiceResultListener mResultListener;

    VoiceCalculator(Context context, VoiceResultListener resultListener) {
        mResultListener = resultListener;
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            mRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
            mRecognizer.setRecognitionListener(this);
        }
    }

    boolean Start() {
        if (mRecognizer == null) {
            return false;
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(EXTRA_LANGUAGE_MODEL, LANGUAGE_MODEL_FREE_FORM);

        mRecognizer.startListening(intent);

        return true;
    }

    void Stop() {
        if (mRecognizer != null) {
            mRecognizer.stopListening();
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
    }

    public void onError(int error) {
        // network or recognition error occurred
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                mResultListener.Result("Error Recording Audio");
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                mResultListener.Result("Client Error");
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                mResultListener.Result("Insufficient Permissions");
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                mResultListener.Result("Network Error");
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                mResultListener.Result("Network Timeout");
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                mResultListener.Result("Failed to Recognise Voice");
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                mResultListener.Result("Recognition Service Busy");
                break;
            case SpeechRecognizer.ERROR_SERVER:
                mResultListener.Result("Server Error");
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                mResultListener.Result("Failed to Recognise Voice");
                break;
            default:
                break;
        }
    }

    public void onEvent(int eventType, Bundle params) {
        // reserved for adding future events
    }

    public void onPartialResults(Bundle partialResults) {
        // called when partial recognition results are available
    }

    public void onReadyForSpeech(Bundle params) {
        // called when the endpoint is ready for the user to start speaking
    }

    public void onResults(Bundle results) {
        // called when recognition results are ready
        ArrayList<String> result = results.getStringArrayList(RESULTS_RECOGNITION);
        if (result == null) {
            return;
        }

        Log.v(TAG, result.toString());
        mResultListener.Result(result.toString());
    }

    public void onRmsChanged(float rmsdB) {

    }

    // ---------------------------------------------
}
