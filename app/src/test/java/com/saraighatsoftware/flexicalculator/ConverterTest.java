package com.saraighatsoftware.flexicalculator;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.saraighatsoftware.flexicalculator.ConverterAngle.AngleUnit.*;
import static com.saraighatsoftware.flexicalculator.ConverterArea.AreaUnit.*;
import static com.saraighatsoftware.flexicalculator.ConverterData.DataUnit.*;
import static com.saraighatsoftware.flexicalculator.ConverterEnergy.EnergyUnit.*;
import static com.saraighatsoftware.flexicalculator.ConverterFuelEconomy.FuelEconomyUnit.*;
import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class ConverterTest {

    @Test
    public void Convert() throws Exception {
        PowerMockito.mockStatic(Log.class); // returns 1 when Log.e() etc is called

        ConverterAngle mConverterAngle = new ConverterAngle(null);
        assertEquals("22", mConverterAngle.Convert("1400", GRADIANS, RADIANS));
        assertEquals("90", mConverterAngle.Convert("100", GRADIANS, DEGREES));

        ConverterArea mConverterArea = new ConverterArea(null);
        assertEquals("3097600", mConverterArea.Convert("1", SQUARE_MILES, SQUARE_YARDS));
        assertEquals("27878400", mConverterArea.Convert("1", SQUARE_MILES, SQUARE_FEET));
        assertEquals("4014489600", mConverterArea.Convert("1", SQUARE_MILES, SQUARE_INCHES));
        assertEquals("10000", mConverterArea.Convert("1", HECTARES, SQUARE_METERS));
        assertEquals("40468564224", mConverterArea.Convert("10000000", ACRES, SQUARE_METERS));
        assertEquals("1000000", mConverterArea.Convert("1", SQUARE_KILOMETERS, SQUARE_METERS));
        assertEquals("10000", mConverterArea.Convert("1", SQUARE_METERS, SQUARE_CENTIMETERS));

        ConverterData mConverterData = new ConverterData(null);
        assertEquals("1024", mConverterData.Convert("1", PEBIBYTES, TEBIBYTES));
        assertEquals("1024", mConverterData.Convert("1", TEBIBYTES, GIBIBYTES));
        assertEquals("1024", mConverterData.Convert("1", GIBIBYTES, MEBIBYTES));
        assertEquals("1024", mConverterData.Convert("1", MEBIBYTES, KIBIBYTES));
        assertEquals("1024", mConverterData.Convert("1", KIBIBYTES, BYTES));
        assertEquals("1024", mConverterData.Convert("1", PEBIBITS, TEBIBITS));
        assertEquals("1024", mConverterData.Convert("1", TEBIBITS, GIBIBITS));
        assertEquals("1024", mConverterData.Convert("1", GIBIBITS, MEBIBITS));
        assertEquals("1024", mConverterData.Convert("1", MEBIBITS, KIBIBITS));
        assertEquals("1024", mConverterData.Convert("1", KIBIBITS, BITS));
        assertEquals("1000", mConverterData.Convert("1", PETABYTES, TERABYTES));
        assertEquals("1000", mConverterData.Convert("1", TERABYTES, GIGABYTES));
        assertEquals("1000", mConverterData.Convert("1", GIGABYTES, MEGABYTES));
        assertEquals("1000", mConverterData.Convert("1", MEGABYTES, KILOBYTES));
        assertEquals("1000", mConverterData.Convert("1", KILOBYTES, BYTES));
        assertEquals("1000", mConverterData.Convert("1", PETABITS, TERABITS));
        assertEquals("1000", mConverterData.Convert("1", TERABITS, GIGABITS));
        assertEquals("1000", mConverterData.Convert("1", GIGABITS, MEGABITS));
        assertEquals("1000", mConverterData.Convert("1", MEGABITS, KILOBITS));
        assertEquals("1000", mConverterData.Convert("1", KILOBITS, BITS));
        assertEquals("8", mConverterData.Convert("1", BYTES, BITS));

        ConverterEnergy mConverterEnergy = new ConverterEnergy(null);
        assertEquals("1055.06", mConverterEnergy.Convert("1", BRITISH_THERMAL_UNITS_ISO, JOULES));
        assertEquals("1055.055853", mConverterEnergy.Convert("1", BRITISH_THERMAL_UNITS_IT, JOULES));
        assertEquals("1054.350264", mConverterEnergy.Convert("1", BRITISH_THERMAL_UNITS_THERMOCHEMICAL, JOULES));
        assertEquals("2118465544267813", mConverterEnergy.Convert("1562500000000000", FOOT_POUNDS, JOULES));
        assertEquals("1000", mConverterEnergy.Convert("1", CALORIES_FOOD, CALORIES_THERMOCHEMICAL));
        assertEquals("4186.8", mConverterEnergy.Convert("1000", CALORIES_IT, JOULES));
        assertEquals("4184", mConverterEnergy.Convert("1000", CALORIES_THERMOCHEMICAL, JOULES));
        assertEquals("1000", mConverterEnergy.Convert("1", KILOWATT_HOURS, WATT_HOURS));
        assertEquals("1000", mConverterEnergy.Convert("1", KILOJOULES, JOULES));
        assertEquals("6241509126000000000", mConverterEnergy.Convert("1", JOULES, ELECTRON_VOLTS));

        ConverterFuelEconomy mConverterFuelEconomy = new ConverterFuelEconomy(null);
        assertEquals("0.832674", mConverterFuelEconomy.Convert("1", MILES_PER_GALLON_UK, MILES_PER_GALLON_US));
        assertEquals("0.354006", mConverterFuelEconomy.Convert("1", MILES_PER_GALLON_UK, KILOMETERS_PER_LITER));
        assertEquals("282.480936", mConverterFuelEconomy.Convert("1", MILES_PER_GALLON_UK, LITERS_PER_100_KILOMETERS));
    }
}