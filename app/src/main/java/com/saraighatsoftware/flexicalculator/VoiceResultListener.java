package com.saraighatsoftware.flexicalculator;

interface VoiceResultListener {

    enum ListenState {
        IDLE,
        LISTENING,
        PROCESSING
    }

    void OnListenStateChange(ListenState state);
    void OnListenResult(String value);
    void OnListenError(int code, String message);
}