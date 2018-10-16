package com.example.doublerecyclerview.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.example.doublerecyclerview.view.TabViewPager;

/**
 * 作者:Created by sinbara on 2018/9/19.
 * 邮箱:hrb940258169@163.com
 */

public class MainAdapter extends DelegateAdapter.Adapter {

    private TabViewPager tabViewPager;
    private int maxTop;

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==1){
            TextView textView=new TextView(parent.getContext());
            textView.setBackgroundColor(Color.parseColor("#BB9D6E"));
            return new MainAdapter.ViewHolder(textView);
        }else if (viewType==2){
            TextView textView=new TextView(parent.getContext());
            return new MainAdapter.ViewHolder(textView);
        }else {
            TabViewPager tabViewPager=new TabViewPager(parent.getContext(), (RecyclerView) parent);
            return new ViewPagerHolder(tabViewPager);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==1){

        }else if (getItemViewType(position)==2){
            ((ViewHolder)holder).textView.setText("第"+position+"项");
        }else {
            tabViewPager= (TabViewPager) holder.itemView;
            tabViewPager.setTabData();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return 1;
        }else if (position<4){
            return 2;
        }else {
            return 3;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public int getMaxTop(){
        if (tabViewPager!=null&&maxTop==0){
            maxTop=tabViewPager.getTop();
        }
        return maxTop;
    }

    public int getTop(){
        if (tabViewPager!=null){
            return tabViewPager.getTop();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView= (TextView) itemView;
            textView.setGravity(Gravity.CENTER_VERTICAL);
            RecyclerView.LayoutParams rl=new RecyclerView.LayoutParams(-1,600);
            textView.setLayoutParams(rl);
        }
    }

    public static class ViewPagerHolder extends RecyclerView.ViewHolder{

        public ViewPagerHolder(View itemView) {
            super(itemView);
        }
    }
}
