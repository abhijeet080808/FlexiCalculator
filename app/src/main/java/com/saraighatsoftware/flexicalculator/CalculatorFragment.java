package com.saraighatsoftware.flexicalculator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class CalculatorFragment extends Fragment {

    private static final String TAG = "CalculatorFragment";

    private static final String ARG_INFIX_EXPRESSION = "infix_expression";
    private static final String ARG_IS_RESULT_SET = "is_result_set";
    private static final String ARG_BASE = "base";
    private static final String ARG_ANGULAR_UNIT = "angular_unit";

    private TextView mTextDisplay;
    private HorizontalScrollView mScrollDisplay;

    private Button mButtonZero;
    private Button mButtonOne;
    private Button mButtonTwo;
    private Button mButtonThree;
    private Button mButtonFour;
    private Button mButtonFive;
    private Button mButtonSix;
    private Button mButtonSeven;
    private Button mButtonEight;
    private Button mButtonNine;
    private Button mButtonA;
    private Button mButtonB;
    private Button mButtonC;
    private Button mButtonD;
    private Button mButtonE;
    private Button mButtonF;
    private Button mButtonPoint;
    private Button mButtonBase;
    private Button mButtonAngularUnit;

    private final Calculator mCalculator;
    private ArrayList<String> mInfixExpression;
    private boolean mIsResultSet;

    private Calculator.Base mBase;
    private Calculator.AngularUnit mAngularUnit;

    public CalculatorFragment() {
        mCalculator = new Calculator();
        mInfixExpression = new ArrayList<>();
        mIsResultSet = false;
        mBase = Calculator.Base.DEC;
        mAngularUnit = Calculator.AngularUnit.DEGREE;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(ARG_INFIX_EXPRESSION, mInfixExpression);
        outState.putBoolean(ARG_IS_RESULT_SET, mIsResultSet);
        outState.putSerializable(ARG_BASE, mBase);
        outState.putSerializable(ARG_ANGULAR_UNIT, mAngularUnit);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // fragment was destroyed by the system, so restore fragment state, set view state
            mInfixExpression = savedInstanceState.getStringArrayList(ARG_INFIX_EXPRESSION);
            mIsResultSet = savedInstanceState.getBoolean(ARG_IS_RESULT_SET);
            mBase = (Calculator.Base) savedInstanceState.getSerializable(ARG_BASE);
            mAngularUnit = (Calculator.AngularUnit) savedInstanceState.getSerializable(ARG_ANGULAR_UNIT);

            updateText();
            setBaseButtonState();
            setDigitButtonStates();
            setAngularUnitButtonState();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // restore view state automatically
        super.onCreateView(inflater, container, savedInstanceState);

        Context context = getContext();

        View root_view = inflater.inflate(R.layout.fragment_calculator, container, false);

        mTextDisplay = (TextView) root_view.findViewById(R.id.text_display);
        mTextDisplay.setTypeface(FontCache.GetLight(context));

        mScrollDisplay = (HorizontalScrollView) root_view.findViewById(R.id.scroll_display);

        Button button;

        mButtonZero = (Button) root_view.findViewById(R.id.button_zero);
        mButtonZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("0");
            }
        });
        mButtonZero.setTypeface(FontCache.GetSemiBold(context));

        mButtonOne = (Button) root_view.findViewById(R.id.button_one);
        mButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("1");
            }
        });
        mButtonOne.setTypeface(FontCache.GetSemiBold(context));

        mButtonTwo = (Button) root_view.findViewById(R.id.button_two);
        mButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("2");
            }
        });
        mButtonTwo.setTypeface(FontCache.GetSemiBold(context));

        mButtonThree = (Button) root_view.findViewById(R.id.button_three);
        mButtonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("3");
            }
        });
        mButtonThree.setTypeface(FontCache.GetSemiBold(context));

        mButtonFour = (Button) root_view.findViewById(R.id.button_four);
        mButtonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("4");
            }
        });
        mButtonFour.setTypeface(FontCache.GetSemiBold(context));

        mButtonFive = (Button) root_view.findViewById(R.id.button_five);
        mButtonFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("5");
            }
        });
        mButtonFive.setTypeface(FontCache.GetSemiBold(context));

        mButtonSix = (Button) root_view.findViewById(R.id.button_six);
        mButtonSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("6");
            }
        });
        mButtonSix.setTypeface(FontCache.GetSemiBold(context));

        mButtonSeven = (Button) root_view.findViewById(R.id.button_seven);
        mButtonSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("7");
            }
        });
        mButtonSeven.setTypeface(FontCache.GetSemiBold(context));

        mButtonEight = (Button) root_view.findViewById(R.id.button_eight);
        mButtonEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("8");
            }
        });
        mButtonEight.setTypeface(FontCache.GetSemiBold(context));

        mButtonNine = (Button) root_view.findViewById(R.id.button_nine);
        mButtonNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("9");
            }
        });
        mButtonNine.setTypeface(FontCache.GetSemiBold(context));

        mButtonA = (Button) root_view.findViewById(R.id.button_a);
        mButtonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("A");
            }
        });
        mButtonA.setTypeface(FontCache.GetRegular(context));

        mButtonB = (Button) root_view.findViewById(R.id.button_b);
        mButtonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("B");
            }
        });
        mButtonB.setTypeface(FontCache.GetRegular(context));

        mButtonC = (Button) root_view.findViewById(R.id.button_c);
        mButtonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("C");
            }
        });
        mButtonC.setTypeface(FontCache.GetRegular(context));

        mButtonD = (Button) root_view.findViewById(R.id.button_d);
        mButtonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("D");
            }
        });
        mButtonD.setTypeface(FontCache.GetRegular(context));

        mButtonE = (Button) root_view.findViewById(R.id.button_e);
        mButtonE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("E");
            }
        });
        mButtonE.setTypeface(FontCache.GetRegular(context));

        mButtonF = (Button) root_view.findViewById(R.id.button_f);
        mButtonF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("F");
            }
        });
        mButtonF.setTypeface(FontCache.GetRegular(context));

        mButtonPoint = (Button) root_view.findViewById(R.id.button_point);
        mButtonPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.POINT);
            }
        });
        mButtonPoint.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_open_bracket);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.OPEN_BRACKET.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_close_bracket);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.CLOSE_BRACKET.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_divide);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.DIVIDE.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_multiply);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.MULTIPLY.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_subtract);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.SUBTRACT.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.ADD.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_equal);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluate();
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clear();
                return true;
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_modulus);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.MODULUS.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_power);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.POWER.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_sin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.SIN.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_cos);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.COS.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_tan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.TAN.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_log);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.LOG.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_ln);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.LN.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_percentage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.PERCENTAGE.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_factorial);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.FACTORIAL.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_square_root);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.SQUARE_ROOT.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_square);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.SQUARE.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_lsh);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.LSH.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_rsh);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.RSH.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_and);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.AND.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_or);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.OR.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        button = (Button) root_view.findViewById(R.id.button_xor);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Operator.XOR.Symbol());
            }
        });
        button.setTypeface(FontCache.GetRegular(context));

        mButtonBase = (Button) root_view.findViewById(R.id.button_base);
        mButtonBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hex -> dec -> oct -> bin->hex
                Calculator.Base old_base = mBase;
                switch (mBase) {
                    case HEX:
                        mBase = Calculator.Base.DEC;
                        break;
                    case DEC:
                        mBase = Calculator.Base.OCT;
                        break;
                    case OCT:
                        mBase = Calculator.Base.BIN;
                        break;
                    case BIN:
                        mBase = Calculator.Base.HEX;
                        break;
                }
                setBaseButtonState();
                setDigitButtonStates();

                if (mInfixExpression.size() == 1 &&
                        mCalculator.IsOperand(mInfixExpression.get(0), old_base)) {
                    String old_operand = mInfixExpression.get(0);
                    mInfixExpression.clear();
                    try {
                        String new_operand = mCalculator.Convert(old_operand, old_base, mBase);
                        mInfixExpression.add(new_operand);
                        mIsResultSet = true;
                    } catch (Exception e) {
                        Log.v(TAG, "convert ", e);
                    }
                } else {
                    mInfixExpression.clear();
                }
                updateText();
            }
        });
        mButtonBase.setTypeface(FontCache.GetRegular(context));

        mButtonAngularUnit = (Button) root_view.findViewById(R.id.button_angular_unit);
        mButtonAngularUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mAngularUnit) {
                    case DEGREE:
                        mAngularUnit = Calculator.AngularUnit.RADIAN;
                        break;
                    case RADIAN:
                        mAngularUnit = Calculator.AngularUnit.DEGREE;
                        break;
                }
                setAngularUnitButtonState();
            }
        });
        mButtonAngularUnit.setTypeface(FontCache.GetRegular(context));

        updateText();
        setBaseButtonState();
        setDigitButtonStates();
        setAngularUnitButtonState();

        return root_view;
    }

    private void expressionListener(String token) {
        // reads 0-9 and operators
        if (mInfixExpression.isEmpty()) {

            // allow only operand to be stored or open bracket or pre operators
            if (mCalculator.IsOperandAllowed("", mBase, token.charAt(0))) {
                // point should be preceded by a leading 0
                if (token.equals(Calculator.POINT)) {
                    mInfixExpression.add("0" + token);
                } else {
                    mInfixExpression.add(token);
                }
            } else if (token.equals(Operator.OPEN_BRACKET.Symbol())) {
                mInfixExpression.add(token);
            } else if (mCalculator.IsPreUnaryOperator(token)) {
                mInfixExpression.add(token);
            }

        } else if (mIsResultSet) {

            if (mCalculator.IsBinaryOperator(token) || mCalculator.IsPostUnaryOperator(token)) {
                mInfixExpression.add(token);
                mIsResultSet = false;
            } else if (token.equals(Operator.OPEN_BRACKET.Symbol())) {
                mInfixExpression.remove(mInfixExpression.size() - 1);
                mInfixExpression.add(token);
                mIsResultSet = false;
            //} else if (token.equals(Calculator.CLOSE_BRACKET)) {
                // result operand can not be followed by close bracket
            } else if (mCalculator.IsOperandAllowed("", mBase, token.charAt(0))) {
                mInfixExpression.remove(mInfixExpression.size() - 1);
                // point should be preceded by a leading 0
                if (token.equals(Calculator.POINT)) {
                    mInfixExpression.add("0" + token);
                } else {
                    mInfixExpression.add(token);
                }
                mIsResultSet = false;
            }

        } else {

            final String last_token = mInfixExpression.get(mInfixExpression.size() - 1);
            final String last_to_last_token =
                    mInfixExpression.size() > 1 ? mInfixExpression.get(mInfixExpression.size() - 2) : "";

            // '-' is a special case
            // consider as part of operand only when it is the first operand
            // or when it is just after an open bracket

            if (last_token.equals(Operator.SUBTRACT.Symbol()) &&
                    (last_to_last_token.equals(Operator.OPEN_BRACKET.Symbol()) ||
                            last_to_last_token.equals(""))) {

                if(mCalculator.IsOperandAllowed(last_token, mBase, token.charAt(0))) {
                    mInfixExpression.remove(mInfixExpression.size() - 1);
                    // point should be preceded by a leading 0
                    if (token.equals(Calculator.POINT)) {
                        mInfixExpression.add(last_token + "0" + token);
                    } else {
                        mInfixExpression.add(last_token + token);
                    }
                }

            } else if (mCalculator.IsBinaryOperator(last_token)) {

                if (mCalculator.IsBinaryOperator(token)) {
                    mInfixExpression.remove(mInfixExpression.size() - 1);
                    mInfixExpression.add(token);
                } else if (mCalculator.IsPreUnaryOperator(token)) {
                    mInfixExpression.add(token);
                } else if (token.equals(Operator.OPEN_BRACKET.Symbol())) {
                    mInfixExpression.add(token);
                //} else if (token.equals(Calculator.CLOSE_BRACKET)) {
                    // operator can not be followed by close bracket
                } else if (mCalculator.IsOperandAllowed("", mBase, token.charAt(0))) {
                    // point should be preceded by a leading 0
                    if (token.equals(Calculator.POINT)) {
                        mInfixExpression.add("0" + token);
                    } else {
                        mInfixExpression.add(token);
                    }
                }

            } else if (mCalculator.IsPreUnaryOperator(last_token)) {

                if (mCalculator.IsPreUnaryOperator(token)) {
                    mInfixExpression.remove(mInfixExpression.size() - 1);
                    mInfixExpression.add(token);
                } else if (token.equals(Operator.OPEN_BRACKET.Symbol())) {
                    mInfixExpression.add(token);
                //} else if (token.equals(Calculator.CLOSE_BRACKET)) {
                    // operator can not be followed by close bracket
                } else if (mCalculator.IsOperandAllowed("", mBase, token.charAt(0))) {
                    // point should be preceded by a leading 0
                    if (token.equals(Calculator.POINT)) {
                        mInfixExpression.add("0" + token);
                    } else {
                        mInfixExpression.add(token);
                    }
                }

            } else if (mCalculator.IsPostUnaryOperator(last_token)) {

                if (mCalculator.IsPostUnaryOperator(token)) {
                    mInfixExpression.remove(mInfixExpression.size() - 1);
                    mInfixExpression.add(token);
                } else if (token.equals(Operator.CLOSE_BRACKET.Symbol())) {
                    mInfixExpression.add(token);
                } else if (mCalculator.IsBinaryOperator(token)) {
                    mInfixExpression.add(token);
                }

            } else if (last_token.equals(Operator.OPEN_BRACKET.Symbol())) {

                //if (Calculator.IsOperator(token, false)) {
                    // open bracket can not be followed by operator
                //} else
                if (mCalculator.IsPreUnaryOperator(token)) {
                    mInfixExpression.add(token);
                } else if (token.equals(Operator.OPEN_BRACKET.Symbol())) {
                    mInfixExpression.add(token);
                //} else if (token.equals(Calculator.CLOSE_BRACKET)) {
                    // open bracket can not be followed by close bracket
                } else if (mCalculator.IsOperandAllowed("", mBase, token.charAt(0))) {
                    // point should be preceded by a leading 0
                    if (token.equals(Calculator.POINT)) {
                        mInfixExpression.add("0" + token);
                    } else {
                        mInfixExpression.add(token);
                    }
                }

            } else if (last_token.equals(Operator.CLOSE_BRACKET.Symbol())) {

                if (mCalculator.IsBinaryOperator(token) || mCalculator.IsPostUnaryOperator(token)) {
                    mInfixExpression.add(token);
                //} else if (token.equals(Calculator.OPEN_BRACKET)) {
                    // close bracket can not be followed by open bracket
                } else if (token.equals(Operator.CLOSE_BRACKET.Symbol())) {
                    mInfixExpression.add(token);
                    // check that number of close bracket is balanced
                    if (!mCalculator.IsSane(mInfixExpression, mBase, false)) {
                        mInfixExpression.remove(mInfixExpression.size() - 1);
                    }
                //} else if (Calculator.IsOperand(token)) {
                    // close bracket can not be followed by operand
                }

            } else if (mCalculator.IsOperandAllowed(last_token, mBase, token.charAt(0))) {
                // add another digit
                mInfixExpression.remove(mInfixExpression.size() - 1);
                // do not keep leading zero before decimal point like 01 -01
                // allow 0.1 -0.1
                if (last_token.equals("0") && !token.equals(Calculator.POINT)) {
                    mInfixExpression.add(token);
                } else if (last_token.equals(Operator.SUBTRACT.Symbol() + "0") && !token.equals(Calculator.POINT)) {
                    mInfixExpression.add(Operator.SUBTRACT.Symbol() + token);
                } else {
                    mInfixExpression.add(last_token + token);
                }

            } else if (mCalculator.IsOperand(last_token, mBase)) {

                if (mCalculator.IsBinaryOperator(token) || mCalculator.IsPostUnaryOperator(token)) {
                    // terminate last operand 0. with 0.0
                    if (last_token.indexOf(Calculator.POINT_CHAR) == last_token.length() - 1) {
                        mInfixExpression.remove(mInfixExpression.size() - 1);
                        mInfixExpression.add(last_token + "0");
                    }
                    mInfixExpression.add(token);
                //} else if (token.equals(Calculator.OPEN_BRACKET)) {
                    // operand can not be followed by open bracket
                } else if (token.equals(Operator.CLOSE_BRACKET.Symbol())) {
                    // terminate last operand 0. with 0.0
                    if (last_token.indexOf(Calculator.POINT_CHAR) == last_token.length() - 1) {
                        mInfixExpression.remove(mInfixExpression.size() - 1);
                        mInfixExpression.add(last_token + "0");
                    }
                    mInfixExpression.add(token);
                    // check that number of close bracket is balanced
                    if (!mCalculator.IsSane(mInfixExpression, mBase, false)) {
                        mInfixExpression.remove(mInfixExpression.size() - 1);
                        // roll back 0.0 fix
                        if (last_token.indexOf(Calculator.POINT_CHAR) == last_token.length() - 1) {
                            mInfixExpression.remove(mInfixExpression.size() - 1);
                            mInfixExpression.add(last_token);
                        }
                    }
                }

            }
        }

        updateText();
    }

    private void evaluate() {
        if (mInfixExpression.isEmpty()) return;

        try {
            Log.v(TAG, "Evaluating "
                    + "(" + mBase
                    + "," + mAngularUnit
                    + ") - " + mInfixExpression.toString());
            String result = mCalculator.Evaluate(mInfixExpression, mBase, mAngularUnit);
            Log.v(TAG, "Result - " + result);
            mInfixExpression.clear();
            mInfixExpression.add(result);
            mIsResultSet = true;
            updateText();
        } catch (Exception e) {
            Log.v(TAG, "evaluate ", e);
            mInfixExpression.clear();
            updateText();
        }
    }

    private void delete() {
        if (mInfixExpression.isEmpty()) return;

        String last_element = mInfixExpression.get(mInfixExpression.size() - 1);
        if (mCalculator.IsOperator(last_element) || last_element.length() == 1 || mIsResultSet) {
            mInfixExpression.remove(mInfixExpression.size() - 1);
        } else {
            mInfixExpression.remove(mInfixExpression.size() - 1);
            mInfixExpression.add(last_element.substring(0, last_element.length() - 1));
        }
        mIsResultSet = false;

        updateText();
    }

    private void clear() {
        mInfixExpression.clear();
        updateText();
    }

    private void updateText() {
        if (mInfixExpression.isEmpty()) {
            mTextDisplay.setText("");
        } else {
            StringBuilder buffer = new StringBuilder();
            for (String item : mInfixExpression) {
                buffer.append(item);
            }
            mTextDisplay.setText(buffer.toString());
        }

        // scroll after text is displayed
        mScrollDisplay.post(new Runnable() {
            public void run() {
                mScrollDisplay.fullScroll(View.FOCUS_RIGHT);
            }
        });

    }

    private void setDigitButtonStates() {
        // enable or disable digit buttons as per current base
        switch (mBase) {
            case HEX:
                mButtonZero.setEnabled(true);
                mButtonOne.setEnabled(true);
                mButtonTwo.setEnabled(true);
                mButtonThree.setEnabled(true);
                mButtonFour.setEnabled(true);
                mButtonFive.setEnabled(true);
                mButtonSix.setEnabled(true);
                mButtonSeven.setEnabled(true);
                mButtonEight.setEnabled(true);
                mButtonNine.setEnabled(true);
                mButtonA.setEnabled(true);
                mButtonB.setEnabled(true);
                mButtonC.setEnabled(true);
                mButtonD.setEnabled(true);
                mButtonE.setEnabled(true);
                mButtonF.setEnabled(true);
                mButtonPoint.setEnabled(false);
                break;
            case DEC:
                mButtonZero.setEnabled(true);
                mButtonOne.setEnabled(true);
                mButtonTwo.setEnabled(true);
                mButtonThree.setEnabled(true);
                mButtonFour.setEnabled(true);
                mButtonFive.setEnabled(true);
                mButtonSix.setEnabled(true);
                mButtonSeven.setEnabled(true);
                mButtonEight.setEnabled(true);
                mButtonNine.setEnabled(true);
                mButtonA.setEnabled(false);
                mButtonB.setEnabled(false);
                mButtonC.setEnabled(false);
                mButtonD.setEnabled(false);
                mButtonE.setEnabled(false);
                mButtonF.setEnabled(false);
                mButtonPoint.setEnabled(true);
                break;
            case OCT:
                mButtonZero.setEnabled(true);
                mButtonOne.setEnabled(true);
                mButtonTwo.setEnabled(true);
                mButtonThree.setEnabled(true);
                mButtonFour.setEnabled(true);
                mButtonFive.setEnabled(true);
                mButtonSix.setEnabled(true);
                mButtonSeven.setEnabled(true);
                mButtonEight.setEnabled(false);
                mButtonNine.setEnabled(false);
                mButtonA.setEnabled(false);
                mButtonB.setEnabled(false);
                mButtonC.setEnabled(false);
                mButtonD.setEnabled(false);
                mButtonE.setEnabled(false);
                mButtonF.setEnabled(false);
                mButtonPoint.setEnabled(false);
                break;
            case BIN:
                mButtonZero.setEnabled(true);
                mButtonOne.setEnabled(true);
                mButtonTwo.setEnabled(false);
                mButtonThree.setEnabled(false);
                mButtonFour.setEnabled(false);
                mButtonFive.setEnabled(false);
                mButtonSix.setEnabled(false);
                mButtonSeven.setEnabled(false);
                mButtonEight.setEnabled(false);
                mButtonNine.setEnabled(false);
                mButtonA.setEnabled(false);
                mButtonB.setEnabled(false);
                mButtonC.setEnabled(false);
                mButtonD.setEnabled(false);
                mButtonE.setEnabled(false);
                mButtonF.setEnabled(false);
                mButtonPoint.setEnabled(false);
                break;
        }
    }

    private void setBaseButtonState() {
        switch (mBase) {
            case HEX:
                mButtonBase.setText(getString(R.string.hexadecimal));
                break;
            case DEC:
                mButtonBase.setText(getString(R.string.decimal));
                break;
            case OCT:
                mButtonBase.setText(getString(R.string.octal));
                break;
            case BIN:
                mButtonBase.setText(getString(R.string.binary));
                break;
        }
    }

    private void setAngularUnitButtonState() {
        switch (mAngularUnit) {
            case DEGREE:
                mButtonAngularUnit.setText(getString(R.string.degree));
                break;
            case RADIAN:
                mButtonAngularUnit.setText(getString(R.string.radian));
                break;
        }
    }
}
