package com.saraighatsoftware.flexicalculator;

import android.content.Context;
import android.graphics.Typeface;

class FontCache {

    private static Typeface mLight;
    private static Typeface mRegular;
    private static Typeface mSemiBold;

    static Typeface GetLight(Context context) {
        if (mLight == null) {
            mLight = Typeface.createFromAsset(context.getAssets(),  "fonts/Teko-Light.ttf");
        }
        return mLight;
    }

    static Typeface GetRegular(Context context) {
        if (mRegular == null) {
            mRegular = Typeface.createFromAsset(context.getAssets(),  "fonts/Teko-Regular.ttf");
        }
        return mRegular;
    }

    static Typeface GetSemiBold(Context context) {
        if (mSemiBold == null) {
            mSemiBold = Typeface.createFromAsset(context.getAssets(),  "fonts/Teko-SemiBold.ttf");
        }
        return mSemiBold;
    }
}
