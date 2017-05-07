package com.saraighatsoftware.flexicalculator;

public class Pair {
    private final String mFirst;
    private final String mSecond;

    public Pair(String first, String second) {
        mFirst = first;
        mSecond = second;
    }

    public String first() { return mFirst; }
    public String second() { return mSecond; }
}
