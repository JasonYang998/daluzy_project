package com.daluzy.daluzy_project.daluzy_project.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

public class MyLinearLayoutManager extends LinearLayoutManager {
    private boolean hScroll;
    private boolean vScroll;

    public void sethScroll(boolean hScroll) {
        this.hScroll = hScroll;
    }

    public void setvScroll(boolean vScroll) {
        this.vScroll = vScroll;
    }

    public MyLinearLayoutManager(Context context) {
        super(context);
    }

    public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public MyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
