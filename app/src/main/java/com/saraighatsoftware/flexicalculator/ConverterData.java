package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterData extends Converter {

    // must be same order and value as R.array.data
    enum DataUnit implements Unit {
        BITS(new String[] { "bit" }),
        BYTES(new String[] { "byte" }),
        KILOBITS(new String[] { "kilobit" }),
        KIBIBITS(new String[] { "kibibit" }),
        KILOBYTES(new String[] { "kilobyte" }),
        KIBIBYTES(new String[] { "kibibyte" }),
        MEGABITS(new String[] { "megabit" }),
        MEBIBITS(new String[] { "mebibit" }),
        MEGABYTES(new String[] { "megabyte" }),
        MEBIBYTES(new String[] { "mebibyte" }),
        GIGABITS(new String[] { "gigabit" }),
        GIBIBITS(new String[] { "gibibit" }),
        GIGABYTES(new String[] { "gigabyte" }),
        GIBIBYTES(new String[] { "gibibyte" }),
        TERABITS(new String[] { "terabit" }),
        TEBIBITS(new String[] { "tebibit" }),
        TERABYTES(new String[] { "terabyte" }),
        TEBIBYTES(new String[] { "tebibyte" }),
        PETABITS(new String[] { "petabit" }),
        PEBIBITS(new String[] { "pebibit" }),
        PETABYTES(new String[] { "petabyte" }),
        PEBIBYTES(new String[] { "pebibyte" });

        final String[] mKeywords;

        DataUnit(String[] keywords) {
            mKeywords = keywords;
        }

        public String[] GetKeywords() {
            return mKeywords;
        }
    }

    private final List<String> mUnits;
    private final DataUnit[] mValues;
    private final HashMap<ConversionPair, BigFraction> mConversionFactors;

    ConverterData(Context context) {
        if (context != null) {
            mUnits = Arrays.asList(context.getResources().getStringArray(R.array.data));
        } else {
            mUnits = null;
        }
        mValues = DataUnit.values();
        mConversionFactors = new HashMap<>();

        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.BYTES),
                new BigFraction(1L, 8L));

        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.KILOBITS),
                new BigFraction(1L, 1000L));
        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.KIBIBITS),
                new BigFraction(1L, 1024L));
        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.KILOBYTES),
                new BigFraction(1L, 8000L));
        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.KIBIBYTES),
                new BigFraction(1L, 8192L));

        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.MEGABITS),
                new BigFraction(1L, 1000000L));
        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.MEBIBITS),
                new BigFraction(1L, 1048576L));
        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.MEGABYTES),
                new BigFraction(1L, 8000000L));
        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.MEBIBYTES),
                new BigFraction(1L, 8388608L));

        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.GIGABITS),
                new BigFraction(1L, 1000000000L));
        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.GIBIBITS),
                new BigFraction(1L, 1073741824L));
        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.GIGABYTES),
                new BigFraction(1L, 8000000000L));
        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.GIBIBYTES),
                new BigFraction(1L, 8589934592L));

        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.TERABITS),
                new BigFraction(1L, 1000000000000L));
        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.TEBIBITS),
                new BigFraction(1L, 1099511627776L));
        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.TERABYTES),
                new BigFraction(1L, 8000000000000L));
        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.TEBIBYTES),
                new BigFraction(1L, 8796093022208L));

        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.PETABITS),
                new BigFraction(1L, 1000000000000000L));
        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.PEBIBITS),
                new BigFraction(1L, 1125899906842624L));
        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.PETABYTES),
                new BigFraction(1L, 8000000000000000L));
        mConversionFactors.put(new ConversionPair(DataUnit.BITS, DataUnit.PEBIBYTES),
                new BigFraction(1L, 9007199254740992L));
    }

    protected List<String> getUnits() {
        return mUnits;
    }

    protected Unit getUnitFromInteger(int position) {
        return mValues[position];
    }

    protected Unit getBaseUnit() {
        return DataUnit.BITS;
    }

    protected BigFraction getConversionFactor(ConversionPair pair) {
        return mConversionFactors.get(pair);
    }

    Unit[] GetAllUnits() {
        return mValues;
    }
}
