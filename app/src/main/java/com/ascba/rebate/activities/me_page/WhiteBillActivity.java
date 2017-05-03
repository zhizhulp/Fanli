package com.ascba.rebate.activities.me_page;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.TextView;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.BusiFlowRecordsActivity;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.BillBaseAdapter;
import com.ascba.rebate.beans.BillType;
import com.ascba.rebate.beans.CashAccount;
import com.ascba.rebate.utils.TimeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.BillTypeDialog;
import com.ascba.rebate.view.MoneyBar;
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
 * 财富-白积分
 */

public class WhiteBillActivity extends BaseNetActivity implements SwipeRefreshLayout.OnRefreshListener
        , MoneyBar.CallBack {

    private RecyclerView billRV;
    private BillBaseAdapter billAdapter;
    private List<CashAccount> billData;
    private MoneyBar mb;
    Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
    private int position;//记录筛选位置
    private List<BillType> data;//账单类型 4种
    private int now_page = 1;
    private int total_page;
    private CustomLoadMoreView loadMoreView;
    private static final int LOAD_MORE_END = 0;
    private static final int LOAD_MORE_ERROR = 1;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_MORE_END:
                    if (billAdapter != null) {
                        billAdapter.loadMoreEnd(false);
                    }

                    break;
                case LOAD_MORE_ERROR:
                    if (billAdapter != null) {
                        billAdapter.loadMoreFail();
                    }
                    break;
            }
        }
    };
    private TextView tvFirstMonth;//首月
    private ImageView imCalendar;//日历图标
    private TextView tvSesc;//分类描述
    private View viewHead;
    int yearLast = 0;
    int monthLast = 0;
    private boolean loadmore=false;//当前状态 默认下拉刷新


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_bill);
        initViews();
        requestNetwork(UrlUtils.scoreBillList, 0);
    }

    private void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        if (what == 0) {
            request.add("now_page", now_page);
        }
        executeNetWork(what, request, "请稍后");
    }

    private void initViews() {
        initRefreshLayoutMine();
        initRecyclerView();
        initMoneyBar();
        initHeadView();
    }

    private void initHeadView() {
        viewHead = findViewById(R.id.head);
        viewHead.setBackgroundColor(Color.WHITE);
        tvFirstMonth = ((TextView) findViewById(R.id.tv_month));
        imCalendar = ((ImageView) findViewById(R.id.im_calendar));
        imCalendar.setVisibility(View.VISIBLE);
        viewHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDataPickerDialog();
            }
        });
        tvSesc = (TextView) findViewById(R.id.tv_desc);
        tvSesc.setVisibility(View.VISIBLE);
        findViewById(R.id.line).setVisibility(View.VISIBLE);
        tvSesc.setText("全部");
    }

    private void initRefreshLayoutMine() {
        initRefreshLayout();
        refreshLayout.setOnRefreshListener(this);
    }

    private void initMoneyBar() {
        mb = ((MoneyBar) findViewById(R.id.mb));
        mb.setCallBack(this);
    }

    private void initRecyclerView() {
        billRV = ((RecyclerView) findViewById(R.id.bill_list));
        initData();
        billAdapter = new BillBaseAdapter(billData, this);
        billRV.setLayoutManager(new LinearLayoutManager(this));
        billRV.setAdapter(billAdapter);
        billAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.bill_list_empty, null));
        initLoadMore();
    }

    private void initLoadMore() {
        if (loadMoreView == null) {
            loadMoreView = new CustomLoadMoreView();
            billAdapter.setLoadMoreView(loadMoreView);
        }
        billAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (now_page > total_page && total_page != 0) {
                    loadmore=false;
                    handler.sendEmptyMessage(LOAD_MORE_END);
                } else {
                    loadmore=true;
                    requestNetwork(UrlUtils.scoreBillList, 0);
                }
            }
        });
    }

    /**
     * 时间筛选dialog
     */
    private void showDataPickerDialog() {
        DatePickerDialog dateDlg = new DatePickerDialog(this, R.style.dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Intent intent = new Intent(WhiteBillActivity.this, BusiFlowRecordsActivity.class);
                        if ((month + 1) < 10) {
                            intent.putExtra("date_time", year + "-0" + (month + 1));
                        } else {
                            intent.putExtra("date_time", year + "-" + (month + 1));
                        }

                        startActivity(intent);
                    }
                },
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));
        dateDlg.show();
        DatePicker dp = findDatePicker((ViewGroup) dateDlg.getWindow().getDecorView());
        if (dp != null) {
            ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
        }
        Window window = dateDlg.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int) (d.getWidth() * 0.8f);
        window.setAttributes(wlp);
    }


    private void initData() {
        billData = new ArrayList<>();
    }


    @Override
    public void onRefresh() {
        reset();
        requestNetwork(UrlUtils.scoreBillList, 0);
    }

    private void reset() {
        loadmore=false;
        monthLast=0;
        yearLast=0;
        if (billData.size() != 0) {
            billData.clear();
        }
        total_page = 0;
        now_page = 1;
    }

    @Override
    public void clickImage(View im) {
    }

    /**
     * 点击筛选的回调
     */
    @Override
    public void clickComplete(View tv) {
        requestNetwork(UrlUtils.scoreClass, 1);
    }

    private List<BillType> initTypeData(JSONArray array, int position) {
        data = new ArrayList<>();
        if (array != null && array.length() != 0) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                data.add(new BillType(false, obj.optString("class_name"), "" + obj.optInt("all_score_count"), obj.optInt("class_id")));
            }
            for (int i = 0; i < data.size(); i++) {
                if (i == position) {
                    data.get(i).hasSelect = true;
                } else {
                    data.get(i).hasSelect = false;
                }
            }
        }
        return data;
    }

    private DatePicker findDatePicker(ViewGroup group) {
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

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        if (what == 0) {
            refreshLayout.setRefreshing(false);
            if (billAdapter != null) {
                billAdapter.loadMoreComplete();
            }
            if (billAdapter != null) {
                loadMoreView.setLoadMoreStatus(STATUS_DEFAULT);
            }
            total_page = dataObj.optInt("total_page");
            now_page++;
            JSONArray array = dataObj.optJSONArray("scoreList");
            if (array != null && array.length() != 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.optJSONObject(i);
                    long create_time = obj.optLong("create_time");
                    String day = TimeUtils.milliseconds2String(create_time * 1000, new SimpleDateFormat("MM.dd"));
                    String time = TimeUtils.milliseconds2String(create_time * 1000, new SimpleDateFormat("HH:mm"));
                    String score = obj.optString("score_num");
                    String pic = obj.optString("pic");
                    String filterText = obj.optString("remarks");
                    String month = obj.optString("month");
                    String year = obj.optString("year");
                    if (i != 0) {
                        if (yearLast != Integer.parseInt(year)) {
                            addHead(month,year);

                        } else {
                            if (monthLast != Integer.parseInt(month)) {
                                addHead(month,year);
                            }
                        }

                    } else {
                        if(loadmore){
                            if (yearLast != Integer.parseInt(year)) {
                                addHead(month,year);
                            } else {
                                if (monthLast != Integer.parseInt(month)) {
                                    addHead(month,year);
                                }
                            }
                        }else {
                            addHead(month,year);
                        }

                    }
                    yearLast = Integer.parseInt(year);
                    monthLast = Integer.parseInt(month);

                    CashAccount ca = new CashAccount();
                    ca.setDay(day);
                    ca.setTime(time);
                    ca.setMoney(score);
                    ca.setImgUrl(UrlUtils.baseWebsite + pic);
                    ca.setFilterText(filterText);
                    ca.setItemType(1);
                    billData.add(ca);
                }
                billAdapter.notifyDataSetChanged();
            }else {
                viewHead.setVisibility(View.GONE);
            }
        } else if (what == 1) {
            JSONArray array = dataObj.optJSONArray("scoreList");
            final BillTypeDialog bt = new BillTypeDialog(this, initTypeData(array, position));
            bt.showMyDialog();
            bt.setCallback(new BillTypeDialog.Callback() {
                @Override
                public void onClick(BillType a, int position) {
                    WhiteBillActivity.this.position = position;
                    bt.dismiss();
                    tvSesc.setText(a.title);
                }
            });
        }


    }

    private void addHead(String month,String year) {
        CashAccount ca = new CashAccount();
        int calendarYear = dateAndTime.get(Calendar.YEAR);
        if (!(calendarYear + "").equals(year)) {
            ca.setMonth(year + "年" + month + "月");
        } else {
            ca.setMonth(month + "月");
        }
        ca.setItemType(0);
        billData.add(ca);
    }

    @Override
    protected void mhandle404(int what, JSONObject object, String message) {
        refreshLayout.setRefreshing(false);
        getDm().buildAlertDialog(message);
    }

    @Override
    protected void mhandleFailed(int what, Exception e) {
        refreshLayout.setRefreshing(false);
        getDm().buildAlertDialog(getString(R.string.no_response));
    }

    @Override
    protected void mhandleReLogin(int what) {
        super.mhandleReLogin(what);
        refreshLayout.setRefreshing(false);
    }
}
