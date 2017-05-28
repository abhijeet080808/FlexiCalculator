package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterTorque extends Converter {

    // must be same order and value as R.array.torque
    private enum TorqueUnit implements Unit {
        NEWTON_METERS,
        KILOGRAM_METERS,
        JOULES_PER_RADIAN,
        POUND_INCHES,
        POUND_FEET
    }

    private List<String> mUnits;
    private TorqueUnit[] mValues;
    private HashMap<ConversionPair, BigFraction> mConversionFactors;

    ConverterTorque(Context context) {
        mUnits = Arrays.asList(context.getResources().getStringArray(R.array.torque));
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
}
