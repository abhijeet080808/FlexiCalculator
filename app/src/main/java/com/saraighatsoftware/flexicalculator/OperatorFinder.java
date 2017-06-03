package com.saraighatsoftware.flexicalculator;


import java.util.HashMap;

class OperatorFinder {

    // map of operator symbols vs operators
    private final HashMap<String, Operator> mOperators;

    OperatorFinder() {
        Operator operators[] = Operator.values();
        mOperators = new HashMap<>();
        for (Operator operator : operators) {
            mOperators.put(operator.Symbol(), operator);
        }
    }

    Operator Get(final String symbol) {
        return mOperators.get(symbol);
    }

    static Operator[] GetAll() {
        return Operator.values();
    }
}