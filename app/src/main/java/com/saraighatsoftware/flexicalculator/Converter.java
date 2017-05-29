package com.saraighatsoftware.flexicalculator;

import android.util.Log;

import org.apache.commons.math3.fraction.BigFraction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

abstract class Converter {

    private static final String TAG = "Converter";

    interface Unit {
    }

    class ConversionPair {

        final Unit input;
        final Unit output;

        ConversionPair(Unit input, Unit output) {
            this.input = input;
            this.output = output;
        }

        @Override
        public int hashCode() {
            // From Effective Java
            int hash = 17;
            hash = hash * 31 + input.hashCode();
            hash = hash * 31 + output.hashCode();
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ConversionPair))
                return false;
            if (obj == this)
                return true;

            ConversionPair rhs = (ConversionPair) obj;
            return (this.input == rhs.input && this.output == rhs.output);
        }
    }

    protected abstract List<String> getUnits();

    protected abstract Unit getUnitFromInteger(int position);

    protected abstract Unit getBaseUnit();

    // output = input * conversion_factor
    protected abstract BigFraction getConversionFactor(ConversionPair pair);

    static BigFraction ToBigFraction(BigDecimal val) {
        final int scale = val.scale();
        // If scale >= 0 then the value is val.unscaledValue() / 10^scale
        if(scale >= 0)
            return new BigFraction(val.unscaledValue(), BigInteger.TEN.pow(scale));
        // If scale < 0 then the value is val.unscaledValue() * 10^-scale
        return new BigFraction(val.unscaledValue().multiply(BigInteger.TEN.pow(-scale)));
    }

    String Convert(String value, Unit input, Unit output) {
        if (input == output) {
            return value;
        }
        try {
            // convert to base unit first
            BigFraction in = ToBigFraction(new BigDecimal(value));
            Log.v(TAG, "Converting " + in + " from " + input + " to " + output);
            if (input != getBaseUnit()) {
                in = in.divide(getConversionFactor(new ConversionPair(getBaseUnit(), input)));
            }
            // convert to output format
            BigFraction out = in;
            if (output != getBaseUnit()) {
                out = out.multiply(getConversionFactor(new ConversionPair(getBaseUnit(), output)));
            }
            Log.v(TAG, "Converted to " + out.bigDecimalValue(12, BigDecimal.ROUND_HALF_EVEN));
            return ResultFormat.Format(out.bigDecimalValue(12, BigDecimal.ROUND_HALF_EVEN));
        }
        catch (Exception e) {
            return "";
        }
    }
}
