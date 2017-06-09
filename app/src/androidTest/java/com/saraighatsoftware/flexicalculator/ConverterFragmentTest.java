package com.saraighatsoftware.flexicalculator;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class ConverterFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void Converter() {
        // test that Calculate tab is selected
        onView(allOf(withText("Calculate"), isDescendantOfA(withId(R.id.tabs)), isSelected()))
                .check(matches(isDisplayed()));
        // select Convert tab
        onView(allOf(withText("Convert"), isDescendantOfA(withId(R.id.tabs))))
                .perform(ViewActions.click());
        // test that Convert tab is selected
        onView(allOf(withText("Convert"), isDescendantOfA(withId(R.id.tabs)), isSelected()))
                .check(matches(isDisplayed()));

        // check that converter displays nothing by default on both displays
        // isDisplayed will select the text view of the tab currently displayed on screen
        onView(allOf(withId(R.id.text_display_input), isDisplayed())).check(matches(withText("")));
        onView(allOf(withId(R.id.text_display_output), isDisplayed())).check(matches(withText("")));

        // check default category and input/output units
        onView(allOf(withId(R.id.spinner_category), isDisplayed())).check(matches(withSpinnerText("Volume")));
        onView(allOf(withId(R.id.spinner_input_type), isDisplayed())).check(matches(withSpinnerText("Milliliters")));
        onView(allOf(withId(R.id.spinner_output_type), isDisplayed())).check(matches(withSpinnerText("Liters")));

        // test a simple conversion
        click(R.id.button_one, "1", "0.001");
        click(R.id.button_zero, "10", "0.01");
        click(R.id.button_zero, "100", "0.1");
        click(R.id.button_zero, "1000", "1");

        // change output unit and check
        onView(withId(R.id.spinner_output_type)).perform(ViewActions.click());
        onData(allOf(is(instanceOf(String.class)), is("Fluid Ounces (US)"))).perform(ViewActions.click());
        onView(allOf(withId(R.id.text_display_output), isDisplayed())).check(matches(withText("33.814022702")));

        // delete one digit and check
        click(R.id.button_delete, "100", "3.38140227");

    }

    private void click(int id, String expectedInput, String expectedOutput) {
        onView(allOf(withId(id), isDisplayed())).perform(ViewActions.click());
        onView(allOf(withId(R.id.text_display_input), isDisplayed())).check(matches(withText(expectedInput)));
        onView(allOf(withId(R.id.text_display_output), isDisplayed())).check(matches(withText(expectedOutput)));
    }
}
