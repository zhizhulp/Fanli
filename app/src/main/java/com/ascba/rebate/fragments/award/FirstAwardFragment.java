package com.ascba.rebate.fragments.award;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.me_page.MyAwardActivity;
import com.ascba.rebate.adapter.AwardAdapter;
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
 * 一级奖励
 */
public class FirstAwardFragment extends BaseAwardFragment implements BaseNetFragment.Callback ,
        SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView rvFirst;
    private AwardAdapter adapterFirst;
    private List<FirstRec> dataFirst;
    private View emptyView;

    public FirstAwardFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRefreshLayout(view);
        initViews();
        requestData(UrlUtils.getMyReferee);
    }

    private void requestData(String url) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    private void initViews() {
        rvFirst = getRv();
        refreshLayout.setOnRefreshListener(this);
        adapterFirst = getAdapter();
        dataFirst = getData();
        emptyView = getActivity().getLayoutInflater().inflate(R.layout.empty_award_view,null);
        adapterFirst.setEmptyView(emptyView);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if(dataFirst.size()!=0){
            dataFirst.clear();
        }
        refreshLayout.setRefreshing(false);
        JSONObject recObj = dataObj.optJSONObject("getCashingMoney");
        String cashing_money = recObj.optString("cashing_money");
        int p_referee_count = recObj.optInt("p_referee_count");//一级人数
        int pp_referee_count = recObj.optInt("pp_referee_count");
        ((MyAwardActivity) getActivity()).tvAll.setText(cashing_money);
        ((MyAwardActivity) getActivity()).rbOne.setText(p_referee_count + "笔奖励\n一级推广");
        ((MyAwardActivity) getActivity()).rbTwo.setText(pp_referee_count + "笔奖励\n二级级推广");
        JSONArray list = recObj.optJSONArray("p_member_list");
        if (list != null && list.length() != 0) {
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
                dataFirst.add(firstRec);
            }
            adapterFirst.notifyDataSetChanged();
        }
    }

    @Override
    public void handleReqFailed() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void handle404(String message, JSONObject dataObj) {
        refreshLayout.setRefreshing(false);
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
        requestData(UrlUtils.getMyReferee);
    }

}
