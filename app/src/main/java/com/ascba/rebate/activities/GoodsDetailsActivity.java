package com.ascba.rebate.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.ViewPagerAdapter;
import com.ascba.rebate.beans.GoodsDetailsItem;
import com.ascba.rebate.view.dropDownMultiPager.DropDownMultiPagerView;
import com.ascba.rebate.view.dropDownMultiPager.ultraPullToRefash.component.PtrFrameLayout;
import com.ascba.rebate.view.dropDownMultiPager.ultraPullToRefash.handler.PtrDefaultHandler;
import com.ascba.rebate.view.pullUpToLoadMoreView.PullUpToLoadMoreView;
import com.bumptech.glide.Glide;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

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

    private Context context;

    //viewpager
    private int currentItem;
    private List<View> viewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        context = this;
        InitPull();
        InitFotoplace();

        InitUI();
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
     * 初始化UI
     */
    private void InitUI() {
        /**
         * viewPager
         */
        InitViewPager();

        /**
         * 商品简单介绍
         *
         */
        TextView storeType = (TextView) findViewById(R.id.goods_details_simple_desc_type_store);
        storeType.setText("【自营店】");

        TextView goodsDesc1 = (TextView) findViewById(R.id.goods_details_simple_desc_type_goods1);
        goodsDesc1.setText("NB 530系列男鞋女鞋");

        TextView goodsDesc2 = (TextView) findViewById(R.id.goods_details_simple_desc_type_goods2);
        goodsDesc2.setText("复古跑步鞋休闲鞋运动鞋M530CKA");

        TextView priceNow = (TextView) findViewById(R.id.goods_details_simple_desc_price_now);
        priceNow.setText("￥455");

        TextView priceOld = (TextView) findViewById(R.id.goods_details_simple_desc_price_old);
        priceOld.setText("￥899");
        priceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        /**
         * 售货服务
         * 24小时发货、7天包退换
         */
        String[] strings = new String[]{"24小时发货", "7天包退换", "退货补运费", "支付安全"};

        TextView afterSales1 = (TextView) findViewById(R.id.activity_goods_details_sales_service_text1);
        afterSales1.setText(strings[0]);

        TextView afterSales2 = (TextView) findViewById(R.id.activity_goods_details_sales_service_text2);
        afterSales2.setText(strings[1]);

        TextView afterSales3 = (TextView) findViewById(R.id.activity_goods_details_sales_service_text3);
        afterSales3.setText(strings[2]);

        TextView afterSales4 = (TextView) findViewById(R.id.activity_goods_details_sales_service_text4);
        afterSales4.setText(strings[3]);

        /**
         * 增值：购买送积分
         */
        TextView appreciationText = (TextView) findViewById(R.id.goods_details_appreciation_text);
        appreciationText.setText("购买即账户增值20积分");

        RelativeLayout appreciationRL = (RelativeLayout) findViewById(R.id.goods_details_appreciation_rl);
        appreciationRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IntegralValueActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 规格选择
         */
        TextView chooseText = (TextView) findViewById(R.id.goods_details_choose_text);
        chooseText.setText("选择 颜色尺码");

        RelativeLayout chooseRL = (RelativeLayout) findViewById(R.id.goods_details_choose_rl);
        chooseRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /**
         * 宝贝评价、好评率
         */
        TextView evaluateText2 = (TextView) findViewById(R.id.goods_details_evaluate_text2);
        evaluateText2.setText("(126)");

        TextView evaluateText3 = (TextView) findViewById(R.id.goods_details_evaluate_text3);
        evaluateText3.setText("94.7%");

        RelativeLayout evaluateRL = (RelativeLayout) findViewById(R.id.goods_details_evaluate_rl);
        evaluateRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /**
         * 宝贝评价流布局
         */
        InitFlowLayout();

        /**
         * 买家评价示例
         */
        List<String> imgList = new ArrayList<>();
        imgList.add("http://image18-c.poco.cn/mypoco/myphoto/20170303/17/18505011120170303174057035_640.jpg");
        imgList.add("http://image18-c.poco.cn/mypoco/myphoto/20170303/17/18505011120170303174118033_640.jpg");

        TextView evFirstUsername = (TextView) findViewById(R.id.goods_details_ev_first_username);
        evFirstUsername.setText("离**人");

        TextView evFirstTime = (TextView) findViewById(R.id.goods_details_ev_first_time);
        evFirstTime.setText("2017.02.17");

        TextView evFirstDesc = (TextView) findViewById(R.id.goods_details_ev_first_desc);
        evFirstDesc.setText("鞋子很好，走起路来很舒服");

        TextView evFirstChoose = (TextView) findViewById(R.id.goods_details_ev_first_choose);
        evFirstChoose.setText("颜色：蓝白 尺寸：40");

        ImageView evFirstImg1 = (ImageView) findViewById(R.id.goods_details_ev_first_img1);
        Glide.with(context).load(imgList.get(0)).into(evFirstImg1);

        ImageView evFirstImg2 = (ImageView) findViewById(R.id.goods_details_ev_first_img2);
        Glide.with(context).load(imgList.get(1)).into(evFirstImg2);

        //查看全部
        TextView evFirstBtn = (TextView) findViewById(R.id.goods_details_ev_first_btn);
        evFirstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AllEvaluationActivity.class);
                startActivity(intent);
            }
        });

        /**
         *店铺
         */
        List<String> list = new ArrayList<>();
        list.add("http://image18-c.poco.cn/mypoco/myphoto/20170303/18/18505011120170303180014027_640.jpg");
        list.add("http://image18-c.poco.cn/mypoco/myphoto/20170303/18/18505011120170303180044057_640.jpg");
        list.add("http://image18-c.poco.cn/mypoco/myphoto/20170303/18/18505011120170303180121047_640.jpg");
        String logo = "http://image18-c.poco.cn/mypoco/myphoto/20170303/17/18505011120170303175927036_640.jpg";

        ImageView imgLogo = (ImageView) findViewById(R.id.goods_details_shop_img_logo);
        Glide.with(context).load(logo).into(imgLogo);

        ImageView imageView1 = (ImageView) findViewById(R.id.goods_details_shop_img1);
        Glide.with(context).load(list.get(0)).into(imageView1);

        ImageView imageView2 = (ImageView) findViewById(R.id.goods_details_shop_img2);
        Glide.with(context).load(list.get(1)).into(imageView2);

        ImageView imageView3 = (ImageView) findViewById(R.id.goods_details_shop_img3);
        Glide.with(context).load(list.get(2)).into(imageView3);

        TextView shopName = (TextView) findViewById(R.id.goods_details_shop_text_name);
        shopName.setText("New Balance专卖店");

        TextView shopAll = (TextView) findViewById(R.id.goods_details_shop_text_all);
        shopAll.setText(String.valueOf(102));

        TextView shopRecomm = (TextView) findViewById(R.id.goods_details_shop_img_recomm);
        shopRecomm.setText(String.valueOf(18));

        TextView shopDesc = (TextView) findViewById(R.id.goods_details_shop_text_desc);
        shopDesc.setText(String.valueOf(4.8));

        TextView shopmService = (TextView) findViewById(R.id.goods_details_shop_img_service);
        shopmService.setText(String.valueOf(4.8));

        TextView shopSpeed = (TextView) findViewById(R.id.goods_details_shop_text_speed);
        shopSpeed.setText(String.valueOf(4.8));

        TextView goods1PriceOld = (TextView) findViewById(R.id.goods_details_shop_img1_price_old);
        goods1PriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        TextView goods2PriceOld = (TextView) findViewById(R.id.goods_details_shop_img2_price_old);
        goods2PriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        TextView goods3PriceOld = (TextView) findViewById(R.id.goods_details_shop_img3_price_old);
        goods3PriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        //进店看看
        TextView shopEnter = (TextView) findViewById(R.id.goods_details_shop_img_enter);
        shopEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 初始化Viewpager
     */
    private void InitViewPager() {
        /**
         * 商品轮播展示
         */
        List<String> url = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            url.add("http://image18-c.poco.cn/mypoco/myphoto/20170303/13/18505011120170303135920078_640.jpg");
        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.goods_details_viewpager_vp);

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

    /**
     * 初始化评价流布局
     */
    private void InitFlowLayout() {
        final String[] flowStrings = new String[]{"有图(435)", "追评(79)", "划算(105)", "物流快(319)", "鞋很舒服(70)", "颜色好(9)"};

        final TagFlowLayout flowLayout = (TagFlowLayout) findViewById(R.id.goods_details_flowview);
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
