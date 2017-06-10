package com.saraighatsoftware.flexicalculator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Stack;

class Calculator {

    private static final String TAG = "Calculator";

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

    static final String POINT = ".";

    static final char DIVIDE_CHAR = '\u00f7';
    static final char MULTIPLY_CHAR = '\u00d7';
    static final char SUBTRACT_CHAR = '\u2212';
    static final char POINT_CHAR = '.';

    private static final int INTERNAL_SCALE = 12;
    // Precision is the total number of digits
    private static final int INPUT_PRECISION = 12;
    // Scale is the number of digits after the decimal point
    private static final int INPUT_SCALE = 6;

    private final OperatorFinder mOpFinder;

    Calculator() {
        mOpFinder = new OperatorFinder();
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

    String Evaluate(final ArrayList<String> infixExpression,
                    final Base base,
                    final AngularUnit angularUnit)
            throws IllegalArgumentException, ArithmeticException {
        // evaluate something like [30, +, 2]
        // each vector element either contains a digit in string form
        // or contains a operator in string form

        if (!IsSane(infixExpression, base, true)) {
            // invalid input
            throw new IllegalArgumentException(
                    "Calculator::Evaluate: Invalid expression " + infixExpression.toString());
        }

        Stack<BigDecimal> operands = new Stack<>(); // for numbers
        Stack<Operator> operators = new Stack<>();  // for operators and parenthesis

        for (String item : infixExpression) {
            if (IsOperand(item, base)) {
                Logger.v(TAG, "Processing " + item);
                operands.push(getBigDecimal(item, base));
            } else if (item.equals(Operator.OPEN_BRACKET.Symbol())) {
                Logger.v(TAG, "Processing " + item);
                operators.push(Operator.OPEN_BRACKET);
            } else if (item.equals(Operator.CLOSE_BRACKET.Symbol())) {
                Logger.v(TAG, "Processing " + item);
                while (!operators.peek().equals(Operator.OPEN_BRACKET)) {
                    Operator operator = operators.pop();
                    if (operator.Type() == Operator.OperatorType.PRE_UNARY ||
                            operator.Type() == Operator.OperatorType.POST_UNARY) {
                        BigDecimal operand = operands.pop();
                        Logger.v(TAG, "Got close bracket - "
                                + operator + " " + operand);
                        operands.push(operate(operator, operand, angularUnit));
                    } else {
                        BigDecimal operand2 = operands.pop();
                        BigDecimal operand1 = operands.pop();
                        Logger.v(TAG, "Got close bracket - "
                                + operand1 + " " + operator + " " + operand2);
                        operands.push(operate(operator, operand1, operand2));
                    }
                }
                operators.pop(); // discard the open bracket
            } else { // it is an operator
                // process all operators with higher or same precedence as current operator
                Logger.v(TAG, "Processing " + item);
                Operator current_operator = mOpFinder.Get(item);
                while (!operators.isEmpty() && (
                        (!current_operator.IsRightAssoc() && operators.peek().Precedence() <= current_operator.Precedence()) ||
                        (current_operator.IsRightAssoc() && operators.peek().Precedence() < current_operator.Precedence()))) {
                    // lower the precedence value, higher is the precedence

                    if (operators.peek().equals(Operator.OPEN_BRACKET) ||
                            operators.peek().equals(Operator.CLOSE_BRACKET)) {
                        break;
                    }

                    Operator operator = operators.pop();
                    if (operator.Type() == Operator.OperatorType.PRE_UNARY ||
                            operator.Type() == Operator.OperatorType.POST_UNARY) {
                        BigDecimal operand = operands.pop();
                        Logger.v(TAG, "Got higher precedence than " + item + " - "
                                + operator + " " + operand);
                        operands.push(operate(operator, operand, angularUnit));
                    } else {
                        BigDecimal operand2 = operands.pop();
                        BigDecimal operand1 = operands.pop();
                        Logger.v(TAG, "Got higher precedence than " + item + " - "
                                + operand1 + " " + operator + " " + operand2);
                        operands.push(operate(operator, operand1, operand2));
                    }
                }
                operators.push(current_operator);
            }
        }

        while (!operators.isEmpty()) {
            Operator operator = operators.pop();
            if (operator.Type() == Operator.OperatorType.PRE_UNARY ||
                    operator.Type() == Operator.OperatorType.POST_UNARY) {
                BigDecimal operand = operands.pop();
                Logger.v(TAG, "Got operator in stack - "
                        + operator + " " + operand);
                operands.push(operate(operator, operand, angularUnit));
            } else {
                BigDecimal operand2 = operands.pop();
                BigDecimal operand1 = operands.pop();
                Logger.v(TAG, "Got operator in stack - "
                        + operand1 + " " + operator + " " + operand2);
                operands.push(operate(operator, operand1, operand2));
            }
        }

        if (operands.size() != 1) {
            throw new IllegalStateException("Calculator::Evaluate: Invalid state");
        }

        return getNumber(operands.firstElement(), base);
    }

    private BigDecimal getBigDecimal(final String operand,
                                     final Base base) throws IllegalArgumentException {
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

    private String getNumber(final BigDecimal operand,
                             final Base base) throws IllegalArgumentException {
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

    private BigDecimal operate(final Operator operator,
                               final BigDecimal operand1,
                               final BigDecimal operand2)
            throws IllegalArgumentException, ArithmeticException {
        Logger.v(TAG, "Processing " + operand1 + " " + operator + " " + operand2);

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
                // 5 ^ 5.5 = (5 ^ 5) x (5 ^ 0.5)
                if (operand2.compareTo(new BigDecimal(Integer.MAX_VALUE)) > 0) {
                    throw new ArithmeticException("Exponent is too big");
                }
                int exponent = operand2.intValue();

                BigDecimal integer_part = operand1.pow(Math.abs(exponent));

                BigDecimal decimal_part =  new BigDecimal(Math.pow(
                        operand1.doubleValue(),
                        operand2.remainder(new BigDecimal(exponent != 0 ? Math.abs(exponent) : 1))
                                .abs()
                                .doubleValue()));
                BigDecimal result = integer_part.multiply(decimal_part);
                if (exponent < 0) {
                    return BigDecimal.ONE.divide(result, INTERNAL_SCALE, BigDecimal.ROUND_HALF_EVEN);
                } else {
                    return result;
                }

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

    private BigDecimal operate(final Operator operator,
                               final BigDecimal operand,
                               final AngularUnit angularUnit)
            throws IllegalArgumentException, ArithmeticException {
        Logger.v(TAG, "Processing " + operator + " " + operand);

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
                BigInteger fact = BigInteger.ONE;
                for (int i = 1; i <= operand.intValue(); i++) {
                    fact = fact.multiply(new BigInteger(String.valueOf(i)));
                }
                return new BigDecimal(fact);
            case SQUARE_ROOT:
                return new BigDecimal(Math.sqrt(operand.doubleValue()));
            case SQUARE:
                return operand.pow(2);
            default:
                throw new IllegalArgumentException(
                        "Calculator::operate: Invalid operator " + operator);
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean IsSane(final ArrayList<String> infixExpression, final Base base, final boolean isComplete) {
        // check that expression is not empty and open/close brackets are balanced
        if (infixExpression.isEmpty()) { return false; }

        int brackets = 0;
        for (String item : infixExpression) {
            if (!IsOperand(item, base) && !IsOperator(item)) {
                return false;
            }
            if (item.equals(Operator.OPEN_BRACKET.Symbol())) {
                brackets++;
            } else if (item.equals(Operator.CLOSE_BRACKET.Symbol())) {
                brackets--;
            }
            if (brackets < 0) {
                return false;
            }
        }

        //noinspection SimplifiableIfStatement
        if (isComplete) {
            Operator operator = mOpFinder.Get(infixExpression.get(infixExpression.size() - 1));
            return ((operator == null || (
                            operator.Type() != Operator.OperatorType.BINARY &&
                            operator.Type() != Operator.OperatorType.PRE_UNARY))
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
        return (mOpFinder.Get(s) != null);
    }

    boolean IsBinaryOperator(final String s) {
        Operator operator = mOpFinder.Get(s);
        return (operator != null && operator.Type() == Operator.OperatorType.BINARY);
    }

    boolean IsPreUnaryOperator(final String s) {
        Operator operator = mOpFinder.Get(s);
        return (operator != null && operator.Type() == Operator.OperatorType.PRE_UNARY);
    }

    boolean IsPostUnaryOperator(final String s) {
        Operator operator = mOpFinder.Get(s);
        return (operator != null && operator.Type() == Operator.OperatorType.POST_UNARY);
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
            } else { // isValidDigit(existingChar, base)
                // only 1 zero allowed before decimal point
                return !(existingChar == '0' && newChar == '0') &&
                        (isValidDigit(newChar, base) || (newChar == POINT_CHAR && base == Base.DEC));
            }
        } else { // length > 1
            if (newChar == POINT_CHAR) {
                // only one point allowed
                return point_pos < 0 && base == Base.DEC;
            } else if (isValidDigit(newChar, base)) {
                // only 1 zero allowed before decimal point
                if (newChar == '0' && !CanAppendZero(existingOperand, base)) {
                    return false;
                } else {
                    // precision is number of digits
                    // scale is number of digits after decimal point
                    final boolean is_negative = (existingOperand.charAt(0) == SUBTRACT_CHAR);
                    int precision = existingOperand.length();
                    precision = (point_pos >= 0) ? precision - 1 : precision;
                    precision = is_negative ? precision - 1 : precision;
                    final int scale = (point_pos >= 0) ? existingOperand.length() - point_pos - 1 : 0;
                    Logger.v(TAG, existingOperand + " scale " + scale + " precision " + precision);
                    return (precision < INPUT_PRECISION && scale < INPUT_SCALE);
                }
            } else {
                return false;
            }
        }
    }

    String Convert(final String operand, final Base oldBase, final Base newBase)
            throws IllegalArgumentException {
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

    private boolean CanAppendZero(final String existingOperand, final Base base) {
        // only 1 leading zero allowed
        if (existingOperand.indexOf(POINT_CHAR) >= 0) {
            return true;
        }
        for (char c: existingOperand.toCharArray()) {
            if (isValidDigit(c, base)) {
                return c != '0';
            } // else is SUBTRACT_CHAR
        }
        // empty string or contains only SUBTRACT_CHAR
        return true;
    }

}
