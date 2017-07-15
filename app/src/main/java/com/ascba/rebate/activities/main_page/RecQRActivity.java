package com.ascba.rebate.activities.main_page;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.utils.QrUtils;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class RecQRActivity extends BaseNetActivity implements BaseNetActivity.Callback {
    private TextView tvRecId;
    private TextView tvRecNet;
    private ImageView imQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_qr);
        initViews();
        createQR();
        requestFromServer();
    }

    private void initViews() {
        tvRecId = ((TextView) findViewById(R.id.tv_rec_code));
        tvRecNet = ((TextView) findViewById(R.id.tv_rec_net));//推广链接
        imQR = ((ImageView) findViewById(R.id.im_rec_qr));

    }
    private void createQR() {
        SharedPreferences sf = getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
        String p_id = sf.getString("p_id", "");
        String p_url = sf.getString("p_url", "");
        if (!p_id.equals("") || !p_url.equals("")) {
            tvRecId.setText("推荐ID:" + p_id);
            tvRecNet.setText(p_url);
            Bitmap qrCode = createQRCode(p_url, 250, 250);
            imQR.setImageBitmap(qrCode);
        }
    }


    private void requestFromServer() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.partook, 0, true);
        executeNetWork(request,"请稍后");
        setCallback(this);
    }

    public Bitmap createQRCode(String content,int width,int height){
        int dpWidth = ScreenDpiUtils.dip2px(this, width);
        int dpHeight = ScreenDpiUtils.dip2px(this, height);
        return QrUtils.createQRImage(content,dpWidth,dpHeight,getApplicationContext());
        //return QrUtils.createImage(content, dpWidth, dpHeight, BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
    }

    public void copyText(View view) {
        WXWebpageObject webpage=new WXWebpageObject();
        webpage.webpageUrl = tvRecNet.getText().toString();
        WXMediaMessage msg=new WXMediaMessage(webpage);
        msg.title="哇！花多少赚多少，京东都怕了";
        msg.description="商品多，配送快，品质好，天天省钱，消费能赚钱，快快体验吧！";
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.share_icon);
        msg.thumbData= bmpToByteArray(bitmap,true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction= buildTransaction("webpage");
        req.message= msg;
        shareDialog(req);
    }

    private void shareDialog(final SendMessageToWX.Req req){

        //发送到聊天界面——WXSceneSession
        //发送到朋友圈——WXSceneTimeline
        //添加到微信收藏——WXSceneFavorite

        final BottomSheetDialog dialog =new BottomSheetDialog(this);
        dialog.setContentView(LayoutInflater.from(this).inflate(R.layout.register_share_dialog,null));
        dialog.findViewById(R.id.wx_circle_friends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req.scene= SendMessageToWX.Req.WXSceneTimeline;
                ((MyApplication) getApplication()).msgApi.sendReq(req);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.wx_friends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req.scene= SendMessageToWX.Req.WXSceneSession;
                ((MyApplication) getApplication()).msgApi.sendReq(req);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private   byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
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
