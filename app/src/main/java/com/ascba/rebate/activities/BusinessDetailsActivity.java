package com.ascba.rebate.activities;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;

import com.ascba.rebate.activities.base.NetworkBaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;

import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.baidu.mapapi.model.LatLng;

import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.squareup.picasso.Picasso;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

public class BusinessDetailsActivity extends NetworkBaseActivity {
    // 天安门坐标
    double mLat1 = 39.915291;
    double mLon1 = 116.403857;
    // 百度大厦坐标
    double mLat2 = 40.056858;
    double mLon2 = 116.308194;
    private int business_id;
    private ImageView imBusiPic;
    private TextView tvName;
    private TextView tvType;
    private TextView tvAddress;
    private TextView tvPhone;
    private TextView tvTime;
    private TextView tvRate;
    private String seller_description;
    private SharedPreferences sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);
        initViews();
        getDataFromMain();
    }

    private void initViews() {
        sf=getSharedPreferences("first_login_success_name_password",MODE_PRIVATE);
        imBusiPic = ((ImageView) findViewById(R.id.im_busi_pic));
        tvName = ((TextView) findViewById(R.id.tv_busi_name));
        tvType = ((TextView) findViewById(R.id.tv_busi_type));
        tvAddress = ((TextView) findViewById(R.id.tv_busi_address));
        tvPhone = ((TextView) findViewById(R.id.tv_busi_phone));
        tvTime = ((TextView) findViewById(R.id.tv_busi_time));
        //商家简介特殊处理


        tvRate = ((TextView) findViewById(R.id.tv_busi_rate));
    }

    private void getDataFromMain() {
        Intent intent = getIntent();
        if(intent!=null){
            business_id = intent.getIntExtra("business_id",-1);
            if(business_id!=-1){
                sendMsgToSevr(UrlUtils.getBusinesses,0);
                CheckThread checkThread = getCheckThread();
                if(checkThread!=null){
                    Request<JSONObject> objRequest = checkThread.getObjRequest();
                    final ProgressDialog p=new ProgressDialog(this,R.style.dialog);
                    p.setMessage("请稍后");
                    objRequest.add("businesses_id",business_id);
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
                                JSONObject seObj = dataObj.optJSONObject("sellerInfo");
                                String seller_name = seObj.optString("seller_name");
                                String seller_taglib = seObj.optString("seller_taglib");
                                seller_description = seObj.optString("seller_description");
                                String seller_address = seObj.optString("seller_address");
                                String seller_tel = seObj.optString("seller_tel");
                                String seller_business_hours = seObj.optString("seller_business_hours");
                                String base_url="http://api.qlqwgw.com";
                                String seller_cover = seObj.optString("seller_cover");
                                String seller_return_ratio = seObj.optString("seller_return_ratio");
                                Picasso.with(BusinessDetailsActivity.this).load(base_url+seller_cover).into(imBusiPic);
                                tvName.setText(seller_name);
                                tvType.setText(seller_taglib);
                                tvAddress.setText(seller_address);
                                tvPhone.setText(seller_tel);
                                tvTime.setText(seller_business_hours);
                                tvRate.setText("返佣比例 "+seller_return_ratio+"%" );
                            } else if(status==1||status==2||status==3||status == 4||status==5){//缺少sign参数
                                Intent intent = new Intent(BusinessDetailsActivity.this, LoginActivity.class);
                                sf.edit().putInt("uuid", -1000).apply();
                                startActivity(intent);
                                finish();
                            } else if(status==404){
                                Toast.makeText(BusinessDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else if(status==500){
                                Toast.makeText(BusinessDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    checkThread.start();
                    p.show();
                }

            }
        }
    }

    public void goBaiduNavi(View view) {
        startRoutePlanTransit();
    }

    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(BusinessDetailsActivity.this);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 启动百度地图公交路线规划
     */
    public void startRoutePlanTransit() {
        LatLng ptStart = new LatLng(mLat1, mLon1);
        LatLng ptEnd = new LatLng(mLat2, mLon2);

        RouteParaOption para = new RouteParaOption()
        .startPoint(ptStart).endPoint(ptEnd).busStrategyType(RouteParaOption.EBusStrategyType.bus_recommend_way);

        try {
            BaiduMapRoutePlan.openBaiduMapTransitRoute(para, this);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }

    }
    //用户点击了 联系他
    public void goPhone(View view) {
        final PopupWindow p=new PopupWindow(this);
        View popView = getLayoutInflater().inflate(R.layout.pop_click_phone, null);
        p.setContentView(popView);
        p.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });
        View phoneView = popView.findViewById(R.id.pop_phone);
        View cancelView = popView.findViewById(R.id.pop_cancel);
        phoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvPhone = (TextView) v.findViewById(R.id.tv_phone);
                String phone = tvPhone.getText().toString();
                if(!phone.equals("")){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    startActivity(intent);
                }

            }
        });
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.dismiss();
            }
        });
        p.setOutsideTouchable(true);
        p.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        p.setHeight(ScreenDpiUtils.dip2px(this,135));
        p.setBackgroundDrawable(new ColorDrawable(0xE8E8E7));
        p.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,0,0);
        //设置背景变暗
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
    }

    public void goDescription(View view) {
        if(!"".equals(seller_description)){
            Intent intent=new Intent(this,ShowDescriptionActivity.class);
            intent.putExtra("seller_description",seller_description);
            startActivity(intent);
        }
    }
}
