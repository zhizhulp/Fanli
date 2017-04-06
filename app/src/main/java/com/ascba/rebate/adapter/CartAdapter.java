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
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车列表实体类
 */

public class CartAdapter extends BaseSectionQuickAdapter<CartGoods, BaseViewHolder> {
    private CheckBox cbTotal;
    private Context context;
    private List<CartGoods> data;
    private Handler handler = new Handler();
    private CallBack callBack;

    public interface CallBack {
        void onClickedChild(boolean isChecked,int position);
        void onClickedParent(boolean isChecked,int position);
        void onClickedTotal(boolean isChecked);
        void clickAddBtn(int count,int position);
        void clickSubBtn(int count,int position);
        void clickDelBtn(int position);
    }



    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
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
        this.cbTotal = cb;
        //全局全选
        cbTotal.setOnClickListener(createTotalListener());
    }

    @Override
    protected void convertHead(final BaseViewHolder helper, final CartGoods item) {
        helper.setText(R.id.cart_cb_title, item.header);
        final CheckBox cb = helper.getView(R.id.cart_cb_title);
        cb.setChecked(item.isCheck());
        cb.setOnClickListener(createHeadListener(cb,helper,item));
    }



    @Override
    protected void convert(final BaseViewHolder helper, final CartGoods item) {
        ImageView view = helper.getView(R.id.cart_goods_pic);
        Goods goods = item.t;
        Picasso.with(context).load(goods.getImgUrl()).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).into(view);
        helper.setText(R.id.cart_goods_title, goods.getGoodsTitle());
        helper.setText(R.id.cart_goods_standard, goods.getGoodsStandard());
        helper.setText(R.id.cart_price, goods.getGoodsPrice());
        helper.addOnClickListener(R.id.edit_standard);
        initNumberButton(helper,goods);
        final CheckBox cb = helper.getView(R.id.cb_cart_child);
        cb.setChecked(item.isCheck());
        cb.setOnClickListener(createItemListener(cb,helper,item));

        helper.setOnClickListener(R.id.btnDelete,createDelListener(helper));
    }

    private View.OnClickListener createDelListener(final BaseViewHolder helper) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != callBack){
                    callBack.clickDelBtn(helper.getAdapterPosition());
                }
            }
        };
    }

    private void initNumberButton(final BaseViewHolder helper, Goods goods) {
        final NumberButton nb = helper.getView(R.id.number_button);
        nb.setBuyMax(200000)
                .setInventory(2000000)
                .setCurrentNumber(goods.getUserQuy())
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
        nb.getAddButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = nb.getNumber();
                if(callBack!=null){
                    callBack.clickAddBtn(number,helper.getAdapterPosition());
                }
            }
        });
        nb.getSubButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = nb.getNumber();
                if(callBack!=null){
                    callBack.clickSubBtn(number,helper.getAdapterPosition());
                }
            }
        });

    }

    private View.OnClickListener createHeadListener(final CheckBox cb,final BaseViewHolder helper, final CartGoods item) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setCheck(cb.isChecked());
                //监听子view
                for (int i = 0; i < data.size(); i++) {
                    CartGoods cg = data.get(i);
                    if (cg.getId() == item.getId() && !cg.isHeader) {
                        cg.setCheck(cb.isChecked());
                    }

                }
                List<CartGoods> gl = new ArrayList<>();
                //监听总的checkBox
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).isHeader) {
                        gl.add(data.get(i));
                    }
                }
                boolean isAll = false;
                for (int i = 0; i < gl.size(); i++) {
                    if (i == gl.size() - 1) {
                        isAll = true;
                        break;
                    }
                    if (gl.get(i).isCheck() == gl.get(i + 1).isCheck()) {
                        isAll = true;
                    } else {
                        isAll = false;
                        break;
                    }
                }
                if (isAll) {
                    if (cb.isChecked() && !cbTotal.isChecked()) {
                        cbTotal.setChecked(true);
                    } else if (!cb.isChecked() && cbTotal.isChecked()) {
                        cbTotal.setChecked(false);
                    }
                } else {
                    if (!cb.isChecked() && cbTotal.isChecked()) {
                        cbTotal.setChecked(false);
                    }
                }


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
                if(callBack!=null){
                    callBack.onClickedParent(cb.isChecked(),helper.getAdapterPosition());
                }
            }
        };
    }
    private View.OnClickListener createItemListener(final CheckBox cb, final BaseViewHolder helper, final CartGoods item) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox v1 = (CheckBox) v;
                item.setCheck(v1.isChecked());
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
                    if (isAll) {
                        if (head != null) {
                            if (cb.isChecked() && !head.isCheck()) {
                                head.setCheck(true);
                            } else if (!cb.isChecked() && head.isCheck()) {
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
                List<CartGoods> gl = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    if (!data.get(i).isHeader) {
                        gl.add(data.get(i));
                    }
                }
                boolean isAll = false;
                for (int i = 0; i < gl.size(); i++) {
                    if (i == gl.size() - 1) {
                        break;
                    }
                    if (gl.get(i).isCheck() == gl.get(i + 1).isCheck()) {
                        isAll = true;
                    } else {
                        isAll = false;
                        break;
                    }
                }
                if (isAll) {
                    if (cb.isChecked() && !cbTotal.isChecked()) {
                        cbTotal.setChecked(true);
                    } else if (!cb.isChecked() && cbTotal.isChecked()) {
                        cbTotal.setChecked(false);
                    }
                } else {
                    if (cbTotal.isChecked()) {
                        cbTotal.setChecked(false);
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
                if (callBack != null) {
                    callBack.onClickedChild(v1.isChecked(),helper.getAdapterPosition());
                }
            }
        };
    }

    private View.OnClickListener createTotalListener() {
        return  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.size() == 0) {
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
                if(callBack!=null){
                    callBack.onClickedTotal(cbTotal.isChecked());
                }
            }
        };
    }



}
