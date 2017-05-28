package com.saraighatsoftware.flexicalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.Vector;

public class ConverterFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private TextView mTextDisplayInput;
    private TextView mTextDisplayOutput;

    private HorizontalScrollView mScrollDisplayInput;
    private HorizontalScrollView mScrollDisplayOutput;

    private Spinner mSpinnerCategory;
    private Spinner mSpinnerInput;
    private Spinner mSpinnerOutput;

    private Vector<Converter> mConverters;

    private StringBuffer mInput;
    private String mOutput;

    public ConverterFragment() {
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
        mConverters = new Vector<>();
        // order and length should be same as as R.array.categories
        mConverters.add(new ConverterVolume(getContext()));
        mConverters.add(new ConverterWeight(getContext()));
        mConverters.add(new ConverterLength(getContext()));
        mConverters.add(new ConverterArea(getContext()));
        mConverters.add(new ConverterFuelEconomy(getContext()));
        mConverters.add(new ConverterTemperature(getContext()));
        mConverters.add(new ConverterPressure(getContext()));
        mConverters.add(new ConverterEnergy(getContext()));
        mConverters.add(new ConverterPower(getContext()));
        mConverters.add(new ConverterTorque(getContext()));
        mConverters.add(new ConverterSpeed(getContext()));
        mConverters.add(new ConverterTime(getContext()));
        mConverters.add(new ConverterData(getContext()));
        mConverters.add(new ConverterAngle(getContext()));
        mInput = new StringBuffer("0");
        mOutput = "";

        View root_view = inflater.inflate(R.layout.fragment_converter, container, false);

        mTextDisplayInput = (TextView) root_view.findViewById(R.id.text_display_input);
        mTextDisplayOutput = (TextView) root_view.findViewById(R.id.text_display_output);

        mTextDisplayInput.setTypeface(FontCache.GetLight(getContext()));
        mTextDisplayOutput.setTypeface(FontCache.GetLight(getContext()));

        mScrollDisplayInput = (HorizontalScrollView) root_view.findViewById(R.id.scroll_display_input);
        mScrollDisplayOutput = (HorizontalScrollView) root_view.findViewById(R.id.scroll_display_output);

        updateText();

        mSpinnerCategory = (Spinner) root_view.findViewById(R.id.spinner_category);
        final CustomArrayAdapter category_adapter = new CustomArrayAdapter(
                getContext(),
                android.R.layout.simple_list_item_1,
                Arrays.asList(getResources().getStringArray(R.array.categories))
        );
        mSpinnerCategory.setAdapter(category_adapter);

        mSpinnerInput = (Spinner) root_view.findViewById(R.id.spinner_input_type);
        final CustomArrayAdapter input_type_adapter = new CustomArrayAdapter(
                getContext(),
                android.R.layout.simple_list_item_1,
                new ArrayList<String>()
        );
        mSpinnerInput.setAdapter(input_type_adapter);

        mSpinnerOutput = (Spinner) root_view.findViewById(R.id.spinner_output_type);
        final CustomArrayAdapter output_type_adapter = new CustomArrayAdapter(
                getContext(),
                android.R.layout.simple_list_item_1,
                new ArrayList<String>()
        );
        mSpinnerOutput.setAdapter(output_type_adapter);

        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final int category = mSpinnerCategory.getSelectedItemPosition();

                input_type_adapter.clear();
                for (String item : mConverters.get(category).getUnits()) {
                    input_type_adapter.add(item);
                }
                mSpinnerInput.setSelection(0);
                input_type_adapter.notifyDataSetChanged();

                output_type_adapter.clear();
                for (String item : mConverters.get(category).getUnits()) {
                    output_type_adapter.add(item);
                }
                mSpinnerOutput.setSelection(1);
                output_type_adapter.notifyDataSetChanged();

                clear();
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
        button.setTypeface(FontCache.GetSemiBold(getContext()));

        button = (Button) root_view.findViewById(R.id.button_one);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("1");
            }
        });
        button.setTypeface(FontCache.GetSemiBold(getContext()));

        button = (Button) root_view.findViewById(R.id.button_two);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("2");
            }
        });
        button.setTypeface(FontCache.GetSemiBold(getContext()));

        button = (Button) root_view.findViewById(R.id.button_three);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("3");
            }
        });
        button.setTypeface(FontCache.GetSemiBold(getContext()));

        button = (Button) root_view.findViewById(R.id.button_four);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("4");
            }
        });
        button.setTypeface(FontCache.GetSemiBold(getContext()));

        button = (Button) root_view.findViewById(R.id.button_five);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("5");
            }
        });
        button.setTypeface(FontCache.GetSemiBold(getContext()));

        button = (Button) root_view.findViewById(R.id.button_six);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("6");
            }
        });
        button.setTypeface(FontCache.GetSemiBold(getContext()));

        button = (Button) root_view.findViewById(R.id.button_seven);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("7");
            }
        });
        button.setTypeface(FontCache.GetSemiBold(getContext()));

        button = (Button) root_view.findViewById(R.id.button_eight);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("8");
            }
        });
        button.setTypeface(FontCache.GetSemiBold(getContext()));

        button = (Button) root_view.findViewById(R.id.button_nine);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("9");
            }
        });
        button.setTypeface(FontCache.GetSemiBold(getContext()));

        button = (Button) root_view.findViewById(R.id.button_point);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.POINT);
            }
        });
        button.setTypeface(FontCache.GetRegular(getContext()));

        button = (Button) root_view.findViewById(R.id.button_delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        button.setTypeface(FontCache.GetRegular(getContext()));

        return root_view;
    }

    private void expressionListener(String token) {
        if (mInput.length() == 1 && mInput.charAt(0) == '0') {
            mInput.deleteCharAt(0);
        }

        if (!token.equals(Calculator.POINT) ||
                (token.equals(".") && mInput.indexOf(Calculator.POINT) == -1)) {
            mInput.append(token);
            evaluate();
        }
    }

    private void delete() {
        mInput.deleteCharAt(mInput.length() - 1);
        if (mInput.length() == 0) {
            mInput.append("0");
        }
        evaluate();
    }

    private void clear() {
        mInput.delete(0, mInput.length());
        mInput.append("0");
        evaluate();
    }

    private void evaluate() {
        final int category = mSpinnerCategory.getSelectedItemPosition();
        final int input = mSpinnerInput.getSelectedItemPosition();
        final int output = mSpinnerOutput.getSelectedItemPosition();
        if (category == AdapterView.INVALID_POSITION ||
                input == AdapterView.INVALID_POSITION ||
                output == AdapterView.INVALID_POSITION) {
            return;
        }
        mOutput = mConverters.get(category).Convert(
                mInput.toString(),
                mConverters.get(category).getUnitFromInteger(input),
                mConverters.get(category).getUnitFromInteger(output));
        updateText();
    }

    private void updateText() {
        View root_view = getView();
        if (root_view == null) return;
        mTextDisplayInput.setText(mInput.toString());
        mTextDisplayOutput.setText(mOutput);

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

    }
}
