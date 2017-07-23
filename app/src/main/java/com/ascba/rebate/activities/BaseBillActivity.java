package com.ascba.rebate.activities;

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
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.BillBaseAdapter;
import com.ascba.rebate.beans.CashAccount;
import com.ascba.rebate.handlers.BillNetworker;
import com.ascba.rebate.handlers.MoneyBarClickListener;
import com.ascba.rebate.utils.ViewUtils;
import com.ascba.rebate.view.MoneyBar;
import com.ascba.rebate.view.loadmore.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.chad.library.adapter.base.loadmore.LoadMoreView.STATUS_DEFAULT;

/**
 * 所有账单基类
 */
public class BaseBillActivity extends BaseNetActivity implements SwipeRefreshLayout.OnRefreshListener
            ,BaseQuickAdapter.RequestLoadMoreListener{

    protected MoneyBar mb;
    private MoneyBarClickListener listener;
    public SwipeRefreshLayout refreshLat;
    public int now_page=1;//当前页
    public int total_page=0;//总页数
    private RecyclerView recyclerView;
    public List<CashAccount> data;
    public BillBaseAdapter billAdapter;
    private CustomLoadMoreView loadMoreView;
    public boolean loadmore=false;//当前状态 默认下拉刷新
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
    private BillNetworker billNetworker;
    public Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
    int yearLast = 0;
    int monthLast = 0;
    private View viewHead;
    private ImageView imCalendar;
    private TextView tvSesc;
    int type;//账单类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_bill);
        initMoneyBar();
        initRefresh();
        initRecyclerView();
        initData();
        initAdapter();
        initLoadMore();
        initHeadView();
        if(billNetworker!=null){
            billNetworker.executeNetworkRefresh();
        }
    }

    private void initHeadView() {
        viewHead = findViewById(R.id.head);
        viewHead.setBackgroundColor(Color.WHITE);
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

    /**
     * 时间筛选dialog
     */
    private void showDataPickerDialog() {
        DatePickerDialog dateDlg = new DatePickerDialog(this, R.style.dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Intent intent = new Intent(BaseBillActivity.this, BusiFlowRecordsActivity.class);
                        if ((month + 1) < 10) {
                            intent.putExtra("date_time", year + "-0" + (month + 1));
                        } else {
                            intent.putExtra("date_time", year + "-" + (month + 1));
                        }
                        intent.putExtra("type",type);
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

    private void initLoadMore() {
        loadMoreView=new CustomLoadMoreView();
        billAdapter.setLoadMoreView(loadMoreView);
        billAdapter.setOnLoadMoreListener(this);
    }

    private void initAdapter() {
        billAdapter = new BillBaseAdapter(data,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(billAdapter);
        billAdapter.setEmptyView(ViewUtils.getView(this,R.layout.bill_list_empty));

    }

    private void initData() {
        data=new ArrayList<>();
    }

    private void initRecyclerView() {
        recyclerView = ((RecyclerView) findViewById(R.id.bill_list));
    }

    private void initRefresh() {
        refreshLat = ((SwipeRefreshLayout) findViewById(R.id.refresh_layout));
        refreshLat.setOnRefreshListener(this);
    }

    private void initMoneyBar() {
        mb = ((MoneyBar) findViewById(R.id.mb));
        mb.setCallBack(new MoneyBar.CallBack() {
            @Override
            public void clickImage(View im) {
                finish();
            }
            @Override
            public void clickComplete(View tv) {
                if(listener!=null){
                    listener.clickTailText(tv);
                }
            }
        });
    }
    //设置标题
    public void setMoneyBar(String title,boolean hasTailText,String tailText){
        mb.setTextTitle(title);
        if(hasTailText){
            mb.setTailTitle(tailText);
        }else {
            mb.setTailTitle(null);
        }
    }

    @Override
    public void onRefresh() {
        reset();
        if(billNetworker!=null){
            billNetworker.executeNetworkRefresh();
        }
    }
    //数据，页数重置
    protected void reset() {
        if(data.size()!=0){
            data.clear();
        }
        now_page=1;
        total_page=0;
    }

    @Override
    public void onLoadMoreRequested() {
        if (now_page > total_page && total_page != 0) {
            loadmore=false;
            handler.sendEmptyMessage(LOAD_MORE_END);
        } else if(total_page==0){
            loadmore=false;
            handler.sendEmptyMessage(LOAD_MORE_END);
        }else {
            loadmore=true;
            if(billNetworker!=null){
                billNetworker.executeNetworkRefresh();
            }

        }
    }

    public MoneyBarClickListener getListener() {
        return listener;
    }
    public void setListener(MoneyBarClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        refreshLat.setRefreshing(false);
        if (billAdapter != null) {
            billAdapter.loadMoreComplete();
        }
        if (billAdapter != null) {
            loadMoreView.setLoadMoreStatus(STATUS_DEFAULT);
        }
        total_page = dataObj.optInt("total_page");
        now_page++;
        if(billNetworker!=null){
            billNetworker.parseDataAndRefreshUI(what,dataObj,message);
        }
    }

    @Override
    protected void mhandle404(int what, JSONObject object, String message) {
        refreshLat.setRefreshing(false);
    }

    @Override
    protected void mhandleFailed(int what, Exception e) {
        refreshLat.setRefreshing(false);
    }

    @Override
    protected void mhandleReLogin(int what) {
        super.mhandleReLogin(what);
        refreshLat.setRefreshing(false);
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

    public View getViewHead() {
        return viewHead;
    }

    public void setBillNetworker(BillNetworker billNetworker) {
        this.billNetworker = billNetworker;
    }
}
