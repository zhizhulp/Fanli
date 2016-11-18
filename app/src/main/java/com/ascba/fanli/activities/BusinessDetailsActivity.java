package com.ascba.fanli.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;
import com.ascba.fanli.utils.LogUtils;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.poi.BaiduMapPoiSearch;
import com.baidu.mapapi.utils.poi.PoiParaOption;

public class BusinessDetailsActivity extends BaseActivity {
    // 天安门坐标
    double mLat1 = 39.915291;
    double mLon1 = 116.403857;
    // 百度大厦坐标
    double mLat2 = 40.056858;
    double mLon2 = 116.308194;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);
    }
    //返回按钮
    public void back(View view) {
        finish();
    }

    public void goBaiduNavi(View view) {
        startWalkingNavi();
        //startPoiDetails();
    }
    /**
     * 启动百度地图Poi详情页面
     */
    public void startPoiDetails() {
        PoiParaOption para = new PoiParaOption().uid("65e1ee886c885190f60e77ff"); // 天安门
        try {
            BaiduMapPoiSearch.openBaiduMapPoiDetialsPage(para, this);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }

    }
    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(BusinessDetailsActivity.this);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 启动百度地图步行导航(Native)
     *
     */
    public void startWalkingNavi() {
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(mLat2, mLon2);

        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2)
                .startName("天安门").endName("百度大厦");

        try {
            BaiduMapNavigation.openBaiduMapWalkNavi(para, this);
            LogUtils.PrintLog("BusinessDetailsActivity","百度导航");
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showDialog();
        }

    }
}
