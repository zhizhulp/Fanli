package com.ascba.rebate.activities.shop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.ConfirmBuyOrderActivity;
import com.ascba.rebate.activities.GoodsDetailsActivity;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.supermaket.TypeMarketActivity;
import com.ascba.rebate.adapter.FilterAdapter;
import com.ascba.rebate.adapter.LinerGoodsListAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.beans.GoodsAttr;
import com.ascba.rebate.beans.ShopBaseItem;
import com.ascba.rebate.beans.ShopItemType;
import com.ascba.rebate.fragments.shop.ShopMainFragment;
import com.ascba.rebate.utils.StringUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.utils.ViewUtils;
import com.ascba.rebate.view.StdDialog;
import com.ascba.rebate.view.cart_btn.NumberButton;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 商城搜索界面
 */

public class ShopSearchActivity extends BaseNetActivity implements View.OnClickListener {

    private static final int REQUEST_STD_LOGIN = 3;
    private static final int REQUEST_ADD_TO_CART_LOGIN = 4;
    private EditText etSearch;
    private List<Goods> beanList=new ArrayList<>();
    private Goods selectGoods;//点击到的无规格商品
    private Goods selectGoodsHasSpec;//点击到的有规格的商品
    private String attention = "请选择完整的商品规格";//没选择完整规格的提醒
    private StdDialog sd;
    private boolean isAll;
    private NumberButton nb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_search);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.back_icon).setOnClickListener(this);
        findViewById(R.id.tv_search).setOnClickListener(this);
        etSearch = ((EditText) findViewById(R.id.goods_et_search));
        initRecyclerView();
        loadRequestor=new LoadRequestor() {
            @Override
            public void loadMore() {
                requestNetwork(UrlUtils.searchGoods,0,etSearch.getText().toString());
            }

            @Override
            public void pullToRefresh() {
                requestNetwork(UrlUtils.searchGoods,0,etSearch.getText().toString());
            }
        };
        initRefreshLayout();
        initLoadMoreRequest();
    }

    private void requestNetwork(String url,int what,String keywords){
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        if(what==0){
            if(StringUtils.isEmpty(keywords)){
                showToast("请输入要搜索的关键字");
                return;
            }
            request.add("now_page",now_page);
            request.add("keywords",keywords);
        }else if (what == 1) {//添加商品到购物车
            boolean has_spec = selectGoods.isHasStandard();
            request.add("goods_id", has_spec ? selectGoodsHasSpec.getTitleId() : selectGoods.getTitleId());
            request.add("goods_num", has_spec ? nb.getNumber() : 1);
            request.add("goods_spec_id", has_spec ? selectGoodsHasSpec.getCartId() : null);
        } else if (what == 2) {//规格数据
            request.add("goods_id", selectGoods.getTitleId());
        } else if (what == 3) {//立即购买
            boolean has_spec = selectGoods.isHasStandard();
            request.add("goods_id", has_spec ? selectGoodsHasSpec.getTitleId() : selectGoods.getTitleId());
            request.add("goods_num", has_spec ? nb.getNumber() : 1);
            request.add("goods_spec_id", has_spec ? selectGoodsHasSpec.getCartId() : null);
        }
        executeNetWork(what,request,"请稍后");
    }

    private void initRecyclerView() {
        RecyclerView rv = ((RecyclerView) findViewById(R.id.recyclerView));
        rv.setLayoutManager(new LinearLayoutManager(this));
        baseAdapter=new LinerGoodsListAdapter(R.layout.goods_list_layout_linear,beanList);
        baseAdapter.setEmptyView(ViewUtils.getEmptyView(this,"暂无商品信息"));
        rv.setAdapter(baseAdapter);
        rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Goods goods = beanList.get(position);
                GoodsDetailsActivity.startIntent(ShopSearchActivity.this, goods.getTitleId());

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);

                //加入购物车动画
                if (view.getId() == R.id.goods_list_cart) {
                    selectGoods = beanList.get(position);
                    if (AppConfig.getInstance().getInt("uuid", -1000) != -1000) {
                        if (!selectGoods.isHasStandard()) {//无规格
                            requestNetwork(UrlUtils.cartAddGoods, 1,null);
                        } else {//有规格
                            requestNetwork(UrlUtils.getGoodsSpec, 2,null);
                        }

                    } else {
                        Intent intent2 = new Intent(ShopSearchActivity.this, LoginActivity.class);
                        startActivityForResult(intent2, REQUEST_STD_LOGIN);
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_icon://返回
                finish();
                break;
            case R.id.tv_search://搜索
                resetPage();
                requestNetwork(UrlUtils.searchGoods,0,etSearch.getText().toString());
                break;
        }
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        super.mhandle200Data(what, object, dataObj, message);
        if(what==0){
            refreshGoodsList(dataObj);
        }else if (what == 1) {//添加到购物车成功
            ViewUtils.showMyToast(this,R.layout.add_to_cart_toast);
            if (sd != null) {
                sd.dismiss();
            }

            /*if (nb != null) {
                shopTabs.setThreeNoty(shopTabs.getThreeNotyNum() + (has_spec ? nb.getNumber() : 1));
            } else {
                shopTabs.setThreeNoty(shopTabs.getThreeNotyNum() + 1);
            }*/
            MyApplication.isLoadCartData=true;

        } else if (what == 2) {//规格数据
            JSONArray filter_spec = dataObj.optJSONArray("filter_spec");
            JSONArray array = dataObj.optJSONArray("spec_goods_price");
            JSONObject goodsInfo = dataObj.optJSONObject("goods_info");
            showStandardDialog(parseFilterSpec(filter_spec), parseSpecGoodsPrice(array),parseDefaultGoods(goodsInfo));
        } else if (what == 3) {//立即购买 成功
            if (sd != null) {
                sd.dismiss();
            }
            Intent intent = new Intent(this, ConfirmBuyOrderActivity.class);
            intent.putExtra("json_data", dataObj.toString());
            startActivity(intent);
        }
    }

    private void refreshGoodsList(JSONObject dataObj) {
        if(isRefreshing && beanList.size()>0){
            beanList.clear();
        }
        JSONArray array = dataObj.optJSONArray("mallGoods");
        if(array!=null && array.length() >0){
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.optJSONObject(i);
                Goods goods=new Goods();
                goods.setTitleId(object.optInt("id"));
                goods.setGoodsTitle(object.optString("title"));
                goods.setImgUrl(UrlUtils.baseWebsite+object.optString("img"));
                goods.setGoodsPrice("¥"+object.optString("shop_price"));
                goods.setStoreId(object.optInt("store_id"));
                goods.setHasStandard(object.optInt("has_spec") == 1);
                beanList.add(goods);
            }
        }
        baseAdapter.notifyDataSetChanged();
    }
    //购物车Dialog
    private void showStandardDialog(List<GoodsAttr> gas, List<Goods> goodses, Goods defaultGoods) {
        if (gas.size() == 0 || goodses.size() == 0) {
            return;
        }
        if(sd!=null && sd.isShowing()){
            sd.dismiss();
        }
        sd = new StdDialog(this, gas, goodses,defaultGoods);
        sd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isAll=false;
            }
        });
        nb = sd.getNb();
        //把商品加入购物车
        sd.getTvAddToCart().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAll) {//选择了完整的规格
                    if (AppConfig.getInstance().getInt("uuid", -1000) != -1000) {
                        requestNetwork(UrlUtils.cartAddGoods, 1,null);
                    } else {
                        Intent intent = new Intent(ShopSearchActivity.this, LoginActivity.class);
                        startActivityForResult(intent, REQUEST_ADD_TO_CART_LOGIN);
                    }
                } else {
                    getDm().buildAlertDialog(attention);
                }

            }
        });
        //点击购买
        sd.getTvPurchase().setOnClickListener(new View.OnClickListener() {//点击立即购买
            @Override
            public void onClick(View v) {
                if (selectGoods.isHasStandard()) {
                    //包含规格
                    if (isAll) {
                        //选择完整规格
                        requestNetwork(UrlUtils.goodsCheckout, 3,null);
                    } else {
                        getDm().buildAlertDialog(attention);
                    }
                } else {
                    //不包含规格
                    requestNetwork(UrlUtils.goodsCheckout, 3,null);
                }
            }
        });
        //监听商品规格
        sd.setListener(new StdDialog.Listener() {
            @Override
            public void getSelectGoods(Goods gs) {
                selectGoodsHasSpec= gs;
            }

            @Override
            public void isSelectAll(boolean isAll) {
                ShopSearchActivity.this.isAll = isAll;
            }
        });
        //点击加号
        nb.getAddButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAll){
                    getDm().buildAlertDialog(attention);
                }else {
                    if (nb.getInventory() <= nb.getNumber()) {
                        //库存不足
                        nb.warningForInventory();
                    }else {
                        nb.setCurrentNumber(nb.getNumber() + 1);
                    }
                }
            }
        });
        //点击减号
        nb.getSubButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAll){
                    if(!isAll){
                        getDm().buildAlertDialog(attention);
                    }else {
                        nb.setCurrentNumber(nb.getNumber()-1);
                    }
                }
            }
        });
        sd.showMyDialog();
    }

    private List<Goods> parseSpecGoodsPrice(JSONArray array) {
        List<Goods> goodses = new ArrayList<Goods>();
        if (array != null && array.length() != 0) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                int id = obj.optInt("id");
                int goods_id = obj.optInt("goods_id");
                String goods_number = obj.optString("goods_number");
                String spec_keys = obj.optString("spec_keys");
                String spec_names = obj.optString("spec_names");
                String shop_price = obj.optString("shop_price");
                String market_price = obj.optString("market_price");
                int inventory = obj.optInt("inventory");

                Goods goods = new Goods();
                goods.setCartId(id + "");
                goods.setTitleId(goods_id);
                goods.setGoodsNumber(goods_number);
                goods.setSpecKeys(spec_keys);
                goods.setSpecNames(spec_names);
                goods.setGoodsPrice(shop_price);
                goods.setTotalPrice(market_price);
                goods.setInventory(inventory);

                goodses.add(goods);
            }
        }
        return goodses;
    }

    private List<GoodsAttr> parseFilterSpec(JSONArray filter_spec) {
        List<GoodsAttr> gas = new ArrayList<GoodsAttr>();
        if (filter_spec != null && filter_spec.length() != 0) {
            for (int i = 0; i < filter_spec.length(); i++) {
                JSONObject jObj = filter_spec.optJSONObject(i);
                if (jObj != null) {
                    GoodsAttr ga = new GoodsAttr();
                    String title = jObj.optString("title");
                    JSONArray item = jObj.optJSONArray("item");
                    if (item != null && item.length() != 0) {
                        List<GoodsAttr.Attrs> ats = new ArrayList<GoodsAttr.Attrs>();
                        for (int j = 0; j < item.length(); j++) {
                            JSONObject obj = item.optJSONObject(j);
                            if (obj != null) {
                                int item_id = obj.optInt("item_id");
                                String item_value = obj.optString("item_value");
                                GoodsAttr.Attrs as = ga.new Attrs(item_id, item_value, 0);

                                ats.add(as);
                            }
                        }
                        ga.setSelect(false);
                        ga.setLayout(R.layout.filter_layout);
                        ga.setStrs(ats);
                        ga.setTitle(title);
                        ga.setType(FilterAdapter.TYPE1);

                    }
                    gas.add(ga);
                }
            }
        }
        return gas;
    }
    private Goods parseDefaultGoods(JSONObject goodsInfo) {
        Goods goods=new Goods();
        if(goodsInfo!=null){
            goods.setGoodsTitle(goodsInfo.optString("title"));
            goods.setInventory(Integer.parseInt(goodsInfo.optString("inventory")));
            goods.setGoodsPrice(goodsInfo.optString("shop_price"));
            goods.setImgUrl(UrlUtils.baseWebsite + goodsInfo.optString("img"));
        }
        return goods;
    }
}
