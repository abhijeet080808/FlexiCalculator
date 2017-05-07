package com.saraighatsoftware.flexicalculator;

class Pair {
    private final String mFirst;
    private final String mSecond;

    Pair(String first, String second) {
        mFirst = first;
        mSecond = second;
    }

    String first() { return mFirst; }
    String second() { return mSecond; }
}
