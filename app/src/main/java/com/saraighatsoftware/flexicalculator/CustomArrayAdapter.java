package com.saraighatsoftware.flexicalculator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class CustomArrayAdapter extends ArrayAdapter<String> {

    private Context mContext;

    @SuppressWarnings("unused")
    CustomArrayAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
    }

    @SuppressWarnings("unused")
    CustomArrayAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        mContext = context;
    }

    @SuppressWarnings("unused")
    CustomArrayAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        mContext = context;
    }

    @SuppressWarnings("unused")
    CustomArrayAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);
        mContext = context;
    }

    CustomArrayAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        mContext = context;
    }

    @SuppressWarnings("unused")
    CustomArrayAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
        mContext = context;
    }

    // Affects default (closed) state of the spinner
    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(FontCache.GetRegular(mContext));
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mContext.getResources().getDimension(R.dimen.spinner_font_size));
        return view;
    }

    // Affects opened state of the spinner
    @Override
    public @NonNull View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTypeface(FontCache.GetRegular(mContext));
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mContext.getResources().getDimension(R.dimen.spinner_font_size));
        return view;
    }
}
