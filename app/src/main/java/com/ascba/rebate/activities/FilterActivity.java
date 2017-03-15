package com.ascba.rebate.activities;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.ProfileAdapter;
import com.ascba.rebate.beans.GoodsAttr;
import com.ascba.rebate.view.ShopABarText;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends BaseNetWork4Activity implements View.OnClickListener,ShopABarText.Callback {

    private RecyclerView filterRV;
    private Button btnReset;
    private Button btnOver;
    private List<GoodsAttr> gas;
    private ProfileAdapter adapter;
    private ShopABarText sab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        initShopAbar();
        initRecyclerView();
        initReset();//重置
        initComPlete();//完成
    }

    private void initShopAbar() {
        sab = ((ShopABarText) findViewById(R.id.sab));
        sab.setBtnEnable(false);
        sab.setCallback(this);
    }

    private void initComPlete() {
        btnOver = ((Button) findViewById(R.id.filter_over));
        btnOver.setOnClickListener(this);
    }

    private void initReset() {
        btnReset = ((Button) findViewById(R.id.filter_reset));
        btnReset.setOnClickListener(this);
    }

    private void initRecyclerView() {
        filterRV = ((RecyclerView) findViewById(R.id.filter_list));
        gas=new ArrayList<>();
        initAttrsData(gas);
        adapter=new ProfileAdapter(R.layout.goods_attrs_layout,gas);
        filterRV.setLayoutManager(new LinearLayoutManager(this));
        filterRV.setAdapter(adapter);
    }

    private void initAttrsData(List<GoodsAttr> gas) {
        for (int i = 0; i < 5; i++) {
            if(i==0){
                List<GoodsAttr.Attrs> strs=new ArrayList<>();
                GoodsAttr ga=new GoodsAttr();
                for (int j = 0; j < 3; j++) {
                    strs.add(ga.new Attrs("红色/白色",0));
                }
                ga.setTitle("颜色分类：");
                ga.setStrs(strs);
                gas.add(ga);
            }
            if(i==1){
                List<GoodsAttr.Attrs> strs=new ArrayList<>();
                GoodsAttr ga=new GoodsAttr();
                for (int j = 0; j < 15; j++) {

                    strs.add(ga.new Attrs((40+j+0.5)+"",0));
                }
                ga.setTitle("鞋码：");
                ga.setStrs(strs);
                gas.add(ga);
            }
            if(i==2){
                List<GoodsAttr.Attrs> strs=new ArrayList<>();
                GoodsAttr ga=new GoodsAttr();
                for (int j = 0; j < 3; j++) {
                    strs.add(ga.new Attrs("方形"+i,0));
                }
                ga.setTitle("其他分类：");
                ga.setStrs(strs);
                gas.add(ga);
            }
            if(i==3){
                List<GoodsAttr.Attrs> strs=new ArrayList<>();
                GoodsAttr ga=new GoodsAttr();
                for (int j = 0; j < 15; j++) {
                    strs.add(ga.new Attrs((40+j+0.5)+"",0));
                }
                ga.setTitle("鞋码：");
                ga.setStrs(strs);
                gas.add(ga);
            }
            if(i==4){
                List<GoodsAttr.Attrs> strs=new ArrayList<>();
                GoodsAttr ga=new GoodsAttr();
                for (int j = 0; j < 3; j++) {
                    strs.add(ga.new Attrs("方形"+i,0));
                }
                ga.setTitle("其他分类：");
                ga.setStrs(strs);
                gas.add(ga);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.filter_reset:
                if(gas!=null && gas.size()!=0){
                    for (int i = 0; i < gas.size(); i++) {
                        List<GoodsAttr.Attrs> strs = gas.get(i).getStrs();
                        for (int j = 0; j < strs.size(); j++) {
                            GoodsAttr.Attrs attrs = strs.get(j);
                            if(attrs.getTextColor()!=0){
                                attrs.setTextColor(0);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.filter_over:
                finish();
                break;
        }
    }

    @Override
    public void back(View v) {
        finish();
    }

    @Override
    public void clkBtn(View v) {

    }
}
