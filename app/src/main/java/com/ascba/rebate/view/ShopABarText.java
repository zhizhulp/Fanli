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
public class ShopABarText extends RelativeLayout implements View.OnClickListener {
    private ImageView imBack;
    private TextView tvTitle;
    private TextView tvBtn;
    private Callback callback;
    private Context context;

    public ShopABarText(Context context) {
        super(context);
    }

    public ShopABarText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
        this.context=context;
    }

    public ShopABarText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
        this.context=context;
    }

    private void initViews(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.shop_abar_text, this, true);

        imBack = ((ImageView) findViewById(R.id.abar_im_back));//返回图标
        tvTitle = ((TextView) findViewById(R.id.abar_tv_title));//标题
        tvBtn = (TextView) findViewById(R.id.abar_btn);//按钮

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShopABar);
        String title = ta.getString(R.styleable.ShopABar_abar_title);//自定义标题

        ta.recycle();

        imBack.setOnClickListener(this);
        tvBtn.setOnClickListener(this);
        tvTitle.setText(title);
    }

    //设置返回图标是否显示
    public void setBackEnable(boolean enable) {
        if (enable) {
            imBack.setVisibility(VISIBLE);
        } else {
            imBack.setVisibility(GONE);
        }

    }

    public void setBtnEnable(boolean enable) {
        if (enable) {
            tvBtn.setVisibility(VISIBLE);
        } else {
            tvBtn.setVisibility(GONE);
        }
    }


    public void setBtnText(String text) {
        tvBtn.setText(text);
    }

    //设置标题
    public void setTitle(String title) {
        tvTitle.setText(title);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.abar_im_back:
                if(context instanceof Activity){
                    ((Activity) context).finish();
                }
                if (callback != null) {
                    callback.back(v);
                }
                break;
            case R.id.abar_btn:
                if (callback != null) {
                    callback.clkBtn(v);
                }
                break;
        }
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void back(View v);

        void clkBtn(View v);

    }
}
