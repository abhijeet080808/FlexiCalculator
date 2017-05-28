package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterPressure extends Converter {

    // must be same order and value as R.array.pressure
    private enum PressureUnit implements Unit {
        ATMOSPHERES,
        BARS,
        PASCALS,
        KILOPASCALS,
        POUNDS_PER_SQUARE_INCH,
        TORRS
    }

    private List<String> mUnits;
    private PressureUnit[] mValues;
    private HashMap<ConversionPair, BigFraction> mConversionFactors;

    ConverterPressure(Context context) {
        mUnits = Arrays.asList(context.getResources().getStringArray(R.array.pressure));
        mValues = PressureUnit.values();
        mConversionFactors = new HashMap<>();

        // 1 atm = 1.01325 bar
        mConversionFactors.put(new ConversionPair(PressureUnit.ATMOSPHERES, PressureUnit.BARS),
                new BigFraction(101325L, 100000L));
        // 1 bar = 100000 pa
        mConversionFactors.put(new ConversionPair(PressureUnit.ATMOSPHERES, PressureUnit.PASCALS),
                new BigFraction(101325L, 1L));
        // 1000 pa = 1 kpa
        mConversionFactors.put(new ConversionPair(PressureUnit.ATMOSPHERES, PressureUnit.KILOPASCALS),
                new BigFraction(101325L, 1000L));
        // 1 pound force = 1 lb * g = 45359237/100000000 * 9.80665 N
        // 1 sq inch = 16129/25000000 sq m
        // 1 psi = 45359237/100000000 * 9.80665 * 25000000 / 16129 N/m^2
        //       = 11120554038151250 / 1612900000000 Pa
        //       = 8896443230521 / 1290320000 Pa
        mConversionFactors.put(new ConversionPair(PressureUnit.ATMOSPHERES, PressureUnit.POUNDS_PER_SQUARE_INCH),
                new BigFraction(130741674000000L, 8896443230521L));
        // 101325 pa = 760 torr
        mConversionFactors.put(new ConversionPair(PressureUnit.ATMOSPHERES, PressureUnit.TORRS),
                new BigFraction(760L, 1L));
    }

    List<String> GetUnits() {
        return mUnits;
    }

    Unit GetUnitFromInteger(int position) {
        return mValues[position];
    }

    Unit GetBaseUnit() {
        return PressureUnit.ATMOSPHERES;
    }

    BigFraction GetConversionFactor(ConversionPair pair) {
        return mConversionFactors.get(pair);
    }
}
