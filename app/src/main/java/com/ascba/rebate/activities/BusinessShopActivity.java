package com.ascba.rebate.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.shop.ShopActivity;
import com.ascba.rebate.adapter.BusinessShopAdapter;
import com.ascba.rebate.adapter.FilterAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.beans.GoodsAttr;
import com.ascba.rebate.beans.ShopBaseItem;
import com.ascba.rebate.beans.ShopItemType;
import com.ascba.rebate.beans.TypeWeight;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.utils.ViewUtils;
import com.ascba.rebate.view.ShopABar;
import com.ascba.rebate.view.StdDialog;
import com.ascba.rebate.view.cart_btn.NumberButton;
import com.ascba.rebate.view.loadmore.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.chad.library.adapter.base.loadmore.LoadMoreView.STATUS_DEFAULT;


/**
 * Created by 李鹏 on 2017/03/15 0015.
 * 商家店铺
 */

public class BusinessShopActivity extends BaseNetActivity implements
        SwipeRefreshLayout.OnRefreshListener
        , BaseNetActivity.Callback {

    private static final int LOAD_MORE_END = 1;
    private static final int LOAD_MORE_ERROR = 0;
    private static final int REQUEST_ADD_TO_CART_LOGIN = 2;
    private static final int REQUEST_STD_LOGIN = 3;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_MORE_END:
                    adapter.loadMoreEnd(false);
                    break;
                case LOAD_MORE_ERROR:
                    if (adapter != null) {
                        adapter.loadMoreFail();
                    }
                    break;
            }
        }
    };
    private Context context;
    private ShopABar shopABar;
    private RecyclerView recyclerView;
    private int store_id;
    private static final int REQUEST_LOGIN = 0;
    private BusinessShopAdapter adapter;
    private int now_page = 1;
    private int total_page;
    private View headView;
    private ImageView backImg;
    private ImageView headImg;
    private TextView tvShopName;
    private List<ShopBaseItem> goodsList = new ArrayList<>();
    private CustomLoadMoreView loadMoreView;
    private boolean isRefresh = true;//true 下拉刷新 false 上拉加载
    private int finalScene;
    private int goodsId;
    private StdDialog sd;//规格dialog
    private NumberButton nb;//加减控件
    private boolean isAll;//是否选择了所有的规格
    private Goods goodsSelect;//选择的商品(有规格)
    private ShopBaseItem sbi;//选择的商品（无规格）
    private String attention = "请选择完整的商品规格";//没选择完整规格的提醒

    private boolean has_spec;//加入购物车的商品是否有规格

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_shop);
        context = this;
        initView();
        isLogin();//此页面需要登录
    }

    private void isLogin() {
        if (AppConfig.getInstance().getInt("uuid", -1000) != -1000) {
            getStoreFromIntent();
            requestData(UrlUtils.getStore, 0);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        }
    }

    private void getStoreFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            store_id = intent.getIntExtra("store_id", -1000);
        }
    }

    private void requestData(String url, int flag) {
        finalScene = flag;
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        switch (flag) {
            case 0:
                //获取数据
                request.add("store_id", store_id);
                break;
            case 1:
                //添加商品到购物车
                request.add("goods_id", has_spec ? goodsSelect.getTitleId() : sbi.getColor());
                request.add("goods_num", has_spec ? nb.getNumber() : 1);
                request.add("goods_spec_id", has_spec ? goodsSelect.getCartId() : null);
                break;
            case 2:
                //规格数据
                request = buildNetRequest(url, 0, true);
                request.add("goods_id", goodsId);
                break;
            case 3:
                //立即购买
                request.add("goods_id", has_spec ? goodsSelect.getTitleId() : sbi.getColor());
                request.add("goods_num", has_spec ? nb.getNumber() : 1);
                request.add("goods_spec_id", has_spec ? goodsSelect.getCartId() : null);
                break;
        }
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    private void initView() {
        //刷新
        initRefreshLayout();
        refreshLayout.setOnRefreshListener(this);

        //导航栏
        shopABar = (ShopABar) findViewById(R.id.shopbar);
        shopABar.setImageOther(R.mipmap.icon_cart_black);
        shopABar.setCallback(new ShopABar.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkMsg(View v) {
                //消息中心
                ShopMessageActivity.startIntent(context);
            }

            @Override
            public void clkOther(View v) {
                ShopActivity.setIndex(ShopActivity.CART);
                startActivity(new Intent(context, ShopActivity.class));
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        //头布局
        headView = LayoutInflater.from(context).inflate(R.layout.business_shop_head, null);
        backImg = (ImageView) headView.findViewById(R.id.shop_img);
        tvShopName = ((TextView) headView.findViewById(R.id.shop_name));
        headImg = (ImageView) headView.findViewById(R.id.shop_img_head);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShopBaseItem shopBaseItem = goodsList.get(position);
                if (goodsList.size() != 0) {
                    GoodsDetailsActivity.startIntent(context, shopBaseItem.getColor());
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);

                //加入购物车动画
                if (view.getId() == R.id.goods_list_cart) {
                    ShopBaseItem shopBaseItem = goodsList.get(position);
                    sbi = shopBaseItem;
                    has_spec = shopBaseItem.isHasStandard();
                    goodsId = shopBaseItem.getColor();
                    if (AppConfig.getInstance().getInt("uuid", -1000) != -1000) {
                        if (!has_spec) {//无规格
                            requestData(UrlUtils.cartAddGoods, 1);
                        } else {//有规格
                            requestData(UrlUtils.getGoodsSpec, 2);
                        }
                    } else {
                        Intent intent2 = new Intent(context, LoginActivity.class);
                        startActivityForResult(intent2, REQUEST_STD_LOGIN);
                    }

                }
            }
        });
    }


    @Override
    public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;
        }
        clearData();
        resetPage();
        requestData(UrlUtils.getStore, 0);
    }


    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if (finalScene == 0) {
            refreshLayout.setRefreshing(false);
            if (adapter != null) {
                adapter.loadMoreComplete();
            }
            if (loadMoreView != null) {
                loadMoreView.setLoadMoreStatus(STATUS_DEFAULT);
            }
            getPageCount(dataObj);//分页数据
            if (isRefresh) {//下拉刷新
                resetPage();
                clearData();
                refreshHeadData(dataObj);//头部数据
                refreshGoodsData(dataObj);//商品列表数据
            } else {//上拉加载
                refreshGoodsData(dataObj);//商品列表数据
            }
            initAdapterAndRefresh();
            initLoadMore();
        } else if (finalScene == 1) {//添加到购物车成功
            getDm().buildAlertDialog(message);
            MyApplication.isLoadCartData=true;
            if (sd != null) {
                sd.dismiss();
            }
        } else if (finalScene == 2) {//规格数据
            LogUtils.PrintLog("ShopMainFragment", "data-->" + dataObj);
            JSONArray filter_spec = dataObj.optJSONArray("filter_spec");
            JSONArray array = dataObj.optJSONArray("spec_goods_price");
            JSONObject goodsInfo = dataObj.optJSONObject("goods_info");
            showStandardDialog(parseFilterSpec(filter_spec), parseSpecGoodsPrice(array),parseDefaultGoods(goodsInfo));
        } else if (finalScene == 3) {//立即购买 成功
            if (sd != null) {
                sd.dismiss();
            }
            Intent intent = new Intent(context, ConfirmBuyOrderActivity.class);
            intent.putExtra("json_data", dataObj.toString());
            startActivity(intent);
        }
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

    private void initLoadMore() {
        if (isRefresh) {
            isRefresh = false;
        }
        if (loadMoreView == null) {
            loadMoreView = new CustomLoadMoreView();
            adapter.setLoadMoreView(loadMoreView);
            adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    if (now_page > total_page - 1 && total_page != 0) {
                        handler.sendEmptyMessage(LOAD_MORE_END);
                    } else {
                        requestData(UrlUtils.getStore, 0);
                    }
                }
            });
        }
    }

    private void initAdapterAndRefresh() {
        if (adapter == null) {
            adapter = new BusinessShopAdapter(R.layout.shop_goods, goodsList, context);
            adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
                @Override
                public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                    return 1;
                }
            });
            adapter.addHeaderView(headView);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }

    private void refreshGoodsData(JSONObject dataObj) {
        JSONArray array = dataObj.optJSONArray("mallGoods");
        if (array == null || array.length() == 0) {
            adapter.setEmptyView(ViewUtils.getEmptyView(this, "暂无商品数据"));
        } else {
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                String id = obj.optString("id");
                String title = obj.optString("title");
                String img = obj.optString("img");
                String shop_price = obj.optString("shop_price");

                ShopBaseItem shopBaseItem = new ShopBaseItem(ShopItemType.TYPE_GOODS, TypeWeight.TYPE_SPAN_SIZE_30, R.layout.shop_goods
                        , UrlUtils.baseWebsite + img, title, "￥" + shop_price, "", false);
                shopBaseItem.setColor(Integer.parseInt(id));
                shopBaseItem.setHasStandard(obj.optString("has_spec").equals("1"));
                goodsList.add(shopBaseItem);
            }
        }
    }

    private void refreshHeadData(JSONObject dataObj) {
        JSONObject head = dataObj.optJSONObject("store");
        if (head == null) {
            adapter.removeHeaderView(headView);
        } else {
            Picasso.with(this).load(UrlUtils.baseWebsite + head.optString("store_banner")).into(backImg);
            Picasso.with(this).load(UrlUtils.baseWebsite + head.optString("store_logo")).into(headImg);
            tvShopName.setText(head.optString("store_name"));
        }
    }

    private void getPageCount(JSONObject dataObj) {
        total_page = dataObj.optInt("total_page");
        now_page++;
    }

    @Override
    public void handle404(String message) {
        getDm().buildAlertDialog(message);
    }

    @Override
    public void handleNoNetWork() {
        refreshLayout.setRefreshing(false);
        handler.sendEmptyMessage(LOAD_MORE_ERROR);
    }


    private void clearData() {
        if (goodsList.size() != 0) {
            goodsList.clear();
            adapter.notifyDataSetChanged();
        }
    }

    private void resetPage() {
        if (now_page != 1) {
            now_page = 1;
        }
    }

    //购物车Dialog
    private void showStandardDialog(List<GoodsAttr> gas, List<Goods> goodses,Goods defaultGoods) {
        if (gas.size() == 0 || goodses.size() == 0) {
            return;
        }
        sd = new StdDialog(context, gas, goodses,defaultGoods);
        sd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isAll = false;
            }
        });
        nb = sd.getNb();
        //把商品加入购物车
        sd.getTvAddToCart().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAll) {//选择了完整的规格
                    if (AppConfig.getInstance().getInt("uuid", -1000) != -1000) {
                        requestData(UrlUtils.cartAddGoods, 1);
                    } else {
                        Intent intent = new Intent(context, LoginActivity.class);
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
                if (has_spec) {
                    //包含规格
                    if (isAll) {
                        //选择完整规格
                        requestData(UrlUtils.goodsCheckout, 3);
                    } else {
                        getDm().buildAlertDialog(attention);
                    }
                } else {
                    //不包含规格
                    requestData(UrlUtils.goodsCheckout, 3);
                }
            }
        });
        //监听商品规格
        sd.setListener(new StdDialog.Listener() {
            @Override
            public void getSelectGoods(Goods gs) {
                goodsSelect = gs;
            }

            @Override
            public void isSelectAll(boolean isAll) {
                BusinessShopActivity.this.isAll = isAll;
            }
        });
        //点击加号
        nb.getAddButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAll) {
                    getDm().buildAlertDialog(attention);
                } else {
                    if (nb.getInventory() <= nb.getNumber()) {
                        //库存不足
                        nb.warningForInventory();
                    } else {
                        nb.setCurrentNumber(nb.getNumber() + 1);
                    }
                }
            }
        });
        //点击减号
        nb.getSubButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAll) {
                    if (!isAll) {
                        getDm().buildAlertDialog(attention);
                    } else {
                        nb.setCurrentNumber(nb.getNumber() - 1);
                    }
                }
            }
        });
        sd.showMyDialog();
    }

    //解析规格
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
                String weight = obj.optString("weight");

                Goods goods = new Goods();
                goods.setCartId(id + "");
                goods.setTitleId(goods_id);
                goods.setGoodsNumber(goods_number);
                goods.setSpecKeys(spec_keys);
                goods.setSpecNames(spec_names);
                goods.setGoodsPrice(shop_price);
                goods.setTotalPrice(market_price);
                goods.setInventory(inventory);
                //goods.setWeight(22222);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case REQUEST_ADD_TO_CART_LOGIN:
                    if (resultCode == RESULT_OK) {
                        requestData(UrlUtils.cartAddGoods, 1);
                    }
                    break;
                case REQUEST_STD_LOGIN:
                    if (resultCode == RESULT_OK) {
                        if (!has_spec) {//无规格
                            requestData(UrlUtils.cartAddGoods, 1);
                        } else {//有规格
                            requestData(UrlUtils.getGoodsSpec, 2);
                        }
                    }
                    break;

                case REQUEST_LOGIN:
                    if (resultCode == RESULT_OK) {//登录成功
                        getStoreFromIntent();
                        requestData(UrlUtils.getStore, 0);
                    } else {//登录失败
                        finish();
                    }
                    break;
            }
        }
    }

}

