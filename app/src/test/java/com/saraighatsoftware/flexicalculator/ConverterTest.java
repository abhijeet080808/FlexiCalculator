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
import static com.saraighatsoftware.flexicalculator.ConverterLength.LengthUnit.*;
import static com.saraighatsoftware.flexicalculator.ConverterPower.PowerUnit.*;
import static com.saraighatsoftware.flexicalculator.ConverterPressure.PressureUnit.*;
import static com.saraighatsoftware.flexicalculator.ConverterSpeed.SpeedUnit.*;
import static com.saraighatsoftware.flexicalculator.ConverterTemperature.TemperatureUnit.*;
import static com.saraighatsoftware.flexicalculator.ConverterTime.TimeUnit.*;
import static com.saraighatsoftware.flexicalculator.ConverterTorque.TorqueUnit.*;
import static com.saraighatsoftware.flexicalculator.ConverterVolume.VolumeUnit.*;
import static com.saraighatsoftware.flexicalculator.ConverterWeight.WeightUnit.*;
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
        assertEquals("1055.05585262", mConverterEnergy.Convert("1", BRITISH_THERMAL_UNITS_IT, JOULES));
        assertEquals("1054.350264489", mConverterEnergy.Convert("1", BRITISH_THERMAL_UNITS_THERMOCHEMICAL, JOULES));
        assertEquals("2118465544267813", mConverterEnergy.Convert("1562500000000000", FOOT_POUNDS, JOULES));
        assertEquals("1000", mConverterEnergy.Convert("1", CALORIES_FOOD, CALORIES_THERMOCHEMICAL));
        assertEquals("4186.8", mConverterEnergy.Convert("1000", CALORIES_IT, JOULES));
        assertEquals("4184", mConverterEnergy.Convert("1000", CALORIES_THERMOCHEMICAL, JOULES));
        assertEquals("1000", mConverterEnergy.Convert("1", KILOWATT_HOURS, WATT_HOURS));
        assertEquals("1000", mConverterEnergy.Convert("1", KILOJOULES, JOULES));
        assertEquals("6241509126000000000", mConverterEnergy.Convert("1", JOULES, ELECTRON_VOLTS));

        ConverterFuelEconomy mConverterFuelEconomy = new ConverterFuelEconomy(null);
        assertEquals("0.832674185", mConverterFuelEconomy.Convert("1", MILES_PER_GALLON_UK, MILES_PER_GALLON_US));
        assertEquals("0.35400619", mConverterFuelEconomy.Convert("1", MILES_PER_GALLON_UK, KILOMETERS_PER_LITER));
        assertEquals("282.480936332", mConverterFuelEconomy.Convert("1", MILES_PER_GALLON_UK, LITERS_PER_100_KILOMETERS));

        ConverterLength mConverterLength = new ConverterLength(null);
        assertEquals("1.852", mConverterLength.Convert("1", NAUTICAL_MILES, KILOMETERS));
        assertEquals("1760", mConverterLength.Convert("1", MILES, YARDS));
        assertEquals("3", mConverterLength.Convert("1", YARDS, FEET));
        assertEquals("12", mConverterLength.Convert("1", FEET, INCHES));
        assertEquals("1000", mConverterLength.Convert("1", KILOMETERS, METERS));
        assertEquals("100", mConverterLength.Convert("1", METERS, CENTIMETERS));
        assertEquals("10", mConverterLength.Convert("1", CENTIMETERS, MILLIMETERS));
        assertEquals("1000", mConverterLength.Convert("1", MILLIMETERS, MICRONS));

        ConverterPower mConverterPower = new ConverterPower(null);
        assertEquals("52752792631", mConverterPower.Convert("3000000000", BTUS_IT_PER_MINUTE, JOULES_PER_SECOND));
        assertEquals("1", mConverterPower.Convert("33000", FOOT_POUNDS_PER_MINUTE, HORSEPOWER_MECHANICAL));
        assertEquals("1.013869665", mConverterPower.Convert("1", HORSEPOWER_MECHANICAL, PFERDESTARKE));
        assertEquals("746", mConverterPower.Convert("1", HORSEPOWER_ELECTRICAL, JOULES_PER_SECOND));
        assertEquals("1000000000", mConverterPower.Convert("1", GIGAWATTS, JOULES_PER_SECOND));
        assertEquals("1000000", mConverterPower.Convert("1", MEGAWATTS, WATTS));
        assertEquals("1000", mConverterPower.Convert("1", KILOWATTS, WATTS));

        ConverterPressure mConverterPressure = new ConverterPressure(null);
        assertEquals("101325", mConverterPressure.Convert("760", TORRS, PASCALS));
        assertEquals("1000", mConverterPressure.Convert("1", KILOPASCALS, PASCALS));
        assertEquals("1.01325", mConverterPressure.Convert("1", ATMOSPHERES, BARS));
        assertEquals("6.894757293", mConverterPressure.Convert("1", POUNDS_PER_SQUARE_INCH, KILOPASCALS));

        ConverterSpeed mConverterSpeed = new ConverterSpeed(null);
        assertEquals("343", mConverterSpeed.Convert("1", MACH, METERS_PER_SECOND));
        assertEquals("1852", mConverterSpeed.Convert("3600", KNOTS, METERS_PER_SECOND));
        assertEquals("1", mConverterSpeed.Convert("3600", MILES_PER_HOUR, MILES_PER_SECOND));
        assertEquals("5280", mConverterSpeed.Convert("1", MILES_PER_SECOND, FEET_PER_SECOND));
        assertEquals("1", mConverterSpeed.Convert("3600", KILOMETERS_PER_HOUR, KILOMETERS_PER_SECOND));

        ConverterTemperature mConverterTemperature = new ConverterTemperature(null);
        assertEquals("0", mConverterTemperature.Convert("32", FAHRENHEIT, CELSIUS));
        assertEquals("32", mConverterTemperature.Convert("0", CELSIUS, FAHRENHEIT));
        assertEquals("273.15", mConverterTemperature.Convert("0", CELSIUS, KELVIN));
        assertEquals("0", mConverterTemperature.Convert("273.15", KELVIN, CELSIUS));
        assertEquals("273.15", mConverterTemperature.Convert("32", FAHRENHEIT, KELVIN));
        assertEquals("32", mConverterTemperature.Convert("273.15", KELVIN, FAHRENHEIT));

        ConverterTime mConverterTime = new ConverterTime(null);
        assertEquals("365", mConverterTime.Convert("1", YEARS, DAYS));
        assertEquals("7", mConverterTime.Convert("1", WEEKS, DAYS));
        assertEquals("24", mConverterTime.Convert("1", DAYS, HOURS));
        assertEquals("60", mConverterTime.Convert("1", HOURS, MINUTES));
        assertEquals("60", mConverterTime.Convert("1", MINUTES, SECONDS));
        assertEquals("1000", mConverterTime.Convert("1", SECONDS, MILLISECONDS));
        assertEquals("1000", mConverterTime.Convert("1", MILLISECONDS, MICROSECONDS));

        ConverterTorque mConverterTorque =  new ConverterTorque(null);
        assertEquals("12", mConverterTorque.Convert("1", POUND_FEET, POUND_INCHES));
        assertEquals("1", mConverterTorque.Convert("1", JOULES_PER_RADIAN, NEWTON_METERS));
        assertEquals("9.80665", mConverterTorque.Convert("1", KILOGRAM_METERS, JOULES_PER_RADIAN));

        ConverterVolume mConverterVolume = new ConverterVolume(null);
        assertEquals("4", mConverterVolume.Convert("1", GALLONS_UK, QUARTS_UK));
        assertEquals("2", mConverterVolume.Convert("1", QUARTS_UK, PINTS_UK));
        assertEquals("2", mConverterVolume.Convert("1", PINTS_UK, CUPS_UK));
        assertEquals("10", mConverterVolume.Convert("1", CUPS_UK, FLUID_OUNCES_UK));
        assertEquals("8", mConverterVolume.Convert("5", FLUID_OUNCES_UK, TABLESPOONS_UK));
        assertEquals("3", mConverterVolume.Convert("1", TABLESPOONS_UK, TEASPOONS_UK));
        assertEquals("4", mConverterVolume.Convert("1", GALLONS_US, QUARTS_US));
        assertEquals("2", mConverterVolume.Convert("1", QUARTS_US, PINTS_US));
        assertEquals("2", mConverterVolume.Convert("1", PINTS_US, CUPS_US));
        assertEquals("8", mConverterVolume.Convert("1", CUPS_US, FLUID_OUNCES_US));
        assertEquals("2", mConverterVolume.Convert("1", FLUID_OUNCES_US, TABLESPOONS_US));
        assertEquals("3", mConverterVolume.Convert("1", TABLESPOONS_US, TEASPOONS_US));
        assertEquals("27", mConverterVolume.Convert("1", CUBIC_YARDS, CUBIC_FEET));
        assertEquals("1728", mConverterVolume.Convert("1", CUBIC_FEET, CUBIC_INCHES));
        assertEquals("1000000", mConverterVolume.Convert("1", CUBIC_METERS, CUBIC_CENTIMETERS));
        assertEquals("0.001", mConverterVolume.Convert("1", CUBIC_CENTIMETERS, LITERS));
        assertEquals("1", mConverterVolume.Convert("1", CUBIC_CENTIMETERS, MILLILITERS));

        ConverterWeight mConverterWeight = new ConverterWeight(null);
        assertEquals("1.12", mConverterWeight.Convert("1", LONG_TONS_UK, SHORT_TONS_US));
        assertEquals("2240", mConverterWeight.Convert("1", LONG_TONS_UK, POUNDS));
        assertEquals("2000", mConverterWeight.Convert("1", SHORT_TONS_US, POUNDS));
        assertEquals("14", mConverterWeight.Convert("1", STONES, POUNDS));
        assertEquals("16", mConverterWeight.Convert("1", POUNDS, OUNCES));
        assertEquals("1000", mConverterWeight.Convert("1", METRIC_TONNES, KILOGRAMS));
        assertEquals("1", mConverterWeight.Convert("10", HECTOGRAMS, KILOGRAMS));
        assertEquals("10", mConverterWeight.Convert("1", DEKAGRAMS, GRAMS));
        assertEquals("1", mConverterWeight.Convert("10", DECIGRAMS, GRAMS));
        assertEquals("10", mConverterWeight.Convert("1", CENTIGRAMS, MILLIGRAMS));
        assertEquals("1", mConverterWeight.Convert("10", DECIGRAMS, GRAMS));
        assertEquals("1", mConverterWeight.Convert("200", MILLIGRAMS, CARATS));
        assertEquals("1600000", mConverterWeight.Convert("45359237", GRAMS, OUNCES));
    }
}