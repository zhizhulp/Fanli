package com.ascba.rebate.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.main_page.BusinessDetailsActivity;
import com.ascba.rebate.activities.main_page.CityList;
import com.ascba.rebate.adapter.BusAdapter;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.beans.Business;
import com.ascba.rebate.fragments.base.BaseNetFragment;
import com.ascba.rebate.utils.StringUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.utils.ViewUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.loadmore.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.warmtel.expandtab.ExpandPopTabView;
import com.warmtel.expandtab.KeyValueBean;
import com.warmtel.expandtab.PopOneListView;
import com.warmtel.expandtab.PopTwoListView;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.chad.library.adapter.base.loadmore.LoadMoreView.STATUS_DEFAULT;

/**
 * 周边
 */
public class SideFragment extends BaseNetFragment implements
        SwipeRefreshLayout.OnRefreshListener, BaseNetFragment.Callback {

    private static final int LOAD_MORE_END = 0;
    private static final int LOAD_MORE_ERROR = 1;
    private static final int REQUEST_LOCATE = 2;
    private ExpandPopTabView popTab;
    private List<KeyValueBean> typeAll;//全部
    private List<KeyValueBean> typeSide;//附近
    private List<KeyValueBean> typeAuto;//智能排序
    private RecyclerView busRV;
    private List<Business> data = new ArrayList<>();
    private BusAdapter adapter;
    private int now_page = 1;//当前页数
    private int total_page;//总页数
    private CustomLoadMoreView loadMoreView;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_MORE_END:
                    adapter.loadMoreEnd(false);
                    break;
                case LOAD_MORE_ERROR:
                    if (adapter != null) {
                        adapter.loadMoreFail();
                    }
                    break;
            }
        }
    };
    private TextView tvLocate;
    private AMapLocationClient locationClient = null;
    private double lon;
    private double lat;
    private String region_name;//地区
    private int finalScene;
    private EditTextWithCustomHint etSearch;
    private String keywords;

    public SideFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_side, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        initRefreshLayout(view);
        refreshLayout.setOnRefreshListener(this);
        /*popTab = ((ExpandPopTabView) view.findViewById(R.id.expandtab_view));
        initData();
        addItem(popTab, typeAll, "全部0", "全部0");
        addItem(popTab, typeSide, "附近0", "附近0");
        addItem(popTab, typeAuto, "智能排序0", "智能排序0");*/

        busRV = ((RecyclerView) view.findViewById(R.id.bus_list));
        busRV.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), BusinessDetailsActivity.class);
                Business business = data.get(position);
                intent.putExtra("business_id", business.getId());
                startActivity(intent);
            }
        });
        initLocation();//定位
        goLocation(view);//进入城市列表
        initSearch(view);//搜索
    }

    private void initSearch(View view) {
        etSearch = ((EditTextWithCustomHint) view.findViewById(R.id.side_et_search));
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isEmpty(s.toString())) {
                    finalScene = 0;
                    clearData();
                    resetPage();
                    requestNetwork(0);
                }
            }
        });
        view.findViewById(R.id.tv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keywords = etSearch.getText().toString();
                if (!StringUtils.isEmpty(etSearch.getText().toString())) {
                    finalScene = 1;
                    clearData();
                    resetPage();
                    requestNetwork(1);
                } else {
                    getDm().buildAlertDialog("请输入商家名称");
                }

            }
        });
    }

    private void goLocation(View view) {
        View locateLat = view.findViewById(R.id.side_locate_lat);
        locateLat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CityList.class);
                startActivityForResult(intent, REQUEST_LOCATE);
            }
        });
        tvLocate = ((TextView) view.findViewById(R.id.side_tv_locate));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case REQUEST_LOCATE:
                String city = data.getStringExtra("city");
                if (!StringUtils.isEmpty(city)) {
                    finalScene = 0;
                    clearData();
                    resetPage();
                    region_name = city;
                    requestNetwork(0);
                }
                tvLocate.setText(city);
                break;
        }
    }

    private void requestNetwork(int scene) {

        Request<JSONObject> request = buildNetRequest(UrlUtils.getNearBy, 0, false);
        request.add("sign", UrlEncodeUtils.createSign(UrlUtils.getNearBy));
        if (scene == 0) {
            request.add("now_page", now_page);
            request.add("lon", lon);
            request.add("lat", lat);
            request.add("region_name", region_name);
        } else {//搜索
            request.add("seller_name", keywords);
        }
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    /**
     * 初始化筛选菜单数据
     */
    private void initData() {
        typeAll = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            typeAll.add(new KeyValueBean("all", "全部" + i));
        }
        typeSide = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            typeSide.add(new KeyValueBean("side", "附近" + i));
        }
        typeAuto = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            typeAuto.add(new KeyValueBean("auto", "智能排序" + i));
        }
    }

    //一级
    public void addItem(ExpandPopTabView expandTabView, List<KeyValueBean> lists, String defaultSelect, String defaultShowText) {
        PopOneListView popOneListView = new PopOneListView(getActivity());
        popOneListView.setDefaultSelectByValue(defaultSelect);
        //popViewOne.setDefaultSelectByKey(defaultSelect);
        popOneListView.setCallBackAndData(lists, expandTabView, new PopOneListView.OnSelectListener() {
            @Override
            public void getValue(String key, String value) {
                Log.e("tag", "key :" + key + " ,value :" + value);
            }
        });
        expandTabView.addItemToExpandTab(defaultShowText, popOneListView);
    }

    //二级
    public void addItem(ExpandPopTabView expandTabView, List<KeyValueBean> parentLists,
                        List<ArrayList<KeyValueBean>> childrenListLists, String defaultParentSelect, String defaultChildSelect, String defaultShowText) {
        PopTwoListView popTwoListView = new PopTwoListView(getActivity());
        popTwoListView.setDefaultSelectByValue(defaultParentSelect, defaultChildSelect);
        //distanceView.setDefaultSelectByKey(defaultParent, defaultChild);
        popTwoListView.setCallBackAndData(expandTabView, parentLists, childrenListLists, new PopTwoListView.OnSelectListener() {
            @Override
            public void getValue(String showText, String parentKey, String childrenKey) {
                Log.e("tag", "showText :" + showText + " ,parentKey :" + parentKey + " ,childrenKey :" + childrenKey);
            }
        });
        expandTabView.addItemToExpandTab(defaultShowText, popTwoListView);
    }


    private void initLoadMore() {

        if (loadMoreView == null) {
            loadMoreView = new CustomLoadMoreView();
            adapter.setLoadMoreView(loadMoreView);
        }
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (now_page > total_page - 1 && total_page != 0) {
                    handler.sendEmptyMessage(LOAD_MORE_END);
                } else {
                    requestNetwork(finalScene);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        initLocation();
        resetPage();
        clearData();

        requestNetwork(finalScene);

    }


    private void getPageCount(JSONObject dataObj) {
        total_page = dataObj.optInt("total_page");
        now_page++;
    }

    /**
     * 解析商家数据
     */
    private void getBussinData(JSONObject dataobj) {
        JSONArray jsonArray = dataobj.optJSONArray("pushBusinessList");
        if (jsonArray != null && jsonArray.length() != 0) {

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                Business business = new Business();
                //logo
                business.setLogo(UrlUtils.baseWebsite + jsonObject.optString("seller_cover_logo"));
                //店名
                business.setbName(jsonObject.optString("seller_name"));
                business.setId(jsonObject.optInt("id"));
                int distance = jsonObject.optInt("earth_radius");
                int is_new = jsonObject.optInt("is_news");
                if (distance >= 1000) {
                    double a = distance / 1000.0;
                    DecimalFormat f = new DecimalFormat("##0.0");
                    String dd = f.format(a);
                    business.setDistance((dd + "km"));
                } else {
                    business.setDistance((distance + "m"));
                }
                business.setbCategory(jsonObject.optString("seller_taglib"));
                business.setNew(is_new == 1);
                data.add(business);
            }
        } else {
            adapter.setEmptyView(ViewUtils.getEmptyView(getActivity(), "暂无商家数据"));
        }

    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        refreshLayout.setRefreshing(false);
        if (adapter != null) {
            adapter.loadMoreComplete();
        }
        if (loadMoreView != null) {
            loadMoreView.setLoadMoreStatus(STATUS_DEFAULT);
        }
        //分页
        getPageCount(dataObj);
        getBussinData(dataObj);
        initadapter();
        initLoadMore();

    }

    private void initadapter() {
        if (adapter == null) {
            adapter = new BusAdapter(R.layout.main_bussiness_list_item, data, getActivity());
            busRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            busRV.setAdapter(adapter);
            adapter.setEmptyView(ViewUtils.getEmptyView(getActivity(), "暂无商家信息"));
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void handleReqFailed() {
        refreshLayout.setRefreshing(false);
        handler.sendEmptyMessage(LOAD_MORE_ERROR);
    }

    @Override
    public void handle404(String message, JSONObject dataObj) {
        getDm().buildAlertDialog(message);
    }


    @Override
    public void handleReLogin() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void handleNoNetWork() {
        refreshLayout.setRefreshing(false);
        handler.sendEmptyMessage(LOAD_MORE_ERROR);
    }

    /**
     * 初始化并开始定位
     */
    private void initLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            checkAndRequestAllPermission(permissions);
            setRequestPermissionAndBack(new PermissionCallback() {
                @Override
                public void requestPermissionAndBack(boolean isOk) {
                    finalScene = 0;
                    if (isOk) {
                        initLocationListener();
                    } else {
                        requestNetwork(0);
                    }
                }
            });
        } else {
            initLocationListener();
        }

    }

    private void initLocationListener() {
        //初始化client
        locationClient = new AMapLocationClient(getActivity());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        locationClient.startLocation();
    }

    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
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
            if (null != loc) {
                stopLocation();
                tvLocate.setText(loc.getCity());
                region_name = loc.getCity();
                lat = loc.getLatitude();
                lon = loc.getLongitude();
                clearData();
                finalScene = 0;
                requestNetwork(0);
            } else {
                Toast.makeText(getActivity(), "定位失败", Toast.LENGTH_SHORT).show();
                stopLocation();
            }
        }
    };

    /**
     * 停止定位
     */
    private void stopLocation() {
        locationClient.stopLocation();
    }

    private void clearData() {
        if (data.size() != 0) {
            data.clear();
        }
    }

    private void resetPage() {
        if (now_page != 1) {
            now_page = 1;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
