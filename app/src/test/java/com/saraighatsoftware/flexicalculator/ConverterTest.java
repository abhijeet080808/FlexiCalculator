package com.saraighatsoftware.flexicalculator;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.saraighatsoftware.flexicalculator.ConverterAngle.AngleUnit.*;
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
    }
}