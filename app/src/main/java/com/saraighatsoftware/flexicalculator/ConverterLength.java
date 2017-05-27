package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterLength extends Converter {

    // must be same order and value as R.array.length
    private enum LengthUnit implements Unit {
        NANOMETERS,
        MICRONS,
        MILLIMETERS,
        CENTIMETERS,
        METERS,
        KILOMETERS,
        INCHES,
        FEET,
        YARDS,
        MILES,
        NAUTICAL_MILES,
    }

    private List<String> mUnits;
    private LengthUnit[] mValues;
    private HashMap<ConversionPair, BigFraction> mConversionFactors;

    ConverterLength(Context context) {
        mUnits = Arrays.asList(context.getResources().getStringArray(R.array.length));
        mValues = LengthUnit.values();
        mConversionFactors = new HashMap<>();

        // 1000 nm = 1 micron
        mConversionFactors.put(new ConversionPair(LengthUnit.NANOMETERS, LengthUnit.MICRONS),
                new BigFraction(1L, 1000L));
        // 1000 microns = 1 mm
        mConversionFactors.put(new ConversionPair(LengthUnit.NANOMETERS, LengthUnit.MILLIMETERS),
                new BigFraction(1L, 1000000L));
        // 10 mm = 1 cm
        mConversionFactors.put(new ConversionPair(LengthUnit.NANOMETERS, LengthUnit.CENTIMETERS),
                new BigFraction(1L, 10000000L));
        // 100 cm = 1 m
        mConversionFactors.put(new ConversionPair(LengthUnit.NANOMETERS, LengthUnit.METERS),
                new BigFraction(1L, 1000000000L));
        // 1000 m = 1 km
        mConversionFactors.put(new ConversionPair(LengthUnit.NANOMETERS, LengthUnit.KILOMETERS),
                new BigFraction(1L, 1000000000000L));
        // 127000000 nm = 5 inch
        mConversionFactors.put(new ConversionPair(LengthUnit.NANOMETERS, LengthUnit.INCHES),
                new BigFraction(5L, 127000000L));
        // 12 inch = 1 ft
        mConversionFactors.put(new ConversionPair(LengthUnit.NANOMETERS, LengthUnit.FEET),
                new BigFraction(5L, 1524000000L));
        // 3 ft = 1 yard
        mConversionFactors.put(new ConversionPair(LengthUnit.NANOMETERS, LengthUnit.YARDS),
                new BigFraction(5L, 4572000000L));
        // 1760 yard = 1 mile
        mConversionFactors.put(new ConversionPair(LengthUnit.NANOMETERS, LengthUnit.MILES),
                new BigFraction(5L, 8046720000000L));
        // 1852 m = 1 nautical mile
        mConversionFactors.put(new ConversionPair(LengthUnit.NANOMETERS, LengthUnit.NAUTICAL_MILES),
                new BigFraction(1L, 1852000000000L));
    }

    List<String> GetUnits() {
        return mUnits;
    }

    Unit GetUnitFromInteger(int position) {
        return mValues[position];
    }

    Unit GetBaseUnit() {
        return LengthUnit.NANOMETERS;
    }

    BigFraction GetConversionFactor(ConversionPair pair) {
        return mConversionFactors.get(pair);
    }
}
