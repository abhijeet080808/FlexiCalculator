package com.saraighatsoftware.flexicalculator;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class FragmentPager extends FragmentStatePagerAdapter {

    private final Context mContext;

    FragmentPager(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new CalculatorFragment();
        }
        if (position == 1) {
            return new ConverterFragment();
        }
        if (position == 2) {
            return new VoiceFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.fragment_calculator);
            case 1:
                return mContext.getString(R.string.fragment_converter);
            case 2:
                return mContext.getString(R.string.fragment_voice);
        }
        return null;
    }
}
