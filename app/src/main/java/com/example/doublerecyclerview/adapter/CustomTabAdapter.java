package com.example.doublerecyclerview.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.doublerecyclerview.R;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.List;

/**
 * Name: D2CMALL
 * Anthor: hrb
 * Date: 2017/12/27 11:28
 * Copyright (c) 2016 d2cmall. All rights reserved.
 */
public class CustomTabAdapter extends TabPagerAdapter implements SlidingTabLayout.CustomTabProvider {

    private List<String> cateList;
    private Context context;

    public CustomTabAdapter(Context context,FragmentManager fm, List<Fragment> fragments, List<String> cateList) {
        super(fm,fragments,null);
        this.cateList=cateList;
        this.context=context;
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
    public View getCustomTabView(ViewGroup parent, int position) {
        View tabView=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tab,new LinearLayout(parent.getContext()),false);
        TextView name= tabView.findViewById(R.id.text);
        if (position==0){
            name.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
        name.setText(cateList.get(position));
        return tabView;
    }

    @Override
    public void tabSelect(View tab) {
        TextView name= tab.findViewById(R.id.text);
        name.setTextColor(context.getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void tabUnselect(View tab) {
        TextView name= tab.findViewById(R.id.text);
        name.setTextColor(context.getResources().getColor(R.color.colorPrimary));
    }
}
