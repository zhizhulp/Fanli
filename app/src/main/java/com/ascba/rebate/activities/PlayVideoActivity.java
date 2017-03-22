package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.superplayer.library.SuperPlayer;

/**
 * Created by 李鹏 on 2017/03/22 0022.
 * 视频播放
 */

public class PlayVideoActivity extends BaseNetWork4Activity implements SuperPlayer.OnNetChangeListener {

    private SuperPlayer player;
    private String videoUrl = null;
    private static String extras = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        initData();

        if (videoUrl != null && videoUrl.length() > 0) {
            initViews();
        } else {
            showToast("没有发现资源");
            finish();
        }
    }

    public static void newIndexIntent(Context context, String message) {
        Intent newIntent = new Intent(context, PlayVideoActivity.class);
        newIntent.putExtra(extras, message);
        context.startActivity(newIntent);
    }

    /**
     * 初始化相关的信息
     */
    private void initData() {
        videoUrl = getIntent().getStringExtra(extras);
    }

    private void initViews() {
        player = (SuperPlayer) findViewById(R.id.super_player);
        player.setLive(false);
        player.setNetChangeListener(true)//设置监听手机网络的变化
                .setOnNetChangeListener(this)//实现网络变化的回调
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
        }).setTitle(videoUrl)//设置视频的titleName
                .play(videoUrl);//开始播放视频
        player.setScaleType(SuperPlayer.SCALETYPE_FITXY);
        player.setPlayerWH(0, player.getMeasuredHeight());//设置竖屏的时候屏幕的高度，如果不设置会切换后按照16:9的高度重置
    }

    @Override
    public void onWifi() {

    }

    @Override
    public void onMobile() {

    }

    @Override
    public void onDisConnect() {

    }

    @Override
    public void onNoAvailable() {

    }

    /**
     * 下面的这几个Activity的生命状态很重要
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
//        if (player != null && player.onBackPressed()) {
//            return;
//        }
        super.onBackPressed();
    }
}
