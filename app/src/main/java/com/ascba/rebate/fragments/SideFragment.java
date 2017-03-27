package com.ascba.rebate.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.BusAdapter;
import com.ascba.rebate.beans.Business;
import com.ascba.rebate.fragments.base.Base2Fragment;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.warmtel.expandtab.ExpandPopTabView;
import com.warmtel.expandtab.KeyValueBean;
import com.warmtel.expandtab.PopOneListView;
import com.warmtel.expandtab.PopTwoListView;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 周边
 */
public class SideFragment extends Base2Fragment implements SuperSwipeRefreshLayout.OnPullRefreshListener, SuperSwipeRefreshLayout.OnPushLoadMoreListener {


    private ExpandPopTabView popTab;
    private List<KeyValueBean> typeAll;//全部
    private List<KeyValueBean> typeSide;//附近
    private List<KeyValueBean> typeAuto;//智能排序
    private RecyclerView busRV;
    private List<Business> data=new ArrayList<>();
    private BusAdapter busAdapter;
    private int nowPage = 1;//当前页数
    private int pageNum = 1;//总页数
    private SuperSwipeRefreshLayout refreshLat;

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
        refreshLat = (SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        refreshLat.setOnPullRefreshListener(this);

        View footView = LayoutInflater.from(getContext()).inflate(R.layout.foot_view, null);
        refreshLat.setFooterView(footView);
        refreshLat.setOnPushLoadMoreListener(this);


        popTab = ((ExpandPopTabView) view.findViewById(R.id.expandtab_view));
        initData();
        addItem(popTab, typeAll, "全部0", "全部0");
        addItem(popTab, typeSide, "附近0", "附近0");
        addItem(popTab, typeAuto, "智能排序0", "智能排序0");

        busRV = ((RecyclerView) view.findViewById(R.id.bus_list));
        requestNetwork();
    }

    private void initBusData() {
        data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(new Business("http://image18-c.poco.cn/mypoco/myphoto/20170301/16/18505011120170301161107098_640.jpg", "金牌炒面", "五星级商家", R.mipmap.main_business_category, "0个评论", "0m"));
        }
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

    @Override
    public void onRefresh() {
        requestNetwork();
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }

    /**
     * 请求周边数据
     */
    private void requestNetwork() {

        if (nowPage <= pageNum) {
            Request<JSONObject> request = buildNetRequest(UrlUtils.getNearBy, 0, false);
            request.add("sign", UrlEncodeUtils.createSign(UrlUtils.getNearBy));
            request.add("now_page", nowPage);
            executeNetWork(request, "请稍后");
            setCallback(new Callback() {
                @Override
                public void handle200Data(JSONObject dataObj, String message) {
                    getBussinData(dataObj);
                    nowPage = dataObj.optInt("now_page");
                    pageNum = dataObj.optInt("total_page");

                    if (refreshLat.isRefreshing()) {
                        refreshLat.setRefreshing(false);
                        refreshLat.setLoadMore(false);
                    }
                }

                @Override
                public void handleReqFailed() {
                    if (refreshLat.isRefreshing()) {
                        refreshLat.setRefreshing(false);
                        refreshLat.setLoadMore(false);
                    }
                }

                @Override
                public void handle404(String message) {
                    getDm().buildAlertDialog(message);
                }

                @Override
                public void handleReLogin() {
                    if (refreshLat.isRefreshing()) {
                        refreshLat.setRefreshing(false);
                        refreshLat.setLoadMore(false);
                    }
                }

                @Override
                public void handleNoNetWork() {
                    if (refreshLat.isRefreshing()) {
                        refreshLat.setRefreshing(false);
                        refreshLat.setLoadMore(false);
                    }
                    getDm().buildAlertDialog(getActivity().getResources().getString(R.string.no_network));
                }
            });
        } else {
            if (refreshLat.isRefreshing()) {
                refreshLat.setRefreshing(false);
                refreshLat.setLoadMore(false);
            }
            showToast("已经到底了");
        }
    }

    /**
     * 解析商家数据
     *
     * @param dataobj
     */
    private void getBussinData(JSONObject dataobj) {
        JSONArray jsonArray = dataobj.optJSONArray("pushBusinessList");
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Business business = new Business();

                //logo
                business.setLogo(UrlUtils.baseWebsite + jsonObject.optString("seller_cover_logo"));
                //店名
                business.setbName(jsonObject.optString("seller_name"));
                data.add(business);

                if (busAdapter == null) {
                    busAdapter = new BusAdapter(R.layout.main_bussiness_list_item, data, getActivity());
                    busRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                    busRV.setAdapter(busAdapter);
                } else {
                    busAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
            }
        }
    }

    @Override
    public void onLoadMore() {
        nowPage++;
        requestNetwork();
    }

    @Override
    public void onPushDistance(int distance) {

    }

    @Override
    public void onPushEnable(boolean enable) {

    }
}
