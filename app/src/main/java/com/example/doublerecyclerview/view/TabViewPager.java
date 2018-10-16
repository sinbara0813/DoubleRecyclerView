package com.example.doublerecyclerview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import com.example.doublerecyclerview.listener.ExpandListener;
import com.example.doublerecyclerview.R;
import com.example.doublerecyclerview.fragment.SubFragment;
import com.example.doublerecyclerview.adapter.TabPagerAdapter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2018/9/19.
 * 邮箱:hrb940258169@163.com
 */

public class TabViewPager extends FrameLayout {

    @Bind(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    private Context mContext;
    private TabPagerAdapter tabAdapter;

    private int mInitialTouchX; //DOWN事件X坐标
    private int mInitialTouchY; //DOWN事件Y坐标
    private int mLastTouchX; //最近一次事件X坐标
    private int mLastTouchY; //最近一次事件Y坐标
    private int mTouchSlop; //最小移动距离
    private int mMinFlingVelocity; //最小fling速度
    private int mMaxFlingVelocity; //最大fling速度
    private RecyclerView outRecyclerView; //外部recyclerview
    private View currentScrollView; //内部recyclerview
    private boolean isEnterFirst =true; //事件由外部处理变成内部处理的临界点
    private VelocityTracker mVelocityTracker; //测量速度工具类
    private ViewFlinger mViewFlinger; //处理fling事件工具类

    public TabViewPager(@NonNull Context context, RecyclerView outRecyclerView) {
        super(context);
        this.outRecyclerView=outRecyclerView;
        mViewFlinger=new ViewFlinger(outRecyclerView);
        init(context);
    }

    public TabViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        this.mContext=context;
        LayoutInflater.from(context).inflate(R.layout.layout_tab_view_pager, this, true);
        ButterKnife.bind(this,this);
        viewPager.getLayoutParams().height=getDisplayHeight()-dip2px(112)-getStatusBarHeight(context); //设置viewpager高度 112viewpager滑动到顶部时到屏幕顶部的高度
        ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
        mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
    }

    public void setTabData() {
        int size = 10;
        final List<Fragment> fragments = new ArrayList<>();
        List<String> cateList=new ArrayList<>();
        for (int i = 0; i < size; i++) {
            fragments.add(new SubFragment());
            switch (i){
                case 0:
                    cateList.add("精选推荐");
                    break;
                case 1:
                    cateList.add("平价手机");
                    break;
                case 2:
                    cateList.add("电子书童");
                    break;
                case 3:
                    cateList.add("军迷吧");
                    break;
                case 4:
                    cateList.add("搞机");
                    break;
                case 5:
                    cateList.add("个性潮装");
                    break;
                case 6:
                    cateList.add("型男");
                    break;
                case 7:
                    cateList.add("户外服饰");
                    break;
                case 8:
                    cateList.add("风雨无阻");
                    break;
                case 9:
                    cateList.add("酷跑一族");
                    break;
            }
        }
        tabAdapter = new TabPagerAdapter(((FragmentActivity)mContext).getSupportFragmentManager(), fragments, cateList);
        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentScrollView=((ExpandListener)fragments.get(position)).getScrollableView();//获取当前内部滑动的recyclerview
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        slidingTab.setViewPager(viewPager);
        slidingTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                RecyclerView.SmoothScroller scroller=new LinearSmoothScroller(mContext){
                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return 100f / displayMetrics.densityDpi; //这里控制滑动的速度
                    }
                };
                scroller.setTargetPosition(4);
                outRecyclerView.getLayoutManager().startSmoothScroll(scroller);//让tab已一定的速度滑动到顶部
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    public boolean isTop() {
        return getTop() == dip2px(56);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mInitialTouchX=(int)(event.getX()+0.5f);
                mInitialTouchY=(int)(event.getY()+0.5f);
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                final int y = (int) (event.getY() + 0.5f);
                final int dy = y - mInitialTouchY;
                if (Math.abs(dy) > mTouchSlop){ //垂直方向拦截事件
                    return true;
                }
            case MotionEvent.ACTION_UP:
                mVelocityTracker.clear();
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        MotionEvent vtev = MotionEvent.obtain(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mInitialTouchX=(int)(event.getX()+0.5f);
                mInitialTouchY=(int)(event.getY()+0.5f);
                vtev.offsetLocation(0,0);
                break;
            case MotionEvent.ACTION_MOVE:
                final int x = (int) (event.getX() + 0.5f);
                final int y = (int) (event.getY() + 0.5f);
                int dx=mInitialTouchX-x;
                int dy=mInitialTouchY-y;
                //判断滑动事件由外部recyclerview处理还是内部recyclerview处理
                if (!isTop()||(isTop()&&(mLastTouchY-y)<0&&isExpand())){
                    if ((isTop()&&(mLastTouchY-y)<0&&isExpand())){
                        mInitialTouchX=(int)(event.getX()+0.5f);
                        mInitialTouchY=(int)(event.getY()+0.5f);
                        dx=0;
                        dy=-1;
                    }
                    outRecyclerView.scrollBy(dx,dy);
                    vtev.offsetLocation(dx,dy);
                }else {
                    if (isEnterFirst){
                        if (event.getAction()!=MotionEvent.ACTION_DOWN){
                            event.setAction(MotionEvent.ACTION_DOWN);
                        }
                        isEnterFirst =false;
                    }
                    getScrollView().onTouchEvent(event);
                }
                mLastTouchX=x;
                mLastTouchY=y;
                break;
            case MotionEvent.ACTION_UP:
                if (isTop()||!isEnterFirst){
                    getScrollView().onTouchEvent(event);
                }else {
                    mVelocityTracker.addMovement(event);
                    mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
                    final float xvel = 0;
                    final float yvel = -mVelocityTracker.getYVelocity(event.getPointerId(0));
                    fling((int) xvel, (int) yvel);//处理fling事件
                }
                isEnterFirst =true;
                if (mVelocityTracker!=null){
                    mVelocityTracker.clear();
                }
                break;
        }
        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return true;
    }

