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

public class VoiceFragment extends Fragment implements VoiceResultListener {

    private static final int PERMISSION_RECORD_AUDIO = 1;

    private static final String ARG_DISPLAY_TEXT = "display_text";
    private static final String ARG_DISPLAY_SCROLL_POS = "display_scroll_pos";

    private Button mButtonVoice;
    private TextView mTextDisplay;
    private ScrollView mScrollDisplay;

    private VoiceCalculator mVoiceCalculator;

    private ListenState mListenState;

    private StringBuffer mVoiceText;

    public VoiceFragment() {
        mVoiceText = new StringBuffer();
    }

    @Override
    public void onPause() {
        super.onPause();
        // stop ongoing listening if applicable
        mVoiceCalculator.Cancel();
        OnListenStateChange(ListenState.IDLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_DISPLAY_TEXT, mVoiceText.toString());
        outState.putIntArray(ARG_DISPLAY_SCROLL_POS,
                new int[]{ mScrollDisplay.getScrollX(), mScrollDisplay.getScrollY()});
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // restore view state automatically
        super.onCreateView(inflater, container, savedInstanceState);

        Context context = getContext();

        int[] scroll_position = null;

        if (savedInstanceState == null) {
            mVoiceText = new StringBuffer();
        } else {
            mVoiceText = new StringBuffer(savedInstanceState.getString(ARG_DISPLAY_TEXT));
            scroll_position = savedInstanceState.getIntArray(ARG_DISPLAY_SCROLL_POS);
        }

        mVoiceCalculator = new VoiceCalculator(context, this);
        mListenState = ListenState.IDLE;

        View root_view = inflater.inflate(R.layout.fragment_voice, container, false);

        mButtonVoice = (Button) root_view.findViewById(R.id.button_voice);
        mButtonVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListenState == ListenState.LISTENING) {
                    mVoiceCalculator.Stop();
                } else if (mListenState == ListenState.IDLE){
                    mVoiceCalculator.Start();
                }
            }
        });
        mButtonVoice.setTypeface(FontCache.GetLight(context));

        mTextDisplay = (TextView) root_view.findViewById(R.id.text_display_voice);
        mTextDisplay.setTypeface(FontCache.GetLight(context));

        mScrollDisplay = (ScrollView) root_view.findViewById(R.id.scroll_display_voice);

        updateText(scroll_position);

        return root_view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mVoiceCalculator.Destroy();
    }

    public void OnListenStateChange(final ListenState state) {
        mListenState = state;
        if (state == ListenState.IDLE) {
            mButtonVoice.setText(getResources().getString(R.string.listen));
        } else if (state == ListenState.LISTENING) {
            mButtonVoice.setText(getResources().getString(R.string.listening));
        } else if (state == ListenState.PROCESSING){
            mButtonVoice.setText(getResources().getString(R.string.processing));
        }
    }

    public void OnListenResult(final String value) {
        if (mVoiceText.length() > 0) {
            mVoiceText.append("\n");
        }
        mVoiceText.append(value);

        updateText(null);
    }

    public void OnListenError(final int code, final String message) {
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

    private void updateText(final int[] scrollPosition ) {
        mTextDisplay.setText(mVoiceText.toString());

        if (scrollPosition == null) {
            // scroll after text is displayed
            mScrollDisplay.post(new Runnable() {
                public void run() {
                    mScrollDisplay.fullScroll(View.FOCUS_DOWN);
                }
            });
        } else {
            mScrollDisplay.post(new Runnable() {
                public void run() {
                    mScrollDisplay.scrollTo(scrollPosition[0], scrollPosition[1]);
                }
            });
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
