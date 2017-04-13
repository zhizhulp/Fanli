package com.ascba.rebate.adapter;

import android.widget.CheckBox;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.ReceiveAddressBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by 李鹏 on 2017/04/05 0005.
 */

public class ReceiveAddressAdapter extends BaseQuickAdapter<ReceiveAddressBean, BaseViewHolder> {


    public ReceiveAddressAdapter(int layoutResId, List<ReceiveAddressBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReceiveAddressBean item) {
        //姓名
        helper.setText(R.id.item_receive_address_name, item.getName());

        //隐藏手机号中间4位
        String phone = item.getPhone();
        if(phone!=null && phone.length()==11){
            phone = phone.substring(0, 3) + "****" + phone.substring(7, 11);
        }
        helper.setText(R.id.item_receive_address_phone, phone);

        //地址
        helper.setText(R.id.item_receive_address_address, item.getAddress());
        helper.addOnClickListener(R.id.item_receive_address_del);
        helper.addOnClickListener(R.id.item_receive_address_edit);

        CheckBox checkBox = helper.getView(R.id.item_receive_address_check);
        if (item.getIsDefault().equals("1")) {
            checkBox.setChecked(true);
            checkBox.setEnabled(false);
        } else {
            checkBox.setChecked(false);
            checkBox.setEnabled(true);
        }
        helper.addOnClickListener(R.id.item_receive_address_check);
    }
}
