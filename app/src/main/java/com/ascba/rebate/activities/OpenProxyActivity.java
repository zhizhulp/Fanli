package com.ascba.rebate.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ProviderInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.activities.base.NetworkBaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.adapter.CitiesAdapter;
import com.ascba.rebate.beans.City;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.MySqliteOpenHelper;
import com.ascba.rebate.utils.NetUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.cityList.Text;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OpenProxyActivity extends NetworkBaseActivity implements AdapterView.OnItemClickListener{
    private static final int REQUEST_CITY = 1;
    private static final int REQUEST_MESSAGE = 2;
    //private AutoCompleteTextView tvCity;
    private int group_id;
    private TextView tvMoney;
    private TextView tvName;
    private TextView tvPhone;
    //private List<City> cities=new ArrayList<>();
    private List<City> cacheList=new ArrayList<>();
    //private CitiesAdapter adapter;
    private CitiesAdapter cAdapter;
    private MySqliteOpenHelper db;
    private EditTextWithCustomHint edCity;
    private ListPopupWindow pList;
    private ProgressDialog p;
    private SharedPreferences sf;
    private int finalGroupId;
    private int finalGroup;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            p.dismiss();
            if(msg.what==1){
                if(cacheList.size()!=0){
                    cacheList.clear();
                }
                for (int i = 0; i < 10; i++) {
                    City city=new City(i,"测试"+i,0,i,"A"+i,(3475+i)+"","cas"+i);
                    cacheList.add(city);
                }
                initListPopupWindow();
            }
        }
    };
    private int group;
//    private RadioButton rbHave;
//    private RadioButton rbNo;
    private Button checkBtn;
    private TextView tvProxyName;
    private String finalCascadeId;
    private int finalCityId;
    private RadioButton rbAgree;
    private View searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_proxy);
        initViews();
        getData();
        RequestForSevver();
        //getCityFromServer();
        //getList();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        pList.dismiss();
        if(cacheList.size()!=0){
            City city = cacheList.get(position);
            String cascade_name = city.getCascade_name();
            int cityLevel = city.getCityLevel();
            finalCityId = city.getCityId();
            finalCascadeId = city.getCascade_id();
            edCity.setText(cascade_name);
            requestProxyInfo(cityLevel);//获取用户选择的代理信息
        }

    }

    private void requestProxyInfo(int cityLevel) {
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
        sf=getSharedPreferences("first_login_success_name_password",MODE_PRIVATE);
        //tvCity = ((AutoCompleteTextView) findViewById(R.id.city));
        tvMoney = ((TextView) findViewById(R.id.proxy_money));
        tvName = ((TextView) findViewById(R.id.proxy_name));
        tvPhone = ((TextView) findViewById(R.id.proxy_phone));
        edCity = ((EditTextWithCustomHint) findViewById(R.id.ed_proxy_area));
//        rbHave = ((RadioButton) findViewById(R.id.rb_have));
//        rbNo = ((RadioButton) findViewById(R.id.rb_no));
        checkBtn = ((Button) findViewById(R.id.check_btn));
        tvProxyName = ((TextView) findViewById(R.id.proxy_proxy_name));
        rbAgree = ((RadioButton) findViewById(R.id.rb_agree));
        searchView = findViewById(R.id.proxy_city_search);
    }


    public void checkCity(View view) {
        String s = edCity.getText().toString();
        if("".equals(s)){
            return;
        }
        getCityListFromServer(s);
    }

    private void getCityListFromServer(String s) {
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
    }
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
    private void RequestForSevver() {
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
                        String price = upObj.optString("money");
                        String name = upObj.optString("name");//代理名称
                        String realname = upObj.optString("realname");//推荐人
                        String mobile = upObj.optString("mobile");//推荐人手机号码
                        int isReferee = upObj.optInt("isReferee");//0 无推荐人 1 有推荐人
                        if(isReferee==0){
//                            rbNo.setChecked(true);
                        }else{
//                            rbHave.setChecked(true);
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

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.closeDB();
        }
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
        requestOpen();
    }

    private void requestOpen() {
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
                objRequest.add("group",finalGroup);
                objRequest.add("group_id",finalGroupId);
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

    }
}
