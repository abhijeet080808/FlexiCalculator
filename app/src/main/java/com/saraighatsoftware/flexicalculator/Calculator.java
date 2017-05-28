package com.saraighatsoftware.flexicalculator;

import android.util.Log;

import org.apache.commons.math3.util.CombinatoricsUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;

class Calculator {

    private static final String TAG = "Calculator";

    private enum OperatorType {
        BINARY, PRE_UNARY, POST_UNARY, OTHER
    }

    enum Base {
        HEX(16), DEC(10), OCT(8), BIN(2);

        private final int mBase;

        Base(int base) {
            this.mBase = base;
        }

        public int getValue() {
            return mBase;
        }
    }

    enum AngularUnit {
        DEGREE, RADIAN
    }

    private class OperatorInfo {
        OperatorInfo(OperatorType type, int precedence, boolean isRightAssoc) {
            mType = type;
            mPrecedence = precedence;
            mIsRightAssoc = isRightAssoc;
        }

        final OperatorType mType;
        final int mPrecedence; // lower the value, higher the precedence
        final boolean mIsRightAssoc;
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
    static final String POWER = "^";
    static final String DIVIDE = "\u00f7";
    static final String MULTIPLY = "\u00d7";
    static final String MODULUS = "mod";
    static final String SUBTRACT = "\u2212";
    static final String ADD = "+";
    static final String LSH = "<<"; // left shift
    static final String RSH = ">>"; // right shift
    static final String AND = "and"; // right shift
    static final String OR = "or"; // right shift
    static final String XOR = "xor"; // right shift
    static final String POINT = ".";

    static final char DIVIDE_CHAR = '\u00f7';
    static final char MULTIPLY_CHAR = '\u00d7';
    static final char SUBTRACT_CHAR = '\u2212';
    private static final char POINT_CHAR = '.';

    private static final int INTERNAL_SCALE = 12;
    // Precision is the total number of digits
    private static final int INPUT_PRECISION = 12;
    // Scale is the number of digits after the decimal point
    private static final int INPUT_SCALE = 6;

    private final HashMap<String, OperatorInfo> mOperators;

