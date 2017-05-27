package com.saraighatsoftware.flexicalculator;

import android.content.Context;
import android.util.Log;

import org.apache.commons.math3.fraction.BigFraction;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

class ConverterTemperature extends Converter {

    private static final String TAG = "ConverterTemperature";

    // must be same order and value as R.array.temperature
    private enum TemperatureUnit implements Unit {
        CELSIUS,
        FAHRENHEIT,
        KELVIN
    }

    private List<String> mUnits;
    private TemperatureUnit[] mValues;

    ConverterTemperature(Context context) {
        mUnits = Arrays.asList(context.getResources().getStringArray(R.array.temperature));
        mValues = TemperatureUnit.values();
    }

    List<String> GetUnits() {
        return mUnits;
    }

    Unit GetUnitFromInteger(int position) {
        return mValues[position];
    }

    Unit GetBaseUnit() {
        return null;
    }

    BigFraction GetConversionFactor(ConversionPair pair) {
        return null;
    }

    @Override
    String Convert(String value, Unit input, Unit output) {
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
