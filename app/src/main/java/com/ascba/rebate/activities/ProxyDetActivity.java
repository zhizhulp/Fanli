package com.ascba.rebate.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.ProxyDetAdapter;
import com.ascba.rebate.beans.ProxyDet;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MarqueeTextView;
import com.ascba.rebate.view.MoneyBar;
import com.ascba.rebate.view.MyBottomSheet;
import com.ascba.rebate.view.SpaceItemDecoration;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 合伙人专区
 */
public class ProxyDetActivity extends BaseNetActivity implements MoneyBar.CallBack
        ,SwipeRefreshLayout.OnRefreshListener {

    private MoneyBar mb;
    private RecyclerView rv;
    private ProxyDetAdapter adapter;
    private List<ProxyDet> data;
    private MarqueeTextView tvMsg;
    private int index;//切换时的索引id
    private TextView tvProxyName;
    private TextView tvProxyRegion;
    private ArrayList<String> strs=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy_det);
        initViews();
        requestNetWork(UrlUtils.agent,0);
    }

    private void requestNetWork(String url,int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        if(what==0){
            request.add("now_region",index);
        }
        executeNetWork(what,request,"请稍后");
    }

    private void initViews() {
        initRecyclerView();
        initRefreshLayout();
        refreshLayout.setOnRefreshListener(this);
        initHeadView();
        tvMsg = ((MarqueeTextView) findViewById(R.id.tv_msg));
    }

    private void initHeadView() {
        tvProxyName = ((TextView) findViewById(R.id.tv_proxy_name));
        tvProxyRegion = ((TextView) findViewById(R.id.tv_proxy_region));
    }

    private void initRecyclerView() {
        rv = ((RecyclerView) findViewById(R.id.rv));
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.addItemDecoration(new SpaceItemDecoration());

        mb = ((MoneyBar) findViewById(R.id.mb));
        mb.setTailTitle("切换地区");
        mb.setCallBack(this);
        getData();
        adapter = new ProxyDetAdapter(R.layout.proxy_det_item, data);
        rv.setAdapter(adapter);
    }

    private void getData() {
        data = new ArrayList<>();

        data.add(new ProxyDet(R.mipmap.proxy_jrls, "今日流水", "￥ 0"));
        data.add(new ProxyDet(R.mipmap.proxy_zlse, "总流水额", "￥ 0"));
        data.add(new ProxyDet(R.mipmap.proxy_qysj, "区域商家", "0家"));
        data.add(new ProxyDet(R.mipmap.proxy_qyhy, "区域会员", "0人"));
    }

    //切换地区
    @Override
    public void clickComplete(View tv) {
        requestNetWork(UrlUtils.switchLocale,1);
    }

    @Override
    public void onRefresh() {
        requestNetWork(UrlUtils.agent,0);
    }
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        if(what==0){
            stopRefresh();
            JSONObject agentObj = dataObj.optJSONObject("agent");
            int count = agentObj.optInt("agency_count");//代理的区域数量
            tvMsg.setText(agentObj.optString("notice"));
            if(count > 1){
                mb.setTailEnable(true);
            }else {
                mb.setTailEnable(false);
                mb.setTailTitle("");
            }
            JSONObject memberAgencyObj = agentObj.optJSONObject("member_agency");
            tvProxyName.setText(memberAgencyObj.optString("member_realname")+"("+memberAgencyObj.optString("name")+")");
            tvProxyRegion.setText(memberAgencyObj.optString("cascade_name"));
        }else if(what==1){

            if(strs.size()!=0){
                strs.clear();
            }
            JSONArray array = dataObj.optJSONArray("switch_locale");
            if(array!=null && array.length()!=0){
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.optJSONObject(i);
                    strs.add( obj.optString("cascade_name")+ "("+obj.optString("name")+ ")");
                }
            }

            final MyBottomSheet dialog=new MyBottomSheet(this,strs,index);
            dialog.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    ProxyDetActivity.this.index=checkedId;
                    dialog.dismiss();
                    requestNetWork(UrlUtils.agent,0);
                }
            });
            dialog.show();
        }

    }

    protected void mhandle404(int what, JSONObject object, String message) {
        stopRefresh();
        finish();
    }


    protected void mhandleFailed(int what, Exception e) {
        stopRefresh();
    }

    protected void mhandleReLogin(int what) {
        stopRefresh();
    }

    private void stopRefresh(){
        if(refreshLayout!=null && refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
    }
    @Override
    public void clickImage(View im) {

    }

}
