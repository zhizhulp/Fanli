package com.ascba.rebate.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.fragments.me.FourthFragment;
import com.ascba.rebate.utils.UrlUtils;
import com.jaeger.library.StatusBarUtil;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

public class SettingActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        StatusBarUtil.setColor(this, 0xffe52020);
    }
    //进入  个人资料  界面
    public void settingPersonalDataClick(View view) {
        Intent intent=new Intent(this,PersonalDataActivity.class);
        startActivity(intent);
    }
    //进入  实名认证  界面
    public void settingRealNameConfirm(View view) {
        //判断是否实名成功
        requestIsSuccess(UrlUtils.checkCardId);
        /*Intent intent=new Intent(this,RealNameCofirmActivity.class);
        startActivity(intent);*/
    }

    private void requestIsSuccess(String url) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        executeNetWork(request,"请稍候");
        setCallback(this);
    }


    //进入  我的二维码  界面
    public void settingMyQRCode(View view) {
        Intent intent=new Intent(this,QRCodeActivity.class);
        startActivity(intent);
    }
    //进入  安全中心  界面
    public void settingSafeSetting(View view) {
        Intent intent=new Intent(this,SafeSettingActivity.class);
        startActivity(intent);
    }
    //进入  支付设置  界面
    public void settingPay(View view) {
        Intent intent=new Intent(this,PayPasswordChangeActivity.class);
        startActivity(intent);
    }
    //进入  密保设置  界面
    public void goPasswordProtect(View view) {
        Intent intent=new Intent(this,PasswordProtect2Activity.class);
        startActivity(intent);
    }
    //点击退出，清除缓存，进入登录界面
    public void exitUser(View view) {
        SharedPreferences sf = getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
        sf.edit()
                .putInt("uuid",-1000)
                .putString("token","")
                .putLong("expiring_time",-2000)
                .putString("login_phone","")
                .putString("login_password","").apply();
        Intent intent=new Intent(this, FourthFragment.class);
        setResult(2,intent);
        finish();
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
            int isCardId = dataObj.optInt("isCardId");
            if(isCardId==0){
                Intent intent=new Intent(SettingActivity.this,RealNameCofirmActivity.class);
                startActivity(intent);
            }else{
                JSONObject cardData = dataObj.optJSONObject("cardInfo");
                String realname = cardData.optString("realname");
                String cardid = cardData.optString("cardid");
                int sex = cardData.optInt("sex");
                int age = cardData.optInt("age");
                String location = cardData.optString("location");
                Intent intent1=new Intent(SettingActivity.this,RealNameSuccessActivity.class);
                intent1.putExtra("realname",realname);
                intent1.putExtra("cardid",cardid);
                intent1.putExtra("sex",sex);
                intent1.putExtra("age",age);
                intent1.putExtra("location",location);
                startActivity(intent1);
            }
    }
}
