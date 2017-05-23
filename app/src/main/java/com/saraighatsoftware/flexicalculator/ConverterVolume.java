package com.saraighatsoftware.flexicalculator;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class ConverterVolume {

    private String[] mUnits = { "Milliliters", "Cubic centimeters" };

    List<String> GetUnits() {
        return Arrays.asList(mUnits);
    }
}
