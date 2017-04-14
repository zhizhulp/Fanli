package com.ascba.rebate.view.pullUpToLoadMoreView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class NickScrollView extends ScrollView {

    private ScrollListener mScrollListener;
    private OnScollChangedListener onScollChangedListener;

    public void setScrollListener(ScrollListener scrollListener) {
        this.mScrollListener = scrollListener;
    }

    public NickScrollView(Context context) {
        super(context);
    }

    public NickScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NickScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (mScrollListener != null) {
                    int contentHeight = getChildAt(0).getHeight();
                    int scrollHeight = getHeight();

                    int scrollY = getScrollY();
                    mScrollListener.onScroll(scrollY);

                    if (scrollY + scrollHeight >= contentHeight || contentHeight <= scrollHeight) {
                        mScrollListener.onScrollToBottom();
                    } else {
                        mScrollListener.notBottom();
                    }

                    if (scrollY <= 0) {
                        mScrollListener.onScrollToTop();
                    } else {
                        mScrollListener.noTop();
                    }

                }

                break;
        }
        boolean result = super.onTouchEvent(ev);
        requestDisallowInterceptTouchEvent(false);

        return result;
    }

    public interface ScrollListener {
        void onScrollToBottom();

        void onScrollToTop();

        void onScroll(int scrollY);

        void notBottom();

        void noTop();
    }

    public void setOnScollChangedListener(OnScollChangedListener onScollChangedListener) {
        this.onScollChangedListener = onScollChangedListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (onScollChangedListener != null) {
            onScollChangedListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public interface OnScollChangedListener {

        void onScrollChanged(NickScrollView scrollView, int x, int y, int oldx, int oldy);

    }
}
