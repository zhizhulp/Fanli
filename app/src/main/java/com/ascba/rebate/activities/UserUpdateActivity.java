package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.adapter.PowerUpdateAdapter;
import com.ascba.rebate.beans.Proxy;
import com.ascba.rebate.beans.UpdateTitle;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.yolanda.nohttp.rest.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserUpdateActivity extends BaseNetWorkActivity implements AdapterView.OnItemClickListener,BaseNetWorkActivity.Callback {

    private ListView updateListView;
    private List<UpdateTitle> mList;
    private PowerUpdateAdapter powerUpdateAdapter;
    private List<Proxy> mProxies=new ArrayList<>();
    private int isCardId;
    private DialogManager dm=new DialogManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);
        initView();
        requestForServer();
    }

    private void requestForServer() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.upgradedList, 0, true);
        executeNetWork(request,"请稍后");
        setCallback(this);
    }

    private void initView() {
        updateListView = ((ListView) findViewById(R.id.update_list));
        initData();
        powerUpdateAdapter = new PowerUpdateAdapter(mList,this);
        updateListView.setOnItemClickListener(this);
        updateListView.setAdapter(powerUpdateAdapter);
        updateListView.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        Proxy proxy = mProxies.get(position);
        if(proxy.getIcon()!=null){
            if(isCardId==0){
                dm.buildAlertDialog("请实名认证");
                return;
            }
            Intent intent=new Intent(UserUpdateActivity.this, OpenProxyActivity.class);
            intent.putExtra("group_id",proxy.getGroup_id());
            intent.putExtra("group",proxy.getGroup());
            if(proxy.getGroup()==0){
                dm.buildAlertDialog("普通会员无需开通");
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
        JSONObject pJObj = dataObj.optJSONObject("pReferee");
        isCardId = dataObj.optInt("isCardId");
        Iterator<String> keys = pJObj.keys();
        while(keys.hasNext()){
            String key = keys.next();
            mProxies.add(new Proxy());
            List<Proxy> proxies=new ArrayList<>();
            JSONArray jsonArray = pJObj.optJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                String name = jsonObject.optString("name");
                String money = jsonObject.optString("money");
                String icon = jsonObject.optString("icon");
                String description = jsonObject.optString("description");
                int group = jsonObject.optInt("group");
                int id = jsonObject.optInt("id");
                int isUpgraded = jsonObject.optInt("isUpgraded");
                boolean open;
                if(isUpgraded==1 || group==0){
                    open=true;
                }else{
                    open=false;
                }
                String baseurl="http://api.qlqwgw.com";
                String finalUrl=baseurl+icon;
                Proxy proxy=new Proxy(finalUrl,name,money,open);
                proxy.setDesc(description);
                proxy.setGroup_id(id);
                proxy.setGroup(group);
                proxies.add(proxy);
                mProxies.add(proxy);
            }
            UpdateTitle updateTitle=new UpdateTitle(key,proxies);
            mList.add(updateTitle);
        }
        powerUpdateAdapter.notifyDataSetChanged();
    }
}
