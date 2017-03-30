package com.ascba.rebate.fragments.recommend;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.me_page.MyRecActivity;
import com.ascba.rebate.adapter.TuiGAdapter;
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
 * 二级推广
 */
public class SecReccFragment extends BaseReccFragment implements Base2Fragment.Callback,SuperSwipeRefreshLayout.OnPullRefreshListener {
    private RecyclerView rvSec;
    private SuperSwipeRefreshLayout refreshLatSec;
    private TuiGAdapter adapterSec;
    private List<FirstRec> dataSec;
    private View emptyView;
    private int idAll;

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
                idAll=id;
                requestData(id, UrlUtils.getSearchPpspread);
            }

        });
        requestData(idAll,UrlUtils.getSearchPpspread);
    }

    private void requestData(int id,String url) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        request.add("id", id);
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
        JSONArray array = dataObj.optJSONArray("getSearchSpread");
        if (array == null || array.length() == 0) {
            adapterSec.setEmptyView(emptyView);
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
                dataSec.add(fr);
            }
        }
        adapterSec.notifyDataSetChanged();
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
        requestData(idAll,UrlUtils.getSearchPpspread);
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }

}
