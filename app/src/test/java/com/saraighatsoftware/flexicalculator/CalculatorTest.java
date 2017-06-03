package com.saraighatsoftware.flexicalculator;

import android.util.Log;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class CalculatorTest {

    @Test
    public void Evaluate() throws Exception {
        PowerMockito.mockStatic(Log.class); // returns 1 when Log.e() etc is called
        Calculator mCalculator = new Calculator();

        // expected result vs input pairs
        ArrayList<Pair<String, String>> tests = new ArrayList<>();
        tests.add(Pair.of("4", "2 + 2"));
        tests.add(Pair.of("9", "5 + 2 * 2"));
        tests.add(Pair.of("5", "5 / 2 * 2"));
        tests.add(Pair.of("2", "( 2 )"));
        tests.add(Pair.of("1.25", "5 / ( 2 * 2 )"));
        tests.add(Pair.of("4", "( 2 * 2 )"));
        tests.add(Pair.of("12", "5 + ( 2 * 2 + 4 - 1 )"));
        tests.add(Pair.of("15", "5 + ( 2 * ( 2 + 4 - 1 ) )"));
        tests.add(Pair.of("35", "5 + ( 2 * ( 2 + 4 - 1 ) ) * ( 2 + 1 )"));
        tests.add(Pair.of("35", "( 2 * ( 2 + 4 - 1 ) ) * ( 2 + 1 ) + 5"));
        tests.add(Pair.of("3.142857", "( 22 / 7 )"));
        tests.add(Pair.of("4.714286", "( 33 / 7 )"));
        tests.add(Pair.of("1.1", ".1 + 1"));
        tests.add(Pair.of("1.1", "1 + .1"));
        tests.add(Pair.of("1.1", "0.1 + 1"));
        tests.add(Pair.of("1.1", "1 + 0.1"));
        tests.add(Pair.of("2", "4 - 2"));
        tests.add(Pair.of("4", "2 - ( -2 )"));
        tests.add(Pair.of("4", "-2 + 6"));
        tests.add(Pair.of("4", "6 + -2"));
        tests.add(Pair.of("5", "7 - 4 + 2")); // (7 - 4) + 2 and not 7 - (4 + 2) left associative
        tests.add(Pair.of("262144", "4 ^ 3 ^ 2")); // 4 ^ (3 ^ 2) right associative
        tests.add(Pair.of("2", "sin 90 * 2"));
        tests.add(Pair.of("0", "cos 90"));
        tests.add(Pair.of("1", "tan ( 45 + 10 - ( 2 * 5 ) )"));
        tests.add(Pair.of("2", "log 100"));
        tests.add(Pair.of("2.995732", "ln 20"));
        tests.add(Pair.of("100", "500 * 20 %"));
        tests.add(Pair.of("100", "20 % * 500"));
        tests.add(Pair.of("125", "5 ! + 5"));
        tests.add(Pair.of("5", Calculator.SQUARE_ROOT + " 25"));
        tests.add(Pair.of("25", "5 " + Calculator.SQUARE ));
        tests.add(Pair.of("200", "100 " + Calculator.LSH + " 1"));
        tests.add(Pair.of("50", "100 " + Calculator.RSH + " 1"));
        tests.add(Pair.of("32", "100 " + Calculator.AND + " 50"));
        tests.add(Pair.of("118", "100 " + Calculator.OR + " 50"));
        tests.add(Pair.of("86", "100 " + Calculator.XOR + " 50"));
        tests.add(Pair.of("3", "10 " + Calculator.MODULUS + " 7"));
        tests.add(Pair.of("3", "10 " + Calculator.MODULUS + " -7"));
        tests.add(Pair.of("-3", "-10 " + Calculator.MODULUS + " -7"));
        tests.add(Pair.of("-3", "-10 " + Calculator.MODULUS + " 7"));
        tests.add(Pair.of("30414093201713378043612608166064768844377641568960512000000000000",
                "50 " + Calculator.FACTORIAL));
        tests.add(Pair.of("7886578673647905035523632139321850622951359776871732632947425332443594" +
                        "499634033429203042840119846239041772121389196388302576427902426371050619" +
                        "266249528299311134628572707633172373969889439224456214516642402540332918" +
                        "641312274282948532775242424075739032403212574055795686602260319041703240" +
                        "623517008587961789222227896237038973747200000000000000000000000000000000" +
                        "00000000000000000",
                "200 " + Calculator.FACTORIAL));
        tests.add(Pair.of("1024", "2 " + Calculator.POWER + " 10"));
        tests.add(Pair.of("1267650600228229401496703205376", "2 " + Calculator.POWER + " 100"));
        tests.add(Pair.of("1000000000000000000000000000000000000000000000000000000000000000000000" +
                        "000000000000000000000000000000000000000000000000000000000000000000000000" +
                        "000000000000000000000000000000000000000000000000000000000000000000000000" +
                        "000000000000000000000000000000000000000000000000000000000000000000000000" +
                        "000000000000000000000000000000000000000000000000000000000000000000000000" +
                        "000000000000000000000000000000000000000000000000000000000000000000000000" +
                        "00000000000000000000000000000000000000000000000000000000000000000000000",
                "10 " + Calculator.POWER + " 500"));
        tests.add(Pair.of("1.414214", "2 " + Calculator.POWER + " 0.5"));
        tests.add(Pair.of("45.254834", "2 " + Calculator.POWER + " 5.5"));
        tests.add(Pair.of("0.176777", "2 " + Calculator.POWER + " -2.5"));
        tests.add(Pair.of("0.25", "2 " + Calculator.POWER + " -2"));
        tests.add(Pair.of("0.25", "-2 " + Calculator.POWER + " -2"));
        tests.add(Pair.of("4", "-2 " + Calculator.POWER + " 2"));
        tests.add(Pair.of("0.125", "2 " + Calculator.POWER + " -3"));
        tests.add(Pair.of("-0.125", "-2 " + Calculator.POWER + " -3"));
        tests.add(Pair.of("-8", "-2 " + Calculator.POWER + " 3"));

        for (Pair<String, String> test : tests) {
            String input = test.getRight().
                    replace("/", Calculator.DIVIDE).
                    replace("*", Calculator.MULTIPLY).
                    replace("-", Calculator.SUBTRACT);
            assertEquals(
                    test.getLeft().replace("-", Calculator.SUBTRACT),
                    mCalculator.Evaluate(
                            new ArrayList<>(Arrays.asList(input.split(" "))),
                            Calculator.Base.DEC,
                            Calculator.AngularUnit.DEGREE));
        }

        assertEquals("19",
                mCalculator.Evaluate(
                        new ArrayList<>(Arrays.asList("A + F".split(" "))),
                        Calculator.Base.HEX,
                        Calculator.AngularUnit.DEGREE));
        assertEquals("110",
                mCalculator.Evaluate(
                        new ArrayList<>(Arrays.asList("30 + 60".split(" "))),
                        Calculator.Base.OCT,
                        Calculator.AngularUnit.DEGREE));
        assertEquals("10101",
                mCalculator.Evaluate(
                        new ArrayList<>(Arrays.asList("1010 + 1011".split(" "))),
                        Calculator.Base.BIN,
                        Calculator.AngularUnit.DEGREE));

        assertEquals("5",
                mCalculator.Evaluate(
                        new ArrayList<>(Arrays.asList((Calculator.SUBTRACT + "A + F").split(" "))),
                        Calculator.Base.HEX,
                        Calculator.AngularUnit.DEGREE));
        assertEquals("30",
                mCalculator.Evaluate(
                        new ArrayList<>(Arrays.asList((Calculator.SUBTRACT + "30 + 60").split(" "))),
                        Calculator.Base.OCT,
                        Calculator.AngularUnit.DEGREE));
        assertEquals("1",
                mCalculator.Evaluate(
                        new ArrayList<>(Arrays.asList((Calculator.SUBTRACT + "1010 + 1011").split(" "))),
                        Calculator.Base.BIN,
                        Calculator.AngularUnit.DEGREE));

        assertEquals(Calculator.SUBTRACT  + "3F",
                mCalculator.Evaluate(
                        new ArrayList<>(Arrays.asList(
                                (Calculator.SUBTRACT + "FF " + Calculator.DIVIDE + " 4").split(" "))),
                        Calculator.Base.HEX,
                        Calculator.AngularUnit.DEGREE));
        assertEquals(Calculator.SUBTRACT  + "77",
                mCalculator.Evaluate(
                        new ArrayList<>(Arrays.asList(
                                (Calculator.SUBTRACT + "377 " + Calculator.DIVIDE + " 4").split(" "))),
                        Calculator.Base.OCT,
                        Calculator.AngularUnit.DEGREE));
        assertEquals(Calculator.SUBTRACT  + "111111",
                mCalculator.Evaluate(
                        new ArrayList<>(Arrays.asList(
                                (Calculator.SUBTRACT + "11111111 " + Calculator.DIVIDE + " 100").split(" "))),
                        Calculator.Base.BIN,
                        Calculator.AngularUnit.DEGREE));

        assertEquals("0.707107",
                mCalculator.Evaluate(
                        new ArrayList<>(Arrays.asList(("sin 45").split(" "))),
                        Calculator.Base.DEC,
                        Calculator.AngularUnit.DEGREE));
        assertEquals("0.707107",
                mCalculator.Evaluate(
                        new ArrayList<>(Arrays.asList(("cos 45").split(" "))),
                        Calculator.Base.DEC,
                        Calculator.AngularUnit.DEGREE));
        assertEquals("1",
                mCalculator.Evaluate(
                        new ArrayList<>(Arrays.asList(("tan 45").split(" "))),
                        Calculator.Base.DEC,
                        Calculator.AngularUnit.DEGREE));

        assertEquals("0.850904",
                mCalculator.Evaluate(
                        new ArrayList<>(Arrays.asList(("sin 45").split(" "))),
                        Calculator.Base.DEC,
                        Calculator.AngularUnit.RADIAN));
        assertEquals("0.525322",
                mCalculator.Evaluate(
                        new ArrayList<>(Arrays.asList(("cos 45").split(" "))),
                        Calculator.Base.DEC,
                        Calculator.AngularUnit.RADIAN));
        assertEquals("1.619775",
                mCalculator.Evaluate(
                        new ArrayList<>(Arrays.asList(("tan 45").split(" "))),
                        Calculator.Base.DEC,
                        Calculator.AngularUnit.RADIAN));
    }

    @Test
    public void IsAllowed() {
        PowerMockito.mockStatic(Log.class); // returns 1 when Log.e() etc is called

        // input pairs
        ArrayList<Pair<String, Character>> pass_tests = new ArrayList<>();
        pass_tests.add(Pair.of("0.2", '1'));
        pass_tests.add(Pair.of("22", '1'));
        pass_tests.add(Pair.of("0", '1'));
        pass_tests.add(Pair.of(".2", '1'));
        pass_tests.add(Pair.of(".21", '1'));
        pass_tests.add(Pair.of("-0.", '1'));
        pass_tests.add(Pair.of("-.2", '1'));
        pass_tests.add(Pair.of("-2", '1'));
        pass_tests.add(Pair.of("-22", '1'));
        pass_tests.add(Pair.of("-22", '.'));
        pass_tests.add(Pair.of("-22.", '1'));
        pass_tests.add(Pair.of("-", '1'));
        pass_tests.add(Pair.of(".", '1'));
        pass_tests.add(Pair.of("2", '1'));
        pass_tests.add(Pair.of("-", '.'));
        pass_tests.add(Pair.of("", '-'));
        pass_tests.add(Pair.of("", '.'));
        pass_tests.add(Pair.of("", '0'));
        pass_tests.add(Pair.of("1", '.'));
        pass_tests.add(Pair.of("12345678901", '0'));
        pass_tests.add(Pair.of("123456789.01", '0'));
        pass_tests.add(Pair.of("123456789012", '.'));
        pass_tests.add(Pair.of("0.12345", '0'));
        pass_tests.add(Pair.of("-12345678901", '0'));
        pass_tests.add(Pair.of("-123456789.01", '0'));
        pass_tests.add(Pair.of("-123456789012", '.'));
        pass_tests.add(Pair.of("0.", '0'));
        pass_tests.add(Pair.of(".", '0'));
        pass_tests.add(Pair.of("-0.", '0'));
        pass_tests.add(Pair.of("-.", '0'));

        // input pairs
        ArrayList<Pair<String, Character>> fail_test = new ArrayList<>();
        fail_test.add(Pair.of("1.", '.'));
        fail_test.add(Pair.of("1.", '-'));
        fail_test.add(Pair.of(".", '-'));
        fail_test.add(Pair.of("-", '-'));
        fail_test.add(Pair.of("12", '-'));
        fail_test.add(Pair.of("-.", '.'));
        fail_test.add(Pair.of("123456789012", '0'));
        fail_test.add(Pair.of("123456789.012", '0'));
        fail_test.add(Pair.of(".123456", '0'));
        fail_test.add(Pair.of("-123456789012", '0'));
        fail_test.add(Pair.of("-123456789.012", '0'));
        fail_test.add(Pair.of("12", '+'));
        fail_test.add(Pair.of("+", '1'));
        fail_test.add(Pair.of("1-", '1'));
        fail_test.add(Pair.of("0", '0'));

        Calculator mCalculator = new Calculator();

        for (Pair<String, Character> test : pass_tests) {
            assertEquals(true,
                    mCalculator.IsOperandAllowed(
                            test.getLeft().replace("-", Calculator.SUBTRACT),
                            Calculator.Base.DEC,
                            test.getRight() == '-' ? Calculator.SUBTRACT_CHAR : test.getRight()));
        }

        for (Pair<String, Character> test : fail_test) {
            assertEquals(false,
                    mCalculator.IsOperandAllowed(
                            test.getLeft().replace("-", Calculator.SUBTRACT),
                            Calculator.Base.DEC,
                            test.getRight() == '-' ? Calculator.SUBTRACT_CHAR : test.getRight()));
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