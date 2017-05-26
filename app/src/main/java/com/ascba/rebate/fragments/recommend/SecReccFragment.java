package com.ascba.rebate.fragments.recommend;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.me_page.MyRecActivity;
import com.ascba.rebate.adapter.TuiGAdapter;
import com.ascba.rebate.beans.FirstRec;
import com.ascba.rebate.fragments.base.BaseNetFragment;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.loadmore.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.chad.library.adapter.base.loadmore.LoadMoreView.STATUS_DEFAULT;

/**
 * 二级推广
 */
public class SecReccFragment extends BaseReccFragment implements BaseNetFragment.Callback,
        SwipeRefreshLayout.OnRefreshListener {
    private static final int LOAD_MORE_END = 0;
    private static final int LOAD_MORE_ERROR = 1;
    private RecyclerView rvSec;
    private TuiGAdapter adapterSec;
    private List<FirstRec> dataSec;
    private int idAll;
    private int now_page = 1;
    private int total_page;
    private CustomLoadMoreView loadMoreView;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_MORE_END:
                    if (adapterSec != null) {
                        adapterSec.loadMoreEnd(false);
                    }

                    break;
                case LOAD_MORE_ERROR:
                    if (adapterSec != null) {
                        adapterSec.loadMoreFail();
                    }
                    break;
            }
        }
    };

    public SecReccFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        ((MyRecActivity) getActivity()).setListener2(new MyRecActivity.Listener2() {
            @Override
            public void onDataTypeClick(int id) {
                if(dataSec.size()!=0){
                    dataSec.clear();
                }
                now_page=1;
                total_page=0;
                idAll=id;
                requestData(UrlUtils.getSearchPpspread);
            }

        });
        requestData(UrlUtils.getSearchPpspread);
    }

    private void requestData(String url) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        request.add("id", idAll);
        request.add("now_page", now_page);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    private void initViews() {
        rvSec = getRv();
        refreshLayout.setOnRefreshListener(this);
        adapterSec = getAdapter();
        dataSec = getData();
        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.empty_recc_view,null);
        adapterSec.setEmptyView(emptyView);
        initLoadMore();
    }
    private void initLoadMore() {
        loadMoreView = new CustomLoadMoreView();
        adapterSec.setLoadMoreView(loadMoreView);
        adapterSec.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (now_page > total_page && total_page != 0) {
                    handler.sendEmptyMessage(LOAD_MORE_END);
                } else {
                    requestData(UrlUtils.getSearchPpspread);
                }
            }
        });
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {

        refreshLayout.setRefreshing(false);
        if (adapterSec != null) {
            adapterSec.loadMoreComplete();
        }
        if (loadMoreView != null) {
            loadMoreView.setLoadMoreStatus(STATUS_DEFAULT);
        }
        total_page = dataObj.optInt("total_page");
        now_page++;
        JSONArray array = dataObj.optJSONArray("getSearchSpread");
        if (array != null || array.length() != 0) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                FirstRec fr = new FirstRec();
                fr.setName(obj.optString("realname"));
                fr.setGroupName(obj.optString("user_group_name"));
                fr.setMoney(obj.optString("mobile"));
                long create_time = obj.optLong("register_time");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                String time = sdf.format(new Date(create_time * 1000));
                fr.setTime(time);
                dataSec.add(fr);
            }
        }
        adapterSec.notifyDataSetChanged();

    }

    @Override
    public void handleReqFailed() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void handle404(String message, JSONObject dataObj) {
    }

    @Override
    public void handleReLogin() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void handleNoNetWork() {
        refreshLayout.setRefreshing(false);
        getDm().buildAlertDialog(getActivity().getResources().getString(R.string.no_network));
    }

    @Override
    public void onRefresh() {
        if(dataSec.size()!=0){
            dataSec.clear();
        }
        now_page=1;
        total_page=0;
        requestData(UrlUtils.getSearchPpspread);
    }

}
