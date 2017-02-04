package com.ascba.rebate.activities.me_page.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.me_page.settings.child.SafeSettingActivity;
import com.ascba.rebate.activities.me_page.settings.child.PasswordProtect2Activity;
import com.ascba.rebate.activities.me_page.settings.child.PayPasswordChangeActivity;
import com.ascba.rebate.activities.me_page.settings.child.PersonalDataActivity;
import com.ascba.rebate.activities.me_page.settings.child.QRCodeActivity;
import com.ascba.rebate.activities.me_page.settings.child.RealNameCofirmActivity;
import com.ascba.rebate.activities.me_page.settings.child.real_name_confirm.RealNameSuccessActivity;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.fragments.me.FourthFragment;
import com.ascba.rebate.utils.UrlUtils;
import com.jaeger.library.StatusBarUtil;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

public class SettingActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback {
    private View qrView;
    private int finalScene;
    private View qrLineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
        finalScene=2;
        requestIsSuccess(UrlUtils.checkCardId);
    }

    private void initViews() {
        qrView = findViewById(R.id.setting_my_qr);
        qrLineView = findViewById(R.id.setting_my_qr_line);
    }

    //进入  个人资料  界面
    public void settingPersonalDataClick(View view) {
        Intent intent = new Intent(this, PersonalDataActivity.class);
        startActivity(intent);
    }

    //进入  实名认证  界面
    public void settingRealNameConfirm(View view) {
        //判断是否实名成功
        finalScene=1;
        requestIsSuccess(UrlUtils.checkCardId);
    }

    private void requestIsSuccess(String url) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        executeNetWork(request, "请稍候");
        setCallback(this);
    }


    //进入  我的二维码  界面
    public void settingMyQRCode(View view) {
        Intent intent = new Intent(this, QRCodeActivity.class);
        startActivity(intent);
    }

    //进入  安全中心  界面
    public void settingSafeSetting(View view) {
        Intent intent = new Intent(this, SafeSettingActivity.class);
        startActivity(intent);
    }

    //进入  支付设置  界面
    public void settingPay(View view) {
        Intent intent = new Intent(this, PayPasswordChangeActivity.class);
        startActivity(intent);
    }

    //进入  密保设置  界面
    public void goPasswordProtect(View view) {
        Intent intent = new Intent(this, PasswordProtect2Activity.class);
        startActivity(intent);
    }

    //点击退出，清除缓存，进入登录界面
    public void exitUser(View view) {
        SharedPreferences sf = getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
        sf.edit()
                .putInt("uuid", -1000)
                .putString("token", "")
                .putLong("expiring_time", -2000)
                .putString("login_phone", "")
                .putString("login_password", "").apply();
        Intent intent = new Intent(this, FourthFragment.class);
        setResult(2, intent);
        finish();
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if(finalScene==1){
            int isCardId = dataObj.optInt("isCardId");
            if (isCardId == 0) {
                Intent intent = new Intent(SettingActivity.this, RealNameCofirmActivity.class);
                startActivity(intent);
            } else {
                JSONObject cardData = dataObj.optJSONObject("cardInfo");
                String realname = cardData.optString("realname");
                String cardid = cardData.optString("cardid");
                int sex = cardData.optInt("sex");
                int age = cardData.optInt("age");
                String location = cardData.optString("location");
                Intent intent1 = new Intent(SettingActivity.this, RealNameSuccessActivity.class);
                intent1.putExtra("realname", realname);
                intent1.putExtra("cardid", cardid);
                intent1.putExtra("sex", sex);
                intent1.putExtra("age", age);
                intent1.putExtra("location", location);
                startActivity(intent1);
            }
        }else if(finalScene==2){
            int is_sell = dataObj.optInt("is_sell");
            if(is_sell==0){
                qrView.setVisibility(View.GONE);
                qrLineView.setVisibility(View.GONE);
            }else {
                qrView.setVisibility(View.VISIBLE);
                qrLineView.setVisibility(View.VISIBLE);
            }
        }


    }
}
