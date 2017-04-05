package com.ascba.rebate.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.IntegralValueAdapter;
import com.ascba.rebate.adapter.ProfileAdapter;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.beans.GoodsAttr;
import com.ascba.rebate.beans.GoodsDetailsItem;
import com.ascba.rebate.beans.GoodsImgBean;
import com.ascba.rebate.beans.IntegralValueItem;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ImageViewDialog;
import com.ascba.rebate.view.ShopABar;
import com.ascba.rebate.view.dropDownMultiPager.DropDownMultiPagerView;
import com.ascba.rebate.view.dropDownMultiPager.ultraPullToRefash.component.PtrFrameLayout;
import com.ascba.rebate.view.dropDownMultiPager.ultraPullToRefash.handler.PtrDefaultHandler;
import com.ascba.rebate.view.pullUpToLoadMoreView.PullUpToLoadMoreView;
import com.squareup.picasso.Picasso;
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

    /**
     * 商品轮播展示
     */
    List<String> url;

    private TextView btnCart, btnBuy;

    /**
     * 商品展示图轮播
     */
    private ImageAdapter imageAdapter;
    private ViewPager viewPager;
    private int store_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        context = this;
        initView();
        //获取商品详情
        getGoodsId();

        //上滑加载更多
        InitPull();

        //足迹
        InitFotoplace();
    }

    private void initView() {
        btnCart = (TextView) findViewById(R.id.btn_cart);
        btnCart.setOnClickListener(this);
        btnBuy = (TextView) findViewById(R.id.btn_buy);
        btnBuy.setOnClickListener(this);

        findViewById(R.id.det_tv_shop).setOnClickListener(this);
        findViewById(R.id.det_tv_phone).setOnClickListener(this);
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
        shopABar.setImageOther(R.mipmap.icon_cart_black);
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
                showIntegralValue();
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
        Picasso.with(context).load(imgList.get(0)).into(evFirstImg1);

        ImageView evFirstImg2 = (ImageView) findViewById(R.id.goods_details_ev_first_img2);
        Picasso.with(context).load(imgList.get(1)).into(evFirstImg2);

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
        Picasso.with(context).load(logo).into(imgLogo);

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
        Picasso.with(context).load(goods1.getImgUrl()).into(imageView1);
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
        Picasso.with(context).load(goods2.getImgUrl()).into(imageView2);
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
        Picasso.with(context).load(goods3.getImgUrl()).into(imageView3);
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
                intent.putExtra("store_id", store_id);
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
        url = new ArrayList<>();

        for (int i = 0; i < goods.getImgBeanList().size(); i++) {
            url.add(goods.getImgBeanList().get(i).getImgUrl());
        }
        viewPager = (ViewPager) findViewById(R.id.goods_details_viewpager_vp);

        for (int i = 1; i <= url.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.goods_details_viewpager_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.goods_details_viewpager_item_img);
            TextView textView = (TextView) view.findViewById(R.id.goods_details_viewpager_item_text);
            Picasso.with(context).load(url.get(i - 1)).placeholder(R.mipmap.loading_rect).error(R.mipmap.loading_rect).into(imageView);
            textView.setText(i + "/" + (url.size()));
            viewList.add(view);
            /**
             * 点击查看大图
             */
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ImageViewDialog(context, url);
                }
            });
        }
        viewPager.setAdapter(imageAdapter = new ImageAdapter());
        viewPager.addOnPageChangeListener(new ViewPagerOnPageChangeListener());
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
            case R.id.btn_cart:
                /**
                 * 加入购物车
                 */
                break;
            case R.id.btn_buy:
                /**
                 * 立即购买
                 */
                showDialog();
                break;
            case R.id.det_tv_shop://进入店铺
                Intent intent = new Intent(this, BusinessShopActivity.class);
                intent.putExtra("store_id", store_id);
                startActivity(intent);
                break;
            case R.id.det_tv_phone://打电话
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse("tel:15206292150"));
                startActivity(intent1);
                break;
        }
    }

    /**
     * 解析广告轮播数据
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
        store_id = goodsObject.optInt("store_id");
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

    /**
     * 加载商品详情页
     */
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

    /**
     * 详情页点击返回首页
     *
     * @param keyCode
     * @param event
     * @return
     */
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

    //=======================商品详情轮播====================
    public class ViewPagerOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int currPosition = 0; // 当前滑动到了哪一页
        boolean canJump = false;
        boolean canLeft = true;

        boolean isObjAnmatitor = true;
        boolean isObjAnmatitor2 = false;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (position == (viewList.size() - 1)) {
                if (positionOffset > 0.35) {
                    canJump = true;
                    if (imageAdapter.arrowImage != null && imageAdapter.slideText != null) {
                        if (isObjAnmatitor) {
                            isObjAnmatitor = false;
                            ObjectAnimator animator = ObjectAnimator.ofFloat(imageAdapter.arrowImage, "rotation", 0f, 180f);
                            animator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    imageAdapter.slideText.setText("松开跳到详情");
                                    isObjAnmatitor2 = true;
                                }
                            });
                            animator.setDuration(500).start();
                        }
                    }
                } else if (positionOffset <= 0.35 && positionOffset > 0) {
                    canJump = false;
                    if (imageAdapter.arrowImage != null && imageAdapter.slideText != null) {
                        if (isObjAnmatitor2) {
                            isObjAnmatitor2 = false;
                            ObjectAnimator animator = ObjectAnimator.ofFloat(imageAdapter.arrowImage, "rotation", 180f, 360f);
                            animator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    imageAdapter.slideText.setText("继续滑动跳到详情");
                                    isObjAnmatitor = true;
                                }
                            });
                            animator.setDuration(500).start();
                        }
                    }
                }
                canLeft = false;
            } else {
                canLeft = true;
            }
        }

        @Override
        public void onPageSelected(int position) {
            currPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

            if (currPosition == (viewList.size() - 1) && !canLeft) {
                if (state == ViewPager.SCROLL_STATE_SETTLING) {

                    if (canJump) {
                        pullUpToLoadMoreView.scrollToSecond();
                    }

                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            // 在handler里调用setCurrentItem才有效
                            viewPager.setCurrentItem(viewList.size() - 1);
                        }
                    });
                }
            }
        }
    }

    public class ImageAdapter extends PagerAdapter {

        private TextView slideText;
        private ImageView arrowImage;

        @Override
        public int getCount() {
            return viewList.size() + 1; // 这里要加1，是因为多了一个隐藏的view
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (position < viewList.size()) {

                container.addView(viewList.get(position));
                return viewList.get(position);
            } else {
                View hintView = LayoutInflater.from(container.getContext()).inflate(R.layout.more_view, container, false);

                slideText = (TextView) hintView.findViewById(R.id.tv);
                arrowImage = (ImageView) hintView.findViewById(R.id.iv);

                container.addView(hintView);
                return hintView;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    //=======================商品详情轮播结束====================

    /**
     * 规格选择
     */
    private void showDialog() {
        final Dialog dialog = new Dialog(this, R.style.AlertDialog);
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
        rvRule.setLayoutManager(new LinearLayoutManager(this));
        //添加尾部试图
        View view1 = getLayoutInflater().inflate(R.layout.num_btn_layout, null);
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

    /**
     * 增值积分dialog
     */
    private void showIntegralValue() {
        final Dialog dialog = new Dialog(this, R.style.AlertDialog);
        dialog.setContentView(R.layout.activity_integralvale);

        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.activity_integralvale_recyclerview);
        Button button = (Button) dialog.findViewById(R.id.activity_integralvale_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        IntegralValueAdapter integralValueAdapter = new IntegralValueAdapter(R.layout.integral_value_item, getData());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(integralValueAdapter);

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

    private List<IntegralValueItem> getData() {
        List<IntegralValueItem> data = new ArrayList<>();
        data.add(new IntegralValueItem("购买后送20积分", "购买后可获得20积分，会员等级越高购买商品送的积分越多"));
        data.add(new IntegralValueItem("积分有什么用", "在购买商品时，可使用积分抵扣一部分现金"));
        return data;
    }
}
