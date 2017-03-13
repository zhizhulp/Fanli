package com.ascba.rebate.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.CartGoods;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.fragments.CartFragment;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.view.cart_btn.NumberButton;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车列表实体类
 */

public class CartAdapter extends BaseSectionQuickAdapter<CartGoods, BaseViewHolder> {
    private CheckBox cbTotal;
    private Context context;
    private Callback callback;
    private List<CartGoods> data;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void titleCheck(CompoundButton buttonView, boolean isChecked, CartGoods item);

        void childCheck(CompoundButton buttonView, boolean isChecked, CartGoods item);
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public CartAdapter(int layoutResId, int sectionHeadResId, final List<CartGoods> data, Context context, CheckBox cb) {
        super(layoutResId, sectionHeadResId, data);
        this.context = context;
        this.data = data;
        this.cbTotal=cb;
        //全局全选
        cbTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.size()==0){
                    return;
                }
                for (int i = 0; i < data.size(); i++) {
                    CartGoods cg = data.get(i);
                    cg.setCheck(cbTotal.isChecked());
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    protected void convertHead(BaseViewHolder helper, final CartGoods item) {
        helper.setText(R.id.cart_cb_title, item.header);
        CheckBox cb = helper.getView(R.id.cart_cb_title);
        cb.setOnCheckedChangeListener(null);
        cb.setChecked(item.isCheck());
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (callback != null) {
                    callback.titleCheck(buttonView, isChecked, item);
                    item.setCheck(isChecked);
                    //监听子view
                    for (int i = 0; i < data.size(); i++) {
                        CartGoods cg = data.get(i);
                        if (cg.getId() == item.getId() && !cg.isHeader) {
                            cg.setCheck(isChecked);
                        }

                    }
                    List<CartGoods> gl=new ArrayList<>();
                    //监听总的checkBox
                    for (int i = 0; i < data.size(); i++) {
                        if(data.get(i).isHeader){
                            gl.add(data.get(i));
                        }
                    }
                    boolean isAll=false;
                    for (int i = 0; i <gl.size() ; i++) {
                        if(i==gl.size()-1){
                            break;
                        }
                        if(gl.get(i).isCheck()==gl.get(i+1).isCheck()){
                            isAll=true;
                        }else {
                            isAll=false;
                            break;
                        }
                    }
                    if(isAll){
                        if(isChecked && !cbTotal.isChecked()){
                            cbTotal.setChecked(true);
                        }else if(!isChecked && cbTotal.isChecked()){
                            cbTotal.setChecked(false);
                        }
                    }else {
                        if(!isChecked && cbTotal.isChecked()){
                            cbTotal.setChecked(false);
                        }
                    }


                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void convert(BaseViewHolder helper, final CartGoods item) {
        ImageView view = helper.getView(R.id.cart_goods_pic);
        Goods goods = item.t;
        Glide.with(context).load(goods.getImgUrl()).into(view);
        helper.setText(R.id.cart_goods_title, goods.getGoodsTitle());
        helper.setText(R.id.cart_goods_standard, goods.getGoodsStandard());
        helper.setText(R.id.cart_price, goods.getGoodsPrice());
        NumberButton nb = helper.getView(R.id.number_button);
        nb.setBuyMax(5)
                .setInventory(6)
                .setCurrentNumber(10)
                .setOnWarnListener(new NumberButton.OnWarnListener() {
                    @Override
                    public void onWarningForInventory(int inventory) {
                        Toast.makeText(context, "当前库存:" + inventory, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onWarningForBuyMax(int buyMax) {
                        Toast.makeText(context, "超过最大购买数:" + buyMax, Toast.LENGTH_SHORT).show();
                    }
                });

        final CheckBox cb = helper.getView(R.id.cb_cart_child);
        cb.setOnCheckedChangeListener(null);
        cb.setChecked(item.isCheck());
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (callback != null) {
                    callback.childCheck(buttonView, isChecked, item);
                    item.setCheck(isChecked);
                    List<CartGoods> gL = new ArrayList<>();
                    CartGoods head = null;
                    for (int i = 0; i < data.size(); i++) {
                        CartGoods cg = data.get(i);
                        if (cg.getId() == item.getId()) {
                            if (cg.isHeader) {
                                head = cg;
                            } else {
                                gL.add(cg);
                            }
                        }
                    }
                    if (gL.size() != 0) {
                        boolean isAll = true;
                        for (int i = 0; i < gL.size(); i++) {
                            if (i == gL.size() - 1) {
                                break;
                            }
                            if (gL.get(i).isCheck() == gL.get(i + 1).isCheck()) {
                                isAll = true;
                            } else {
                                isAll = false;
                                break;
                            }
                        }
                        LogUtils.PrintLog(CartFragment.LOG_TAG, "IS aLL?  " + isAll);
                        if (isAll) {
                            if (head != null) {
                                if (isChecked && !head.isCheck()) {
                                    head.setCheck(true);
                                } else if (!isChecked && head.isCheck()) {
                                    head.setCheck(false);
                                }

                            }
                        } else {
                            if (head != null) {
                                if (head.isCheck()) {
                                    head.setCheck(false);
                                }
                            }
                        }
                    }
                    //监听总的checkBox
                    List<CartGoods> gl=new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        if(!data.get(i).isHeader){
                            gl.add(data.get(i));
                        }
                    }
                    boolean isAll=false;
                    for (int i = 0; i <gl.size() ; i++) {
                        if(i==gl.size()-1){
                            break;
                        }
                        if(gl.get(i).isCheck()==gl.get(i+1).isCheck()){
                            isAll=true;
                        }else {
                            isAll=false;
                            break;
                        }
                    }
                    if(isAll){
                        if(isChecked && !cbTotal.isChecked()){
                            cbTotal.setChecked(true);
                        }else if(!isChecked && cbTotal.isChecked()){
                            cbTotal.setChecked(false);
                        }
                    }else {
                        if(cbTotal.isChecked()){
                            cbTotal.setChecked(false);
                        }
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

}
