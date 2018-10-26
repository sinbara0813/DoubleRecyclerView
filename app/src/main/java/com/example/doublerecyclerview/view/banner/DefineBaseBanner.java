package com.example.doublerecyclerview.view.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.doublerecyclerview.R;
import com.example.doublerecyclerview.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Name: D2CMALL
 * Anthor: hrb
 * Date: 2017/9/2 16:24
 * Copyright (c) 2016 d2cmall. All rights reserved.
 */
public abstract class DefineBaseBanner<E, VH extends RecyclerView.ViewHolder,T extends DefineBaseBanner<E,VH, T>> extends RelativeLayout {
    /** 日志 */
    private static final String TAG = DefineBaseBanner.class.getSimpleName();
    /** 单线程池定时任务 */
    private ScheduledExecutorService mStse;
    /** 上下文 */
    protected Context mContext;
    /** 设备密度 */
    protected DisplayMetrics mDisplayMetrics;
    /** RecyclerView */
    protected RecyclerView recyclerView;
    /** 是否是无限循环 */
    protected boolean isLoopEnable;
    /** 数据源 */
    protected List<E> mDatas = new ArrayList<>();
    /** 当前position */
    protected int mCurrentPositon;
    /** 上一个position */
    protected int mLastPositon;
    /** 多久后开始滚动 */
    private long mDelay;
    /** 滚动间隔 */
    private long mPeriod;
    /** 是否自动滚动 */
    private boolean mIsAutoScrollEnable;
    /** 是否正在自动滚动中 */
    private boolean mIsAutoScrolling;
    /** 滚动速度 */
    private int mScrollSpeed = 450;
    /** 切换动画 */
    private Class<? extends ViewPager.PageTransformer> mTransformerClass;

    /** 显示器(小点)的最顶层父容器 */
    private RelativeLayout mRlBottomBarParent;
    private int mItemWidth;
    private int mItemHeight;

    /** 显示器和标题的直接父容器 */
    private LinearLayout mLlBottomBar;
    /** 最后一条item是否显示背景条 */
    private boolean mIsBarShowWhenLast;

    /** 显示器的的直接容器 */
    private LinearLayout mLlIndicatorContainer;

    /** 标题 */
    private TextView mTvTitle;

