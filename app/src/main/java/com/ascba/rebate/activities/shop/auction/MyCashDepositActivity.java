package com.ascba.rebate.activities.shop.auction;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.CashDepositAdapter;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.view.ShopABarText;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/5/25.
 * 我的保证金
 */

public class MyCashDepositActivity extends BaseNetActivity {

    private ShopABarText shopBar;
    private RecyclerView recyclerView;
    private CashDepositAdapter adapter;
    private List<AcutionGoodsBean> beanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_deposit);
        initView();
    }

    private void initView() {
        shopBar = (ShopABarText) findViewById(R.id.shopBar);
        shopBar.setCallback(new ShopABarText.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkBtn(View v) {

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        gerData();
        adapter = new CashDepositAdapter(this, R.layout.item_auction_cash_deposit, beanList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
            }
        });
    }

    private void gerData() {
        String imgUl = "https://img14.360buyimg.com/n0/jfs/t4594/342/1086676431/290324/feb08a33/58d87996N329d56d2.jpg";
        AcutionGoodsBean bean = new AcutionGoodsBean();
        bean.setImgUrl(imgUl);
        bean.setState("0");
        bean.setName("小米MIX 全网通 标准版 4GB内存 128GB ROM 陶瓷黑 移动联通电信4G手机");
        bean.setTimeRemaining("离开始：00时 02分 45秒");
        bean.setCashDeposit("￥100.00");
        bean.setPayState("0");
        beanList.add(bean);

        bean = new AcutionGoodsBean();
        bean.setImgUrl(imgUl);
        bean.setState("1");
        bean.setName("小米MIX 全网通 标准版 4GB内存 128GB ROM 陶瓷黑 移动联通电信4G手机");
        bean.setTimeRemaining("离结束：00时 02分 45秒");
        bean.setCashDeposit("￥100.00");
        bean.setPayState("0");
        beanList.add(bean);

        bean = new AcutionGoodsBean();
        bean.setImgUrl(imgUl);
        bean.setState("2");
        bean.setName("小米MIX 全网通 标准版 4GB内存 128GB ROM 陶瓷黑 移动联通电信4G手机");
        bean.setCashDeposit("￥100.00");
        bean.setPayState("1");
        beanList.add(bean);

    }
}
