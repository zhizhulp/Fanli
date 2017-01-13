package com.ascba.rebate.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.BusinessArea;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.geocoder.RegeocodeRoad;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.road.Crossroad;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
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
        ,SearchBar.Callback{

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
    private double lon;
    private double lat;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaode_poisearch);
        StatusBarUtil.setColor(this, 0xffe52020);
        dm = new DialogManager(this);
        initSearchBar();
        initKeywords();//初始化AutoCompleteTextView
        initMaps(savedInstanceState);//初始化地图
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            lon = intent.getDoubleExtra("lon",0);
            lat = intent.getDoubleExtra("lat",0);
            if(lon!=0 &&lat!=0){
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.position(new LatLng(lat,lon));
                aMap.clear();
                aMap.addMarker(markerOption);
            }

        }
    }

    private void initSearchBar() {
        sb = ((SearchBar) findViewById(R.id.search_bar));
        sb.setCallback(this);
    }


    private void initMaps(Bundle savedInstanceState) {
        mapView=((MapView) findViewById(R.id.map));
        mapView.onCreate(savedInstanceState); // 此方法必须重写
        if (aMap == null) {
            /*aMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();*/
            aMap = mapView.getMap();
            aMap.setOnMarkerClickListener(this);
            aMap.setInfoWindowAdapter(this);
            aMap.setOnInfoWindowClickListener(this);
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
        query.setPageNum(currentPage);// 设置查第一页
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
        doSearchQuery(keyWorldsView.getText().toString());
    }

    //maker点击响应事件
    @Override
    public boolean onMarkerClick(Marker marker) {

        getInfoContents(marker);
        return false;
    }

    //显示infowindow
    @Override
    public View getInfoWindow(Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri,
                null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(marker.getTitle());

        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        return view;
    }

    /**
     * 监听点击infowindow窗口事件回调
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        LatLng position = marker.getPosition();
        final double latitude = position.latitude;
        final double longitude = position.longitude;
/*        final String snippet = marker.getSnippet();
        final String title = marker.getTitle();*/
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


    @Override
    public View getInfoContents(Marker marker) {
        return null;
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
                /*String street1 = result.getRegeocodeAddress().getStreetNumber().getStreet();//光彩路
                List<BusinessArea> businessAreas = result.getRegeocodeAddress().getBusinessAreas();
                List<Crossroad> crossroads = result.getRegeocodeAddress().getCrossroads();
                List<RegeocodeRoad> roads = result.getRegeocodeAddress().getRoads();*/
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
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}