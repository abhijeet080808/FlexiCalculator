package com.saraighatsoftware.flexicalculator;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.saraighatsoftware.flexicalculator.ConverterAngle.AngleUnit.*;
import static com.saraighatsoftware.flexicalculator.ConverterArea.AreaUnit.*;
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

    }
}