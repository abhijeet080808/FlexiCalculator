package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterWeight extends Converter {

    // must be same order and value as R.array.weight
    enum WeightUnit implements Unit {
        CARATS(new String[] {
                "carats",
                "carat" }),
        MILLIGRAMS(new String[] {
                "milligrams",
                "milligram",
                "mg" }),
        CENTIGRAMS(new String[] {
                "centigrams",
                "centigram" }),
        DECIGRAMS(new String[] {
                "decigrams",
                "decigram" }),
        GRAMS(new String[] {
                "grams",
                "gram" }),
        DEKAGRAMS(new String[] {
                "dekagrams",
                "dekagram" }),
        HECTOGRAMS(new String[] {
                "hectograms",
                "hectogram" }),
        KILOGRAMS(new String[] {
                "kilograms",
                "kilogram",
                "kg" }),
        METRIC_TONNES(new String[] {
                "metric tonnes",
                "metric tonne",
                "metric tons",
                "metric ton" }),
        OUNCES(new String[] {
                "ounces",
                "ounce",
                "oz" }),
        POUNDS(new String[] {
                "pounds",
                "pound",
                "lbs",
                "lb" }),
        STONES(new String[] {
                "stones",
                "stone" }),
        SHORT_TONS_US(new String[] {
                "short tons",
                "short ton" }),
        LONG_TONS_UK(new String[] {
                "long tons",
                "long ton" });

        final String[] mKeywords;

        WeightUnit(String[] keywords) {
            mKeywords = keywords;
        }

        public String[] GetKeywords() {
            return mKeywords;
        }
    }

    private final List<String> mUnits;
    private final WeightUnit[] mValues;
    private final HashMap<ConversionPair, BigFraction> mConversionFactors;

    ConverterWeight(Context context) {
        if (context != null) {
            mUnits = Arrays.asList(context.getResources().getStringArray(R.array.weight));
        } else {
            mUnits = null;
        }
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

    protected List<String> getUnits() {
        return mUnits;
    }

    protected Unit getUnitFromInteger(int position) {
        return mValues[position];
    }

    protected Unit getBaseUnit() {
        return WeightUnit.CARATS;
    }

    protected BigFraction getConversionFactor(ConversionPair pair) {
        return mConversionFactors.get(pair);
    }

    Unit[] GetAllUnits() {
        return mValues;
    }
}
