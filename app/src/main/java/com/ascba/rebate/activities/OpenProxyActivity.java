package com.ascba.rebate.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.activities.base.WebViewBaseActivity;
import com.ascba.rebate.adapter.CitiesAdapter;
import com.ascba.rebate.beans.City;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class OpenProxyActivity extends BaseNetWorkActivity implements AdapterView.OnItemClickListener,BaseNetWorkActivity.Callback{
    private static final int REQUEST_CITY = 1;
    private static final int REQUEST_MESSAGE = 2;
    private int group_id;
    private int group;
    private TextView tvMoney;
    private TextView tvName;
    private TextView tvPhone;
    private List<City> cacheList=new ArrayList<>();
    private CitiesAdapter cAdapter;
    private EditTextWithCustomHint edCity;
    private ListPopupWindow pList;
    private ProgressDialog p;
    private int finalGroupId;
    private int finalGroup;
    private String url="";
    private Button checkBtn;
    private TextView tvProxyName;
    private String finalCascadeId;
    private int finalCityId;
    private RadioButton rbAgree;
    private View searchView;
    private RadioButton rbHave;
    private RadioButton rbNo;
    private int finalScene;
    private int cityLevel;
    private DialogManager dm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_proxy);
        initViews();
        getData();
        netRequest(0);
    }

    private void netRequest(int scene) {
        finalScene=scene;
        if(scene==0){
            Request<JSONObject> request = buildNetRequest(UrlUtils.getUpgraded, 0, true);
            request.add("group_id",group_id);
            request.add("group",group);
            executeNetWork(request,"请稍后");
            setCallback(this);
        }else if(scene==1){
            Request<JSONObject> request = buildNetRequest(UrlUtils.backJoinArea, 0, true);
            request.add("level",cityLevel);
            executeNetWork(request,"请稍后");
            setCallback(this);
        }else if(scene==2){
            String s = edCity.getText().toString();
            if("".equals(s)){
                return;
            }
            Request<JSONObject> request = buildNetRequest(UrlUtils.getJoinArea, 0, true);
            request.add("region_name", s);
            executeNetWork(request,"请稍后");
            setCallback(this);
        }else if(scene==3){
            Request<JSONObject> objRequest = buildNetRequest(UrlUtils.memberUpgraded, 0, true);
            objRequest.add("region_id",finalCityId+","+finalCascadeId);
            objRequest.add("price",tvMoney.getText().toString());
            String s = tvName.getText().toString();
            if("".equals(s)){
                objRequest.add("isReferee",0);
            }else{
                objRequest.add("isReferee",1);
            }
            objRequest.add("realname",tvName.getText().toString());
            objRequest.add("mobile",tvPhone.getText().toString());
            if(group==1){
                objRequest.add("group",group);
                objRequest.add("group_id",group_id);
            }else{
                objRequest.add("group",finalGroup);
                objRequest.add("group_id",finalGroupId);
            }
            executeNetWork(objRequest,"请稍后");
            setCallback(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        pList.dismiss();
        if(cacheList.size()!=0){
            City city = cacheList.get(position);
            String cascade_name = city.getCascade_name();
            cityLevel = city.getCityLevel();
            finalCityId = city.getCityId();
            finalCascadeId = city.getCascade_id();
            edCity.setText(cascade_name);
            netRequest(1);
        }

    }

    /*private void requestProxyInfo(int cityLevel) {
        boolean netAva = NetUtils.isNetworkAvailable(this);
        if(!netAva){
            Toast.makeText(this, "请打开网络", Toast.LENGTH_SHORT).show();
            return;
        }
        sendMsgToSevr(UrlUtils.backJoinArea,0);
        CheckThread checkThread = getCheckThread();
        if(checkThread!=null){
            final ProgressDialog p=new ProgressDialog(this,R.style.dialog);
            p.setMessage("请稍后");
            Request<JSONObject> objRequest = checkThread.getObjRequest();
            objRequest.add("level",cityLevel);
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
                        JSONObject bObj = dataObj.optJSONObject("backJoinArea");
                        if(bObj!=null){
                            finalGroupId = bObj.optInt("id");
                            String name = bObj.optString("name");
                            String money = bObj.optString("money");
                            finalGroup = bObj.optInt("group");
                            tvMoney.setText(money);
                            tvProxyName.setText(name);
                        }
                    }

                }
            });
            checkThread.start();
            p.show();
        }
    }
*/
    private void getData() {
        Intent intent = getIntent();
        if(intent!=null){
            group_id = intent.getIntExtra("group_id",-3000);
            group = intent.getIntExtra("group",-3000);
            if( group==1){
                searchView.setVisibility(View.GONE);
            }
        }
    }
    private void initViews() {
        dm=new DialogManager(this);
        rbHave = ((RadioButton) findViewById(R.id.rb_have));
        rbNo = ((RadioButton) findViewById(R.id.rb_no));
        tvMoney = ((TextView) findViewById(R.id.proxy_money));
        tvName = ((TextView) findViewById(R.id.proxy_name));
        tvPhone = ((TextView) findViewById(R.id.proxy_phone));
        edCity = ((EditTextWithCustomHint) findViewById(R.id.ed_proxy_area));
        checkBtn = ((Button) findViewById(R.id.check_btn));
        tvProxyName = ((TextView) findViewById(R.id.proxy_proxy_name));
        rbAgree = ((RadioButton) findViewById(R.id.rb_agree));
        searchView = findViewById(R.id.proxy_city_search);
    }


    public void checkCity(View view) {
        netRequest(2);
    }

    /*private void getCityListFromServer(String s) {
        boolean netAva = NetUtils.isNetworkAvailable(this);
        if(!netAva){
            Toast.makeText(this, "请打开网络", Toast.LENGTH_SHORT).show();
            return;
        }
        p=new ProgressDialog(this,R.style.dialog);
        sendMsgToSevr(UrlUtils.getJoinArea,0);
        CheckThread checkThread = getCheckThread();
        Request<JSONObject> objRequest = checkThread.getObjRequest();
        objRequest.add("region_name", s);
        PhoneHandler phoneHandler = checkThread.getPhoneHandler();
        phoneHandler.setCallback(phoneHandler.new Callback2(){
            @Override
            public void getMessage(Message msg) {
                p.dismiss();
                super.getMessage(msg);
                JSONObject jObj = (JSONObject) msg.obj;
                try {
                    int status = jObj.getInt("status");
                    String message = jObj.optString("msg");
                    if(status==200){
                        JSONObject dataObj = jObj.getJSONObject("data");
                        Toast.makeText(OpenProxyActivity.this,message, Toast.LENGTH_SHORT).show();
                        JSONArray getRegion = dataObj.getJSONArray("getRegion");
                        if(getRegion!=null && getRegion.length()!=0){
                            cacheList.clear();
                            for (int i = 0; i < getRegion.length(); i++) {
                                JSONObject rObj = getRegion.getJSONObject(i);
                                int id = rObj.getInt("id");
                                String cascade_id = rObj.getString("cascade_id");
                                String cascade_name = rObj.getString("cascade_name");
                                int level = rObj.getInt("level");
                                City city=new City(level,cascade_name,cascade_id,id);
                                cacheList.add(city);
                            }
                            initListPopupWindow();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        checkThread.start();
        p.show();
    }*/
    //返回的城市列表
    private void initListPopupWindow() {
        if (pList == null) {
            pList = new ListPopupWindow(OpenProxyActivity.this);
            pList.setAnchorView(edCity);
            pList.setOnItemClickListener(this);
            pList.setHeight(ListPopupWindow.MATCH_PARENT);
            pList.setWidth(ListPopupWindow.MATCH_PARENT);
        }
        if (cAdapter == null && cacheList.size() != 0) {
            cAdapter = new CitiesAdapter(cacheList, this);
            pList.setAdapter(cAdapter);
        } else {
            cAdapter.notifyDataSetChanged();
        }
        pList.show();
    }

    public void goPopPhone(View view) {
        final PopupWindow p=new PopupWindow(this);

        View popView = getLayoutInflater().inflate(R.layout.pop_service,null);
        p.setContentView(popView);

        p.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        p.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        p.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        p.setOutsideTouchable(true);

        p.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,0,0);
        //设置背景变暗
        WindowManager.LayoutParams params =getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);

        final TextView tvPhone = (TextView) popView.findViewById(R.id.service_phone);
        TextView tvCancel = (TextView) popView.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.dismiss();
            }
        });
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = tvPhone.getText().toString();
                if(!phone.equals("")){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    startActivity(intent);
                }
            }
        });

        p.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

    }
    /*private void RequestForSevver() {
        boolean netAva = NetUtils.isNetworkAvailable(this);
        if(!netAva){
            Toast.makeText(this, "请打开网络", Toast.LENGTH_SHORT).show();
            return;
        }
        sendMsgToSevr(UrlUtils.getUpgraded,0);
        CheckThread checkThread = getCheckThread();
        if(checkThread!=null){
            final ProgressDialog p=new ProgressDialog(this,R.style.dialog);
            p.setMessage("请稍后");
            Request<JSONObject> objRequest = checkThread.getObjRequest();
            objRequest.add("group_id",group_id);
            objRequest.add("group",group);
            PhoneHandler phoneHandler = checkThread.getPhoneHandler();
            phoneHandler.setCallback(phoneHandler.new Callback2(){
                @Override
                public void getMessage(Message msg) {
                    p.dismiss();
                    super.getMessage(msg);
                    JSONObject obj = (JSONObject) msg.obj;
                    String message = obj.optString("msg");
                    int status = obj.optInt("status");
                    if(status==200){
                        JSONObject dataObj = obj.optJSONObject("data");
                        JSONObject upObj = dataObj.optJSONObject("getUpgraded");
                        int id= upObj.optInt("id");//自身id
                        int group= upObj.optInt("group");//所在用户id
                        String integral = upObj.optString("integral");//推荐人所获得积分
                        String price = upObj.optString("money");//代理价格
                        String name = upObj.optString("name");//代理名称
                        String realname = upObj.optString("realname");//推荐人
                        String mobile = upObj.optString("mobile");//推荐人手机号码
                        int isReferee = upObj.optInt("isReferee");//0 无推荐人 1 有推荐人
                        url = upObj.optString("url");//代理协议网址
                        if(isReferee==0){
                            rbNo.setChecked(true);
                            rbNo.setEnabled(false);
                            rbHave.setEnabled(false);
                        }else{
                            rbHave.setChecked(true);
                            rbNo.setEnabled(false);
                            rbHave.setEnabled(false);
                            tvName.setText(realname);
                            tvPhone.setText(mobile);
                        }
                        tvMoney.setText(price);
                        tvProxyName.setText(name);
                    }
                }
            });
            checkThread.start();
            p.show();
        }

    }*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    //留言
    public void leaveMsg(View view) {
        Intent intent=new Intent(this,MessageActivity.class);
        startActivityForResult(intent,REQUEST_MESSAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            return;
        }
        switch (requestCode){
            case REQUEST_CITY:
                String location = data.getStringExtra("location");
                if(location!=null && !"".equals(location)){
                    //tvCity.setText(location);
                }
                break;
            case REQUEST_MESSAGE:
                String msg=data.getStringExtra("message");
                Toast.makeText(this, "留言成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    //确认开通
    public void goOpenProxy(View view) {
        netRequest(3);
    }

    /*private void requestOpen() {
        boolean checked = rbAgree.isChecked();
        if(checked){
            boolean netAva = NetUtils.isNetworkAvailable(this);
            if(!netAva){
                Toast.makeText(this, "请打开网络", Toast.LENGTH_SHORT).show();
                return;
            }
            sendMsgToSevr(UrlUtils.memberUpgraded,0);
            CheckThread checkThread = getCheckThread();
            if(checkThread!=null){//需判空，否则无网络会报错
                final ProgressDialog p=new ProgressDialog(this,R.style.dialog);
                p.setMessage("请稍后");
                Request<JSONObject> objRequest = checkThread.getObjRequest();
                objRequest.add("region_id",finalCityId+","+finalCascadeId);
                objRequest.add("price",tvMoney.getText().toString());
                String s = tvName.getText().toString();
                if("".equals(s)){
                    objRequest.add("isReferee",0);
                }else{
                    objRequest.add("isReferee",1);
                }
                objRequest.add("realname",tvName.getText().toString());
                objRequest.add("mobile",tvPhone.getText().toString());
                if(group==1){
                    objRequest.add("group",group);
                    objRequest.add("group_id",group_id);
                }else{
                    objRequest.add("group",finalGroup);
                    objRequest.add("group_id",finalGroupId);
                }
                PhoneHandler phoneHandler = checkThread.getPhoneHandler();
                phoneHandler.setCallback(phoneHandler.new Callback2(){
                    @Override
                    public void getMessage(Message msg) {
                        p.dismiss();
                        super.getMessage(msg);
                        JSONObject jObj = (JSONObject) msg.obj;
                        int status = jObj.optInt("status");
                        String message = jObj.optString("msg");
                        if(status==200){
                            Toast.makeText(OpenProxyActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                checkThread.start();
                p.show();
            }
        }else{
            Toast.makeText(this, "请同意代理协议", Toast.LENGTH_SHORT).show();
        }

    }*/
    //查看协议
    public void goProxyProtocol(View view) {
        if(!"".equals(url)){
            Intent intent=new Intent(this, WebViewBaseActivity.class);
            intent.putExtra("url",url);
            startActivity(intent);
        }
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if(finalScene==0){
            JSONObject upObj = dataObj.optJSONObject("getUpgraded");
            int id= upObj.optInt("id");//自身id
            int group= upObj.optInt("group");//所在用户id
            String integral = upObj.optString("integral");//推荐人所获得积分
            String price = upObj.optString("money");//代理价格
            String name = upObj.optString("name");//代理名称
            String realname = upObj.optString("realname");//推荐人
            String mobile = upObj.optString("mobile");//推荐人手机号码
            int isReferee = upObj.optInt("isReferee");//0 无推荐人 1 有推荐人
            url = upObj.optString("url");//代理协议网址
            if(isReferee==0){
                rbNo.setChecked(true);
                rbNo.setEnabled(false);
                rbHave.setEnabled(false);
            }else{
                rbHave.setChecked(true);
                rbNo.setEnabled(false);
                rbHave.setEnabled(false);
                tvName.setText(realname);
                tvPhone.setText(mobile);
            }
            tvMoney.setText(price);
            tvProxyName.setText(name);
        }else if(finalScene==1){
            JSONObject bObj = dataObj.optJSONObject("backJoinArea");
            if(bObj!=null){
                finalGroupId = bObj.optInt("id");
                String name = bObj.optString("name");
                String money = bObj.optString("money");
                finalGroup = bObj.optInt("group");
                tvMoney.setText(money);
                tvProxyName.setText(name);
            }
        }else if(finalScene==2){
            dm.buildAlertDialog(message);
            JSONArray getRegion = dataObj.optJSONArray("getRegion");
            if(getRegion!=null && getRegion.length()!=0){
                cacheList.clear();
                for (int i = 0; i < getRegion.length(); i++) {
                    JSONObject rObj = getRegion.optJSONObject(i);
                    int id = rObj.optInt("id");
                    String cascade_id = rObj.optString("cascade_id");
                    String cascade_name = rObj.optString("cascade_name");
                    int level = rObj.optInt("level");
                    City city=new City(level,cascade_name,cascade_id,id);
                    cacheList.add(city);
                }
                initListPopupWindow();
            }
        }else if(finalScene==3){
            dm.buildAlertDialog(message);
        }
    }
}
