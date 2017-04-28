package com.ascba.rebate.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.me_page.WhiteBillActivity;
import com.ascba.rebate.adapter.FlowRecordsAdapter;
import com.ascba.rebate.beans.CashAccount;
import com.ascba.rebate.utils.TimeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ShopABarText;
import com.ascba.rebate.view.loadmore.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.chad.library.adapter.base.loadmore.LoadMoreView.STATUS_DEFAULT;

/**
 * 流水记录
 */

public class BusiFlowRecordsActivity extends BaseNetActivity implements
        SwipeRefreshLayout.OnRefreshListener
        ,ShopABarText.Callback{
    private static final int LOAD_MORE_END = 0;
    private static final int LOAD_MORE_ERROR = 1;
    private RecyclerView rv;
    private FlowRecordsAdapter adapter;
    private List<CashAccount> data;
    private ShopABarText sb;
    private TextView tvTime;
    private TextView tvMoney;
    Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
    private String dateTime;
    private int now_page = 1;
    private int total_page;
    private int type;//账单类型
    private CustomLoadMoreView loadMoreView;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_MORE_END:
                    if (adapter != null) {
                        adapter.loadMoreEnd(false);
                    }

                    break;
                case LOAD_MORE_ERROR:
                    if (adapter != null) {
                        adapter.loadMoreFail();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busi_flow_records);
        initViews();
        getDataFromItent();
    }

    private void getDataFromItent() {
        Intent intent = getIntent();
        if(intent!=null){
            dateTime = intent.getStringExtra("date_time");
            type = intent.getIntExtra("type", 0);
            if(type==0){
                requestNetwork(UrlUtils.dateFindScore);
            }else if(type==1){
                requestNetwork(UrlUtils.dateMoneyRecharge);
            }else if(type==2){
                requestNetwork(UrlUtils.dateMonthlyLog);
            }else if(type==3){
                requestNetwork(UrlUtils.dateMoneyTillLog);
            }

        }
    }

    private void requestNetwork(String url) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        request.add("now_page",now_page);
        request.add("date_time",dateTime);
        executeNetWork(type,request,"请稍后");
    }

    private void initViews() {
        initRefreshLayout();
        initRecyclerview();
        initShopBar();
        refreshLayout.setOnRefreshListener(this);
    }

    private void initShopBar() {
        sb = ((ShopABarText) findViewById(R.id.sb));
        sb.setCallback(this);
        sb.setTitle("白积分账单");
    }

    private void initRecyclerview() {
        rv = ((RecyclerView) findViewById(R.id.rv));
        getData();
        adapter = new FlowRecordsAdapter(R.layout.wsaccount_list_item,data,this);
        View inflate = getLayoutInflater().inflate(R.layout.flow_records, null);
        initHeadView(inflate);
        adapter.addHeaderView(inflate);
        adapter.setEmptyView(getLayoutInflater().inflate(R.layout.bill_list_empty, null));
        initLoadMore();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    private void initLoadMore() {
        if (loadMoreView == null) {
            loadMoreView = new CustomLoadMoreView();
            adapter.setLoadMoreView(loadMoreView);
        }
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (now_page > total_page && total_page != 0) {
                    handler.sendEmptyMessage(LOAD_MORE_END);
                } else {
                    if(type==0){
                        requestNetwork(UrlUtils.dateFindScore);
                    }else if(type==1){
                        requestNetwork(UrlUtils.dateMoneyRecharge);
                    }else if(type==2){
                        requestNetwork(UrlUtils.dateMonthlyLog);
                    }else if(type==3){
                        requestNetwork(UrlUtils.dateMoneyTillLog);
                    }

                }
            }
        });
    }

    private void initHeadView(View view) {
        tvTime = ((TextView) view.findViewById(R.id.tv_flow_time));
        tvMoney = ((TextView) view.findViewById(R.id.tv_flow_money));
        //点击头部显示时间控件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDataPickerDialog();
            }
        });
    }

    private void showDataPickerDialog() {
        DatePickerDialog dateDlg = new DatePickerDialog(this,R.style.dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        //刷新数据
                        if((month+1)<10){
                            dateTime=year +"-0"+ (month+1);
                        }else {
                            dateTime=year +"-"+ (month+1);
                        }
                        if(data.size()!=0){
                            data.clear();
                        }
                        now_page=1;
                        total_page=0;
                        if(type==0){
                            requestNetwork(UrlUtils.dateFindScore);
                        }else if(type==1){
                            requestNetwork(UrlUtils.dateMoneyRecharge);
                        }else if(type==2){
                            requestNetwork(UrlUtils.dateMonthlyLog);
                        }

                    }
                },
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));
        dateDlg.show();

        DatePicker dp = findDatePicker((ViewGroup) dateDlg.getWindow().getDecorView());
        if (dp != null) {
            ((ViewGroup)((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
        }

        Window window = dateDlg.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int) (d.getWidth() * 0.8f);
        window.setAttributes(wlp);
    }

    private DatePicker findDatePicker(ViewGroup group){
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }

    private void getData() {
        data=new ArrayList<>();
    }


    @Override
    public void onRefresh() {
        if(data.size()!=0){
            data.clear();
        }
        now_page=1;
        total_page=0;
        if(type==0){
            requestNetwork(UrlUtils.dateFindScore);
        }else if(type==1){
            requestNetwork(UrlUtils.dateMoneyRecharge);
        }else if(type==2){
            requestNetwork(UrlUtils.dateMonthlyLog);
        }else if(type==3){
            requestNetwork(UrlUtils.dateMoneyTillLog);
        }

    }


    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        if(what==0){
            stopRefreshAndLoadMore();
            JSONObject obj = dataObj.optJSONObject("scoreList");
            tvTime.setText(obj.optString("datetime"));
            tvMoney.setText("￥"+obj.optString("sum_score"));
            total_page = obj.optInt("total_page");
            now_page++;
            JSONArray array = obj.optJSONArray("score_data");
            if(array!=null && array.length()!=0){
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.optJSONObject(i);
                    long create_time = object1.optLong("create_time");
                    String day = TimeUtils.milliseconds2String(create_time * 1000, new SimpleDateFormat("yyyy.MM.dd"));
                    String time = TimeUtils.milliseconds2String(create_time * 1000, new SimpleDateFormat("HH.mm"));
                    CashAccount ca = new CashAccount(day, time, object1.optString("score_num"),object1.optString("remarks") , null, R.mipmap.cash_cost);
                    ca.setImgUrl(UrlUtils.baseWebsite+ object1.optString("img"));
                    data.add(ca);
                }
                adapter.notifyDataSetChanged();
            }
        }else if(what==1){
            stopRefreshAndLoadMore();
            JSONObject obj = dataObj.optJSONObject("moneyList");
            tvTime.setText(obj.optString("datetime"));
            tvMoney.setText("￥"+obj.optString("sum_money"));
            total_page = obj.optInt("total_page");
            now_page++;
            JSONArray array = obj.optJSONArray("score_data");
            if(array!=null && array.length()!=0){
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.optJSONObject(i);
                    long create_time = object1.optLong("create_time");
                    String day = TimeUtils.milliseconds2String(create_time * 1000, new SimpleDateFormat("yyyy.MM.dd"));
                    String time = TimeUtils.milliseconds2String(create_time * 1000, new SimpleDateFormat("HH.mm"));
                    CashAccount ca = new CashAccount(day, time, object1.optString("money_num"),object1.optString("remarks") , null, R.mipmap.cash_cost);
                    ca.setImgUrl(UrlUtils.baseWebsite+ object1.optString("pic"));
                    data.add(ca);
                }
                adapter.notifyDataSetChanged();
            }
        } else if(what==2){
            stopRefreshAndLoadMore();
            JSONObject obj = dataObj.optJSONObject("moneyList");
            tvTime.setText(obj.optString("datetime"));
            tvMoney.setText("￥"+obj.optString("money"));
            total_page = obj.optInt("total_page");
            now_page++;
            JSONArray array = obj.optJSONArray("score_data");
            if(array!=null && array.length()!=0){
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.optJSONObject(i);
                    long create_time = object1.optLong("create_time");
                    String day = TimeUtils.milliseconds2String(create_time * 1000, new SimpleDateFormat("yyyy.MM.dd"));
                    String time = TimeUtils.milliseconds2String(create_time * 1000, new SimpleDateFormat("HH.mm"));
                    CashAccount ca = new CashAccount(day, time, object1.optString("money_num"),object1.optString("remarks") , null, R.mipmap.cash_cost);
                    ca.setImgUrl(UrlUtils.baseWebsite+ object1.optString("pic"));
                    data.add(ca);
                }
                adapter.notifyDataSetChanged();
            }
        } else if(what==3){
            stopRefreshAndLoadMore();
            JSONObject obj = dataObj.optJSONObject("moneyList");
            tvTime.setText(obj.optString("datetime"));
            tvMoney.setText("￥"+obj.optString("money"));
            total_page = obj.optInt("total_page");
            now_page++;
            JSONArray array = obj.optJSONArray("score_data");
            if(array!=null && array.length()!=0){
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.optJSONObject(i);
                    long create_time = object1.optLong("create_time");
                    String day = TimeUtils.milliseconds2String(create_time * 1000, new SimpleDateFormat("yyyy.MM.dd"));
                    String time = TimeUtils.milliseconds2String(create_time * 1000, new SimpleDateFormat("HH.mm"));
                    CashAccount ca = new CashAccount(day, time, object1.optString("tills_money_num"),object1.optString("remarks") , null, R.mipmap.cash_cost);
                    ca.setImgUrl(UrlUtils.baseWebsite+ object1.optString("pic"));
                    data.add(ca);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void stopRefreshAndLoadMore() {
        refreshLayout.setRefreshing(false);
        if (adapter != null) {
            adapter.loadMoreComplete();
        }
        if (adapter != null) {
            loadMoreView.setLoadMoreStatus(STATUS_DEFAULT);
        }
    }

    protected void mhandle404(int what, JSONObject object, String message) {
        refreshLayout.setRefreshing(false);
        getDm().buildAlertDialog(message);
    }

    protected void mhandleFailed(int what, Exception e) {
        refreshLayout.setRefreshing(false);
        getDm().buildAlertDialog(getString(R.string.no_response));
    }

    @Override
    protected void mhandleReLogin(int what) {
        super.mhandleReLogin(what);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void back(View v) {
        finish();
    }

    @Override
    public void clkBtn(View v) {

    }
}
