package com.saraighatsoftware.flexicalculator;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Vector;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class CalculatorTest {

    @Test
    public void Evaluate() throws Exception {
        PowerMockito.mockStatic(Log.class); // returns 1 when Log.e() etc is called
        Calculator mCalculator = new Calculator();

        // expected result vs input pairs
        Vector<Pair> tests = new Vector<>();
        tests.add(new Pair("4", "2 + 2"));
        tests.add(new Pair("9", "5 + 2 * 2"));
        tests.add(new Pair("4", "5 / 2 * 2"));
        tests.add(new Pair("2", "( 2 )"));
        tests.add(new Pair("1", "5 / ( 2 * 2 )"));
        tests.add(new Pair("4", "( 2 * 2 )"));
        tests.add(new Pair("12", "5 + ( 2 * 2 + 4 - 1 )"));
        tests.add(new Pair("15", "5 + ( 2 * ( 2 + 4 - 1 ) )"));
        tests.add(new Pair("35", "5 + ( 2 * ( 2 + 4 - 1 ) ) * ( 2 + 1 )"));
        tests.add(new Pair("35", "( 2 * ( 2 + 4 - 1 ) ) * ( 2 + 1 ) + 5"));

        for (Pair test : tests) {
            assertEquals(
                    test.first(),
                    mCalculator.Evaluate(new Vector<>(Arrays.asList(test.second().split(" ")))));
        }
    }
}