package com.saraighatsoftware.flexicalculator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

interface VoiceResultListener {
    void Result(String s);
}

public class VoiceFragment extends Fragment implements VoiceResultListener {

    private TextView mTextResult;

    private VoiceCalculator mVoiceCalculator;

    public VoiceFragment(){
        mVoiceCalculator = new VoiceCalculator(getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // restore view state automatically
        super.onCreateView(inflater, container, savedInstanceState);

        Context context = getContext();

        View root_view = inflater.inflate(R.layout.fragment_voice, container, false);

        mTextResult = (TextView) root_view.findViewById(R.id.text_voice_result);
        mTextResult.setTypeface(FontCache.GetLight(context));

        Button button;

        button = (Button) root_view.findViewById(R.id.button_start_voice);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVoiceCalculator.Start();
            }
        });
        button.setTypeface(FontCache.GetSemiBold(context));

        return root_view;
    }

    public void Result(String s) {
        mTextResult.append("\n" + s);
    }
}
