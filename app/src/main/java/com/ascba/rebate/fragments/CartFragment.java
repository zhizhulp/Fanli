package com.ascba.rebate.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
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
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.CartAdapter;
import com.ascba.rebate.adapter.ProfileAdapter;
import com.ascba.rebate.beans.CartGoods;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.beans.GoodsAttr;
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
public class CartFragment extends BaseFragment implements SuperSwipeRefreshLayout.OnPullRefreshListener {


    private ShopABar sab;
    private SuperSwipeRefreshLayout refreshLayout;
    private RecyclerView rv;
    private Handler handler=new Handler(){
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
        //初始化标题栏
        sab = ((ShopABar) view.findViewById(R.id.sab));
        sab.setBackEnable(false);
        sab.setImageOtherEnable(false);
        sab.setTitle("购物车(100)");
        //初始化刷新控件
        refreshLayout = ((SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_layout));
        refreshLayout.setOnPullRefreshListener(this);
        //初始化recyclerView
        rv = ((RecyclerView) view.findViewById(R.id.cart_goods_list));
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        initData();
        cbTotal = ((CheckBox) view.findViewById(R.id.cart_cb_total));
        adapter = new CartAdapter(R.layout.cart_list_item, R.layout.cart_list_title, data, getActivity(),cbTotal);
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                int id = view.getId();
                if(id==R.id.edit_standard){
                    showDialog();
                }
            }
        });
        //总计
        tvCost = ((TextView) view.findViewById(R.id.cart_tv_cost_total));
        tvCostNum = ((TextView) view.findViewById(R.id.cart_tv_cost_total_count));
    }

    private void showDialog() {
        final Dialog dialog=new Dialog(getActivity(),R.style.AlertDialog);
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
        List<GoodsAttr> gas=new ArrayList<>();
        initAttrsData(gas);
        ProfileAdapter adapter=new ProfileAdapter(R.layout.goods_attrs_layout,gas);
        rvRule.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRule.setAdapter(adapter);

        //显示对话框
        dialog.show();
        Window window = dialog.getWindow();
        if(window!=null){
            window.setWindowAnimations(R.style.goods_profile_anim);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams wlp = window.getAttributes();
            Display d = window.getWindowManager().getDefaultDisplay();
            wlp.width = d.getWidth();
            wlp.gravity=Gravity.BOTTOM;
            window.setAttributes(wlp);
        }
    }

    private void initAttrsData(List<GoodsAttr> gas) {
        for (int i = 0; i < 3; i++) {
            if(i==0){
                List<String> strs=new ArrayList<>();
                for (int j = 0; j < 3; j++) {
                    strs.add("白色/红色");
                }
                GoodsAttr ga=new GoodsAttr("颜色分类",strs);
                gas.add(ga);
            }
            if(i==1){
                List<String> strs=new ArrayList<>();
                for (int j = 0; j < 20; j++) {
                    strs.add(40+j+"");
                }
                GoodsAttr ga=new GoodsAttr("鞋码",strs);
                gas.add(ga);
            }
            if(i==2){
                List<String> strs=new ArrayList<>();
                for (int j = 0; j < 4; j++) {
                    strs.add("方形"+i);
                }
                GoodsAttr ga=new GoodsAttr("其他",strs);
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
}
