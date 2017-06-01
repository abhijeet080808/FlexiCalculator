package com.saraighatsoftware.flexicalculator;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

interface VoiceResultListener {
    enum ListenState {
        NOT_LISTENING,
        LISTENING,
        PROCESSING
    }
    void IsListening(ListenState state);
    void Result(String value);
    void Error(int code, String message);
}

public class VoiceFragment extends Fragment implements VoiceResultListener {

    private static final int PERMISSION_RECORD_AUDIO = 1;

    private Button mButtonDisplayVoice;
    private TextView mTextDisplayVoice;
    private ScrollView mScrollDisplayVoice;

    private VoiceCalculator mVoiceCalculator;

    private ListenState mListenState;

    public VoiceFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // restore view state automatically
        super.onCreateView(inflater, container, savedInstanceState);

        Context context = getContext();

        mVoiceCalculator = new VoiceCalculator(context, this);
        mListenState = ListenState.NOT_LISTENING;

        View root_view = inflater.inflate(R.layout.fragment_voice, container, false);

        mButtonDisplayVoice = (Button) root_view.findViewById(R.id.button_display_voice);
        mButtonDisplayVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListenState == ListenState.LISTENING) {
                    mVoiceCalculator.Stop();
                } else if (mListenState == ListenState.NOT_LISTENING){
                    mVoiceCalculator.Start();
                }
            }
        });
        mButtonDisplayVoice.setTypeface(FontCache.GetLight(context));

        mTextDisplayVoice = (TextView) root_view.findViewById(R.id.text_display_voice);
        mTextDisplayVoice.setTypeface(FontCache.GetLight(context));

        mScrollDisplayVoice = (ScrollView) root_view.findViewById(R.id.scroll_display_voice);

        return root_view;
    }

    public void IsListening(ListenState state) {
        mListenState = state;
        if (state == ListenState.NOT_LISTENING) {
            mButtonDisplayVoice.setText(getResources().getString(R.string.listen));
        } else if (state == ListenState.LISTENING) {
            mButtonDisplayVoice.setText(getResources().getString(R.string.listening));
        } else if (state == ListenState.PROCESSING){
            mButtonDisplayVoice.setText(getResources().getString(R.string.processing));
        }
    }

    public void Result(String value) {
        if (mTextDisplayVoice.getText().toString().isEmpty()) {
            mTextDisplayVoice.append(value);
        } else {
            mTextDisplayVoice.append("\n" + value);
        }

        // scroll after text is displayed
        mScrollDisplayVoice.post(new Runnable() {
            public void run() {
                mScrollDisplayVoice.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    public void Error(int code, String message) {
        View view = getView();
        if (view != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        }
        if (code == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS) {
            requestPermissions();
        }
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSION_RECORD_AUDIO);
        }
    }
}
