package com.saraighatsoftware.flexicalculator;

import android.util.Log;

import org.apache.commons.math3.util.CombinatoricsUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;

class Calculator {

    private static final String TAG = "Calculator";

    private enum OperatorType {
        BINARY, PRE_UNARY, POST_UNARY, OTHER
    }

    private class OperatorInfo {
        OperatorInfo(String representation, OperatorType type, int precedence, boolean isRightAssoc) {
            mRepresentation = representation;
            mType = type;
            mPrecedence = precedence;
            mIsRightAssoc = isRightAssoc;
        }

        String mRepresentation;
        OperatorType mType;
        int mPrecedence;
        boolean mIsRightAssoc;
    }

    static final String OPEN_BRACKET = "(";
    static final String CLOSE_BRACKET = ")";
    static final String SIN = "sin";
    static final String COS = "cos";
    static final String TAN = "tan";
    static final String LOG = "log";
    static final String LN = "ln";
    static final String PERCENTAGE = "%";
    static final String FACTORIAL = "!";
    static final String SQUARE_ROOT = "\u221a";
    static final String SQUARE = "\u00b2";
    static final String CUBE = "\u00b3";
    static final String POWER = "^";
    static final String DIVIDE = "\u00f7";
    static final String MULTIPLY = "\u00d7";
    static final String MODULUS = "mod";
    static final String SUBTRACT = "\u2212";
    static final String ADD = "+";
    static final String DECIMAL = ".";

    static final char DIVIDE_CHAR = '\u00f7';
    static final char MULTIPLY_CHAR = '\u00d7';
    static final char SUBTRACT_CHAR = '\u2212';
    private static final char DECIMAL_CHAR = '.';

    private static final int INTERNAL_SCALE = 12;
    private static final int RESULT_SCALE = 6;
    // Precision is the total number of digits
    private static final int INPUT_PRECISION = 12;
    // Scale is the number of digits after the decimal point
    private static final int INPUT_SCALE = 6;

    private final HashMap<String, OperatorInfo> mOperators;
    private final DecimalFormat mResultFormat;

    Calculator() {
        mOperators = new HashMap<>();
        mOperators.put(OPEN_BRACKET, new OperatorInfo(OPEN_BRACKET, OperatorType.OTHER, 1, false));
        mOperators.put(CLOSE_BRACKET, new OperatorInfo(CLOSE_BRACKET, OperatorType.OTHER, 1, false));
        mOperators.put(SIN, new OperatorInfo(SIN, OperatorType.PRE_UNARY, 2, false));
        mOperators.put(COS, new OperatorInfo(COS, OperatorType.PRE_UNARY, 2, false));
        mOperators.put(TAN, new OperatorInfo(TAN, OperatorType.PRE_UNARY, 2, false));
        mOperators.put(LOG, new OperatorInfo(LOG, OperatorType.PRE_UNARY, 2, false));
        mOperators.put(LN, new OperatorInfo(LN, OperatorType.PRE_UNARY, 2, false));
        mOperators.put(PERCENTAGE, new OperatorInfo(PERCENTAGE, OperatorType.POST_UNARY, 2, false));
        mOperators.put(FACTORIAL, new OperatorInfo(FACTORIAL, OperatorType.POST_UNARY, 2, false));
        mOperators.put(SQUARE_ROOT, new OperatorInfo(SQUARE_ROOT, OperatorType.PRE_UNARY, 2, false));
        mOperators.put(SQUARE, new OperatorInfo(SQUARE, OperatorType.POST_UNARY, 2, false));
        mOperators.put(CUBE, new OperatorInfo(CUBE, OperatorType.POST_UNARY, 2, false));
        // power is the only right associative operator
        mOperators.put(POWER, new OperatorInfo(POWER, OperatorType.BINARY, 3, true));
        mOperators.put(DIVIDE, new OperatorInfo(DIVIDE, OperatorType.BINARY, 4, false));
        mOperators.put(MULTIPLY, new OperatorInfo(MULTIPLY, OperatorType.BINARY, 4, false));
        mOperators.put(MODULUS, new OperatorInfo(MODULUS, OperatorType.BINARY, 4, false));
        mOperators.put(SUBTRACT, new OperatorInfo(SUBTRACT, OperatorType.BINARY, 5, false));
        mOperators.put(ADD, new OperatorInfo(ADD, OperatorType.BINARY, 5, false));

        mResultFormat = new DecimalFormat();
        mResultFormat.setMaximumFractionDigits(RESULT_SCALE);
        mResultFormat.setMinimumFractionDigits(0);
        mResultFormat.setGroupingUsed(false);
        mResultFormat.setRoundingMode(RoundingMode.HALF_EVEN);
    }

