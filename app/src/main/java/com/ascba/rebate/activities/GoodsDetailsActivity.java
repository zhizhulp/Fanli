package com.ascba.rebate.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.ViewPagerAdapter;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.beans.GoodsDetailsItem;
import com.ascba.rebate.beans.GoodsImgBean;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ShopABar;
import com.ascba.rebate.view.dropDownMultiPager.DropDownMultiPagerView;
import com.ascba.rebate.view.dropDownMultiPager.ultraPullToRefash.component.PtrFrameLayout;
import com.ascba.rebate.view.dropDownMultiPager.ultraPullToRefash.handler.PtrDefaultHandler;
import com.ascba.rebate.view.pullUpToLoadMoreView.PullUpToLoadMoreView;
import com.bumptech.glide.Glide;
import com.yolanda.nohttp.rest.Request;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/02 0002.
 * 商品详情页
 */
@SuppressLint("SetTextI18n")
public class GoodsDetailsActivity extends BaseNetWork4Activity implements View.OnClickListener {

    /**
     * 商品id
     */
    private int goodsId;

    //足记控件
    private PtrFrameLayout ptrLayout;

    //向上拖动查看详情
    private PullUpToLoadMoreView pullUpToLoadMoreView;

    private Context context;

    //viewpager
    private int currentItem;
    private List<View> viewList = new ArrayList<>();

    /**
     * 导航栏
     */
    private ShopABar shopABar;

    private DialogManager dm;

    /**
     * 商品实体类
     */
    private Goods goods = new Goods();

    /**
     * 店铺推荐
     */
    private List<Goods> storeGoodsList = new ArrayList<>();

    /**
     * 商品详情页地址
     */
    private String webUrl;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        context = this;
        //获取商品详情
        getGoodsId();

        //上滑加载更多
        InitPull();

