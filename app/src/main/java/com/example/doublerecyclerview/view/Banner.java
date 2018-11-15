package com.example.doublerecyclerview.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doublerecyclerview.R;
import com.example.doublerecyclerview.view.banner.DefineBaseIndicatorBanner;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Name: d2c
 * Anthor: hrb
 * Date: 2017/7/27 16:38
 * Copyright (c) 2016 d2cmall. All rights reserved.
 */
public class Banner extends DefineBaseIndicatorBanner<String,Banner.ViewHolder, Banner> {

    private int[] resources =new int[]{
            R.mipmap.one,R.mipmap.two,R.mipmap.third,R.mipmap.four
    };

    private int loadingId;
    private int bottomPadding;
    private boolean crop;

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public Banner.ViewHolder onCreateItemViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_banner_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindItemView(Banner.ViewHolder holder, int position) {
        holder.text.setBackgroundColor(position%2==0? Color.parseColor("#ff0000"):Color.parseColor("#0000ff"));
        super.onBindItemView(holder,position);
    }

/*    @Override
    public View onCreateItemView(final int position) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_banner_item,null);
        if (position<mDatas.size()){
            ImageView imageView=(ImageView) view;
            if (crop){
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            UniversalImageLoader.displayImage(mContext,mDatas.get(position),imageView,loadingId,loadingId);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener!=null){
                        itemClickListener.itemClick(v,position);
                    }
                }
            });
        }
        return view;
    }*/

    public Banner setLoadingPic(int id) {
        this.loadingId = id;
        return this;
    }

    public Banner setScaleType(boolean is) {
        this.crop = is;
        return this;
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.text)
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