    /*
    https://en.wikipedia.org/wiki/Shunting-yard_algorithm
    http://wcipeg.com/wiki/Shunting_yard_algorithm
    http://www.geeksforgeeks.org/expression-evaluation/

    Shunting Yard Algorithm by Edgar Dijkstra
    -----------------------------------------
    1. While there are still tokens to be read in,
        1.1 Get the next token.
        1.2 If the token is:
            1.2.1 A number: push it onto the value stack.
            1.2.2 A variable: get its value, and push onto the value stack.
            1.2.3 A left parenthesis "(": push it onto the operator stack.
            1.2.4 A right parenthesis ")":
                1 While the thing on top of the operator stack is not a left parenthesis,
                    1 Pop the operator from the operator stack.
                    2 Pop the value stack twice, getting two operands.
                    3 Apply the operator to the operands, in the correct order.
                    4 Push the result onto the value stack.
                2 Pop the left parenthesis from the operator stack, and discard it.
            1.2.5 An operator (call it thisOp):
                1 While the operator stack is not empty, and the top operator on the operator stack has
                the same or greater precedence as thisOp (if this is left associative) or the top
                operator has greater precedence than thisOp (if it is right associative) -
                    1 Pop the operator from the operator stack.
                    2 Pop the value stack twice, getting two operands.
                    3 Apply the operator to the operands, in the correct order.
                    4 Push the result onto the value stack.
                2 Push thisOp onto the operator stack.
    2. While the operator stack is not empty,
        1 Pop the operator from the operator stack.
        2 Pop the value stack twice, getting two operands.
        3 Apply the operator to the operands, in the correct order.
        4 Push the result onto the value stack.
    3. At this point the operator stack should be empty, and the value stack should have only one
    value in it, which is the final result.

    Left associativity - 7 - 4 + 2 = (7 - 4) + 2
    Right associativity - 1 ^ 2 ^ 3 = 1 ^ (2 ^ 3)
    */

    String Evaluate(final Vector<String> infixExpression) throws Exception {
        // evaluate something like [30, +, 2]
        // each vector element either contains a digit in string form
        // or contains a operator in string form

        if (!IsSane(infixExpression, true)) {
            // invalid input
            throw new IllegalArgumentException(
                    "Calculator::Evaluate: Invalid expression " + infixExpression.toString());
        }

        Stack<BigDecimal> operands = new Stack<>();  // for numbers
        Stack<String> operators = new Stack<>(); // for operators and parenthesis

        for (String item : infixExpression) {
            if (IsOperand(item)) {
                Log.v(TAG, "Processing " + item);
                operands.push(new BigDecimal(item.replace(SUBTRACT_CHAR, '-')));
            } else if (item.equals(OPEN_BRACKET)) {
                Log.v(TAG, "Processing " + item);
                operators.push(item);
            } else if (item.equals(CLOSE_BRACKET)) {
                Log.v(TAG, "Processing " + item);
                while (!operators.peek().equals(OPEN_BRACKET)) {
                    String operator = operators.pop();
                    if (IsPreUnaryOperator(operator) || IsPostUnaryOperator(operator)) {
                        BigDecimal operand = operands.pop();
                        Log.v(TAG, "Got close bracket - "
                                + operator + " " + operand);
                        operands.push(operate(operator, operand));
                    } else {
                        BigDecimal operand2 = operands.pop();
                        BigDecimal operand1 = operands.pop();
                        Log.v(TAG, "Got close bracket - "
                                + operand1 + " " + operator + " " + operand2);
                        operands.push(operate(operator, operand1, operand2));
                    }
                }
                operators.pop(); // discard the open bracket
            } else { // it is an operator
                // process all operators with higher or same precedence as current operator
                Log.v(TAG, "Processing " + item);
                while (!operators.isEmpty() &&
                        ((!mOperators.get(item).mIsRightAssoc &&
                                mOperators.get(operators.peek()).mPrecedence <= mOperators.get(item).mPrecedence)
                        || (!mOperators.get(item).mIsRightAssoc &&
                                mOperators.get(operators.peek()).mPrecedence < mOperators.get(item).mPrecedence))) {
                    // lower the precedence value, higher is the precedence

                    if (operators.peek().equals(OPEN_BRACKET) ||
                            operators.peek().equals(CLOSE_BRACKET)) {
                        break;
                    }

                    String operator = operators.pop();
                    if (IsPreUnaryOperator(operator) || IsPostUnaryOperator(operator)) {
                        BigDecimal operand = operands.pop();
                        Log.v(TAG, "Got higher precedence than " + item + " - "
                                + operator + " " + operand);
                        operands.push(operate(operator, operand));
                    } else {
                        BigDecimal operand2 = operands.pop();
                        BigDecimal operand1 = operands.pop();
                        Log.v(TAG, "Got higher precedence than " + item + " - "
                                + operand1 + " " + operator + " " + operand2);
                        operands.push(operate(operator, operand1, operand2));
                    }
                }
                operators.push(item);
            }
        }

        while (!operators.isEmpty()) {
            String operator = operators.pop();
            if (IsPreUnaryOperator(operator) || IsPostUnaryOperator(operator)) {
                BigDecimal operand = operands.pop();
                Log.v(TAG, "Got operator in stack - "
                        + operator + " " + operand);
                operands.push(operate(operator, operand));
            } else {
                BigDecimal operand2 = operands.pop();
                BigDecimal operand1 = operands.pop();
                Log.v(TAG, "Got operator in stack - "
                        + operand1 + " " + operator + " " + operand2);
                operands.push(operate(operator, operand1, operand2));
            }
        }

        if (operands.size() != 1) {
            throw new IllegalStateException("Calculator::Evaluate: Invalid state");
        }

        return mResultFormat.format(operands.firstElement()).replace('-', SUBTRACT_CHAR);
    }

