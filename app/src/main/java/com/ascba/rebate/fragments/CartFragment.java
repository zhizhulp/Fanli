package com.ascba.rebate.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.BusinessShopActivity;
import com.ascba.rebate.activities.ConfirmOrderActivity;
import com.ascba.rebate.activities.GoodsDetailsActivity;
import com.ascba.rebate.activities.ShopMessageActivity;
import com.ascba.rebate.activities.shop.ShopActivity;
import com.ascba.rebate.adapter.CartAdapter;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.beans.CartGoods;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.fragments.base.Base2Fragment;
import com.ascba.rebate.fragments.base.LazyFragment;
import com.ascba.rebate.utils.StringUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ShopABar;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yanzhenjie.nohttp.rest.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 购物车
 */
public class CartFragment extends LazyFragment implements
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_cart;
    }

    @Override
    protected void lazyLoad() {
        if (MyApplication.isLoad) {
            requestNetwork(UrlUtils.shoppingCart, 0);
        }
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
                CartGoods cartGoods = data.get(position);
                int id = view.getId();
                if (id == R.id.edit_standard) {//选择规格
                    //showDialog();
                } else if (id == R.id.tv_go_shop) {//点击进店
                    Intent intent = new Intent(getActivity(), BusinessShopActivity.class);
                    intent.putExtra("store_id", cartGoods.getId());
                    startActivity(intent);
                } else if (id == R.id.cart_goods_title) {//点击购物车商品title,进去商品页
                    GoodsDetailsActivity.startIntent(getActivity(), cartGoods.t.getTitleId());
                }
            }
        });

        //总计
        tvCost = ((TextView) view.findViewById(R.id.cart_tv_cost_total));
        tvCostNum = ((TextView) view.findViewById(R.id.cart_tv_cost_total_count));
        tvCostNum.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cart_tv_cost_total_count://点击结算
                if (canClearCart()) {
                    requestNetwork(UrlUtils.cartAccount, 4);
                } else {
                    getDm().buildAlertDialog("请先选择商品");
                }
                break;
        }
    }

    //购物车是否有选择的商品
    private boolean canClearCart() {
        boolean hasCheck = false;
        if (data.size() != 0) {
            for (int i = 0; i < data.size(); i++) {
                CartGoods cartGoods = data.get(i);
                if (!cartGoods.isHeader) {
                    if (cartGoods.isCheck()) {
                        hasCheck = true;
                        break;
                    }
                }
            }
        }

        return hasCheck;
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
                        a.selFrgByPos(ShopActivity.HOMEPAGE);
                        a.getShopTabs().statusChaByPosition(0, 2);
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
                        if (!StringUtils.isEmpty(goods_id)) {
                            goods.setTitleId(Integer.parseInt(goods_id));
                        }

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
    }


    @Override
    public void handle404(String message) {
        getDm().buildAlertDialog(message);
    }

    @Override
    public void handleReLogin() {
    }

    @Override
    public void handleNoNetWork() {
        getDm().buildAlertDialog(getString(R.string.no_network));
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

}
