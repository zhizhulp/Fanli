package com.ascba.rebate.activities.clothes;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.ShopTypeRVAdapter;
import com.ascba.rebate.beans.ShopBaseItem;
import com.ascba.rebate.beans.ShopItemType;
import com.ascba.rebate.beans.TypeWeight;
import com.ascba.rebate.view.ShopABar;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class TypeClothActivity extends BaseNetWork4Activity implements SuperSwipeRefreshLayout.OnPullRefreshListener,ShopABar.Callback {

    private RecyclerView rv;
    private SuperSwipeRefreshLayout refreshLat;
    private ShopTypeRVAdapter adapter;
    private List<ShopBaseItem> data;
    private List<String> urls;//viewPager数据源
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private ShopABar sab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_cloth);
        initViews();
    }

    private void initViews() {
        rv = ((RecyclerView) findViewById(R.id.list_clothes));
        refreshLat = ((SuperSwipeRefreshLayout) findViewById(R.id.refresh_layout));
        refreshLat.setOnPullRefreshListener(this);
        initData();
        adapter=new ShopTypeRVAdapter(data,this);
        final GridLayoutManager manager = new GridLayoutManager(this, TypeWeight.TYPE_SPAN_SIZE_MAX);
        rv.setLayoutManager(manager);
        adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return data.get(position).getSpanSize();
            }
        });
        rv.setAdapter(adapter);

        sab = ((ShopABar) findViewById(R.id.sab));
        sab.setCallback(this);
    }
    private void initData() {
        data=new ArrayList<>();
        urls=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            urls.add("http://image18-c.poco.cn/mypoco/myphoto/20170301/16/18505011120170301161128072_640.jpg");
        }
        //viewPager
        data.add(new ShopBaseItem(ShopItemType.TYPE_PAGER,TypeWeight.TYPE_SPAN_SIZE_60,R.layout.shop_pager,urls));
        //导航栏
        data.add(new ShopBaseItem(ShopItemType.TYPE_NAVIGATION,TypeWeight.TYPE_SPAN_SIZE_12,R.layout.shop_navigation,
                R.mipmap.cloth_man,"男装"));
        data.add(new ShopBaseItem(ShopItemType.TYPE_NAVIGATION,TypeWeight.TYPE_SPAN_SIZE_12,R.layout.shop_navigation,
                R.mipmap.cloth_woman,"女装"));
        data.add(new ShopBaseItem(ShopItemType.TYPE_NAVIGATION,TypeWeight.TYPE_SPAN_SIZE_12,R.layout.shop_navigation,
                R.mipmap.cloth_with,"配饰"));
        data.add(new ShopBaseItem(ShopItemType.TYPE_NAVIGATION,TypeWeight.TYPE_SPAN_SIZE_12,R.layout.shop_navigation,
                R.mipmap.cloth_brand,"品牌"));
        data.add(new ShopBaseItem(ShopItemType.TYPE_NAVIGATION,TypeWeight.TYPE_SPAN_SIZE_12,R.layout.shop_navigation,
                R.mipmap.cloth_all,"全部"));
        //横线
        data.add(new ShopBaseItem(ShopItemType.TYPE_LINE,TypeWeight.TYPE_SPAN_SIZE_60,R.layout.shop_line,7));
        //标题栏
        data.add(new ShopBaseItem(ShopItemType.TYPE_TITLE,TypeWeight.TYPE_SPAN_SIZE_60,R.layout.shop_title,
                R.mipmap.cloth_great,"精品推荐"));
        //横线
        data.add(new ShopBaseItem(ShopItemType.TYPE_LINE,TypeWeight.TYPE_SPAN_SIZE_60,R.layout.shop_line,0.5f));
        //商品
        for (int i = 0; i < 8; i++) {
            data.add(new ShopBaseItem(ShopItemType.TYPE_GOODS,TypeWeight.TYPE_SPAN_SIZE_30,R.layout.shop_goods
            ,"http://image18-c.poco.cn/mypoco/myphoto/20170301/16/18505011120170301161107098_640.jpg","拉菲庄园2009珍酿原装进口红酒艾格力古堡干红葡","￥ 498.00","已售4件"));
        }
    }


    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
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
    public void back(View v) {
        finish();
    }

    @Override
    public void clkMsg(View v) {

    }

    @Override
    public void clkOther(View v) {

    }
}
