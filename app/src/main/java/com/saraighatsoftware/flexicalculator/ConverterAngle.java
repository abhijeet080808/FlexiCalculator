package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterAngle extends Converter {

    // must be same order and value as R.array.angle
    private enum AngleUnit implements Unit {
        DEGREES,
        RADIANS,
        GRADIANS
    }

    private List<String> mUnits;
    private AngleUnit[] mValues;
    private HashMap<ConversionPair, BigFraction> mConversionFactors;

    ConverterAngle(Context context) {
        mUnits = Arrays.asList(context.getResources().getStringArray(R.array.angle));
        mValues = AngleUnit.values();
        mConversionFactors = new HashMap<>();

        // 180 degree = 22/7 radian
        mConversionFactors.put(new ConversionPair(AngleUnit.DEGREES, AngleUnit.RADIANS),
                new BigFraction(22L, 1260L));
        // 90 degree = 100 gradian
        mConversionFactors.put(new ConversionPair(AngleUnit.DEGREES, AngleUnit.GRADIANS),
                new BigFraction(100L, 90L));
    }

    List<String> GetUnits() {
        return mUnits;
    }

    Unit GetUnitFromInteger(int position) {
        return mValues[position];
    }

    Unit GetBaseUnit() {
        return AngleUnit.DEGREES;
    }

    BigFraction GetConversionFactor(ConversionPair pair) {
        return mConversionFactors.get(pair);
    }
}
