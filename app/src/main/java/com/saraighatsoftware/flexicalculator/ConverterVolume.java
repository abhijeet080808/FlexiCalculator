package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.fraction.BigFraction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterVolume {

    private enum VolumeUnit {
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

    private class ConversionPair {

        final VolumeUnit input;
        final VolumeUnit output;

        ConversionPair(VolumeUnit input, VolumeUnit output) {
            this.input = input;
            this.output = output;
        }

        @Override
        public int hashCode() {
            // From Effective Java
            int hash = 17;
            hash = hash * 31 + input.hashCode();
            hash = hash * 31 + output.hashCode();
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ConversionPair))
                return false;
            if (obj == this)
                return true;

            ConversionPair rhs = (ConversionPair) obj;
            return (this.input == rhs.input && this.output == rhs.output);
        }
    }

    private String[] mUnits;
    // output = input * conversion_factor
    private HashMap<ConversionPair, BigFraction> mConversionFactors;

    ConverterVolume(Context context) {
        mUnits = context.getResources().getStringArray(R.array.volume);

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

    List<String> GetUnits() {
        return Arrays.asList(mUnits);
    }

    private static BigFraction ToBigFraction(BigDecimal val) {
        final int scale = val.scale();
        // If scale >= 0 then the value is val.unscaledValue() / 10^scale
        if(scale >= 0)
            return new BigFraction(val.unscaledValue(), BigInteger.TEN.pow(scale));
        // If scale < 0 then the value is val.unscaledValue() * 10^-scale
        return new BigFraction(val.unscaledValue().multiply(BigInteger.TEN.pow(-scale)));
    }

    String Convert(String value, VolumeUnit input, VolumeUnit output) {
        try {
            // convert to milliliters first
            BigFraction in = ToBigFraction(new BigDecimal(value));
            if (input != VolumeUnit.MILLILITERS) {
                in = in.divide(mConversionFactors.get(
                        new ConversionPair(VolumeUnit.MILLILITERS, input)));
            }
            // convert to output format
            BigFraction out = in;
            if (output != VolumeUnit.MILLILITERS) {
                out = out.multiply(mConversionFactors.get(
                        new ConversionPair(VolumeUnit.MILLILITERS, output)));
            }

            return ResultFormat.Format(out.bigDecimalValue(BigDecimal.ROUND_HALF_EVEN));
        }
        catch (Exception e) {
            return "0";
        }
    }
}
