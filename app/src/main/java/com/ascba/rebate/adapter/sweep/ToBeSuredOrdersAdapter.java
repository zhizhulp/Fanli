package com.ascba.rebate.adapter.sweep;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.sweep.ToBeSuredOrdersEntity;
import com.ascba.rebate.utils.TimeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lenovo on 2017/7/5.
 */

public class ToBeSuredOrdersAdapter extends BaseQuickAdapter<ToBeSuredOrdersEntity.DataListBean,BaseViewHolder>{


    public ToBeSuredOrdersAdapter(@LayoutRes int layoutResId, @Nullable List<ToBeSuredOrdersEntity.DataListBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, ToBeSuredOrdersEntity.DataListBean item) {
        ImageView imageView = helper.getView(R.id.myallorders_icon);//商品图片
        Picasso.with(mContext).load(UrlUtils.baseWebsite+item.getAvatar()).into(imageView);
        helper.setText(R.id.myallorders_money, item.getMoney());//名称
        helper.setText(R.id.myallorders_category_txt, TimeUtils.milliseconds2String(item.getCreate_time() * 1000));//交易时间
        helper.setText(R.id.myallorders_pay_cash,item.getPay_type_text());//支付类型
        helper.setText(R.id.await_sure_order,item.getOrder_status_text());//确认状态


    }
}
