package com.wanandroid.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 随着RecycleView滑动放大的Behavior
 */
public class NestedScaleBehavior extends CoordinatorLayout.Behavior<View> {

    private AnimatorSet animatorSet;

    public NestedScaleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

        //由于RecycleView很常见于与SwipeRefreshLayout使用，所以提取
        if (target instanceof SwipeRefreshLayout) {
            target = ((SwipeRefreshLayout) target).getChildAt(0);
        }

        if (target instanceof RecyclerView) {
            int scrollY = ((RecyclerView) target).computeVerticalScrollOffset();
            if (scrollY > coordinatorLayout.getHeight() / 3 && child.getScaleX() < 1.0f) {
                if (animatorSet == null || !animatorSet.isRunning()) {
                    ObjectAnimator scaleX = ObjectAnimator.ofFloat(child, "scaleX", 0.0f, 1.0f);
                    ObjectAnimator scaleY = ObjectAnimator.ofFloat(child, "scaleY", 0.0f, 1.0f);
                    animatorSet = new AnimatorSet();
                    animatorSet.playTogether(scaleX, scaleY);
                    animatorSet.setDuration(500);
                    animatorSet.start();
                }
            }
            if (scrollY <= 60) {
                if (animatorSet != null && animatorSet.isRunning()) {
                    animatorSet.end();
                }
                child.setScaleY(0.0f);
                child.setScaleX(0.0f);
            }
        }
    }
}