    Calculator() {
        mOperators = new HashMap<>();
        mOperators.put(OPEN_BRACKET, new OperatorInfo(OperatorType.OTHER, 1, false));
        mOperators.put(CLOSE_BRACKET, new OperatorInfo(OperatorType.OTHER, 1, false));
        mOperators.put(SIN, new OperatorInfo(OperatorType.PRE_UNARY, 2, false));
        mOperators.put(COS, new OperatorInfo(OperatorType.PRE_UNARY, 2, false));
        mOperators.put(TAN, new OperatorInfo(OperatorType.PRE_UNARY, 2, false));
        mOperators.put(LOG, new OperatorInfo(OperatorType.PRE_UNARY, 2, false));
        mOperators.put(LN, new OperatorInfo(OperatorType.PRE_UNARY, 2, false));
        mOperators.put(PERCENTAGE, new OperatorInfo(OperatorType.POST_UNARY, 2, false));
        mOperators.put(FACTORIAL, new OperatorInfo(OperatorType.POST_UNARY, 2, false));
        mOperators.put(SQUARE_ROOT, new OperatorInfo(OperatorType.PRE_UNARY, 2, false));
        mOperators.put(SQUARE, new OperatorInfo(OperatorType.POST_UNARY, 2, false));
        // power is the only right associative operator
        mOperators.put(POWER, new OperatorInfo(OperatorType.BINARY, 3, true));
        mOperators.put(DIVIDE, new OperatorInfo(OperatorType.BINARY, 4, false));
        mOperators.put(MULTIPLY, new OperatorInfo(OperatorType.BINARY, 4, false));
        mOperators.put(MODULUS, new OperatorInfo(OperatorType.BINARY, 4, false));
        mOperators.put(SUBTRACT, new OperatorInfo(OperatorType.BINARY, 5, false));
        mOperators.put(ADD, new OperatorInfo(OperatorType.BINARY, 5, false));
        mOperators.put(LSH, new OperatorInfo(OperatorType.BINARY, 6, false));
        mOperators.put(RSH, new OperatorInfo(OperatorType.BINARY, 6, false));
        mOperators.put(AND, new OperatorInfo(OperatorType.BINARY, 7, false));
        mOperators.put(OR, new OperatorInfo(OperatorType.BINARY, 7, false));
        mOperators.put(XOR, new OperatorInfo(OperatorType.BINARY, 7, false));
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

    String Evaluate(final Vector<String> infixExpression,
                    final Base base,
                    final AngularUnit angularUnit) throws Exception {
        // evaluate something like [30, +, 2]
        // each vector element either contains a digit in string form
        // or contains a operator in string form

        if (!IsSane(infixExpression, base, true)) {
            // invalid input
            throw new IllegalArgumentException(
                    "Calculator::Evaluate: Invalid expression " + infixExpression.toString());
        }

        Stack<BigDecimal> operands = new Stack<>();  // for numbers
        Stack<String> operators = new Stack<>(); // for operators and parenthesis

        for (String item : infixExpression) {
            if (IsOperand(item, base)) {
                Log.v(TAG, "Processing " + item);
                operands.push(getBigDecimal(item, base));
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
                        operands.push(operate(operator, operand, angularUnit));
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
                        operands.push(operate(operator, operand, angularUnit));
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
                operands.push(operate(operator, operand, angularUnit));
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

        return getNumber(operands.firstElement(), base);
    }

    private BigDecimal getBigDecimal(final String operand, final Base base) throws Exception {
        // convert integer operands to BigDecimal in base 10
        // floating point operands have to be already in base 10 else they are truncated
        switch (base) {
            case HEX:
            case OCT:
            case BIN:
                // floating point here will throw exception
                return new BigDecimal(
                        new BigInteger(operand.replace(SUBTRACT_CHAR, '-'), base.getValue()));
            case DEC:
                return new BigDecimal(operand.replace(SUBTRACT_CHAR, '-'));
            default:
                throw new IllegalArgumentException(
                        "Calculator::operate: Invalid base " + base);
        }
    }

    private String getNumber(final BigDecimal operand, final Base base) throws Exception {
        // convert base 10 operand to other base as specified
        // floating point operands have to be already in base 10 else they are truncated
        switch (base) {
            case HEX:
            case OCT:
            case BIN:
                return operand.toBigInteger().toString(base.getValue())
                        .replace('-', SUBTRACT_CHAR).toUpperCase();
            case DEC:
                return ResultFormat.Format(operand);
            default:
                throw new IllegalArgumentException(
                        "Calculator::operate: Invalid base " + base);
        }
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
            case LSH:
                return new BigDecimal(operand1.toBigInteger().shiftLeft(operand2.intValue()));
            case RSH:
                return new BigDecimal(operand1.toBigInteger().shiftRight(operand2.intValue()));
            case AND:
                return new BigDecimal(operand1.toBigInteger().and(operand2.toBigInteger()));
            case OR:
                return new BigDecimal(operand1.toBigInteger().or(operand2.toBigInteger()));
            case XOR:
                return new BigDecimal(operand1.toBigInteger().xor(operand2.toBigInteger()));
            default:
                throw new IllegalArgumentException(
                        "Calculator::operate: Invalid operator " + operator);
        }
    }

    private BigDecimal operate(final String operator,
                               final BigDecimal operand,
                               final AngularUnit angularUnit) throws Exception {
        Log.v(TAG, "Processing " + operator + " " + operand);

        // sin/cos/tan expects value in degree
        switch (operator) {
            case SIN:
                return new BigDecimal(
                        Math.sin(angularUnit == AngularUnit.DEGREE ?
                                Math.toRadians(operand.doubleValue()) :
                                operand.doubleValue()));
            case COS:
                return new BigDecimal(
                        Math.cos(angularUnit == AngularUnit.DEGREE ?
                                Math.toRadians(operand.doubleValue()) :
                                operand.doubleValue()));
            case TAN:
                return new BigDecimal(
                        Math.tan(angularUnit == AngularUnit.DEGREE ?
                                Math.toRadians(operand.doubleValue()) :
                                operand.doubleValue()));
            case LOG:
                return new BigDecimal(Math.log10(operand.doubleValue()));
            case LN:
                return new BigDecimal(Math.log(operand.doubleValue()));
            case PERCENTAGE:
                return operand.divide(new BigDecimal(100), INTERNAL_SCALE, BigDecimal.ROUND_HALF_EVEN);
            case FACTORIAL:
                return new BigDecimal(CombinatoricsUtils.factorialDouble(operand.intValue()));
            case SQUARE_ROOT:
                return new BigDecimal(Math.sqrt(operand.doubleValue()));
            case SQUARE:
                return new BigDecimal(Math.pow(operand.doubleValue(), 2));
            default:
                throw new IllegalArgumentException(
                        "Calculator::operate: Invalid operator " + operator);
        }
    }

    boolean IsSane(final Vector<String> infixExpression, final Base base, final boolean isComplete) {
        // check that expression is not empty and open/close brackets are balanced
        if (infixExpression.isEmpty()) { return false; }

        int brackets = 0;
        for (String item : infixExpression) {
            if (!IsOperand(item, base) && !IsOperator(item)) {
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

    private boolean isValidDigit(final char c, final Base base) {
        switch (c) {
            case '0':
            case '1':
                return (base == Base.BIN || base == Base.OCT || base == Base.DEC || base == Base.HEX);
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
                return (base == Base.OCT || base == Base.DEC || base == Base.HEX);
            case '8':
            case '9':
                return  (base == Base.DEC || base == Base.HEX);
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
                return (base == Base.HEX);
            default:
                return false;
        }
    }

    boolean IsOperand(final String s, final Base base) {
        // operand can start with zero or one negative sign
        // operand can have zero or one decimal point
        // operand must have one or more digits

        int minus_count = 0;
        int point_count = 0;
        int digit_count = 0;
        for(char c : s.toCharArray()) {
            if (c == SUBTRACT_CHAR) {
                minus_count++;
            } else if (c == POINT_CHAR) {
                point_count++;
            } else if (isValidDigit(c, base)) {
                digit_count++;
            } else {
                return false;
            }
            if (minus_count > 1) {
                return false;
            } else if (point_count > 0 && base != Base.DEC) {
                return false;
            } else if (point_count > 1 && base == Base.DEC){
                return false;
            }
        }
        return digit_count > 0 &&
                (minus_count == 0 || (minus_count == 1 && s.charAt(0) == SUBTRACT_CHAR));
    }

    boolean IsOperator(final String s) {
        OperatorInfo info = mOperators.get(s);
        return (info != null);
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

    boolean IsOperandAllowed(final String existingOperand, final Base base, final char newChar) {
        // returns true if the new character can be appended to the existing operand
        // decimal point is not allowed for HEX/OCT/BIN

        for (char c : existingOperand.toCharArray()) {
            if (c != SUBTRACT_CHAR && c != POINT_CHAR && !isValidDigit(c, base)) {
                return false;
            }
        }

        final int subtract_pos = existingOperand.indexOf(SUBTRACT_CHAR);
        if (subtract_pos != -1 && subtract_pos != 0) {
            return false;
        }
        final int point_pos = existingOperand.indexOf(POINT_CHAR);
        if (point_pos >= 0 && base != Base.DEC) {
            return false;
        }

        if (existingOperand.isEmpty()) {
            return newChar == SUBTRACT_CHAR
                    || (newChar == POINT_CHAR && base == Base.DEC)
                    || isValidDigit(newChar, base);
        } else if (existingOperand.length() == 1) {
            // allowed -> -. -1 .1 11 1.
            final char existingChar = existingOperand.charAt(0);
            if (existingChar == SUBTRACT_CHAR) {
                return (newChar == POINT_CHAR && base == Base.DEC)
                        || isValidDigit(newChar, base);
            } else if (existingChar == POINT_CHAR) {
                return isValidDigit(newChar, base);
            } else { // IsValidDigit(existingChar, base)
                // TODO consider leading zeroes
                return isValidDigit(newChar, base)
                        || (newChar == POINT_CHAR && base == Base.DEC);
            }
        } else { // length > 1
            if (newChar == POINT_CHAR) {
                // only one point allowed
                return point_pos < 0 && base == Base.DEC;
            } else if (isValidDigit(newChar, base)) {
                // TODO consider leading zeroes
                // precision is number of digits
                // scale is number of digits after decimal point
                final boolean is_negative = (existingOperand.charAt(0) == SUBTRACT_CHAR);
                int precision = existingOperand.length();
                precision = (point_pos >= 0) ? precision - 1 : precision;
                precision = is_negative ? precision - 1 : precision;
                final int scale = (point_pos >= 0) ? existingOperand.length() - point_pos - 1 : 0;
                Log.v(TAG, existingOperand + " scale " + scale + " precision " + precision);
                return (precision < INPUT_PRECISION && scale < INPUT_SCALE);
            } else {
                return false;
            }
        }
    }

    String Convert(final String operand, final Base oldBase, final Base newBase) throws Exception {
        // convert the operand's base
        if (oldBase == newBase) {
            return operand;
        }

        String value = operand.replace(SUBTRACT_CHAR, '-');
        final int point_pos = operand.indexOf(POINT_CHAR);
        // truncate the part after decimal point
        if (point_pos >= 0) {
            value = value.substring(0, point_pos);
        }
        if (value.isEmpty()) {
            value = "0";
        }

        final BigInteger base_10_value = new BigInteger(value, oldBase.getValue());
        return base_10_value.toString(newBase.getValue())
                .replace('-', SUBTRACT_CHAR)
                .toUpperCase();
    }
}
