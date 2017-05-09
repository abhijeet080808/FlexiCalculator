package com.saraighatsoftware.flexicalculator;

class Pair<T1, T2> {
    private final T1 mFirst;
    private final T2 mSecond;

    Pair(T1 first, T2 second) {
        mFirst = first;
        mSecond = second;
    }

    T1 first() { return mFirst; }
    T2 second() { return mSecond; }
}