    private BigDecimal operate(final String operator,
                               final BigDecimal operand1,
                               final BigDecimal operand2) throws Exception {
        Log.v(TAG, "Processing " + operand1 + " " + operator + " " + operand2);

        switch (operator) {
            case ADD:
                return operand1.add(operand2);
            case SUBTRACT:
                return operand1.subtract(operand2);
            case MULTIPLY:
                return operand1.multiply(operand2);
            case DIVIDE:
                return operand1.divide(operand2, INTERNAL_SCALE, BigDecimal.ROUND_HALF_EVEN);
            case MODULUS:
                return operand1.remainder(operand2);
            case POWER:
                return new BigDecimal(Math.pow(operand1.doubleValue(), operand2.doubleValue()));
            default:
                throw new IllegalArgumentException(
                        "Calculator::operate: Invalid operator " + operator);
        }
    }

    private BigDecimal operate(final String operator,
                               final BigDecimal operand) throws Exception {
        Log.v(TAG, "Processing " + operator + " " + operand);

        switch (operator) {
            case SIN:
                return new BigDecimal(Math.sin(Math.toRadians(operand.doubleValue())));
            case COS:
                return new BigDecimal(Math.cos(Math.toRadians(operand.doubleValue())));
            case TAN:
                return new BigDecimal(Math.tan(Math.toRadians(operand.doubleValue())));
            case LOG:
                return new BigDecimal(Math.log10(operand.doubleValue()));
            case LN:
                return new BigDecimal(Math.log(operand.doubleValue()));
            case PERCENTAGE:
                return operand.divide(new BigDecimal(100), INTERNAL_SCALE, BigDecimal.ROUND_HALF_EVEN);
            case FACTORIAL:
                // TODO handle decimal
                return new BigDecimal(CombinatoricsUtils.factorialDouble(operand.intValue()));
            case SQUARE_ROOT:
                return new BigDecimal(Math.sqrt(operand.doubleValue()));
            case SQUARE:
                return new BigDecimal(Math.pow(operand.doubleValue(), 2));
            case CUBE:
                return new BigDecimal(Math.pow(operand.doubleValue(), 3));
            default:
                throw new IllegalArgumentException(
                        "Calculator::operate: Invalid operator " + operator);
        }
    }

