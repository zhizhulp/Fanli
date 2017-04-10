package com.ascba.rebate.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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

    public SpaceItemDecoration(int num, int space) {
        this.num = num;
        this.space = space;
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
}
