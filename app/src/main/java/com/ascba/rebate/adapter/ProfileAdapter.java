package com.ascba.rebate.adapter;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.GoodsAttr;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.view.RadioGroupEx;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

/**
 * 购物车-规格 适配器
 */

public class ProfileAdapter extends BaseQuickAdapter<GoodsAttr,BaseViewHolder> {
    public ProfileAdapter(int layoutResId, List<GoodsAttr> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsAttr item) {
        helper.setText(R.id.goods_attrs_title,item.getTitle());
        RadioGroupEx rgEx=helper.getView(R.id.goods_attrs_content);
        List<GoodsAttr.Attrs> strs = item.getStrs();
        for (GoodsAttr.Attrs s : strs) {
            final RadioButton rb=new RadioButton(mContext);
            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        rb.setTextColor(mContext.getResources().getColor(R.color.white));
                    }else {
                        rb.setTextColor(mContext.getResources().getColor(R.color.shop_normal_text_color));
                    }

                }
            });
            rb.setButtonDrawable(new ColorDrawable());
            rb.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.goods_standrad_bg));
            rb.setText(s.getContent());
            int textColor = s.getTextColor();
            if(textColor==0){//未选择
                rb.setEnabled(true);
                rb.setTextColor(mContext.getResources().getColor(R.color.shop_normal_text_color));
            }else if(textColor==1){//选择
                rb.setEnabled(true);
                rb.setTextColor(mContext.getResources().getColor(R.color.white));
            }else {//不可用
                rb.setEnabled(false);
                rb.setTextColor(0xffd1d1d1);
            }
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0,ScreenDpiUtils.dip2px(mContext,14),ScreenDpiUtils.dip2px(mContext,11),0);
            rb.setLayoutParams(lp );
            rgEx.addView(rb);
        }
    }
}
