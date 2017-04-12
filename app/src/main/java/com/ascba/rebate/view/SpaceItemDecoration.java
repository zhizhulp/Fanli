package com.ascba.rebate.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ascba.rebate.R;

/**
 * Created by 李鹏 on 2017/04/10 0010.
 * GridLayoutManager item 边距
 * 第一个item左边距0，其他为space，每行item数量为num
 * int space = getResources().getDimensionPixelSize(2);
 * recyleView.addItemDecoration(new SpacesItemDecoration(2, space));
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int num;
    private Paint paint;

    public SpaceItemDecoration(Context context,int num, int space) {
        this.num = num;
        this.space = space;
        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.light_gray));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = space;
        outRect.bottom = space;
        //由于每行都只有num个，所以第一个都是num的倍数，把左边距设为0
        if (parent.getChildLayoutPosition(view) % num == 0) {
            outRect.left = 0;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);

            int top1 = view.getBottom();
            int bottom1 = top1 + space;
            int left1 = view.getLeft();
            int right1 = left1 + view.getWidth();
            c.drawRect(left1, top1, right1, bottom1, paint);

            if (i % 2 == 0) {
                int top2 = view.getTop();
                int bottom2 = top2 + view.getHeight();
                int left2 = view.getWidth();
                float right2 = left2 + space;
                c.drawRect(left2, top2, right2, bottom2, paint);
            }
        }
    }
}
