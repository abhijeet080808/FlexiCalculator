package com.saraighatsoftware.flexicalculator;

import android.content.Context;
import android.util.Log;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.fraction.BigFraction;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

class ConverterTemperature extends Converter {

    private static final String TAG = "ConverterTemperature";

    // must be same order and value as R.array.temperature
    enum TemperatureUnit implements Unit {
        CELSIUS(new String[] { "celsius" }),
        FAHRENHEIT(new String[] { "fahrenheit" }),
        KELVIN(new String[] { "kelvin" });

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
            throws NullArgumentException, MathArithmeticException {
        try {
            BigFraction in = ToBigFraction(new BigDecimal(value));
            Log.v(TAG, "Converting " + in + " from " + input + " to " + output);
            BigFraction out;
            final BigFraction five_by_nine = new BigFraction(5, 9);
            final BigFraction kelvin_offset = new BigFraction(27315, 100);

            if (input == output) {
                out = in;
            } else if (input == TemperatureUnit.CELSIUS && output == TemperatureUnit.FAHRENHEIT) {
                // F = (C * 9/5) + 32
                out = in.divide(five_by_nine).add(32);
            } else if (input == TemperatureUnit.FAHRENHEIT && output == TemperatureUnit.CELSIUS) {
                // C = (F - 32) * 5/9
                out = in.subtract(32).multiply(five_by_nine);
            } else if (input == TemperatureUnit.CELSIUS && output == TemperatureUnit.KELVIN) {
                // K = C + 273.15
                out = in.add(kelvin_offset);
            } else if (input == TemperatureUnit.KELVIN && output == TemperatureUnit.CELSIUS) {
                // C = K - 273.15
                out = in.subtract(kelvin_offset);
            } else if (input == TemperatureUnit.FAHRENHEIT && output == TemperatureUnit.KELVIN) {
                // K = ((F - 32) * 5/9) + 273.15
                out = in.subtract(32).multiply(five_by_nine).add(kelvin_offset);
            } else if (input == TemperatureUnit.KELVIN && output == TemperatureUnit.FAHRENHEIT) {
                // F = ((K - 273.15) * 9/5) + 32
                out = in.subtract(kelvin_offset).divide(five_by_nine).add(32);
            } else {
                // unknown conversion
                out = BigFraction.ZERO;
            }

            Log.v(TAG, "Converted to " + out.bigDecimalValue(12, BigDecimal.ROUND_HALF_EVEN));
            return ResultFormat.Format(out.bigDecimalValue(12, BigDecimal.ROUND_HALF_EVEN));
        }
        catch (Exception e) {
            return "0";
        }
    }
}
