package com.ascba.rebate.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.ProfileAdapter;
import com.ascba.rebate.beans.GoodsAttr;
import com.ascba.rebate.view.cart_btn.NumberButton;

import java.util.List;

/**
 * Created by 李平 on 2017/4/8 0008.15:37
 */

public class StdDialog extends Dialog {
    private List<GoodsAttr> gas;
    private NumberButton nb;
    public StdDialog(@NonNull Context context,List<GoodsAttr> gas,int unitPrice,int inventory,int currentNum) {
        super(context);
        this.gas=gas;
        init(context,gas,unitPrice,inventory,currentNum);
    }

    private void init(Context context,List<GoodsAttr> gas,int unitPrice,int inventory,int currentNum) {
        setContentView(R.layout.layout_by_shop);
        //关闭对话框
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView tvUnitPrice = (TextView) findViewById(R.id.tv_shop_price);
        tvUnitPrice.setText("￥"+unitPrice);
        TextView tvInv = (TextView) findViewById(R.id.tv_inventory);
        tvInv.setText("库存"+ inventory);
        TextView tvListener = (TextView) findViewById(R.id.tv_listener);
        tvListener.setText(createSelectStr());
        //规格列表
        RecyclerView rvRule = (RecyclerView) findViewById(R.id.goods_profile_list);
        ProfileAdapter adapter = new ProfileAdapter(R.layout.goods_attrs_layout, gas);
        rvRule.setLayoutManager(new LinearLayoutManager(context));
        //添加尾部试图
        View view1 = LayoutInflater.from(context).inflate(R.layout.num_btn_layout, null);

        nb = (NumberButton) view1.findViewById(R.id.num_btn);
        nb.setCurrentNumber(currentNum);//默认数量为1
        nb.setInventory(inventory);
        nb.getAddButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nb.setCurrentNumber(nb.getNumber()+1);
            }
        });
        nb.getSubButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nb.setCurrentNumber(nb.getNumber()-1);
            }
        });
        adapter.addFooterView(view1, 0);
        rvRule.setAdapter(adapter);

        adapter.setCallback(new ProfileAdapter.Callback() {
            @Override
            public void click(GoodsAttr.Attrs s) {
                
            }
        });
    }
    public void showMyDialog(){
        show();
        Window window = getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.goods_profile_anim);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams wlp = window.getAttributes();
            Display d = window.getWindowManager().getDefaultDisplay();
            wlp.width = d.getWidth();
            wlp.gravity = Gravity.BOTTOM;
            window.setAttributes(wlp);
        }
    }


    private  String createSelectStr() {
        if(gas!=null && gas.size()!=0){
            StringBuilder sb=new StringBuilder();
            for (int i = 0; i < gas.size(); i++) {
                GoodsAttr goodsAttr = gas.get(i);
                sb.append("请选择");
                if(i==gas.size()-1){
                    sb.append(goodsAttr.getTitle());
                }else {
                    sb.append(goodsAttr.getTitle());
                    sb.append(" ");
                }

            }
            return sb.toString();
        }

        return null;
    }
}
