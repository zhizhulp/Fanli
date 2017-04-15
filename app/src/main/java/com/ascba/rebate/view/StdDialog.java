package com.ascba.rebate.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
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
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.beans.GoodsAttr;
import com.ascba.rebate.view.cart_btn.NumberButton;

import java.util.List;

/**
 * Created by 李平 on 2017/4/8 0008.15:37
 */

public class StdDialog extends Dialog {
    private List<GoodsAttr> gas;
    private List<Goods> goodses;
    private NumberButton nb;
    private TextView tvUnitPrice;//单价
    private TextView tvInv;//库存
    private TextView tvListener;//动态标题
    private Listener listener;
    private TextView tvAddToCart;
    private TextView tvPurchase;

    public NumberButton getNb() {
        return nb;
    }

    public void setNb(NumberButton nb) {
        this.nb = nb;
    }

    public TextView getTvPurchase() {
        return tvPurchase;
    }

    public void setTvPurchase(TextView tvPurchase) {
        this.tvPurchase = tvPurchase;
    }

    public TextView getTvAddToCart() {
        return tvAddToCart;
    }

    public void setTvAddToCart(TextView tvAddToCart) {
        this.tvAddToCart = tvAddToCart;
    }

    public interface Listener {
        void getSelectGoods(Goods gs);

        void isSelectAll(boolean isAll);
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public StdDialog(@NonNull Context context, List<GoodsAttr> gas, List<Goods> goodses) {
        super(context);
        this.gas = gas;
        this.goodses = goodses;
        init(context, gas);
    }

    private void init(Context context, final List<GoodsAttr> gas) {
        setContentView(R.layout.layout_by_shop);
        //关闭对话框
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvAddToCart = ((TextView) findViewById(R.id.tv_add_to_cart));
        tvUnitPrice = (TextView) findViewById(R.id.tv_shop_price);
        tvPurchase = ((TextView) findViewById(R.id.tv_purchase_cart));
        tvUnitPrice.setText("￥ ?");
        tvInv = (TextView) findViewById(R.id.tv_inventory);
        tvInv.setText("库存 ?");
        tvListener = (TextView) findViewById(R.id.tv_listener);
        tvListener.setText("请选择完整的规格");
        //规格列表
        RecyclerView rvRule = (RecyclerView) findViewById(R.id.goods_profile_list);
        ProfileAdapter adapter = new ProfileAdapter(R.layout.goods_attrs_layout, gas);
        rvRule.setLayoutManager(new LinearLayoutManager(context));
        //添加尾部试图
        View view1 = LayoutInflater.from(context).inflate(R.layout.num_btn_layout, null);

        nb = (NumberButton) view1.findViewById(R.id.num_btn);
        nb.setCurrentNumber(1);//默认数量为1
        nb.getAddButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nb.setCurrentNumber(nb.getNumber() + 1);
            }
        });
        nb.getSubButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nb.setCurrentNumber(nb.getNumber() - 1);
            }
        });
        adapter.addFooterView(view1, 0);
        rvRule.setAdapter(adapter);

        adapter.setCallback(new ProfileAdapter.Callback() {
            @Override
            public void click(GoodsAttr.Attrs s, GoodsAttr item) {
                boolean isAllSelect = true;
                for (int i = 0; i < gas.size(); i++) {
                    GoodsAttr goodsAttr = gas.get(i);
                    if (!goodsAttr.isSelect()) {
                        isAllSelect = false;
                        break;
                    }
                }
                if (listener != null) {
                    listener.isSelectAll(isAllSelect);
                }
                if (isAllSelect) {//所有选择完毕
                    setTitleText();
                }

            }
        });
        //去除Holo主题的蓝色线条
        try {
            int dividerID = context.getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = findViewById(dividerID);
            divider.setBackgroundColor(Color.TRANSPARENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMyDialog() {
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


    private void setTitleText() {
        if (gas != null && gas.size() != 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < gas.size(); i++) {
                GoodsAttr goodsAttr = gas.get(i);
                List<GoodsAttr.Attrs> strs = goodsAttr.getStrs();
                for (int j = 0; j < strs.size(); j++) {
                    GoodsAttr.Attrs attrs = strs.get(j);
                    if (attrs.getTextStatus()==1) {
                        if (i == gas.size() - 1) {
                            sb.append(attrs.getItemId());
                        } else {
                            sb.append(attrs.getItemId());
                            sb.append("_");
                        }
                    }
                }
            }
            for (int i = 0; i < goodses.size(); i++) {
                Goods goods = goodses.get(i);
                if (sb.toString().equals(goods.getSpecKeys())) {
                    nb.setInventory(goods.getInventory());
                    tvInv.setText("库存" + goods.getInventory());
                    tvUnitPrice.setText("￥" + goods.getGoodsPrice());
                    tvListener.setText(goods.getSpecNames());
                    if (listener != null) {
                        listener.getSelectGoods(goods);
                    }
                }
            }
        }
    }
}
