package com.ascba.rebate.activities.me_page;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.BusiFlowRecordsActivity;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.BillAdapter;
import com.ascba.rebate.beans.BillType;
import com.ascba.rebate.beans.CashAccount;
import com.ascba.rebate.beans.CashAccountType;
import com.ascba.rebate.utils.TimeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.BillTypeDialog;
import com.ascba.rebate.view.MoneyBar;
import com.ascba.rebate.view.loadmore.RefreshLayout;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class WhiteBillActivity extends BaseNetActivity implements RefreshLayout.OnRefreshListener
        , RefreshLayout.OnLoadListener
        , MoneyBar.CallBack, StickyListHeadersListView.OnHeaderClickListener
        , StickyListHeadersListView.OnStickyHeaderChangedListener {

    private RefreshLayout refreshLat;
    private StickyListHeadersListView billRV;
    private BillAdapter billAdapter;
    private List<CashAccount> billData;
    private MoneyBar mb;
    Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
    private int position;//记录筛选位置
    private List<BillType> data;//账单类型 4种
    private View m_listViewFooter;
    private int now_page = 1;
    private int total_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_bill);
        initViews();

        m_listViewFooter = getLayoutInflater().inflate(R.layout.foot_view, null);

        requestNetwork(UrlUtils.scoreBillList);
    }

    private void requestNetwork(String url) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        request.add("now_page", now_page);
        executeNetWork(0, request, "请稍后");
    }

    private void initViews() {
        initRefresh();
        initRecyclerView();
        initMoneyBar();
    }

    private void initMoneyBar() {
        mb = ((MoneyBar) findViewById(R.id.mb));
        mb.setCallBack(this);
    }

    private void initRecyclerView() {
        billRV = ((StickyListHeadersListView) findViewById(R.id.bill_list));
        initData();
        billAdapter = new BillAdapter(billData);
        billRV.setDrawingListUnderStickyHeader(true);
        billRV.setAreHeadersSticky(true);
        billRV.setOnHeaderClickListener(this);
        billRV.setOnStickyHeaderChangedListener(this);
        billRV.setAdapter(billAdapter);
        billRV.setEmptyView(getLayoutInflater().inflate(R.layout.bill_list_empty, null));

    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
        View im = header.findViewById(R.id.im_calendar);
        if (im.getVisibility() == View.VISIBLE) {
            showDataPickerDialog();
        }
    }

    @Override
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
        TextView desc = (TextView) header.findViewById(R.id.tv_desc);
        View im = header.findViewById(R.id.im_calendar);
        if (itemPosition != 0) {
            im.setVisibility(View.VISIBLE);
            desc.setVisibility(View.VISIBLE);
            desc.setText(billData.get(0).getTitleText());

        }
    }

    /**
     * 时间筛选dialog
     */
    private void showDataPickerDialog() {
        DatePickerDialog dateDlg = new DatePickerDialog(this, R.style.dialog,
                new DatePickerDialog.OnDateSetListener() {
                    Boolean mFired = false;

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if (mFired == true) {
                            return;
                        } else {
                            Intent intent = new Intent(WhiteBillActivity.this, BusiFlowRecordsActivity.class);
                            startActivity(intent);
                            mFired = true;
                        }
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
        /*for (int i = 0; i < 30; i++) {
            CashAccount ca = new CashAccount(null, "21.36", (200 + i) + "", "你管不着", null, R.mipmap.cash_cost);
            if (i >= 0 && i <= 2) {
                ca.setMonth("一月");
                ca.setDay("2015.01.15");
                ca.setType(CashAccountType.ALL);
            } else if (i >= 3 && i <= 10) {
                ca.setMonth("三月");
                ca.setDay("2015.03.15");
                ca.setType(CashAccountType.EXCHANGE);
            } else if (i >= 10 && i <= 20) {
                ca.setMonth("五月");
                ca.setDay("2015.05.15");
                ca.setType(CashAccountType.AWARD);
            } else {
                ca.setType(CashAccountType.COST);
                ca.setMonth("七月");
                ca.setDay("2015.07.15");
            }
            billData.add(ca);
        }*/
    }

    private void initRefresh() {
        refreshLat = ((RefreshLayout) findViewById(R.id.refresh_layout));
        refreshLat.setOnRefreshListener(this);
        refreshLat.setOnLoadListener(this);
    }

    @Override
    public void onRefresh() {
        if (billData.size() != 0) {
            billData.clear();
        }
        total_page = 0;
        now_page = 1;
        requestNetwork(UrlUtils.scoreBillList);
    }

    @Override
    public void clickImage(View im) {
    }

    /**
     * 点击筛选的回调
     */
    @Override
    public void clickComplete(View tv) {
        final BillTypeDialog bt = new BillTypeDialog(this, initTypeData(position));
        bt.showMyDialog();
        bt.setCallback(new BillTypeDialog.Callback() {
            @Override
            public void onClick(BillType a, int position) {
                WhiteBillActivity.this.position = position;
                bt.dismiss();
                billData.get(0).setTitleText(a.title);
                billAdapter.notifyDataSetChanged();
                billRV.getmList().smoothScrollToPosition(0);//滑动到顶部
            }
        });
    }

    private List<BillType> initTypeData(int position) {
        data = new ArrayList<>();
        data.add(new BillType(false, "全部", "123", CashAccountType.ALL));
        data.add(new BillType(false, "奖励", "234", CashAccountType.AWARD));
        data.add(new BillType(false, "消费", "235", CashAccountType.COST));
        data.add(new BillType(false, "兑换", "369", CashAccountType.EXCHANGE));
        for (int i = 0; i < data.size(); i++) {
            if (i == position) {
                data.get(i).hasSelect = true;
            } else {
                data.get(i).hasSelect = false;
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
    public void onLoad() {
        if (now_page > total_page && total_page != 0) {
            refreshLat.setLoading(false);
        } else {
            requestNetwork(UrlUtils.scoreBillList);
        }
    }

    @Override
    public void setFooterView(boolean isLoading) {

        if (isLoading) {
            billRV.getmList().removeFooterView(m_listViewFooter);
            billRV.getmList().addFooterView(m_listViewFooter);
        } else {
            billRV.getmList().removeFooterView(m_listViewFooter);
        }
    }

    /**
     * "id": 167,
     * "seller_name": "五悦北平四季涮肉",
     * "score": 26600,
     * "create_time": 1490766176,
     * "year": "2017",
     * "month": "03",
     * "pic": "http://home.qlqwp2p.com/public/app/images/xf.png",
     * "remarks": " - 消费增值"
     */
    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        refreshLat.setLoading(false);
        refreshLat.setRefreshing(false);
        total_page = dataObj.optInt("total_page");
        now_page++;
        JSONArray array = dataObj.optJSONArray("scoreList");
        if (array != null && array.length() != 0) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                CashAccount ca = new CashAccount();
                long create_time = obj.optLong("create_time");
                String day = TimeUtils.milliseconds2String(create_time * 1000, new SimpleDateFormat("yyyy.MM.dd"));
                String time = TimeUtils.milliseconds2String(create_time * 1000, new SimpleDateFormat("HH.mm"));
                int score = obj.optInt("score");
                String pic = obj.optString("pic");
                String filterText = obj.optString("remarks");
                String month = obj.optString("month");
                String year = obj.optString("year");
                ca.setDay(day);
                ca.setTime(time);
                ca.setMoney(score + "");
                ca.setImgUrl(pic);
                ca.setFilterText(filterText);
                ca.setTitleText("全部");
                int calendarYear = dateAndTime.get(Calendar.YEAR);
                if (!(calendarYear + "").equals(year)) {
                    ca.setMonth(year + "年" + month + "月");
                } else {
                    //ca.setMonth(month+"月");
                    ca.setMonth(month);
                }

                billData.add(ca);
            }
            billAdapter.notifyDataSetChanged();

        }
    }

    @Override
    protected void mhandle404(int what, JSONObject object, String message) {
    }

    @Override
    protected void mhandleFinish(int what) {
    }

    @Override
    protected void mhandleFailed(int what, Exception e) {
    }
}
