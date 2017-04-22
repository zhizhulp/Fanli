package com.ascba.rebate.activities.me_page;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.ascba.rebate.view.BillTypeDialog;
import com.ascba.rebate.view.MoneyBar;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class WhiteBillActivity extends BaseNetActivity implements SuperSwipeRefreshLayout.OnPullRefreshListener
        , MoneyBar.CallBack, StickyListHeadersListView.OnHeaderClickListener
        , StickyListHeadersListView.OnStickyHeaderChangedListener {

    private SuperSwipeRefreshLayout refreshLat;
    private StickyListHeadersListView billRV;
    private BillAdapter billAdapter;
    private List<CashAccount> billData;
    private MoneyBar mb;
    Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
    private int position;//记录筛选位置
    private List<BillType> data;//账单类型 4种

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_bill);
        initViews();
    }

    private void initViews() {
        initRefreshLayout();
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
            switch (billData.get(0).getType()) {
                case ALL:
                    desc.setText("全部");
                    break;
                case AWARD:
                    desc.setText("奖励");
                    break;
                case COST:
                    desc.setText("消费");
                    break;
                case EXCHANGE:
                    desc.setText("兑换");
                    break;
                default:
                    desc.setText("未知分类");
                    break;
            }
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
        for (int i = 0; i < 30; i++) {
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
        }
    }

    private void initRefreshLayout() {
        refreshLat = ((SuperSwipeRefreshLayout) findViewById(R.id.refresh_layout));
        refreshLat.setOnPullRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLat.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

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
                billData.get(0).setType(a.type);//修改首行标题  全部。奖励。消费。兑换
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


}
