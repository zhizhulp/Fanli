package com.ascba.rebate.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;

import java.util.ArrayList;
import java.util.List;

/**
 * AppTabs
 */

public class AppTabs extends RelativeLayout implements View.OnClickListener {
    private ImageView imOne;
    private ImageView imZero;
    private ImageView imThree;
    private ImageView imFour;
    private List<TextView> tvs = new ArrayList<>();
    private int type;//0 商城 1代理
    private Callback callback;
    private int filPos;//代表被选择的位置

    public AppTabs(Context context) {
        super(context);
        initViews(context);
    }

    public AppTabs(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);

    }

    public AppTabs(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);

    }


    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.app_tabs, this, true);

        imZero = ((ImageView) findViewById(R.id.im_tabs_zero));
        imOne = ((ImageView) findViewById(R.id.im_tabs_one));
        /*imTwo = ((ImageView) findViewById(R.id.im_tabs_two));*/
        imThree = ((ImageView) findViewById(R.id.im_tabs_three));
        imFour = ((ImageView) findViewById(R.id.im_tabs_four));

        TextView tvZero = ((TextView) findViewById(R.id.tv_tabs_zero));
        TextView tvOne = ((TextView) findViewById(R.id.tv_tabs_one));
        TextView tvTwo = ((TextView) findViewById(R.id.tv_tabs_two));
        TextView tvThree = ((TextView) findViewById(R.id.tv_tabs_three));
        TextView tvFour = ((TextView) findViewById(R.id.tv_tabs_four));
        tvs.add(tvZero);
        tvs.add(tvOne);
        tvs.add(tvTwo);
        tvs.add(tvThree);
        tvs.add(tvFour);

        View viewZero = findViewById(R.id.tabs_zero_par);
        View viewOne = findViewById(R.id.tabs_one_par);
        View viewThree = findViewById(R.id.tabs_three_par);
        View viewFour = findViewById(R.id.tabs_four_par);

        viewZero.setOnClickListener(this);
        viewOne.setOnClickListener(this);
        tvTwo.setOnClickListener(this);
        viewThree.setOnClickListener(this);
        viewFour.setOnClickListener(this);

        filPos = 2;
    }

    public interface Callback {
        void clickZero(View v, int type);

        void clickOne(View v, int type);

        void clickTwo(View v, int type);

        void clickThree(View v, int type);

        void clickFour(View v, int type);
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tabs_zero_par://首页
                //设置图标
                draClickChg(0);
                for (int i = 0; i < tvs.size(); i++) {
                    //设置字体颜色
                    if (i == 0) {
                        tvs.get(i).setTextColor(getResources().getColor(R.color.moneyBarColor));
                    } else {
                        tvs.get(i).setTextColor(getResources().getColor(R.color.textgray));
                    }

                }
                //设置文字
                tvs.get(1).setText("消息");
                tvs.get(4).setText("我");
                callback.clickZero(v, type);
                if (type == 0) {
                    type = 1;
                }
                break;
            case R.id.tabs_one_par://分类--消息
                draClickChg(1);
                for (int i = 0; i < tvs.size(); i++) {
                    //设置字体颜色
                    if (i == 1) {
                        tvs.get(i).setTextColor(getResources().getColor(R.color.moneyBarColor));
                    } else {
                        tvs.get(i).setTextColor(getResources().getColor(R.color.textgray));
                    }
                }
                callback.clickOne(v, type);
                break;
            case R.id.tv_tabs_two://商城
                draClickChg(2);
                for (int i = 0; i < tvs.size(); i++) {
                    //设置字体颜色
                    if (i == 2) {
                        tvs.get(i).setTextColor(getResources().getColor(R.color.moneyBarColor));
                    } else {
                        tvs.get(i).setTextColor(getResources().getColor(R.color.textgray));
                    }
                }
                //设置文字
                tvs.get(1).setText("分类");
                tvs.get(4).setText("设置");
                callback.clickTwo(v, type);
                if (type == 1) {
                    type = 0;
                }
                break;
            case R.id.tabs_three_par://购物车
                draClickChg(3);
                for (int i = 0; i < tvs.size(); i++) {
                    //设置字体颜色
                    if (i == 3) {
                        tvs.get(i).setTextColor(getResources().getColor(R.color.moneyBarColor));
                    } else {
                        tvs.get(i).setTextColor(getResources().getColor(R.color.textgray));
                    }
                }
                callback.clickThree(v, type);
                break;
            case R.id.tabs_four_par://商城设置--我
                draClickChg(4);
                for (int i = 0; i < tvs.size(); i++) {
                    //设置字体颜色
                    if (i == 4) {
                        tvs.get(i).setTextColor(getResources().getColor(R.color.moneyBarColor));
                    } else {
                        tvs.get(i).setTextColor(getResources().getColor(R.color.textgray));
                    }
                }
                callback.clickFour(v, type);
                break;
        }
    }

    public void draClickChg(int position) {
        switch (position) {
            case 0:
                imZero.setImageResource(R.mipmap.tab_main_select);
                switch (filPos) {//上一个点击的位置
                    case 0:
                        break;
                    case 1:
                        imOne.setImageResource(R.mipmap.tab_message);
                        break;
                    case 2:
                        imOne.setImageResource(R.mipmap.tab_message);
                        break;
                    case 3:
                        imOne.setImageResource(R.mipmap.tab_message);
                        imThree.setImageResource(R.mipmap.tab_shop);
                        break;
                    case 4:
                        imOne.setImageResource(R.mipmap.tab_message);
                        imFour.setImageResource(R.mipmap.tab_me);
                        break;
                    default:
                        break;
                }

                break;
            case 1:
                if(type==0){
                    imOne.setImageResource(R.mipmap.shop_test_06);
                }else {
                    imOne.setImageResource(R.mipmap.tab_message_select);
                }

                switch (filPos) {//上一个点击的位置
                    case 0:
                        imZero.setImageResource(R.mipmap.tab_main);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        imThree.setImageResource(R.mipmap.tab_shop);
                        break;
                    case 4:
                        imFour.setImageResource(R.mipmap.tab_me);
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                switch (filPos) {//上一个点击的位置
                    case 0:
                        imZero.setImageResource(R.mipmap.tab_main);
                        imOne.setImageResource(R.mipmap.shop_test_05);
                        break;
                    case 1:
                        imOne.setImageResource(R.mipmap.shop_test_05);
                        break;
                    case 2:
                        break;
                    case 3:
                        imThree.setImageResource(R.mipmap.tab_shop);
                        if(type==1){
                            imOne.setImageResource(R.mipmap.shop_test_05);
                        }
                        break;
                    case 4:
                        imFour.setImageResource(R.mipmap.tab_me);
                        if(type==1){
                            imOne.setImageResource(R.mipmap.shop_test_05);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case 3:
                imThree.setImageResource(R.mipmap.tab_shop_select);

                switch (filPos) {//上一个点击的位置
                    case 0:
                        imZero.setImageResource(R.mipmap.tab_main);
                        break;
                    case 1:
                        if (type == 0) {
                            imOne.setImageResource(R.mipmap.shop_test_05);
                        } else {
                            imOne.setImageResource(R.mipmap.tab_message);
                        }
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        imFour.setImageResource(R.mipmap.tab_me);
                        break;
                    default:
                        break;
                }
                break;
            case 4:
                imFour.setImageResource(R.mipmap.tab_me_select);

                switch (filPos) {//上一个点击的位置
                    case 0:
                        imZero.setImageResource(R.mipmap.tab_main);
                        break;
                    case 1:
                        if (type == 0) {
                            imOne.setImageResource(R.mipmap.shop_test_05);
                        } else {
                            imOne.setImageResource(R.mipmap.tab_message);
                        }
                        break;
                    case 2:
                        break;
                    case 3:
                        imThree.setImageResource(R.mipmap.tab_shop);
                        break;
                    case 4:
                        break;
                    default:
                        break;
                }
                break;
        }
        filPos = position;
    }
}
