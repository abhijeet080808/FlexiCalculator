package com.saraighatsoftware.flexicalculator;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SimpleCalcFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.saraighatsoftware.flexicalculator", appContext.getPackageName());
    }

    @Test
    public void simpleCalculator() {
        // test that Simple tab is selected
        onView(allOf(withText("Simple"), isDescendantOfA(withId(R.id.tabs)), isSelected()))
                .check(matches(isDisplayed()));

        // check that calculator displays 0 by default on current tab
        // isDisplayed will select the text view of the tab currently displayed on screen
        onView(allOf(withId(R.id.text_display), isDisplayed())).check(matches(withText("0")));

        // test 1 DEL
        click(R.id.button_one, "1");
        click(R.id.button_del, "0");

        // test 1 + 1 =
        click(R.id.button_one, "1");
        click(R.id.button_add, "1+");
        click(R.id.button_one, "1+1");
        click(R.id.button_equal, "2");

        // test 1 + 1 = + 1 =
        click(R.id.button_one, "1");
        click(R.id.button_add, "1+");
        click(R.id.button_one, "1+1");
        click(R.id.button_equal, "2");
        click(R.id.button_add, "2+");
        click(R.id.button_one, "2+1");
        click(R.id.button_equal, "3");

        // test 1 + 1 + 1 =
        click(R.id.button_one, "1");
        click(R.id.button_add, "1+");
        click(R.id.button_one, "1+1");
        click(R.id.button_add, "1+1+");
        click(R.id.button_one, "1+1+1");
        click(R.id.button_equal, "3");

        // test 1 + ( 1 + 1 ) =
        click(R.id.button_one, "1");
        click(R.id.button_add, "1+");
        click(R.id.button_open_bracket, "1+(");
        click(R.id.button_one, "1+(1");
        click(R.id.button_add, "1+(1+");
        click(R.id.button_one, "1+(1+1");
        click(R.id.button_close_bracket, "1+(1+1)");
        click(R.id.button_equal, "3");

        // test 2 - 1 =
        click(R.id.button_two, "2");
        click(R.id.button_subtract, "2-");
        click(R.id.button_one, "2-1");
        click(R.id.button_equal, "1");

        // test 2 - + 1 =
        click(R.id.button_two, "2");
        click(R.id.button_subtract, "2-");
        click(R.id.button_add, "2+");
        click(R.id.button_one, "2+1");
        click(R.id.button_equal, "3");

        //test -1 + 2 =
        click(R.id.button_del, "0");
        click(R.id.button_subtract, "-");
        click(R.id.button_one, "-1");
        click(R.id.button_add, "-1+");
        click(R.id.button_two, "-1+2");
        click(R.id.button_equal, "1");

        // test 1 - 2 = + 2 =
        click(R.id.button_one, "1");
        click(R.id.button_subtract, "1-");
        click(R.id.button_two, "1-2");
        click(R.id.button_equal, "-1");
        click(R.id.button_add, "-1+");
        click(R.id.button_two, "-1+2");
        click(R.id.button_equal, "1");

        // test 1 + ( - 3 ) =
        click(R.id.button_one, "1");
        click(R.id.button_add, "1+");
        click(R.id.button_open_bracket, "1+(");
        click(R.id.button_subtract, "1+(-");
        click(R.id.button_three, "1+(-3");
        click(R.id.button_close_bracket, "1+(-3)");
        click(R.id.button_equal, "-2");

        // test 1 + ( 2 + ( 2 * 3 ) - ( -3 + 2 ) ) + 0 =
        click(R.id.button_one, "1");
        click(R.id.button_add, "1+");
        click(R.id.button_open_bracket, "1+(");
        click(R.id.button_two, "1+(2");
        click(R.id.button_add, "1+(2+");
        click(R.id.button_open_bracket, "1+(2+(");
        click(R.id.button_two, "1+(2+(2");
        click(R.id.button_multiply, "1+(2+(2*");
        click(R.id.button_three, "1+(2+(2*3");
        click(R.id.button_close_bracket, "1+(2+(2*3)");
        click(R.id.button_subtract, "1+(2+(2*3)-");
        click(R.id.button_open_bracket, "1+(2+(2*3)-(");
        click(R.id.button_subtract, "1+(2+(2*3)-(-");
        click(R.id.button_three, "1+(2+(2*3)-(-3");
        click(R.id.button_add, "1+(2+(2*3)-(-3+");
        click(R.id.button_two, "1+(2+(2*3)-(-3+2");
        click(R.id.button_close_bracket, "1+(2+(2*3)-(-3+2)");
        click(R.id.button_close_bracket, "1+(2+(2*3)-(-3+2))");
        click(R.id.button_add, "1+(2+(2*3)-(-3+2))+");
        click(R.id.button_zero, "1+(2+(2*3)-(-3+2))+0");
        click(R.id.button_equal, "10");

        // - + 1 + 3 =
        click(R.id.button_del, "0");
        click(R.id.button_subtract, "-");
        click(R.id.button_add, "-");
        click(R.id.button_one, "-1");
        click(R.id.button_add, "-1+");
        click(R.id.button_three, "-1+3");
        click(R.id.button_equal, "2");

        // 1 + ( - + * ( ) 3 ) =
        click(R.id.button_one, "1");
        click(R.id.button_add, "1+");
        click(R.id.button_open_bracket, "1+(");
        click(R.id.button_subtract, "1+(-");
        click(R.id.button_add, "1+(-");
        click(R.id.button_multiply, "1+(-");
        click(R.id.button_open_bracket, "1+(-");
        click(R.id.button_close_bracket, "1+(-");
        click(R.id.button_three, "1+(-3");
        click(R.id.button_close_bracket, "1+(-3)");
        click(R.id.button_equal, "-2");

        // 10 * + - / ( + - / . . - + 5 ) =
        click(R.id.button_one, "1");
        click(R.id.button_zero, "10");
        click(R.id.button_multiply, "10*");
        click(R.id.button_add, "10+");
        click(R.id.button_subtract, "10-");
        click(R.id.button_divide, "10/");
        click(R.id.button_open_bracket, "10/(");
        click(R.id.button_add, "10/(");
        click(R.id.button_subtract, "10/(-");
        click(R.id.button_divide, "10/(-");
        click(R.id.button_dot, "10/(-.");
        click(R.id.button_dot, "10/(-.");
        click(R.id.button_subtract, "10/(-.");
        click(R.id.button_add, "10/(-.");
        click(R.id.button_five, "10/(-.5");
        click(R.id.button_close_bracket, "10/(-.5)");
        click(R.id.button_equal, "-20");

        // 10 + ( ( ( + / 1 ) 1 ) ) =
        click(R.id.button_one, "1");
        click(R.id.button_zero, "10");
        click(R.id.button_add, "10+");
        click(R.id.button_open_bracket, "10+(");
        click(R.id.button_open_bracket, "10+((");
        click(R.id.button_open_bracket, "10+(((");
        click(R.id.button_add, "10+(((");
        click(R.id.button_divide, "10+(((");
        click(R.id.button_one, "10+(((1");
        click(R.id.button_close_bracket, "10+(((1)");
        click(R.id.button_one, "10+(((1)");
        click(R.id.button_close_bracket, "10+(((1))");
        click(R.id.button_close_bracket, "10+(((1)))");
        click(R.id.button_equal, "11");

    }

    private void click(int id, String expected) {
        onView(allOf(withId(id), isDisplayed())).perform(ViewActions.click());

        expected = expected.
                replace('/', Calculator.DIVIDE_CHAR).
                replace('*', Calculator.MULTIPLY_CHAR).
                replace('-', Calculator.SUBTRACT_CHAR);

        onView(allOf(withId(R.id.text_display), isDisplayed())).check(matches(withText(expected)));
    }
}
