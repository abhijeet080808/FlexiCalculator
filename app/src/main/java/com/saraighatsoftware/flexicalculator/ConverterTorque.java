package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterTorque extends Converter {

    // must be same order and value as R.array.torque
    enum TorqueUnit implements Unit {
        NEWTON_METERS(new String[] {
                "newton meters",
                "newton meter",
                "newton metres",
                "newton metre",
                "nm" }),
        KILOGRAM_METERS(new String[] {
                "kilogram meters",
                "kilogram meter",
                "kilogram metres",
                "kilogram metre",
                "kgm" }),
        JOULES_PER_RADIAN(new String[] {
                "joules per radian",
                "joule per radian" }),
        POUND_INCHES(new String[] {
                "pound inches",
                "pound inch" }),
        POUND_FEET(new String[] {
                "pound feet",
                "pound foot" });

        final String[] mKeywords;

        TorqueUnit(String[] keywords) {
            mKeywords = keywords;
        }

        public String[] GetKeywords() {
            return mKeywords;
        }
    }

    private final List<String> mUnits;
    private final TorqueUnit[] mValues;
    private final HashMap<ConversionPair, BigFraction> mConversionFactors;

    ConverterTorque(Context context) {
        if (context != null) {
            mUnits = Arrays.asList(context.getResources().getStringArray(R.array.torque));
        } else {
            mUnits = null;
        }
        mValues = TorqueUnit.values();
        mConversionFactors = new HashMap<>();

        // 1 Nm = 1 / 9.80665 kg-m
        mConversionFactors.put(new ConversionPair(TorqueUnit.NEWTON_METERS, TorqueUnit.KILOGRAM_METERS),
                new BigFraction(100000L, 980665L));
        // 1 Nm kW = 1 J/rad
        mConversionFactors.put(new ConversionPair(TorqueUnit.NEWTON_METERS, TorqueUnit.JOULES_PER_RADIAN),
                new BigFraction(1L, 1L));
        // https://www.mathpapa.com/algebra-calculator.html
        // 45359237 kg = 100000000 lb
        // 127 m = 5000 in
        // 1 kg-m = 100000000 / 45359237 * 5000 / 127 lb-in
        //        = 500000000000 / 5760623099 lb-in
        // 1 Nm = 100000 / 980665 * 500000000000 / 5760623099 lb-in
        //      = 10000000000000000 / 1129848290276167 lb-in
        mConversionFactors.put(new ConversionPair(TorqueUnit.NEWTON_METERS, TorqueUnit.POUND_INCHES),
                new BigFraction(10000000000000000L, 1129848290276167L));
        // https://www.mathpapa.com/algebra-calculator.html
        // 45359237 kg = 100000000 lb
        // 1524 m = 5000 ft
        // 1 kg-m = 100000000 / 45359237 * 5000 / 1524 lb-ft
        //        = 125000000000 / 17281869297 lb-ft
        // 1 Nm = 100000 / 980665 * 125000000000 / 17281869297 lb-ft
        //      = 2500000000000000 / 3389544870828501 lb-ft
        mConversionFactors.put(new ConversionPair(TorqueUnit.NEWTON_METERS, TorqueUnit.POUND_FEET),
                new BigFraction(2500000000000000L, 3389544870828501L));
    }

    protected List<String> getUnits() {
        return mUnits;
    }

    protected Unit getUnitFromInteger(int position) {
        return mValues[position];
    }

    protected Unit getBaseUnit() {
        return TorqueUnit.NEWTON_METERS;
    }

    protected BigFraction getConversionFactor(ConversionPair pair) {
        return mConversionFactors.get(pair);
    }

    Unit[] GetAllUnits() {
        return mValues;
    }
}
