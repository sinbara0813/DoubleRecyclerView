package com.example.doublerecyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
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

import com.example.doublerecyclerview.adapter.CustomTabAdapter;
import com.example.doublerecyclerview.adapter.TabPagerAdapter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/9/11.
 */

public class TabViewPager extends FrameLayout {

    @Bind(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    private Context mContext;
    private TabPagerAdapter tabAdapter;

    private int mInitialTouchX;
    private int mInitialTouchY;
    private int mLastTouchX;
    private int mLastTouchY;
    private int mTouchSlop;
    private int mMinFlingVelocity;
    private int mMaxFlingVelocity;
    private RecyclerView outRecyclerView;
    private View currentScrollView;
    private boolean isInterFirst=true;
    private boolean isOutFirst=true;
    private VelocityTracker mVelocityTracker;
    private ViewFlinger mViewFlinger;

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
        viewPager.getLayoutParams().height=getDisplayHeight()-dip2px(112)-getStatusBarHeight(context);
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
                currentScrollView=((ExpandListener)fragments.get(position)).getScrollableView();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        slidingTab.setViewPager(viewPager);
        slidingTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                outRecyclerView.scrollToPosition(4);
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
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
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
                final int x = (int) (event.getX() + 0.5f);
                final int y = (int) (event.getY() + 0.5f);
                int dx=mInitialTouchX-x;
                int dy=mInitialTouchY-y;
                if (!isTop()||(isTop()&&(mLastTouchY-y)<0&&isExpand())){
                    if ((isTop()&&(mLastTouchY-y)<0&&isExpand())){
                        mInitialTouchX=(int)(event.getX()+0.5f);
                        mInitialTouchY=(int)(event.getY()+0.5f);
                        dx=0;
                        dy=-1;
                    }
                    outRecyclerView.scrollBy(dx,dy);
                }else {
                    if (isInterFirst){
                        if (event.getAction()!=MotionEvent.ACTION_DOWN){
                            event.setAction(MotionEvent.ACTION_DOWN);
                        }
                        isInterFirst=false;
                    }
                    getScrollView().onTouchEvent(event);
                }
                mLastTouchX=x;
                mLastTouchY=y;
                break;
            case MotionEvent.ACTION_UP:
                if (isTop()||!isInterFirst){
                    getScrollView().onTouchEvent(event);
                }else {
                    mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
                    final float xvel = 0;
                    final float yvel = mVelocityTracker.getYVelocity();
                    fling((int) xvel, (int) yvel);
                }
                isInterFirst=true;
                isOutFirst=true;
                if (mVelocityTracker!=null){
                    mVelocityTracker.clear();
                }
                break;
        }
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
