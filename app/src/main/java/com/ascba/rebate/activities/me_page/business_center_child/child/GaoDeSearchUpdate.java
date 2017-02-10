package com.ascba.rebate.activities.me_page.business_center_child.child;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.ascba.rebate.R;
import com.ascba.rebate.adapter.SearchAdapter;
import com.ascba.rebate.beans.SearchBean;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.view.MyAutoCompleteTextView;
import com.ascba.rebate.view.SearchBar;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 演示poi搜索功能
 */
public class GaoDeSearchUpdate extends AppCompatActivity implements TextWatcher
        , Inputtips.InputtipsListener
        , AdapterView.OnItemClickListener
        ,SearchBar.Callback {

    private DialogManager dm;
    private AMap aMap;
    private SearchBar sb;
    private MyAutoCompleteTextView keyWorldsView;
    private List<SearchBean> suggest=new ArrayList<>();
    private AMapLocationClient locationClient;
    private Marker marker;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaode_poisearch_update);
        StatusBarUtil.setColor(this, 0xffe52020);
        dm = new DialogManager(this);
        initSearchBar();
        initKeywords();//初始化AutoCompleteTextView
        initMaps();//初始化地图
        initMyLocation();
    }


    private void initSearchBar() {
        sb = ((SearchBar) findViewById(R.id.search_bar));
        sb.setCallback(this);
    }


    private void initMaps() {
        if (aMap == null) {
            aMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
        }
    }
    /**
     * 当输入关键字变化时，动态更新建议列表
     */
    private void initKeywords() {
        keyWorldsView = sb.getTvSearch();
        keyWorldsView.addTextChangedListener(this);
        keyWorldsView.setOnItemClickListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
        String newText = cs.toString().trim();
        if ("".equals(newText)) {
            return;
        }
        InputtipsQuery inputquery = new InputtipsQuery(newText, "");
        Inputtips inputTips = new Inputtips(GaoDeSearchUpdate.this, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        suggest.clear();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            for (int i = 0; i < tipList.size(); i++) {
                SearchBean searchBean=new SearchBean(tipList.get(i).getName(),tipList.get(i).getAddress());
                suggest.add(searchBean);
            }
            SearchAdapter aAdapter = new SearchAdapter(suggest, this);
            keyWorldsView.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        } else {
            dm.buildAlertDialog("无建议信息");
        }
    }




    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        SearchBean searchBean = suggest.get(i);
        keyWorldsView.setText(searchBean.getName());
        keyWorldsView.setSelection(keyWorldsView.getText().length());
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(keyWorldsView.getWindowToken(), 0);
        //搜索出的item点击事件
    }

    /**
     * searchbar回调
     * @param v
     */
    @Override
    public void onSearchClick(View v) {

    }

    /**
     * 初始化并开始定位
     */
    private void initLocation(){
        if(locationClient==null){
            //初始化client
            locationClient = new AMapLocationClient(this.getApplicationContext());
            //设置定位参数
            locationClient.setLocationOption(getDefaultOption());
            // 设置定位监听
            locationClient.setLocationListener(locationListener);
            locationClient.startLocation();
        }else {
            locationClient.startLocation();
        }
    }

    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setOnceLocation(true);//只定位一次
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
            dm.dismissDialog();
            if (null != loc) {
                LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());

                marker=aMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        /*.anchor(0.5f,0.5f)*/
                       /* .icon(BitmapDescriptorFactory.fromResource(R.drawable.purple_pin))*/
                );
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),19));
                //设置Marker在屏幕上,不跟随地图移动
                Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
                marker.setPositionByPixels(screenPosition.x,screenPosition.y);
            } else {
                dm.buildAlertDialog("定位失败，请稍后再试");
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }

    /**
     * 停止定位
     *
     */
    private void stopLocation(){
        // 停止定位
        locationClient.stopLocation();
    }
    /**
     * 销毁定位
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

    public void initMyLocation() {
        aMap.clear();
        dm.buildWaitDialog("定位中");
        initLocation();
    }

}