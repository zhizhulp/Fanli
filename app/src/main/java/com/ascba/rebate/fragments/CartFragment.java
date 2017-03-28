package com.ascba.rebate.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.ConfirmOrderActivity;
import com.ascba.rebate.activities.ShopMessageActivity;
import com.ascba.rebate.adapter.CartAdapter;
import com.ascba.rebate.adapter.PayTypeAdapter;
import com.ascba.rebate.adapter.ProfileAdapter;
import com.ascba.rebate.beans.CartGoods;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.beans.GoodsAttr;
import com.ascba.rebate.beans.PayType;
import com.ascba.rebate.fragments.base.BaseFragment;
import com.ascba.rebate.view.ShopABar;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车
 */
public class CartFragment extends BaseFragment implements SuperSwipeRefreshLayout.OnPullRefreshListener, View.OnClickListener {


    private ShopABar sab;
    private SuperSwipeRefreshLayout refreshLayout;
    private RecyclerView rv;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private List<CartGoods> data = new ArrayList<>();
    private CartAdapter adapter;
    public static final String LOG_TAG = "CartFragment";
    private CheckBox cbTotal;
    private TextView tvCost;
    private TextView tvCostNum;
    private RelativeLayout cartClean;

    public CartFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        /**
         * 合计 .结算
         */
        cartClean = (RelativeLayout) view.findViewById(R.id.cart_clear);

        //初始化标题栏
        sab = ((ShopABar) view.findViewById(R.id.sab));
        sab.setImageOtherEnable(false);
        sab.setTitle("购物车(100)");
        sab.setCallback(new ShopABar.Callback() {
            @Override
            public void back(View v) {
                getActivity().finish();
            }

            @Override
            public void clkMsg(View v) {
                ShopMessageActivity.startIntent(getActivity());
            }

            @Override
            public void clkOther(View v) {

            }
        });
        //初始化刷新控件
        refreshLayout = ((SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_layout));
        refreshLayout.setOnPullRefreshListener(this);
        //初始化recyclerView
        rv = ((RecyclerView) view.findViewById(R.id.cart_goods_list));
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        initData();
        cbTotal = ((CheckBox) view.findViewById(R.id.cart_cb_total));
        adapter = new CartAdapter(R.layout.cart_list_item, R.layout.cart_list_title, data, getActivity(), cbTotal);

