package com.example.doublerecyclerview.view.banner.select;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import com.example.doublerecyclerview.view.banner.BaseAnimator;


public class RotateEnter extends BaseAnimator {
    public RotateEnter() {
        this.mDuration = 200;
    }

    public void setAnimation(View view) {
        this.mAnimatorSet.playTogether(new Animator[]{
                ObjectAnimator.ofFloat(view, "rotation", 0, 180)});
    }
}
