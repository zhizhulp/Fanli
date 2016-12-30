package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.adapter.PowerUpdateAdapter;
import com.ascba.rebate.beans.Proxy;
import com.ascba.rebate.beans.UpdateTitle;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ScrollViewWithListView;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.yolanda.nohttp.rest.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class UserUpdateActivity extends BaseNetWorkActivity implements
        AdapterView.OnItemClickListener,
        BaseNetWorkActivity.Callback,
        SuperSwipeRefreshLayout.OnPullRefreshListener {

    private ScrollViewWithListView updateListView;
    private List<UpdateTitle> mList;
    private PowerUpdateAdapter powerUpdateAdapter;
    private List<Proxy> mProxies=new ArrayList<>();
    private int isCardId;
    private DialogManager dm=new DialogManager(this);
    private SuperSwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);
        initView();
        requestForServer();
    }

    private void requestForServer() {
        /*textView.setText("正在刷新");
        imageView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);*/
        Request<JSONObject> request = buildNetRequest(UrlUtils.upgradedList, 0, true);
        executeNetWork(request,"请稍后");
        setCallback(this);
    }

    private void initView() {
        updateListView = ((ScrollViewWithListView) findViewById(R.id.update_list));

        /*View child = LayoutInflater.from(this)
                .inflate(R.layout.layout_head, null);
        progressBar = (ProgressBar) child.findViewById(R.id.pb_view);
        textView = (TextView) child.findViewById(R.id.text_view);
        textView.setText("下拉刷新");
        imageView = (ImageView) child.findViewById(R.id.image_view);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.down_arrow);
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setHeaderView(child);*/

        initData();
        powerUpdateAdapter = new PowerUpdateAdapter(mList,this);
        updateListView.setOnItemClickListener(this);
        updateListView.setAdapter(powerUpdateAdapter);
        swipeRefreshLayout = (SuperSwipeRefreshLayout) findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnPullRefreshListener(this);

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        LogUtils.PrintLog("123",position+"");

        Proxy proxy = mProxies.get(position);
        LogUtils.PrintLog("123","group-->"+proxy.getGroup()+";"+"groupId-->"+proxy.getGroup_id());
        if(proxy.getIcon()!=null){
            if(isCardId==0){
                dm.buildAlertDialog1("暂未实名认证，是否立即实名认证？");
                dm.setCallback(new DialogManager.Callback() {
                    @Override
                    public void handleSure() {
                        Intent intent=new Intent(UserUpdateActivity.this,RealNameCofirmActivity.class);
                        startActivity(intent);
                    }
                });
                return;
            }
            Intent intent=new Intent(UserUpdateActivity.this, OpenProxyActivity.class);
            intent.putExtra("group_id",proxy.getGroup_id());
            intent.putExtra("group",proxy.getGroup());
            if(proxy.getGroup()==0){
                dm.buildAlertDialog("您已经开通普通会员");
                return;
            }
            startActivity(intent);
        }

    }

    private void initData() {
        mList=new ArrayList<>();
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        mList.clear();
        /*progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);*/
        swipeRefreshLayout.setRefreshing(false);
        JSONObject pJObj = dataObj.optJSONObject("pReferee");
        isCardId = dataObj.optInt("isCardId");
        Iterator<String> keys = pJObj.keys();
        while(keys.hasNext()){
            String key = keys.next();
            List<Proxy> proxies=new ArrayList<>();
            JSONArray jsonArray = pJObj.optJSONArray(key);
            int groupId=-1;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                String name = jsonObject.optString("name");
                String money = jsonObject.optString("money");
                String icon = jsonObject.optString("icon");
                String description = jsonObject.optString("description");
                int group = jsonObject.optInt("group");
                int id = jsonObject.optInt("id");
                groupId=group;
                int isUpgraded = jsonObject.optInt("isUpgraded");
                boolean open;
                if(isUpgraded==1 || group==0){
                    open=true;
                }else{
                    open=false;
                }
                String finalUrl=UrlUtils.baseWebsite+icon;
                Proxy proxy=new Proxy(finalUrl,name,money,open);
                proxy.setDesc(description);
                proxy.setGroup_id(id);
                proxy.setGroup(group);
                proxies.add(proxy);
            }
            UpdateTitle updateTitle=new UpdateTitle(groupId,key,proxies);
            mList.add(updateTitle);
        }
        Collections.sort(mList);
        for (int i = 0; i < mList.size(); i++) {
            UpdateTitle updateTitle = mList.get(i);
            List<Proxy> proxies = updateTitle.getmList();
            mProxies.add(new Proxy());
            for (int j = 0; j < proxies.size(); j++) {
                Proxy proxy = proxies.get(j);
                mProxies.add(proxy);
            }
        }
        powerUpdateAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        requestForServer();
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {
        /*textView.setText(enable ? "松开刷新" : "下拉刷新");
        imageView.setVisibility(View.VISIBLE);
        imageView.setRotation(enable ? 180 : 0);*/
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        requestForServer();
    }
}
