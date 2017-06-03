package com.saraighatsoftware.flexicalculator;

enum Operator {
    OPEN_BRACKET("(", OperatorType.OTHER, 1, false, new String[]{ "open bracket" }),
    CLOSE_BRACKET(")", OperatorType.OTHER, 1, false, new String[]{ "close bracket" }),
    SIN("sin", OperatorType.PRE_UNARY, 2, false, new String[]{ "sine" }),
    COS("cos", OperatorType.PRE_UNARY, 2, false, new String[]{ "cosine" }),
    TAN("tan", OperatorType.PRE_UNARY, 2, false, new String[]{ "tangent" }),
    LOG("log", OperatorType.PRE_UNARY, 2, false, new String[]{ "log" }),
    LN("ln", OperatorType.PRE_UNARY, 2, false, new String[]{ "base e log" }),
    PERCENTAGE("%", OperatorType.POST_UNARY, 2, false, new String[]{ }), // this needs special treatment
    FACTORIAL("!", OperatorType.POST_UNARY, 2, false, new String[]{ "factorial" }),
    SQUARE_ROOT("\u221a", OperatorType.PRE_UNARY, 2, false, new String[]{ "square root of", "square root", "root" }),
    SQUARE("\u00b2", OperatorType.POST_UNARY, 2, false, new String[]{ "square", "squared" }),
    // power is the only right associative operator
    POWER("^", OperatorType.BINARY, 3, true, new String[]{ "to the power", "to the power of" }),
    DIVIDE("\u00f7", OperatorType.BINARY, 4, false, new String[]{ "/", "by", "divided by", "over" }),
    MULTIPLY("\u00d7", OperatorType.BINARY, 4, false, new String[]{ "x", "multiplied by", "into", "times" }),
    MODULUS("mod", OperatorType.BINARY, 4, false, new String[]{ "modulus", "modulo", "mod" }),
    SUBTRACT("\u2212", OperatorType.BINARY, 5, false, new String[]{ "minus" }),
    ADD("+", OperatorType.BINARY, 5, false, new String[]{ "plus" }),
    LSH("<<", OperatorType.BINARY, 6, false, new String[]{ "left shift by" }), // left shift
    RSH(">>", OperatorType.BINARY, 6, false, new String[]{ "right shift by" }), // right shift
    AND("and", OperatorType.BINARY, 7, false, new String[]{ "and" }),
    OR("or", OperatorType.BINARY, 7, false, new String[]{ "or" }),
    XOR("xor", OperatorType.BINARY, 7, false, new String[]{ "xor" });

    enum OperatorType {
        BINARY, PRE_UNARY, POST_UNARY, OTHER
    }

    private final String mSymbol;
    private final OperatorType mType;
    private final int mPrecedence; // lower the value, higher the precedence
    private final boolean mIsRightAssoc;
    private final String[] mKeywords;

    Operator(final String symbol,
             final OperatorType type,
             final int precedence,
             final boolean isRightAssoc,
             final String[] keywords) {
        mSymbol = symbol;
        mType = type;
        mPrecedence = precedence;
        mIsRightAssoc = isRightAssoc;
        mKeywords = keywords;
    }

    String Symbol() {
        return mSymbol;
    }

    OperatorType Type() {
        return mType;
    }

    int Precedence() {
        return mPrecedence;
    }

    boolean IsRightAssoc() {
        return mIsRightAssoc;
    }

    String[] Keywords() {
        return mKeywords;
    }

    @Override
    public String toString() {
        return mSymbol;
    }
}
