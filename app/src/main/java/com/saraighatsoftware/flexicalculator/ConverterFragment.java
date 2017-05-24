package com.saraighatsoftware.flexicalculator;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConverterFragment extends Fragment {

    private static final String TAG = "ConverterFragment";

    private static final String ARG_SECTION_NUMBER = "section_number";

    private TextView mTextDisplayInput;
    private TextView mTextDisplayOutput;

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
    private Button mButtonPoint;
    private Button mButtonDelete;

    private Spinner mSpinnerCategory;
    private Spinner mSpinnerInputType;
    private Spinner mSpinnerOutputType;

    private ConverterVolume mConverterVolume;

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
        mConverterVolume =  new ConverterVolume(getContext());

        View root_view = inflater.inflate(R.layout.fragment_converter, container, false);

        mTextDisplayInput = (TextView) root_view.findViewById(R.id.text_display_input);
        mTextDisplayInput.setText("0");
        mTextDisplayOutput = (TextView) root_view.findViewById(R.id.text_display_output);
        mTextDisplayOutput.setText("0");

        Typeface display_font = Typeface.createFromAsset(getContext().getAssets(),  "fonts/Teko-Light.ttf");
        mTextDisplayInput.setTypeface(display_font);
        mTextDisplayOutput.setTypeface(display_font);

        Typeface button_font = Typeface.createFromAsset(getContext().getAssets(),  "fonts/Teko-Regular.ttf");
        Typeface bold_button_font = Typeface.createFromAsset(getContext().getAssets(),  "fonts/Teko-SemiBold.ttf");

        mSpinnerCategory = (Spinner) root_view.findViewById(R.id.spinner_category);
        final CustomArrayAdapter category_adapter = new CustomArrayAdapter(
                getContext(),
                android.R.layout.simple_list_item_1,
                Arrays.asList(getResources().getStringArray(R.array.categories))
        );
        mSpinnerCategory.setAdapter(category_adapter);

        mSpinnerInputType = (Spinner) root_view.findViewById(R.id.spinner_input_type);
        final CustomArrayAdapter input_type_adapter = new CustomArrayAdapter(
                getContext(),
                android.R.layout.simple_list_item_1,
                new ArrayList<String>()
        );
        mSpinnerInputType.setAdapter(input_type_adapter);

        mSpinnerOutputType = (Spinner) root_view.findViewById(R.id.spinner_output_type);
        final CustomArrayAdapter output_type_adapter = new CustomArrayAdapter(
                getContext(),
                android.R.layout.simple_list_item_1,
                new ArrayList<String>()
        );
        mSpinnerOutputType.setAdapter(output_type_adapter);

        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                input_type_adapter.clear();
                for (String item : mConverterVolume.GetUnits()) {
                    input_type_adapter.add(item);
                }
                input_type_adapter.notifyDataSetChanged();

                output_type_adapter.clear();
                for (String item : mConverterVolume.GetUnits()) {
                    output_type_adapter.add(item);
                }
                output_type_adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

        mButtonPoint = (Button) root_view.findViewById(R.id.button_point);
        mButtonPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionListener(Calculator.POINT);
            }
        });
        mButtonPoint.setTypeface(button_font);

        mButtonDelete = (Button) root_view.findViewById(R.id.button_delete);
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });
        mButtonDelete.setTypeface(button_font);

        return root_view;
    }

    private void expressionListener(String token) {
        // reads 0-9
    }

    private void clear() {
    }
}
