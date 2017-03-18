package com.ascba.rebate.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.me_page.CardActivity;
import com.ascba.rebate.activities.me_page.UserUpdateActivity;
import com.ascba.rebate.activities.me_page.bank_card_child.AddCardActivity;
import com.ascba.rebate.activities.me_page.business_center_child.BCProcessActivity;
import com.ascba.rebate.activities.me_page.business_center_child.BusinessCenterActivity;
import com.ascba.rebate.activities.me_page.business_center_child.child.BusinessDataActivity;
import com.ascba.rebate.activities.me_page.settings.SettingActivity;
import com.ascba.rebate.activities.me_page.settings.child.PersonalDataActivity;
import com.ascba.rebate.activities.me_page.settings.child.QRCodeActivity;
import com.ascba.rebate.activities.me_page.settings.child.RealNameCofirmActivity;
import com.ascba.rebate.activities.me_page.settings.child.real_name_confirm.RealNameSuccessActivity;
import com.ascba.rebate.fragments.base.Base2Fragment;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人中心
 */
public class MeFragment extends Base2Fragment implements SuperSwipeRefreshLayout.OnPullRefreshListener, View.OnClickListener, Base2Fragment.Callback {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private SuperSwipeRefreshLayout refreshLayout;
    private CircleImageView userIcon;
    private LinearLayout imgsLat;
    private View viewTuiGuang;
    private View viewJiangLi;
    private View viewPower;
    private TextView tvSmrz;
    private View viewSmrz;
    private TextView tvSjlm;
    private View viewSjlm;
    private View viewMsg;
    private View viewSetting;
    private TextView tvPhone;
    private int finalScene;

    private static final int REQUEST_APPLY = 0;
    private static final int REQUEST_CLOSE=1;
    private TextView tvUserName;
    private View qrView;

    public MeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        refreshLayout = ((SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_layout));
        refreshLayout.setOnPullRefreshListener(this);
        //用户头像
        userIcon = ((CircleImageView) view.findViewById(R.id.me_user_img));
        userIcon.setOnClickListener(this);

        tvUserName = ((TextView) view.findViewById(R.id.me_tv_nick_name));
        //级别图片组
        imgsLat = ((LinearLayout) view.findViewById(R.id.container_imgs));
        //我的推广
        viewTuiGuang = view.findViewById(R.id.me_lat_tuiguang);
        viewJiangLi = view.findViewById(R.id.me_lat_jiangli);
        viewPower = view.findViewById(R.id.me_lat_power);
        viewPower.setOnClickListener(this);
        viewJiangLi.setOnClickListener(this);
        viewTuiGuang.setOnClickListener(this);
        //实名认证
        tvSmrz = ((TextView) view.findViewById(R.id.me_tv_smrz_sta));
        viewSmrz = view.findViewById(R.id.me_lat_smrz);
        viewSmrz.setOnClickListener(this);
        //商户中心
        tvSjlm = ((TextView) view.findViewById(R.id.me_tv_sjlm_sta));
        viewSjlm = view.findViewById(R.id.me_lat_sjlm);
        viewSjlm.setOnClickListener(this);
        //消息
        viewMsg = view.findViewById(R.id.me_lat_msg);
        viewMsg.setOnClickListener(this);
        //商家二维码
        qrView = view.findViewById(R.id.setting_my_qr);
        qrView.setOnClickListener(this);
        //设置
        viewSetting = view.findViewById(R.id.me_lat_setting);
        viewSetting.setOnClickListener(this);
        //电话
        tvPhone = ((TextView) view.findViewById(R.id.me_tv_phone));

