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
        tests.add(new Pair<>("5", "7 - 4 + 2")); // (7 - 4) + 2 and not 7 - (4 + 2) left associative
        tests.add(new Pair<>("262144", "4 ^ 3 ^ 2")); // 4 ^ (3 ^ 2) right associative
        tests.add(new Pair<>("2", "sin 90 * 2"));
        tests.add(new Pair<>("0", "cos 90"));
        tests.add(new Pair<>("1", "tan ( 45 + 10 - ( 2 * 5 ) )"));
        tests.add(new Pair<>("2", "log 100"));
        tests.add(new Pair<>("2.995732", "ln 20"));
        tests.add(new Pair<>("100", "500 * 20 %"));
        tests.add(new Pair<>("100", "20 % * 500"));
        tests.add(new Pair<>("125", "5 ! + 5"));
        tests.add(new Pair<>("5", Calculator.SQUARE_ROOT + " 25"));
        tests.add(new Pair<>("25", "5 " + Calculator.SQUARE ));
        tests.add(new Pair<>("200", "100 " + Calculator.LSH + " 1"));
        tests.add(new Pair<>("50", "100 " + Calculator.RSH + " 1"));
        tests.add(new Pair<>("32", "100 " + Calculator.AND + " 50"));
        tests.add(new Pair<>("118", "100 " + Calculator.OR + " 50"));
        tests.add(new Pair<>("86", "100 " + Calculator.XOR + " 50"));

        for (Pair<String, String> test : tests) {
            String input = test.second().
                    replace("/", Calculator.DIVIDE).
                    replace("*", Calculator.MULTIPLY).
                    replace("-", Calculator.SUBTRACT);
            assertEquals(
                    test.first(),
                    mCalculator.Evaluate(
                            new Vector<>(Arrays.asList(input.split(" "))),
                            Calculator.Base.DEC,
                            Calculator.AngularUnit.DEGREE));
        }

        assertEquals("19",
                mCalculator.Evaluate(
                        new Vector<>(Arrays.asList("A + F".split(" "))),
                        Calculator.Base.HEX,
                        Calculator.AngularUnit.DEGREE));
        assertEquals("110",
                mCalculator.Evaluate(
                        new Vector<>(Arrays.asList("30 + 60".split(" "))),
                        Calculator.Base.OCT,
                        Calculator.AngularUnit.DEGREE));
        assertEquals("10101",
                mCalculator.Evaluate(
                        new Vector<>(Arrays.asList("1010 + 1011".split(" "))),
                        Calculator.Base.BIN,
                        Calculator.AngularUnit.DEGREE));

        assertEquals("5",
                mCalculator.Evaluate(
                        new Vector<>(Arrays.asList((Calculator.SUBTRACT + "A + F").split(" "))),
                        Calculator.Base.HEX,
                        Calculator.AngularUnit.DEGREE));
        assertEquals("30",
                mCalculator.Evaluate(
                        new Vector<>(Arrays.asList((Calculator.SUBTRACT + "30 + 60").split(" "))),
                        Calculator.Base.OCT,
                        Calculator.AngularUnit.DEGREE));
        assertEquals("1",
                mCalculator.Evaluate(
                        new Vector<>(Arrays.asList((Calculator.SUBTRACT + "1010 + 1011").split(" "))),
                        Calculator.Base.BIN,
                        Calculator.AngularUnit.DEGREE));

        assertEquals(Calculator.SUBTRACT  + "3F",
                mCalculator.Evaluate(
                        new Vector<>(Arrays.asList(
                                (Calculator.SUBTRACT + "FF " + Calculator.DIVIDE + " 4").split(" "))),
                        Calculator.Base.HEX,
                        Calculator.AngularUnit.DEGREE));
        assertEquals(Calculator.SUBTRACT  + "77",
                mCalculator.Evaluate(
                        new Vector<>(Arrays.asList(
                                (Calculator.SUBTRACT + "377 " + Calculator.DIVIDE + " 4").split(" "))),
                        Calculator.Base.OCT,
                        Calculator.AngularUnit.DEGREE));
        assertEquals(Calculator.SUBTRACT  + "111111",
                mCalculator.Evaluate(
                        new Vector<>(Arrays.asList(
                                (Calculator.SUBTRACT + "11111111 " + Calculator.DIVIDE + " 100").split(" "))),
                        Calculator.Base.BIN,
                        Calculator.AngularUnit.DEGREE));

        assertEquals("0.707107",
                mCalculator.Evaluate(
                        new Vector<>(Arrays.asList(("sin 45").split(" "))),
                        Calculator.Base.DEC,
                        Calculator.AngularUnit.DEGREE));
        assertEquals("0.707107",
                mCalculator.Evaluate(
                        new Vector<>(Arrays.asList(("cos 45").split(" "))),
                        Calculator.Base.DEC,
                        Calculator.AngularUnit.DEGREE));
        assertEquals("1",
                mCalculator.Evaluate(
                        new Vector<>(Arrays.asList(("tan 45").split(" "))),
                        Calculator.Base.DEC,
                        Calculator.AngularUnit.DEGREE));

        assertEquals("0.850904",
                mCalculator.Evaluate(
                        new Vector<>(Arrays.asList(("sin 45").split(" "))),
                        Calculator.Base.DEC,
                        Calculator.AngularUnit.RADIAN));
        assertEquals("0.525322",
                mCalculator.Evaluate(
                        new Vector<>(Arrays.asList(("cos 45").split(" "))),
                        Calculator.Base.DEC,
                        Calculator.AngularUnit.RADIAN));
        assertEquals("1.619775",
                mCalculator.Evaluate(
                        new Vector<>(Arrays.asList(("tan 45").split(" "))),
                        Calculator.Base.DEC,
                        Calculator.AngularUnit.RADIAN));
    }

    @Test
    public void IsAllowed() {
        PowerMockito.mockStatic(Log.class); // returns 1 when Log.e() etc is called

        // input pairs
        Vector<Pair<String, Character>> pass_tests = new Vector<>();
        pass_tests.add(new Pair<>("0.2", '1'));
        pass_tests.add(new Pair<>("22", '1'));
        pass_tests.add(new Pair<>("0", '1'));
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
        pass_tests.add(new Pair<>("0.", '0'));
        pass_tests.add(new Pair<>(".", '0'));
        pass_tests.add(new Pair<>("-0.", '0'));
        pass_tests.add(new Pair<>("-.", '0'));

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
        fail_test.add(new Pair<>("0", '0'));

        Calculator mCalculator = new Calculator();

        for (Pair<String, Character> test : pass_tests) {
            assertEquals(true,
                    mCalculator.IsOperandAllowed(
                            test.first().replace("-", Calculator.SUBTRACT),
                            Calculator.Base.DEC,
                            test.second() == '-' ? Calculator.SUBTRACT_CHAR : test.second()));
        }

        for (Pair<String, Character> test : fail_test) {
            assertEquals(false,
                    mCalculator.IsOperandAllowed(
                            test.first().replace("-", Calculator.SUBTRACT),
                            Calculator.Base.DEC,
                            test.second() == '-' ? Calculator.SUBTRACT_CHAR : test.second()));
        }

        assertEquals(false, mCalculator.IsOperandAllowed("10", Calculator.Base.DEC, 'A'));
        assertEquals(true, mCalculator.IsOperandAllowed("10", Calculator.Base.HEX, 'A'));
        assertEquals(false, mCalculator.IsOperandAllowed("10", Calculator.Base.OCT, '8'));
        assertEquals(true, mCalculator.IsOperandAllowed("10", Calculator.Base.OCT, '5'));
        assertEquals(false, mCalculator.IsOperandAllowed("10", Calculator.Base.DEC, 'A'));
        assertEquals(true, mCalculator.IsOperandAllowed("10", Calculator.Base.DEC, '1'));
        assertEquals(false, mCalculator.IsOperandAllowed("10", Calculator.Base.BIN, '2'));
        assertEquals(true, mCalculator.IsOperandAllowed("10", Calculator.Base.BIN, '1'));

    }

    @Test
    public void Convert() {
        PowerMockito.mockStatic(Log.class); // returns 1 when Log.e() etc is called

        Calculator mCalc = new Calculator();

        try {
            assertEquals("4106", mCalc.Convert("100A", Calculator.Base.HEX, Calculator.Base.DEC));
            assertEquals("10012", mCalc.Convert("100A", Calculator.Base.HEX, Calculator.Base.OCT));
            assertEquals("1000000001010", mCalc.Convert("100A", Calculator.Base.HEX, Calculator.Base.BIN));
            assertEquals("100A", mCalc.Convert("100A", Calculator.Base.HEX, Calculator.Base.HEX));

            assertEquals("144", mCalc.Convert("100.5", Calculator.Base.DEC, Calculator.Base.OCT));
            assertEquals("1100100", mCalc.Convert("100.5", Calculator.Base.DEC, Calculator.Base.BIN));
            assertEquals("64", mCalc.Convert("100.5", Calculator.Base.DEC, Calculator.Base.HEX));
            assertEquals("100.5", mCalc.Convert("100.5", Calculator.Base.DEC, Calculator.Base.DEC));

            assertEquals("0", mCalc.Convert(".5", Calculator.Base.DEC, Calculator.Base.OCT));
            assertEquals("0", mCalc.Convert(".5", Calculator.Base.DEC, Calculator.Base.BIN));
            assertEquals("0", mCalc.Convert(".5", Calculator.Base.DEC, Calculator.Base.HEX));
            assertEquals(".5", mCalc.Convert(".5", Calculator.Base.DEC, Calculator.Base.DEC));

            assertEquals("1000000", mCalc.Convert("100", Calculator.Base.OCT, Calculator.Base.BIN));
            assertEquals("40", mCalc.Convert("100", Calculator.Base.OCT, Calculator.Base.HEX));
            assertEquals("64", mCalc.Convert("100", Calculator.Base.OCT, Calculator.Base.DEC));
            assertEquals("100", mCalc.Convert("100", Calculator.Base.OCT, Calculator.Base.OCT));

            assertEquals("A", mCalc.Convert("1010", Calculator.Base.BIN, Calculator.Base.HEX));
            assertEquals("10", mCalc.Convert("1010", Calculator.Base.BIN, Calculator.Base.DEC));
            assertEquals("12", mCalc.Convert("1010", Calculator.Base.BIN, Calculator.Base.OCT));
            assertEquals("1010", mCalc.Convert("1010", Calculator.Base.BIN, Calculator.Base.BIN));
        } catch (Exception e) {
            assertEquals(false, true);
        }
    }
}