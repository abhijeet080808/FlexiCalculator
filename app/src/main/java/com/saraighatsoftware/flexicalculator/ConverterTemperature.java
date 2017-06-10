package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.fraction.BigFraction;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

class ConverterTemperature extends Converter {

    private static final String TAG = "ConverterTemperature";

    private static final BigFraction FIVE_BY_NINE = new BigFraction(5, 9);
    private static final BigFraction KELVIN_OFFSET = new BigFraction(27315, 100);

    // must be same order and value as R.array.temperature
    enum TemperatureUnit implements Unit {
        CELSIUS(new String[] {
                "degrees centigrade",
                "degree centigrade",
                "centigrade",
                "degrees celsius",
                "degree celsius",
                "celsius" }),
        FAHRENHEIT(new String[] {
                "degrees fahrenheit",
                "degree fahrenheit",
                "fahrenheit" }),
        KELVIN(new String[] {
                "kelvin" });

        final String[] mKeywords;

        TemperatureUnit(String[] keywords) {
            mKeywords = keywords;
        }

        public String[] GetKeywords() {
            return mKeywords;
        }
    }

    private final List<String> mUnits;
    private final TemperatureUnit[] mValues;

    ConverterTemperature(Context context) {
        if (context != null) {
            mUnits = Arrays.asList(context.getResources().getStringArray(R.array.temperature));
        } else {
            mUnits = null;
        }
        mValues = TemperatureUnit.values();
    }

    protected List<String> getUnits() {
        return mUnits;
    }

    protected Unit getUnitFromInteger(int position) {
        return mValues[position];
    }

    protected Unit getBaseUnit() {
        return null;
    }

    protected BigFraction getConversionFactor(ConversionPair pair) {
        return null;
    }

    Unit[] GetAllUnits() {
        return mValues;
    }

    @Override
    String Convert(String value, Unit input, Unit output)
            throws NullArgumentException, MathArithmeticException, NumberFormatException {
        BigFraction in = ToBigFraction(new BigDecimal(value));
        Logger.v(TAG, "Converting " + in + " from " + input + " to " + output);
        BigFraction out;
        if (input == output) {
            out = in;
        } else if (input == TemperatureUnit.CELSIUS && output == TemperatureUnit.FAHRENHEIT) {
            // F = (C * 9/5) + 32
            out = in.divide(FIVE_BY_NINE).add(32);
        } else if (input == TemperatureUnit.FAHRENHEIT && output == TemperatureUnit.CELSIUS) {
            // C = (F - 32) * 5/9
            out = in.subtract(32).multiply(FIVE_BY_NINE);
        } else if (input == TemperatureUnit.CELSIUS && output == TemperatureUnit.KELVIN) {
            // K = C + 273.15
            out = in.add(KELVIN_OFFSET);
        } else if (input == TemperatureUnit.KELVIN && output == TemperatureUnit.CELSIUS) {
            // C = K - 273.15
            out = in.subtract(KELVIN_OFFSET);
        } else if (input == TemperatureUnit.FAHRENHEIT && output == TemperatureUnit.KELVIN) {
            // K = ((F - 32) * 5/9) + 273.15
            out = in.subtract(32).multiply(FIVE_BY_NINE).add(KELVIN_OFFSET);
        } else if (input == TemperatureUnit.KELVIN && output == TemperatureUnit.FAHRENHEIT) {
            // F = ((K - 273.15) * 9/5) + 32
            out = in.subtract(KELVIN_OFFSET).divide(FIVE_BY_NINE).add(32);
        } else {
            // unknown conversion
            out = BigFraction.ZERO;
        }
        BigDecimal out_decimal = out.bigDecimalValue(INTERNAL_SCALE, BigDecimal.ROUND_HALF_EVEN);
        Logger.v(TAG, "Converted to " + out_decimal);
        return ResultFormat.Format(out_decimal);
    }
}
