package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterLength extends Converter {

    // must be same order and value as R.array.length
    enum LengthUnit implements Unit {
        NANOMETERS(new String[] {
                "nanometers",
                "nanometer",
                "nanometres",
                "nanometre" }),
        MICRONS(new String[] {
                "microns",
                "micron" }),
        MILLIMETERS(new String[] {
                "millimeters",
                "millimeter",
                "millimetres",
                "millimetre",
                "mm" }),
        CENTIMETERS(new String[] {
                "centimeters",
                "centimeter",
                "centimetres",
                "centimetre",
                "cm" }),
        METERS(new String[] {
                "meters",
                "meter",
                "metres",
                "metre" }),
        KILOMETERS(new String[] {
                "kilometers",
                "kilometer",
                "kilometres",
                "kilometre",
                "km" }),
        INCHES(new String[] {
                "inches",
                "inch" }),
        FEET(new String[] {
                "feet",
                "foot" }),
        YARDS(new String[] {
                "yards",
                "yard" }),
        MILES(new String[] {
                "miles",
                "mile" }),
        NAUTICAL_MILES(new String[] {
                "nautical miles",
                "nautical mile" });

        final String[] mKeywords;

        LengthUnit(String[] keywords) {
            mKeywords = keywords;
        }

        public String[] GetKeywords() {
            return mKeywords;
        }
    }

    private final List<String> mUnits;
    private final LengthUnit[] mValues;
    private final HashMap<ConversionPair, BigFraction> mConversionFactors;

    ConverterLength(Context context) {
        if (context != null) {
            mUnits = Arrays.asList(context.getResources().getStringArray(R.array.length));
        } else {
            mUnits = null;
        }
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

    protected List<String> getUnits() {
        return mUnits;
    }

    protected Unit getUnitFromInteger(int position) {
        return mValues[position];
    }

    protected Unit getBaseUnit() {
        return LengthUnit.NANOMETERS;
    }

    protected BigFraction getConversionFactor(ConversionPair pair) {
        return mConversionFactors.get(pair);
    }

    Unit[] GetAllUnits() {
        return mValues;
    }
}
