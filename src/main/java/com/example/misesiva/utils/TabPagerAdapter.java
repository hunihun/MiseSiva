package com.example.misesiva.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.misesiva.FindGroupTapFragment;
import com.example.misesiva.GroupTapFragment;
import com.example.misesiva.SettingTapFragment;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    //Count number of tabs
    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        //Returning the current tabs;
        switch (position){
            case 0:
                GroupTapFragment mainTabFragment1 = new GroupTapFragment();
                return mainTabFragment1;
            case 1:
                FindGroupTapFragment mainTabFragment2 = new FindGroupTapFragment();
                return mainTabFragment2;
            case 2:
                SettingTapFragment mainTabFragment3 = new SettingTapFragment();
                return mainTabFragment3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}