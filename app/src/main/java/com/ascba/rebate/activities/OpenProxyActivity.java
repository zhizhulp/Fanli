package com.ascba.rebate.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListPopupWindow;
import android.widget.ListView;
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
import com.ascba.rebate.view.MoneyBar;
import com.jaeger.library.StatusBarUtil;
import com.yolanda.nohttp.rest.Request;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class OpenProxyActivity extends BaseNetWorkActivity implements
        AdapterView.OnItemClickListener,BaseNetWorkActivity.Callback,
        MoneyBar.CallBack
{
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
    private int finalGroupId;
    private int finalGroup;
    private String url="";
    private String group_url;
    private TextView tvProxyName;
    private int finalCityId;
    private CheckBox rbAgree;
    private View searchView;
    private RadioButton rbHave;
    private RadioButton rbNo;
    private int finalScene;
    private DialogManager dm;
    private int finalRegionId;
    private int referee_id;//推荐人id
    private PopupWindow popCities;
    private ListView listView;
    private MoneyBar mb;
    private View agreeProtocol;
    private View noView01;
    private View noView02;
    private View noView03;
    private View noView04;
    private TextView tvProxyNameStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_proxy);
        StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
        getData();
        netRequest(0);
    }

    private void netRequest(int scene) {
        finalScene=scene;
        if(scene==0){//页面数据获取
            Request<JSONObject> request = buildNetRequest(UrlUtils.getUpgraded, 0, true);
            request.add("group_id",group_id);
            request.add("group",group);
            executeNetWork(request,"请稍后");
            setCallback(this);
        }else if(scene==1){//选定地区，刷新数据
            Request<JSONObject> request = buildNetRequest(UrlUtils.backJoinArea, 0, true);
            request.add("region_id",finalCityId);
            executeNetWork(request,"请稍后");
            setCallback(this);
        }else if(scene==2){//获取地区列表
            String s = edCity.getText().toString();
            if("".equals(s)){
                dm.buildAlertDialog("请输入地区");
                return;
            }
            Request<JSONObject> request = buildNetRequest(UrlUtils.getJoinArea, 0, true);
            request.add("region_name", s);
            executeNetWork(request,"请稍后");
            setCallback(this);
        }else if(scene==3){//确认开通
            Request<JSONObject> objRequest = buildNetRequest(UrlUtils.memberUpgraded, 0, true);
            if(finalRegionId==0 && group!=1){
                netRequest(2);
                return;
            }
            objRequest.add("region_id",finalRegionId);
            objRequest.add("region_name",edCity.getText().toString());
            objRequest.add("price",tvMoney.getText().toString());
            String s = tvName.getText().toString();
            if("".equals(s)){
                objRequest.add("referee_id",0);
            }else{
                objRequest.add("referee_id",referee_id);
            }
            objRequest.add("realname",tvName.getText().toString());
            objRequest.add("mobile",tvPhone.getText().toString());
            if(group==1){//VIP组
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

        popCities.dismiss();
        if(cacheList.size()!=0){
            City city = cacheList.get(position);
            String cascade_name = city.getCascade_name();
            finalCityId = city.getCityId();
            edCity.setText(cascade_name);
            edCity.setSelection(cascade_name.length());
            netRequest(1);
        }

    }
    private void getData() {
        Intent intent = getIntent();
        if(intent!=null){
            group_id = intent.getIntExtra("group_id",-3000);
            finalGroupId=group_id;
            group = intent.getIntExtra("group",-3000);
            finalGroup=group;
            if( group==1){
                mb.setTextTitle("开通会员");
                searchView.setVisibility(View.GONE);
                agreeProtocol.setVisibility(View.GONE);
            }else {
                mb.setTextTitle("开通代理");
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
        tvProxyName = ((TextView) findViewById(R.id.proxy_proxy_name));
        rbAgree = ((CheckBox) findViewById(R.id.rb_agree));
        searchView = findViewById(R.id.proxy_city_search);
        agreeProtocol = findViewById(R.id.click_protocol);
        mb = ((MoneyBar) findViewById(R.id.mb_open_proxy));
        mb.setCallBack(this);
        mb.setTailTitle("特权说明");
        noView01 = findViewById(R.id.no_view_01);
        noView02 = findViewById(R.id.no_view_02);
        noView03 = findViewById(R.id.no_view_03);
        noView04 = findViewById(R.id.no_view_04);
        tvProxyNameStr = ((TextView) findViewById(R.id.tv_proxy_name));
    }


    public void checkCity(View view) {
        netRequest(2);
    }

    private void initListPopupWindow2() {
        if (popCities == null) {
            popCities=new PopupWindow(this);
            popCities.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
            popCities.setOutsideTouchable(false);
            View popView=getLayoutInflater().inflate(R.layout.city_list,null);
            listView = ((ListView) popView.findViewById(R.id.listView));
            listView.setOnItemClickListener(this);
            popCities.setContentView(popView);
            popCities.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            popCities.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if (cAdapter == null && cacheList.size() != 0) {
            cAdapter = new CitiesAdapter(cacheList, this);
            listView.setAdapter(cAdapter);
        } else {
            cAdapter.notifyDataSetChanged();
        }
        popCities.showAsDropDown(searchView);
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
        if(finalRegionId==0 && group!=1){
            dm.buildAlertDialog2("请选择地区");
            dm.setCallback(new DialogManager.Callback() {
                @Override
                public void handleSure() {
                    dm.dismissDialog();
                    if(!"".equals(edCity.getText().toString())){
                        netRequest(2);
                    }else {
                        return;
                    }


                }
            });
            return;
        }
        if(group!=1 && "".equals(edCity.getText().toString())){
            dm.buildAlertDialog("请输入代理区域");
            return;
        }
        if(!rbAgree.isChecked()&&group!=1){
            dm.buildAlertDialog("请同意代理协议");
            return;
        }
        dm.buildAlertDialog1("确定开通吗？");
        dm.setCallback(new DialogManager.Callback() {
            @Override
            public void handleSure() {
                dm.dismissDialog();
                netRequest(3);
            }
        });


    }

    //查看协议
    public void goProxyProtocol(View view) {
        if(!"".equals(url)){
            Intent intent=new Intent(this, WebViewBaseActivity.class);
            intent.putExtra("url",url);
            intent.putExtra("name","代理协议");
            startActivity(intent);
        }
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if(finalScene==0){//页面数据获取
            JSONObject upObj = dataObj.optJSONObject("getUpgraded");
            int id= upObj.optInt("id");//自身id
            String name = upObj.optString("name");//代理名称
            if(name!=null){
                if(finalGroup==3){
                    tvProxyNameStr.setText("我同意《"+name+"协议》");
                }else {
                    tvProxyNameStr.setText("我同意《"+name+"权协议》");
                }
            }
            String price = upObj.optString("money");//代理价格
            int group= upObj.optInt("group");//所在用户id
            group_url = upObj.optString("group_url");//组说明
            url=upObj.optString("url");//代理协议
            String realname = upObj.optString("realname");//推荐人
            String mobile = upObj.optString("mobile");//推荐人手机号码
            int isReferee = upObj.optInt("isReferee");//0 无推荐人 1 有推荐人
            referee_id = upObj.optInt("referee_id");//推荐人id
            if(isReferee==0){
                rbNo.setChecked(true);
                rbNo.setEnabled(false);
                rbHave.setEnabled(false);
                noView01.setVisibility(View.GONE);
                noView02.setVisibility(View.GONE);
                noView03.setVisibility(View.GONE);
                noView04.setVisibility(View.GONE);
            }else{
                rbHave.setChecked(true);
                rbNo.setEnabled(false);
                rbHave.setEnabled(false);
                tvName.setText(realname);
                tvPhone.setText(mobile);
                noView01.setVisibility(View.VISIBLE);
                noView02.setVisibility(View.VISIBLE);
                noView03.setVisibility(View.VISIBLE);
                noView04.setVisibility(View.VISIBLE);
            }
            tvMoney.setText(price);
            tvProxyName.setText(name);
        }else if(finalScene==1){//选定地区，刷新数据
            JSONObject bObj = dataObj.optJSONObject("backJoinArea");
            if(bObj!=null){
                finalRegionId = bObj.optInt("id");
                String name = bObj.optString("name");
                if(name!=null){
                    if(finalGroup==3){
                        tvProxyNameStr.setText("我同意《"+name+"协议》");
                    }else {
                        tvProxyNameStr.setText("我同意《"+name+"权协议》");
                    }
                }
                String money = bObj.optString("money");
                finalGroup = bObj.optInt("group");
                url=bObj.optString("url");//代理协议
                group_url = bObj.optString("group_url");//组说明
                finalGroupId=bObj.optInt("group_id");
                tvMoney.setText(money);
                tvProxyName.setText(name);
            }
        }else if(finalScene==2){//获取地区列表
            JSONArray getRegion = dataObj.optJSONArray("getRegion");
            if(getRegion!=null && getRegion.length()!=0){
                cacheList.clear();
                for (int i = 0; i < getRegion.length(); i++) {
                    JSONObject rObj = getRegion.optJSONObject(i);
                    int id = rObj.optInt("id");
                    String cascade_name = rObj.optString("cascade_name");
                    City city=new City(id,cascade_name);
                    cacheList.add(city);
                }
                initListPopupWindow2();
            }else{
                dm.buildAlertDialog(message);
            }
        }else if(finalScene==3){//确认开通
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            //dm.buildAlertDialog(message);
            Intent intent=new Intent(this,UserUpdateActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void clickImage(View im) {

    }

    @Override
    public void clickComplete(View tv) {
        Intent intent=new Intent(this, WebViewBaseActivity.class);
        intent.putExtra("url",group_url);
        intent.putExtra("name","特权说明");
        startActivity(intent);
    }
}
