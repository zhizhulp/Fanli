package com.ascba.rebate.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.ASKCollegeActivity;
import com.ascba.rebate.adapter.HomePageAdapter;
import com.ascba.rebate.beans.HomePageMultiItemItem;
import com.ascba.rebate.beans.NewsBean;
import com.ascba.rebate.beans.VideoBean;
import com.ascba.rebate.fragments.base.BaseFragment;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/10 0010.
 * 首页
 */

public class HomePageFragment extends BaseFragment implements View.OnClickListener {

    private Context context;

    private RecyclerView recylerview;
    private SuperSwipeRefreshLayout refreshLayout;//刷新
    private HomePageAdapter homePageAdapter;

    /**
     * 头部
     */
    private RelativeLayout homepage_head;

    private int mDistanceY = 0;
    private TextView floatButton;

    private FrameLayout btnAdd;
    private ImageView imgAdd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        initView(view);

    }

    private void initView(View view) {


        /**
         * 悬浮球
         */
        floatButton = (TextView) view.findViewById(R.id.floatButton);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("点我了");
            }
        });
        String text = "本月任务\n已完成30%";
        SpannableString textSpan = new SpannableString(text);
        textSpan.setSpan(new RelativeSizeSpan(1.0f), 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textSpan.setSpan(new RelativeSizeSpan(0.7f), 4, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        floatButton.setText(textSpan);

        /**
         * 刷新
         */
        refreshLayout = (SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        View footView = LayoutInflater.from(context).inflate(R.layout.foot_view, null);
        refreshLayout.setFooterView(footView);

        //加载更多
        refreshLayout.setOnPushLoadMoreListener(new SuperSwipeRefreshLayout.OnPushLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setLoadMore(false);
                    }
                }, 1000);
            }

            @Override
            public void onPushDistance(int distance) {

            }

            @Override
            public void onPushEnable(boolean enable) {

            }
        });

        //刷新
        refreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }

            @Override
            public void onPullDistance(int distance) {
                /**
                 * 刷新隐藏头部
                 */
                if (distance > 0) {
                    homepage_head.setVisibility(View.GONE);
                } else {
                    homepage_head.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPullEnable(boolean enable) {

            }
        });

        /**
         * 头部信息
         */
        homepage_head = (RelativeLayout) view.findViewById(R.id.homepage_head);
        //+号
        btnAdd = (FrameLayout) view.findViewById(R.id.homepage_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
            }
        });
        imgAdd = (ImageView) view.findViewById(R.id.homepage_img_add);


        /**
         * 初始化recylerview
         */
        recylerview = (RecyclerView) view.findViewById(R.id.homepage_recylerview);
        homePageAdapter = new HomePageAdapter(getRecylerViewData(), context);

        recylerview.setLayoutManager(new LinearLayoutManager(context));
        recylerview.setAdapter(homePageAdapter);

        /**
         * 滑动标题栏渐变
         */
        recylerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //滑动的距离
                mDistanceY += dy;
                //toolbar的高度
                int toolbarHeight = homepage_head.getBottom();

                //当滑动的距离 <= toolbar高度的时候，改变Toolbar背景色的透明度，达到渐变的效果
                if (mDistanceY <= toolbarHeight) {
                    float scale = (float) mDistanceY / toolbarHeight;
                    float alpha = scale * 255;
                    homepage_head.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                } else {
                    //将标题栏的颜色设置为完全不透明状态
                    homepage_head.setBackgroundResource(R.color.white);
                }
            }
        });
        recylerview.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.homepage_btn_speedmon:
                        Toast.makeText(context, "花钱", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.homepage_btn_makemon:
                        Toast.makeText(context, "赚钱", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.homepage_btn_policy:
                        //创业扶持
                        break;
                    case R.id.homepage_btn_college:
                        //ASK商学院
                        Intent college = new Intent(getActivity(), ASKCollegeActivity.class);
                        startActivity(college);
                        break;
                }
            }
        });
    }

    private List<HomePageMultiItemItem> getRecylerViewData() {
        List<HomePageMultiItemItem> items = new ArrayList<>();

        /**
         * 广告轮播
         *
         * @param type
         * @param layout
         * @param list
         */
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add("http://image18-c.poco.cn/mypoco/myphoto/20170301/16/18505011120170301161128072_640.jpg");
        }
        items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE1, R.layout.home_page_viewpager, list));

        /**
         * 花钱赚钱
         * @param type
         * @param layout
         */
        items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE2, R.layout.home_page_makemoney));

        /**
         * ASK商学院  创业扶持
         * @param type
         * @param layout
         */
        items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE3, R.layout.home_page_college));

        /**
         * 分割线
         * @param type
         * @param layout
         */
        items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE4, R.layout.item_divider1));

        /**
         * 券购商城
         * @param type
         * @param layout
         * @param title
         */
        items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE5, R.layout.home_page_title, "券购商城"));

        /**
         * 分割线
         * @param type
         * @param layout
         */
        items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE4, R.layout.item_divider1));

        /**
         * 全球券购 天天特价 品牌精选
         * @param type
         * @param layout
         */
        items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE6, R.layout.home_page_comm));

        /**
         * 宽分割线
         * @param type
         * @param layout
         */
        items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE7, R.layout.goods_details_cuttingline_wide));

        /**
         * ASK资讯
         * @param type
         * @param layout
         * @param title
         */
        items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE8, R.layout.home_page_title, "ASK资讯"));

        /**
         * 分割线
         * @param type
         * @param layout
         */
        items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE4, R.layout.item_divider1));

        /**
         * 视频
         */
        List<VideoBean> videoBeen = new ArrayList<>();

        String img1 = "http://image18-c.poco.cn/mypoco/myphoto/20170311/13/18505011120170311135526047_640.jpg";
        String video1 = "http://baobab.wandoujia.com/api/v1/playUrl?vid=9502&editionType=normal";
        VideoBean videoBean1 = new VideoBean(img1, video1);
        videoBeen.add(videoBean1);

        String img2 = "http://image18-c.poco.cn/mypoco/myphoto/20170316/11/18505011120170316110739017_640.jpg";
        String video2 = "http://baobab.wandoujia.com/api/v1/playUrl?vid=9508&editionType=normal";
        VideoBean videoBean2 = new VideoBean(img2, video2);
        videoBeen.add(videoBean2);

        String img3 = "http://image18-c.poco.cn/mypoco/myphoto/20170315/16/18505011120170315160125061_640.jpg";
        String video3 = "http://baobab.wandoujia.com/api/v1/playUrl?vid=8438&editionType=normal";
        VideoBean videoBean3 = new VideoBean(img3, video3);
        videoBeen.add(videoBean3);

        items.add(new HomePageMultiItemItem(videoBeen, HomePageMultiItemItem.TYPE9, R.layout.home_page_videopage));

        /**
         * 宽分割线
         * @param type
         * @param layout
         */
        items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE7, R.layout.goods_details_cuttingline_wide));

        /**
         * 最新动态
         * @param type
         * @param layout
         * @param title
         */
        items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE10, R.layout.home_page_title, "最新动态"));

        /**
         * 分割线
         * @param type
         * @param layout
         */
        items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE4, R.layout.item_divider1));

        /**
         * 新闻
         */
        List<NewsBean> newsBeen = new ArrayList<>();
        String img = "http://image18-c.poco.cn/mypoco/myphoto/20170311/13/18505011120170311135433013_640.jpg";
        newsBeen.add(new NewsBean("实时资讯", img));
        newsBeen.add(new NewsBean("创业动态", img));
        newsBeen.add(new NewsBean("官方公告", img));
        newsBeen.add(new NewsBean("官方公告", img));
        newsBeen.add(new NewsBean("帮助中心", img));
        items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE11, newsBeen, R.layout.home_page_news1));

        for (int i = 0; i < 3; i++) {
            items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE12, new NewsBean(true, "钱来钱往牵手沃尔玛", "2017-03-07"), R.layout.home_page_news2));
        }

        return items;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 弹窗
     */
    private void showPopWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.popwindow_homepage, null);

        //消息
        LinearLayout btnMsg = (LinearLayout) view.findViewById(R.id.pop_hm_msg);
        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("消息");
            }
        });

        //付款
        LinearLayout btnPay = (LinearLayout) view.findViewById(R.id.pop_hm_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("付款");
            }
        });

        //收款
        LinearLayout btnRece = (LinearLayout) view.findViewById(R.id.pop_hm_rece);
        btnRece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("收款");
            }
        });

        //推广码
        LinearLayout btnCode = (LinearLayout) view.findViewById(R.id.pop_hm_code);
        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("推广码");
            }
        });

        PopupWindow window = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        // 设置可以获取焦点
        window.setFocusable(true);
        // 设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        // 更新popupwindow的状态
        window.update();
        // 以下拉的方式显示，并且可以设置显示的位置
        window.showAtLocation(imgAdd, Gravity.TOP | Gravity.RIGHT, ScreenDpiUtils.dip2px(context, 15), ScreenDpiUtils.dip2px(context, 75));
    }

}
