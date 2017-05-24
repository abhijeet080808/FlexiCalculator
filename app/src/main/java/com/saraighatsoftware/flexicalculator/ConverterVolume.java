package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

class ConverterVolume {

    private String[] mUnits;

    ConverterVolume(Context context) {
        mUnits = context.getResources().getStringArray(R.array.volume);
    }

    List<String> GetUnits() {
        return Arrays.asList(mUnits);
    }
}
