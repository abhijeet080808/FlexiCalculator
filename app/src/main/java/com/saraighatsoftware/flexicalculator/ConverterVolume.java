package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterVolume extends Converter {

    // must be same order and value as R.array.volume
    enum VolumeUnit implements Converter.Unit {
        MILLILITERS(new String[] {
                "milliliters",
                "milliliter",
                "millilitres",
                "millilitre",
                "ml"}),
        LITERS(new String[] {
                "liters",
                "liter",
                "litres",
                "litre" }),
        CUBIC_CENTIMETERS(new String[] {
                "cubic centimeters",
                "cubic centimeter",
                "cubic centimetres",
                "cubic centimetre",
                "cc"}),
        CUBIC_METERS(new String[] {
                "cubic meters",
                "cubic meter",
                "cubic metres",
                "cubic metre" }),
        CUBIC_INCHES(new String[] {
                "cubic inches",
                "cubic inch",
                "ci" }),
        CUBIC_FEET(new String[] {
                "cubic feet",
                "cubic foot"}),
        CUBIC_YARDS(new String[] {
                "cubic yards",
                "cubic yard" }),
        TEASPOONS_US(new String[] {
                "us teaspoons",
                "us teaspoon",
                "teaspoons us",
                "teaspoon us",
                "teaspoons",
                "teaspoon" }),
        TABLESPOONS_US(new String[] {
                "us tablespoons us",
                "us tablespoon",
                "tablespoons us",
                "tablespoon us",
                "tablespoons",
                "tablespoon" }),
        FLUID_OUNCES_US(new String[] {
                "us fluid ounces",
                "us fluid ounce",
                "fluid ounces us",
                "fluid ounce us",
                "fluid ounces",
                "fluid ounce",
                "fl oz" }),
        CUPS_US(new String[] {
                "us cups",
                "us cup",
                "cups us",
                "cup us",
                "cups",
                "cup" }),
        PINTS_US(new String[] {
                "us pints",
                "us pint",
                "pints us",
                "pint us",
                "pints",
                "pint" }),
        QUARTS_US(new String[] {
                "us quarts",
                "us quart",
                "quarts us",
                "quart us",
                "quarts",
                "quart" }),
        GALLONS_US(new String[] {
                "us gallons",
                "us gallon",
                "gallons us",
                "gallon us",
                "gallons",
                "gallon" }),
        TEASPOONS_UK(new String[] {
                "uk teaspoons",
                "uk teaspoon",
                "imperial teaspoons",
                "imperial teaspoon",
                "british teaspoons",
                "british teaspoon",
                "teaspoons uk",
                "teaspoon uk" }),
        TABLESPOONS_UK(new String[] {
                "uk tablespoons",
                "uk tablespoon",
                "imperial tablespoons",
                "imperial tablespoon",
                "british tablespoons",
                "british tablespoon",
                "tablespoons uk",
                "tablespoon uk" }),
        FLUID_OUNCES_UK(new String[] {
                "uk fluid ounces",
                "uk fluid ounce",
                "imperial fluid ounces",
                "imperial fluid ounce",
                "british fluid ounces",
                "british fluid ounce",
                "fluid ounces uk",
                "fluid ounce uk" }),
        CUPS_UK(new String[] {
                "uk cups",
                "uk cup",
                "imperial cups",
                "imperial cup",
                "british cups",
                "british cup",
                "cups uk",
                "cup uk" }),
        PINTS_UK(new String[] {
                "uk pints",
                "uk pint",
                "imperial pints",
                "imperial pint",
                "british pints",
                "british pint",
                "pints uk",
                "pint uk" }),
        QUARTS_UK(new String[] {
                "uk quarts",
                "uk quart",
                "imperial quarts",
                "imperial quart",
                "british quarts",
                "british quart",
                "quarts uk",
                "quart uk" }),
        GALLONS_UK(new String[] {
                "uk gallons",
                "uk gallon",
                "imperial gallons",
                "imperial gallon",
                "british gallons",
                "british gallon",
                "gallons uk",
                "gallon uk" });

        final String[] mKeywords;

        VolumeUnit(String[] keywords) {
            mKeywords = keywords;
        }

        public String[] GetKeywords() {
            return mKeywords;
        }
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

    Unit[] GetAllUnits() {
        return mValues;
    }
}
