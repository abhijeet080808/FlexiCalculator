package com.saraighatsoftware.flexicalculator;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class CustomArrayAdapter extends ArrayAdapter<String> {

    private final Typeface mTypeface;

    CustomArrayAdapter(Context context, int resource) {
        super(context, resource);
        mTypeface = Typeface.createFromAsset(getContext().getAssets(),  "fonts/Teko-Regular.ttf");
    }

    CustomArrayAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Teko-Regular.ttf");
    }

    CustomArrayAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Teko-Regular.ttf");
    }

    CustomArrayAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);
        mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Teko-Regular.ttf");
    }

    CustomArrayAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Teko-Regular.ttf");
    }

    CustomArrayAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
        mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Teko-Regular.ttf");
    }

    // Affects default (closed) state of the spinner
    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(mTypeface);
        view.setTextSize(24); // sp
        return view;
    }

    // Affects opened state of the spinner
    @Override
    public @NonNull View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTypeface(mTypeface);
        view.setTextSize(24); // sp
        return view;
    }
}
