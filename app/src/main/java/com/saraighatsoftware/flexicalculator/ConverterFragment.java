package com.saraighatsoftware.flexicalculator;

import android.graphics.Typeface;
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
        mConverters.add(new ConverterVolume(getContext()));
        mInput = new StringBuffer();

        View root_view = inflater.inflate(R.layout.fragment_converter, container, false);

        mTextDisplayInput = (TextView) root_view.findViewById(R.id.text_display_input);
        mTextDisplayInput.setText("0");
        mTextDisplayOutput = (TextView) root_view.findViewById(R.id.text_display_output);
        mTextDisplayOutput.setText("0");

        Typeface display_font = Typeface.createFromAsset(getContext().getAssets(),  "fonts/Teko-Light.ttf");
        mTextDisplayInput.setTypeface(display_font);
        mTextDisplayOutput.setTypeface(display_font);

        mScrollDisplayInput = (HorizontalScrollView) root_view.findViewById(R.id.scroll_display_input);
        mScrollDisplayOutput = (HorizontalScrollView) root_view.findViewById(R.id.scroll_display_output);

        Typeface button_font = Typeface.createFromAsset(getContext().getAssets(),  "fonts/Teko-Regular.ttf");
        Typeface bold_button_font = Typeface.createFromAsset(getContext().getAssets(),  "fonts/Teko-SemiBold.ttf");

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
                for (String item : mConverters.get(category).GetUnits()) {
                    input_type_adapter.add(item);
                }
                input_type_adapter.notifyDataSetChanged();

                output_type_adapter.clear();
                for (String item : mConverters.get(category).GetUnits()) {
                    output_type_adapter.add(item);
                }
                output_type_adapter.notifyDataSetChanged();

                evaluate();
                updateText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                evaluate();
                updateText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerOutput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                evaluate();
                updateText();
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
        button.setTypeface(bold_button_font);

        button = (Button) root_view.findViewById(R.id.button_one);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("1");
            }
        });
        button.setTypeface(bold_button_font);

        button = (Button) root_view.findViewById(R.id.button_two);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("2");
            }
        });
        button.setTypeface(bold_button_font);

        button = (Button) root_view.findViewById(R.id.button_three);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("3");
            }
        });
        button.setTypeface(bold_button_font);

        button = (Button) root_view.findViewById(R.id.button_four);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("4");
            }
        });
        button.setTypeface(bold_button_font);

        button = (Button) root_view.findViewById(R.id.button_five);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("5");
            }
        });
        button.setTypeface(bold_button_font);

        button = (Button) root_view.findViewById(R.id.button_six);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("6");
            }
        });
        button.setTypeface(bold_button_font);

        button = (Button) root_view.findViewById(R.id.button_seven);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("7");
            }
        });
        button.setTypeface(bold_button_font);

        button = (Button) root_view.findViewById(R.id.button_eight);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("8");
            }
        });
        button.setTypeface(bold_button_font);

        button = (Button) root_view.findViewById(R.id.button_nine);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener("9");
            }
        });
        button.setTypeface(bold_button_font);

        button = (Button) root_view.findViewById(R.id.button_point);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.POINT);
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

        return root_view;
    }

    private void expressionListener(String token) {
        if (!token.equals(Calculator.POINT) ||
                (token.equals(".") && mInput.indexOf(Calculator.POINT) == -1)) {
            mInput.append(token);
        } else {
            return;
        }

        evaluate();
        updateText();
    }

    private void clear() {
        if (mInput.length() == 0) {
            return;
        }

        mInput.deleteCharAt(mInput.length() - 1);
        evaluate();
        updateText();
    }

    private void evaluate() {
        final int category = mSpinnerCategory.getSelectedItemPosition();
        mOutput = mConverters.get(category).Convert(
                mInput.toString(),
                mConverters.get(category).GetUnitFromInteger(mSpinnerInput.getSelectedItemPosition()),
                mConverters.get(category).GetUnitFromInteger(mSpinnerOutput.getSelectedItemPosition()));
    }

    private void updateText() {
        View root_view = getView();
        if (root_view == null) return;
        if (mInput.length() == 0) {
            mTextDisplayInput.setText("0");
        } else {
            mTextDisplayInput.setText(mInput.toString());
        }
        if (mOutput.length() == 0) {
            mTextDisplayOutput.setText("0");
        } else {
            mTextDisplayOutput.setText(mOutput);
        }

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
