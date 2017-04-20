package com.ascba.rebate.activities.main_page;

import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.utils.QrUtils;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MoneyBar;
import com.yanzhenjie.nohttp.rest.Request;
import org.json.JSONObject;

public class RecQRActivity extends BaseNetActivity implements BaseNetActivity.Callback {
    private MoneyBar shareBar;
    private TextView tvRecId;
    private TextView tvRecNet;
    private ImageView imQR;
    private SharedPreferences sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_qr);
        //StatusBarUtil.setColor(this, 0xffe52020);
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
        Request<JSONObject> request = buildNetRequest(UrlUtils.partook, 0, true);
        executeNetWork(request,"请稍后");
        setCallback(this);
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

        return QrUtils.createImage(content, dpWidth, dpHeight, BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
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

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        JSONObject paObj = dataObj.optJSONObject("partook");
        String p_id = paObj.optString("p_id");
        String p_url = paObj.optString("p_url");
        tvRecId.setText("推广ID:"+ p_id);
        tvRecNet.setText(p_url);
        Bitmap qrCode = createQRCode(p_url, 200, 200);
        imQR.setImageBitmap(qrCode);
    }

    @Override
    public void handle404(String message) {

    }

    @Override
    public void handleNoNetWork() {

    }
}
