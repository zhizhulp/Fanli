package com.ascba.rebate.activities.me_page;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.BusiFlowRecordsActivity;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.BillAdapter;
import com.ascba.rebate.beans.CashAccount;
import com.ascba.rebate.view.BillTypeDialog;
import com.ascba.rebate.view.MoneyBar;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WhiteBillActivity extends BaseNetActivity implements SuperSwipeRefreshLayout.OnPullRefreshListener
        ,MoneyBar.CallBack{

    private SuperSwipeRefreshLayout refreshLat;
    private RecyclerView billRV;
    private BillAdapter billAdapter;
    private List<CashAccount> billData;
    private StickyRecyclerHeadersDecoration headerDecor;
    private MoneyBar mb;
    Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);

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
        billRV = ((RecyclerView) findViewById(R.id.bill_list));
        billRV.setLayoutManager(new LinearLayoutManager(this));
        initData();
        billAdapter = new BillAdapter(billData);
        billRV.setAdapter(billAdapter);
        headerDecor = new StickyRecyclerHeadersDecoration(billAdapter);
        billRV.addItemDecoration(headerDecor);
        addOnClickListener();
    }

    private void addOnClickListener() {
        StickyRecyclerHeadersTouchListener touchListener =
                new StickyRecyclerHeadersTouchListener(billRV, headerDecor);
        touchListener.setOnHeaderClickListener(
                new StickyRecyclerHeadersTouchListener.OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(View header, int position, long headerId) {
                        if(position==0){
                            showDataPickerDialog();
                        }
                    }
                });
        billRV.addOnItemTouchListener(touchListener);
    }

    private void showDataPickerDialog() {
        DatePickerDialog dateDlg = new DatePickerDialog(this,R.style.dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        showToast("当前选择"+ year +"年"+ (month+1)+"月");
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

    private void initData() {
        billData=new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            CashAccount ca=new CashAccount("2015.6.1","21.36",(200+i)+"","你管不着",null,R.mipmap.cash_cost);
            if(i>=0 && i<=2){
                ca.setMonth("一月");
                ca.setDay("2015.01.15");
            }else if(i>=3 && i<=10){
                ca.setMonth("三月");
                ca.setDay("2015.03.15");
            }else if(i>=10 && i<=20){
                ca.setMonth("五月");
                ca.setDay("2015.05.15");
            }else{
                ca.setMonth("七月");
                ca.setDay("2015.07.15");
            }
            billData.add(ca);
        }
    }
    /**
     * 若使用recyclerview修改visibility
     */
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
        },1000);
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
    /*
     * 点击筛选的回调
     */
    @Override
    public void clickComplete(View tv) {
        final BillTypeDialog bt=new BillTypeDialog(this);
        bt.showMyDialog();
        bt.setCallback(new BillTypeDialog.Callback() {
            @Override
            public void onClick(BillTypeDialog.BillType a) {
                bt.dismiss();
                Intent intent=new Intent(WhiteBillActivity.this, BusiFlowRecordsActivity.class);
                startActivity(intent);
            }
        });
    }
}
