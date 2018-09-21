package com.example.doublerecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.example.doublerecyclerview.adapter.MainAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.alpha_view)
    View alphaView;
    @Bind(R.id.portrait_iv)
    ImageView portraitIv;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.setting_iv)
    ImageView settingIv;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private VirtualLayoutManager layoutManager;
    private DelegateAdapter delegateAdapter;
    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        layoutManager = new VirtualLayoutManager(this);
        delegateAdapter = new DelegateAdapter(layoutManager);
        recyclerView.setLayoutManager(layoutManager);
        mainAdapter = new MainAdapter();
        delegateAdapter.addAdapter(mainAdapter);
        recyclerView.setAdapter(delegateAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                float alpha = layoutManager.getOffsetToStart()*1.0f/dip2px(500);
                alphaView.setAlpha(alpha);
                if (alpha>0.3){
                    titleTv.setText("我的");
                    portraitIv.setImageResource(R.mipmap.ic_launcher_round);
                    settingIv.setImageResource(R.mipmap.icon_nav_share_black);
                }else {
                    titleTv.setText("");
                    portraitIv.setImageResource(0);
                    settingIv.setImageResource(R.mipmap.icon_nav_share_white);
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private int dip2px(int dipValue) {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        float scale = dm.density;
        return (int) (dipValue * scale + 0.5f);
    }
}
