package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterVolume extends Converter {

    // must be same order and value as R.array.volume
    enum VolumeUnit implements Converter.Unit {
        MILLILITERS,
        LITERS,
        CUBIC_CENTIMETERS,
        CUBIC_METERS,
        CUBIC_INCHES,
        CUBIC_FEET,
        CUBIC_YARDS,
        TEASPOONS_US,
        TABLESPOONS_US,
        FLUID_OUNCES_US,
        CUPS_US,
        PINTS_US,
        QUARTS_US,
        GALLONS_US,
        TEASPOONS_UK,
        TABLESPOONS_UK,
        FLUID_OUNCES_UK,
        CUPS_UK,
        PINTS_UK,
        QUARTS_UK,
        GALLONS_UK
    }

    private final List<String> mUnits;
    private final VolumeUnit[] mValues;
    private final HashMap<ConversionPair, BigFraction> mConversionFactors;

    ConverterVolume(Context context) {
        if (context != null) {
            mUnits = Arrays.asList(context.getResources().getStringArray(R.array.volume));
        } else {
            mUnits = null;
        }
        mValues = VolumeUnit.values();
        mConversionFactors = new HashMap<>();

        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.LITERS),
                new BigFraction(1L, 1000L));
        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.CUBIC_CENTIMETERS),
                new BigFraction(1L, 1L));
        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.CUBIC_METERS),
                new BigFraction(1L, 1000000L));
        // 127 cm = 50 inches
        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.CUBIC_INCHES),
                new BigFraction(125000L, 2048383L));
        // 762 cm = 25 feet
        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.CUBIC_FEET),
                new BigFraction(15625L, 442450728L));
        // 2286 cm = 25 yards
        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.CUBIC_YARDS),
                new BigFraction(15625L, 11946169656L));
        // 77 ci3 = 256 us teaspoon
        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.TEASPOONS_US),
                new BigFraction(32000000L, 157725491L));
        // 3 us teaspoon = 1 us tablespoon
        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.TABLESPOONS_US),
                new BigFraction(32000000L, 473176473L));
        // 2 us tablespoon = 1 us fluid ounce
        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.FLUID_OUNCES_US),
                new BigFraction(32000000L, 946352946L));
        // 8 us fluid ounce = 1 us cup
        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.CUPS_US),
                new BigFraction(32000000L, 7570823568L));
        // 2 us cup = 1 us pint
        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.PINTS_US),
                new BigFraction(32000000L, 15141647136L));
        // 2 us pint = 1 us quart
        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.QUARTS_US),
                new BigFraction(32000000L, 30283294272L));
        // 4 us quart = 1 us gallon
        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.GALLONS_US),
                new BigFraction(32000000L, 121133177088L));
        // 28.4130625 ml = 1 uk fluid ounce
        // 5 uk fluid ounce = 24 uk teaspoon
        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.TEASPOONS_UK),
                new BigFraction(240000000L, 1420653125L));
        // 3 uk teaspoon = 1 uk tablespoon
        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.TABLESPOONS_UK),
                new BigFraction(240000000L, 4261959375L));
        // 28.4130625 ml = 1 uk fluid ounce
        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.FLUID_OUNCES_UK),
                new BigFraction(10000000L, 284130625L));
        // 10 uk fluid ounce - 1 uk cup
        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.CUPS_UK),
                new BigFraction(10000000L, 2841306250L));
        // 2 uk cup - 1 uk pint
        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.PINTS_UK),
                new BigFraction(10000000L, 5682612500L));
        // 2 uk pint = 1 uk quart
        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.QUARTS_UK),
                new BigFraction(10000000L, 11365225000L));
        // 4 uk quart = 1 uk gallon
        mConversionFactors.put(new ConversionPair(VolumeUnit.MILLILITERS, VolumeUnit.GALLONS_UK),
                new BigFraction(10000000L, 45460900000L));
    }

    protected List<String> getUnits() {
        return mUnits;
    }

    protected Unit getUnitFromInteger(int position) {
        return mValues[position];
    }

    protected Unit getBaseUnit() {
        return VolumeUnit.MILLILITERS;
    }

    protected BigFraction getConversionFactor(ConversionPair pair) {
        return mConversionFactors.get(pair);
    }
}
