package com.ascba.rebate.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
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
import com.ascba.rebate.activities.shop.ShopActivity;
import com.ascba.rebate.adapter.CartAdapter;
import com.ascba.rebate.adapter.PayTypeAdapter;
import com.ascba.rebate.adapter.ProfileAdapter;
import com.ascba.rebate.beans.CartGoods;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.beans.GoodsAttr;
import com.ascba.rebate.beans.PayType;
import com.ascba.rebate.fragments.base.Base2Fragment;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.StringUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ShopABar;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 购物车
 */
public class CartFragment extends Base2Fragment implements SuperSwipeRefreshLayout.OnPullRefreshListener,
        View.OnClickListener, Base2Fragment.Callback, CartAdapter.CallBack {


    private ShopABar sab;
    private SuperSwipeRefreshLayout refreshLayout;
    private RecyclerView rv;
    private List<CartGoods> data = new ArrayList<>();
    private CartAdapter adapter;
    public static final String LOG_TAG = "CartFragment";
    private CheckBox cbTotal;
    private TextView tvCost;
    private TextView tvCostNum;
    private RelativeLayout cartClean;
    private int finalScene;
    private CartGoods cgSelect;//被选中的
    private int goodsCount;//当前商品数量
    private int position;//当前点击位置

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
        requestNetwork(UrlUtils.shoppingCart, 0);
    }

    private void requestNetwork(String url, int scene) {
        finalScene = scene;
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        if (scene == 1) {
            request.add("cart_ids", createIds());
            request.add("status", (cgSelect != null) ? (cgSelect.isCheck() ? 1 : 0) : (cbTotal.isChecked() ? 1 : 0));
        } else if (scene == 2) {
            request.add("cart_id", data.get(position).t.getCartId());
            request.add("new_num", goodsCount);
        } else if (scene == 3) {
            request.add("cart_ids", data.get(position).t.getCartId());
        } else if (scene == 4) {
            request.add("cart_ids", createClearIds());
        }
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    private String createClearIds() {

        if (data.size() != 0) {
            List<CartGoods> filter = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.size(); i++) {
                CartGoods cg = data.get(i);
                if (!cg.isHeader && cg.isCheck()) {
                    filter.add(cg);
                }
            }

            for (int i = 0; i < filter.size(); i++) {
                CartGoods cg = filter.get(i);
                if (i == filter.size() - 1) {
                    sb.append(cg.t.getCartId());
                } else {
                    sb.append(cg.t.getCartId());
                    sb.append(",");
                }
            }
            return sb.toString();
        } else {
            return null;
        }

    }

    private String createIds() {
        if (cgSelect == null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.size(); i++) {
                CartGoods cg = data.get(i);
                if (!cg.isHeader) {
                    if (i == data.size() - 1) {
                        sb.append(cg.t.getCartId());
                    } else {
                        sb.append(cg.t.getCartId());
                        sb.append(",");
                    }
                }
            }
            return sb.toString();
        } else {
            if (!cgSelect.isHeader) {
                return cgSelect.t.getCartId();
            } else {
                StringBuilder sb = new StringBuilder();
                List<CartGoods> filter = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    CartGoods cg = data.get(i);
                    if (!cg.isHeader) {
                        if (cg.getId() == (cgSelect.getId())) {
                            filter.add(cg);
                        }

                    }

                }
                for (int i = 0; i < filter.size(); i++) {
                    CartGoods cg = filter.get(i);
                    if (i == filter.size() - 1) {
                        sb.append(cg.t.getCartId());
                    } else {
                        sb.append(cg.t.getCartId());
                        sb.append(",");
                    }
                }

                return sb.toString();
            }

        }
    }

    private void initViews(View view) {
        /**
         * 合计 .结算
         */
        cartClean = (RelativeLayout) view.findViewById(R.id.cart_clear);

        //初始化标题栏
        sab = ((ShopABar) view.findViewById(R.id.sab));
        sab.setImageOtherEnable(false);
        sab.setTitle("购物车");
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

        cbTotal = ((CheckBox) view.findViewById(R.id.cart_cb_total));


        rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(final BaseQuickAdapter adapter, View view, int position) {
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
                        strs.add(ga.new Attrs(1,"红色/白色", 2, false));
                    } else {
                        strs.add(ga.new Attrs(1,"红色/白色", 0, false));
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
                        strs.add(ga.new Attrs(1,(40 + j + 0.5) + "", 2, false));
                    } else {
                        strs.add(ga.new Attrs(1,(40 + j + 0.5) + "", 0, false));
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
                    strs.add(ga.new Attrs(1,"方形" + i, 0, false));
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
                        strs.add(ga.new Attrs(1,(40 + j + 0.5) + "", 2, false));
                    } else {
                        strs.add(ga.new Attrs(1,(40 + j + 0.5) + "", 0, false));
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
                    strs.add(ga.new Attrs(1,"方形" + i, 0, false));
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
                            "RCC男装 春夏 设计师修身尖领翻领免烫薄长衫寸袖 韩国代购1", "颜色:贪色;尺码:S", "￥ 368.00", 2, i);
                    data.add(new CartGoods(goods, i, false));
                }
            } else if (i == 1) {
                data.add(new CartGoods(true, "RCC男装" + i, i, false));
                for (int j = 0; j < 4; j++) {
                    Goods goods = new Goods("http://image18-c.poco.cn/mypoco/myphoto/20170301/16/18505011120170301161107098_640.jpg",
                            "RCC男装 春夏 设计师修身尖领翻领免烫薄长衫寸袖 韩国代购1", "颜色:贪色;尺码:S", "￥ 368.00", 2, i);
                    data.add(new CartGoods(goods, i, false));
                }
            } else if (i == 2) {
                data.add(new CartGoods(true, "RCC男装" + i, i, false));
                for (int j = 0; j < 1; j++) {
                    Goods goods = new Goods("http://image18-c.poco.cn/mypoco/myphoto/20170301/16/18505011120170301161107098_640.jpg",
                            "RCC男装 春夏 设计师修身尖领翻领免烫薄长衫寸袖 韩国代购1", "颜色:贪色;尺码:S", "￥ 368.00", 2, i);
                    data.add(new CartGoods(goods, i, false));
                }
            } else {
                data.add(new CartGoods(true, "RCC男装" + i, i, false));
                for (int j = 0; j < 2; j++) {
                    Goods goods = new Goods("http://image18-c.poco.cn/mypoco/myphoto/20170301/16/18505011120170301161107098_640.jpg",
                            "RCC男装 春夏 设计师修身尖领翻领免烫薄长衫寸袖 韩国代购1", "颜色:贪色;尺码:S", "￥ 368.00", 2, i);
                    data.add(new CartGoods(goods, i, false));
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        requestNetwork(UrlUtils.shoppingCart, 0);
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
                requestNetwork(UrlUtils.cartAccount, 4);

                //showFinalDialog();
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

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if (finalScene == 0) {//购物车数据

            getData(dataObj);
            if (adapter == null) {
                adapter = new CartAdapter(R.layout.cart_list_item, R.layout.cart_list_title, data, getActivity(), cbTotal);
                /**
                 * 购物车是空的，去逛逛吧
                 */
                View emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.cart_empty_view, null);
                TextView goShop = (TextView) emptyView.findViewById(R.id.tx_go_shop);
                goShop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShopActivity a = (ShopActivity) getActivity();
                        a.selFrgByPos(0,a.getmFirstFragment());
                        a.getShopTabs().statusChaByPosition(0,2);
                        a.getShopTabs().setFilPos(0);
                    }
                });
                adapter.setEmptyView(emptyView);
                rv.setAdapter(adapter);
                adapter.setCallBack(this);
            } else {
                adapter.notifyDataSetChanged();
            }
            calculateNumAndCost();
        } else if (finalScene == 1) {//选择商品
            calculateNumAndCost();
            //getDm().buildAlertDialog(message);
        } else if (finalScene == 2) {//加减商品
            //getDm().buildAlertDialog(message);
            data.get(position).t.setUserQuy(goodsCount);
            adapter.notifyItemChanged(position);
            calculateNumAndCost();
        } else if (finalScene == 3) {//删除商品
            //getDm().buildAlertDialog(message);
            data.remove(position);
            adapter.notifyItemRemoved(position);
            calculateNumAndCost();
            requestNetwork(UrlUtils.shoppingCart, 0);
        } else if (finalScene == 4) {//商品结算
            Intent intent = new Intent(getActivity(), ConfirmOrderActivity.class);
            intent.putExtra("json_data", dataObj.toString());
            startActivity(intent);
        }


    }

    private void getData(JSONObject dataObj) {
        JSONArray array = dataObj.optJSONArray("shoppingCar");
        stopRefresh();
        if (data.size() != 0) {
            data.clear();
        }

        if (array != null && array.length() != 0) {
            cartClean.setVisibility(View.VISIBLE);
            boolean isAll = true;
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);

                JSONObject storeObj = obj.optJSONObject("store_info");
                String store_name = (String) storeObj.opt("store_name");
                String store_id = String.valueOf(storeObj.opt("store_id"));//商品id 用于判断是否是一组

                CartGoods cgTitle = new CartGoods(true, store_name, Integer.parseInt(store_id), false);
                data.add(cgTitle);

                JSONArray gl = obj.optJSONArray("goods_list");
                if (gl != null && gl.length() != 0) {
                    boolean isChild = true;
                    for (int j = 0; j < gl.length(); j++) {
                        JSONObject goodsOBj = gl.optJSONObject(j);
                        String goods_id = (String) goodsOBj.opt("goods_id");//商品id
                        String goods_number = (String) goodsOBj.opt("goods_number");//商品编号
                        String goods_name = (String) goodsOBj.opt("goods_name");//商品名称
                        String goods_price = (String) goodsOBj.opt("goods_price");//商品价格
                        String goods_num = (String) goodsOBj.opt("goods_num");//商品数量
                        String goods_img = UrlUtils.baseWebsite + goodsOBj.optString("goods_img");//商品图片
                        String spec_names = (String) goodsOBj.opt("spec_names");//商品规格
                        String selected = (String) goodsOBj.opt("selected");//商品是否被选中
                        String cart_id = String.valueOf(goodsOBj.opt("cart_id"));//
                        Goods goods = new Goods();
                        goods.setGoodsNumber(goods_number);
                        goods.setGoodsTitle(goods_name);
                        goods.setGoodsPrice(goods_price);
                        goods.setUserQuy(Integer.parseInt(goods_num));
                        goods.setImgUrl(goods_img);
                        goods.setGoodsStandard(spec_names);
                        goods.setCartId(cart_id);
                        int sele = Integer.parseInt(selected);
                        CartGoods dg = new CartGoods(goods, Integer.parseInt(store_id), sele != 0);
                        data.add(dg);
                        if (sele == 0) {//未选择
                            isChild = false;
                        }
                    }
                    cgTitle.setCheck(isChild);
                }
                if (!cgTitle.isCheck()) {
                    isAll = false;
                }
            }
            cbTotal.setChecked(isAll);
        } else {
            cartClean.setVisibility(View.GONE);
        }
    }

    @Override
    public void handleReqFailed() {
        stopRefresh();

    }


    @Override
    public void handle404(String message) {
        getDm().buildAlertDialog(message);
    }

    @Override
    public void handleReLogin() {
        stopRefresh();
    }

    @Override
    public void handleNoNetWork() {
        stopRefresh();
        getDm().buildAlertDialog(getString(R.string.no_network));
    }

    private void stopRefresh() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onClickedChild(boolean isChecked, int position) {
        cgSelect = data.get(position);
        requestNetwork(UrlUtils.cartSelectdGoods, 1);

    }

    @Override
    public void onClickedParent(boolean isChecked, int position) {
        cgSelect = data.get(position);
        requestNetwork(UrlUtils.cartSelectdGoods, 1);

    }

    @Override
    public void onClickedTotal(boolean isChecked) {
        cgSelect = null;
        requestNetwork(UrlUtils.cartSelectdGoods, 1);
    }

    @Override
    public void clickAddBtn(int count, int position) {
        goodsCount = count + 1;
        this.position = position;
        requestNetwork(UrlUtils.cartChangenumGoods, 2);
    }

    @Override
    public void clickSubBtn(int count, int position) {
        goodsCount = count - 1;
        this.position = position;
        requestNetwork(UrlUtils.cartChangenumGoods, 2);
    }

    @Override
    public void clickDelBtn(int position) {
        this.position = position;
        requestNetwork(UrlUtils.cartDeleteGoods, 3);
    }

    private void calculateNumAndCost() {
        if (data.size() != 0) {
            double totalCost = 0;
            int totalCount = 0;
            for (int i = 0; i < data.size(); i++) {
                CartGoods cg = data.get(i);
                if (!cg.isHeader && cg.isCheck()) {
                    String goodsPrice = cg.t.getGoodsPrice();
                    if (!StringUtils.isEmpty(goodsPrice)) {
                        double price = Double.parseDouble(goodsPrice);
                        int userQuy = cg.t.getUserQuy();
                        if (userQuy != 0) {
                            totalCost += userQuy * price;
                            totalCount += userQuy;
                        }
                    }

                }
            }
            tvCost.setText("￥" + totalCost);
            tvCostNum.setText("结算(" + totalCount + ")");
        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            FragmentActivity activity = getActivity();
            if (activity instanceof ShopActivity) {
                ShopActivity a = (ShopActivity) activity;
                a.selFrgByPos(0, a.getmFirstFragment());
                a.getShopTabs().statusChaByPosition(0,2);
                a.getShopTabs().setFilPos(0);
            }
        } else {
            if (resultCode != Activity.RESULT_OK) {
                FragmentActivity activity = getActivity();
                if (activity instanceof ShopActivity) {
                    ShopActivity a = (ShopActivity) activity;
                    a.selFrgByPos(0, a.getmFirstFragment());
                    a.getShopTabs().statusChaByPosition(0,2);
                    a.getShopTabs().setFilPos(0);
                }
            }
        }
    }
}
