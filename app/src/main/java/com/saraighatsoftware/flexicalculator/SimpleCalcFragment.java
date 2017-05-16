package com.saraighatsoftware.flexicalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.util.Vector;

public class SimpleCalcFragment extends Fragment {

    private static final String TAG = "SimpleCalcFragment";

    private static final String ARG_SECTION_NUMBER = "section_number";

    private final Calculator mCalculator;
    private final Vector<String> mInfixExpression;
    private boolean mIsResultSet;
    private TextView mTextDisplay;
    private HorizontalScrollView mScrollDisplay;

    public SimpleCalcFragment() {
        mCalculator = new Calculator();
        mInfixExpression = new Vector<>();
        mIsResultSet = false;
    }

    public void setArguments(int sectionNumber) {
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_calculator, container, false);

        mInfixExpression.clear();
        mIsResultSet = false;

        mTextDisplay = (TextView) root_view.findViewById(R.id.text_display);
        mTextDisplay.setText("0");
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        mScrollDisplay = (HorizontalScrollView) root_view.findViewById(R.id.scroll_display);

        root_view.findViewById(R.id.button_zero).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("0");
            }
        });

        root_view.findViewById(R.id.button_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("1");
            }
        });

        root_view.findViewById(R.id.button_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("2");
            }
        });

        root_view.findViewById(R.id.button_three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("3");
            }
        });

        root_view.findViewById(R.id.button_four).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("4");
            }
        });

        root_view.findViewById(R.id.button_five).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("5");
            }
        });

        root_view.findViewById(R.id.button_six).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("6");
            }
        });

        root_view.findViewById(R.id.button_seven).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("7");
            }
        });

        root_view.findViewById(R.id.button_eight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("8");
            }
        });

        root_view.findViewById(R.id.button_nine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("9");
            }
        });

        root_view.findViewById(R.id.button_decimal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.DECIMAL);
            }
        });

        root_view.findViewById(R.id.button_open_bracket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.OPEN_BRACKET);
            }
        });

        root_view.findViewById(R.id.button_close_bracket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.CLOSE_BRACKET);
            }
        });

        root_view.findViewById(R.id.button_divide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.DIVIDE);
            }
        });

        root_view.findViewById(R.id.button_multiply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.MULTIPLY);
            }
        });

        root_view.findViewById(R.id.button_subtract).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.SUBTRACT);
            }
        });

        root_view.findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.ADD);
            }
        });

        root_view.findViewById(R.id.button_equal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluate();
            }
        });

        root_view.findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

        root_view.findViewById(R.id.button_modulus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.MODULUS);
            }
        });

        root_view.findViewById(R.id.button_power).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.POWER);
            }
        });

        root_view.findViewById(R.id.button_sin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.SIN);
            }
        });

        root_view.findViewById(R.id.button_cos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.COS);
            }
        });

        root_view.findViewById(R.id.button_tan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.TAN);
            }
        });

        root_view.findViewById(R.id.button_log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.LOG);
            }
        });

        root_view.findViewById(R.id.button_ln).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.LN);
            }
        });

        return root_view;
    }

    private void expressionListener(String token) {
        // reads 0-9 and operators
        if (mInfixExpression.isEmpty()) {

            // allow only operand to be stored or open bracket or pre operators
            if (mCalculator.IsOperandAllowed("", token.charAt(0))) {
                mInfixExpression.add(token);
            } else if (token.equals(Calculator.OPEN_BRACKET)) {
                mInfixExpression.add(token);
            } else if (mCalculator.IsPreUnaryOperator(token)) {
                mInfixExpression.add(token);
            }

        } else if (mIsResultSet) {

            if (mCalculator.IsBinaryOperator(token) || mCalculator.IsPostUnaryOperator(token)) {
                mInfixExpression.add(token);
                mIsResultSet = false;
            } else if (token.equals(Calculator.OPEN_BRACKET)) {
                mInfixExpression.remove(mInfixExpression.size() - 1);
                mInfixExpression.add(token);
                mIsResultSet = false;
            //} else if (token.equals(Calculator.CLOSE_BRACKET)) {
                // result operand can not be followed by close bracket
            } else if (mCalculator.IsOperandAllowed("", token.charAt(0))) {
                mInfixExpression.remove(mInfixExpression.size() - 1);
                mInfixExpression.add(token);
                mIsResultSet = false;
            }

        } else {

            final String last_token = mInfixExpression.lastElement();
            final String last_to_last_token =
                    mInfixExpression.size() > 1 ? mInfixExpression.get(mInfixExpression.size() - 2) : "";

            // '-' is a special case
            // consider as part of operand only when it is the first operand
            // or when it is just after an open bracket

            if (last_token.equals(Calculator.SUBTRACT) &&
                    (last_to_last_token.equals(Calculator.OPEN_BRACKET) || last_to_last_token.equals(""))) {

                if(mCalculator.IsOperandAllowed(last_token, token.charAt(0))) {
                    mInfixExpression.remove(mInfixExpression.size() - 1);
                    mInfixExpression.add(last_token + token);
                }

            } else if (mCalculator.IsBinaryOperator(last_token)) {

                if (mCalculator.IsBinaryOperator(token)) {
                    mInfixExpression.remove(mInfixExpression.size() - 1);
                    mInfixExpression.add(token);
                } else if (mCalculator.IsPreUnaryOperator(token)) {
                    mInfixExpression.add(token);
                } else if (token.equals(Calculator.OPEN_BRACKET)) {
                    mInfixExpression.add(token);
                //} else if (token.equals(Calculator.CLOSE_BRACKET)) {
                    // operator can not be followed by close bracket
                } else if (mCalculator.IsOperandAllowed("", token.charAt(0))) {
                    mInfixExpression.add(token);
                }

            } else if (mCalculator.IsPreUnaryOperator(last_token)) {

                if (mCalculator.IsPreUnaryOperator(token)) {
                    mInfixExpression.remove(mInfixExpression.size() - 1);
                    mInfixExpression.add(token);
                } else if (token.equals(Calculator.OPEN_BRACKET)) {
                    mInfixExpression.add(token);
                //} else if (token.equals(Calculator.CLOSE_BRACKET)) {
                    // operator can not be followed by close bracket
                } else if (mCalculator.IsOperandAllowed("", token.charAt(0))) {
                    mInfixExpression.add(token);
                }

            } else if (mCalculator.IsPostUnaryOperator(last_token)) {

                if (mCalculator.IsPostUnaryOperator(token)) {
                    mInfixExpression.remove(mInfixExpression.size() - 1);
                    mInfixExpression.add(token);
                } else if (token.equals(Calculator.CLOSE_BRACKET)) {
                    mInfixExpression.add(token);
                } else if (mCalculator.IsBinaryOperator(token)) {
                    mInfixExpression.add(token);
                }

            } else if (last_token.equals(Calculator.OPEN_BRACKET)) {

                //if (Calculator.IsOperator(token, false)) {
                    // open bracket can not be followed by operator
                //} else
                if (mCalculator.IsPreUnaryOperator(token)) {
                    mInfixExpression.add(token);
                } else if (token.equals(Calculator.OPEN_BRACKET)) {
                    mInfixExpression.add(token);
                //} else if (token.equals(Calculator.CLOSE_BRACKET)) {
                    // open bracket can not be followed by close bracket
                } else if (mCalculator.IsOperandAllowed("", token.charAt(0))) {
                    mInfixExpression.add(token);
                }

            } else if (last_token.equals(Calculator.CLOSE_BRACKET)) {

                if (mCalculator.IsBinaryOperator(token) || mCalculator.IsPostUnaryOperator(token)) {
                    mInfixExpression.add(token);
                //} else if (token.equals(Calculator.OPEN_BRACKET)) {
                    // close bracket can not be followed by open bracket
                } else if (token.equals(Calculator.CLOSE_BRACKET)) {
                    mInfixExpression.add(token);
                    // check that number of close bracket is balanced
                    if (!mCalculator.IsSane(mInfixExpression, false)) {
                        mInfixExpression.remove(mInfixExpression.size() - 1);
                    }
                //} else if (Calculator.IsOperand(token)) {
                    // close bracket can not be followed by operand
                }

            } else if (mCalculator.IsOperandAllowed(last_token, token.charAt(0))) {

                mInfixExpression.remove(mInfixExpression.size() - 1);
                mInfixExpression.add(last_token + token);

            } else if (mCalculator.IsOperand(last_token)) {

                if (mCalculator.IsBinaryOperator(token) || mCalculator.IsPostUnaryOperator(token)) {
                    mInfixExpression.add(token);
                //} else if (token.equals(Calculator.OPEN_BRACKET)) {
                    // operand can not be followed by open bracket
                } else if (token.equals(Calculator.CLOSE_BRACKET)) {
                    mInfixExpression.add(token);
                    // check that number of close bracket is balanced
                    if (!mCalculator.IsSane(mInfixExpression, false)) {
                        mInfixExpression.remove(mInfixExpression.size() - 1);
                    }
                }

            }
        }

        updateText();
    }

    private void evaluate() {
        if (mInfixExpression.isEmpty()) return;

        try {
            Log.v(TAG, "Evaluating " + mInfixExpression.toString());
            String result = mCalculator.Evaluate(mInfixExpression);
            Log.v(TAG, "Result " + result);
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

    private void clear() {
        if (mInfixExpression.isEmpty()) return;

        String last_element = mInfixExpression.lastElement();
        if (last_element.equals(Calculator.SIN)
                || last_element.equals(Calculator.COS)
                || last_element.equals(Calculator.TAN)
                || last_element.equals(Calculator.LOG)
                || last_element.equals(Calculator.LN)
                || last_element.equals(Calculator.MODULUS)) {
            mInfixExpression.remove(mInfixExpression.size() - 1);
        } else if (last_element.length() == 1 || mIsResultSet) {
            mInfixExpression.remove(mInfixExpression.size() - 1);
        } else {
            mInfixExpression.remove(mInfixExpression.size() - 1);
            mInfixExpression.add(last_element.substring(0, last_element.length() - 1));
        }
        mIsResultSet = false;

        updateText();
    }

    private void updateText() {
        View root_view = getView();
        if (root_view == null) return;
        if (mInfixExpression.isEmpty()) {
            mTextDisplay.setText("0");
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
}
