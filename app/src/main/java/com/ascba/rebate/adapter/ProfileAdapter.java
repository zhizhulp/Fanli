package com.ascba.rebate.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

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
        List<String> strs = item.getStrs();
        for (String s : strs) {
            RadioButton rb=new RadioButton(mContext);
            rb.setButtonDrawable(new ColorDrawable());
            rb.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.goods_standrad_bg));
            rb.setText(s);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.leftMargin= ScreenDpiUtils.dip2px(mContext,11);
            rb.setLayoutParams(lp );
            rgEx.addView(rb);
        }
    }
}
