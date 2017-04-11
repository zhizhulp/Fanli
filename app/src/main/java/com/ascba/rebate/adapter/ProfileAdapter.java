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
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback{
        void click(GoodsAttr.Attrs s, GoodsAttr item);
    }
    public ProfileAdapter(int layoutResId, List<GoodsAttr> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final GoodsAttr item) {
        helper.setText(R.id.goods_attrs_title,item.getTitle());
        RadioGroupEx rgEx=helper.getView(R.id.goods_attrs_content);
        rgEx.removeAllViews();//清除之前的视图
        final List<GoodsAttr.Attrs> strs = item.getStrs();
        for (final GoodsAttr.Attrs s : strs) {
            final RadioButton rb=new RadioButton(mContext);
            rb.setChecked(s.isHasCheck());
            rb.setTextColor(s.getTextStatus());

            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    s.setHasCheck(rb.isChecked());
                    s.setTextStatus(1);
                    for (int i = 0; i < strs.size(); i++) {
                        GoodsAttr.Attrs attrs = strs.get(i);
                        if(attrs.getTextStatus()!=2){
                            if(attrs.getTextStatus()==1 && attrs!=s){
                                attrs.setTextStatus(0);
                                attrs.setHasCheck(false);
                            }
                        }
                    }
                    //父类置true
                    item.setSelect(true);

                    if(callback!=null){
                        callback.click(s,item);
                    }
                    notifyItemChanged(helper.getAdapterPosition());
                }
            });
            rb.setButtonDrawable(new ColorDrawable());//去掉圆圈
            rb.setText(s.getContent());
            int textStatus = s.getTextStatus();
            if(textStatus==0){//未选择
                rb.setEnabled(true);
                rb.setTextColor(mContext.getResources().getColor(R.color.shop_normal_text_color));
                rb.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.sta_no_press_shape));
            }else if(textStatus==1){//选择
                rb.setEnabled(true);
                rb.setTextColor(mContext.getResources().getColor(R.color.white));
                rb.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.sta_press_shape));
            }else {//不可用
                rb.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.sta_no_press_shape));
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
