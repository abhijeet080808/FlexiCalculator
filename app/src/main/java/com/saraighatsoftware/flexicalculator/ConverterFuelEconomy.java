package com.saraighatsoftware.flexicalculator;

import android.content.Context;
import android.util.Log;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.fraction.BigFraction;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterFuelEconomy extends Converter {

    private static final String TAG = "ConverterFuelEconomy";

    // must be same order and value as R.array.fuel_economy
    enum FuelEconomyUnit implements Unit {
        MILES_PER_GALLON_US,
        MILES_PER_GALLON_UK,
        KILOMETERS_PER_LITER,
        LITERS_PER_100_KILOMETERS
    }

    private final List<String> mUnits;
    private final FuelEconomyUnit[] mValues;
    private final HashMap<ConversionPair, BigFraction> mConversionFactors;

    ConverterFuelEconomy(Context context) {
        if (context != null) {
            mUnits = Arrays.asList(context.getResources().getStringArray(R.array.fuel_economy));
        } else {
            mUnits = null;
        }
        mValues = FuelEconomyUnit.values();
        mConversionFactors = new HashMap<>();

        // 1 ml = 10000000/45460900000 uk gallon
        // 1 ml = 32000000/121133177088 us gallon
        // uk gallon = 45460900000/10000000 * 32000000/121133177088 us gallon
        //           = 568261250/473176473 us gallon
        // 1 miles per us gallon = 1 miles per 473176473/568261250 uk gallon
        //                       = 568261250/473176473 miles per uk gallon
        mConversionFactors.put(
                new ConversionPair(FuelEconomyUnit.MILES_PER_GALLON_US, FuelEconomyUnit.MILES_PER_GALLON_UK),
                new BigFraction(568261250L, 473176473L));
        // 1 liter = 32000000000/121133177088 us gallon
        // 1 mile = 804672/500000 km
        // 1 miles per us gallon = 1 miles per 121133177088/32000000000 liter
        //                       = 32000000000/121133177088 miles per liter
        //                       = 804672/500000 * 32000000000/121133177088 km per liter
        //                       = 48000/112903 km per liter
        mConversionFactors.put(
                new ConversionPair(FuelEconomyUnit.MILES_PER_GALLON_US, FuelEconomyUnit.KILOMETERS_PER_LITER),
                new BigFraction(48000L, 112903L));
        // 5 km/l = 100/5 liter per 100 km
        // the result is inverse of the output from below - output = 1 / (input * factor)
        mConversionFactors.put(
                new ConversionPair(FuelEconomyUnit.MILES_PER_GALLON_US, FuelEconomyUnit.LITERS_PER_100_KILOMETERS),
                new BigFraction(480L, 112903L));
    }

    String Convert(String value, Unit input, Unit output)
            throws NullArgumentException, MathArithmeticException {
        if (input != FuelEconomyUnit.LITERS_PER_100_KILOMETERS &&
                output != FuelEconomyUnit.LITERS_PER_100_KILOMETERS) {
            return super.Convert(value, input, output);
        }
        if (input == FuelEconomyUnit.LITERS_PER_100_KILOMETERS &&
                output == FuelEconomyUnit.LITERS_PER_100_KILOMETERS) {
            return value;
        }
        // only handle special case input LITERS_PER_100_KILOMETERS here
        if (input == FuelEconomyUnit.LITERS_PER_100_KILOMETERS) {
            try {
                // convert to base unit first
                BigFraction in = ToBigFraction(new BigDecimal(value));
                Log.v(TAG, "Converting " + in + " from " + input + " to " + output);
                // convert from liters per 100 kms to kms per liter
                in = new BigFraction(100L, 1L).divide(in);
                // convert to base unit
                in = in.divide(getConversionFactor(
                        new ConversionPair(getBaseUnit(), FuelEconomyUnit.KILOMETERS_PER_LITER)));
                // convert to output format
                BigFraction out = in;
                if (output != getBaseUnit()) {
                    out = out.multiply(getConversionFactor(new ConversionPair(getBaseUnit(), output)));
                }
                Log.v(TAG, "Converted to " + out.bigDecimalValue(12, BigDecimal.ROUND_HALF_EVEN));
                return ResultFormat.Format(out.bigDecimalValue(12, BigDecimal.ROUND_HALF_EVEN));
            } catch (Exception e) {
                return "0";
            }
        } else { // only handle special case output LITERS_PER_100_KILOMETERS here
            try {
                // convert to base unit first
                BigFraction in = ToBigFraction(new BigDecimal(value));
                Log.v(TAG, "Converting " + in + " from " + input + " to " + output);
                if (input != getBaseUnit()) {
                    in = in.divide(getConversionFactor(new ConversionPair(getBaseUnit(), input)));
                }
                // convert to output format
                BigFraction out = in;
                // convert to km/l first
                out = out.multiply(getConversionFactor(
                        new ConversionPair(getBaseUnit(), FuelEconomyUnit.KILOMETERS_PER_LITER)));
                // convert from kms per liter to liters per 100 kms
                out = new BigFraction(100L, 1L).divide(out);
                Log.v(TAG, "Converted to " + out.bigDecimalValue(12, BigDecimal.ROUND_HALF_EVEN));
                return ResultFormat.Format(out.bigDecimalValue(12, BigDecimal.ROUND_HALF_EVEN));
            } catch (Exception e) {
                return "0";
            }
        }
    }

    protected List<String> getUnits() {
        return mUnits;
    }

    protected Unit getUnitFromInteger(int position) {
        return mValues[position];
    }

    protected Unit getBaseUnit() {
        return FuelEconomyUnit.MILES_PER_GALLON_US;
    }

    protected BigFraction getConversionFactor(ConversionPair pair) {
        return mConversionFactors.get(pair);
    }
}
