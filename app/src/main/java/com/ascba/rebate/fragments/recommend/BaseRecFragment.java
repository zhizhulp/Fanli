package com.ascba.rebate.fragments.recommend;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.MyRecActivity;
import com.ascba.rebate.adapter.TuiGAdapter;
import com.ascba.rebate.beans.FirstRec;
import com.ascba.rebate.fragments.base.Base2Fragment;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 一级推荐碎片
 */
public class BaseRecFragment extends Base2Fragment implements Base2Fragment.Callback
        , SuperSwipeRefreshLayout.OnPullRefreshListener {


    private RecyclerView recListView;
    private TuiGAdapter tGaAdapter;
    private List<FirstRec> data = new ArrayList<>();
    private DialogManager dm;
    private SuperSwipeRefreshLayout refreshLayout;
    private int classes;
    private View emptyView;

    public BaseRecFragment() {
    }

    /**
     * @param classes 级别（一级？ 二级？）
     * @return BaseRecFragment
     */
    public static BaseRecFragment getInstance(int classes) {
        BaseRecFragment fragment = new BaseRecFragment();
        Bundle b = new Bundle();
        b.putInt("classes", classes);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        classes = bundle.getInt("classes");
        return inflater.inflate(R.layout.fragment_first_rec, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dm = new DialogManager(getActivity());
        ((MyRecActivity) getActivity()).setListener(new MyRecActivity.Listener() {
            @Override
            public void onDataTypeClick(int id, int type) {
                classes = type;
                if (type == 0) {
                    requestNetData(id, UrlUtils.getSearchPspread);
                } else {
                    requestNetData(id, UrlUtils.getSearchPpspread);
                }

            }
        });

        refreshLayout = ((SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_layout));
        refreshLayout.setOnPullRefreshListener(this);

        recListView = ((RecyclerView) view.findViewById(R.id.recommend_list));
        recListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tGaAdapter = new TuiGAdapter(data, R.layout.rec_list_item_rec);
        recListView.setAdapter(tGaAdapter);
        if (classes == 0) {
            requestNetData(0, UrlUtils.getSearchPspread);
        } else if (classes == 1) {
            requestNetData(0, UrlUtils.getSearchPpspread);
        }

        emptyView = getActivity().getLayoutInflater().inflate(R.layout.empty_list_view,null);
    }

    private void requestNetData(int id, String url) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        request.add("id", id);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        if (data.size() != 0) {
            data.clear();
        }
        JSONArray array = dataObj.optJSONArray("getSearchSpread");
        if (array != null && array.length() != 0) {
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
                data.add(fr);
            }
        } else {
            tGaAdapter.setEmptyView(emptyView);
        }

        Log.d("BaseRecFragment", "data.size():" + data.size());
        tGaAdapter.notifyDataSetChanged();
    }

    @Override
    public void handleReqFailed() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void handle404(String message) {
        dm.buildAlertDialog(message);
    }

    @Override
    public void handleReLogin() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void handleNoNetWork() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        dm.buildAlertDialog("请打开手机网络");
    }

    @Override
    public void onRefresh() {
        if (classes == 0) {
            requestNetData(0, UrlUtils.getSearchPspread);
        } else {
            requestNetData(0, UrlUtils.getSearchPpspread);
        }

    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }
}
