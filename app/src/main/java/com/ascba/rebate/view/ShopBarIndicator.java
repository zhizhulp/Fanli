package com.ascba.rebate.view;

import android.app.Activity;
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
 * 商城ActionBar
 */
public class ShopBarIndicator extends RelativeLayout implements View.OnClickListener {
    private ImageView imBack;
    private TextView tvTitle;
    private Callback callback;
    private Context context;
    private ImageView img1, img2;
    private ImageView imgIndicator1, imgIndicator2;
    private RelativeLayout imgLayout1, imgLayout2;

    public ShopBarIndicator(Context context) {
        super(context);
    }

    public ShopBarIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public ShopBarIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.shop_bar_indicator, this, true);

        imBack = (ImageView) findViewById(R.id.abar_im_back);//返回图标
        tvTitle = (TextView) findViewById(R.id.abar_tv_title);//标题
        img1 = (ImageView) findViewById(R.id.abar_im1);
        img2 = (ImageView) findViewById(R.id.abar_im2);

        imgIndicator1 = (ImageView) findViewById(R.id.abar_im1_indicator);
        imgIndicator2 = (ImageView) findViewById(R.id.abar_im2_indicator);
        imgLayout1 = (RelativeLayout) findViewById(R.id.abar_im1_layout);
        imgLayout2 = (RelativeLayout) findViewById(R.id.abar_im2_layout);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShopBarIndicator);

        String title = typedArray.getString(R.styleable.ShopBarIndicator_bar_title);//自定义标题
        tvTitle.setText(title);

        int img1Resource = typedArray.getResourceId(R.styleable.ShopBarIndicator_bar_img1, R.mipmap.shop_no_msg);
        img1.setImageResource(img1Resource);

        int img2Resource = typedArray.getResourceId(R.styleable.ShopBarIndicator_bar_img2, R.mipmap.icon_cart_black);
        img2.setImageResource(img2Resource);

        boolean imgIn1 = typedArray.getBoolean(R.styleable.ShopBarIndicator_bar_img1_indicator, false);
        if (imgIn1) {
            imgIndicator1.setVisibility(VISIBLE);
        } else {
            imgIndicator1.setVisibility(INVISIBLE);
        }

        boolean imgIn2 = typedArray.getBoolean(R.styleable.ShopBarIndicator_bar_img2_indicator, false);
        if (imgIn2) {
            imgIndicator2.setVisibility(VISIBLE);
        } else {
            imgIndicator2.setVisibility(INVISIBLE);
        }

        typedArray.recycle();

        imBack.setOnClickListener(this);
        imgLayout1.setOnClickListener(this);
        imgLayout2.setOnClickListener(this);
    }

    //设置返回图标是否显示
    public void setBackEnable(boolean enable) {
        if (enable) {
            imBack.setVisibility(VISIBLE);
        } else {
            imBack.setVisibility(GONE);
        }
    }

    public void setImg1Icon(int resource, boolean isIndicator) {
        img1.setImageResource(resource);
        if (isIndicator) {
            imgIndicator1.setVisibility(VISIBLE);
        } else {
            imgIndicator1.setVisibility(INVISIBLE);
        }
    }

    public void setImg1Msg(boolean isIndicator) {
        if (isIndicator) {
            imgIndicator1.setVisibility(VISIBLE);
        } else {
            imgIndicator1.setVisibility(INVISIBLE);
        }
    }

    public void setImg2Icon(int resource, boolean isIndicator) {
        img2.setImageResource(resource);
        if (isIndicator) {
            imgIndicator2.setVisibility(VISIBLE);
        } else {
            imgIndicator2.setVisibility(INVISIBLE);
        }
    }

    public void setImg2Msg(boolean isIndicator) {
        if (isIndicator) {
            imgIndicator2.setVisibility(VISIBLE);
        } else {
            imgIndicator2.setVisibility(INVISIBLE);
        }
    }


    //设置标题
    public void setTitle(String title) {
        tvTitle.setText(title);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.abar_im_back:
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
                if (callback != null) {
                    callback.back(v);
                }
                break;
            case R.id.abar_im1_layout:
                callback.clkImg1(v);
                break;

            case R.id.abar_im2_layout:
                callback.clkImg2(v);
                break;
        }
    }


    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void back(View v);

        void clkImg1(View v);

        void clkImg2(View v);

    }
}
