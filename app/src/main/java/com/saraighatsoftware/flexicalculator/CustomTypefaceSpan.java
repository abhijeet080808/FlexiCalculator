package com.saraighatsoftware.flexicalculator;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

@SuppressLint("ParcelCreator")
class CustomTypefaceSpan extends TypefaceSpan {

    private final Typeface mTypeface;

    CustomTypefaceSpan(Typeface type) {
        super("");
        mTypeface = type;
    }

    @Override
    public void updateDrawState(TextPaint paint) {
        applyCustomTypeFace(paint, mTypeface);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, mTypeface);
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf) {
        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        int fake = oldStyle & ~tf.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(tf);
    }
}