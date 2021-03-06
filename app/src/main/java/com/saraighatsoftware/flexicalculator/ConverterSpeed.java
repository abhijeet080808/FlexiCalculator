package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterSpeed extends Converter {

    // must be same order and value as R.array.speed
    enum SpeedUnit implements Unit {
        METERS_PER_SECOND(new String[] {
                "meters per second",
                "meter per second",
                "metres per second",
                "metre per second" }),
        KILOMETERS_PER_SECOND(new String[] {
                "kilometers per second",
                "kilometer per second",
                "kilometres per second",
                "kilometre per second" }),
        KILOMETERS_PER_HOUR(new String[] {
                "kilometers per hour",
                "kilometer per hour",
                "kilometres per hour",
                "kilometre per hour",
                "kmph",
                "kph" }),
        FEET_PER_SECOND(new String[] {
                "feet per second",
                "foot per second" }),
        MILES_PER_SECOND(new String[] {
                "miles per second",
                "mile per second" }),
        MILES_PER_HOUR(new String[] {
                "miles per hour",
                "mile per hour",
                "mph" }),
        KNOTS(new String[] {
                "knots" }),
        MACH(new String[] {
                "mach" });

        final String[] mKeywords;

        SpeedUnit(String[] keywords) {
            mKeywords = keywords;
        }

        public String[] GetKeywords() {
            return mKeywords;
        }
    }

    private final List<String> mUnits;
    private final SpeedUnit[] mValues;
    private final HashMap<ConversionPair, BigFraction> mConversionFactors;

    ConverterSpeed(Context context) {
        if (context != null) {
            mUnits = Arrays.asList(context.getResources().getStringArray(R.array.speed));
        } else {
            mUnits = null;
        }
        mValues = SpeedUnit.values();
        mConversionFactors = new HashMap<>();

        // 1000 m/s = 1 km/s
        mConversionFactors.put(new ConversionPair(SpeedUnit.METERS_PER_SECOND, SpeedUnit.KILOMETERS_PER_SECOND),
                new BigFraction(1L, 1000L));
        // 1 km/s = 3600 km/h
        mConversionFactors.put(new ConversionPair(SpeedUnit.METERS_PER_SECOND, SpeedUnit.KILOMETERS_PER_HOUR),
                new BigFraction(3600L, 1000L));
        // 1524 m = 5000 ft
        mConversionFactors.put(new ConversionPair(SpeedUnit.METERS_PER_SECOND, SpeedUnit.FEET_PER_SECOND),
                new BigFraction(5000L, 1524L));
        // 5280 ft = 1 mile
        mConversionFactors.put(new ConversionPair(SpeedUnit.METERS_PER_SECOND, SpeedUnit.MILES_PER_SECOND),
                new BigFraction(500L, 804672L));
        // 1 m/s = 3600 m/h
        mConversionFactors.put(new ConversionPair(SpeedUnit.METERS_PER_SECOND, SpeedUnit.MILES_PER_HOUR),
                new BigFraction(1800000L, 804672L));
        // 1 knot = 1 nautical mile / hour
        // 1852 m = 1 nautical mile
        mConversionFactors.put(new ConversionPair(SpeedUnit.METERS_PER_SECOND, SpeedUnit.KNOTS),
                new BigFraction(3600L, 1852L));
        // 1 mach = 343 m/s at 20 C in dry air
        mConversionFactors.put(new ConversionPair(SpeedUnit.METERS_PER_SECOND, SpeedUnit.MACH),
                new BigFraction(1L, 343L));
    }

    protected List<String> getUnits() {
        return mUnits;
    }

    protected Unit getUnitFromInteger(int position) {
        return mValues[position];
    }

    protected Unit getBaseUnit() {
        return SpeedUnit.METERS_PER_SECOND;
    }

    protected BigFraction getConversionFactor(ConversionPair pair) {
        return mConversionFactors.get(pair);
    }

    Unit[] GetAllUnits() {
        return mValues;
    }
}
