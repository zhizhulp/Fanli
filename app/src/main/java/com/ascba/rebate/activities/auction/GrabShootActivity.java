package com.ascba.rebate.activities.auction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.GoodsDetailsActivity;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.ImageAdapter;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.fragments.auction.AuctionMainPlaceChildFragment;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ImageViewDialog;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 李鹏 on 2017/5/24.
 * 抢拍详情界面
 */

public class GrabShootActivity extends BaseNetActivity {

    public static final int STATE_NO_PAY = 0; //未交保证金
    public static final int STATE_NO_START = 1; //未开始
    public static final int STATE_START = 2; //开始
    private static final int REDUCE_TIME = 3;
    private AcutionGoodsBean agb;
    private View noPay;//未交保证金
    private View noStart, viewNotic;//未开始
    private View payStart;//开始竞拍
    private TextView tvStatus;
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvOrgPrice;
    private TextView tvScore;
    private TextView tvGoodsDet;
    private TextView tvTD;
    private TextView tvTDOver;
    private TextView tvSureMoney;
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
                    if (agb == null) {
                        showToast("错误，无商品信息");
                        finish();
                        return;
                    }
                    setBeanProperty();
                    break;
            }
        }
    };
    private List<String> urls;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grab_shoot);
        initUI();
        requestNetwork(UrlUtils.auctionArticle, 0);
    }

    private void initUI() {
        //尚未开始
        viewNotic = findViewById(R.id.tv_status);
        noStart = findViewById(R.id.view_noStart);

        //未交保证金
        noPay = findViewById(R.id.view_noPay);

        //开始竞拍
        payStart = findViewById(R.id.view_pay);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getFlag();

        tvStatus = ((TextView) findViewById(R.id.tv_status));
        tvName = ((TextView) findViewById(R.id.tv_name));
        tvPrice = ((TextView) findViewById(R.id.tv_price));
        tvOrgPrice = ((TextView) findViewById(R.id.tv_orignal_price));
        tvTD = ((TextView) findViewById(R.id.tv_time_down));
        tvTDOver = (TextView) findViewById(R.id.tv_time_down_over);
        tvScore = ((TextView) findViewById(R.id.tv_score));
        tvStartPrice = ((TextView) findViewById(R.id.tv_start_price));
        tvGapPrice = (TextView) findViewById(R.id.tv_gap_price);
        tvGapTime = ((TextView) findViewById(R.id.tv_gap_time));
        tvCount = ((TextView) findViewById(R.id.tv_reduce_count));
        tvGoodsDet = ((TextView) findViewById(R.id.tv_goods_details));
        tvSureMoney = ((TextView) findViewById(R.id.tv_sure_money));

        initViewPager();
    }
    //初始化Viewpager
    private void initViewPager() {

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_img);
        urls =new ArrayList<>();
        imageAdapter = new ImageAdapter(urls);
        viewPager.setAdapter(imageAdapter);
    }

    private void setBeanProperty() {
        int currentLeftTime = agb.getCurrentLeftTime();
        int reduceTimes = agb.getReduceTimes();
        int maxReduceTimes = agb.getMaxReduceTimes();
        Double price = agb.getPrice();
        if (reduceTimes >= maxReduceTimes) {
            return;
        }
        currentLeftTime--;
        if (currentLeftTime <= 0) {
            reduceTimes++;
            price -= agb.getGapPrice();
            currentLeftTime = agb.getGapTime();
            agb.setReduceTimes(reduceTimes);
            agb.setPrice(price);
            tvCount.setText("降价次数："+reduceTimes + "次");
            tvPrice.setText(price + "");
        }
        agb.setCurrentLeftTime(currentLeftTime);
        tvTD.setText(currentLeftTime + "s");
        tvTDOver.setText(getRemainingTime(agb));
    }

    private void requestNetwork(String url, int what) {
        if (what == 0) {
            Request<JSONObject> request = buildNetRequest(url, 0, false);
            request.add("goods_id", agb.getId());
            executeNetWork(what, request, "请稍后");
        }
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        if (what == 0) {
            refreshGoodsData(dataObj);
            refreshViewPagerData(dataObj);
            if (timer == null) {
                timer = new Timer();
                timer.schedule(new MyTimerTask(), 0, 1000);
            }
        }
    }

    private void refreshViewPagerData(JSONObject dataObj) {
        JSONObject obj = dataObj.optJSONObject("auctionArticle");
        JSONArray photos = obj.optJSONArray("photos");
        if(photos!=null && photos.length()!=0){
            if(urls.size()>0){
                urls.clear();
            }
            for (int i = 0; i < photos.length(); i++) {
                String img = photos.optString(i);
                urls.add(UrlUtils.baseWebsite+img);
                Log.d(TAG, "refreshViewPagerData: "+UrlUtils.baseWebsite+img);
            }
        }
        imageAdapter.notifyDataSetChanged();
    }

    private void refreshGoodsData(JSONObject dataObj) {
        JSONObject obj = dataObj.optJSONObject("auctionArticle");
        int id = obj.optInt("id");
        int type = obj.optInt("type");
        String name = obj.optString("name");
        double transaction_price = obj.optDouble("transaction_price");
        String points = obj.optString("points");
        String cash_deposit = obj.optString("cash_deposit");
        int refresh_count = obj.optInt("refresh_count");
        double range = obj.optDouble("range");
        int depreciate_count = obj.optInt("depreciate_count");
        int count_down = obj.optInt("count_down");
        int interval_second = obj.optInt("interval_second");
        int status = obj.optInt("status");
        String auction_tip = obj.optString("auction_tip");
        double begin_price = obj.optDouble("begin_price");
        double end_price = obj.optDouble("end_price");
        long starttime = obj.optLong("starttime");
        long endtime = obj.optLong("endtime");

        this.agb = new AcutionGoodsBean(id, type, null,
                name, transaction_price, points, cash_deposit, refresh_count);
        agb.setGapPrice(range);
        agb.setMaxReduceTimes(depreciate_count);
        agb.setCurrentLeftTime(count_down);
        agb.setGapTime(interval_second);
        agb.setIntState(status);
        agb.setStrState(auction_tip);
        agb.setStartPrice(begin_price);
        agb.setEndPrice(end_price);
        agb.setStartTime(starttime);
        agb.setEndTime(endtime);

        tvStatus.setText(obj.optString("is_endtime_tip"));
        tvName.setText(name);
        CharSequence content= Html.fromHtml(obj.optString("content"));
        tvGoodsDet.setText(content);
        tvPrice.setText(transaction_price + "");
        tvOrgPrice.setText(begin_price + "");
        tvTD.setText(count_down + "s");
        tvScore.setText(points + "");
        tvStartPrice.setText("起拍价：￥" + begin_price);
        tvGapTime.setText("延时周期："+interval_second + "s/次");
        tvGapPrice.setText("降价幅度：￥" + range+"/次");
        tvCount.setText("降价次数："+refresh_count + "次");
        tvSureMoney.setText("保证金￥" + cash_deposit);
        tvTDOver.setText(getRemainingTime(agb));
    }

    /**
     * 计算倒计时时间
     * @param item 商品
     * @return 倒计时时间
     */
    private String getRemainingTime(AcutionGoodsBean item) {
        int leftTime = (int) (item.getEndTime() - System.currentTimeMillis() / 1000);
        if(leftTime <=0){
            return "竞拍结束";
        }
        int hour = leftTime % (24 * 3600) / 3600;
        int minute = leftTime % 3600 / 60;
        int second = leftTime % 60;
        return hour + "时" + minute + "分" + second + "秒 后结束";
    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            if (!isTimerOver()) {
                handler.sendEmptyMessage(REDUCE_TIME);
            }
        }

    }

    //用于判断倒计时是否结束
    private boolean isTimerOver() {
        int reduceTimes = agb.getReduceTimes();
        int maxReduceTimes = agb.getMaxReduceTimes();
        return reduceTimes >= maxReduceTimes;
    }

    public static void startIntent(Context context, AcutionGoodsBean agb) {
        Intent intent = new Intent(context, GrabShootActivity.class);
        intent.putExtra("agb", agb);
        context.startActivity(intent);
    }

    private void getFlag() {
        Intent intent = getIntent();
        agb = intent.getParcelableExtra("agb");
        // TODO: 2017/6/5
        setState(STATE_NO_START);
    }


    //更改状态
    private void setState(int flag) {
        viewNotic.setVisibility(View.GONE);
        noStart.setVisibility(View.GONE);
        noPay.setVisibility(View.GONE);
        payStart.setVisibility(View.GONE);
        switch (flag) {
            case STATE_NO_PAY:
                viewNotic.setVisibility(View.VISIBLE);
                noPay.setVisibility(View.VISIBLE);
                break;

            case STATE_NO_START:
                viewNotic.setVisibility(View.VISIBLE);
                noStart.setVisibility(View.VISIBLE);
                break;
            case STATE_START:
                viewNotic.setVisibility(View.VISIBLE);
                payStart.setVisibility(View.VISIBLE);
                break;
        }
    }
}
