package com.ascba.rebate.activities;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.NetworkBaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MoneyBar;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import org.json.JSONObject;

public class RecQRActivity extends NetworkBaseActivity {
    private MoneyBar shareBar;
    private TextView tvRecId;
    private TextView tvRecNet;
    private ImageView imQR;
    private SharedPreferences sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_qr);
        initViews();
        createQR();
        requestFromServer();
    }

    private void initViews() {
        //initMoneyBar();
        tvRecId = ((TextView) findViewById(R.id.tv_rec_code));
        tvRecNet = ((TextView) findViewById(R.id.tv_rec_net));
        imQR = ((ImageView) findViewById(R.id.im_rec_qr));

    }
    private void createQR() {
        sf = getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
        String p_id = sf.getString("p_id", "");
        String p_url = sf.getString("p_url", "");
        if(!p_id.equals("")||!p_url.equals("")){
            tvRecId.setText("推荐ID:"+p_id);
            tvRecNet.setText(p_url);
            Bitmap qrCode = createQRCode(p_url, 200, 200);
            imQR.setImageBitmap(qrCode);
        }
    }

    private void requestFromServer() {
        sendMsgToSevr(UrlUtils.partook,0);
        CheckThread checkThread = getCheckThread();
        if(checkThread!=null){
            PhoneHandler phoneHandler = checkThread.getPhoneHandler();
            final ProgressDialog p=new ProgressDialog(this,R.style.dialog);
            p.setMessage("请稍后");
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
                        JSONObject paObj = dataObj.optJSONObject("partook");
                        String p_id = paObj.optString("p_id");
                        String p_url = paObj.optString("p_url");
                        String sf_url = sf.getString("p_url", "");
                        if(sf_url.equals("")){
                            sf.edit().putString("p_url",p_url).putString("p_id",p_id).apply();
                            tvRecId.setText("推广ID:"+ p_id);
                            tvRecNet.setText(p_url);
                            Bitmap qrCode = createQRCode(p_url, 200, 200);
                            imQR.setImageBitmap(qrCode);
                        }
                    } else if(status==1||status==2||status==3||status == 4||status==5){//缺少sign参数
                        Intent intent = new Intent(RecQRActivity.this, LoginActivity.class);
                        sf.edit().putInt("uuid", -1000).apply();
                        startActivity(intent);
                        finish();
                    } else if(status==404){
                        Toast.makeText(RecQRActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if(status==500){
                        Toast.makeText(RecQRActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            checkThread.start();
            p.show();
        }


    }



    private void initMoneyBar() {
        shareBar = ((MoneyBar) findViewById(R.id.mb_rec_qr));
        shareBar.setCallBack(new MoneyBar.CallBack() {
            @Override
            public void clickImage(View im) {
                //跳转第三方分享
            }

            @Override
            public void clickComplete(View tv) {

            }
        });
    }

    public Bitmap createQRCode(String content,int width,int height){
        int dpWidth = ScreenDpiUtils.dip2px(this, width);
        int dpHeight = ScreenDpiUtils.dip2px(this, height);
        return CodeUtils.createImage(content, dpWidth, dpHeight, BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
    }

    public void copyText(View view) {
        if(getSDKVersionNumber() >= 11){
            android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(tvRecNet.getText());
        }else{
            // 得到剪贴板管理器
            android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(ClipData.newPlainText(null, tvRecNet.getText()));
        }
        Toast.makeText(this, "复制到剪贴板", Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取手机操作系统版本
     * @return
     * @author SHANHY
     * @date   2015年12月4日
     */
    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }
}
