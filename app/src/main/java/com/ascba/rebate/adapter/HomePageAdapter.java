package com.ascba.rebate.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.HomePageMultiItemItem;
import com.ascba.rebate.view.pagerWithTurn.ShufflingViewPager;
import com.ascba.rebate.view.pagerWithTurn.ShufflingViewPagerAdapter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.superplayer.library.SuperPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/11 0011.
 */

public class HomePageAdapter extends BaseMultiItemQuickAdapter<HomePageMultiItemItem, BaseViewHolder> implements SuperPlayer.OnNetChangeListener {

    private Context context;
    private ShufflingViewPager pagerVideo;

    public HomePageAdapter(List<HomePageMultiItemItem> data, Context context) {
        super(data);
        this.context = context;
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                HomePageMultiItemItem item = data.get(i);
                addItemType(item.getItemType(), item.getLayout());
            }
        }
    }


    @Override
    protected void convert(BaseViewHolder helper, HomePageMultiItemItem item) {
        switch (helper.getItemViewType()) {
            case HomePageMultiItemItem.TYPE1:
                /**
                 * 新闻轮播
                 */
                ShufflingViewPager viewPager = helper.getView(R.id.homepage_pager);
                ShufflingViewPagerAdapter adapter = new ShufflingViewPagerAdapter(context, item.getList());
                viewPager.setAdapter(adapter);
                viewPager.start();
                break;
            case HomePageMultiItemItem.TYPE2:
                /**
                 * 赚钱花钱
                 */
                helper.addOnClickListener(R.id.homepage_btn_speedmon);
                helper.addOnClickListener(R.id.homepage_btn_makemon);
                break;
            case HomePageMultiItemItem.TYPE3:
                /**
                 * ASK商学院、创业扶持
                 */
                helper.addOnClickListener(R.id.homepage_btn_college);

                helper.addOnClickListener(R.id.homepage_btn_policy);
                break;

            case HomePageMultiItemItem.TYPE5:
                /**
                 * 券购商城
                 */
                helper.setText(R.id.homepage_text_title, item.getTitle());
                break;

            case HomePageMultiItemItem.TYPE6:
                /**
                 * 全球券购
                 */
                helper.addOnClickListener(R.id.homepage_btn_global);
                ImageView btnGlobalImg = helper.getView(R.id.homepage_btn_global_img);
                Glide.with(context).load("http://image18-c.poco.cn/mypoco/myphoto/20170309/11/18505011120170309114124067_640.jpg").into(btnGlobalImg);

                /**
                 * 天天特价
                 */

                helper.addOnClickListener(R.id.homepage_btn_offer);
                ImageView btnOfferImg = helper.getView(R.id.homepage_btn_offer_img);
                Glide.with(context).load("http://image18-c.poco.cn/mypoco/myphoto/20170309/11/18505011120170309114124067_640.jpg").into(btnOfferImg);

                /**
                 * 品牌精选
                 */
                helper.addOnClickListener(R.id.homepage_btn_selected);
                ImageView btnSelectedImg = helper.getView(R.id.homepage_btn_selected_img);
                Glide.with(context).load("http://image18-c.poco.cn/mypoco/myphoto/20170309/11/18505011120170309114124067_640.jpg").into(btnSelectedImg);
                break;

            case HomePageMultiItemItem.TYPE8:
                /**
                 * ASK资讯
                 */
                helper.setText(R.id.homepage_text_title, item.getTitle());
                break;

            case HomePageMultiItemItem.TYPE9:
                /**
                 * 视频
                 */
                ViewPager viewPager1 = helper.getView(R.id.home_page_video_pager);
//                List<View> viewList = initVideo(item.getList());
//                ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(viewList);
//                viewPager1.setAdapter(pagerAdapter);

                break;
            case HomePageMultiItemItem.TYPE10:
                /**
                 * 最新动态
                 */
                helper.setText(R.id.homepage_text_title, item.getTitle());
                break;

            /**
             * 新闻
             */
            case HomePageMultiItemItem.TYPE11:
                helper.setText(R.id.item_news_head_text1, item.getNewsBeen().get(0).getTitle());
                ImageView img1 = helper.getView(R.id.item_news_head_img1);
                Glide.with(context).load(item.getNewsBeen().get(0).getImg()).into(img1);
                helper.addOnClickListener(R.id.item_news_head_rl1);

                helper.setText(R.id.item_news_head_text2, item.getNewsBeen().get(1).getTitle());
                ImageView img2 = helper.getView(R.id.item_news_head_img2);
                Glide.with(context).load(item.getNewsBeen().get(1).getImg()).into(img2);
                helper.addOnClickListener(R.id.item_news_head_rl2);

                helper.setText(R.id.item_news_head_text3, item.getNewsBeen().get(2).getTitle());
                ImageView img3 = helper.getView(R.id.item_news_head_img3);
                Glide.with(context).load(item.getNewsBeen().get(2).getImg()).into(img3);
                helper.addOnClickListener(R.id.item_news_head_rl3);

                helper.setText(R.id.item_news_head_text4, item.getNewsBeen().get(3).getTitle());
                ImageView img4 = helper.getView(R.id.item_news_head_img4);
                Glide.with(context).load(item.getNewsBeen().get(3).getImg()).into(img4);
                helper.addOnClickListener(R.id.item_news_head_rl4);

                helper.setText(R.id.item_news_head_text5, item.getNewsBeen().get(4).getTitle());
                ImageView img5 = helper.getView(R.id.item_news_head_img5);
                Glide.with(context).load(item.getNewsBeen().get(4).getImg()).into(img5);
                helper.addOnClickListener(R.id.item_news_head_rl5);
                break;

            case HomePageMultiItemItem.TYPE12:
                ImageView imageView = helper.getView(R.id.item_news_icon);
                if (item.getBean().isIcon()) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }
                helper.setText(R.id.item_news_title, item.getBean().getTitle());
                helper.setText(R.id.item_news_time, item.getBean().getTime());
                break;
        }
    }

    /**
     * 初始化视频播放
     */
    private List<View> initVideo(List<String> strings) {
        List<View> viewList = new ArrayList<>();
        for (String string : strings) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_video, null);
            SuperPlayer player = (SuperPlayer) view.findViewById(R.id.super_player);
            player.setLive(false);//true：表示直播地址；false表示点播地址
            player.setNetChangeListener(true)//设置监听手机网络的变化
                    .setOnNetChangeListener(this)//true ： 表示监听网络的变化；false ： 播放的过程中不监听网络的变化
                    .onPrepared(new SuperPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared() {
                            /**
                             * 监听视频是否已经准备完成开始播放。（可以在这里处理视频封面的显示跟隐藏）
                             */
                        }
                    }).onComplete(new Runnable() {
                @Override
                public void run() {
                    /**
                     * 监听视频是否已经播放完成了。（可以在这里处理视频播放完成进行的操作）
                     */
                }
            }).onInfo(new SuperPlayer.OnInfoListener() {
                @Override
                public void onInfo(int what, int extra) {
                    /**
                     * 监听视频的相关信息。
                     */

                }
            }).onError(new SuperPlayer.OnErrorListener() {
                @Override
                public void onError(int what, int extra) {
                    /**
                     * 监听视频播放失败的回调
                     */

                }
            }).setTitle(string)//设置视频的titleName
                    .play(string);//开始播放视频
            player.setScaleType(SuperPlayer.SCALETYPE_FITXY);
            player.setPlayerWH(0, player.getMeasuredHeight());//设置竖屏的时候屏幕的高度，如果不设置会切换后按照16:9的高度重置

            viewList.add(player);
        }
        return viewList;
    }

    /**
     * 网络链接监听类
     */
    @Override
    public void onWifi() {
        Toast.makeText(context,"当前网络环境是WIFI",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMobile() {
        Toast.makeText(context,"当前网络环境是手机网络",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisConnect() {
        Toast.makeText(context,"网络链接断开",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoAvailable() {
        Toast.makeText(context,"无网络链接",Toast.LENGTH_SHORT).show();
    }
}
