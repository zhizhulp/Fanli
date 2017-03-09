package com.ascba.rebate.view.dropDownMultiPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.GoodsDetailsItem;
import com.ascba.rebate.view.dropDownMultiPager.transformer.ZoomPageTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/01 0001.
 */

@SuppressLint({"SetTextI18n", "InflateParams"})
public class DropDownMultiPagerView extends Dialog {

    private Context context;
    private MultiViewPager pager;
    private TextView textView;
    private List<GoodsDetailsItem> beanList;
    private OnDropDownMultiPagerViewItemClick onDropDownMultiPagerViewItemClick;

    public DropDownMultiPagerView(Context context, List<GoodsDetailsItem> beanList) {
        super(context, R.style.DropDown);
        this.context = context;
        this.beanList = beanList;
    }

    public void setOnDropDownMultiPagerViewItemClick(OnDropDownMultiPagerViewItemClick onDropDownMultiPagerViewItemClick) {
        this.onDropDownMultiPagerViewItemClick = onDropDownMultiPagerViewItemClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_dropdownfootprint, null);

        setContentView(view);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        Window dialogWindow = getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        lp.width = dm.widthPixels;
        lp.height = dip2px(context, 310);
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.TOP);
        dialogWindow.setWindowAnimations(R.style.DropDown);

        pager = (MultiViewPager) view.findViewById(R.id.pager);
        pager.setPageTransformer(true, new ZoomPageTransformer());

        textView = (TextView) view.findViewById(R.id.view_dropdownfootprint_text);

        List<View> list = new ArrayList<>();
        for (int i = 0; i < beanList.size(); i++) {
            DropDownMultiPagerItem item = new DropDownMultiPagerItem(context, i, beanList);
            list.add(item);
        }

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                textView.setText("我的足迹(" + (pager.getCurrentItem() + 1) + "/" + beanList.size() + ")");
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        DropDownMultiPagerAdapter adapter = new DropDownMultiPagerAdapter(list);

        pager.setAdapter(adapter);
        onclick();

        adapter.notifyDataSetChanged();

    }

    private int dip2px(Context context, float dpValue) {
        try {
            return (int) (dpValue * context.getResources().getDisplayMetrics().density + 0.5f);
        } catch (Exception e) {
            e.printStackTrace();
            return (int) (dpValue * 1 + 0.5f);
        }
    }

    /**
     * 点击事件
     */
    private void onclick() {
        pager.setOnTouchListener(new View.OnTouchListener() {
            int flage = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        flage = 0;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        flage = 1;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (flage == 0) {
                            int item = pager.getCurrentItem();
                            onDropDownMultiPagerViewItemClick.onItemClick(item);
                        }
                        break;


                }
                return false;
            }
        });
    }

    public interface OnDropDownMultiPagerViewItemClick {
        void onItemClick(int position);
    }
}