    public int indicatorType;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            scrollToNextItem(mCurrentPositon);
        }
    };

    public DefineBaseBanner(Context context) {
        this(context, null, 0);
    }

    public DefineBaseBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefineBaseBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        mDisplayMetrics = context.getResources().getDisplayMetrics();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseBanner);
        float scale = ta.getFloat(R.styleable.BaseBanner_bb_scale, -1);

        isLoopEnable = ta.getBoolean(R.styleable.BaseBanner_bb_isLoopEnable, true);
        mDelay = ta.getInt(R.styleable.BaseBanner_bb_delay, 5);
        mPeriod = ta.getInt(R.styleable.BaseBanner_bb_period, 5);
        mIsAutoScrollEnable = ta.getBoolean(R.styleable.BaseBanner_bb_isAutoScrollEnable, true);

        int barColor = ta.getColor(R.styleable.BaseBanner_bb_barColor, Color.TRANSPARENT);
        mIsBarShowWhenLast = ta.getBoolean(R.styleable.BaseBanner_bb_isBarShowWhenLast, true);
        int indicatorGravity = ta.getInt(R.styleable.BaseBanner_bb_indicatorGravity, Gravity.CENTER);
        float barPaddingLeft = ta.getDimension(R.styleable.BaseBanner_bb_barPaddingLeft, dp2px(10));
        float barPaddingTop = ta.getDimension(R.styleable.BaseBanner_bb_barPaddingTop, dp2px(indicatorGravity == Gravity.CENTER ? 6 : 2));
        float barPaddingRight = ta.getDimension(R.styleable.BaseBanner_bb_barPaddingRight, dp2px(10));
        float barPaddingBottom = ta.getDimension(R.styleable.BaseBanner_bb_barPaddingBottom, dp2px(indicatorGravity == Gravity.CENTER ? 6 : 2));
        int textColor = ta.getColor(R.styleable.BaseBanner_bb_textColor, Color.parseColor("#ffffff"));
        float textSize = ta.getDimension(R.styleable.BaseBanner_bb_textSize, sp2px(12.5f));
        boolean isTitleShow = ta.getBoolean(R.styleable.BaseBanner_bb_isTitleShow, true);
        boolean isIndicatorShow = ta.getBoolean(R.styleable.BaseBanner_bb_isIndicatorShow, true);
        ta.recycle();

        //get layout_height
        String height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");

        //create recyclerView
        recyclerView=new RecyclerView(mContext);
        mItemWidth = mDisplayMetrics.widthPixels;
        if (scale < 0) {//scale not set in xml
            if (height.equals(ViewGroup.LayoutParams.MATCH_PARENT + "")) {
                mItemHeight = LayoutParams.MATCH_PARENT;
            } else if (height.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
                mItemHeight = LayoutParams.WRAP_CONTENT;
            } else {
                int[] systemAttrs = {android.R.attr.layout_height};
                TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
                int h = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                a.recycle();
                mItemHeight = h;
            }
        } else {
            if (scale > 1) {
                scale = 1;
            }
            mItemHeight = (int) (mItemWidth * scale);
        }

        LayoutParams lp = new LayoutParams(mItemWidth, mItemHeight);
        addView(recyclerView, lp);

        //top parent of indicators
        mRlBottomBarParent = new RelativeLayout(context);
        addView(mRlBottomBarParent, lp);

        //container of indicators and title
        mLlBottomBar = new LinearLayout(context);
        LayoutParams lp2 = new LayoutParams(mItemWidth, LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        mRlBottomBarParent.addView(mLlBottomBar, lp2);

        mLlBottomBar.setBackgroundColor(barColor);
        mLlBottomBar.setPadding((int) barPaddingLeft, (int) barPaddingTop, (int) barPaddingRight, ScreenUtil.dip2px(barPaddingBottom));
        mLlBottomBar.setClipChildren(false);
        mLlBottomBar.setClipToPadding(false);

        //container of indicators
        mLlIndicatorContainer = new LinearLayout(context);
        mLlIndicatorContainer.setGravity(Gravity.CENTER);
        mLlIndicatorContainer.setVisibility(isIndicatorShow ? VISIBLE : INVISIBLE);
        mLlIndicatorContainer.setClipChildren(false);
        mLlIndicatorContainer.setClipToPadding(false);

        // title
        mTvTitle = new TextView(context);
        mTvTitle.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0F));
        mTvTitle.setSingleLine(true);
        mTvTitle.setTextColor(textColor);
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mTvTitle.setVisibility(isTitleShow ? VISIBLE : INVISIBLE);

        if (indicatorGravity == Gravity.CENTER) {
            mLlBottomBar.setGravity(Gravity.CENTER);
            mLlBottomBar.addView(mLlIndicatorContainer);
        } else {
            if (indicatorGravity == Gravity.RIGHT) {
                mLlBottomBar.setGravity(Gravity.CENTER_VERTICAL);
                mLlBottomBar.addView(mTvTitle);
                mLlBottomBar.addView(mLlIndicatorContainer);

                mTvTitle.setPadding(0, 0, dp2px(7), 0);
                mTvTitle.setEllipsize(TextUtils.TruncateAt.END);
                mTvTitle.setGravity(Gravity.LEFT);
            } else if (indicatorGravity == Gravity.LEFT) {
                mLlBottomBar.setGravity(Gravity.CENTER_VERTICAL);
                mLlBottomBar.addView(mLlIndicatorContainer);
                mLlBottomBar.addView(mTvTitle);

                mTvTitle.setPadding(dp2px(7), 0, 0, 0);
                mTvTitle.setEllipsize(TextUtils.TruncateAt.END);
                mTvTitle.setGravity(Gravity.RIGHT);
            }
        }
    }

    /** 创建ViewPager的Item布局 */
    public abstract VH onCreateItemViewHolder(ViewGroup parent);

    /** 绑定item数据 */
    public abstract void onBindItemView(VH holder,int position);

    /** 创建显示器 */
    public abstract View onCreateIndicator();

    /** 设置当前显示器的状态,选中或者未选中 */
    public abstract void setCurrentIndicator(int position);

    /** 覆写这个方法设置标题 */
    public void onTitleSlect(TextView tv, int position) {
    }

    /** 设置数据源 */
    public T setSource(List<E> list) {
        this.mDatas = list;
        return (T) this;
    }

    public T setBottomPadding(int bottomPadding){
        mLlBottomBar.setPadding(mLlBottomBar.getPaddingLeft(), mLlBottomBar.getPaddingTop(),mLlBottomBar.getPaddingRight(), ScreenUtil.dip2px(bottomPadding));
        return (T) this;
    }

    public T setIndicatorType(int type){
        this.indicatorType=type;
        return (T) this;
    }

    /** 滚动延时,默认5秒 */
    public T setDelay(long delay) {
        this.mDelay = delay;
        return (T) this;
    }

    /** 滚动间隔,默认5秒 */
    public T setPeriod(long period) {
        this.mPeriod = period;
        return (T) this;
    }

    /** 设置是否支持自动滚动,默认true.仅对LoopViewPager有效 */
    public T setAutoScrollEnable(boolean isAutoScrollEnable) {
        this.mIsAutoScrollEnable = isAutoScrollEnable;
        return (T) this;
    }

    /** 设置页面切换动画 */
    public T setTransformerClass(Class<? extends ViewPager.PageTransformer> transformerClass) {
        this.mTransformerClass = transformerClass;
        return (T) this;
    }

    /** 设置底部背景条颜色,默认透明 */
    public T setBarColor(int barColor) {
        mLlBottomBar.setBackgroundColor(barColor);
        return (T) this;
    }

    /** 设置最后一条item是否显示背景条,默认true */
    public T setBarShowWhenLast(boolean isBarShowWhenLast) {
        this.mIsBarShowWhenLast = isBarShowWhenLast;
        return (T) this;
    }

    /** 设置底部背景条padding,单位dp */
    public T barPadding(float left, float top, float right, float bottom) {
        mLlBottomBar.setPadding(dp2px(left), dp2px(top), dp2px(right), dp2px(bottom));
        return (T) this;
    }

    /** 设置标题颜色,默认"#ffffff" */
    public T setTextColor(int textColor) {
        mTvTitle.setTextColor(textColor);
        return (T) this;
    }

    /** set title text size,unit sp,default 14sp */
    public T setTextSize(float textSize) {
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        return (T) this;
    }

    /** 设置是否显示标题,默认true */
    public T setTitleShow(boolean isTitleShow) {
        mTvTitle.setVisibility(isTitleShow ? VISIBLE : INVISIBLE);
        return (T) this;
    }

    /** 设置是否显示显示器,默认true */
    public T setIndicatorShow(boolean isIndicatorShow) {
        mLlIndicatorContainer.setVisibility(isIndicatorShow ? VISIBLE : INVISIBLE);
        return (T) this;
    }

    /** 滚动到下一个item */
    private void scrollToNextItem(int position) {
        position++;
        //mViewPager.setCurrentItem(position);
    }

    private void setRecyclerView() {
        BannerAdapter bannerAdapter=new BannerAdapter();
        final LinearLayoutManager layoutManager=new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bannerAdapter);

        BannerSnapHelper snapHelper=new BannerSnapHelper() {
            @Override
            public void snapView(int adaptPosition) {
                if (adaptPosition<=0){
                    recyclerView.scrollToPosition(mDatas.size());
                }
                if (adaptPosition>=(mDatas.size()+1)){
                    recyclerView.scrollToPosition(1);
                }
                mInternalPageListener.onPageSelected(adaptPosition%mDatas.size());
            }
        };
        snapHelper.attachToRecyclerView(recyclerView);
    }

    private ViewPager.OnPageChangeListener mInternalPageListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            mCurrentPositon = position % mDatas.size();

            setCurrentIndicator(mCurrentPositon);
            onTitleSlect(mTvTitle, mCurrentPositon);
            mLlBottomBar.setVisibility(mCurrentPositon == mDatas.size() - 1 && !mIsBarShowWhenLast ? GONE : VISIBLE);

            mLastPositon = mCurrentPositon;
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    };

    /** 开始滚动 */
    public void startScroll() {
        if (mDatas == null) {
            throw new IllegalStateException("Data source is empty,you must setSource() before startScroll()");
        }

        if (mDatas.size() > 0 && mCurrentPositon > mDatas.size() - 1) {
            mCurrentPositon = 0;
        }

        onTitleSlect(mTvTitle, mCurrentPositon);
        setRecyclerView();
        //create indicator
        View indicatorViews = onCreateIndicator();
        if (indicatorViews != null) {
            mLlIndicatorContainer.removeAllViews();
            mLlIndicatorContainer.addView(indicatorViews);
        }

        goOnScroll();
    }

    /** 继续滚动(for LoopViewPager) */
    public void goOnScroll() {
        if (!isValid()) {
            return;
        }

        if (mIsAutoScrolling) {
            return;
        }
        if (isLoopViewPager() && mIsAutoScrollEnable) {
            pauseScroll();
            mStse = Executors.newSingleThreadScheduledExecutor();
            mStse.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    mHandler.obtainMessage().sendToTarget();
                }
            }, mDelay, mPeriod, TimeUnit.SECONDS);
            mIsAutoScrolling = true;
            Log.d(TAG, this.getClass().getSimpleName() + "--->goOnScroll()");
        } else {
            mIsAutoScrolling = false;
        }
    }

    /** 停止滚动(for LoopViewPager) */
    public void pauseScroll() {
        if (mStse != null) {
            mStse.shutdown();
            mStse = null;
        }
        Log.d(TAG, this.getClass().getSimpleName() + "--->pauseScroll()");

        mIsAutoScrolling = false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                pauseScroll();
                break;
            case MotionEvent.ACTION_UP:
                goOnScroll();
                break;
            case MotionEvent.ACTION_CANCEL:
                goOnScroll();
                break;

        }
        return super.dispatchTouchEvent(ev);
    }

//    @Override
//    protected void onWindowVisibilityChanged(int visibility) {
//        super.onWindowVisibilityChanged(visibility);
//        if (mIsSmart) {
//            if (visibility != VISIBLE) {
//                pauseScroll();
//            } else {
//                goOnScroll();
//            }
//        }
//    }

    private class BannerAdapter extends RecyclerView.Adapter<VH>{

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return onCreateItemViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            onBindItemView(holder,position);
        }

        @Override
        public int getItemCount() {
            return isLoopEnable?mDatas.size()+2:mDatas.size();
        }
    }

    protected int dp2px(float dp) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5F);
    }

    private float sp2px(float sp) {
        final float scale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    protected boolean isLoopViewPager() {
        return false;
    }

    protected boolean isValid() {

        if (mDatas == null || mDatas.size() == 0) {
            Log.e(TAG, "DataList must be not empty!");
            return false;
        }

        return true;
    }

    //listener
    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mOnPageChangeListener = listener;
    }

    private OnItemClickL mOnItemClickL;

    public void setOnItemClickL(OnItemClickL onItemClickL) {
        this.mOnItemClickL = onItemClickL;
    }

    public interface OnItemClickL {
        void onItemClick(int position);
    }
}
