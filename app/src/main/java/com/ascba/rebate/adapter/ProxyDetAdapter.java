package com.ascba.rebate.adapter;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.ProxyDet;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by 李平 on 2017/4/11 0011.17:02
 * 代理专区适配器
 */

public class ProxyDetAdapter extends BaseQuickAdapter<ProxyDet,BaseViewHolder> {
    public ProxyDetAdapter(int layoutResId, List<ProxyDet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProxyDet item) {
        helper.setImageResource(R.id.proxy_item_im_img,item.getIcon());
        helper.setText(R.id.proxy_item_tv_title,item.getTitle());
        helper.setText(R.id.proxy_item_tv_content,item.getContent());
    }
}