    private void fling(int velocityX, int velocityY) {
        velocityX = Math.max(-mMaxFlingVelocity, Math.min(velocityX, mMaxFlingVelocity));
        velocityY = Math.max(-mMaxFlingVelocity, Math.min(velocityY, mMaxFlingVelocity));
        mViewFlinger.fling(velocityX, velocityY);
    }

    public boolean isExpand(){
        boolean is=false;
        TabPagerAdapter mainPageAdapter= (TabPagerAdapter) viewPager.getAdapter();
        Fragment fragment= null;
        if (mainPageAdapter!=null){
            fragment=mainPageAdapter.getItem(viewPager.getCurrentItem());
        }
        if (fragment!=null&&fragment instanceof ExpandListener){
            is=((ExpandListener)fragment).isExpand();
        }
        return is;
    }

    private View getScrollView(){
        if (currentScrollView==null){
            return ((ExpandListener)tabAdapter.getItem(viewPager.getCurrentItem())).getScrollableView();
        }
        return currentScrollView;
    }

    private int dip2px(int dipValue){
        DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics();
        float scale=dm.density;
        return (int) (dipValue * scale + 0.5f);
    }

    private int getDisplayHeight(){
        DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception E) {
            E.printStackTrace();
        }
        return sbar;
    }

    static final Interpolator sQuinticInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    class ViewFlinger implements Runnable{

        private int mLastFlingX;
        private int mLastFlingY;
        private OverScroller mScroller;
        private RecyclerView recyclerView;

        public ViewFlinger(RecyclerView recyclerView){
            this.recyclerView=recyclerView;
            mScroller=new OverScroller(recyclerView.getContext(),sQuinticInterpolator);
        }

        @Override
        public void run() {
            final OverScroller scroller = mScroller;
            if (scroller.computeScrollOffset()){
                final int x = scroller.getCurrX();
                final int y = scroller.getCurrY();
                int dx = x - mLastFlingX;
                int dy = y - mLastFlingY;
                recyclerView.scrollBy(dx,dy);
                postOnAnimation();
            }
        }

        void postOnAnimation() {
            recyclerView.removeCallbacks(this);
            ViewCompat.postOnAnimation(recyclerView, this);
        }

        public void fling(int velocityX, int velocityY) {
            mLastFlingX = mLastFlingY = 0;
            mScroller.fling(0, 0, velocityX, velocityY,
                    Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            postOnAnimation();
        }
    }
}
