package com.ascba.rebate.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.NetworkBaseActivity;
import com.ascba.rebate.activities.base.WebViewBaseActivity;
import com.ascba.rebate.adapter.PowerUpdateAdapter;
import com.ascba.rebate.beans.Proxy;
import com.ascba.rebate.beans.ProxyProtocol;
import com.ascba.rebate.beans.UpdateTitle;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.LogUtils;
import com.yolanda.nohttp.rest.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserUpdateActivity extends NetworkBaseActivity implements AdapterView.OnItemClickListener {

    private ListView updateListView;
    private List<UpdateTitle> mList;
    private PowerUpdateAdapter powerUpdateAdapter;
    private List<ProxyProtocol> pros=new ArrayList<>();
    private List<Proxy> mProxies=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);
        initView();
        requestForServer();
    }

    private void requestForServer() {
        sendMsgToSevr("http://api.qlqwgw.com/v1/upgradedList",0);
        CheckThread checkThread = getCheckThread();
        final ProgressDialog p=new ProgressDialog(this,R.style.dialog);
        p.setMessage("请稍后");
        PhoneHandler phoneHandler = checkThread.getPhoneHandler();
        phoneHandler.setCallback(phoneHandler.new Callback2(){
            @Override
            public void getMessage(Message msg) {
                p.dismiss();
                super.getMessage(msg);
                JSONObject jObj = (JSONObject) msg.obj;
                int status = jObj.optInt("status");
                JSONObject dataObj = jObj.optJSONObject("data");
                if(status==200){
                    JSONObject pJObj = dataObj.optJSONObject("pReferee");
                    Iterator<String> keys = pJObj.keys();
                    while(keys.hasNext()){
                        String key = keys.next();
                        pros.add(new ProxyProtocol(null,null));
                        mProxies.add(new Proxy());
                        List<Proxy> proxies=new ArrayList<>();
                        JSONArray jsonArray = pJObj.optJSONArray(key);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.optJSONObject(i);
                            String name = jsonObject.optString("name");
                            String money = jsonObject.optString("money");
                            String icon = jsonObject.optString("icon");
                            String url = jsonObject.optString("url");
                            int group = jsonObject.optInt("group");
                            int sort = jsonObject.optInt("sort");
                            String baseurl="http://api.qlqwgw.com";
                            int isUpgraded = jsonObject.optInt("isUpgraded");
                            boolean open;
                            pros.add(new ProxyProtocol(name,url));
                            if(isUpgraded==0){
                                open=false;
                            }else{
                                open=true;
                            }
                            String finalUrl=baseurl+icon;
                            Proxy proxy=new Proxy(finalUrl,name,money,open);
                            mProxies.add(proxy);
                            proxy.setGroup_id(sort);
                            proxies.add(proxy);
                        }
                        UpdateTitle updateTitle=new UpdateTitle(key,proxies);
                        mList.add(updateTitle);
                    }
                    powerUpdateAdapter.notifyDataSetChanged();
                }
            }
        });
        checkThread.start();
        p.show();
    }

    private void initView() {
        updateListView = ((ListView) findViewById(R.id.update_list));
        initData();
        powerUpdateAdapter = new PowerUpdateAdapter(mList,this);
        powerUpdateAdapter.setCallback(new PowerUpdateAdapter.Callback() {
            @Override
            public void clickProtocol(int position) {

                ProxyProtocol proxyProtocol = pros.get(position);
                if(proxyProtocol.getName()!=null){
                    Intent intent=new Intent(UserUpdateActivity.this, WebViewBaseActivity.class);
                    intent.putExtra("name",proxyProtocol.getName());
                    intent.putExtra("url",proxyProtocol.getUrl());
                    startActivity(intent);
                }
            }

            @Override
            public void clickOpen(int position) {
                Proxy proxy = mProxies.get(position);
                if(proxy.getIcon()!=null){
                    Intent intent=new Intent(UserUpdateActivity.this, OpenProxyActivity.class);
                    intent.putExtra("group_id",proxy.getGroup_id());
                    startActivity(intent);
                }
            }
        });
        updateListView.setAdapter(powerUpdateAdapter);
        updateListView.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


    }

    private void initData() {
        mList=new ArrayList<>();
    }
}
