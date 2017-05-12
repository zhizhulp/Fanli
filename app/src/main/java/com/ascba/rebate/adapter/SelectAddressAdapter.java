package com.ascba.rebate.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.ReceiveAddressBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by 李鹏 on 2017/04/06 0006.
 * 确认订单——选择收货地址
 */

public class SelectAddressAdapter extends BaseQuickAdapter<ReceiveAddressBean, BaseViewHolder> {
    private Callback callback;
    public interface Callback{
        void click(int position);
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public SelectAddressAdapter(int layoutResId, List<ReceiveAddressBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReceiveAddressBean item) {
        //姓名
        helper.setText(R.id.item_select_address_username, item.getName());

        //隐藏手机号中间4位
        String phone = item.getPhone();
        if(phone!=null && phone.length()==11){
            phone = phone.substring(0, 3) + "****" + phone.substring(7, 11);
        }
        helper.setText(R.id.item_select_address_phone, phone);

        //地址
        helper.setText(R.id.item_select_address_address, item.getAddressDetl());

        //编辑
        helper.addOnClickListener(R.id.item_select_address_edit);

        //默认收货地址
        TextView defaultAddress = helper.getView(R.id.item_select_address_defaule);
        if (item.getIsDefault().equals("1")) {
            defaultAddress.setVisibility(View.VISIBLE);
        } else {
            defaultAddress.setVisibility(View.INVISIBLE);
        }

        helper.addOnClickListener(R.id.item_select_address_rl);

        //选中为当前收货地址
        CheckBox checkBox = helper.getView(R.id.item_select_address_check);
        checkBox.setChecked(item.isSelect());

        helper.setOnClickListener(R.id.btnDelete,createDelListener(helper));
    }

    private View.OnClickListener createDelListener(final BaseViewHolder helper) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != callback){
                    callback.click(helper.getAdapterPosition());
                }
            }
        };
    }
}
