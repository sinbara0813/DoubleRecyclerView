package com.example.doublerecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.example.doublerecyclerview.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2018/9/19.
 * 邮箱:hrb940258169@163.com
 */

public class SampleAdapter extends DelegateAdapter.Adapter<SampleAdapter.ViewHolder> {

    private int[] resources =new int[]{
            R.mipmap.one,R.mipmap.two,R.mipmap.third,R.mipmap.four
    };
    private int itemWidth;
    private Context context;
    private LayoutHelper layoutHelper;

    public SampleAdapter(Context context,LayoutHelper layoutHelper,int itemWidth){
        this.context=context;
        this.layoutHelper=layoutHelper;
        this.itemWidth=itemWidth;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_item, parent, false);
        return new ViewHolder(view,itemWidth);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productImage.setImageResource(resources[position%4]);
        holder.productName.setText("我是第"+position+"个商品品品品品品品品品品");
        holder.productPrice.setText("¥"+(position+1)*500);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.product_image)
        ImageView productImage;
        @Bind(R.id.product_name)
        TextView productName;
        @Bind(R.id.product_price)
        TextView productPrice;
        @Bind(R.id.root_rl)
        RelativeLayout rootRl;

        public ViewHolder(View itemView,int itemWith) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            RelativeLayout.LayoutParams ll = (RelativeLayout.LayoutParams) productImage.getLayoutParams();
            ll.width = itemWith- 3;
            ll.height = ll.width * 1558 / 1000;
        }
    }
}