        //足迹
        InitFotoplace();
    }

    /**
     * 页面跳转
     *
     * @param context
     * @param goodsId
     */
    public static void startIntent(Context context, int goodsId) {
        Intent intent = new Intent(context, GoodsDetailsActivity.class);
        intent.putExtra("goodsId", goodsId);
        context.startActivity(intent);
    }

    /**
     * 获取商品id
     */
    private void getGoodsId() {
        Intent intent = getIntent();
        if (intent != null) {
            goodsId = intent.getIntExtra("goodsId", 0);

            //获取商品详情
            getdata();
        }
    }

    /**
     * 获取商品详情数据
     */
    private void getdata() {
        dm = new DialogManager(context);
        Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.getGoodsArticle, 0, false);
        jsonRequest.add("sign", UrlEncodeUtils.createSign(UrlUtils.getGoodsArticle));
        jsonRequest.add("id", goodsId);
        executeNetWork(jsonRequest, "请稍后");
        setCallback(new Callback() {
            @Override
            public void handle200Data(JSONObject dataObj, String message) {

                dataObj = dataObj.optJSONObject("mallgoods");

                //广告轮播数据
                getPagerList(dataObj);

                //解析商品详情
                getGoodsDetails(dataObj);

                //店铺推荐
                getStoreComm(dataObj);

                //详情页地址
                webUrl = dataObj.optString("details");

                if (goods.getGoodsTitle() != null && goods.getGoodsTitle().length() > 0) {
                    InitView();
                } else {
                    showToast("获取数据失败!");
                }

            }

            @Override
            public void handle404(String message) {
                dm.buildAlertDialog(message);
            }

            @Override
            public void handleNoNetWork() {
                dm.buildAlertDialog("请检查网络！");
            }
        });
    }


    /**
     * 初始化滑动详情控件
     */
    private void InitPull() {
        pullUpToLoadMoreView = (PullUpToLoadMoreView) findViewById(R.id.activity_goods_details_pv);

        /**
         * 获取当前控件位置
         */
        pullUpToLoadMoreView.setOnCurrPosition(new PullUpToLoadMoreView.OnCurrPosition() {
            @Override
            public void currPosition(int currPosition, boolean isTop) {

                if (currPosition == 0 && isTop) {
                    ptrLayout.setEnabled(true);
                } else {
                    ptrLayout.setEnabled(false);
                }
            }
        });
    }


    /**
     * 初始化足迹控件
     */
    private void InitFotoplace() {
        ptrLayout = (PtrFrameLayout) findViewById(R.id.activity_goods_details_pl);
        final TextView textView = new TextView(this);
        textView.setText("");
        ptrLayout.setHeaderView(textView);
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrLayout.refreshComplete();
                DropDownMultiPagerView dropDownMultiPagerView = new DropDownMultiPagerView(context, getList());
                dropDownMultiPagerView.show();
                dropDownMultiPagerView.setOnDropDownMultiPagerViewItemClick(new DropDownMultiPagerView.OnDropDownMultiPagerViewItemClick() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(context, "position:" + position, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 足迹数据
     *
     * @return
     */
    private List<GoodsDetailsItem> getList() {
        List<GoodsDetailsItem> beanList = new ArrayList<>();
        String url = "http://img5.ph.126.net/6NHiP2WgCjVnd42P3BWFeQ==/2612932208822079285.jpg";
        for (int i = 0; i < 6; i++) {
            GoodsDetailsItem bean = new GoodsDetailsItem(url, "Teenie Weenie小熊春季女装竖纹条纹衬", "￥ 398", "url地址：" + i);
            beanList.add(bean);
        }
        return beanList;
    }

    /**
     * 初始化UI
     */
    private void InitView() {

        /**
         * bar
         */
        shopABar = (ShopABar) findViewById(R.id.shopbar);
        shopABar.setImageOther(R.mipmap.pc_gouwuche);
        shopABar.setCallback(new ShopABar.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkMsg(View v) {
            ShopMessageActivity.startIntent(context);
            }

            @Override
            public void clkOther(View v) {

            }
        });

        /**
         * viewPager
         */
        InitViewPager();

        /**
         * 商品简单介绍
         *
         */
        //商品名
        TextView goodsDesc1 = (TextView) findViewById(R.id.goods_details_simple_desc_type_goods1);
        SpannableStringBuilder builder = new SpannableStringBuilder("【自营店】" + goods.getGoodsTitle());
        ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.shop_red_text_color));
        builder.setSpan(redSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        goodsDesc1.setText(builder);

        //商品价格
        TextView priceNow = (TextView) findViewById(R.id.goods_details_simple_desc_price_now);
        priceNow.setText("￥" + goods.getGoodsPrice());
        //市场价
        TextView priceOld = (TextView) findViewById(R.id.goods_details_simple_desc_price_old);
        priceOld.setText("￥" + goods.getGoodsPriceOld());
        priceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        /**
         * 售货服务
         * 24小时发货、7天包退换
         */
        String[] strings = new String[]{"24小时发货", "7天包退换", "退货补运费", "支付安全"};

        TextView afterSales1 = (TextView) findViewById(R.id.activity_goods_details_sales_service_text1);
        afterSales1.setText(strings[0]);

        TextView afterSales2 = (TextView) findViewById(R.id.activity_goods_details_sales_service_text2);
        afterSales2.setText(strings[1]);

        TextView afterSales3 = (TextView) findViewById(R.id.activity_goods_details_sales_service_text3);
        afterSales3.setText(strings[2]);

        TextView afterSales4 = (TextView) findViewById(R.id.activity_goods_details_sales_service_text4);
        afterSales4.setText(strings[3]);

        /**
         * 增值：购买送积分
         */
        TextView appreciationText = (TextView) findViewById(R.id.goods_details_appreciation_text);
        appreciationText.setText("购买即账户增值" + (Float.valueOf(goods.getGoodsPrice()) * 100) + "积分");
        RelativeLayout appreciationRL = (RelativeLayout) findViewById(R.id.goods_details_appreciation_rl);
        appreciationRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IntegralValueActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 规格选择
         */
        TextView chooseText = (TextView) findViewById(R.id.goods_details_choose_text);
        chooseText.setText("选择 颜色尺码");

        RelativeLayout chooseRL = (RelativeLayout) findViewById(R.id.goods_details_choose_rl);
        chooseRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /**
         * 宝贝评价、好评率
         */
        TextView evaluateText2 = (TextView) findViewById(R.id.goods_details_evaluate_text2);
        evaluateText2.setText("(126)");

        TextView evaluateText3 = (TextView) findViewById(R.id.goods_details_evaluate_text3);
        evaluateText3.setText("94.7%");

        RelativeLayout evaluateRL = (RelativeLayout) findViewById(R.id.goods_details_evaluate_rl);
        evaluateRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /**
         * 宝贝评价流布局
         */
        InitFlowLayout();

        /**
         * 买家评价示例
         */
        List<String> imgList = new ArrayList<>();
        imgList.add("http://image18-c.poco.cn/mypoco/myphoto/20170303/17/18505011120170303174057035_640.jpg");
        imgList.add("http://image18-c.poco.cn/mypoco/myphoto/20170303/17/18505011120170303174118033_640.jpg");

        TextView evFirstUsername = (TextView) findViewById(R.id.goods_details_ev_first_username);
        evFirstUsername.setText("离**人");

        TextView evFirstTime = (TextView) findViewById(R.id.goods_details_ev_first_time);
        evFirstTime.setText("2017.02.17");

        TextView evFirstDesc = (TextView) findViewById(R.id.goods_details_ev_first_desc);
        evFirstDesc.setText("鞋子很好，走起路来很舒服");

        TextView evFirstChoose = (TextView) findViewById(R.id.goods_details_ev_first_choose);
        evFirstChoose.setText("颜色：蓝白 尺寸：40");

        ImageView evFirstImg1 = (ImageView) findViewById(R.id.goods_details_ev_first_img1);
        Glide.with(context).load(imgList.get(0)).into(evFirstImg1);

        ImageView evFirstImg2 = (ImageView) findViewById(R.id.goods_details_ev_first_img2);
        Glide.with(context).load(imgList.get(1)).into(evFirstImg2);

        //查看全部
        TextView evFirstBtn = (TextView) findViewById(R.id.goods_details_ev_first_btn);
        evFirstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AllEvaluationActivity.class);
                startActivity(intent);
            }
        });

        /**
         *店铺
         */

        //logo
        String logo = "http://image18-c.poco.cn/mypoco/myphoto/20170303/17/18505011120170303175927036_640.jpg";
        ImageView imgLogo = (ImageView) findViewById(R.id.goods_details_shop_img_logo);
        Glide.with(context).load(logo).into(imgLogo);

        //店名
        TextView shopName = (TextView) findViewById(R.id.goods_details_shop_text_name);
        shopName.setText("New Balance专卖店");

        //全部宝贝
        TextView shopAll = (TextView) findViewById(R.id.goods_details_shop_text_all);
        shopAll.setText(String.valueOf(102));

        //大人推荐
        TextView shopRecomm = (TextView) findViewById(R.id.goods_details_shop_img_recomm);
        shopRecomm.setText(String.valueOf(18));

        //描述相符
        TextView shopDesc = (TextView) findViewById(R.id.goods_details_shop_text_desc);
        shopDesc.setText(String.valueOf(4.8));

        //服务态度
        TextView shopmService = (TextView) findViewById(R.id.goods_details_shop_img_service);
        shopmService.setText(String.valueOf(4.8));

        //发货速度
        TextView shopSpeed = (TextView) findViewById(R.id.goods_details_shop_text_speed);
        shopSpeed.setText(String.valueOf(4.8));

        /**
         * 商品1
         */
        Goods goods1 = storeGoodsList.get(0);
        ImageView imageView1 = (ImageView) findViewById(R.id.goods_details_shop_img1);
        Glide.with(context).load(goods1.getImgUrl()).into(imageView1);
        //商城价
        TextView goods1price = (TextView) findViewById(R.id.goods_details_shop_img1_price);
        goods1price.setText("￥" + goods1.getGoodsPrice());
        //市场价
        TextView goods1PriceOld = (TextView) findViewById(R.id.goods_details_shop_img1_price_old);
        goods1PriceOld.setText("￥" + goods1.getGoodsPriceOld());
        goods1PriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        /**
         * 商品2
         */
        Goods goods2 = storeGoodsList.get(1);
        ImageView imageView2 = (ImageView) findViewById(R.id.goods_details_shop_img2);
        Glide.with(context).load(goods2.getImgUrl()).into(imageView2);
        //商城价
        TextView goods2price = (TextView) findViewById(R.id.goods_details_shop_img2_price);
        goods2price.setText("￥" + goods2.getGoodsPrice());
        //市场价
        TextView goods2PriceOld = (TextView) findViewById(R.id.goods_details_shop_img2_price_old);
        goods2PriceOld.setText("￥" + goods2.getGoodsPriceOld());
        goods2PriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


        /**
         * 商品3
         */
        Goods goods3 = storeGoodsList.get(2);
        ImageView imageView3 = (ImageView) findViewById(R.id.goods_details_shop_img3);
        Glide.with(context).load(goods3.getImgUrl()).into(imageView3);
        //商城价
        TextView goods3price = (TextView) findViewById(R.id.goods_details_shop_img3_price);
        goods3price.setText("￥" + goods3.getGoodsPrice());
        //市场价
        TextView goods3PriceOld = (TextView) findViewById(R.id.goods_details_shop_img3_price_old);
        goods3PriceOld.setText("￥" + goods3.getGoodsPriceOld());
        goods3PriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


        //进店看看
        TextView shopEnter = (TextView) findViewById(R.id.goods_details_shop_img_enter);
        shopEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusinessShopActivity.class);
                startActivity(intent);
            }
        });

        initWebView();
    }

    /**
     * 初始化Viewpager
     */
    private void InitViewPager() {
        /**
         * 商品轮播展示
         */
        List<String> url = new ArrayList<>();

        for (int i = 0; i < goods.getImgBeanList().size(); i++) {
            url.add(goods.getImgBeanList().get(i).getImgUrl());
        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.goods_details_viewpager_vp);

        for (int i = 1; i <= url.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.goods_details_viewpager_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.goods_details_viewpager_item_img);
            TextView textView = (TextView) view.findViewById(R.id.goods_details_viewpager_item_text);
            Glide.with(context).load(url.get(i - 1)).placeholder(R.mipmap.loading_rect).error(R.mipmap.loading_rect).into(imageView);
            textView.setText(i + "/" + (url.size()));
            viewList.add(view);
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(viewList);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;//获取位置，即第几页
                Log.i("Guide", "监听改变" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            float startX;
            float startY;
            float endX;
            float endY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        endY = event.getY();
                        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                        //获取屏幕的宽度
                        Point size = new Point();
                        windowManager.getDefaultDisplay().getSize(size);
                        int width = size.x;
                        //首先要确定的是，是否到了最后一页，然后判断是否向左滑动，并且滑动距离是否符合，我这里的判断距离是屏幕宽度的4分之一（这里可以适当控制）
                        if (currentItem == (viewList.size() - 1) && startX - endX > 0 && startX - endX >= (width / 4)) {
                            pullUpToLoadMoreView.scrollToSecond();
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 初始化评价流布局
     */
    private void InitFlowLayout() {
        final String[] flowStrings = new String[]{"有图(435)", "追评(79)", "划算(105)", "物流快(319)", "鞋很舒服(70)", "颜色好(9)"};

        final TagFlowLayout flowLayout = (TagFlowLayout) findViewById(R.id.goods_details_flowview);
        final LayoutInflater mInflater = LayoutInflater.from(context);
        flowLayout.setAdapter(new TagAdapter<String>(flowStrings) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.goods_details_flow_item,
                        flowLayout, false);
                tv.setText(s);
                return tv;
            }
        });
        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Toast.makeText(context, flowStrings[position], Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    /**
     * 解析广告轮播数据
     *
     * @param dataObj
     */
    private void getPagerList(JSONObject dataObj) {
        List<GoodsImgBean> imgBeanList = new ArrayList<>();
        JSONArray pagerArray = dataObj.optJSONArray("mallGoodsImg");
        if (pagerArray != null && pagerArray.length() > 0) {
            for (int i = 0; i < pagerArray.length(); i++) {
                try {
                    JSONObject jsonObject = pagerArray.getJSONObject(i);
                    GoodsImgBean imgBean = new GoodsImgBean();
                    imgBean.setId(jsonObject.optInt("id"));
                    imgBean.setImgUrl(UrlUtils.baseWebsite + jsonObject.optString("img_url"));
                    imgBeanList.add(imgBean);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            goods.setImgBeanList(imgBeanList);
        }
    }

    /**
     * 解析商品详情
     *
     * @param goodsObject
     */
    private void getGoodsDetails(JSONObject goodsObject) {
        //商品id
        goods.setTitleId(goodsObject.optInt("id"));
        //商品名
        goods.setGoodsTitle(goodsObject.optString("title"));
        //商品编号
        goods.setGoodsNumber(goodsObject.optString("goods_number"));
        //店铺id
        goods.setStoreId(goodsObject.optInt("store_id"));
        //商品缩略图
        goods.setImgUrl(UrlUtils.baseWebsite + "/" + goodsObject.optString("img"));
        //品牌id
        goods.setBrand(goodsObject.optInt("brand"));
        //价格
        goods.setGoodsPrice(goodsObject.optString("shop_price"));
        //市场价
        goods.setGoodsPriceOld(goodsObject.optString("market_price"));
        //总库存
        goods.setInventory(goodsObject.optInt("inventory"));
        //重量g
        goods.setWeight(goodsObject.optInt("weight"));
        //运费
        goods.setFreightPrice(goodsObject.optInt("freight_price"));
    }

    /**
     * 解析店铺推荐
     *
     * @param goodsObject
     */
    private void getStoreComm(JSONObject goodsObject) {
        JSONArray jsonArray = goodsObject.optJSONArray("other_goods");
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Goods goods = new Goods();
                goods.setTitleId(jsonObject.optInt("id"));
                goods.setImgUrl(UrlUtils.baseWebsite + jsonObject.optString("img"));
                goods.setGoodsTitle(jsonObject.optString("title"));
                goods.setGoodsPrice(jsonObject.optString("shop_price"));
                goods.setGoodsPriceOld(jsonObject.optString("market_price"));
                storeGoodsList.add(goods);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initWebView() {
        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(webUrl);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        //启用支持javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (pullUpToLoadMoreView.getCurrPosition() == 1) {
                pullUpToLoadMoreView.scrollToTop();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
