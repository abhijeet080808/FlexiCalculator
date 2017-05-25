package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

class ConverterVolume {

    private static final int INTERNAL_SCALE = 12;

    private enum Type {
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
        PINTS_UK,
        QUARTS_UK,
        GALLONS_UK
    }

    private class ConversionPair {

        final Type input;
        final Type output;

        ConversionPair(Type input, Type output) {
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
    private HashMap<ConversionPair, BigDecimal> mConversionFactors;

    ConverterVolume(Context context) {
        mUnits = context.getResources().getStringArray(R.array.volume);
        // http://www.metric-conversions.org/volume/milliliters-conversion.htm
        mConversionFactors.put(new ConversionPair(Type.MILLILITERS, Type.LITERS), new BigDecimal("0.001"));
        mConversionFactors.put(new ConversionPair(Type.MILLILITERS, Type.CUBIC_CENTIMETERS), new BigDecimal("1"));
        mConversionFactors.put(new ConversionPair(Type.MILLILITERS, Type.CUBIC_METERS), new BigDecimal("0.000001"));
        mConversionFactors.put(new ConversionPair(Type.MILLILITERS, Type.CUBIC_INCHES), new BigDecimal("0.061023744"));
        mConversionFactors.put(new ConversionPair(Type.MILLILITERS, Type.CUBIC_FEET), new BigDecimal("0.000035314667"));
        mConversionFactors.put(new ConversionPair(Type.MILLILITERS, Type.CUBIC_YARDS), new BigDecimal("0.0000013079506"));
        mConversionFactors.put(new ConversionPair(Type.MILLILITERS, Type.TEASPOONS_US), new BigDecimal("0.20288414"));
        mConversionFactors.put(new ConversionPair(Type.MILLILITERS, Type.TABLESPOONS_US), new BigDecimal("0.067628045"));
        mConversionFactors.put(new ConversionPair(Type.MILLILITERS, Type.FLUID_OUNCES_US), new BigDecimal("0.033814023"));
        mConversionFactors.put(new ConversionPair(Type.MILLILITERS, Type.CUPS_US), new BigDecimal("0.0042267528"));
        mConversionFactors.put(new ConversionPair(Type.MILLILITERS, Type.PINTS_US), new BigDecimal("0.0021133764"));
        mConversionFactors.put(new ConversionPair(Type.MILLILITERS, Type.QUARTS_US), new BigDecimal("0.0010566882"));
        mConversionFactors.put(new ConversionPair(Type.MILLILITERS, Type.GALLONS_US), new BigDecimal("0.00026417205"));
        mConversionFactors.put(new ConversionPair(Type.MILLILITERS, Type.TEASPOONS_UK), new BigDecimal("0.28156000"));
        mConversionFactors.put(new ConversionPair(Type.MILLILITERS, Type.TABLESPOONS_UK), new BigDecimal("0.070390100"));
        mConversionFactors.put(new ConversionPair(Type.MILLILITERS, Type.FLUID_OUNCES_UK), new BigDecimal("0.035195080"));
        mConversionFactors.put(new ConversionPair(Type.MILLILITERS, Type.PINTS_UK), new BigDecimal("0.0017597540"));
        mConversionFactors.put(new ConversionPair(Type.MILLILITERS, Type.QUARTS_UK), new BigDecimal("0.00087987699"));
        mConversionFactors.put(new ConversionPair(Type.MILLILITERS, Type.GALLONS_UK), new BigDecimal("0.00021996925"));
    }

    List<String> GetUnits() {
        return Arrays.asList(mUnits);
    }

    String Convert(String input, Type inputType, Type outputType) {
        try {
            // convert to milliliters first
            BigDecimal in = new BigDecimal(input);
            if (inputType != Type.MILLILITERS) {
                in = in.divide(
                        mConversionFactors.get(new ConversionPair(Type.MILLILITERS, inputType)),
                        INTERNAL_SCALE,
                        BigDecimal.ROUND_HALF_EVEN);
            }
            // convert to output format
            BigDecimal out = in;
            if (outputType != Type.MILLILITERS) {
                out = out.multiply(
                        mConversionFactors.get(new ConversionPair(Type.MILLILITERS, outputType)));
            }

            return ResultFormat.Format(out);
        }
        catch (Exception e) {
            return "0";
        }
    }
}
