package com.saraighatsoftware.flexicalculator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ConverterFragment extends Fragment {

    private static final String TAG = "ConverterFragment";

    private static final String ARG_INPUT_TEXT = "input_text";
    private static final String ARG_OUTPUT_TEXT = "output_text";
    private static final String ARG_INPUT_SCROLL_POS = "input_scroll_pos";
    private static final String ARG_OUTPUT_SCROLL_POS = "output_scroll_pos";
    private static final String ARG_LAST_SELECTED_CATEGORY = "last_selected_category";

    private TextView mTextDisplayInput;
    private TextView mTextDisplayOutput;

    private HorizontalScrollView mScrollDisplayInput;
    private HorizontalScrollView mScrollDisplayOutput;

    private Spinner mSpinnerCategory;
    private Spinner mSpinnerInput;
    private Spinner mSpinnerOutput;

    private Converter[] mConverters;

    private StringBuffer mInput;
    private String mOutput;

    private int mLastSelectedCategory;

    public ConverterFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_INPUT_TEXT, mInput.toString());
        outState.putString(ARG_OUTPUT_TEXT, mOutput);
        outState.putIntArray(ARG_INPUT_SCROLL_POS,
                new int[]{ mScrollDisplayInput.getScrollX(), mScrollDisplayInput.getScrollY()});
        outState.putIntArray(ARG_OUTPUT_SCROLL_POS,
                new int[]{ mScrollDisplayOutput.getScrollX(), mScrollDisplayOutput.getScrollY()});
        outState.putInt(ARG_LAST_SELECTED_CATEGORY, mLastSelectedCategory);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // restore view state automatically
        super.onCreateView(inflater, container, savedInstanceState);

        Context context = getContext();
        // order and length should be same as as R.array.categories
        mConverters = new Converter[] {
                new ConverterVolume(context),
                new ConverterWeight(context),
                new ConverterLength(context),
                new ConverterArea(context),
                new ConverterFuelEconomy(context),
                new ConverterTemperature(context),
                new ConverterPressure(context),
                new ConverterEnergy(context),
                new ConverterPower(context),
                new ConverterTorque(context),
                new ConverterSpeed(context),
                new ConverterTime(context),
                new ConverterData(context),
                new ConverterAngle(context)
        };

        int[] scroll_position_input = null;
        int[] scroll_position_output = null;

        if (savedInstanceState == null) {
            mInput = new StringBuffer();
            mOutput = "";
            mLastSelectedCategory = -1;
        } else {
            mInput = new StringBuffer(savedInstanceState.getString(ARG_INPUT_TEXT));
            mOutput = savedInstanceState.getString(ARG_OUTPUT_TEXT);
            scroll_position_input = savedInstanceState.getIntArray(ARG_INPUT_SCROLL_POS);
            scroll_position_output = savedInstanceState.getIntArray(ARG_OUTPUT_SCROLL_POS);
            mLastSelectedCategory = savedInstanceState.getInt(ARG_LAST_SELECTED_CATEGORY);
        }

        View root_view = inflater.inflate(R.layout.fragment_converter, container, false);

        mTextDisplayInput = (TextView) root_view.findViewById(R.id.text_display_input);
        mTextDisplayOutput = (TextView) root_view.findViewById(R.id.text_display_output);

        mTextDisplayInput.setTypeface(FontCache.GetLight(context));
        mTextDisplayOutput.setTypeface(FontCache.GetLight(context));

        mScrollDisplayInput = (HorizontalScrollView) root_view.findViewById(R.id.scroll_display_input);
        mScrollDisplayOutput = (HorizontalScrollView) root_view.findViewById(R.id.scroll_display_output);

        mSpinnerCategory = (Spinner) root_view.findViewById(R.id.spinner_category);
        final CustomArrayAdapter category_adapter = new CustomArrayAdapter(
                context,
                android.R.layout.simple_list_item_1,
                Arrays.asList(getResources().getStringArray(R.array.categories))
        );
        mSpinnerCategory.setAdapter(category_adapter);

        mSpinnerInput = (Spinner) root_view.findViewById(R.id.spinner_input_type);
        final CustomArrayAdapter input_type_adapter = new CustomArrayAdapter(
                context,
                android.R.layout.simple_list_item_1,
                new ArrayList<String>()
        );
        mSpinnerInput.setAdapter(input_type_adapter);

        mSpinnerOutput = (Spinner) root_view.findViewById(R.id.spinner_output_type);
        final CustomArrayAdapter output_type_adapter = new CustomArrayAdapter(
                context,
                android.R.layout.simple_list_item_1,
                new ArrayList<String>()
        );
        mSpinnerOutput.setAdapter(output_type_adapter);

        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // this is erroneously called when activity is created or fragment is (re)created
                final int category = mSpinnerCategory.getSelectedItemPosition();

                input_type_adapter.clear();
                for (String item : mConverters[category].getUnits()) {
                    input_type_adapter.add(item);
                }
                mSpinnerInput.setSelection(0);
                input_type_adapter.notifyDataSetChanged();

                output_type_adapter.clear();
                for (String item : mConverters[category].getUnits()) {
                    output_type_adapter.add(item);
                }
                mSpinnerOutput.setSelection(1);
                output_type_adapter.notifyDataSetChanged();

                if (category != mLastSelectedCategory) {
                    clear();
                }
                mLastSelectedCategory = category;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                evaluate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerOutput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                evaluate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button button;
        button = (Button) root_view.findViewById(R.id.button_zero);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("0");
            }
        });
        button.setTypeface(FontCache.GetSemiBold(context));

        button = (Button) root_view.findViewById(R.id.button_one);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("1");
            }
        });
        button.setTypeface(FontCache.GetSemiBold(context));

        button = (Button) root_view.findViewById(R.id.button_two);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("2");
            }
        });
        button.setTypeface(FontCache.GetSemiBold(context));

        button = (Button) root_view.findViewById(R.id.button_three);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("3");
            }
        });
        button.setTypeface(FontCache.GetSemiBold(context));

        button = (Button) root_view.findViewById(R.id.button_four);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("4");
            }
        });
        button.setTypeface(FontCache.GetSemiBold(context));

        button = (Button) root_view.findViewById(R.id.button_five);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("5");
            }
        });
        button.setTypeface(FontCache.GetSemiBold(context));

        button = (Button) root_view.findViewById(R.id.button_six);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("6");
            }
        });
        button.setTypeface(FontCache.GetSemiBold(context));

        button = (Button) root_view.findViewById(R.id.button_seven);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("7");
            }
        });
        button.setTypeface(FontCache.GetSemiBold(context));

        button = (Button) root_view.findViewById(R.id.button_eight);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("8");
            }
        });
        button.setTypeface(FontCache.GetSemiBold(context));

        button = (Button) root_view.findViewById(R.id.button_nine);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("9");
            }
        });
        button.setTypeface(FontCache.GetSemiBold(context));

        button = (Button) root_view.findViewById(R.id.button_point);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.POINT);
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

        updateText(scroll_position_input, scroll_position_output);

        return root_view;
    }

    private void expressionListener(String token) {
        // this is the one and only POINT
        if (token.equals(Calculator.POINT) && mInput.indexOf(Calculator.POINT) == -1) {
            if (mInput.length() == 0) {
                mInput.append("0");
            }
            mInput.append(token);
            evaluate();
        } else if (!token.equals(Calculator.POINT)) {
            // replace 0 with this new digit
            if (mInput.length() == 1 && mInput.charAt(0) == '0') {
                mInput.deleteCharAt(0);
            }
            mInput.append(token);
            evaluate();
        }
    }

    private void delete() {
        if (mInput.length() != 0) {
            mInput.deleteCharAt(mInput.length() - 1);
        }
        evaluate();
    }

    private void clear() {
        mInput.delete(0, mInput.length());
        evaluate();
    }

    private void evaluate() {
        if (mInput.length() == 0) {
            mOutput = "";
        } else {
            final int category = mSpinnerCategory.getSelectedItemPosition();
            final int input = mSpinnerInput.getSelectedItemPosition();
            final int output = mSpinnerOutput.getSelectedItemPosition();
            if (category == AdapterView.INVALID_POSITION ||
                    input == AdapterView.INVALID_POSITION ||
                    output == AdapterView.INVALID_POSITION) {
                return;
            }

            try {
                mOutput = mConverters[category].Convert(
                        mInput.toString(),
                        mConverters[category].getUnitFromInteger(input),
                        mConverters[category].getUnitFromInteger(output));
            } catch (Exception e) {
                Log.v(TAG, "evaluate ", e);
                mOutput = "";
            }
        }
        updateText(null, null);
    }

    private void updateText(final int[] scrollPositionInput, final int[] scrollPositionOutput) {
        mTextDisplayInput.setText(mInput.toString());
        mTextDisplayOutput.setText(mOutput);

        if (scrollPositionInput == null || scrollPositionOutput == null) {
            // scroll after text is displayed
            mScrollDisplayInput.post(new Runnable() {
                public void run() {
                    mScrollDisplayInput.fullScroll(View.FOCUS_RIGHT);
                }
            });
            mScrollDisplayOutput.post(new Runnable() {
                public void run() {
                    mScrollDisplayOutput.fullScroll(View.FOCUS_RIGHT);
                }
            });
        } else {
            mScrollDisplayInput.post(new Runnable() {
                public void run() {
                    mScrollDisplayInput.scrollTo(scrollPositionInput[0], scrollPositionInput[1]);
                }
            });
            mScrollDisplayOutput.post(new Runnable() {
                public void run() {
                    mScrollDisplayOutput.scrollTo(scrollPositionOutput[0], scrollPositionOutput[1]);
                }
            });
        }
    }
}
