package com.ascba.rebate.adapter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.PayType;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 支付类型适配器
 */

public class PayTypeAdapter extends BaseQuickAdapter<PayType,BaseViewHolder> {
    public PayTypeAdapter(int layoutResId, List<PayType> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final PayType item) {
        helper.setChecked(R.id.pay_type_cb,item.isSelect());
        helper.setOnClickListener(R.id.pay_type_cb, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setSelect(true);
                List<PayType> data = getData();
                if(data!=null && data.size()!=0){
                    for (int i = 0; i < data.size(); i++) {
                        PayType payType = data.get(i);
                        if(payType.isSelect()&& item!=payType){
                            payType.setSelect(false);
                        }
                    }

                }
                notifyDataSetChanged();
            }
        });
        helper.setImageResource(R.id.pay_icon,item.getIcon());
        helper.setText(R.id.pay_type_title,item.getTitle());
        helper.setText(R.id.pay_type_content,item.getContent());
    }
}
