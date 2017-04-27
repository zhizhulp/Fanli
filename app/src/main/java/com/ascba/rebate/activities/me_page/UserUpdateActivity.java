package com.ascba.rebate.activities.me_page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.me_page.settings.child.RealNameCofirmActivity;
import com.ascba.rebate.activities.me_page.user_update_child.OpenProxyActivity;
import com.ascba.rebate.adapter.PowerUpdateAdapter;
import com.ascba.rebate.beans.Proxy;
import com.ascba.rebate.beans.UpdateTitle;
import com.ascba.rebate.utils.DialogHome;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ScrollViewWithListView;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class UserUpdateActivity extends BaseNetActivity implements
        AdapterView.OnItemClickListener,
        BaseNetActivity.Callback,
        SwipeRefreshLayout.OnRefreshListener{

    private ScrollViewWithListView updateListView;
    private List<UpdateTitle> mList;
    private PowerUpdateAdapter powerUpdateAdapter;
    private List<Proxy> mProxies=new ArrayList<>();
    private int isCardId;
    private int finalScene;
    private Proxy proxy;//被选中的组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);
        //StatusBarUtil.setColor(this, 0xffe52020);
        initView();
        requestForServer(1);
    }

    private void requestForServer(int scene) {
        finalScene=scene;
        if(scene==1){
            Request<JSONObject> request = buildNetRequest(UrlUtils.upgradedList, 0, true);
            executeNetWork(request,"请稍后");
            setCallback(this);
        }else if(scene==2){
            Request<JSONObject> request = buildNetRequest(UrlUtils.checkCardId, 0, true);
            executeNetWork(request,"请稍后");
            setCallback(this);
        }
    }

    private void initView() {
        updateListView = ((ScrollViewWithListView) findViewById(R.id.update_list));
        initData();
        powerUpdateAdapter = new PowerUpdateAdapter(mList,this);
        updateListView.setOnItemClickListener(this);
        updateListView.setAdapter(powerUpdateAdapter);

        //刷新
        initRefreshLayout();
        refreshLayout.setOnRefreshListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        proxy = mProxies.get(position);
        if(proxy.getIcon()!=null){
            requestForServer(2);//判断是否实名
        }

    }

    private void initData() {
        mList=new ArrayList<>();
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if(finalScene==1){
            mList.clear();
            refreshLayout.setRefreshing(false);
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
                    String member_agency_name = jsonObject.optString("member_agency_name");
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
                    proxy.setOpen_region_name(member_agency_name);
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
        }else if(finalScene==2){

            if(proxy.getGroup()==0){
                getDm().buildAlertDialog("您已经开通普通会员");
                return;
            }
            isCardId = dataObj.optInt("isCardId");
            if(isCardId==0){
                getDm().buildAlertDialog("暂未实名认证，是否立即实名认证？");
                getDm().setCallback(new DialogHome.Callback() {
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
            startActivity(intent);
        }

    }

    @Override
    public void handle404(String message) {

    }

    @Override
    public void handleNoNetWork() {

    }

    @Override
    public void onRefresh() {
        requestForServer(1);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        requestForServer(1);
    }
}
