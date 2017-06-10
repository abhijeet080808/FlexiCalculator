package com.saraighatsoftware.flexicalculator;

import android.util.Log;

class Logger {

    private static final boolean LOGGING_ENABLED = !BuildConfig.BUILD_TYPE.equalsIgnoreCase("release");

    static void v(final String tag, final String message) {
        if (LOGGING_ENABLED) Log.v(tag, message);
    }

    static void v(final String tag, final String message, Throwable tr) {
        if (LOGGING_ENABLED) Log.v(tag, message, tr);
    }

    static void d(final String tag, final String message) {
        if (LOGGING_ENABLED) Log.d(tag, message);
    }

    static void d(final String tag, final String message, Throwable tr) {
        if (LOGGING_ENABLED) Log.d(tag, message, tr);
    }

    static void i(final String tag, final String message) {
        if (LOGGING_ENABLED) Log.i(tag, message);
    }

    static void i(final String tag, final String message, Throwable tr) {
        if (LOGGING_ENABLED) Log.i(tag, message, tr);
    }

    static void w(final String tag, final String message) {
        if (LOGGING_ENABLED) Log.w(tag, message);
    }

    static void w(final String tag, final String message, Throwable tr) {
        if (LOGGING_ENABLED) Log.w(tag, message, tr);
    }

    static void e(final String tag, final String message) {
        if (LOGGING_ENABLED) Log.e(tag, message);
    }

    static void e(final String tag, final String message, Throwable tr) {
        if (LOGGING_ENABLED) Log.e(tag, message, tr);
    }
}
