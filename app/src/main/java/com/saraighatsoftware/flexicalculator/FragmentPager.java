package com.saraighatsoftware.flexicalculator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class FragmentPager extends FragmentPagerAdapter {

    FragmentPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        SimpleCalcFragment fragment = new SimpleCalcFragment();
        fragment.setArguments(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Simple";
            case 1:
                return "Expression";
            case 2:
                return "Help";
        }
        return null;
    }
}
