package com.ascba.rebate.fragments.recommend;


import android.os.Bundle;
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
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 一级推广
 */
public class FirstReccFragment extends BaseReccFragment implements BaseNetFragment.Callback,SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView rvFirst;
    private TuiGAdapter adapterFirst;
    private List<FirstRec> dataFirst;
    private View emptyView;
    private int idAll;

    public FirstReccFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRefreshLayout(view);
        initViews();
        ((MyRecActivity) getActivity()).setListener1(new MyRecActivity.Listener1() {
            @Override
            public void onDataTypeClick(int id) {
                idAll=id;
                requestData(id, UrlUtils.getSearchPspread);
            }

        });
        requestData(idAll,UrlUtils.getSearchPspread);
    }

    private void requestData(int id,String url) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        request.add("id", id);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    private void initViews() {
        rvFirst = getRv();
        refreshLayout.setOnRefreshListener(this);
        adapterFirst = getAdapter();
        dataFirst = getData();
        emptyView = getActivity().getLayoutInflater().inflate(R.layout.empty_recc_view,null);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if(dataFirst.size()!=0){
            dataFirst.clear();
        }
        refreshLayout.setRefreshing(false);
        JSONArray array = dataObj.optJSONArray("getSearchSpread");
        if (array == null || array.length() == 0) {
            adapterFirst.setEmptyView(emptyView);
        } else {
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
                dataFirst.add(fr);
            }
        }
        adapterFirst.notifyDataSetChanged();
    }

    @Override
    public void handleReqFailed() {
        refreshLayout.setRefreshing(false);
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
    }

    @Override
    public void onRefresh() {
        requestData(idAll,UrlUtils.getSearchPspread);
    }


}
