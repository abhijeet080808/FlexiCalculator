package com.saraighatsoftware.flexicalculator;

import android.content.Context;

import org.apache.commons.math3.fraction.BigFraction;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class ConverterEnergy extends Converter {

    // must be same order and value as R.array.energy
    private enum EnergyUnit implements Unit {
        ELECTRON_VOLTS,
        JOULES,
        KILOJOULES,
        WATT_HOUR,
        KILOWATT_HOUR,
        CALORIES_THERMOCHEMICAL,
        CALORIES_IT,
        CALORIES_FOOD,
        FOOT_POUNDS,
        BRITISH_THERMAL_UNITS_THERMOCHEMICAL,
        BRITISH_THERMAL_UNITS_IT,
        BRITISH_THERMAL_UNITS_ISO
    }

    private List<String> mUnits;
    private EnergyUnit[] mValues;
    private HashMap<ConversionPair, BigFraction> mConversionFactors;

    ConverterEnergy(Context context) {
        mUnits = Arrays.asList(context.getResources().getStringArray(R.array.energy));
        mValues = EnergyUnit.values();
        mConversionFactors = new HashMap<>();

        // 1 J = 6.241509126 x 10^18 eV - http://physics.nist.gov/cgi-bin/cuu/Value?jev
        mConversionFactors.put(new ConversionPair(EnergyUnit.ELECTRON_VOLTS, EnergyUnit.JOULES),
                new BigFraction(new BigInteger("1"), new BigInteger("6241509126000000000")));
        // 1000 J = 1 kJ
        mConversionFactors.put(new ConversionPair(EnergyUnit.ELECTRON_VOLTS, EnergyUnit.KILOJOULES),
                new BigFraction(new BigInteger("1"), new BigInteger("6241509126000000000000")));
        // 3.6 kJ = 1 WH
        mConversionFactors.put(new ConversionPair(EnergyUnit.ELECTRON_VOLTS, EnergyUnit.WATT_HOUR),
                new BigFraction(new BigInteger("1"), new BigInteger("22469432853600000000000")));
        // 1000 WH = 1 KWH
        mConversionFactors.put(new ConversionPair(EnergyUnit.ELECTRON_VOLTS, EnergyUnit.KILOWATT_HOUR),
                new BigFraction(new BigInteger("1"), new BigInteger("22469432853600000000000000")));
        // 4184 J = 1000 thermal cal
        mConversionFactors.put(new ConversionPair(EnergyUnit.ELECTRON_VOLTS, EnergyUnit.CALORIES_THERMOCHEMICAL),
                new BigFraction(new BigInteger("1000"), new BigInteger("26114474183184000000000")));
        // 4186.8 J = 1000 IT cal
        mConversionFactors.put(new ConversionPair(EnergyUnit.ELECTRON_VOLTS, EnergyUnit.CALORIES_IT),
                new BigFraction(new BigInteger("1000"), new BigInteger("26131950408736800000000")));
        // 1000 thermal cal = 1 food cal
        mConversionFactors.put(new ConversionPair(EnergyUnit.ELECTRON_VOLTS, EnergyUnit.CALORIES_FOOD),
                new BigFraction(new BigInteger("1"), new BigInteger("26114474183184000000000")));
        // J = kg m^2 sec^-2 = kg m (m/s^2) = kg m (g)
        // 45359237 kg = 100000000 lb
        // 1524 m = 5000 ft
        // g = 9.80665 m/s^2
        // 1 J = 100000000/45359237 * 5000/1524 / 9.80665 ft-lb
        // 1 J = 100000000/45359237 * 5000/1524 * 100000 / 980665 ft-lb
        // 1 J = 50000000000000000 / 67790897416570020 ft-lbs
        // 1 J = 1562500000000000 / 2118465544267813 ft-lbs
        // 1 eV = 1562500000000000 /(2118465544267813 * 6241509126000000000) ft-lbs
        // 1 ev = 1562500000000000 / 13222422027664111827561438000000000 ft-lbs
        // 1 ev = 1562500 / 13222422027664111827561438 ft-lbs
        mConversionFactors.put(new ConversionPair(EnergyUnit.ELECTRON_VOLTS, EnergyUnit.FOOT_POUNDS),
                new BigFraction(
                        new BigInteger("1562500"),
                        new BigInteger("13222422027664111827561438")));
        // https://www.aps.org/policy/reports/popa-reports/energy/units.cfm
        // 1 thermal cal = energy to heat 1 g water from freezing to boiling divided by 100 C = 4184 J
        // 1 thermal BTU = energy to heat 1 lb water from freezing to boiling divided by 180 F = 1054.3503 J
        // 45359237 g = 100000 lb
        // 1 BTU = (45359237 * 4184) / (100000 * 1800) J
        //       = 189783047608 / 180000000 J
        //       = 23722880951 / 22500000 J
        mConversionFactors.put(new ConversionPair(EnergyUnit.ELECTRON_VOLTS, EnergyUnit.BRITISH_THERMAL_UNITS_THERMOCHEMICAL),
                new BigFraction(
                        new BigInteger("22500000"),
                        new BigInteger("148066577950678058826000000000")));
        // https://www.aps.org/policy/reports/popa-reports/energy/units.cfm
        // 1 IT cal = energy to heat 1 g water from freezing to boiling divided by 100 C = 4186.8 J
        // 1 IT BTU = energy to heat 1 lb water from freezing to boiling divided by 180 F = 1055.0559 J
        // 45359237 g = 100000 lb
        // 1 BTU = (45359237 * 41868) / (100000 * 18000) J
        //       = 1899100534716 / 1800000000 J
        //       = 52752792631 / 50000000 J
        mConversionFactors.put(new ConversionPair(EnergyUnit.ELECTRON_VOLTS, EnergyUnit.BRITISH_THERMAL_UNITS_IT),
                new BigFraction(
                        new BigInteger("50000000"),
                        new BigInteger("329257036628372050506000000000")));
        // 1 BTU = 1055.06 J
        mConversionFactors.put(new ConversionPair(EnergyUnit.ELECTRON_VOLTS, EnergyUnit.BRITISH_THERMAL_UNITS_ISO),
                new BigFraction(
                        new BigInteger("1"),
                        new BigInteger("6585166618477560000000")));
    }

    List<String> GetUnits() {
        return mUnits;
    }

    Unit GetUnitFromInteger(int position) {
        return mValues[position];
    }

    Unit GetBaseUnit() {
        return EnergyUnit.ELECTRON_VOLTS;
    }

    BigFraction GetConversionFactor(ConversionPair pair) {
        return mConversionFactors.get(pair);
    }
}
