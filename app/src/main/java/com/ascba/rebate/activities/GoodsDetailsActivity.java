package com.ascba.rebate.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.GoodsDetailsAdapter;
import com.ascba.rebate.beans.GoodsDetailsItem;
import com.ascba.rebate.beans.TypeWeight;
import com.ascba.rebate.view.dropDownMultiPager.DropDownMultiPagerView;
import com.ascba.rebate.view.dropDownMultiPager.ultraPullToRefash.component.PtrFrameLayout;
import com.ascba.rebate.view.dropDownMultiPager.ultraPullToRefash.handler.PtrDefaultHandler;
import com.ascba.rebate.view.pullUpToLoadMoreView.PullUpToLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/02 0002.
 * 商品详情页
 */
@SuppressLint("SetTextI18n")
public class GoodsDetailsActivity extends BaseNetWork4Activity {

    //足记控件
    private PtrFrameLayout ptrLayout;

    //向上拖动查看详情
    private PullUpToLoadMoreView pullUpToLoadMoreView;


    private RecyclerView recyclerView;

    private Context context;

    private GoodsDetailsAdapter adapter;

    private List<GoodsDetailsItem> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        context = this;
        InitPull();
        InitFotoplace();
        InitRecyclerView();
    }

    /**
     * 初始化滑动详情控件
     */
    private void InitPull() {
        pullUpToLoadMoreView = (PullUpToLoadMoreView) findViewById(R.id.activity_goods_details_pv);

        /**
         * 获取当前控件位置
         */
        pullUpToLoadMoreView.setOnCurrPosition(new PullUpToLoadMoreView.OnCurrPosition() {
            @Override
            public void currPosition(int currPosition, boolean isTop) {

                if (currPosition == 0 && isTop) {
                    ptrLayout.setEnabled(true);
                } else {
                    ptrLayout.setEnabled(false);
                }
            }
        });
    }


    /**
     * 初始化足迹控件
     */
    private void InitFotoplace() {
        ptrLayout = (PtrFrameLayout) findViewById(R.id.activity_goods_details_pl);
        final TextView textView = new TextView(this);
        textView.setText("");
        ptrLayout.setHeaderView(textView);
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrLayout.refreshComplete();
                DropDownMultiPagerView dropDownMultiPagerView = new DropDownMultiPagerView(context, getList());
                dropDownMultiPagerView.show();
                dropDownMultiPagerView.setOnDropDownMultiPagerViewItemClick(new DropDownMultiPagerView.OnDropDownMultiPagerViewItemClick() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(context, "position:" + position, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 足迹数据
     *
     * @return
     */
    private List<GoodsDetailsItem> getList() {
        List<GoodsDetailsItem> beanList = new ArrayList<>();
        String url = "http://img5.ph.126.net/6NHiP2WgCjVnd42P3BWFeQ==/2612932208822079285.jpg";
        for (int i = 0; i < 6; i++) {
            GoodsDetailsItem bean = new GoodsDetailsItem(url, "Teenie Weenie小熊春季女装竖纹条纹衬", "￥ 398", "url地址：" + i);
            beanList.add(bean);
        }
        return beanList;
    }

    /**
     * 初始化RecyclerView
     */
    private void InitRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.activity_goods_details_recylerView);

        //加载数据
        getData();

        adapter = new GoodsDetailsAdapter(data, context, pullUpToLoadMoreView);
        final GridLayoutManager manager = new GridLayoutManager(this, TypeWeight.TYPE_SPAN_SIZE_MAX);
        recyclerView.setLayoutManager(manager);
        adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return data.get(position).getSpanSize();
            }
        });
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(context, "position:" + Integer.toString(position), Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 4:
                        //积分增值
                        Intent integralValus=new Intent(context,IntegralValueActivity.class);
                        startActivity(integralValus);
                        break;
                }

            }

        });
    }

    /**
     * 加载数据
     */
    private void getData() {
        data = new ArrayList<>();

        /**
         * 商品轮播展示
         *
         * @param itemType  类型
         * @param resLat    布局id
         * @param pagerUrls 图片地址
         */
        List<String> url = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            url.add("http://image18-c.poco.cn/mypoco/myphoto/20170303/13/18505011120170303135920078_640.jpg");
        }
        data.add(new GoodsDetailsItem(GoodsDetailsItem.TYPE_VIEWPAGER, R.layout.goods_details_viewpager, url));

        /**
         * 商品简单介绍
         *
         * @param itemType    类型
         * @param resLat      布局id
         * @param store_type  商店类型
         * @param goods_title 商品类型
         * @param goods_desc  商品详细内容
         * @param price_new   当前价格
         * @param price_old   旧价格
         */
        data.add(new GoodsDetailsItem(GoodsDetailsItem.TYPE_GOODS_SIMPLE_DESC, R.layout.goods_details_simple_desc, "【自营店】", "NB 530系列男鞋女鞋", "复古跑步鞋休闲鞋运动鞋M530CKA", "￥455", "899￥"));


        /**
         * 售货服务
         * 24小时发货、7天包退
         *
         * @param itemType
         * @param resLat
         * @param strings 内容数组
         */
        String[] strings = new String[]{"24小时发货", "7天包退换", "退货补运费", "支付安全"};
        data.add(new GoodsDetailsItem(GoodsDetailsItem.TYPE_GOODS_SALES_SERVICE, R.layout.goods_details_sales_service, strings));

        /**
         * 分割线
         * @param itemType
         */
        data.add(new GoodsDetailsItem(GoodsDetailsItem.TYPE_GOODS_CUTTINGLINE_WIDE, R.layout.goods_details_cuttingline_wide));


        /**
         * 增值：购买送积分
         * @param itemType
         * @param resLat
         * @param content  内容
         */
        data.add(new GoodsDetailsItem(GoodsDetailsItem.TYPE_GOODS_APPRECIATION, R.layout.goods_details_appreciation, "购买即账户增值20积分"));

        /**
         * 宽分割线
         * @param itemType
         */
        data.add(new GoodsDetailsItem(GoodsDetailsItem.TYPE_GOODS_CUTTINGLINE_WIDE, R.layout.goods_details_cuttingline_wide));

        /**
         * 规格选择
         *
         * @param itemType
         * @param resLat
         * @param content  内容
         */
        data.add(new GoodsDetailsItem(GoodsDetailsItem.TYPE_GOODS_CHOOSE, R.layout.goods_details_choose, "选择 颜色尺码"));

        /**
         * 宽分割线
         * @param itemType
         */
        data.add(new GoodsDetailsItem(GoodsDetailsItem.TYPE_GOODS_CUTTINGLINE_WIDE, R.layout.goods_details_cuttingline_wide));

        /**
         * 宝贝评价、好评率
         * @param itemType
         * @param resLat
         * @param ev_all 所有评价
         * @param ev_good 好评率
         */
        data.add(new GoodsDetailsItem(GoodsDetailsItem.TYPE_GOODS_EVALUATE, R.layout.goods_details_evaluate, 126, 94.7));

        /**
         * 分割线
         * @param itemType
         */
        data.add(new GoodsDetailsItem(GoodsDetailsItem.TYPE_GOODS_CUTTINGLINE, R.layout.goods_details_cuttingline));

        /**
         * 宝贝评价流布局
         */
        String[] flowStrings = new String[]{"有图(435)", "追评(79)", "划算(105)", "物流快(319)", "鞋很舒服(70)", "颜色好(9)"};
        data.add(new GoodsDetailsItem(GoodsDetailsItem.TYPE_GOODS_FLOW, R.layout.goods_details_flow, flowStrings));

        /**
         * 买家评价示例
         * @param itemType
         * @param resLat
         * @param username 买家昵称
         * @param time 时间
         * @param evDesc    评价内容
         * @param chooseDesc 购买规格
         */
        List<String> imgList = new ArrayList<>();
        imgList.add("http://image18-c.poco.cn/mypoco/myphoto/20170303/17/18505011120170303174057035_640.jpg");
        imgList.add("http://image18-c.poco.cn/mypoco/myphoto/20170303/17/18505011120170303174118033_640.jpg");
        data.add(new GoodsDetailsItem(GoodsDetailsItem.TYPE_GOODS_EVALUATE_FIRST, R.layout.goods_details_evaluate_first, "离**人", "2017.02.17", "鞋子很好，走起路来很舒服", "颜色：蓝白 尺寸：40", imgList));

        /**
         * 宽分割线
         * @param itemType
         */
        data.add(new GoodsDetailsItem(GoodsDetailsItem.TYPE_GOODS_CUTTINGLINE_WIDE, R.layout.goods_details_cuttingline_wide));

        /**
         *店铺
         * @param itemType
         * @param resLat
         * @param pagerUrls 图片地址
         * @param goods_logo 店铺名称logo
         * @param store_name    店铺名称
         * @param goods_all 全部商品
         * @param goods_recomm 达人推荐
         * @param ev_desc 描述相符
         * @param ev_service 服务态度
         * @param ev_delivery 发货速度
         */
        List<String> list = new ArrayList<>();
        list.add("http://image18-c.poco.cn/mypoco/myphoto/20170303/18/18505011120170303180014027_640.jpg");
        list.add("http://image18-c.poco.cn/mypoco/myphoto/20170303/18/18505011120170303180044057_640.jpg");
        list.add("http://image18-c.poco.cn/mypoco/myphoto/20170303/18/18505011120170303180121047_640.jpg");
        String logo = "http://image18-c.poco.cn/mypoco/myphoto/20170303/17/18505011120170303175927036_640.jpg";
        data.add(new GoodsDetailsItem(GoodsDetailsItem.TYPE_GOODS_SHOP, R.layout.goods_details_shop, list, logo, "New Balance专卖店", 102, 18, 4.8, 4.8, 4.8));
    }
}
