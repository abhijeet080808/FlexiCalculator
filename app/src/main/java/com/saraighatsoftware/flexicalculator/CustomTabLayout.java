package com.saraighatsoftware.flexicalculator;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomTabLayout extends TabLayout {

    private final Typeface mTypeface;

    public CustomTabLayout(Context context) {
        super(context);
        mTypeface = Typeface.createFromAsset(getContext().getAssets(),  "fonts/Teko-Regular.ttf");
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTypeface = Typeface.createFromAsset(getContext().getAssets(),  "fonts/Teko-Regular.ttf");
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTypeface = Typeface.createFromAsset(getContext().getAssets(),  "fonts/Teko-Regular.ttf");
    }

    @Override
    public void setupWithViewPager(ViewPager viewPager) {
        super.setupWithViewPager(viewPager);

        PagerAdapter adapter = viewPager.getAdapter();
        if (mTypeface != null && adapter != null) {
            this.removeAllTabs();
            ViewGroup sliding_tab_strip = (ViewGroup) getChildAt(0);

            for (int i = 0, count = adapter.getCount(); i < count; i++) {
                Tab tab = this.newTab();
                this.addTab(tab.setText(adapter.getPageTitle(i)));

                ViewGroup tab_view = ((ViewGroup) sliding_tab_strip.getChildAt(i));

                if (tab_view.getChildAt(1) instanceof TextView) {
                    ((TextView) tab_view.getChildAt(1)).setTypeface(mTypeface);
                    // does not work, so setting via styles.xml
                    // ((TextView) tab_view.getChildAt(1)).setTextSize(15);
                }
            }
        }
    }
}
