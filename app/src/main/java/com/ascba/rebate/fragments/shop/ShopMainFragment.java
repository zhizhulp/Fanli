package com.ascba.rebate.fragments.shop;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.ConfirmBuyOrderActivity;
import com.ascba.rebate.activities.GoodsDetailsActivity;
import com.ascba.rebate.activities.ShopMessageActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.shop.ShopActivity;
import com.ascba.rebate.activities.shop.ShopSearchActivity;
import com.ascba.rebate.activities.supermaket.TypeMarketActivity;
import com.ascba.rebate.adapter.FilterAdapter;
import com.ascba.rebate.adapter.ShopTypeRVAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.beans.Banner;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.beans.GoodsAttr;
import com.ascba.rebate.beans.ShopBaseItem;
import com.ascba.rebate.beans.ShopItemType;
import com.ascba.rebate.beans.TypeWeight;
import com.ascba.rebate.fragments.base.BaseNetFragment;
import com.ascba.rebate.utils.JsonUtil;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.utils.ViewUtils;
import com.ascba.rebate.view.MsgView;
import com.ascba.rebate.view.ShopTabs;
import com.ascba.rebate.view.StdDialog;
import com.ascba.rebate.view.cart_btn.NumberButton;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 商城首页
 */

public class ShopMainFragment extends BaseNetFragment implements BaseNetFragment.Callback {
    private static final int REQUEST_ADD_TO_CART_LOGIN = 2;
    private static final int REQUEST_STD_LOGIN = 3;
    private RecyclerView rv;
    private List<ShopBaseItem> data = new ArrayList<>();
    private List<Banner> urls = new ArrayList<>();//viewPager数据源
    private RelativeLayout searchHead;//搜索头
    private View searchHeadLine;
    private int mDistanceY = 0;//下拉刷新滑动距离
    private int finalScene;
    private int goodsId;
    private StdDialog sd;//规格dialog
    private NumberButton nb;//加减控件
    private boolean isAll;//是否选择了所有的规格
    private Goods goodsSelect;//选择的商品(有规格)
    private ShopBaseItem sbi;//选择的商品（无规格）
    private String attention = "请选择完整的商品规格";//没选择完整规格的提醒
    private MsgView msgView;
    private ShopTabs shopTabs;
    private boolean has_spec;//加入购物车的商品是否有规格

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.third_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        initHeadView(view);
        //消息
        LinearLayout messageBtn = (LinearLayout) view.findViewById(R.id.head_rr);
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopMessageActivity.startIntent(getActivity());
            }
        });
        msgView = (MsgView) view.findViewById(R.id.head_img_xiaoxi);
        rv = ((RecyclerView) view.findViewById(R.id.list_clothes));
        setLoadRequestor(new LoadRequestor() {
            @Override
            public void loadMore() {
                requestNetwork(UrlUtils.shop, 0);
            }

            @Override
            public void pullToRefresh() {
                if (data.size() != 0) {
                    data.clear();
                    baseAdapter.notifyDataSetChanged();
                }
                requestNetwork(UrlUtils.shop, 0);
            }
        });
        initRefreshLayout(view);
        rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShopBaseItem shopBaseItem = data.get(position);
                if (data.size() != 0) {
                    if (shopBaseItem.getItemType() == ShopItemType.TYPE_GOODS) {
                        GoodsDetailsActivity.startIntent(getActivity(), shopBaseItem.getColor());
                    } else if (shopBaseItem.getItemType() == ShopItemType.TYPE_NAVIGATION) {
                        TypeMarketActivity.startIntent(getActivity(), shopBaseItem.getColor(),shopBaseItem.getDesc());
                    }
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);

                //加入购物车动画
                if (view.getId() == R.id.goods_list_cart) {
                    ShopBaseItem shopBaseItem = data.get(position);
                    sbi = shopBaseItem;
                    has_spec = shopBaseItem.isHasStandard();
                    goodsId = shopBaseItem.getColor();
                    if (AppConfig.getInstance().getInt("uuid", -1000) != -1000) {
                        if (!has_spec) {//无规格
                            requestNetwork(UrlUtils.cartAddGoods, 1);
                        } else {//有规格
                            requestNetwork(UrlUtils.getGoodsSpec, 2);
                        }

                    } else {
                        Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                        startActivityForResult(intent2, REQUEST_STD_LOGIN);
                    }

                }
            }
        });

        //滑动标题栏渐变
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //滑动的距离
                mDistanceY += dy;
                //toolbar的高度
                int toolbarHeight = searchHead.getBottom();
                float maxAlpha = 229.5f;//最大透明度80%
                //当滑动的距离 <= toolbar高度的时候，改变Toolbar背景色的透明度，达到渐变的效果
                if (mDistanceY <= toolbarHeight) {
                    float scale = (float) mDistanceY / toolbarHeight;
                    float alpha = scale * maxAlpha;
                    searchHead.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    searchHeadLine.setAlpha(alpha);
                }
            }
        });

        ShopActivity shopActivity = (ShopActivity) getActivity();
        shopTabs = shopActivity.getShopTabs();

        requestNetwork(UrlUtils.shop, 0);
    }

    private void initHeadView(View view) {
        searchHead = (RelativeLayout) view.findViewById(R.id.head_search_rr);
        searchHeadLine = view.findViewById(R.id.homepage_head_view);
        view.findViewById(R.id.head_ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        view.findViewById(R.id.et_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ShopSearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void requestNetwork(String url, int scene) {
        finalScene = scene;
        Request<JSONObject> request = null;
        if (scene == 0) {//商品列表
            request = buildNetRequest(url, 0, false);
            request.add("now_page", now_page);
        } else if (scene == 1) {//添加商品到购物车
            request = buildNetRequest(url, 0, true);
            request.add("goods_id", has_spec ? goodsSelect.getTitleId() : sbi.getColor());
            request.add("goods_num", has_spec ? nb.getNumber() : 1);
            request.add("goods_spec_id", has_spec ? goodsSelect.getCartId() : null);
        } else if (scene == 2) {//规格数据
            request = buildNetRequest(url, 0, true);
            request.add("goods_id", goodsId);
        } else if (scene == 3) {//立即购买
            request = buildNetRequest(url, 0, true);
            request.add("goods_id", has_spec ? goodsSelect.getTitleId() : sbi.getColor());
            request.add("goods_num", has_spec ? nb.getNumber() : 1);
            request.add("goods_spec_id", has_spec ? goodsSelect.getCartId() : null);
        }
        executeNetWork(request, "请稍后");
        setCallback(this);
    }


    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if (finalScene == 0) {

            if (isRefreshing) {//下拉刷新
                if (urls.size() != 0) {
                    urls.clear();
                }
                //广告轮播
                initViewpager(dataObj);

                //商城首页导航栏
                initShoopNave(dataObj);

                //商品列表
                initGoodsList(dataObj);

                initadapter();

                initLoadMoreRequest();
                //购物车标志
                int mallcartNum = dataObj.optInt("mallcart_num");//购物车商品数量
                shopTabs.setThreeNoty(mallcartNum);

            } else {//上拉加载

                initGoodsList(dataObj);
            }
        } else if (finalScene == 1) {//添加到购物车成功
            ViewUtils.showMyToast(getActivity(),R.layout.add_to_cart_toast);
            if (sd != null) {
                sd.dismiss();
            }

            if (nb != null) {
                shopTabs.setThreeNoty(shopTabs.getThreeNotyNum() + (has_spec ? nb.getNumber() : 1));
            } else {
                shopTabs.setThreeNoty(shopTabs.getThreeNotyNum() + 1);
            }
            MyApplication.isLoadCartData=true;

        } else if (finalScene == 2) {//规格数据
            JSONArray filter_spec = dataObj.optJSONArray("filter_spec");
            JSONArray array = dataObj.optJSONArray("spec_goods_price");
            JSONObject goodsInfo = dataObj.optJSONObject("goods_info");
            showStandardDialog(parseFilterSpec(filter_spec), parseSpecGoodsPrice(array),parseDefaultGoods(goodsInfo));
        } else if (finalScene == 3) {//立即购买 成功
            if (sd != null) {
                sd.dismiss();
            }
            Intent intent = new Intent(getActivity(), ConfirmBuyOrderActivity.class);
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
            goods.setImgUrl( UrlUtils.getNewUrl(goodsInfo.optString("img")));
        }
        return goods;
    }


    /**
     * 广告轮播
     */
    private void initViewpager(JSONObject dataObj) {
        //轮播数据
        JSONArray pagerArray = dataObj.optJSONArray("banners" );
        if (pagerArray != null && pagerArray.length() != 0) {
            List<Banner> banners = JsonUtil.stringToList(pagerArray.toString(), Banner.class);
            ShopBaseItem shopBaseItem = new ShopBaseItem(ShopItemType.TYPE_PAGER, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_pager);
            shopBaseItem.setBanners(banners);
            data.add(shopBaseItem);
        }
    }

    /**
     * 商城首页导航栏
     */
    private void initShoopNave(JSONObject dataObj) {
        //商品导航
        JSONArray goodsAy = dataObj.optJSONArray("mallCategory");
        int weight;
        if(goodsAy.length()<=5){
            weight = TypeWeight.TYPE_SPAN_SIZE_MAX / goodsAy.length();
        }else{
            weight=TypeWeight.TYPE_SPAN_SIZE_MAX/5;
        }

        if ( goodsAy.length() != 0) {
            for (int i = 0; i < goodsAy.length(); i++) {
                JSONObject gObj = goodsAy.optJSONObject(i);
                String id = gObj.optString("id");
                String cover = gObj.optString("cover");
                String subtitle = gObj.optString("sub_title");

                ShopBaseItem baseItem = new ShopBaseItem(ShopItemType.TYPE_NAVIGATION, weight, R.layout.shop_navigation,
                        UrlUtils.getNewUrl(cover), subtitle);
                baseItem.setColor(Integer.parseInt(id));
                data.add(baseItem);
            }
            //横线
            data.add(new ShopBaseItem(ShopItemType.TYPE_LINE, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_line, 1.0f));
        }
    }

    /**
     * 商品列表
     */
    private void initGoodsList(JSONObject dataObj) {
        JSONArray mallGoodsAy = dataObj.optJSONArray("mallGoods");
        if (mallGoodsAy != null &&  mallGoodsAy.length() != 0) {
            if(isRefreshing){
                data.add(new ShopBaseItem(ShopItemType.TYPE_GUESS,TypeWeight.TYPE_SPAN_SIZE_60,R.layout.shop_title));
                data.add(new ShopBaseItem(ShopItemType.TYPE_LINE, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_line, 1.0f));
            }
            for (int i = 0; i < mallGoodsAy.length(); i++) {
                JSONObject gObj = mallGoodsAy.optJSONObject(i);
                String id = gObj.optString("id");
                String imgUrl = gObj.optString("img");
                String title = gObj.optString("title");
                String shop_price = gObj.optString("shop_price");
                ShopBaseItem shopBaseItem = new ShopBaseItem(ShopItemType.TYPE_GOODS, TypeWeight.TYPE_SPAN_SIZE_30, R.layout.shop_goods
                        , imgUrl, title, "￥" + shop_price, "", false);
                shopBaseItem.setColor(Integer.parseInt(id));
                shopBaseItem.setHasStandard(gObj.optString("has_spec").equals("1"));
                shopBaseItem.setTeiHui(gObj.optString("promotion_text"));
                shopBaseItem.setUseTicketToReduce(gObj.optString("promotion_mark"));
                data.add(shopBaseItem);
            }
        }
    }

    /**
     * 初始化adapter
     */
    private void initadapter() {
        if (baseAdapter == null) {
            baseAdapter = new ShopTypeRVAdapter(data, getActivity());
            GridLayoutManager manager = new GridLayoutManager(getActivity(), TypeWeight.TYPE_SPAN_SIZE_MAX);
            rv.setLayoutManager(manager);
            MySpanSizeLookUp mySpanSizeLookUp = new MySpanSizeLookUp();
            baseAdapter.setSpanSizeLookup(mySpanSizeLookUp);
        //    rv.addItemDecoration(new DividerGridItemDecoration(getActivity(),mySpanSizeLookUp,getResources().getColor(R.color.main_bg)));
            rv.setAdapter(baseAdapter);
            baseAdapter.setEmptyView(ViewUtils.getEmptyView(getActivity(),"暂无信息"));
        } else {
            baseAdapter.notifyDataSetChanged();
        }
    }

    private class MySpanSizeLookUp implements BaseQuickAdapter.SpanSizeLookup{

        @Override
        public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
            ShopBaseItem shopBaseItem;
            try {
                shopBaseItem = data.get(position);
            }catch (IndexOutOfBoundsException e){
                return 60;
            }
            return shopBaseItem.getSpanSize();
        }
    }

    @Override
    public void handleReqFailed() {
        refreshLayout.setRefreshing(false);
        handler.sendEmptyMessage(LOAD_MORE_ERROR);
    }

    @Override
    public void handle404(String message, JSONObject dataObj) {
    }


    @Override
    public void handleReLogin() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void handleNoNetWork() {
        refreshLayout.setRefreshing(false);
        handler.sendEmptyMessage(LOAD_MORE_ERROR);
    }

    //购物车Dialog
    private void showStandardDialog(List<GoodsAttr> gas, List<Goods> goodses,Goods defaultGoods) {
        if (gas.size() == 0 || goodses.size() == 0) {
            return;
        }
        if(sd!=null && sd.isShowing()){
            sd.dismiss();
        }
        sd = new StdDialog(getActivity(), gas, goodses,defaultGoods);
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
                        requestNetwork(UrlUtils.cartAddGoods, 1);
                    } else {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
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
                        requestNetwork(UrlUtils.goodsCheckout, 3);
                    } else {
                        getDm().buildAlertDialog(attention);
                    }
                } else {
                    //不包含规格
                    requestNetwork(UrlUtils.goodsCheckout, 3);
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
                ShopMainFragment.this.isAll = isAll;
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
                    nb.setCurrentNumber(nb.getNumber()-1);
                }else {
                    getDm().buildAlertDialog(attention);
                }
            }
        });
        sd.showMyDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case REQUEST_ADD_TO_CART_LOGIN:
                    if (resultCode == RESULT_OK) {
                        requestNetwork(UrlUtils.cartAddGoods, 1);
                    }
                    break;
                case REQUEST_STD_LOGIN:
                    if (resultCode == RESULT_OK) {
                        if (!has_spec) {//无规格
                            requestNetwork(UrlUtils.cartAddGoods, 1);
                        } else {//有规格
                            requestNetwork(UrlUtils.getGoodsSpec, 2);
                        }
                    }
                    break;
            }
        }
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
}