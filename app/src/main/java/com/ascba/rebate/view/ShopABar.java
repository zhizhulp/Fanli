package com.ascba.rebate.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;

/**
 * 商城ActionBar
 */
public class ShopABar extends RelativeLayout implements View.OnClickListener {
    private ImageView imBack;
    private TextView tvTitle;
    private FrameLayout imMsg;
    private FrameLayout imOther;
    private MsgView msgView1, msgView2;
    private Callback callback;
    private Context context;

    public ShopABar(Context context) {
        super(context);
    }

    public ShopABar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public ShopABar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.shop_abar, this, true);

        imBack = ((ImageView) findViewById(R.id.abar_im_back));//返回图标
        tvTitle = ((TextView) findViewById(R.id.abar_tv_title));//标题
        imMsg = ((FrameLayout) findViewById(R.id.abar_im_msg));//消息图标(固定图标，有2种状态)
        imOther = ((FrameLayout) findViewById(R.id.abar_im_other));//可自定义图标
        msgView1 = (MsgView) findViewById(R.id.abar_msg1);
        msgView2 = (MsgView) findViewById(R.id.abar_msg2);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShopABar);
        String title = ta.getString(R.styleable.ShopABar_abar_title);//自定义标题
        int icon = ta.getResourceId(R.styleable.ShopABar_abar_icon, R.mipmap.abar_search);
        boolean hasBack = ta.getBoolean(R.styleable.ShopABar_abar_has_back, true);
        ta.recycle();

        msgView2.setImage(icon);
        if(hasBack){
            imBack.setOnClickListener(this);
            imBack.setVisibility(VISIBLE);
        }else {
            imBack.setVisibility(GONE);
        }

        tvTitle.setText(title);
        imMsg.setOnClickListener(this);
        imOther.setOnClickListener(this);
    }

    //设置返回图标是否显示
    public void setBackEnable(boolean enable) {
        if (enable) {
            imBack.setVisibility(VISIBLE);
        } else {
            imBack.setVisibility(GONE);
        }
    }


    public void setImageOtherEnable(boolean enable) {
        if (enable) {
            imOther.setVisibility(VISIBLE);
        } else {
            imOther.setVisibility(GONE);
        }
    }

    public void setImageOther(int drawable) {
        msgView2.setImage(drawable);
    }

    public void setImageOtherIn(boolean hasMsg) {
        msgView2.setIsIndicator(hasMsg);
    }


    public void setMsgEnable(boolean enable) {
        if (enable) {
            imMsg.setVisibility(VISIBLE);
        } else {
            imMsg.setVisibility(GONE);
        }
    }

    //设置标题
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public MsgView getMsgView1() {
        return msgView1;
    }

    public MsgView getMsgView2() {
        return msgView2;
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
            case R.id.abar_im_msg:
                if (callback != null) {
                    callback.clkMsg(v);
                }
                break;
            case R.id.abar_im_other:
                if (callback != null) {
                    callback.clkOther(v);
                }
                break;
        }
    }

    //改变图标状态
    public void setImMsgSta(int resId) {
        msgView1.setImage(resId);
    }

    public void setImMsgStaIn(boolean hasMsg) {
        msgView1.setIsIndicator(hasMsg);
    }


    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void back(View v);

        void clkMsg(View v);

        void clkOther(View v);

    }
}
