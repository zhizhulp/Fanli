package com.ascba.rebate.adapter;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.MemMsg;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 学员证信息适配器
 */

public class MemMsgAdapter extends BaseQuickAdapter<MemMsg,BaseViewHolder> {
    public MemMsgAdapter(int layoutResId, List<MemMsg> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MemMsg item) {
        helper.setText(R.id.title,item.getTitle());
        helper.setText(R.id.content,item.getContent());
    }
}
