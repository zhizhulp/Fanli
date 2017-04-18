package com.ascba.rebate.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.BusinessUnionActivity;
import com.ascba.rebate.activities.ProxyDetActivity;
import com.ascba.rebate.activities.ShopMessageActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.main.MainActivity;
import com.ascba.rebate.activities.main_page.RecQRActivity;
import com.ascba.rebate.activities.me_page.CardActivity;
import com.ascba.rebate.activities.me_page.MyAwardActivity;
import com.ascba.rebate.activities.me_page.MyRecActivity;
import com.ascba.rebate.activities.me_page.UserUpdateActivity;
import com.ascba.rebate.activities.me_page.bank_card_child.AddCardActivity;
import com.ascba.rebate.activities.me_page.business_center_child.child.BusinessDataActivity;
import com.ascba.rebate.activities.me_page.settings.SettingActivity;
import com.ascba.rebate.activities.me_page.settings.child.PersonalDataActivity;
import com.ascba.rebate.activities.me_page.settings.child.RealNameCofirmActivity;
import com.ascba.rebate.activities.me_page.settings.child.real_name_confirm.RealNameSuccessActivity;
import com.ascba.rebate.fragments.base.Base2Fragment;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.NetUtils;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.AppTabs;
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
    private static final int REQUEST_CLOSE = 1;
    private static final int REQUEST_LOGIN = 2;
    private TextView tvUserName;
    private View qrView;
    private View viewDlZq;

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
        //商家联盟
        tvSjlm = ((TextView) view.findViewById(R.id.me_tv_sjlm_sta));
        viewSjlm = view.findViewById(R.id.me_lat_sjlm);
        viewSjlm.setOnClickListener(this);
        //代理专区
        viewDlZq = view.findViewById(R.id.me_lat_dlzq);
        viewDlZq.setOnClickListener(this);
        //消息
        viewMsg = view.findViewById(R.id.me_lat_msg);
        viewMsg.setOnClickListener(this);
        //我的推广码
        qrView = view.findViewById(R.id.setting_my_qr);
        qrView.setOnClickListener(this);
        //设置
        viewSetting = view.findViewById(R.id.me_lat_setting);
        viewSetting.setOnClickListener(this);
        //电话
        tvPhone = ((TextView) view.findViewById(R.id.me_tv_phone));

        requestData(UrlUtils.user, 3);
    }

    @Override
    public void onRefresh() {
        if (NetUtils.isNetworkAvailable(getActivity())) {
            requestData(UrlUtils.user, 3);
        } else {
            refreshLayout.setRefreshing(false);
            getDm().buildAlertDialog(getActivity().getResources().getString(R.string.no_network));
        }
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
                Intent intent4 = new Intent(getActivity(), MyRecActivity.class);
                startActivity(intent4);
                break;
            case R.id.me_lat_jiangli://奖励
                Intent intent5 = new Intent(getActivity(), MyAwardActivity.class);
                startActivity(intent5);
                break;
            case R.id.me_lat_power://会员特权
                Intent intent1 = new Intent(getActivity(), UserUpdateActivity.class);
                startActivity(intent1);
                break;
            case R.id.me_lat_smrz://实名认证
                requestData(UrlUtils.checkCardId, 0);
                break;
            case R.id.me_lat_sjlm://商户中心
                //requestData(UrlUtils.getCompany, 1);
                requestData(UrlUtils.user, 1);
                break;
            case R.id.me_lat_dlzq://代理专区
                Intent intent6=new Intent(getActivity(), ProxyDetActivity.class);
                startActivity(intent6);
                break;
            case R.id.me_lat_msg://消息
                ShopMessageActivity.startIntent(getActivity());
                break;
            case R.id.setting_my_qr://我的推广码
                startActivity(new Intent(getActivity(), RecQRActivity.class));
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
                requestData(UrlUtils.user, 3);
                break;
            case REQUEST_CLOSE://设置传来的
                if (resultCode == Activity.RESULT_OK) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    //MyApplication.isPersonalData = true;
                    startActivityForResult(intent, REQUEST_LOGIN);
                }
                break;
            case REQUEST_LOGIN://登录传来的
                if (resultCode != Activity.RESULT_OK) {//登录不成功
                    FragmentActivity activity = getActivity();
                    if (activity instanceof MainActivity) {
                        ((MainActivity) activity).selFrgByPos(0);
                        AppTabs appTabs = ((MainActivity) activity).getAppTabs();
                        appTabs.setFilPos(0);
                        appTabs.statusChaByPosition(0, 4);
                    }
                } else {//登录成功，刷新界面
                    requestData(UrlUtils.user, 3);
                }
                break;
//            case BaseNetWork4Activity.REQUEST_LOGIN://被挤掉或登录超时
//                if (resultCode == Activity.RESULT_OK) {
//                    requestData(UrlUtils.user, 3);
//                }
//                break;


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

            JSONObject infoObj = dataObj.optJSONObject("myInfo");
            LogUtils.PrintLog("MeFragment","data-->"+infoObj);
            int seller_status = infoObj.optInt("seller_status");
            int merchant = infoObj.optInt("merchant");
            if(merchant==3){
                if(seller_status==3){
                    Intent intent=new Intent(getActivity(),BusinessUnionActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(getActivity(),BusinessDataActivity.class);
                    startActivity(intent);
                }
            }else {
                Intent intent=new Intent(getActivity(),BusinessDataActivity.class);
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
        } else if (finalScene == 3) {
            if (refreshLayout != null && refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
            }
            JSONObject infoObj = dataObj.optJSONObject("myInfo");
            Picasso.with(getActivity()).load(UrlUtils.baseWebsite + infoObj.optString("avatar")).error(R.mipmap.logo).noPlaceholder().into(userIcon);
            JSONArray group_type = infoObj.optJSONArray("group_type");
            if (group_type == null || group_type.length() == 0) {
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
            tvSmrz.setText(infoObj.optInt("card_status") == 0 ? "未实名" : "已实名");
            tvUserName.setText(infoObj.optString("nickname"));
            tvSjlm.setText(infoObj.optInt("merchant") < 3 ? infoObj.optString("merchant_tip") : infoObj.optString("seller_status_tip"));
            tvPhone.setText(infoObj.optString("telephone"));
        }
    }

    @Override
    public void handleReqFailed() {
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void handle404(String message) {
        getDm().buildAlertDialog(message);
    }

    @Override
    public void handleReLogin() {

        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void handleNoNetWork() {
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
