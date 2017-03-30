package com.ascba.rebate.fragments.award;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.AwardAdapter;
import com.ascba.rebate.beans.FirstRec;
import com.ascba.rebate.fragments.base.Base2Fragment;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 二级奖励
 */
public class SecAwardFragment extends BaseAwardFragment implements Base2Fragment.Callback,SuperSwipeRefreshLayout.OnPullRefreshListener {


    private RecyclerView rvSec;
    private SuperSwipeRefreshLayout refreshLatSec;
    private AwardAdapter adapterSec;
    private List<FirstRec> dataSec;
    private View emptyView;

    public SecAwardFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        requestData(UrlUtils.getMyPReferee);
    }

    private void requestData(String url) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    private void initViews() {
        rvSec = getRv();
        refreshLatSec = getRefreshLat();
        refreshLatSec.setOnPullRefreshListener(this);
        adapterSec = getAdapter();
        dataSec = getData();
        emptyView = getActivity().getLayoutInflater().inflate(R.layout.empty_list_view,null);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if(dataSec.size()!=0){
            dataSec.clear();
        }
        refreshLatSec.setRefreshing(false);
        JSONObject recObj = dataObj.optJSONObject("getCashingMoney");
        JSONArray list = recObj.optJSONArray("p_member_list");
        if (list == null || list.length() == 0) {
            adapterSec.setEmptyView(emptyView);
        } else {
            for (int i = 0; i < list.length(); i++) {
                JSONObject memObj = list.optJSONObject(i);
                int member_id = memObj.optInt("member_id");
                String realname = memObj.optString("m_realname");
                String group_name = memObj.optString("m_group_name");
                String cashing_money1 = memObj.optString("cashing_money");
                long create_time = memObj.optLong("create_time");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                String time = sdf.format(new Date(create_time * 1000));
                FirstRec firstRec = new FirstRec(member_id, realname, group_name, cashing_money1, time);
                dataSec.add(firstRec);
            }
            adapterSec.notifyDataSetChanged();
        }
    }

    @Override
    public void handleReqFailed() {
        refreshLatSec.setRefreshing(false);
    }

    @Override
    public void handle404(String message) {
        refreshLatSec.setRefreshing(false);
        getDm().buildAlertDialog(message);
    }

    @Override
    public void handleReLogin() {
        refreshLatSec.setRefreshing(false);
    }

    @Override
    public void handleNoNetWork() {
        refreshLatSec.setRefreshing(false);
        getDm().buildAlertDialog(getActivity().getResources().getString(R.string.no_network));
    }

    @Override
    public void onRefresh() {
        requestData(UrlUtils.getMyPReferee);
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }

}
