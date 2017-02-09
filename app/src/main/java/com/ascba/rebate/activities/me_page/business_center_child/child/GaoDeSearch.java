package com.ascba.rebate.activities.me_page.business_center_child.child;

import android.content.Context;
import android.content.Intent;
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
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.ascba.rebate.R;
import com.ascba.rebate.adapter.SearchAdapter;
import com.ascba.rebate.beans.SearchBean;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.gaodemap.PoiOverlay;
import com.ascba.rebate.view.MyAutoCompleteTextView;
import com.ascba.rebate.view.SearchBar;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 演示poi搜索功能
 */
public class GaoDeSearch extends AppCompatActivity implements TextWatcher
        , Inputtips.InputtipsListener
        , PoiSearch.OnPoiSearchListener
        , AdapterView.OnItemClickListener
        , AMap.OnMarkerClickListener
        , AMap.InfoWindowAdapter
        , AMap.OnInfoWindowClickListener
        , GeocodeSearch.OnGeocodeSearchListener
        ,SearchBar.Callback
        ,AMap.OnMarkerDragListener{

    private DialogManager dm;
    private AMap aMap;
    private MapView mapView;
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private GeocodeSearch geocoderSearch;//反向地理编码
    private SearchBar sb;
    private MyAutoCompleteTextView keyWorldsView;
    private List<SearchBean> suggest=new ArrayList<>();
    private AMapLocationClient locationClient;
    private Marker marker1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaode_poisearch);
        StatusBarUtil.setColor(this, 0xffe52020);
        dm = new DialogManager(this);
        initSearchBar();
        initKeywords();//初始化AutoCompleteTextView
        initMaps(savedInstanceState);//初始化地图
        //initLocation();//在地图上标注当前位置
    }


    private void initSearchBar() {
        sb = ((SearchBar) findViewById(R.id.search_bar));
        sb.setCallback(this);
    }


    private void initMaps(Bundle savedInstanceState) {
        /*mapView=((MapView) findViewById(R.id.map));
        mapView.onCreate(savedInstanceState); // 此方法必须重写*/
        if (aMap == null) {
            aMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
//            aMap = mapView.getMap();
            aMap.setOnMarkerClickListener(this);
            aMap.setInfoWindowAdapter(this);
            aMap.setOnInfoWindowClickListener(this);
            aMap.setOnMarkerDragListener(this);
            geocoderSearch = new GeocodeSearch(this);//逆向地理编码
            geocoderSearch.setOnGeocodeSearchListener(this);
        }
    }

    private void initKeywords() {
        keyWorldsView = sb.getTvSearch();
        /**
         * 当输入关键字变化时，动态更新建议列表
         */
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
        Inputtips inputTips = new Inputtips(GaoDeSearch.this, inputquery);
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

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String keyWord) {
        if("".equals(keyWord.trim())){
            dm.buildAlertDialog("请输入关键词");
            return;
        }
        dm.buildWaitDialog("正在搜索").showDialog();
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);//设置查第一页
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dm.dismissDialog();
        aMap.clear();// 清理之前的图标
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    /*List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息*/
                    if (poiItems != null && poiItems.size() > 0) {
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else {
                        dm.buildAlertDialog("无搜索结果");
                    }
                }
            } else {
                dm.buildAlertDialog("无搜索结果");
            }
        } else {
            dm.buildAlertDialog("搜索出现问题");
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        SearchBean searchBean = suggest.get(i);
        keyWorldsView.setText(searchBean.getName());
        keyWorldsView.setSelection(searchBean.getName().length());
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(keyWorldsView.getWindowToken(), 0);
        doSearchQuery(keyWorldsView.getText().toString());
    }

    //maker点击响应事件
    @Override
    public boolean onMarkerClick(Marker marker) {

        /*getInfoWindow(marker);*/
        return false;
    }

    //显示infowindow
    @Override
    public View getInfoWindow(Marker marker) {
        /*View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri,
                null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(marker.getTitle());

        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());*/
        return null;
    }
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    /**
     * 监听点击infowindow窗口事件回调
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        LatLng position = marker.getPosition();
        final double latitude = position.latitude;
        final double longitude = position.longitude;
        dm.buildAlertDialog1("您确定要使用此地址吗？");
        dm.setCallback(new DialogManager.Callback() {
            @Override
            public void handleSure() {
                dm.dismissDialog();
                LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
                getAddress(latLonPoint);
                /*Intent intent = getIntent();
                intent.putExtra("longitude", longitude);//经度 0-180度
                intent.putExtra("latitude", latitude);//纬度 0-90度
                LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
                getAddress(latLonPoint);
                intent.putExtra("location", snippet + title);
                setResult(RESULT_OK, intent);
                finish();*/
            }
        });
    }



    //逆地理编码回调
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        dm.dismissDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                String addressName = result.getRegeocodeAddress().getFormatAddress();
                LatLonPoint point = result.getRegeocodeQuery().getPoint();
                Intent intent = getIntent();
                intent.putExtra("longitude", point.getLongitude());//经度 0-180度
                intent.putExtra("latitude", point.getLatitude());//纬度 0-90度
                intent.putExtra("location", addressName);
                String province = result.getRegeocodeAddress().getProvince();
                String city = result.getRegeocodeAddress().getCity();
                String district = result.getRegeocodeAddress().getDistrict();
                String street = result.getRegeocodeAddress().getTownship();//大红门街道
                if(street.contains("街道")){
                    String[] strS= street.split("街道");
                    street=strS[0];
                }
                if("".equals(city)){
                    intent.putExtra("street",province+"-"+province+"-"+district+"-"+street);//把街道传回去
                }else {
                    intent.putExtra("street",province+"-"+city+"-"+district+"-"+street);//把街道传回去
                }
                setResult(RESULT_OK, intent);
                finish();
            } else {
                dm.buildAlertDialog("没有找到地址");
            }
        } else {
            dm.buildAlertDialog("地理编码错误");
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        dm.buildWaitDialog("请稍后");
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }
    //searchbar回调
    @Override
    public void onItemSelect(AdapterView<?> adapterView, View view, int i, long l) {

    }
    //searchbar回调
    @Override
    public void onSearchClick(View v) {
        doSearchQuery(keyWorldsView.getText().toString());
    }
    /**
     * 方法必须重写
     */
    /*@Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    *//**
     * 方法必须重写
     *//*
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    *//**
     * 方法必须重写
     *//*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }*/
    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mapView.onDestroy();
        destroyLocation();
    }
    /**
     * 初始化并开始定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
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
     * @since 2.8.0
     * @author hongming.wang
     *
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

                marker1=aMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .anchor(0.5f,0.5f)
                        .draggable(true)
                        .infoWindowEnable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.purple_pin)));

                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker1.getPosition(),19));
                marker1.setTitle("长按大头针移动地址");
                marker1.setSnippet("或点击使用此地址");
                marker1.showInfoWindow();
                //设置Marker在屏幕上,不跟随地图移动
                /*Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
                marker1.setPositionByPixels(screenPosition.x,screenPosition.y);*/
            } else {
                dm.buildAlertDialog("定位失败，请稍后再试");
            }

        }
    };

    /**
     * 停止定位
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void stopLocation(){
        // 停止定位
        locationClient.stopLocation();
    }
    /**
     * 销毁定位
     *
     * @since 2.8.0
     * @author hongming.wang
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

    //maker拖动回调
    @Override
    public void onMarkerDragStart(Marker marker) {
    }
    //maker拖动回调
    @Override
    public void onMarkerDrag(Marker marker) {
        marker1.hideInfoWindow();
    }
    //maker拖动回调
    @Override
    public void onMarkerDragEnd(Marker marker) {

        if(marker1!=null){
            marker1.setTitle("长按大头针移动地址");
            marker1.setSnippet("或点击使用此地址");
            marker1.showInfoWindow();
        }
    }

    public void initMyLocation(View view) {
        aMap.clear();
        dm.buildWaitDialog("定位中");
        initLocation();
    }
}