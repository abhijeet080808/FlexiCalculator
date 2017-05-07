package com.saraighatsoftware.flexicalculator;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;

class Calculator {

    private static final String TAG = "Calculator";

    static final String OPEN_BRACKET = "(";
    static final String CLOSE_BRACKET = ")";
    static final String DIVIDE = "/";
    static final String MULTIPLY = "*";
    static final String SUBTRACT = "-";
    static final String ADD = "+";

    private static final String[] OPERATORS = {
            OPEN_BRACKET,
            CLOSE_BRACKET,
            DIVIDE,
            MULTIPLY,
            SUBTRACT,
            ADD };

    private static final int SCALE = 12;

    private final HashMap<String, Integer> mOperatorPrecedence;
    private final DecimalFormat mFormat;

    Calculator() {
        mOperatorPrecedence = new HashMap<>();
        mOperatorPrecedence.put(OPEN_BRACKET, 1);
        mOperatorPrecedence.put(CLOSE_BRACKET, 1);
        mOperatorPrecedence.put(DIVIDE, 2);
        mOperatorPrecedence.put(MULTIPLY, 2);
        mOperatorPrecedence.put(SUBTRACT, 3);
        mOperatorPrecedence.put(ADD, 3);

        mFormat = new DecimalFormat();
        mFormat.setMaximumFractionDigits(6);
        mFormat.setMinimumFractionDigits(0);
        mFormat.setGroupingUsed(false);
        mFormat.setRoundingMode(RoundingMode.HALF_EVEN);
    }

    /*
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
                1 While the operator stack is not empty, and the top thing on the operator stack has
                the same or greater precedence as thisOp,
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
                operands.push(new BigDecimal(item));
            } else if (item.equals(OPEN_BRACKET)) {
                Log.v(TAG, "Processing " + item);
                operators.push(item);
            } else if (item.equals(CLOSE_BRACKET)) {
                Log.v(TAG, "Processing " + item);
                while (!operators.peek().equals(OPEN_BRACKET)) {
                    String operator = operators.pop();
                    BigDecimal operand2 = operands.pop();
                    BigDecimal operand1 = operands.pop();
                    Log.v(TAG, "Got close bracket - " + operand1 + " " + operator + " " + operand2);
                    operands.push(operate(operator, operand1, operand2));
                }
                operators.pop(); // discard the open bracket
            } else { // it is an operator
                // process all operators with higher or same precedence as current operator
                Log.v(TAG, "Processing " + item);
                while (!operators.isEmpty() &&
                        mOperatorPrecedence.get(operators.peek()) <= mOperatorPrecedence.get(item)) {
                    if (operators.peek().equals(OPEN_BRACKET) ||
                            operators.peek().equals(CLOSE_BRACKET)) {
                        break;
                    }
                    // lower the precedence value, higher is the precedence
                    String operator = operators.pop();
                    BigDecimal operand2 = operands.pop();
                    BigDecimal operand1 = operands.pop();
                    Log.v(TAG, "Got higher precedence than " + item + " - " + operand1 + " " + operator + " " + operand2);
                    operands.push(operate(operator, operand1, operand2));
                }
                operators.push(item);
            }
        }

        while (!operators.isEmpty()) {
            String operator = operators.pop();
            BigDecimal operand2 = operands.pop();
            BigDecimal operand1 = operands.pop();
            Log.v(TAG, "Got operator in stack - " + operand1 + " " + operator + " " + operand2);
            operands.push(operate(operator, operand1, operand2));
        }

        if (operands.size() != 1) {
            throw new IllegalStateException("Calculator::Evaluate: Invalid state");
        }

        return mFormat.format(operands.firstElement());
    }

    static boolean IsSane(final Vector<String> infixExpression, boolean isComplete) {
        // check that open and close brackets are balanced
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
            return (!IsOperator(infixExpression.lastElement(), false) && brackets == 0);
        } else {
            return true;
        }
    }

    private static BigDecimal operate(final String operator,
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
                return operand1.divide(operand2, SCALE, BigDecimal.ROUND_HALF_EVEN);
            default:
                throw new IllegalArgumentException(
                        "Calculator::operate: Invalid operator " + operator);
        }
    }

    static boolean IsOperand(final String s) {
        for(char c : s.toCharArray()) {
            if (!(Character.isDigit(c) || (c == '.'))) {
                return false;
            }
        }
        return true;
    }

    static boolean IsOperator(final String s, boolean includingBrackets) {
        for (String item : OPERATORS) {
            if (!includingBrackets && (item.equals(OPEN_BRACKET) || item.equals(CLOSE_BRACKET))) {
                continue;
            }
            if (s.equals(item)) {
                return true;
            }
        }
        return false;
    }

    static boolean IsAllowed(final String s) {
        // check if maximum length is reached
        // TODO restrict to BigDecimal capacity, restrict to 6 decimal points
        // TODO study scale and precision
        return s.length() <= 10;
    }
}
