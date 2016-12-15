package com.ascba.rebate.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.utils.UrlUtils;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.yolanda.nohttp.rest.Request;
import org.json.JSONObject;

public class SweepActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sweep);
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
            Request<JSONObject> objRequest = buildNetRequest(UrlUtils.checkMember, 0, true);
            objRequest.add("seller",result);
            objRequest.add("scenetype",2);
            executeNetWork(objRequest,"请稍后");
            setCallback(SweepActivity.this);
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

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        int uuid = dataObj.optInt("uuid");
        Intent intent1=new Intent(SweepActivity.this,PayActivity.class);
        intent1.putExtra("bus_uuid",uuid);
        startActivity(intent1);
        finish();
    }
}
