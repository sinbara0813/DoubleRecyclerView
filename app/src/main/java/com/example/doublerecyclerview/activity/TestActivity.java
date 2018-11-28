package com.example.doublerecyclerview.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PagerSnapHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.example.doublerecyclerview.R;
import com.example.doublerecyclerview.view.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者:Created by sinbara on 2018/10/24.
 * 邮箱:hrb940258169@163.com
 */

public class TestActivity extends AppCompatActivity {

    Banner banner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        banner=findViewById(R.id.banner);
        init();
    }

    private void init() {
        List<String> list=new ArrayList<>();
        for (int i=0;i<4;i++){
            list.add("xxx");
        }
        banner.setSource(list).startScroll();
        /*Log.e("han","onTouchEvent "+(event.getAction()== MotionEvent.ACTION_DOWN?"ACTION_DOWN":event.getAction()==MotionEvent.ACTION_MOVE?"ACTION_MOVE":
                event.getAction()==MotionEvent.ACTION_UP?"ACTION_UP":event.getAction()));*/
    }

}
