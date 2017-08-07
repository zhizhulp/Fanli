package com.ascba.rebate.activities.base;

import android.content.Intent;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.utils.JpushSetManager;

import org.json.JSONObject;

import java.util.LinkedHashSet;

import static com.ascba.rebate.activities.base.BaseNetActivity.REQUEST_LOGIN;

public class BaseNetActivity2 extends BaseActivityNet1 {

    @Override
    protected void requstSuccess(int what, JSONObject jObj) {

        int status = jObj.optInt("status");
        String message = jObj.optString("msg");
        JSONObject dataObj = jObj.optJSONObject("data");
        if (status == 200) {
            getPageCount(dataObj);
            int update_status = dataObj.optInt("update_status");
            if (update_status == 1) {
                AppConfig.getInstance().putString("token", dataObj.optString("token"));
                AppConfig.getInstance().putLong("expiring_time", dataObj.optLong("expiring_time"));
            }
            mhandle200Data(what, jObj, dataObj, message);

        } else if (status == 1 || status == 2 || status == 3 || status == 4 || status == 5) {//缺少sign参数
            //jpush
            AppConfig.getInstance().putBoolean("jpush_set_tag_success",false);
            AppConfig.getInstance().putBoolean("jpush_set_alias_success",false);
            JpushSetManager js=new JpushSetManager(this,1);
            js.setTag(new LinkedHashSet<String>());
            js.setAlias("");

            mhandleReLogin(what);
            Intent intent = new Intent(this, LoginActivity.class);
            MyApplication.isSignOut=true;
            MyApplication.isLoadCartData=true;
            AppConfig.getInstance().putString("token", null);
            AppConfig.getInstance().putLong("expiring_time", -2000);
            AppConfig.getInstance().putInt("uuid", -1000);
            startActivityForResult(intent, REQUEST_LOGIN);
        } else if (status == 404) {
            mhandle404(what, dataObj, message);
        } else if (status == 500) {
            showToast(message);
        } else if (status == 6) {
            showToast(message);
        }

    }

    private void getPageCount(JSONObject dataObj) {
        total_page = dataObj.optInt("total_page");
        now_page++;
    }

    @Override
    protected void requstFailed(int what, Exception e) {
        mhandleFailed(what, e);
    }

    @Override
    protected void requstFinish(int what) {
        mhandleFinish(what);
    }

    @Override
    protected void isNetWork(boolean isNetWork) {
        if (!isNetWork) {
            //没网
            mhandleNoNetWord();
            showToast(getString(R.string.no_network));
            //getDm().buildAlertDialog(getString(R.string.no_network));
        }
    }


    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
    }

    protected void mhandle404(int what, JSONObject dataObj, String message) {
        showToast(message);
    }


    protected void mhandleFailed(int what, Exception e) {
    }

    protected void mhandleReLogin(int what) {
    }

    protected void mhandleFinish(int what) {
    }

    protected void mhandleNoNetWord() {
    }
}
