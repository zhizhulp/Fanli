package com.ascba.rebate.activities.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import com.ascba.rebate.activities.base.BaseNetActivity1;
import com.ascba.rebate.adapter.CartAdapter;
import com.ascba.rebate.beans.CartGoods;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.utils.StringUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.BetterRecyclerView;
import com.ascba.rebate.view.ShopABar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShopCartActivity extends BaseNetActivity1 implements
        View.OnClickListener, CartAdapter.CallBack  {
    private ShopABar sab;
    private BetterRecyclerView rv;
    private List<CartGoods> data = new ArrayList<>();
    private CartAdapter adapter;
    private CheckBox cbTotal;
    private TextView tvCost;
    private TextView tvCostNum;
    private RelativeLayout cartClean;
    private int goodsCount;//当前商品数量
    private int position;//当前点击位置
    private int allGoodsNum;//购物车所有商品数量
    private boolean isAdd;//当前在点加号还是减号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cart);
        initViews();
        requestNetwork(UrlUtils.shoppingCart, 0);
    }
    private void requestNetwork(String url, int scene) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        if (scene == 1) {//选商品
            request.add("cart_ids", createIds());
        } else if (scene == 2) {//加减商品
            request.add("cart_id", data.get(position).t.getCartId());
            request.add("new_num", goodsCount);
        } else if (scene == 3) {//删除商品
            request.add("cart_ids", data.get(position).t.getCartId());
        } else if (scene == 4) {//购物车结算
            //request.add("cart_ids", createClearIds());
        }
        executeNetWork(scene,request, "请稍后");
    }

    private void initViews() {
        /**
         * 合计 .结算
         */
        cartClean = (RelativeLayout) findViewById(R.id.cart_clear);

        //初始化标题栏
        sab = ((ShopABar) findViewById(R.id.sab));
        sab.setImageOtherEnable(false);
        sab.setTitle("购物车");
        sab.setCallback(new ShopABar.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkMsg(View v) {
                ShopMessageActivity.startIntent(ShopCartActivity.this);
            }

            @Override
            public void clkOther(View v) {

            }
        });
        //初始化recyclerView
        rv = ((BetterRecyclerView) findViewById(R.id.cart_goods_list));
        rv.setLayoutManager(new LinearLayoutManager(this));

        cbTotal = ((CheckBox) findViewById(R.id.cart_cb_total));
        initRefreshLayout();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestNetwork(UrlUtils.shoppingCart, 0);
            }
        });
        rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(final BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                CartGoods cartGoods = data.get(position);
                int id = view.getId();
                if (id == R.id.edit_standard) {//选择规格,进商品页
                    //GoodsDetailsActivity.startIntent(getActivity(), cartGoods.t.getTitleId());
                } else if (id == R.id.tv_go_shop) {//点击进店
                    Intent intent = new Intent(ShopCartActivity.this, BusinessShopActivity.class);
                    intent.putExtra("store_id", cartGoods.getId());
                    startActivity(intent);
                } else if (id == R.id.cart_goods_title) {//点击购物车商品title,进去商品页
                    GoodsDetailsActivity.startIntent(ShopCartActivity.this, cartGoods.t.getTitleId());
                } else if(id == R.id.cart_goods_pic){
                    GoodsDetailsActivity.startIntent(ShopCartActivity.this, cartGoods.t.getTitleId());
                }
            }
        });

        //总计
        tvCost = ((TextView) findViewById(R.id.cart_tv_cost_total));
        tvCostNum = ((TextView) findViewById(R.id.cart_tv_cost_total_count));
        tvCostNum.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cart_tv_cost_total_count://点击结算
                if (canClearCart()) {
                    requestNetwork(UrlUtils.cartCheckout, 4);
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
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        super.mhandle200Data(what, object, dataObj, message);
        if (what == 0) {//购物车数据
            allGoodsNum = 0;
            getData(dataObj);
            //shopTabs.setThreeNoty(allGoodsNum);
            if (adapter == null) {
                adapter = new CartAdapter(R.layout.cart_list_item, R.layout.cart_list_title, data, this, cbTotal);
                //购物车是空的，去逛逛吧
                View emptyView = LayoutInflater.from(this).inflate(R.layout.cart_empty_view, null);
                TextView goShop = (TextView) emptyView.findViewById(R.id.tx_go_shop);
                goShop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*ShopActivity a = (ShopActivity) getActivity();
                        a.getShopTabs().statusChaByPosition(0, 1);
                        a.selFrgByPos(ShopActivity.HOMEPAGE);*/
                    }
                });
                adapter.setEmptyView(emptyView);
                rv.setAdapter(adapter);
                adapter.setCallBack(this);
            } else {
                adapter.notifyDataSetChanged();
            }
            calculateNumAndCost();
        } else if (what == 1) {//选择商品
            calculateNumAndCost();
        } else if (what == 2) {//加减商品
            data.get(position).t.setUserQuy(goodsCount);
            adapter.notifyItemChanged(position);
            calculateNumAndCost();
            //shopTabs.setThreeNoty(shopTabs.getThreeNotyNum()+ (isAdd ? 1:-1));
        } else if (what == 3) {//删除商品
            data.remove(position);
            adapter.notifyItemRemoved(position);
            calculateNumAndCost();
            requestNetwork(UrlUtils.shoppingCart, 0);
        } else if (what == 4) {//商品结算
            Intent intent = new Intent(this, ConfirmOrderActivity.class);
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
                        int goods_id = goodsOBj.optInt("goods_id");//商品id
                        String goods_number = (String) goodsOBj.opt("goods_number");//商品编号
                        String goods_name = (String) goodsOBj.opt("goods_name");//商品名称
                        String goods_price = (String) goodsOBj.opt("goods_price");//商品价格
                        int goods_num = goodsOBj.optInt("goods_num");//商品数量
                        String goods_img =  goodsOBj.optString("goods_img");//商品图片
                        String spec_names = (String) goodsOBj.opt("spec_names");//商品规格
                        int selected = goodsOBj.optInt("selected");//商品是否被选中
                        int cart_id = goodsOBj.optInt("cart_id");//
                        Goods goods = new Goods();
                        goods.setGoodsNumber(goods_number);
                        goods.setGoodsTitle(goods_name);
                        goods.setGoodsPrice(goods_price);
                        goods.setUserQuy(goods_num);
                        allGoodsNum = allGoodsNum + goods_num;
                        goods.setImgUrl(goods_img);
                        goods.setGoodsStandard(spec_names);
                        goods.setCartId(cart_id+"");
                        goods.setTitleId(goods_id);
                        goods.setTeiHui(goodsOBj.optString("promotion_text"));
                        goods.setUseTicketToReduce(goodsOBj.optString("promotion_mark"));
                        CartGoods dg = new CartGoods(goods, Integer.parseInt(store_id), selected != 0);
                        data.add(dg);
                        if (selected == 0) {//未选择
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
    public void onClickedChild(boolean isChecked, int position) {
        requestNetwork(UrlUtils.cartSelectdGoods, 1);
    }

    @Override
    public void onClickedParent(boolean isChecked, int position) {
        requestNetwork(UrlUtils.cartSelectdGoods, 1);

    }

    @Override
    public void onClickedTotal(boolean isChecked) {
        requestNetwork(UrlUtils.cartSelectdGoods, 1);
    }

    @Override
    public void clickAddBtn(int count, int position) {
        isAdd=true;
        goodsCount = count + 1;
        this.position = position;
        requestNetwork(UrlUtils.cartChangenumGoods, 2);
    }

    @Override
    public void clickSubBtn(int count, int position) {
        isAdd=false;
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

            tvCost.setText("￥" + new DecimalFormat("##0.00").format(totalCost));
            tvCostNum.setText("结算(" + totalCount + ")");
        }
    }

    private String createIds() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            CartGoods cg = data.get(i);
            if (!cg.isHeader) {
                if (cg.isCheck()) {
                    sb.append(cg.t.getCartId());
                    sb.append(",");
                }
            }
        }
        String ids = sb.toString();
        if (ids.endsWith(",")) {
            ids = ids.substring(0, ids.length() - 1);
        }
        return ids;
    }
}
