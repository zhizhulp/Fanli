package com.ascba.rebate.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.util.Log;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.shop.ShopActivity;
import com.ascba.rebate.adapter.FilterAdapter;
import com.ascba.rebate.adapter.IntegralValueAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.beans.GoodsAttr;
import com.ascba.rebate.beans.GoodsDetailsItem;
import com.ascba.rebate.beans.GoodsImgBean;
import com.ascba.rebate.beans.IntegralValueItem;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.utils.ViewUtils;
import com.ascba.rebate.view.ImageViewDialog;
import com.ascba.rebate.view.StdDialog;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.ascba.rebate.view.cart_btn.NumberButton;
import com.ascba.rebate.view.dropDownMultiPager.DropDownMultiPagerView;
import com.ascba.rebate.view.pullUpToLoadMoreView.PullUpToLoadMoreView;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.rest.Request;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/02 0002.
 * 商品详情页
 */
@SuppressLint("SetTextI18n")
public class GoodsDetailsActivity extends BaseNetActivity implements View.OnClickListener
        , BaseNetActivity.Callback {
    private static final int REQUEST_STD_LOGIN = 2016;
    private static final int REQUEST_ADD_TO_CART_LOGIN = 2018;

    // 商品id
    private int goodsId;

    //足记控件
    private SuperSwipeRefreshLayout ptrLayout;

    //向上拖动查看详情
    private PullUpToLoadMoreView pullUpToLoadMoreView;

    //viewpager
    private List<View> viewList = new ArrayList<>();

    /*
     * 导航栏
     */
    private View shopABar;

    /*
     * 商品实体类
     */
    private Goods goods = new Goods();

    /*
     * 店铺推荐
     */
    private List<Goods> storeGoodsList = new ArrayList<>();

    /*
     * 商品详情页地址
     */
    private String webUrl;
    private WebView webView;

    /*
     * 商品轮播展示
     */
    List<String> url;

    private TextView btnCart, btnBuy;

    /*
     * 商品展示图轮播
     */
    private ImageAdapter imageAdapter;
    private ViewPager viewPager;
    private String phone;//客服电话

    private DecimalFormat fnum = new DecimalFormat("##0.00");//格式化，保留两位
    boolean footIsObjAnmatitor = true;
    boolean footIsObjAnmatitor2 = false;
    private int finalScene;

    private Goods goodsSelect;
    private boolean isAll;//是否可以加入购物车
    private StdDialog sd;
    private NumberButton nb;

    private ImageView imgLogo;//店铺logo
    private TextView shopName;//店名
    private TextView shopAll;//全部宝贝
    private TextView shopRecomm;//达人推荐

    private int indence;
    private int has_spec;//是否有规格
    private int operation;//1——加入购物车，2——立即购买
    private String attention = "请选择完整的商品规格";

    private TextView txDescNum, txDesc;//描述相符
    private TextView txServiceNum, txService;//服务态度
    private TextView txSpeedNum, txSpeed;//发货速度


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ((MyApplication) getApplication()).addActivity(this);

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

        /**
         *店铺
         */
        //logo
        String logo = "http://image18-c.poco.cn/mypoco/myphoto/20170303/17/18505011120170303175927036_640.jpg";
        imgLogo = (ImageView) findViewById(R.id.goods_details_shop_img_logo);
        Picasso.with(this).load(logo).into(imgLogo);


        //描述相符
        txDescNum = (TextView) findViewById(R.id.text_desc_num);
        txDesc = (TextView) findViewById(R.id.text_desc);

        //服务态度
        txServiceNum = (TextView) findViewById(R.id.text_service_num);
        txService = (TextView) findViewById(R.id.text_service);

        //发货速度
        txSpeedNum = (TextView) findViewById(R.id.text_speed_num);
        txSpeed = (TextView) findViewById(R.id.text_speed);

        //店名
        shopName = (TextView) findViewById(R.id.goods_details_shop_text_name);
        shopName.setText("New Balance专卖店");

        //全部宝贝
        shopAll = (TextView) findViewById(R.id.goods_details_shop_text_all);

        //大人推荐
        shopRecomm = (TextView) findViewById(R.id.goods_details_shop_img_recomm);

        shopABar = findViewById(R.id.shopbar);
        findViewById(R.id.abar_im_back).setOnClickListener(this);
        findViewById(R.id.abar_im_msg).setOnClickListener(this);
        findViewById(R.id.abar_im_cart).setOnClickListener(this);
    }

    /*
     * 页面跳转
     */
    public static void startIntent(Context context, int goodsId) {
        Intent intent = new Intent(context, GoodsDetailsActivity.class);
        intent.putExtra("goodsId", goodsId);
        context.startActivity(intent);
    }

    /*
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

    /*
     * 获取商品详情数据
     */
    private void getdata() {
        Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.getGoodsArticle, 0, false);
        jsonRequest.add("sign", UrlEncodeUtils.createSign(UrlUtils.getGoodsArticle));
        jsonRequest.add("id", goodsId);
        executeNetWork(jsonRequest, "请稍后");
        setCallback(new Callback() {

            @Override
            public void handle200Data(JSONObject dataObj, String message) {
                dataObj = dataObj.optJSONObject("mallgoods");
                //是否有规格
                has_spec = dataObj.optInt("has_spec");

                //广告轮播数据
                getPagerList(dataObj);

                //解析商品详情
                getGoodsDetails(dataObj);

                //店铺推荐
                getStoreComm(dataObj);

                //店铺
                getStore(dataObj);
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
                getDm().buildAlertDialog(message);
            }

            @Override
            public void handleNoNetWork() {

            }
        });
    }

    /*
        店铺推荐
     */
    private void getStore(JSONObject dataObj) {
        JSONObject obj = dataObj.optJSONObject("mallStore");
        String storeId = dataObj.optString("id");
        String store_name = obj.optString("store_name");
        String store_logo = UrlUtils.baseWebsite + obj.optString("store_logo");
        phone = obj.optString("store_mobile");
        int goods_count = obj.optInt("goods_count");
        Picasso.with(this).load(store_logo).placeholder(R.mipmap.busi_loading).into(imgLogo);
        shopName.setText(store_name);
        shopAll.setText(String.valueOf(goods_count));
        shopRecomm.setText(String.valueOf(goods_count));

        //描述相符
        try {
            float desc = Float.parseFloat(obj.optString("store_desccredit"));
            txDescNum.setText(String.valueOf(desc));
            if (desc > 2.5) {
                txDesc.setText("高");
            } else {
                txDesc.setText("低");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //服务态度
        try {
            float service = Float.parseFloat(obj.optString("store_servicecredit"));
            txServiceNum.setText(String.valueOf(service));
            if (service > 3.0) {
                txService.setText("高");
            } else {
                txDesc.setText("低");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //发货速度
        try {
            float speed = Float.parseFloat(obj.optString("store_deliverycredit"));
            txSpeedNum.setText(String.valueOf(speed));
            if (speed > 3.0) {
                txSpeed.setText("高");
            } else {
                txSpeed.setText("低");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
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
                    shopABar.setBackgroundColor(Color.argb(0, 255, 255, 255));
                } else {
                    ptrLayout.setEnabled(false);
                }

                if (currPosition == 0) {
                    shopABar.setVisibility(View.VISIBLE);
                } else {
                    shopABar.setVisibility(View.GONE);
                }
            }

            @Override
            public void topScrollView(int scrollY) {
                indence = +scrollY;
                //toolbar的高度
                int toolbarHeight = shopABar.getBottom();
                float maxAlpha = 229.5f;//最大透明度80%
                //当滑动的距离 <= toolbar高度的时候，改变Toolbar背景色的透明度，达到渐变的效果
                if (indence > 0 && indence <= toolbarHeight) {
                    float scale = (float) indence / toolbarHeight;
                    float alpha = scale * maxAlpha;
                    shopABar.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                }
            }
        });
    }


    /*
     * 初始化足迹控件
     */
    private void InitFotoplace() {
        ptrLayout = (SuperSwipeRefreshLayout) findViewById(R.id.activity_goods_details_pl);
        final View header = LayoutInflater.from(this).inflate(R.layout.ptrlayout_headview, null);
        final TextView textView = (TextView) header.findViewById(R.id.tv);
        final ImageView imageView = (ImageView) header.findViewById(R.id.img);
        ptrLayout.setHeaderView(header);
        ptrLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                requestNetwork(UrlUtils.history,2);

            }

            @Override
            public void onPullDistance(int distance) {

                if (distance > 0) {
                    shopABar.setVisibility(View.GONE);
                } else {
                    shopABar.setVisibility(View.VISIBLE);
                }

                if (distance > (header.getHeight() + 10)) {
                    if (footIsObjAnmatitor) {
                        footIsObjAnmatitor = false;
                        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 180f);
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                textView.setText("释放查看更多精彩");
                                footIsObjAnmatitor2 = true;
                            }
                        });
                        animator.setDuration(500).start();
                    }
                } else if (footIsObjAnmatitor2) {
                    footIsObjAnmatitor2 = false;
                    ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 180f, 360f);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            textView.setText("下拉查看更多精彩");
                            footIsObjAnmatitor = true;
                        }
                    });
                    animator.setDuration(500).start();
                }
            }

            @Override
            public void onPullEnable(boolean enable) {
            }
        });
    }

    /*
     * 足迹数据
     */
    private List<GoodsDetailsItem> getList(JSONObject dataObj) {
        JSONArray array = dataObj.optJSONArray("history");
        if(array!=null && array.length()!=0){
            List<GoodsDetailsItem> beanList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.optJSONObject(i);
                int id = object.optInt("id");
                String title = object.optString("title");
                String img = UrlUtils.baseWebsite + object.optString("img");
                String shop_price = object.optString("shop_price");
                GoodsDetailsItem bean = new GoodsDetailsItem(id,img, title, "￥"+shop_price, null);
                beanList.add(bean);
            }
            return beanList;
        }
        return null;
    }

    /*
     * 初始化UI
     */
    private void InitView() {

        /**
         * viewPager
         */
        InitViewPager();

        /**
         * 商品简单介绍
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
        String goodsPriceOld = goods.getGoodsPriceOld();
        float v = Float.parseFloat(goodsPriceOld);
        if(v==0){
            priceOld.setVisibility(View.GONE);
        }else {
            priceOld.setVisibility(View.VISIBLE);
            priceOld.setText("￥" + goods.getGoodsPriceOld());
        }

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
        appreciationText.setText("购买即账户增值" + fnum.format(Float.valueOf(goods.getGoodsPrice()) * 100) + "积分");
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
        chooseText.setText("规格");

        RelativeLayout chooseRL = (RelativeLayout) findViewById(R.id.goods_details_choose_rl);
        chooseRL.setOnClickListener(this);
        chooseRL.setVisibility(View.GONE);

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
        Picasso.with(this).load(imgList.get(0)).into(evFirstImg1);

        ImageView evFirstImg2 = (ImageView) findViewById(R.id.goods_details_ev_first_img2);
        Picasso.with(this).load(imgList.get(1)).into(evFirstImg2);

        //查看全部
        TextView evFirstBtn = (TextView) findViewById(R.id.goods_details_ev_first_btn);
        evFirstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodsDetailsActivity.this, AllEvaluationActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 商品1
         */
        Goods goods1 = storeGoodsList.get(0);
        ImageView imageView1 = (ImageView) findViewById(R.id.goods_details_shop_img1);
        Picasso.with(this).load(goods1.getImgUrl()).into(imageView1);
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
        Picasso.with(this).load(goods2.getImgUrl()).into(imageView2);
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
        Picasso.with(this).load(goods3.getImgUrl()).into(imageView3);
        //商城价
        TextView goods3price = (TextView) findViewById(R.id.goods_details_shop_img3_price);
        goods3price.setText("￥" + goods3.getGoodsPrice());
        //市场价
        TextView goods3PriceOld = (TextView) findViewById(R.id.goods_details_shop_img3_price_old);
        goods3PriceOld.setText("￥" + goods3.getGoodsPriceOld());
        goods3PriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


        //进店看看
        FrameLayout shopEnter = (FrameLayout) findViewById(R.id.goods_details_shop_img_enter);
        shopEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodsDetailsActivity.this, BusinessShopActivity.class);
                intent.putExtra("store_id", goods.getStoreId());
                startActivity(intent);
            }
        });

        initWebView();
    }

    /*
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
            View view = LayoutInflater.from(this).inflate(R.layout.goods_details_viewpager_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.goods_details_viewpager_item_img);
            TextView textView = (TextView) view.findViewById(R.id.goods_details_viewpager_item_text);
            Picasso.with(this).load(url.get(i - 1)).placeholder(R.mipmap.loading_rect).error(R.mipmap.loading_rect).into(imageView);
            textView.setText(i + "/" + (url.size()));
            viewList.add(view);
            /**
             * 点击查看大图
             */
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ImageViewDialog(GoodsDetailsActivity.this, url);
                }
            });
        }
        viewPager.setAdapter(imageAdapter = new ImageAdapter());
        viewPager.addOnPageChangeListener(new ViewPagerOnPageChangeListener());
    }

    /*
     * 初始化评价流布局
     */
    private void InitFlowLayout() {
        final String[] flowStrings = new String[]{"有图(435)", "追评(79)", "划算(105)", "物流快(319)", "鞋很舒服(70)", "颜色好(9)"};

        final TagFlowLayout flowLayout = (TagFlowLayout) findViewById(R.id.goods_details_flowview);
        final LayoutInflater mInflater = LayoutInflater.from(this);
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
                Toast.makeText(GoodsDetailsActivity.this, flowStrings[position], Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cart:
                //加入购物车
                if (AppConfig.getInstance().getInt("uuid", -1000) != -1000) {
                    operation = 1;
                    selectSpecification();
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent, REQUEST_STD_LOGIN);
                }
                break;
            case R.id.btn_buy:
                //立即购买
                if (AppConfig.getInstance().getInt("uuid", -1000) != -1000) {
                    operation = 2;
                    selectSpecification();
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent, REQUEST_STD_LOGIN);
                }
                break;
            case R.id.det_tv_shop://进入店铺
                Intent intent = new Intent(this, BusinessShopActivity.class);
                intent.putExtra("store_id", goods.getStoreId());
                startActivity(intent);
                break;
            case R.id.det_tv_phone://打电话
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse("tel:" + phone));
                startActivity(intent1);
                break;
            case R.id.goods_details_choose_rl:
                //规格选择
                if (AppConfig.getInstance().getInt("uuid", -1000) != -1000) {
                    selectSpecification();
                } else {
                    Intent intent2 = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent2, REQUEST_STD_LOGIN);
                }
                break;

            case R.id.abar_im_back:
                finish();
                break;
            case R.id.abar_im_msg:
                ShopMessageActivity.startIntent(this);
                break;
            case R.id.abar_im_cart:
                //购物车
                ShopActivity.setIndex(ShopActivity.CART);
                startActivity(new Intent(this, ShopActivity.class));
                break;
        }
    }

    /*
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

    /*
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
        goods.setFreightPrice(fnum.format(goodsObject.optInt("freight_price")));
        //是否有规格
        goods.setHasStandard(goodsObject.optInt("has_spec") == 1);
    }

    /*
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

    /*
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

    /*
     * 详情页点击返回首页
     *
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


    /*
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

    /**
     * 获取规格数据
     */
    private void selectSpecification() {
        if (has_spec == 0) {//无规格
            switch (operation) {
                case 1:
                    //加入购物车
                    requestNetwork(UrlUtils.cartAddGoods, 0);
                    break;
                case 2:
                    //立即购买
                    requestNetwork(UrlUtils.goodsCheckout, 1);
                    break;
            }

        } else {//有规格
            Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.getGoodsSpec, 0, true);
            jsonRequest.add("goods_id", goodsId);
            executeNetWork(jsonRequest, "请稍后");
            setCallback(new Callback() {
                @Override
                public void handle200Data(JSONObject dataObj, String message) {
                    Log.d("GoodsDetailsActivity", dataObj.toString());
                    JSONArray filter_spec = dataObj.optJSONArray("filter_spec");

                    JSONArray array = dataObj.optJSONArray("spec_goods_price");

                    JSONObject goodsInfo = dataObj.optJSONObject("goods_info");
                    showStandardDialog(parseFilterSpec(filter_spec), parseSpecGoodsPrice(array),parseDefaultGoods(goodsInfo));
                }

                @Override
                public void handle404(String message) {
                    getDm().buildAlertDialog(message);
                }

                @Override
                public void handleNoNetWork() {
                    getDm().buildAlertDialog(getString(R.string.no_network));
                }
            });
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

    private List<Goods> parseSpecGoodsPrice(JSONArray array) {
        List<Goods> goodses = new ArrayList<Goods>();
        if (array != null && array.length() != 0) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                int id = obj.optInt("id");//商品规格id
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

    //商品规格选择
    private void showStandardDialog(List<GoodsAttr> gas, List<Goods> goodses,Goods defaultGoods) {
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
                isAll = false;
            }
        });
        nb = sd.getNb();
        //加入购物车
        sd.getTvAddToCart().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAll) {//选择了完整的规格
                    if (AppConfig.getInstance().getInt("uuid", -1000) != -1000) {
                        requestNetwork(UrlUtils.cartAddGoods, 0);
                    } else {
                        Intent intent = new Intent(GoodsDetailsActivity.this, LoginActivity.class);
                        startActivityForResult(intent, REQUEST_ADD_TO_CART_LOGIN);
                    }
                } else {
                    getDm().buildAlertDialog("请选择商品");
                }

            }
        });
        //立即购买
        sd.getTvPurchase().setOnClickListener(new View.OnClickListener() {//点击立即购买
            @Override
            public void onClick(View v) {
                if (isAll) {
                    requestNetwork(UrlUtils.goodsCheckout, 1);
                } else {
                    getDm().buildAlertDialog("请先选择商品");
                }
            }
        });
        //监听规格选择
        sd.setListener(new StdDialog.Listener() {
            @Override
            public void getSelectGoods(Goods gs) {
                goodsSelect = gs;
                nb.setInventory(goodsSelect.getInventory());
            }

            @Override
            public void isSelectAll(boolean isAll) {
                GoodsDetailsActivity.this.isAll = isAll;
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
                if (!isAll) {
                    getDm().buildAlertDialog(attention);
                } else {
                    nb.setCurrentNumber(nb.getNumber() - 1);
                }
            }
        });

        sd.showMyDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case REQUEST_STD_LOGIN:
                    if (resultCode == RESULT_OK) {
                        selectSpecification();
                    }
                    break;
                case REQUEST_ADD_TO_CART_LOGIN:
                    if (resultCode == RESULT_OK) {
                        requestNetwork(UrlUtils.cartAddGoods, 0);
                    }
                    break;
            }
        }

    }

    private void requestNetwork(String url, int scene) {
        finalScene = scene;
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        if (scene == 0) {//把商品加入购物车
            request.add("store_id", goods.getStoreId());
            request.add("goods_id", has_spec == 0 ? goods.getTitleId() : goodsSelect.getTitleId());
            request.add("goods_num", has_spec == 0 ? 1 : nb.getNumber());
            request.add("goods_spec_id", has_spec == 0 ? null : goodsSelect.getCartId());
        } else if (scene == 1) {//结算
            request.add("goods_id", has_spec == 0 ? goods.getTitleId() : goodsSelect.getTitleId());
            request.add("goods_num", has_spec == 0 ? 1 : nb.getNumber());
            request.add("goods_spec_id", has_spec == 0 ? null : goodsSelect.getCartId());
        }
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        stopRefresh();
        if (finalScene == 0) {
            if (sd != null) {
                sd.dismiss();
                isAll = false;
            }
            MyApplication.isLoadCartData=true;
            ViewUtils.showMyToast(this,R.layout.add_to_cart_toast);
        } else if (finalScene == 1) {
            if (sd != null) {
                sd.dismiss();
                isAll = false;
            }
            Intent intent = new Intent(this, ConfirmBuyOrderActivity.class);
            intent.putExtra("json_data", dataObj.toString());
            startActivity(intent);
            finish();
        } else if(finalScene==2){
            ptrLayout.setRefreshing(false);
            final List<GoodsDetailsItem> list = getList(dataObj);
            if(list !=null){
                final DropDownMultiPagerView dropDownMultiPagerView = new DropDownMultiPagerView(this,list );
                dropDownMultiPagerView.show();
                dropDownMultiPagerView.setOnDropDownMultiPagerViewItemClick(new DropDownMultiPagerView.OnDropDownMultiPagerViewItemClick() {
                    @Override
                    public void onItemClick(int position) {
                        dropDownMultiPagerView.dismiss();
                        GoodsDetailsActivity.startIntent(GoodsDetailsActivity.this,list.get(position).getId());
                    }
                });
            }

        }
    }

    @Override
    public void handle404(String message) {
        getDm().buildAlertDialog(message);
        stopRefresh();
    }

    @Override
    public void handleNoNetWork() {
        stopRefresh();
    }

    @Override
    protected void mhandleReLogin(int what) {
        super.mhandleReLogin(what);
        stopRefresh();
    }

    private void stopRefresh(){
        if(ptrLayout!=null && ptrLayout.isRefreshing()){
            ptrLayout.setRefreshing(false);
        }
    }
}