        /**
         * empty
         */
        View emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.cart_empty_view, null);
        adapter.setEmptyView(emptyView);
        if (data.size() > 0) {
            cartClean.setVisibility(View.VISIBLE);
        } else {
            cartClean.setVisibility(View.GONE);
        }

        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                int id = view.getId();
                if (id == R.id.edit_standard) {
                    showDialog();
                }
            }
        });

        //总计
        tvCost = ((TextView) view.findViewById(R.id.cart_tv_cost_total));
        tvCostNum = ((TextView) view.findViewById(R.id.cart_tv_cost_total_count));
        tvCostNum.setOnClickListener(this);
    }

    /**
     * 规格选择
     */
    private void showDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.AlertDialog);
        dialog.setContentView(R.layout.layout_by_shop);
        //关闭对话框
        dialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //规格列表
        RecyclerView rvRule = (RecyclerView) dialog.findViewById(R.id.goods_profile_list);
        List<GoodsAttr> gas = new ArrayList<>();
        initAttrsData(gas);
        ProfileAdapter adapter = new ProfileAdapter(R.layout.goods_attrs_layout, gas);
        rvRule.setLayoutManager(new LinearLayoutManager(getActivity()));
        //添加尾部试图
        View view1 = getActivity().getLayoutInflater().inflate(R.layout.num_btn_layout, null);
        adapter.addFooterView(view1, 0);
        rvRule.setAdapter(adapter);

        //显示对话框
        dialog.show();
        Window window = dialog.getWindow();
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

    private void initAttrsData(List<GoodsAttr> gas) {
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                List<GoodsAttr.Attrs> strs = new ArrayList<>();
                GoodsAttr ga = new GoodsAttr();
                for (int j = 0; j < 3; j++) {
                    if (j == 2) {
                        strs.add(ga.new Attrs("红色/白色", 2));
                    } else {
                        strs.add(ga.new Attrs("红色/白色", 0));
                    }
                }
                ga.setTitle("颜色分类");
                ga.setStrs(strs);
                gas.add(ga);
            }
            if (i == 1) {
                List<GoodsAttr.Attrs> strs = new ArrayList<>();
                GoodsAttr ga = new GoodsAttr();
                for (int j = 0; j < 15; j++) {
                    if (j == 10) {
                        strs.add(ga.new Attrs((40 + j + 0.5) + "", 2));
                    } else {
                        strs.add(ga.new Attrs((40 + j + 0.5) + "", 0));
                    }

                }
                ga.setTitle("鞋码");
                ga.setStrs(strs);
                gas.add(ga);
            }
            if (i == 2) {
                List<GoodsAttr.Attrs> strs = new ArrayList<>();
                GoodsAttr ga = new GoodsAttr();
                for (int j = 0; j < 3; j++) {
                    strs.add(ga.new Attrs("方形" + i, 0));
                }
                ga.setTitle("其他分类");
                ga.setStrs(strs);
                gas.add(ga);
            }
            if (i == 3) {
                List<GoodsAttr.Attrs> strs = new ArrayList<>();
                GoodsAttr ga = new GoodsAttr();
                for (int j = 0; j < 15; j++) {
                    if (j == 10) {
                        strs.add(ga.new Attrs((40 + j + 0.5) + "", 2));
                    } else {
                        strs.add(ga.new Attrs((40 + j + 0.5) + "", 0));
                    }

                }
                ga.setTitle("鞋码");
                ga.setStrs(strs);
                gas.add(ga);
            }
            if (i == 4) {
                List<GoodsAttr.Attrs> strs = new ArrayList<>();
                GoodsAttr ga = new GoodsAttr();
                for (int j = 0; j < 3; j++) {
                    strs.add(ga.new Attrs("方形" + i, 0));
                }
                ga.setTitle("其他分类");
                ga.setStrs(strs);
                gas.add(ga);
            }
        }
    }

    private void initData() {
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                data.add(new CartGoods(true, "RCC男装" + i, i, false));
                for (int j = 0; j < 3; j++) {
                    Goods goods = new Goods("http://image18-c.poco.cn/mypoco/myphoto/20170301/16/18505011120170301161107098_640.jpg",
                            "RCC男装 春夏 设计师修身尖领翻领免烫薄长衫寸袖 韩国代购1", "颜色:贪色;尺码:S", "￥ 368.00", "2", i);
                    data.add(new CartGoods(goods, i, false));
                }
            } else if (i == 1) {
                data.add(new CartGoods(true, "RCC男装" + i, i, false));
                for (int j = 0; j < 4; j++) {
                    Goods goods = new Goods("http://image18-c.poco.cn/mypoco/myphoto/20170301/16/18505011120170301161107098_640.jpg",
                            "RCC男装 春夏 设计师修身尖领翻领免烫薄长衫寸袖 韩国代购1", "颜色:贪色;尺码:S", "￥ 368.00", "2", i);
                    data.add(new CartGoods(goods, i, false));
                }
            } else if (i == 2) {
                data.add(new CartGoods(true, "RCC男装" + i, i, false));
                for (int j = 0; j < 1; j++) {
                    Goods goods = new Goods("http://image18-c.poco.cn/mypoco/myphoto/20170301/16/18505011120170301161107098_640.jpg",
                            "RCC男装 春夏 设计师修身尖领翻领免烫薄长衫寸袖 韩国代购1", "颜色:贪色;尺码:S", "￥ 368.00", "2", i);
                    data.add(new CartGoods(goods, i, false));
                }
            } else {
                data.add(new CartGoods(true, "RCC男装" + i, i, false));
                for (int j = 0; j < 2; j++) {
                    Goods goods = new Goods("http://image18-c.poco.cn/mypoco/myphoto/20170301/16/18505011120170301161107098_640.jpg",
                            "RCC男装 春夏 设计师修身尖领翻领免烫薄长衫寸袖 韩国代购1", "颜色:贪色;尺码:S", "￥ 368.00", "2", i);
                    data.add(new CartGoods(goods, i, false));
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cart_tv_cost_total_count:
                showFinalDialog();
                break;
        }
    }

    private void showFinalDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.AlertDialog);
        dialog.setContentView(R.layout.layout_pay_pop);
        //关闭对话框
        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //去付款
        dialog.findViewById(R.id.go_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConfirmOrderActivity.class);
                startActivity(intent);
            }
        });
        //列表
        RecyclerView rvTypes = (RecyclerView) dialog.findViewById(R.id.pay_type_list);
        List<PayType> types = new ArrayList<>();
        initPayTypesData(types);
        PayTypeAdapter pt = new PayTypeAdapter(R.layout.pay_type_item, types);
        rvTypes.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTypes.setAdapter(pt);
        //显示对话框
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.goods_profile_anim);
            //window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams wlp = window.getAttributes();
            Display d = window.getWindowManager().getDefaultDisplay();
            wlp.width = d.getWidth();
            wlp.gravity = Gravity.BOTTOM;
            window.setAttributes(wlp);
        }
    }

    private void initPayTypesData(List<PayType> types) {
        types.add(new PayType(false, R.mipmap.pay_left, "账户余额支付", "快捷支付"));
        types.add(new PayType(false, R.mipmap.pay_ali, "支付宝支付", "大额支付，支持银行卡、信用卡"));
        types.add(new PayType(false, R.mipmap.pay_weixin, "微信支付", "大额支付，支持银行卡、信用卡"));
    }
}
