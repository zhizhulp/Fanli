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
 * 商城ActionBar
 */
public class ShopABar extends RelativeLayout implements View.OnClickListener {
    private ImageView imBack;
    private TextView tvTitle;
    private ImageView imMsg;
    private ImageView imOther;
    private Callback callback;
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
    private void initViews(Context context,AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.shop_abar,this,true);

        imBack = ((ImageView) findViewById(R.id.abar_im_back));//返回图标
        tvTitle = ((TextView) findViewById(R.id.abar_tv_title));//标题
        imMsg = ((ImageView) findViewById(R.id.abar_im_msg));//消息图标(固定图标，有2种状态)
        imOther = ((ImageView) findViewById(R.id.abar_im_other));//可自定义图标

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShopABar);
        String title = ta.getString(R.styleable.ShopABar_abar_title);//自定义标题
        int imOtherId = ta.getResourceId(R.styleable.ShopABar_abar_icon, R.mipmap.abar_search);//自定义图标
        ta.recycle();

        imBack.setOnClickListener(this);
        tvTitle.setText(title);
        imOther.setImageResource(imOtherId);
        imMsg.setOnClickListener(this);
        imOther.setOnClickListener(this);
    }
    //设置返回图标是否显示
    public void setBackEnable(boolean enable){
        if(enable){
            imBack.setVisibility(VISIBLE);
        }else {
            imBack.setVisibility(GONE);
        }

    }
    //设置搜索图标是否显示
    public void setImageOtherEnable(boolean enable){
        if(enable){
            imOther.setVisibility(VISIBLE);
        }else {
            imOther.setVisibility(GONE);
        }
    }
    //设置标题
    public void setTitle(String title){
        tvTitle.setText(title);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.abar_im_back:
                if(callback!=null){
                    callback.back(v);
                }
                break;
            case R.id.abar_im_msg:
                if(callback!=null){
                    callback.clkMsg(v);
                }
                break;
            case R.id.abar_im_other:
                if(callback!=null){
                    callback.clkOther(v);
                }
                break;
        }
    }
    //改变图标状态
    public void setImMsgSta(int resId){
        imOther.setImageResource(resId);
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
