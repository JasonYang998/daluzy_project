package com.daluzy.daluzy_project.daluzy_project.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;

public class MyGridLayoutManager extends GridLayoutManager {
    private boolean hScroll;
    private boolean vScroll;

    public void sethScroll(boolean hScroll) {
        this.hScroll = hScroll;
    }

    public void setvScroll(boolean vScroll) {
        this.vScroll = vScroll;
    }

    public MyGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public MyGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public boolean canScrollHorizontally() {
        return hScroll;
    }

    @Override
    public boolean canScrollVertically() {
        return vScroll;
    }
}
