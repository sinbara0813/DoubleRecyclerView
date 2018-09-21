package com.example.doublerecyclerview.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Name: d2c
 * Anthor: hrb
 * Date: 2017/7/31 16:45
 * Copyright (c) 2016 d2cmall. All rights reserved.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    public List<Fragment> fragments;
    public List<String> titles;

    public TabPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.fragments=fragments;
        this.titles=titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles!=null&&titles.size()>position){
            return titles.get(position);
        }else {
            return "";
        }
    }
}
