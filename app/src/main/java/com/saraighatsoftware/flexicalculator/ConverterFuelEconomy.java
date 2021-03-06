package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.fraction.BigFraction;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterFuelEconomy extends Converter {

    private static final String TAG = "ConverterFuelEconomy";

    private static final BigFraction HUNDRED = new BigFraction(100);

    // must be same order and value as R.array.fuel_economy
    enum FuelEconomyUnit implements Unit {
        MILES_PER_GALLON_US(new String[] {
                "us miles per gallon",
                "miles per gallon us",
                "miles per gallon",
                "mpg" }),
        MILES_PER_GALLON_UK(new String[] {
                "uk miles per gallon",
                "imperial miles per gallon",
                "british miles per gallon",
                "miles per gallon uk" }),
        KILOMETERS_PER_LITER(new String[] {
                "kilometers per liter",
                "kilometer per liter",
                "kilometres per liter",
                "kilometre per liter",
                "kilometers per litre",
                "kilometer per litre",
                "kilometres per litre",
                "kilometre per litre",
                "kmpl",
                "kpl" }),
        LITERS_PER_100_KILOMETERS(new String[] {
                "liters per 100 kilometers",
                "liter per 100 kilometers",
                "litres per 100 kilometers",
                "litre per 100 kilometers",
                "liters per 100 kilometres",
                "liter per 100 kilometres",
                "litres per 100 kilometres",
                "litre per 100 kilometres",
                "liters per 100 kilometer",
                "liter per 100 kilometer",
                "litres per 100 kilometer",
                "litre per 100 kilometer",
                "liters per 100 kilometre",
                "liter per 100 kilometre",
                "litres per 100 kilometre",
                "litre per 100 kilometre",

                "liters per hundred kilometers",
                "liter per hundred kilometers",
                "litres per hundred kilometers",
                "litre per hundred kilometers",
                "liters per hundred kilometres",
                "liter per hundred kilometres",
                "litres per hundred kilometres",
                "litre per hundred kilometres",
                "liters per hundred kilometer",
                "liter per hundred kilometer",
                "litres per hundred kilometer",
                "litre per hundred kilometer",
                "liters per hundred kilometre",
                "liter per hundred kilometre",
                "litres per hundred kilometre",
                "litre per hundred kilometre" });

        final String[] mKeywords;

        FuelEconomyUnit(String[] keywords) {
            mKeywords = keywords;
        }

        public String[] GetKeywords() {
            return mKeywords;
        }
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
            throws NullArgumentException, MathArithmeticException, NumberFormatException {
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
            // convert to base unit first
            BigFraction in = ToBigFraction(new BigDecimal(value));
            Logger.v(TAG, "Converting " + in + " from " + input + " to " + output);
            // convert from liters per 100 kms to kms per liter
            in = HUNDRED.divide(in);
            // convert to base unit
            in = in.divide(getConversionFactor(
                    new ConversionPair(getBaseUnit(), FuelEconomyUnit.KILOMETERS_PER_LITER)));
            // convert to output format
            BigFraction out = in;
            if (output != getBaseUnit()) {
                out = out.multiply(getConversionFactor(new ConversionPair(getBaseUnit(), output)));
            }
            BigDecimal out_decimal = out.bigDecimalValue(INTERNAL_SCALE, BigDecimal.ROUND_HALF_EVEN);
            Logger.v(TAG, "Converted to " + out_decimal);
            return ResultFormat.Format(out_decimal);

        } else { // only handle special case output LITERS_PER_100_KILOMETERS here

            // convert to base unit first
            BigFraction in = ToBigFraction(new BigDecimal(value));
            Logger.v(TAG, "Converting " + in + " from " + input + " to " + output);
            if (input != getBaseUnit()) {
                in = in.divide(getConversionFactor(new ConversionPair(getBaseUnit(), input)));
            }
            // convert to output format
            BigFraction out = in;
            // convert to km/l first
            out = out.multiply(getConversionFactor(
                    new ConversionPair(getBaseUnit(), FuelEconomyUnit.KILOMETERS_PER_LITER)));
            // convert from kms per liter to liters per 100 kms
            out = HUNDRED.divide(out);
            BigDecimal out_decimal = out.bigDecimalValue(INTERNAL_SCALE, BigDecimal.ROUND_HALF_EVEN);
            Logger.v(TAG, "Converted to " + out_decimal);
            return ResultFormat.Format(out_decimal);
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

    Unit[] GetAllUnits() {
        return mValues;
    }
}
