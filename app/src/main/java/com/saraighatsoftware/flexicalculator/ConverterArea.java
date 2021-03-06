package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterArea extends Converter {

    // must be same order and value as R.array.area
    enum AreaUnit implements Unit {
        SQUARE_MILLIMETERS(new String[]{
                "square millimeters",
                "square millimeter",
                "square millimetres",
                "square millimetre",
                "square mm" }),
        SQUARE_CENTIMETERS(new String[]{
                "square centimeters",
                "square centimeter",
                "square centimetres",
                "square centimetre",
                "square cm" }),
        SQUARE_METERS(new String[]{
                "square meters",
                "square meter",
                "square metres",
                "square metre" }),
        SQUARE_KILOMETERS(new String[]{
                "square kilometers",
                "square kilometer",
                "square kilometres",
                "square kilometre",
                "square km" }),
        ACRES(new String[]{
                "acres",
                "acre" }),
        HECTARES(new String[]{
                "hectares",
                "hectare" }),
        SQUARE_INCHES(new String[]{
                "square inches",
                "square inch" }),
        SQUARE_FEET(new String[]{
                "square feet",
                "square foot" }),
        SQUARE_YARDS(new String[]{
                "square yards",
                "square yard" }),
        SQUARE_MILES(new String[]{
                "square miles",
                "square mile" });

        final String[] mKeywords;

        AreaUnit(String[] keywords) {
            mKeywords = keywords;
        }

        public String[] GetKeywords() {
            return mKeywords;
        }
    }

    private final List<String> mUnits;
    private final AreaUnit[] mValues;
    private final HashMap<ConversionPair, BigFraction> mConversionFactors;

    ConverterArea(Context context) {
        if (context != null) {
            mUnits = Arrays.asList(context.getResources().getStringArray(R.array.area));
        } else {
            mUnits = null;
        }
        mValues = AreaUnit.values();
        mConversionFactors = new HashMap<>();

        // 10 mm = 1 cm
        mConversionFactors.put(new ConversionPair(AreaUnit.SQUARE_MILLIMETERS, AreaUnit.SQUARE_CENTIMETERS),
                new BigFraction(1L, 100L));
        // 100 cm = 1 m
        mConversionFactors.put(new ConversionPair(AreaUnit.SQUARE_MILLIMETERS, AreaUnit.SQUARE_METERS),
                new BigFraction(1L, 1000000L));
        // 1000 m = 1 km
        mConversionFactors.put(new ConversionPair(AreaUnit.SQUARE_MILLIMETERS, AreaUnit.SQUARE_KILOMETERS),
                new BigFraction(1L, 1000000000000L));
        // 4046.8564224 m^2 = 1 acre
        mConversionFactors.put(new ConversionPair(AreaUnit.SQUARE_MILLIMETERS, AreaUnit.ACRES),
                new BigFraction(10L, 40468564224L));
        // 10000 m^2 = 1 hectare
        mConversionFactors.put(new ConversionPair(AreaUnit.SQUARE_MILLIMETERS, AreaUnit.HECTARES),
                new BigFraction(1L, 10000000000L));
        // 127 mm = 5 inch
        mConversionFactors.put(new ConversionPair(AreaUnit.SQUARE_MILLIMETERS, AreaUnit.SQUARE_INCHES),
                new BigFraction(25L, 16129L));
        // 12 inch = 1 ft
        mConversionFactors.put(new ConversionPair(AreaUnit.SQUARE_MILLIMETERS, AreaUnit.SQUARE_FEET),
                new BigFraction(25L, 2322576L));
        // 3 ft = 1 yard
        mConversionFactors.put(new ConversionPair(AreaUnit.SQUARE_MILLIMETERS, AreaUnit.SQUARE_YARDS),
                new BigFraction(25L, 20903184L));
        // 1760 yard = 1 mile
        mConversionFactors.put(new ConversionPair(AreaUnit.SQUARE_MILLIMETERS, AreaUnit.SQUARE_MILES),
                new BigFraction(25L, 64749702758400L));
    }

    protected List<String> getUnits() {
        return mUnits;
    }

    protected Unit getUnitFromInteger(int position) {
        return mValues[position];
    }

    protected Unit getBaseUnit() {
        return AreaUnit.SQUARE_MILLIMETERS;
    }

    protected BigFraction getConversionFactor(ConversionPair pair) {
        return mConversionFactors.get(pair);
    }

    Unit[] GetAllUnits() {
        return mValues;
    }
}