    boolean IsSane(final Vector<String> infixExpression, boolean isComplete) {
        // check that expression is not empty and open/close brackets are balanced
        if (infixExpression.isEmpty()) { return false; }

        int brackets = 0;
        for (String item : infixExpression) {
            if (!IsOperand(item) && !IsOperator(item, true)) {
                return false;
            }
            if (item.equals(OPEN_BRACKET)) {
                brackets++;
            } else if (item.equals(CLOSE_BRACKET)) {
                brackets--;
            }
            if (brackets < 0) {
                return false;
            }
        }

        //noinspection SimplifiableIfStatement
        if (isComplete) {
            return (!IsBinaryOperator(infixExpression.lastElement())
                    && !IsPreUnaryOperator(infixExpression.lastElement())
                    && brackets == 0);
        } else {
            return true;
        }
    }

    boolean IsOperand(final String s) {
        // operand can start with zero or one negative sign
        // operand can have zero or one decimal point
        // operand must have one or more digits
        int minus_count = 0;
        int point_count = 0;
        int digit_count = 0;
        for(char c : s.toCharArray()) {
            if (c == SUBTRACT_CHAR) {
                minus_count++;
            } else if (c == DECIMAL_CHAR) {
                point_count++;
            } else if (Character.isDigit(c)) {
                digit_count++;
            } else {
                return false;
            }
            if (minus_count > 1 || point_count > 1) {
                return false;
            }
        }
        return digit_count > 0 &&
                (minus_count == 0 || (minus_count == 1 && s.charAt(0) == SUBTRACT_CHAR));
    }

    private boolean IsOperator(final String s, boolean includingBrackets) {
        OperatorInfo info = mOperators.get(s);
        if (includingBrackets) {
            return (info != null);
        } else {
            return (info != null && !(s.equals(OPEN_BRACKET) || s.equals(CLOSE_BRACKET)));
        }
    }

    boolean IsBinaryOperator(final String s) {
        OperatorInfo info = mOperators.get(s);
        return (info != null && info.mType == OperatorType.BINARY);
    }

    boolean IsPreUnaryOperator(final String s) {
        OperatorInfo info = mOperators.get(s);
        return (info != null && info.mType == OperatorType.PRE_UNARY);
    }

    boolean IsPostUnaryOperator(final String s) {
        OperatorInfo info = mOperators.get(s);
        return (info != null && info.mType == OperatorType.POST_UNARY);
    }

    boolean IsOperandAllowed(final String existingOperand, final char newChar) {
        // returns true if the new character can be appended to the existing operand

        for (char c : existingOperand.toCharArray()) {
            if (!(c == SUBTRACT_CHAR || c == DECIMAL_CHAR || Character.isDigit(c))) {
                return false;
            }
        }
        int subtract_pos = existingOperand.indexOf(SUBTRACT_CHAR);
        if (subtract_pos != -1 && subtract_pos != 0) {
            return false;
        }

        if (existingOperand.isEmpty()) {
            return newChar == SUBTRACT_CHAR || newChar == DECIMAL_CHAR || Character.isDigit(newChar);
        } else if (existingOperand.length() == 1) {
            // allowed -> -. -1 .1 11 1.
            final char existingChar = existingOperand.charAt(0);
            if (existingChar == SUBTRACT_CHAR) {
                return newChar == DECIMAL_CHAR || Character.isDigit(newChar);
            } else if (existingChar == DECIMAL_CHAR) {
                return Character.isDigit(newChar);
            } else { // Character.isDigit(existingChar)
                // TODO consider leading zeroes
                return Character.isDigit(newChar) || (newChar == DECIMAL_CHAR);
            }
        } else { // length > 1
            if (newChar == DECIMAL_CHAR) {
                // only one point allowed
                return existingOperand.indexOf(DECIMAL_CHAR) < 0;
            } else if (Character.isDigit(newChar)) {
                // TODO consider leading zeroes
                // precision is number of digits
                // scale is number of digits after decimal point
                final int decimal = existingOperand.indexOf(DECIMAL_CHAR);
                final boolean is_negative = (existingOperand.charAt(0) == SUBTRACT_CHAR);
                int precision = existingOperand.length();
                precision = (decimal >= 0) ? precision - 1 : precision;
                precision = is_negative ? precision - 1 : precision;
                final int scale = (decimal >= 0) ? existingOperand.length() - decimal - 1 : 0;
                Log.v(TAG, existingOperand + " scale " + scale + " precision " + precision);
                return (precision < INPUT_PRECISION && scale < INPUT_SCALE);
            } else {
                return false;
            }
        }
    }
}
