package com.saraighatsoftware.flexicalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

class ResultFormat {

    private static final int RESULT_SCALE = 9;
    private static final DecimalFormat format = new DecimalFormat();

    static String Format(BigDecimal value) {
        format.setMaximumFractionDigits(RESULT_SCALE);
        format.setMinimumFractionDigits(0);
        format.setGroupingUsed(false);
        format.setRoundingMode(RoundingMode.HALF_EVEN);
        return format.format(value).replace('-', Calculator.SUBTRACT_CHAR);
    }
}