        requestData(UrlUtils.user,3);
    }

    @Override
    public void onRefresh() {
        requestData(UrlUtils.user,3);
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_user_img://用户头像
                Intent intent = new Intent(getActivity(), PersonalDataActivity.class);
                startActivity(intent);
                break;
            case R.id.me_lat_tuiguang://推广

                break;
            case R.id.me_lat_jiangli://奖励
                break;
            case R.id.me_lat_power://会员特权
                Intent intent1 = new Intent(getActivity(), UserUpdateActivity.class);
                startActivity(intent1);
                break;
            case R.id.me_lat_smrz://实名认证
                requestData(UrlUtils.checkCardId, 0);
                break;
            case R.id.me_lat_sjlm://商户中心
                requestData(UrlUtils.getCompany, 1);
                break;
            case R.id.me_lat_msg://消息
                break;
            case R.id.setting_my_qr:
                Intent intent3 = new Intent(getActivity(), QRCodeActivity.class);
                startActivity(intent3);
                break;
            case R.id.me_lat_setting://设置
                Intent intent2 = new Intent(getActivity(), SettingActivity.class);
                startActivityForResult(intent2, REQUEST_CLOSE);
                break;

        }
    }

    private void requestData(String url, int scene) {
        finalScene = scene;
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        executeNetWork(request, "请稍候");
        setCallback(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_APPLY:
                requestData(UrlUtils.user,3);
                break;
            case REQUEST_CLOSE:
                if(resultCode== Activity.RESULT_OK){
                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;

        }
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if (finalScene == 0) {
            int isCardId = dataObj.optInt("isCardId");
            if (isCardId == 0) {
                Intent intent = new Intent(getActivity(), RealNameCofirmActivity.class);
                startActivity(intent);
            } else {
                JSONObject cardData = dataObj.optJSONObject("cardInfo");
                String realname = cardData.optString("realname");
                String cardid = cardData.optString("cardid");
                int sex = cardData.optInt("sex");
                int age = cardData.optInt("age");
                String location = cardData.optString("location");
                Intent intent1 = new Intent(getActivity(), RealNameSuccessActivity.class);
                intent1.putExtra("realname", realname);
                intent1.putExtra("cardid", cardid);
                intent1.putExtra("sex", sex);
                intent1.putExtra("age", age);
                intent1.putExtra("location", location);
                startActivity(intent1);
            }
        } else if (finalScene == 1) {
            //点击商户中心
            JSONObject company = dataObj.optJSONObject("company");
            int seller_enable_time = dataObj.optInt("seller_enable_time");
            String seller_enable_tip = dataObj.optString("seller_enable_tip");
            int merchant = company.optInt("status");
            if (merchant == 3) {//申请成功
                String seller_name = company.optString("seller_name");
                String seller_cover_logo = company.optString("seller_cover_logo");
                String seller_image = company.optString("seller_image");
                String seller_taglib = company.optString("seller_taglib");
                String seller_address = company.optString("seller_address");
                String seller_localhost = company.optString("seller_localhost");
                String seller_lon = company.optString("seller_lon");
                String seller_lat = company.optString("seller_lat");
                String seller_tel = company.optString("seller_tel");
                String seller_business_hours = company.optString("seller_business_hours");
                String seller_return_ratio = company.optString("seller_return_ratio");
                String seller_return_ratio_tip = company.optString("seller_return_ratio_tip");
                String seller_description = company.optString("seller_description");
                Intent intent = new Intent(getActivity(), BusinessDataActivity.class);
                intent.putExtra("seller_name", seller_name);
                intent.putExtra("seller_cover_logo", seller_cover_logo);
                intent.putExtra("seller_image", seller_image);
                intent.putExtra("seller_taglib", seller_taglib);
                intent.putExtra("seller_address", seller_address);
                intent.putExtra("seller_localhost", seller_localhost);
                intent.putExtra("seller_lon", seller_lon);
                intent.putExtra("seller_lat", seller_lat);
                intent.putExtra("seller_tel", seller_tel);
                intent.putExtra("seller_business_hours", seller_business_hours);
                intent.putExtra("seller_return_ratio", seller_return_ratio);
                intent.putExtra("seller_return_ratio_tip", seller_return_ratio_tip);
                intent.putExtra("seller_description", seller_description);
                intent.putExtra("seller_enable_time", seller_enable_time);
                intent.putExtra("seller_enable_tip", seller_enable_tip);
                startActivity(intent);
            } else if (merchant == 0) {//填写申请资料
                Intent intent = new Intent(getActivity(), BCProcessActivity.class);
                startActivityForResult(intent, REQUEST_APPLY);
            } else if (merchant == 1) {//资料审核中
                Intent intent = new Intent(getActivity(), BusinessCenterActivity.class);
                String name = company.optString("name");
                String oper_name = company.optString("oper_name");
                String regist_capi = company.optString("regist_capi");
                String company_status = company.optString("company_status");
                String chartered = company.optString("chartered");//营业执照图片地址
                String scope = company.optString("scope");
                int is_oper_name = company.optInt("is_oper_name");
                if (is_oper_name == 1) {//与法人姓名不一致
                    String clientele_name = company.optString("clientele_name");//授权人姓名
                    String warrant = company.optString("warrant");//授权书图片地址
                    intent.putExtra("clientele_name", clientele_name);
                    intent.putExtra("warrant", warrant);
                }
                intent.putExtra("type", 0);
                intent.putExtra("name", name);
                intent.putExtra("oper_name", oper_name);
                intent.putExtra("regist_capi", regist_capi);
                intent.putExtra("company_status", company_status);
                intent.putExtra("scope", scope);
                intent.putExtra("is_oper_name", is_oper_name);
                intent.putExtra("chartered", chartered);
                startActivity(intent);
            } else if (merchant == 2) {//资料有误
                Intent intent = new Intent(getActivity(), BusinessCenterActivity.class);
                String name = company.optString("name");
                String oper_name = company.optString("oper_name");
                String regist_capi = company.optString("regist_capi");
                String company_status = company.optString("company_status");
                String chartered = company.optString("chartered");//营业执照图片地址
                String scope = company.optString("scope");
                int is_oper_name = company.optInt("is_oper_name");
                if (is_oper_name == 1) {//与法人姓名不一致
                    String clientele_name = company.optString("clientele_name");//授权人姓名
                    String warrant = company.optString("warrant");//授权书图片地址
                    intent.putExtra("clientele_name", clientele_name);
                    intent.putExtra("warrant", warrant);
                }
                intent.putExtra("type", 1);
                intent.putExtra("name", name);
                intent.putExtra("oper_name", oper_name);
                intent.putExtra("regist_capi", regist_capi);
                intent.putExtra("company_status", company_status);
                intent.putExtra("scope", scope);
                intent.putExtra("is_oper_name", is_oper_name);
                intent.putExtra("chartered", chartered);
                startActivity(intent);
            }
        } else if (finalScene == 2) {//检查是否实名，点击银行卡前
            int isCardId = dataObj.optInt("isCardId");
            int isBankCard = dataObj.optInt("isBankCard");
            if (isCardId == 0) {
                final DialogManager dm = new DialogManager(getActivity());
                dm.buildAlertDialog1("暂未实名认证，是否立即实名认证？");
                dm.setCallback(new DialogManager.Callback() {
                    @Override
                    public void handleSure() {
                        dm.dismissDialog();
                        Intent intent = new Intent(getActivity(), RealNameCofirmActivity.class);
                        startActivity(intent);
                    }
                });
            } else {
                JSONObject cardObj = dataObj.optJSONObject("cardInfo");
                if (isBankCard == 0) {
                    Intent intent = new Intent(getActivity(), AddCardActivity.class);
                    intent.putExtra("realname", cardObj.optString("realname"));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), CardActivity.class);
                    startActivity(intent);
                }
            }
        } else if(finalScene==3){
            if(refreshLayout!=null && refreshLayout.isRefreshing()){
                refreshLayout.setRefreshing(false);
            }
            JSONObject infoObj = dataObj.optJSONObject("myInfo");
            Picasso.with(getActivity()).load(UrlUtils.baseWebsite + infoObj.optString("avatar")).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE).error(R.mipmap.logo).noPlaceholder().into(userIcon);
            JSONArray group_type = infoObj.optJSONArray("group_type");
            if(group_type==null || group_type.length()==0){
                return;
            }
            imgsLat.removeAllViews();
            for (int i = 0; i < group_type.length(); i++) {
                JSONObject typeObj = group_type.optJSONObject(i);
                int isUpgraded = typeObj.optInt("isUpgraded");
                int id = typeObj.optInt("id");
                ImageView imageView = new ImageView(getActivity());
                int i1 = ScreenDpiUtils.dip2px(getActivity(), 14);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(i1, i1);
                lp.leftMargin = ScreenDpiUtils.dip2px(getActivity(), 7);
                imageView.setLayoutParams(lp);
                if (isUpgraded == 1 || id == 1) {
                    String upgraded_icon = typeObj.optString("upgraded_icon");
                    Picasso.with(getActivity()).load(UrlUtils.baseWebsite + upgraded_icon).memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE).error(R.mipmap.logo).noPlaceholder().into(imageView);
                } else {
                    String default_icon = typeObj.optString("default_icon");
                    Picasso.with(getActivity()).load(UrlUtils.baseWebsite + default_icon).memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE).error(R.mipmap.logo).noPlaceholder().into(imageView);
                }
                imgsLat.addView(imageView);
            }
            tvSmrz.setText(infoObj.optInt("card_status")==0 ? "未实名" : "已实名");
            tvUserName.setText(infoObj.optString("nickname"));
            tvSjlm.setText(infoObj.optInt("merchant")<3 ?infoObj.optString("merchant_tip") : infoObj.optString("seller_status_tip"));
            tvPhone.setText(infoObj.optString("telephone"));
            if (infoObj.optInt("seller_status")==2) {
                qrView.setVisibility(View.VISIBLE);
            } else {
                qrView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void handleReqFailed() {
        if(refreshLayout!=null && refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void handle404(String message) {

    }

    @Override
    public void handleReLogin() {
        if(refreshLayout!=null && refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void handleNoNetWork() {

    }
}
