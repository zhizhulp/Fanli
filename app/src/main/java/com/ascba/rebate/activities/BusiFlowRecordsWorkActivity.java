package com.ascba.rebate.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
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
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.adapter.FlowRecordsAdapter;
import com.ascba.rebate.beans.CashAccount;
import com.ascba.rebate.view.ShopABarText;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * 流水记录
 */
public class BusiFlowRecordsActivity extends BaseNetWork4Activity implements
public class BusiFlowRecordsWorkActivity extends BaseNetWorkActivity implements
        SuperSwipeRefreshLayout.OnPullRefreshListener
        ,ShopABarText.Callback{
    private SuperSwipeRefreshLayout refreshLat;
    private RecyclerView rv;
    private FlowRecordsAdapter adapter;
    private List<CashAccount> data;
    private ShopABarText sb;
    private TextView tvTime;
    private TextView tvMoney;
    Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busi_flow_records);

        initViews();
    }

    private void initViews() {
        initRefreshLat();
        initRecyclerview();
        initShopBar();
    }

    private void initShopBar() {
        sb = ((ShopABarText) findViewById(R.id.sb));
        sb.setCallback(this);
    }

    private void initRecyclerview() {
        rv = ((RecyclerView) findViewById(R.id.rv));
        getData();
        adapter = new FlowRecordsAdapter(R.layout.wsaccount_list_item,data);
        View inflate = getLayoutInflater().inflate(R.layout.flow_records, null);
        initHeadView(inflate);
        adapter.addHeaderView(inflate);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
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

    private void getData() {
        data=new ArrayList<>();
        data.add(new CashAccount("今天", "21:41", "+456.12", "农业银行-充值", null, R.mipmap.cash_cost));

        data.add(new CashAccount("今天", "21:41", "-456.12", "农业银行-提现", null, R.mipmap.cash_cost));

        data.add(new CashAccount("昨天", "21:41", "+456.12", "兑换积分-返利", null, R.mipmap.cash_cost));

        data.add(new CashAccount("昨天", "21:41", "-456.12", "老家肉饼-消费", null, R.mipmap.cash_cost));

        data.add(new CashAccount("前天", "21:41", "+456.12", "推荐会员-佣金", null, R.mipmap.cash_cost));
    }

    private void initRefreshLat() {
        refreshLat = ((SuperSwipeRefreshLayout) findViewById(R.id.refresh_layout));
        refreshLat.setOnPullRefreshListener(this);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }

    @Override
    public void back(View v) {
        finish();
    }

    @Override
    public void clkBtn(View v) {

    }
}
