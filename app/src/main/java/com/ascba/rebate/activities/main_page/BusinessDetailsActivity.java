package com.ascba.rebate.activities.main_page;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork2Activity;
import com.ascba.rebate.activities.me_page.business_center_child.child.BusinessDataActivity;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.StringUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

import java.io.File;

public class BusinessDetailsActivity extends BaseNetWork2Activity implements BaseNetWork2Activity.Callback {
    // 起点位置
    double mLat1 = 39.915291;
    double mLon1 = 116.403857;
    // 终点位置
    double mLat2 = 40.056858;
    double mLon2 = 116.308194;
    private int business_id;
    private ImageView imBusiPic;
    private TextView tvName;
    private TextView tvType;
    private TextView tvAddress;
    private TextView tvPhone;
    private TextView tvTime;
    private TextView tvRate;
    private String seller_description;
    private String seller_tel;
    private AMapLocationClient locationClient = null;
    private DialogManager dm;
    private double lon;
    private double lat;
    private String seller_name;
    private String seller_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);
        initViews();
        getDataFromMain();
    }

    private void initViews() {
        dm=new DialogManager(this);
        imBusiPic = ((ImageView) findViewById(R.id.im_busi_pic));
        tvName = ((TextView) findViewById(R.id.tv_busi_name));
        tvType = ((TextView) findViewById(R.id.tv_busi_type));
        tvAddress = ((TextView) findViewById(R.id.tv_busi_address));
        tvPhone = ((TextView) findViewById(R.id.tv_busi_phone));
        tvTime = ((TextView) findViewById(R.id.tv_busi_time));
        //商家简介特殊处理

        tvRate = ((TextView) findViewById(R.id.tv_busi_rate));
    }

    private void getDataFromMain() {
        Intent intent = getIntent();
        if(intent!=null){
            business_id = intent.getIntExtra("business_id",-1);
            if(business_id!=-1){
                requestServer();
            }
        }
    }

    private void requestServer() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.getBusinesses, 0, true);
        request.add("businesses_id",business_id);
        executeNetWork(request,"请稍后");
        setCallback(this);
    }

    public void goBaiduNavi(View view) {
        initLocation();

    }

    private void startGaodeSearch() {
        if(isInstallByread("com.baidu.BaiduMap")){
            Intent baidu = new Intent();
            baidu.setData(Uri.parse("baidumap://map/geocoder?src=openApiDemo&address="+seller_address));
            startActivity(baidu);
        }else {
            showDialog();
        }

        /*if(getAppIn()){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            Uri uri = Uri.parse("androidamap://viewGeo?sourceApplication=softname&addr="+seller_address);
            intent.setData(uri);
            startActivity(intent);
        }else {
            AMapUtils.getLatestAMapApp(getApplicationContext());
        }*/

    }

    //用户点击了 联系他
    public void goPhone(View view) {
        final PopupWindow p=new PopupWindow(this);
        View popView = getLayoutInflater().inflate(R.layout.pop_click_phone, null);
        p.setContentView(popView);
        p.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });
        View phoneView = popView.findViewById(R.id.pop_phone);
        View cancelView = popView.findViewById(R.id.pop_cancel);
        final TextView tvPhone = (TextView) popView.findViewById(R.id.tv_phone);
        tvPhone.setText(seller_tel);
        phoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = tvPhone.getText().toString();
                if(!phone.equals("")){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    startActivity(intent);
                }

            }
        });
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.dismiss();
            }
        });
        p.setOutsideTouchable(true);
        p.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        p.setHeight(ScreenDpiUtils.dip2px(this,135));
        p.setBackgroundDrawable(new ColorDrawable(0xE8E8E7));
        p.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,0,0);
        //设置背景变暗
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
    }

    public void goDescription(View view) {
        if(!"".equals(seller_description)){
            Intent intent=new Intent(this,ShowDescriptionActivity.class);
            intent.putExtra("seller_description",seller_description);
            startActivity(intent);
        }
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message)   {
        JSONObject seObj = dataObj.optJSONObject("sellerInfo");
        seller_name = seObj.optString("seller_name");
        String seller_taglib = seObj.optString("seller_taglib");
        seller_description = seObj.optString("seller_description");
        seller_address = seObj.optString("seller_address");
        String seller_localhost = seObj.optString("seller_localhost");
        String seller_lon = seObj.optString("seller_lon");
        String seller_lat = seObj.optString("seller_lat");
        if(!StringUtils.isEmpty(seller_lon)){
            lon = Double.parseDouble(seller_lon);
        }
        if(!StringUtils.isEmpty(seller_lat)){
            lat = Double.parseDouble(seller_lat);
        }
        seller_tel = seObj.optString("seller_tel");
        String seller_business_hours = seObj.optString("seller_business_hours");
        String seller_return_ratio = seObj.optString("seller_return_ratio");
        String seller_return_ratio_tip = seObj.optString("seller_return_ratio_tip");
        String seller_image = seObj.optString("seller_image");
        Picasso.with(BusinessDetailsActivity.this).load(UrlUtils.baseWebsite+seller_image).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE).into(imBusiPic);
        try {
            tvName.setText(seller_name);
            tvType.setText(seller_taglib);
            tvAddress.setText(StringUtils.isEmpty(seller_localhost)? seller_address:seller_localhost);
            tvPhone.setText(seller_tel);
            tvTime.setText(seller_business_hours);
            tvRate.setText(handleStr(seller_return_ratio_tip) );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String handleStr(String str) {
        String[] split = str.split("-");
        String type = split[0];
        String rate = split[1];
        String user = split[2];
        String bus = split[3];
        return "消费赠返"+user+"%";
    }

    public void back(View view) {
        finish();
    }
    /**
     * 判断高德地图app是否已经安装
     */
    public boolean getAppIn() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = this.getPackageManager().getPackageInfo(
                    "com.autonavi.minimap", 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        // 本手机没有安装高德地图app
        return packageInfo != null;
    }
    /**
     * 初始化并开始定位
     */
    private void initLocation(){
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        locationClient.startLocation();
    }

    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        /*mOption.setOnceLocation(true);//只定位一次*/
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(10000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            stopLocation();
            if (null != loc) {
                stopLocation();
                mLat1 = loc.getLatitude();
                mLon1 = loc.getLongitude();
                startGaodeSearch();
            } else {
                dm.buildAlertDialog("定位失败，请稍后再试");
            }
        }
    };

    /**
     * 停止定位
     */
    private void stopLocation(){
        // 停止定位
        locationClient.stopLocation();
    }
    /**
     * 销毁定位
     *
     */
    private void destroyLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
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
     * 判断是否安装目标应用
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

}
