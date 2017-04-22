package com.ascba.rebate.adapter;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.NewsBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/31 0031.
 * 消息-最新公告
 */

public class MessageLatestAdapter extends BaseQuickAdapter<NewsBean,BaseViewHolder>{
    public MessageLatestAdapter(int layoutResId, List<NewsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsBean item) {
        helper.setText(R.id.latest_title,item.getTitle());
        helper.setText(R.id.latest_time,item.getTime());
        helper.setText(R.id.latest_date,item.getDate());
        helper.setText(R.id.latest_content,item.getContent());
    }
}
