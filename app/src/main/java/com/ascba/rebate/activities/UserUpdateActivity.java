package com.ascba.rebate.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.NetworkBaseActivity;
import com.ascba.rebate.activities.base.WebViewBaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.adapter.PowerUpdateAdapter;
import com.ascba.rebate.beans.Proxy;
import com.ascba.rebate.beans.ProxyProtocol;
import com.ascba.rebate.beans.UpdateTitle;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.UrlUtils;
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
    private SharedPreferences sf;
    private int isCardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);
        initView();
        requestForServer();
    }

    private void requestForServer() {
        sendMsgToSevr(UrlUtils.upgradedList,0);
        CheckThread checkThread = getCheckThread();
        if(checkThread!=null){
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
                    if(status==200){
                        JSONObject dataObj = jObj.optJSONObject("data");
                        JSONObject pJObj = dataObj.optJSONObject("pReferee");
                        isCardId = dataObj.optInt("isCardId");
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
                                String description = jsonObject.optString("description");
                                int group = jsonObject.optInt("group");
                                int id = jsonObject.optInt("id");
                                //int sort = jsonObject.optInt("sort");
                                String baseurl="http://api.qlqwgw.com";
                                int isUpgraded = jsonObject.optInt("isUpgraded");
                                boolean open;
                                pros.add(new ProxyProtocol(name,url));
                                if(isUpgraded==1 || group==0){
                                    open=true;
                                }else{
                                    open=false;
                                }
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
            });
            checkThread.start();
            p.show();
        }

    }

    private void initView() {
        sf=getSharedPreferences("first_login_success_name_password",MODE_PRIVATE);
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
                    if(isCardId==0){
                        Toast.makeText(UserUpdateActivity.this, "请实名认证", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent=new Intent(UserUpdateActivity.this, OpenProxyActivity.class);
                    intent.putExtra("group_id",proxy.getGroup_id());
                    intent.putExtra("group",proxy.getGroup());
                    if(proxy.getGroup()==0){
                        Toast.makeText(UserUpdateActivity.this, "普通会员无需开通", Toast.LENGTH_SHORT).show();
                        return;
                    }
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
