package com.ascba.rebate.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.GoodsDetailsItem;
import com.ascba.rebate.view.pullUpToLoadMoreView.PullUpToLoadMoreView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/02 0002.
 */

public class GoodsDetailsAdapter extends BaseMultiItemQuickAdapter<GoodsDetailsItem, BaseViewHolder> {


    private Context context;

    /**
     * Viewpager
     */
    private int currentItem;
    private List<View> viewList = new ArrayList<>();
    private PullUpToLoadMoreView pullUpToLoadMoreView;


    public GoodsDetailsAdapter(List<GoodsDetailsItem> data, Context context, PullUpToLoadMoreView pullUpToLoadMoreView) {
        super(data);
        this.context = context;
        this.pullUpToLoadMoreView = pullUpToLoadMoreView;
        for (int i = 0; i < data.size(); i++) {
            GoodsDetailsItem item = data.get(i);
            addItemType(item.getItemType(), item.getResLat());
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsDetailsItem item) {
        switch (helper.getItemViewType()) {
            case GoodsDetailsItem.TYPE_VIEWPAGER:
                /**
                 * 商品轮播展示
                 *
                 * @param itemType  类型
                 * @param resLat    布局id
                 * @param pagerUrls 图片地址
                 */
                InitViewPager(helper, item.getPagerUrls());
                break;

            case GoodsDetailsItem.TYPE_GOODS_SIMPLE_DESC:
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
                helper.setText(R.id.goods_details_simple_desc_type_store, item.getStore_type());
                helper.setText(R.id.goods_details_simple_desc_type_goods1, item.getGoods_title());
                helper.setText(R.id.goods_details_simple_desc_type_goods2, item.getGoods_desc());
                helper.setText(R.id.goods_details_simple_desc_price_new, item.getPrice_new());
                helper.setText(R.id.goods_details_simple_desc_price_old, item.getPrice_old());
                break;

            case GoodsDetailsItem.TYPE_GOODS_SALES_SERVICE:
                /**
                 * 售货服务
                 * 24小时发货、7天包退换
                 *
                 * @param itemType
                 * @param resLat
                 * @param strings
                 */
                helper.setText(R.id.activity_goods_details_sales_service_text1, item.getStrings()[0]);
                helper.setText(R.id.activity_goods_details_sales_service_text2, item.getStrings()[1]);
                helper.setText(R.id.activity_goods_details_sales_service_text3, item.getStrings()[2]);
                helper.setText(R.id.activity_goods_details_sales_service_text4, item.getStrings()[3]);
                break;

            case GoodsDetailsItem.TYPE_GOODS_APPRECIATION:
                /**
                 * 增值：购买送积分
                 * @param itemType
                 * @param resLat
                 * @param content
                 */
                helper.setText(R.id.goods_details_appreciation_text, item.getContent());
                break;

            case GoodsDetailsItem.TYPE_GOODS_CHOOSE:
                /**
                 * 选择 颜色尺码
                 * @param itemType
                 * @param resLat
                 * @param content
                 */
                helper.setText(R.id.goods_details_choose_text, item.getContent());
                break;
            case GoodsDetailsItem.TYPE_GOODS_EVALUATE:
                /**
                 * 宝贝评价、好评率
                 * @param itemType
                 * @param resLat
                 * @param ev_all 所有评价
                 * @param ev_good 好评率
                 */
                helper.setText(R.id.goods_details_evaluate_text2, "(" + item.getEv_all() + ")");
                helper.setText(R.id.goods_details_evaluate_text2, item.getEv_good() + "%");
                break;
            case GoodsDetailsItem.TYPE_GOODS_FLOW:
                InitFlowLayout(helper, item.getStrings());
                break;
        }
    }

    /**
     * 初始化ViewPager
     *
     * @param helper
     * @param url
     */
    private void InitViewPager(BaseViewHolder helper, List<String> url) {

        ViewPager viewPager = (ViewPager) helper.getView(R.id.goods_details_viewpager_vp);
        for (int i = 1; i <= url.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.goods_details_viewpager_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.goods_details_viewpager_item_img);
            TextView textView = (TextView) view.findViewById(R.id.goods_details_viewpager_item_text);
            Glide.with(context).load(url.get(i - 1)).into(imageView);
            textView.setText(i + "/" + (url.size()));
            viewList.add(view);
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(viewList);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;//获取位置，即第几页
                Log.i("Guide", "监听改变" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            float startX;
            float startY;
            float endX;
            float endY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        endY = event.getY();
                        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                        //获取屏幕的宽度
                        Point size = new Point();
                        windowManager.getDefaultDisplay().getSize(size);
                        int width = size.x;
                        //首先要确定的是，是否到了最后一页，然后判断是否向左滑动，并且滑动距离是否符合，我这里的判断距离是屏幕宽度的4分之一（这里可以适当控制）
                        if (currentItem == (viewList.size() - 1) && startX - endX > 0 && startX - endX >= (width / 4)) {
                            pullUpToLoadMoreView.scrollToSecond();
                        }
                        break;
                }
                return false;
            }
        });
    }
    //==================================Viewpager初始化结束============================================

    /**
     * 初始化流布局
     *
     * @param helper
     */
    private void InitFlowLayout(BaseViewHolder helper, final String[] flowStrings) {
        final TagFlowLayout flowLayout = helper.getView(R.id.goods_details_flowview);
        final LayoutInflater mInflater = LayoutInflater.from(context);
        flowLayout.setAdapter(new TagAdapter<String>(flowStrings) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.goods_details_flow_item,
                        flowLayout, false);
                tv.setText(s);
                return tv;
            }
        });
        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Toast.makeText(context, flowStrings[position], Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
