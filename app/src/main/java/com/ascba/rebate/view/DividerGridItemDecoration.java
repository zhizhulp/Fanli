package com.ascba.rebate.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.ascba.rebate.beans.ShopBaseItem;
import com.ascba.rebate.beans.ShopItemType;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by 李平 on 2017/7/6.
 * 商城商品分割线(后续需要完善，列数固定死的)
 */
public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;
    private int firstDrawLinePosition;//第一个需要划线的itemPosition
    private boolean isFirstDrawLine=true;//是否第一次划线
    private BaseQuickAdapter.SpanSizeLookup spanSize;
    private int lineWidth ;
    private int lineHeight;
    private Paint mPaint;

    public DividerGridItemDecoration(Context context, BaseQuickAdapter.SpanSizeLookup spanSize,int color) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
//        mDivider=context.getResources().getDrawable(resId);
        this.spanSize = spanSize;
        lineWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics());
        lineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics());

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {
        Log.d("ItemDecoration", "onDraw: ");
        drawHorizontal(c, parent);
        drawVertical(c, parent);

    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            spanCount = 2;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin
                    + /*mDivider.getIntrinsicWidth()*/lineWidth;
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + /*mDivider.getIntrinsicHeight()*/lineHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + /*mDivider.getIntrinsicWidth()*/lineWidth;

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
                                int childCount ) {
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1 ) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
            {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            }
        }
        return false;
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                              int childCount) {
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            childCount = childCount - childCount % spanCount;
            if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
                return true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition,
                               RecyclerView parent) {
        Log.d("ItemDecoration", "getItemOffsets: " + itemPosition);
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int spanSize = this.spanSize.getSpanSize((GridLayoutManager) layoutManager, itemPosition);
            Log.d("ItemDecoration", "spanSize: "+spanSize);
            if (spanSize == 30) {
                if(isFirstDrawLine){
                    firstDrawLinePosition=itemPosition;
                    isFirstDrawLine=false;
                }
                int spanCount = getSpanCount(parent);//固定为2
                int childCount = parent.getAdapter().getItemCount();
                if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
                {
                    outRect.set(0, 0, /*mDivider.getIntrinsicWidth()*/lineWidth, 0);
                    Log.d("ItemDecoration", "getItemOffsets: 最后一行");
                } else if (isLastColum(parent, itemPosition, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
                {
                    outRect.set(0, 0, 0, /*mDivider.getIntrinsicHeight()*/lineHeight);
                    Log.d("ItemDecoration", "getItemOffsets: 最后一列");
                } else {
                    outRect.set(0, 0, /*mDivider.getIntrinsicWidth()*/lineWidth,
                            /*mDivider.getIntrinsicHeight()*/lineHeight);
                    Log.d("ItemDecoration", "getItemOffsets: lineWidth"+lineWidth+",lineHeight"+lineHeight);
                }
            }else {
                outRect.set(0,0,0,0);
                Log.d("ItemDecoration", "getItemOffsets: set0000");
            }
        }



    }
}
