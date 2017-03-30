package com.ascba.rebate.adapter;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.FirstRec;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by 李平 on 2017/3/30 0030.11:31
 */

public class AwardAdapter extends BaseQuickAdapter <FirstRec,BaseViewHolder>{
    public AwardAdapter(int layoutResId, List<FirstRec> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FirstRec item) {
        helper.setText(R.id.first_rec_name,"推荐-"+item.getName()+"("+ item.getGroupName()+")");
        helper.setText(R.id.first_rec_money,"+"+item.getMoney()+"元兑现券");
        helper.setText(R.id.first_rec_time,item.getTime());
    }
}
