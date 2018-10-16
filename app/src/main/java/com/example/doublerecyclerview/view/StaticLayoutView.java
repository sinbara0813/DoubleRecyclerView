package com.example.doublerecyclerview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.example.doublerecyclerview.R;

/**
 * 作者:Created by sinbara on 2018/10/13.
 * 邮箱:hrb940258169@163.com
 */

public class StaticLayoutView extends View{

    private final int DEFAULT_SIZE=14;

    private Context context;
    private Layout layout = null;

    private int width;
    private int height;
    private CharSequence mText;

    private int color; //文字颜色
    private int pxSize; //文字大小单位像素
    private boolean isBold; //是否设置粗体
    private boolean includepad=true; //是否上下留白

    private TextPaint textPaint;
    private Layout.Alignment alignment;

    private int hardCodeWidth; //文字宽度

    public StaticLayoutView(Context context) {
        this(context,null);
    }

    public StaticLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StaticLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.StaticLayoutView,defStyleAttr,0);
        color=a.getColor(R.styleable.StaticLayoutView_color, Color.BLACK);
        pxSize=a.getDimensionPixelSize(R.styleable.StaticLayoutView_size,-1);
        a.recycle();
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        if (this.layout.getWidth() != width || this.layout.getHeight() != height) {
            width = this.layout.getWidth();
            height = this.layout.getHeight();
            requestLayout();
        }
    }

    public void setText(CharSequence text){
        if (mText!=null) try {
            throw new Exception("mText is not null,StaticLayoutView only can used static text");
        } catch (Exception e) {
            e.printStackTrace();
        }
        mText=text;
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(color);
        textPaint.density = context.getResources().getDisplayMetrics().density;
        if (pxSize<0){
            textPaint.setTextSize(textPaint.density*DEFAULT_SIZE);
        }else {
            textPaint.setTextSize(pxSize);
        }
        textPaint.setFakeBoldText(isBold);

        alignment = Layout.Alignment.ALIGN_NORMAL;

        hardCodeWidth = (int)textPaint.measureText(text.toString());

        layout=new StaticLayout(text,textPaint,hardCodeWidth,alignment,1.0f,0f,includepad);
		setLayout(layout);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        if (layout != null) {
            layout.draw(canvas, null, null, 0);
        }
        canvas.restore();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (layout != null) {
            setMeasuredDimension(layout.getWidth(), layout.getHeight());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getPxSize() {
        return pxSize;
    }

    public void setPxSize(int pxSize) {
        this.pxSize = pxSize;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }
}
