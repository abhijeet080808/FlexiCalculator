package com.saraighatsoftware.flexicalculator;

interface VoiceResultListener {

    enum ListenState {
        IDLE,
        LISTENING,
        PROCESSING
    }

    void IsListening(ListenState state);
    void Result(String value);
    void Error(int code, String message);
}