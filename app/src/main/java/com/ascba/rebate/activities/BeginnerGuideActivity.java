package com.ascba.rebate.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.BGExpandablItemAdapter;
import com.ascba.rebate.beans.BGExpandableLevel;
import com.ascba.rebate.beans.BGExpandableLevel0Item;
import com.ascba.rebate.beans.BGExpandableLevel1Item;
import com.ascba.rebate.view.ShopABar;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;

/**
 * Created by 李鹏 on 2017/03/07 0007.
 * 新手指南
 */

public class BeginnerGuideActivity extends BaseNetWork4Activity {

    private ShopABar shopABar;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginner_guide);

        recyclerView = (RecyclerView) findViewById(R.id.beginner_guide_recyler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BGExpandablItemAdapter bgExpandablItemAdapter=new BGExpandablItemAdapter(generateData());

        recyclerView.setAdapter(bgExpandablItemAdapter);

        shopABar = (ShopABar) findViewById(R.id.beginner_guide_bar);
        shopABar.setImageOtherEnable(false);
        shopABar.setMsgEnable(false);
        shopABar.setCallback(new ShopABar.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkMsg(View v) {

            }

            @Override
            public void clkOther(View v) {

            }
        });


    }

    private ArrayList<MultiItemEntity> generateData() {
        String title = "什么车都可以安装吗？";
        String answer = "后视镜行车记录仪基本通用全部车型，如轿车、国产、进口SUV、越野车等。我们这个是无损安装的哦，我们的记录仪直接覆盖到您原车的后视镜，无需改动原车后视镜的状态。";

        ArrayList<MultiItemEntity> itemEntityArrayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BGExpandableLevel0Item lv0 = new BGExpandableLevel0Item(title);
            BGExpandableLevel1Item lv1 = new BGExpandableLevel1Item(answer);
            lv0.addSubItem(lv1);
            itemEntityArrayList.add(lv0);

            //分割线
            BGExpandableLevel bgExpandableLevel=new BGExpandableLevel();
            itemEntityArrayList.add(bgExpandableLevel);
        }
        return itemEntityArrayList;
    }
}
