package com.ascba.rebate.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;

/**
 * 竞拍底部导航
 */

public class AuctionTabs extends RelativeLayout implements View.OnClickListener {
    private ImageView imOne;
    private ImageView imZero;
    private ImageView imThree;
    private ImageView imFour;

    private TextView tvZero;
    private TextView tvOne;
    private TextView tvThree;
    private TextView tvFour;

    private TextView tvThreeNoty;


    private Callback callback;
    private int filPos = 0;//代表被选择的位置（默认）

    public AuctionTabs(Context context) {
        super(context);
        initViews(context, null);
    }

    public AuctionTabs(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);

    }

    public AuctionTabs(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }


    private void initViews(Context context, AttributeSet attrs) {

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ShopTabs);
        String text2 = array.getString(R.styleable.ShopTabs_text2);
        array.recycle();

        LayoutInflater.from(context).inflate(R.layout.shop_tabs, this, true);

        imZero = ((ImageView) findViewById(R.id.im_tabs_zero));
        imOne = ((ImageView) findViewById(R.id.im_tabs_one));
        imThree = ((ImageView) findViewById(R.id.im_tabs_three));
        imFour = ((ImageView) findViewById(R.id.im_tabs_four));

        tvZero = ((TextView) findViewById(R.id.tv_tabs_zero));
        tvOne = ((TextView) findViewById(R.id.tv_tabs_one));
        tvThree = ((TextView) findViewById(R.id.tv_tabs_three));
        tvFour = ((TextView) findViewById(R.id.tv_tabs_four));
        // TODO: 2017/6/1
        tvOne.setText(text2);

        tvThreeNoty = (TextView) findViewById(R.id.im_tabs_three_noty);

        View viewZero = findViewById(R.id.tabs_zero_par);
        View viewOne = findViewById(R.id.tabs_one_par);
        View viewThree = findViewById(R.id.tabs_three_par);
        View viewFour = findViewById(R.id.tabs_four_par);

        viewZero.setOnClickListener(this);
        viewOne.setOnClickListener(this);
        viewThree.setOnClickListener(this);
        viewFour.setOnClickListener(this);

        statusChaByPosition(0, filPos);

    }

    public interface Callback {
        void clickZero(View v);

        void clickOne(View v);

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
                statusChaByPosition(0, filPos);
                callback.clickZero(v);
                break;
            case R.id.tabs_one_par://主会场
                statusChaByPosition(1, filPos);
                callback.clickOne(v);
                break;

            case R.id.tabs_three_par://购物车(需要登陆)
                callback.clickThree(v);
                break;
            case R.id.tabs_four_par://我(需要登陆)
                callback.clickFour(v);
                break;
        }
    }

    /**
     * 改变tab状态
     * @param currentPos 当前位置
     * @param beforePos 之前位置
     */
    public void statusChaByPosition(int currentPos, int beforePos) {
        if (beforePos != currentPos) {
            filPos = currentPos;
            switch (beforePos) {
                case 0:
                    imZero.setImageResource(R.mipmap.tab_main);
                    tvZero.setTextColor(getResources().getColor(R.color.textgray));
                    break;
                case 1:
                    imOne.setImageResource(R.mipmap.fenlei);
                    tvOne.setTextColor(getResources().getColor(R.color.textgray));
                    break;
                case 2:
                    imThree.setImageResource(R.mipmap.tab_shop);
                    tvThree.setTextColor(getResources().getColor(R.color.textgray));
                    break;
                case 3:
                    imFour.setImageResource(R.mipmap.tab_me);
                    tvFour.setTextColor(getResources().getColor(R.color.textgray));
                    break;
                default:
                    break;
            }
            switch (currentPos) {
                case 0:
                    tvZero.setTextColor(getResources().getColor(R.color.moneyBarColor));
                    imZero.setImageResource(R.mipmap.tab_main_select);
                    break;
                case 1:
                    tvOne.setTextColor(getResources().getColor(R.color.moneyBarColor));
                    imOne.setImageResource(R.mipmap.tab_type_select);
                    break;
                case 2:
                    tvThree.setTextColor(getResources().getColor(R.color.moneyBarColor));
                    imThree.setImageResource(R.mipmap.tab_shop_select);
                    break;
                case 3:
                    tvFour.setTextColor(getResources().getColor(R.color.moneyBarColor));
                    imFour.setImageResource(R.mipmap.tab_me_select);
                    break;
                default:
                    break;
            }
        }
    }

    public int getFilPos() {
        return filPos;
    }

    public void setFilPos(int filPos) {
        this.filPos = filPos;
    }

    public void setThreeNoty(int count) {
        if (count > 0) {
            tvThreeNoty.setVisibility(VISIBLE);
        } else {
            tvThreeNoty.setVisibility(GONE);
        }
        tvThreeNoty.setText(String.valueOf(count));
    }

    public int getThreeNotyNum() {
        return Integer.parseInt(tvThreeNoty.getText().toString());
    }

}
