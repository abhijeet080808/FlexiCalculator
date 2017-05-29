package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterPower extends Converter {

    // must be same order and value as R.array.power
    enum PowerUnit implements Unit {
        WATTS,
        KILOWATTS,
        MEGAWATTS,
        GIGAWATTS,
        JOULES_PER_SECOND,
        HORSEPOWER_ELECTRICAL,
        HORSEPOWER_MECHANICAL,
        PFERDESTARKE,
        FOOT_POUNDS_PER_MINUTE,
        BTUS_IT_PER_MINUTE
    }

    private List<String> mUnits;
    private PowerUnit[] mValues;
    private HashMap<ConversionPair, BigFraction> mConversionFactors;

    ConverterPower(Context context) {
        if (context != null) {
            mUnits = Arrays.asList(context.getResources().getStringArray(R.array.power));
        }
        mValues = PowerUnit.values();
        mConversionFactors = new HashMap<>();

        // 1000 W = 1 kW
        mConversionFactors.put(new ConversionPair(PowerUnit.WATTS, PowerUnit.KILOWATTS),
                new BigFraction(1L, 1000L));
        // 1000 kW = 1 mW
        mConversionFactors.put(new ConversionPair(PowerUnit.WATTS, PowerUnit.MEGAWATTS),
                new BigFraction(1L, 1000000L));
        // 1000 mW = 1 gW
        mConversionFactors.put(new ConversionPair(PowerUnit.WATTS, PowerUnit.GIGAWATTS),
                new BigFraction(1L, 1000000000L));
        // 1 W = 1 J/s
        mConversionFactors.put(new ConversionPair(PowerUnit.WATTS, PowerUnit.JOULES_PER_SECOND),
                new BigFraction(1L, 1L));
        // 746 W = 1 hp
        mConversionFactors.put(new ConversionPair(PowerUnit.WATTS, PowerUnit.HORSEPOWER_ELECTRICAL),
                new BigFraction(1L, 746L));
        // 550 ft-lbf/s = 1 hp
        // 5000 ft = 1524 m
        // 100000000 lb = 45359237 kg
        // 1 lb-f = 45359237 / 100000000 * 9.80665 N
        // 1 hp = 550 * 45359237 / 100000000 * 9.80665 * 1524 / 5000 kg-m/s
        //      = 37284993579113511 / 50000000000000 W
        //      = 4660624197389189 / 6250000000000 W
        mConversionFactors.put(new ConversionPair(PowerUnit.WATTS, PowerUnit.HORSEPOWER_MECHANICAL),
                new BigFraction(6250000000000L, 4660624197389189L));
        // 1 PS = 75 * 9.80665 W
        mConversionFactors.put(new ConversionPair(PowerUnit.WATTS, PowerUnit.PFERDESTARKE),
                new BigFraction(100000L, 73549875L));
        // 1 hp = 33000 ft lbf/min
        // 1 W = 6250000000000 / 4660624197389189 * 33000 ft lbf/min
        mConversionFactors.put(new ConversionPair(PowerUnit.WATTS, PowerUnit.FOOT_POUNDS_PER_MINUTE),
                new BigFraction(206250000000000000L, 4660624197389189L));
        // https://www.aps.org/policy/reports/popa-reports/energy/units.cfm
        // 1 IT cal = energy to heat 1 g water from freezing to boiling divided by 100 C = 4186.8 J
        // 1 IT BTU = energy to heat 1 lb water from freezing to boiling divided by 180 F = 1055.0559 J
        // 45359237 g = 100000 lb
        // 1 BTU = (45359237 * 41868) / (100000 * 18000) J
        //       = 1899100534716 / 1800000000 J
        //       = 52752792631 / 50000000 J
        mConversionFactors.put(new ConversionPair(PowerUnit.WATTS, PowerUnit.BTUS_IT_PER_MINUTE),
                new BigFraction(3000000000L, 52752792631L));
    }

    protected List<String> getUnits() {
        return mUnits;
    }

    protected Unit getUnitFromInteger(int position) {
        return mValues[position];
    }

    protected Unit getBaseUnit() {
        return PowerUnit.WATTS;
    }

    protected BigFraction getConversionFactor(ConversionPair pair) {
        return mConversionFactors.get(pair);
    }
}
