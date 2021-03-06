package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterAngle extends Converter {

    // must be same order and value as R.array.angle
    enum AngleUnit implements Unit {
        DEGREES(new String[] {
                "degrees",
                "degree" }),
        RADIANS(new String[] {
                "radians",
                "radian" }),
        GRADIANS(new String[] {
                "gradians",
                "gradian" });

        final String[] mKeywords;

        AngleUnit(String[] keywords) {
            mKeywords = keywords;
        }

        public String[] GetKeywords() {
            return mKeywords;
        }
    }

    private final List<String> mUnits;
    private final AngleUnit[] mValues;
    private final HashMap<ConversionPair, BigFraction> mConversionFactors;

    ConverterAngle(Context context) {
        if (context != null) {
            mUnits = Arrays.asList(context.getResources().getStringArray(R.array.angle));
        } else {
            mUnits = null;
        }
        mValues = AngleUnit.values();
        mConversionFactors = new HashMap<>();

        // 180 degree = 22/7 radian
        mConversionFactors.put(new ConversionPair(AngleUnit.DEGREES, AngleUnit.RADIANS),
                new BigFraction(22L, 1260L));
        // 90 degree = 100 gradian
        mConversionFactors.put(new ConversionPair(AngleUnit.DEGREES, AngleUnit.GRADIANS),
                new BigFraction(100L, 90L));
    }

    protected List<String> getUnits() {
        return mUnits;
    }

    protected Unit getUnitFromInteger(int position) {
        return mValues[position];
    }

    protected Unit getBaseUnit() {
        return AngleUnit.DEGREES;
    }

    protected BigFraction getConversionFactor(ConversionPair pair) {
        return mConversionFactors.get(pair);
    }

    Unit[] GetAllUnits() {
        return mValues;
    }
}
