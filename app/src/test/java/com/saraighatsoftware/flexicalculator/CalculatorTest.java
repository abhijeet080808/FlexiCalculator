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
        Vector<Pair<String, String>> tests = new Vector<>();
        tests.add(new Pair<>("4", "2 + 2"));
        tests.add(new Pair<>("9", "5 + 2 * 2"));
        tests.add(new Pair<>("5", "5 / 2 * 2"));
        tests.add(new Pair<>("2", "( 2 )"));
        tests.add(new Pair<>("1.25", "5 / ( 2 * 2 )"));
        tests.add(new Pair<>("4", "( 2 * 2 )"));
        tests.add(new Pair<>("12", "5 + ( 2 * 2 + 4 - 1 )"));
        tests.add(new Pair<>("15", "5 + ( 2 * ( 2 + 4 - 1 ) )"));
        tests.add(new Pair<>("35", "5 + ( 2 * ( 2 + 4 - 1 ) ) * ( 2 + 1 )"));
        tests.add(new Pair<>("35", "( 2 * ( 2 + 4 - 1 ) ) * ( 2 + 1 ) + 5"));
        tests.add(new Pair<>("3.142857", "( 22 / 7 )"));
        tests.add(new Pair<>("4.714286", "( 33 / 7 )"));
        tests.add(new Pair<>("1.1", ".1 + 1"));
        tests.add(new Pair<>("1.1", "1 + .1"));
        tests.add(new Pair<>("1.1", "0.1 + 1"));
        tests.add(new Pair<>("1.1", "1 + 0.1"));
        tests.add(new Pair<>("2", "4 - 2"));
        tests.add(new Pair<>("4", "2 - ( -2 )"));
        tests.add(new Pair<>("4", "-2 + 6"));
        tests.add(new Pair<>("4", "6 + -2"));

        for (Pair<String, String> test : tests) {
            String input = test.second().
                    replace("/", Calculator.DIVIDE).
                    replace("*", Calculator.MULTIPLY).
                    replace("-", Calculator.SUBTRACT);
            assertEquals(
                    test.first(),
                    mCalculator.Evaluate(new Vector<>(Arrays.asList(input.split(" ")))));
        }
    }

    @Test
    public void IsAllowed() {
        PowerMockito.mockStatic(Log.class); // returns 1 when Log.e() etc is called

        // input pairs
        Vector<Pair<String, Character>> pass_tests = new Vector<>();
        pass_tests.add(new Pair<>("0.2", '1'));
        pass_tests.add(new Pair<>("22", '1'));
        pass_tests.add(new Pair<>("00", '1'));
        pass_tests.add(new Pair<>(".2", '1'));
        pass_tests.add(new Pair<>(".21", '1'));
        pass_tests.add(new Pair<>("-0.", '1'));
        pass_tests.add(new Pair<>("-.2", '1'));
        pass_tests.add(new Pair<>("-2", '1'));
        pass_tests.add(new Pair<>("-22", '1'));
        pass_tests.add(new Pair<>("-22", '.'));
        pass_tests.add(new Pair<>("-22.", '1'));
        pass_tests.add(new Pair<>("-", '1'));
        pass_tests.add(new Pair<>(".", '1'));
        pass_tests.add(new Pair<>("2", '1'));
        pass_tests.add(new Pair<>("-", '.'));
        pass_tests.add(new Pair<>("", '-'));
        pass_tests.add(new Pair<>("", '.'));
        pass_tests.add(new Pair<>("", '0'));
        pass_tests.add(new Pair<>("1", '.'));
        pass_tests.add(new Pair<>("12345678901", '0'));
        pass_tests.add(new Pair<>("123456789.01", '0'));
        pass_tests.add(new Pair<>("123456789012", '.'));
        pass_tests.add(new Pair<>("0.12345", '0'));
        pass_tests.add(new Pair<>("-12345678901", '0'));
        pass_tests.add(new Pair<>("-123456789.01", '0'));
        pass_tests.add(new Pair<>("-123456789012", '.'));

        // input pairs
        Vector<Pair<String, Character>> fail_test = new Vector<>();
        fail_test.add(new Pair<>("1.", '.'));
        fail_test.add(new Pair<>("1.", '-'));
        fail_test.add(new Pair<>(".", '-'));
        fail_test.add(new Pair<>("-", '-'));
        fail_test.add(new Pair<>("12", '-'));
        fail_test.add(new Pair<>("-.", '.'));
        fail_test.add(new Pair<>("123456789012", '0'));
        fail_test.add(new Pair<>("123456789.012", '0'));
        fail_test.add(new Pair<>(".123456", '0'));
        fail_test.add(new Pair<>("-123456789012", '0'));
        fail_test.add(new Pair<>("-123456789.012", '0'));
        fail_test.add(new Pair<>("12", '+'));
        fail_test.add(new Pair<>("+", '1'));
        fail_test.add(new Pair<>("1-", '1'));

        for (Pair<String, Character> test : pass_tests) {
            assertEquals(true,
                    Calculator.IsOperandAllowed(
                            test.first().replace("-", Calculator.SUBTRACT),
                            test.second() == '-' ? Calculator.SUBTRACT_CHAR : test.second()));
        }

        for (Pair<String, Character> test : fail_test) {
            assertEquals(false,
                    Calculator.IsOperandAllowed(
                            test.first().replace("-", Calculator.SUBTRACT),
                            test.second() == '-' ? Calculator.SUBTRACT_CHAR : test.second()));
        }

    }
}