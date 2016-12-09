package com.ascba.rebate.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.activities.base.NetworkBaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.UrlUtils;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

public class SweepActivity extends NetworkBaseActivity {
    private SharedPreferences sf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sweep);
        sf=getSharedPreferences("first_login_success_name_password",MODE_PRIVATE);
        /**
         * 执行扫面Fragment的初始化操作
         */
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);

        captureFragment.setAnalyzeCallback(analyzeCallback);
        /**
         * 替换我们的扫描控件
         */
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }
    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, final String result) {
            Intent intent=new Intent(SweepActivity.this,PayActivity.class);
            intent.putExtra("bus_uuid",result);
            startActivity(intent);
            finish();
            sendMsgToSevr(UrlUtils.checkMember,0);
            CheckThread checkThread = getCheckThread();
            if(checkThread!=null){
                Request<JSONObject> objRequest = checkThread.getObjRequest();
                objRequest.add("seller",result);
                objRequest.add("scenetype",2);
                final ProgressDialog p=new ProgressDialog(SweepActivity.this,R.style.dialog);
                p.setMessage("请稍后");
                p.setCanceledOnTouchOutside(false);
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
                            JSONObject dataObj = jObj.optJSONObject("data");
                            int uuid = dataObj.optInt("uuid");
                            Intent intent1=new Intent(SweepActivity.this,PayActivity.class);
                            intent1.putExtra("bus_uuid",uuid);
                            startActivity(intent1);
                            finish();
//                        Intent resultIntent = new Intent();
//                        Bundle bundle = new Bundle();
//                        bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
//                        bundle.putString(CodeUtils.RESULT_STRING, result);
//                        resultIntent.putExtras(bundle);
//                        setResult(RESULT_OK, resultIntent);
//                        finish();
                        }
                    }
                });
                checkThread.start();
                p.show();
            }

        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    };
}
