package com.saraighatsoftware.flexicalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Vector;

public class ConventionalCalcFragment extends Fragment {

    private static final String TAG = "ConvCalcFragment";

    private static final String ARG_SECTION_NUMBER = "section_number";

    private Calculator mCalculator;
    private Vector<String> mInfixExpression;
    private boolean mIsResultSet;

    public ConventionalCalcFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_calculator, container, false);

        mInfixExpression.clear();
        mIsResultSet = false;

        TextView textView = (TextView) rootView.findViewById(R.id.text_display);
        textView.setText("0");
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        rootView.findViewById(R.id.button_zero).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("0");
            }
        });

        rootView.findViewById(R.id.button_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("1");
            }
        });

        rootView.findViewById(R.id.button_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("2");
            }
        });

        rootView.findViewById(R.id.button_three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("3");
            }
        });

        rootView.findViewById(R.id.button_four).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("4");
            }
        });

        rootView.findViewById(R.id.button_five).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("5");
            }
        });

        rootView.findViewById(R.id.button_six).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("6");
            }
        });

        rootView.findViewById(R.id.button_seven).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("7");
            }
        });

        rootView.findViewById(R.id.button_eight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("8");
            }
        });

        rootView.findViewById(R.id.button_nine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("9");
            }
        });

        rootView.findViewById(R.id.button_open_bracket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.OPEN_BRACKET);
            }
        });

        rootView.findViewById(R.id.button_close_bracket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.CLOSE_BRACKET);
            }
        });

        rootView.findViewById(R.id.button_divide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.DIVIDE);
            }
        });

        rootView.findViewById(R.id.button_multiply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.MULTIPLY);
            }
        });

        rootView.findViewById(R.id.button_subtract).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.SUBTRACT);
            }
        });

        rootView.findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.ADD);
            }
        });

        rootView.findViewById(R.id.button_equal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluate();
            }
        });

        rootView.findViewById(R.id.button_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

        return rootView;
    }

    private void expressionListener(String token) {
        // reads 0-9 and operators
        if (mInfixExpression.isEmpty()) {
            // allow only operand to be stored or open bracket
            if (Calculator.IsOperand(token)) {
                mInfixExpression.add(token);
            } else if (token.equals(Calculator.OPEN_BRACKET)) {
                mInfixExpression.add(token);
            }
        } else if (mIsResultSet) {

            if (Calculator.IsOperator(token, false)) {
                mInfixExpression.add(token);
                mIsResultSet = false;
            } else if (token.equals(Calculator.OPEN_BRACKET)) {
                mInfixExpression.remove(mInfixExpression.size() - 1);
                mInfixExpression.add(token);
                mIsResultSet = false;
            } else if (token.equals(Calculator.CLOSE_BRACKET)) {
                // result operand can not be followed by close bracket
            } else if (Calculator.IsOperand(token)) {
                mInfixExpression.remove(mInfixExpression.size() - 1);
                mInfixExpression.add(token);
                mIsResultSet = false;
            }

        } else {
            String last_token = mInfixExpression.lastElement();

            if (Calculator.IsOperator(last_token, false)) {

                if (Calculator.IsOperator(token, false)) {
                    mInfixExpression.remove(mInfixExpression.size() - 1);
                    mInfixExpression.add(token);
                } else if (token.equals(Calculator.OPEN_BRACKET)) {
                    mInfixExpression.add(token);
                } else if (token.equals(Calculator.CLOSE_BRACKET)) {
                    // operator can not be followed by close bracket
                } else if (Calculator.IsOperand(token)) {
                    mInfixExpression.add(token);
                }

            } else if (last_token.equals(Calculator.OPEN_BRACKET)) {

                if (Calculator.IsOperator(token, false)) {
                    // open bracket can not be followed by operator
                } else if (token.equals(Calculator.OPEN_BRACKET)) {
                    mInfixExpression.add(token);
                } else if (token.equals(Calculator.CLOSE_BRACKET)) {
                    // open bracket can not be followed by close bracket
                } else if (Calculator.IsOperand(token)) {
                    mInfixExpression.add(token);
                }

            } else if (last_token.equals(Calculator.CLOSE_BRACKET)) {

                if (Calculator.IsOperator(token, false)) {
                    mInfixExpression.add(token);
                } else if (token.equals(Calculator.OPEN_BRACKET)) {
                    // close bracket can not be followed by open bracket
                } else if (token.equals(Calculator.CLOSE_BRACKET)) {
                    mInfixExpression.add(token);
                    // check that number of close bracket is balanced
                    if (!Calculator.IsSane(mInfixExpression, false)) {
                        mInfixExpression.remove(mInfixExpression.size() - 1);
                    }
                } else if (Calculator.IsOperand(token)) {
                    // close bracket can not be followed by operand
                }

            } else if (Calculator.IsOperand(last_token)) {

                if (Calculator.IsOperator(token, false)) {
                    mInfixExpression.add(token);
                } else if (token.equals(Calculator.OPEN_BRACKET)) {
                    // operand can not be followed by open bracket
                } else if (token.equals(Calculator.CLOSE_BRACKET)) {
                    mInfixExpression.add(token);
                    // check that number of close bracket is balanced
                    if (!Calculator.IsSane(mInfixExpression, false)) {
                        mInfixExpression.remove(mInfixExpression.size() - 1);
                    }
                } else if (Calculator.IsOperand(token)) {
                    // check for allowed size
                    if (Calculator.IsAllowed(last_token + token)) {
                        mInfixExpression.remove(mInfixExpression.size() - 1);
                        mInfixExpression.add(last_token + token);
                    }
                }

            }
        }

        updateText();
    }

    private void evaluate() {
        if (mInfixExpression.isEmpty()) return;

        try {
            String result = mCalculator.Evaluate(mInfixExpression);
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
        if (last_element.length() == 1 || mIsResultSet) {
            mInfixExpression.remove(mInfixExpression.size() - 1);
        } else {
            mInfixExpression.remove(mInfixExpression.size() - 1);
            mInfixExpression.add(last_element.substring(0, last_element.length() - 1));
        }
        mIsResultSet = false;

        updateText();
    }

    private void updateText() {
        TextView textView = (TextView) getView().findViewById(R.id.text_display);
        if (mInfixExpression.isEmpty()) {
            textView.setText("0");
        } else {
            StringBuilder buffer = new StringBuilder();
            for (String item : mInfixExpression) {
                buffer.append(item);
            }
            textView.setText(buffer.toString());
        }
    }
}
