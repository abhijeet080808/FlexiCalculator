package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterTime extends Converter {

    // must be same order and value as R.array.time
    enum TimeUnit implements Unit {
        MICROSECONDS(new String[] { "microsecond" }),
        MILLISECONDS(new String[] { "millisecond" }),
        SECONDS(new String[] { "second" }),
        MINUTES(new String[] { "minute" }),
        HOURS(new String[] { "hour" }),
        DAYS(new String[] { "day" }),
        WEEKS(new String[] { "week" }),
        YEARS(new String[] { "year" });

        final String[] mKeywords;

        TimeUnit(String[] keywords) {
            mKeywords = keywords;
        }

        public String[] GetKeywords() {
            return mKeywords;
        }
    }

    private final List<String> mUnits;
    private final TimeUnit[] mValues;
    private final HashMap<ConversionPair, BigFraction> mConversionFactors;

    ConverterTime(Context context) {
        if (context != null) {
            mUnits = Arrays.asList(context.getResources().getStringArray(R.array.time));
        } else {
            mUnits = null;
        }
        mValues = TimeUnit.values();
        mConversionFactors = new HashMap<>();

        // 1000 micro sec = 1 milli sec
        mConversionFactors.put(new ConversionPair(TimeUnit.MICROSECONDS, TimeUnit.MILLISECONDS),
                new BigFraction(1L, 1000L));
        // 1000 milli sec = 1 sec
        mConversionFactors.put(new ConversionPair(TimeUnit.MICROSECONDS, TimeUnit.SECONDS),
                new BigFraction(1L, 1000000L));
        // 60 sec = 1 min
        mConversionFactors.put(new ConversionPair(TimeUnit.MICROSECONDS, TimeUnit.MINUTES),
                new BigFraction(1L, 60000000L));
        // 60 min = 1 hour
        mConversionFactors.put(new ConversionPair(TimeUnit.MICROSECONDS, TimeUnit.HOURS),
                new BigFraction(1L, 3600000000L));
        // 24 hour = 1 day
        mConversionFactors.put(new ConversionPair(TimeUnit.MICROSECONDS, TimeUnit.DAYS),
                new BigFraction(1L, 86400000000L));
        // 7 day = 1 week
        mConversionFactors.put(new ConversionPair(TimeUnit.MICROSECONDS, TimeUnit.WEEKS),
                new BigFraction(1L, 604800000000L));
        // 365 day = 1 year
        mConversionFactors.put(new ConversionPair(TimeUnit.MICROSECONDS, TimeUnit.YEARS),
                new BigFraction(1L, 31536000000000L));
    }

    protected List<String> getUnits() {
        return mUnits;
    }

    protected Unit getUnitFromInteger(int position) {
        return mValues[position];
    }

    protected Unit getBaseUnit() {
        return TimeUnit.MICROSECONDS;
    }

    protected BigFraction getConversionFactor(ConversionPair pair) {
        return mConversionFactors.get(pair);
    }

    Unit[] GetAllUnits() {
        return mValues;
    }
}
