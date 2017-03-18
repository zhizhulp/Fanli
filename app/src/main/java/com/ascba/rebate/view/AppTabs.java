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

    private TextView tvZero;
    private TextView tvOne;
    private TextView tvTwo;
    private TextView tvThree;
    private TextView tvFour;

    private Callback callback;
    private int filPos=0;//代表被选择的位置（默认）

    public void setFilPos(int filPos) {
        this.filPos = filPos;
    }

    public TextView getTvTwo() {
        return tvTwo;
    }

    public ImageView getImOne() {
        return imOne;
    }

    public ImageView getImZero() {
        return imZero;
    }

    public ImageView getImThree() {
        return imThree;
    }

    public ImageView getImFour() {
        return imFour;
    }

    public TextView getTvZero() {
        return tvZero;
    }

    public TextView getTvOne() {
        return tvOne;
    }

    public TextView getTvThree() {
        return tvThree;
    }

    public TextView getTvFour() {
        return tvFour;
    }

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

        tvZero = ((TextView) findViewById(R.id.tv_tabs_zero));
        tvOne = ((TextView) findViewById(R.id.tv_tabs_one));
        tvTwo = ((TextView) findViewById(R.id.tv_tabs_two));
        tvThree = ((TextView) findViewById(R.id.tv_tabs_three));
        tvFour = ((TextView) findViewById(R.id.tv_tabs_four));


        View viewZero = findViewById(R.id.tabs_zero_par);
        View viewOne = findViewById(R.id.tabs_one_par);
        View viewThree = findViewById(R.id.tabs_three_par);
        View viewFour = findViewById(R.id.tabs_four_par);

        viewZero.setOnClickListener(this);
        viewOne.setOnClickListener(this);
        tvTwo.setOnClickListener(this);//商城
        viewThree.setOnClickListener(this);
        viewFour.setOnClickListener(this);

    }

    public interface Callback {
        void clickZero(View v);

        void clickOne(View v);

        void clickTwo(View v);

        void clickThree(View v);

        void clickFour(View v);
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
                tvZero.setTextColor(getResources().getColor(R.color.moneyBarColor));
                imZero.setImageResource(R.mipmap.tab_main_select);
                statusChaByPosition(0,filPos);
                filPos=0;
                callback.clickZero(v);
                break;
            case R.id.tabs_one_par://周边
                tvOne.setTextColor(getResources().getColor(R.color.moneyBarColor));
                imOne.setImageResource(R.mipmap.tab_zhoubian_select);
                statusChaByPosition(1,filPos);
                filPos=1;
                callback.clickOne(v);
                break;
            case R.id.tv_tabs_two://商城
                tvTwo.setTextColor(getResources().getColor(R.color.moneyBarColor));
                statusChaByPosition(2,filPos);
                filPos=2;
                callback.clickTwo(v);
                break;
            case R.id.tabs_three_par://财富
                tvThree.setTextColor(getResources().getColor(R.color.moneyBarColor));
                imThree.setImageResource(R.mipmap.tab_caifu_select);
                statusChaByPosition(3,filPos);
                filPos=3;
                callback.clickThree(v);
                break;
            case R.id.tabs_four_par://我
                tvFour.setTextColor(getResources().getColor(R.color.moneyBarColor));
                imFour.setImageResource(R.mipmap.tab_me_select);
                statusChaByPosition(4,filPos);
                filPos=4;
                callback.clickFour(v);
                break;
        }
    }

    public void statusChaByPosition(int currentPos, int beforePos) {
        if(beforePos!=currentPos){
            switch (beforePos){
                case 0:
                    imZero.setImageResource(R.mipmap.tab_main);
                    tvZero.setTextColor(getResources().getColor(R.color.textgray));
                    break;
                case 1:
                    imOne.setImageResource(R.mipmap.tab_zhoubian);
                    tvOne.setTextColor(getResources().getColor(R.color.textgray));
                    break;
                case 2:
                    tvTwo.setTextColor(getResources().getColor(R.color.textgray));
                    break;
                case 3:
                    imThree.setImageResource(R.mipmap.tab_caifu);
                    tvThree.setTextColor(getResources().getColor(R.color.textgray));
                    break;
                case 4:
                    imFour.setImageResource(R.mipmap.tab_me);
                    tvFour.setTextColor(getResources().getColor(R.color.textgray));
                    break;
            }
        }
    }


}
