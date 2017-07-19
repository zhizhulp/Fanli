package com.ascba.rebate.activities.auction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.ImageAdapter;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.utils.NumberFormatUtils;
import com.ascba.rebate.utils.TimeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 李鹏 on 2017/5/24.
 * 拍卖详情详情界面
 */

public class AuctionDetailsActivity extends BaseNetActivity {

    private static final int REDUCE_TIME = 3;
    private static final int REQUEST_PAY_DEPOSIT = 2;
    private AcutionGoodsBean agb;
    private TextView tvStatus;
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvOrgPrice;
    private TextView tvScore;
    //private TextView tvGoodsDet;
    private TextView tvTD;
    private TextView tvTDOver;
    private TextView tvStartPrice;
    private TextView tvGapPrice;
    private TextView tvGapTime;
    private TextView tvCount;
    private Timer timer;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REDUCE_TIME:
                    setBeanProperty();
                    break;
            }
        }
    };
    private List<String> urls;
    private ImageAdapter imageAdapter;
    private View viewGoingNoSureMoney;
    private TextView tvSureMoney;
    private View viewGoingSureMoney;
    private TextView tvPriceRush;
    private View viewGoingSureMoneyBlind;
    private TextView tvPriceBlind;
    private TextView tvAuctionState;
    private TextView tvPersonNum;
    private TextView tvOtherPersonNum;
    private View viewTimeDown;
    private WebView webView;
    private View viewSomeParams;
    private int get_type;//0 非最低价进入详情  1：最低价进入详情
    private int id;//传来的id
    private TextView tvPriceTitle;
    private TextView tvRishingPersons;
    private View viewTopStatusBg;
    private View viewPayDeposite;
    private TextView tvOverTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_details);
        initUI();
        requestNetwork(UrlUtils.auctionArticle, 0);
    }

    private void initUI() {
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getFlag();
        initGoodsDetails();
        initStateLat();
        initViewPager();
        initWebview();
    }

    private void initWebview() {
        webView = (WebView) findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    private String getClientIds(AcutionGoodsBean selectAGB) {
        return "\"" +
                selectAGB.getId() +
                "\"" +
                ":" +
                "\"" +
                selectAGB.getCashDeposit() +
                "\"";
    }

    private void initStateLat() {
        //未开始
        //已开始 未交保证金
        viewGoingNoSureMoney = findViewById(R.id.lat_going_noSureMoney);
        tvSureMoney = ((TextView) findViewById(R.id.tv_sure_money));
        viewPayDeposite = findViewById(R.id.tv_apply_sure_money);
        viewPayDeposite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuctionDetailsActivity.this, PayDepositActivity.class);
                intent.putExtra("client_ids", getClientIds(agb));
                intent.putExtra("total_price", agb.getCashDeposit());
                startActivityForResult(intent, REQUEST_PAY_DEPOSIT);
            }
        });
        //已开始 已交保证金（抢拍）
        viewGoingSureMoney = findViewById(R.id.lat_going_SureMoney_rush);
        tvPriceRush = ((TextView) findViewById(R.id.tv_price_rush));
        findViewById(R.id.btn_pay_apply_rush).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNetwork(UrlUtils.payAuction, 1);
            }
        });
        //已开始 已交保证金（盲拍）
        viewGoingSureMoneyBlind = findViewById(R.id.lat_going_SureMoney_blind);
        tvPriceBlind = ((TextView) findViewById(R.id.tv_price_blind));
        findViewById(R.id.pay_btn_down).setOnClickListener(new View.OnClickListener() {//减
            @Override
            public void onClick(View v) {//current 280 end 200 gap 50
                if (agb.getPrice() < agb.getEndPrice() + agb.getGapPrice()) {
                    showToast("已经到最低价了");
                } else {
                    agb.setPrice(agb.getPrice() - agb.getGapPrice());
                    //tvPrice.setText(NumberFormatUtils.getNewDouble(agb.getPrice()) + "");
                    tvPriceBlind.setText("￥" + NumberFormatUtils.getNewDouble(agb.getPrice()));
                }
            }
        });
        findViewById(R.id.pay_btn_up).setOnClickListener(new View.OnClickListener() {//加
            @Override
            public void onClick(View v) {//current 200 start 280 gap 40
                if (agb.getPrice() > agb.getStartPrice() - agb.getGapPrice()) {
                    showToast("已经到最高价了");
                } else {
                    agb.setPrice(agb.getPrice() + agb.getGapPrice());
                    //tvPrice.setText(NumberFormatUtils.getNewDouble(agb.getPrice()) + "");
                    tvPriceBlind.setText("￥" + NumberFormatUtils.getNewDouble(agb.getPrice()));
                }
            }
        });
        findViewById(R.id.btn_pay_apply_blind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNetwork(UrlUtils.payAuction, 1);
            }
        });
        //<!--结束 获拍--><!--结束 未获拍-->
        tvAuctionState = ((TextView) findViewById(R.id.tv_over_auction_state));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PAY_DEPOSIT && resultCode == RESULT_OK) {
            //setState(2, 2);
            requestNetwork(UrlUtils.auctionArticle, 0);
        }
    }

    //初始化Viewpager
    private void initViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_img);
        urls = new ArrayList<>();
        imageAdapter = new ImageAdapter(urls);
        viewPager.setAdapter(imageAdapter);
    }

    private void setBeanProperty() {
        int currentLeftTime = agb.getCurrentLeftTime();
        int reduceTimes = agb.getReduceTimes();
        Double price = agb.getPrice();
        if (currentLeftTime <= 0) {
            reduceTimes++;
            price -= agb.getGapPrice();//减价格
            currentLeftTime = agb.getGapTime();//重置时间
            agb.setPrice(price);
            agb.setReduceTimes(reduceTimes);
            if (agb.getIntState()!=3 && agb.getType() == 1 && get_type==0 && agb.getIntPriceState()!=11 && agb.getIntPriceState()!=10 && agb.getIntPriceState()!=5) {
                tvCount.setText("降价次数：" + reduceTimes + "次");
                tvPrice.setText(NumberFormatUtils.getNewDouble(price) + "");
                tvPriceBlind.setText("￥" + NumberFormatUtils.getNewDouble(price));
                tvPriceRush.setText("当前价￥" + NumberFormatUtils.getNewDouble(price));
            }
        } else {
            currentLeftTime--;
        }
        agb.setCurrentLeftTime(currentLeftTime);
        tvTD.setText(currentLeftTime + "s");
        tvTDOver.setText(getRemainingTime(agb));
    }

    private String getAutionIds() {
        return "\"" +
                agb.getId() +
                "\"" +
                ":" +
                "\"" +
                (get_type==1? agb.getEndPrice():agb.getPrice()) +
                "\"";
    }

    private void requestNetwork(String url, int what) {
        Request<JSONObject> request = null;
        if (what == 0) {
            request = buildNetRequest(url, 0, false);
            request.add("goods_id", id);
            request.add("get_type", get_type);
        } else if (what == 1) {
            request = buildNetRequest(url, 0, true);
            request.add("client_str", getAutionIds());
            request.add("total_price", get_type==1? agb.getEndPrice() : agb.getPrice());
        }
        executeNetWork(what, request, "请稍后");
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        if (what == 0) {
            refreshGoodsData(dataObj);
            refreshViewPagerData(dataObj);
        } else if (what == 1) {
            showToast(message);
            requestNetwork(UrlUtils.auctionArticle, 0);
        }
    }

    @Override
    protected void mhandleFailed(int what, Exception e) {
        super.mhandleFailed(what, e);
        if(what==0){
            finish();
        }
    }

    private void refreshViewPagerData(JSONObject dataObj) {
        JSONObject obj = dataObj.optJSONObject("auctionArticle");
        JSONArray photos = obj.optJSONArray("photos");
        if (photos != null && photos.length() != 0) {
            if (urls.size() > 0) {
                urls.clear();
            }
            for (int i = 0; i < photos.length(); i++) {
                String img = photos.optString(i);
                urls.add(UrlUtils.baseWebsite + img);
            }
        }
        imageAdapter.notifyDataSetChanged();
    }

    private void refreshGoodsData(JSONObject dataObj) {
        JSONObject obj = dataObj.optJSONObject("auctionArticle");
        int id = obj.optInt("id");
        this.id=id;
        int type = obj.optInt("type");
        String name = obj.optString("name");
        double transaction_price = obj.optDouble("transaction_price");//
        double reserve_money = obj.optDouble("reserve_money");//
        double win_price = obj.optDouble("win_price");
        String points = obj.optString("points");
        String cash_deposit = obj.optString("cash_deposit");
        int refresh_count = obj.optInt("refresh_count");
        double range = obj.optDouble("range");
        int depreciate_count = obj.optInt("depreciate_count");
        int count_down = obj.optInt("count_down");
        int interval_second = obj.optInt("interval_second");
        int status = obj.optInt("is_status");
        String auction_tip = obj.optString("is_status_tip");

        int cart_status = obj.optInt("cart_status");
        String cart_status_tip = obj.optString("cart_status_tip");

        double begin_price = obj.optDouble("begin_price");
        double end_price = obj.optDouble("end_price");
        long price_time = obj.optLong("price_time");
        long starttime = obj.optLong("starttime");
        long endtime = obj.optLong("endtime");

        setState(status, cart_status,type,transaction_price,reserve_money ,win_price,begin_price,end_price);
        this.agb = new AcutionGoodsBean(id, type, null,
                name, (cart_status==0 || cart_status==2)?transaction_price : reserve_money, points, cash_deposit, refresh_count);
        agb.setGapPrice(range);
        agb.setMaxReduceTimes(depreciate_count);
        agb.setCurrentLeftTime(count_down);
        agb.setGapTime(interval_second);
        agb.setIntState(status);
        agb.setStrState(auction_tip);
        agb.setIntPriceState(cart_status);
        agb.setStrPriceState(cart_status_tip);
        agb.setStartPrice(begin_price);
        agb.setEndPrice(end_price);
        agb.setStartTime(starttime);
        agb.setEndTime(endtime);
        agb.setGoodsEndTime(price_time);

        tvStatus.setText(auction_tip);
        tvName.setText(name);
        //CharSequence content = Html.fromHtml(obj.optString("content"), imageGetter, null);
        webView.loadDataWithBaseURL(null, obj.optString("content"), "text/html","UTF-8", null);
        //tvGoodsDet.setText(content);
        tvOverTime.setText("结束时间 "+ TimeUtils.milliseconds2String(endtime*1000));
        tvOrgPrice.setText("原价￥" + NumberFormatUtils.getNewDouble(begin_price));
        tvTD.setText(count_down + "s");
        tvScore.setText(points + "礼品分");
        tvStartPrice.setText("起拍价：￥" + NumberFormatUtils.getNewDouble(begin_price));
        tvGapTime.setText("延时周期：" + interval_second + "s/次");
        tvGapPrice.setText("降价幅度：￥" + range + "/次");
        tvCount.setText("降价次数：" + refresh_count + "次");

        tvTDOver.setText(getRemainingTime(agb));

        tvSureMoney.setText("保证金￥" + cash_deposit);
        tvPriceRush.setText((get_type==1?"最低价￥"+NumberFormatUtils.getNewDouble(end_price) :
                "当前价￥" +NumberFormatUtils.getNewDouble(transaction_price)));
        tvPriceBlind.setText("￥" + NumberFormatUtils.getNewDouble(transaction_price) + "");
        tvAuctionState.setText(cart_status_tip);
        tvPersonNum.setText((type==1?"抢拍：":"盲拍：") + obj.optInt("auction_people") + "人");
        tvOtherPersonNum.setText("围观：" + obj.optInt("flow") + "人");
        tvRishingPersons.setText("目前有"+obj.optInt("flow")+"人正要下手");
        if (timer == null && status==2 || status ==3 ) {
            timer = new Timer();
            timer.schedule(new MyTimerTask(), 0, 1000);
        }
    }

    /**
     * 计算倒计时时间
     *
     * @param item 商品
     * @return 倒计时时间
     */
    private String getRemainingTime(AcutionGoodsBean item) {
        int state = item.getIntState();
        if(state==3){//即将开始
            int leftTime = (int) (item.getStartTime()-System.currentTimeMillis() / 1000);
            int hour = leftTime % (24 * 3600) / 3600;
            int minute = leftTime % 3600 / 60;
            int second = leftTime % 60;
            return hour + "小时" + minute + "分钟" + second + "秒 后开始";
        }else {
            int leftTime = (int) (item.getEndTime() - System.currentTimeMillis() / 1000);
            if (leftTime <= 0) {
                return "竞拍结束";
            }
            int hour = leftTime % (24 * 3600) / 3600;
            int minute = leftTime % 3600 / 60;
            int second = leftTime % 60;
            return hour + "小时" + minute + "分钟" + second + "秒 后结束";
        }

    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            Log.d(TAG, "run: "+isTimerOver());
            if (!isTimerOver()) {
                handler.sendEmptyMessage(REDUCE_TIME);
            }
        }

    }

    //用于判断倒计时是否结束
    private boolean isTimerOver() {
        return (agb.getGoodsEndTime()-System.currentTimeMillis()/1000) <=0  ;
    }

    public static void startIntent(Context context, int id) {
        Intent intent = new Intent(context, AuctionDetailsActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    private void getFlag() {
        Intent intent = getIntent();
        //agb = intent.getParcelableExtra("agb");
        id = intent.getIntExtra("id",-5);
        get_type = intent.getIntExtra("get_type",0);
    }

    /**
     * 设置页面相关状态
     * @param state      1：结束(6 7 8 9) 2：进行中(0 2 4 5 10)3:即将开始(0)
     * @param priceState 0：待交，2：已交，4：已拍(盲拍当前出价，等待竞拍结果)，5：已支付(当前出价，等待发货中)，6：已退款，7：违约惩罚 8：未获拍 9:获拍 10:已拍（当前出价，等待支付中）11:已拍卖完毕(最后成交价)
     * @param type       1抢拍 2盲拍
     */
    private void setState(int state, int priceState,int type ,Double transaction_price, Double reserve_money,Double win_price ,Double begin_price,Double end_price) {
        //降价次数显示与隐藏
        if(state== 1){
            tvCount.setVisibility(View.GONE);
        }else {
            tvCount.setVisibility(View.VISIBLE);
        }

        //顶部状态条背景色
        if(state==2){
            if(priceState==11){
                viewTopStatusBg.setBackgroundColor(0xffA0A0A0);
            }else {
                viewTopStatusBg.setBackgroundColor(0xffEB6060);
            }
        }else if(state==1|| state==3){
            viewTopStatusBg.setBackgroundColor(0xffA0A0A0);
        }
        //原价是否显示
        if(type==1){
            if(state==1 || state==2){
                tvOrgPrice.setVisibility(View.VISIBLE);
            }else if(state==3){
                tvOrgPrice.setVisibility(View.GONE);
            }
        }else if(type==2){
            if(state==1 ){
                tvOrgPrice.setVisibility(View.VISIBLE);
            }else if(state==3|| state==2){
                tvOrgPrice.setVisibility(View.GONE);
            }
        }
        //距离下次降价还剩几秒 是否显示
        if(type==1){
            if(state==1 || state==3){
                viewTimeDown.setVisibility(View.GONE);
            }else if(state==2){
                if(priceState==0 || priceState==2){
                    if(get_type==1){
                        viewTimeDown.setVisibility(View.GONE);
                    }else if(get_type==0){
                        viewTimeDown.setVisibility(View.VISIBLE);
                    }
                }else if(priceState==4 || priceState==5 ||priceState==10 || priceState==11){
                    viewTimeDown.setVisibility(View.GONE);
                }
            }
        }else if(type==2){
            viewTimeDown.setVisibility(View.GONE);
        }
        //拍卖参数是否显示
        if(type==1){//抢拍
            viewSomeParams.setVisibility(View.VISIBLE);
        }else if(type==2){//盲拍
            viewSomeParams.setVisibility(View.GONE);
        }
        //交保证金 是否可点击
        if(state==2){
            viewPayDeposite.setEnabled(true);
        }else {
            viewPayDeposite.setEnabled(false);
        }
        //有多少人围观
        if(state==1 || state==3){
            tvRishingPersons.setVisibility(View.GONE);
        }else {
            tvRishingPersons.setVisibility(View.VISIBLE);
        }
        //倒计时时间条
        if(state==3){//即将开始
            tvTDOver.setBackgroundColor(0xff4c4c4c);
        }else {
            tvTDOver.setBackgroundColor(0xffFF7C82);
        }
        //结束时间是否显示
        if(state==1){
            tvOverTime.setVisibility(View.VISIBLE);
        }else {
            tvOverTime.setVisibility(View.GONE);

        }
        //倒计时时间 //进行中或即将开始，没有付保证金 //进行中，已付保证金(抢拍) //进行中，已付保证金(盲拍)  //底部竞拍状态提示（已获拍和未获拍）
        if (state == 3 && priceState==0) {//即将开始
            setViewState(true,true,false,false,false);
            tvPriceTitle.setText("原价：");
            tvPrice.setText(NumberFormatUtils.getNewDouble(begin_price));
        } else if (state == 2 && priceState == 0) {//待交保证金
            setViewState(true,true,false,false,false);
            if(type==2){
                tvPriceTitle.setText("原价：");
                tvPrice.setText(NumberFormatUtils.getNewDouble(begin_price));
            }else {
                if(get_type==1){
                    tvPriceTitle.setText("最低价：");
                    tvPrice.setText(NumberFormatUtils.getNewDouble(end_price));
                }else if(get_type==0){
                    tvPriceTitle.setText("当前价：");
                    tvPrice.setText(NumberFormatUtils.getNewDouble(transaction_price));
                }
            }
        } else if (state == 2 && priceState == 2 && type == 1) {//抢拍 待拍
            setViewState(true,false,true,false,false);
            tvPriceTitle.setText("当前价：");
            if(get_type==1){
                tvPrice.setText(NumberFormatUtils.getNewDouble(end_price));
            }else if(get_type==0){
                tvPrice.setText(NumberFormatUtils.getNewDouble(transaction_price));
            }
        } else if (state == 2 && priceState == 2 && type == 2) {//盲拍 待拍
            setViewState(true,false,false,true,false);
            tvPriceTitle.setText("原价：");
            tvPrice.setText(NumberFormatUtils.getNewDouble(begin_price));
        } else if (state == 2 && priceState == 4) {//盲拍 等待竞拍结果
            setViewState(true,false,false,false,true);
            tvPriceTitle.setText("原价：");
            tvPrice.setText(NumberFormatUtils.getNewDouble(begin_price));
        } else if (state == 2 && priceState == 5) {//支付完成 等待发货
            setViewState(true,false,false,false,true);
            tvPriceTitle.setText("已支付：");
            tvPrice.setText(NumberFormatUtils.getNewDouble(reserve_money));
        } else if (state == 2 && priceState == 10) {//等待支付
            setViewState(true,false,false,false,true);
            tvPriceTitle.setText("待支付：");
            tvPrice.setText(NumberFormatUtils.getNewDouble(reserve_money));
        } else if (state == 2 && priceState == 11) {//被别人抢拍了
            setViewState(true,false,false,false,true);//？？？
            tvPriceTitle.setText("成交价：");
            tvPrice.setText(NumberFormatUtils.getNewDouble(reserve_money));
        } else if (state == 1 && priceState == 6) {//已退保证金
            setViewState(true,false,false,false,true);
            tvPriceTitle.setText("成交价：");
            tvPrice.setText(NumberFormatUtils.getNewDouble(win_price));
        } else if (state == 1 && priceState == 7) {//违约惩罚
            setViewState(true,false,false,false,true);
            tvPriceTitle.setText("成交价：");
            tvPrice.setText(NumberFormatUtils.getNewDouble(win_price));
        } else if (state == 1 && priceState == 8) {//拍卖已结束(未获拍)
            setViewState(true,false,false,false,true);
            tvPriceTitle.setText("成交价：");
            tvPrice.setText(NumberFormatUtils.getNewDouble(win_price));
        } else if (state == 1 && priceState == 9) {//拍卖已结束(已获拍)
            setViewState(true,false,false,false,true);
            tvPriceTitle.setText("成交价：");
            tvPrice.setText(NumberFormatUtils.getNewDouble(win_price));
        } else {
            setViewState(true,false,false,false,false);
            showToast("i do not set this state,use the default.");
        }

    }

    private void setViewState(boolean b, boolean b1, boolean b2, boolean b3, boolean b4) {
        tvTDOver.setVisibility(b ? View.VISIBLE : View.GONE); //倒计时时间
        viewGoingNoSureMoney.setVisibility(b1 ? View.VISIBLE : View.GONE);//进行中，没有付保证金
        viewGoingSureMoney.setVisibility(b2 ? View.VISIBLE : View.GONE);//进行中，已付保证金(抢拍)
        viewGoingSureMoneyBlind.setVisibility(b3 ? View.VISIBLE : View.GONE);//进行中，已付保证金(盲拍)
        tvAuctionState.setVisibility(b4 ? View.VISIBLE : View.GONE);//底部竞拍状态提示
    }

    private void initGoodsDetails() {
        viewTopStatusBg = findViewById(R.id.view_status_bg);
        tvStatus = ((TextView) findViewById(R.id.tv_status));
        tvName = ((TextView) findViewById(R.id.tv_name));
        tvPrice = ((TextView) findViewById(R.id.tv_price));
        tvOrgPrice = ((TextView) findViewById(R.id.tv_orignal_price));
        tvOrgPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvTD = ((TextView) findViewById(R.id.tv_time_down));
        tvTDOver = (TextView) findViewById(R.id.tv_time_down_over);
        tvScore = ((TextView) findViewById(R.id.tv_score));
        tvStartPrice = ((TextView) findViewById(R.id.tv_start_price));
        tvGapPrice = (TextView) findViewById(R.id.tv_gap_price);
        tvGapTime = ((TextView) findViewById(R.id.tv_gap_time));
        tvCount = ((TextView) findViewById(R.id.tv_reduce_count));
        //tvGoodsDet = ((TextView) findViewById(R.id.tv_goods_details));

        tvPersonNum = ((TextView) findViewById(R.id.tv_auction_person_num));
        tvOtherPersonNum = ((TextView) findViewById(R.id.tv_other_person_num));
        //距离下次降价
        viewTimeDown = findViewById(R.id.lat_reduce_time_down);
        //降价参数
        viewSomeParams = findViewById(R.id.lat_some_params);

        //当前价格描述
        tvPriceTitle = ((TextView) findViewById(R.id.text_price_title));
        //目前有多少人下手
        tvRishingPersons = ((TextView) findViewById(R.id.tv_rushing_persons));
        //结束时间
        tvOverTime = ((TextView) findViewById(R.id.tv_over_time));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
