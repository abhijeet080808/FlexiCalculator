package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterWeight extends Converter {

    // must be same order and value as R.array.weight
    private enum WeightUnit implements Unit {
        CARATS,
        MILLIGRAMS,
        CENTIGRAMS,
        DECIGRAMS,
        GRAMS,
        DEKAGRAMS,
        HECTOGRAMS,
        KILOGRAMS,
        METRIC_TONNES,
        OUNCES,
        POUNDS,
        STONES,
        SHORT_TONS_US,
        LONG_TONS_UK
    }

    private List<String> mUnits;
    private WeightUnit[] mValues;
    private HashMap<ConversionPair, BigFraction> mConversionFactors;

    ConverterWeight(Context context) {
        mUnits = Arrays.asList(context.getResources().getStringArray(R.array.weight));
        mValues = WeightUnit.values();
        mConversionFactors = new HashMap<>();

        // 1 carat = 200 mg
        mConversionFactors.put(new ConversionPair(WeightUnit.CARATS, WeightUnit.MILLIGRAMS),
                new BigFraction(200L, 1L));
        // 10 mg = 1 centigram
        mConversionFactors.put(new ConversionPair(WeightUnit.CARATS, WeightUnit.CENTIGRAMS),
                new BigFraction(200L, 10L));
        // 10 centigram = 1 decigram
        mConversionFactors.put(new ConversionPair(WeightUnit.CARATS, WeightUnit.DECIGRAMS),
                new BigFraction(200L, 100L));
        // 10 decigram = 1 gram
        mConversionFactors.put(new ConversionPair(WeightUnit.CARATS, WeightUnit.GRAMS),
                new BigFraction(200L, 1000L));
        // 10 gram = 1 dekagram
        mConversionFactors.put(new ConversionPair(WeightUnit.CARATS, WeightUnit.DEKAGRAMS),
                new BigFraction(200L, 10000L));
        // 10 dekagram = 1 hectogram
        mConversionFactors.put(new ConversionPair(WeightUnit.CARATS, WeightUnit.HECTOGRAMS),
                new BigFraction(200L, 100000L));
        // 10 hectogram = 1 kg
        mConversionFactors.put(new ConversionPair(WeightUnit.CARATS, WeightUnit.KILOGRAMS),
                new BigFraction(200L, 1000000L));
        // 1000 kg = 1 metric tonne
        mConversionFactors.put(new ConversionPair(WeightUnit.CARATS, WeightUnit.METRIC_TONNES),
                new BigFraction(200L, 1000000000L));
        // 45359237 g = 1600000 ounce
        mConversionFactors.put(new ConversionPair(WeightUnit.CARATS, WeightUnit.OUNCES),
                new BigFraction(320000L, 45359237L));
        // 16 ounce = 1 pound
        mConversionFactors.put(new ConversionPair(WeightUnit.CARATS, WeightUnit.POUNDS),
                new BigFraction(320000L, 725747792L));
        // 14 pound = 1 stone
        mConversionFactors.put(new ConversionPair(WeightUnit.CARATS, WeightUnit.STONES),
                new BigFraction(320000L, 10160469088L));
        // 2000 pound = 1 us ton
        mConversionFactors.put(new ConversionPair(WeightUnit.CARATS, WeightUnit.SHORT_TONS_US),
                new BigFraction(320000L, 1451495584000L));
        // 2240 pound = 1 uk ton
        mConversionFactors.put(new ConversionPair(WeightUnit.CARATS, WeightUnit.LONG_TONS_UK),
                new BigFraction(320000L, 1625675054080L));
    }

    List<String> GetUnits() {
        return mUnits;
    }

    Unit GetUnitFromInteger(int position) {
        return mValues[position];
    }

    Unit GetBaseUnit() {
        return WeightUnit.CARATS;
    }

    BigFraction GetConversionFactor(ConversionPair pair) {
        return mConversionFactors.get(pair);
    }
}
