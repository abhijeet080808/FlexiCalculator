package com.saraighatsoftware.flexicalculator;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.util.Vector;

public class ConverterFragment extends Fragment {

    private static final String TAG = "ConverterFragment";

    private static final String ARG_SECTION_NUMBER = "section_number";

    private final Calculator mCalculator;
    private final Vector<String> mInfixExpression;
    private boolean mIsResultSet;
    private TextView mTextDisplay;
    private HorizontalScrollView mScrollDisplay;

    private Calculator.Base mBase;

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

    private Calculator.AngularUnit mAngularUnit;

    public ConverterFragment() {
        mCalculator = new Calculator();
        mInfixExpression = new Vector<>();
        mIsResultSet = false;
        mBase = Calculator.Base.DEC;
        mAngularUnit = Calculator.AngularUnit.DEGREE;
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
        View root_view = inflater.inflate(R.layout.fragment_converter, container, false);

        mInfixExpression.clear();
        mIsResultSet = false;
        mBase = Calculator.Base.DEC;
        mAngularUnit = Calculator.AngularUnit.DEGREE;

        mTextDisplay = (TextView) root_view.findViewById(R.id.text_display);
        mTextDisplay.setText("0");
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        Typeface display_font = Typeface.createFromAsset(getContext().getAssets(),  "fonts/Teko-Light.ttf");
        mTextDisplay.setTypeface(display_font);

        mScrollDisplay = (HorizontalScrollView) root_view.findViewById(R.id.scroll_display);

        Button button;
        Typeface button_font = Typeface.createFromAsset(getContext().getAssets(),  "fonts/Teko-Regular.ttf");
        Typeface bold_button_font = Typeface.createFromAsset(getContext().getAssets(),  "fonts/Teko-SemiBold.ttf");

        mButtonZero = (Button) root_view.findViewById(R.id.button_zero);
        mButtonZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("0");
            }
        });
        mButtonZero.setTypeface(bold_button_font);

        mButtonOne = (Button) root_view.findViewById(R.id.button_one);
        mButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("1");
            }
        });
        mButtonOne.setTypeface(bold_button_font);

        mButtonTwo = (Button) root_view.findViewById(R.id.button_two);
        mButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("2");
            }
        });
        mButtonTwo.setTypeface(bold_button_font);

        mButtonThree = (Button) root_view.findViewById(R.id.button_three);
        mButtonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("3");
            }
        });
        mButtonThree.setTypeface(bold_button_font);

        mButtonFour = (Button) root_view.findViewById(R.id.button_four);
        mButtonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("4");
            }
        });
        mButtonFour.setTypeface(bold_button_font);

        mButtonFive = (Button) root_view.findViewById(R.id.button_five);
        mButtonFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("5");
            }
        });
        mButtonFive.setTypeface(bold_button_font);

        mButtonSix = (Button) root_view.findViewById(R.id.button_six);
        mButtonSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("6");
            }
        });
        mButtonSix.setTypeface(bold_button_font);

        mButtonSeven = (Button) root_view.findViewById(R.id.button_seven);
        mButtonSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("7");
            }
        });
        mButtonSeven.setTypeface(bold_button_font);

        mButtonEight = (Button) root_view.findViewById(R.id.button_eight);
        mButtonEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("8");
            }
        });
        mButtonEight.setTypeface(bold_button_font);

        mButtonNine = (Button) root_view.findViewById(R.id.button_nine);
        mButtonNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("9");
            }
        });
        mButtonNine.setTypeface(bold_button_font);

        mButtonA = (Button) root_view.findViewById(R.id.button_a);
        mButtonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("A");
            }
        });
        mButtonA.setTypeface(button_font);

        mButtonB = (Button) root_view.findViewById(R.id.button_b);
        mButtonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("B");
            }
        });
        mButtonB.setTypeface(button_font);

        mButtonC = (Button) root_view.findViewById(R.id.button_c);
        mButtonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("C");
            }
        });
        mButtonC.setTypeface(button_font);

        mButtonD = (Button) root_view.findViewById(R.id.button_d);
        mButtonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("D");
            }
        });
        mButtonD.setTypeface(button_font);

        mButtonE = (Button) root_view.findViewById(R.id.button_e);
        mButtonE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("E");
            }
        });
        mButtonE.setTypeface(button_font);

        mButtonF = (Button) root_view.findViewById(R.id.button_f);
        mButtonF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("F");
            }
        });
        mButtonF.setTypeface(button_font);

        mButtonPoint = (Button) root_view.findViewById(R.id.button_point);
        mButtonPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.POINT);
            }
        });
        mButtonPoint.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_open_bracket);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.OPEN_BRACKET);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_close_bracket);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.CLOSE_BRACKET);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_divide);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.DIVIDE);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_multiply);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.MULTIPLY);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_subtract);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.SUBTRACT);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.ADD);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_equal);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluate();
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_modulus);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.MODULUS);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_power);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.POWER);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_sin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.SIN);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_cos);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.COS);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_tan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.TAN);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_log);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.LOG);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_ln);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.LN);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_percentage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.PERCENTAGE);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_factorial);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.FACTORIAL);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_square_root);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.SQUARE_ROOT);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_square);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.SQUARE);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_lsh);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.LSH);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_rsh);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.RSH);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_and);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.AND);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_or);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.OR);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_xor);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.XOR);
            }
        });
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_base);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hex -> dec -> oct -> bin->hex
                Calculator.Base old_base = mBase;
                switch (mBase) {
                    case HEX:
                        ((Button) v).setText(getString(R.string.decimal));
                        mBase = Calculator.Base.DEC;
                        break;
                    case DEC:
                        ((Button) v).setText(getString(R.string.octal));
                        mBase = Calculator.Base.OCT;
                        break;
                    case OCT:
                        ((Button) v).setText(getString(R.string.binary));
                        mBase = Calculator.Base.BIN;
                        break;
                    case BIN:
                        ((Button) v).setText(getString(R.string.hexadecimal));
                        mBase = Calculator.Base.HEX;
                        break;
                }
                setDigitButtonStates();

                if (mInfixExpression.size() == 1 &&
                        mCalculator.IsOperand(mInfixExpression.firstElement(), old_base)) {
                    String old_operand = mInfixExpression.firstElement();
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
        button.setTypeface(button_font);

        button = (Button) root_view.findViewById(R.id.button_angular_unit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mAngularUnit) {
                    case DEGREE:
                        ((Button) v).setText(getString(R.string.radian));
                        mAngularUnit = Calculator.AngularUnit.RADIAN;
                        break;
                    case RADIAN:
                        ((Button) v).setText(getString(R.string.degree));
                        mAngularUnit = Calculator.AngularUnit.DEGREE;
                        break;
                }
            }
        });
        button.setTypeface(button_font);

        setDigitButtonStates();
        return root_view;
    }

    private void expressionListener(String token) {
        // reads 0-9 and operators
        if (mInfixExpression.isEmpty()) {

            // allow only operand to be stored or open bracket or pre operators
            if (mCalculator.IsOperandAllowed("", mBase, token.charAt(0))) {
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
            } else if (mCalculator.IsOperandAllowed("", mBase, token.charAt(0))) {
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

                if(mCalculator.IsOperandAllowed(last_token, mBase, token.charAt(0))) {
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
                } else if (mCalculator.IsOperandAllowed("", mBase, token.charAt(0))) {
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
                } else if (mCalculator.IsOperandAllowed("", mBase, token.charAt(0))) {
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
                } else if (mCalculator.IsOperandAllowed("", mBase, token.charAt(0))) {
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
                    if (!mCalculator.IsSane(mInfixExpression, mBase, false)) {
                        mInfixExpression.remove(mInfixExpression.size() - 1);
                    }
                //} else if (Calculator.IsOperand(token)) {
                    // close bracket can not be followed by operand
                }

            } else if (mCalculator.IsOperandAllowed(last_token, mBase, token.charAt(0))) {

                mInfixExpression.remove(mInfixExpression.size() - 1);
                mInfixExpression.add(last_token + token);

            } else if (mCalculator.IsOperand(last_token, mBase)) {

                if (mCalculator.IsBinaryOperator(token) || mCalculator.IsPostUnaryOperator(token)) {
                    mInfixExpression.add(token);
                //} else if (token.equals(Calculator.OPEN_BRACKET)) {
                    // operand can not be followed by open bracket
                } else if (token.equals(Calculator.CLOSE_BRACKET)) {
                    mInfixExpression.add(token);
                    // check that number of close bracket is balanced
                    if (!mCalculator.IsSane(mInfixExpression, mBase, false)) {
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
}
