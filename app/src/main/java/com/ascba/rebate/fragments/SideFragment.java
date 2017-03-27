package com.ascba.rebate.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.ascba.rebate.view.loadmore.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
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

import static com.chad.library.adapter.base.loadmore.LoadMoreView.STATUS_DEFAULT;

/**
 * 周边
 */
public class SideFragment extends Base2Fragment implements SuperSwipeRefreshLayout.OnPullRefreshListener, SuperSwipeRefreshLayout.OnPushLoadMoreListener, Base2Fragment.Callback {

    private static final int LOAD_MORE_END = 0;
    private static final int LOAD_MORE_ERROR = 1;
    private ExpandPopTabView popTab;
    private List<KeyValueBean> typeAll;//全部
    private List<KeyValueBean> typeSide;//附近
    private List<KeyValueBean> typeAuto;//智能排序
    private RecyclerView busRV;
    private List<Business> data = new ArrayList<>();
    private BusAdapter adapter;
    private int now_page = 1;//当前页数
    private int total_page;//总页数
    private SuperSwipeRefreshLayout refreshLat;
    private CustomLoadMoreView loadMoreView;
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
    private boolean isRefresh = true;//true 下拉刷新 false 上拉加载

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

        popTab = ((ExpandPopTabView) view.findViewById(R.id.expandtab_view));
        initData();
        addItem(popTab, typeAll, "全部0", "全部0");
        addItem(popTab, typeSide, "附近0", "附近0");
        addItem(popTab, typeAuto, "智能排序0", "智能排序0");

        busRV = ((RecyclerView) view.findViewById(R.id.bus_list));
        requestNetwork();
    }

    private void requestNetwork() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.getNearBy, 0, false);
        request.add("sign", UrlEncodeUtils.createSign(UrlUtils.getNearBy));
        request.add("now_page", now_page);
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
        if (isRefresh) {
            isRefresh = false;
        }
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
                    requestNetwork();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;
        }
        now_page = 1;
        if (data.size() != 0) {
            data.clear();
        }
        requestNetwork();
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }


    private void getPageCount(JSONObject dataObj) {
        total_page = dataObj.optInt("total_page");
        now_page++;
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

            } catch (JSONException e) {
            }
        }
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onPushDistance(int distance) {

    }

    @Override
    public void onPushEnable(boolean enable) {

    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        refreshLat.setRefreshing(false);
        if (adapter != null) {
            adapter.loadMoreComplete();
        }
        if (loadMoreView != null) {
            loadMoreView.setLoadMoreStatus(STATUS_DEFAULT);
        }
        //分页
        getPageCount(dataObj);

        if (isRefresh) {//下拉刷新
            getBussinData(dataObj);
            initadapter();
            initLoadMore();
        } else {//上拉加载
            getBussinData(dataObj);
        }
    }

    private void initadapter() {
        if (adapter == null) {
            adapter = new BusAdapter(R.layout.main_bussiness_list_item, data, getActivity());
            busRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            busRV.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void handleReqFailed() {
        refreshLat.setRefreshing(false);
        handler.sendEmptyMessage(LOAD_MORE_ERROR);
    }


    @Override
    public void handle404(String message) {
        getDm().buildAlertDialog(message);
    }



    @Override
    public void handleReLogin() {
        refreshLat.setRefreshing(false);
    }

    @Override
    public void handleNoNetWork() {

        refreshLat.setRefreshing(false);
        handler.sendEmptyMessage(LOAD_MORE_ERROR);
        getDm().buildAlertDialog(getActivity().getResources().getString(R.string.no_network));
    }

}
