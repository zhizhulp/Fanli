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
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
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
    private List<City> citys=new ArrayList<>();
    private List<City> cacheList=new ArrayList<>();
    //private CitiesAdapter adapter;
    private CitiesAdapter cAdapter;
    private MySqliteOpenHelper db;
    private EditTextWithCustomHint edCity;
    private ListPopupWindow pList;
    private ProgressDialog p;
    private SharedPreferences sf;
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
    /*private void getList(){
        db=new MySqliteOpenHelper(this);
        List<City> cityList = db.getCity();
        if(cityList!=null && cityList.size()!=0){
            citys.addAll(cityList);
        }
        adapter=new CitiesAdapter(citys,OpenProxyActivity.this);
        tvCity.setAdapter(adapter);
        tvCity.setOnItemClickListener(OpenProxyActivity.this);

    }*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*City city = citys.get(position);
        tvCity.setText(city.getCascade_name());*/
        pList.dismiss();
        if(cacheList.size()!=0){
            City city = cacheList.get(position);
            String cascade_name = city.getCascade_name();
            String cascade_id = city.getCascade_id();
            edCity.setText(cascade_id+":"+cascade_name);
        }

    }
    private void getData() {
        Intent intent = getIntent();
        if(intent!=null){
            group_id = intent.getIntExtra("group_id",-3000);
        }
    }
    private void initViews() {
        sf=getSharedPreferences("first_login_success_name_password",MODE_PRIVATE);
        //tvCity = ((AutoCompleteTextView) findViewById(R.id.city));
        tvMoney = ((TextView) findViewById(R.id.proxy_money));
        tvName = ((TextView) findViewById(R.id.proxy_name));
        tvPhone = ((TextView) findViewById(R.id.proxy_phone));
        edCity = ((EditTextWithCustomHint) findViewById(R.id.ed_proxy_area));

    }


    public void checkCity(View view) {
        String s = edCity.getText().toString();
        if("".equals(s)){
            return;
        }
        getCityListFromServer();
    }

    private void getCityListFromServer() {
        p=new ProgressDialog(this,R.style.dialog);
        p.setMessage("请稍后");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    handler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        p.show();

        /*sendMsgToSevr("",0);
        CheckThread checkThread = getCheckThread();
        Request<JSONObject> objRequest = checkThread.getObjRequest();
        objRequest.add("", "");
        PhoneHandler phoneHandler = checkThread.getPhoneHandler();
        phoneHandler.setCallback(phoneHandler.new Callback2(){
            @Override
            public void getMessage(Message msg) {
                p.dismiss();
                super.getMessage(msg);
                JSONObject jObj = (JSONObject) msg.obj;
                try {
                    int status = jObj.getInt("status");
                    if(status==200){
                        List<City> list=new ArrayList<City>();
                        for (int i = 0; i < 500; i++) {
                            City city=new City(i,"测试"+i,0,i,"A"+i,(3475+i)+"","cas"+i);
                            list.add(city);
                        }
                        if(list.size()!=0){
                            initListPopupWindow(list);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        checkThread.start();
        p.show();*/
    }

    private void initListPopupWindow() {
        if(pList==null){
            pList=new ListPopupWindow(OpenProxyActivity.this);
            pList.setAnchorView(edCity);
            pList.setOnItemClickListener(this);
            pList.setHeight(ListPopupWindow.WRAP_CONTENT);
            pList.setWidth(ListPopupWindow.WRAP_CONTENT);
        }
        if(cAdapter==null){
            cAdapter=new CitiesAdapter(cacheList,this);
            pList.setAdapter(cAdapter);
        }else{
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
        sendMsgToSevr(UrlUtils.getUpgraded,0);
        CheckThread checkThread = getCheckThread();
        if(checkThread!=null){
            final ProgressDialog p=new ProgressDialog(this,R.style.dialog);
            p.setMessage("请稍后");
            Request<JSONObject> objRequest = checkThread.getObjRequest();
            objRequest.add("group_id",group_id);
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
                        String price = upObj.optString("price");
                        String realname = upObj.optString("realname");
                        String mobile = upObj.optString("mobile");
                        int isReferee = upObj.optInt("isReferee");
                        tvMoney.setText(price);
                        tvName.setText(realname);
                        tvPhone.setText(mobile);
                    } else if(status==1||status==2||status==3||status == 4||status==5){//缺少sign参数
                        Intent intent = new Intent(OpenProxyActivity.this, LoginActivity.class);
                        sf.edit().putInt("uuid", -1000).apply();
                        startActivity(intent);
                        finish();
                    } else if(status==404){
                        Toast.makeText(OpenProxyActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if(status==500){
                        Toast.makeText(OpenProxyActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            checkThread.start();
            p.show();
        }

    }
    private void getCityFromServer() {
        sendMsgToSevr("http://api.qlqwgw.com/v1/getRegion",0);
        CheckThread checkThread = getCheckThread();
        PhoneHandler phoneHandler = checkThread.getPhoneHandler();
        Request<JSONObject> objRequest = checkThread.getObjRequest();
        objRequest.add("type","all");//initial：只获取市
        phoneHandler.setCallback(phoneHandler.new Callback2(){
            @Override
            public void getMessage(Message msg) {
                super.getMessage(msg);
                JSONObject jObj = (JSONObject) msg.obj;
                int status = jObj.optInt("status");
                if(status==200){
                    JSONObject dataObj = jObj.optJSONObject("data");
                    JSONArray contentArray = dataObj.optJSONArray("content");
                    if(contentArray!=null){
                        for (int i = 0; i < contentArray.length(); i++) {
                            JSONObject jsonObject = contentArray.optJSONObject(i);
                            int id = jsonObject.optInt("id");
                            String name = jsonObject.optString("name");
                            int level = jsonObject.optInt("level");
                            int pid = jsonObject.optInt("pid");
                            String initial = jsonObject.optString("initial");
                            String cascade_id = jsonObject.optString("cascade_id");
                            String cascade_name = jsonObject.optString("cascade_name");
                            City city=new City(id,name,level,pid,initial,cascade_id,cascade_name);
                            citys.add(city);
                            db.insertIntoCity(city);
                        }


                        //test();
                    }
                }
            }
        });
        checkThread.start();
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
}
