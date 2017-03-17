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
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.me_page.AccountRechargeActivity;
import com.ascba.rebate.activities.me_page.AllAccountActivity;
import com.ascba.rebate.activities.me_page.CardActivity;
import com.ascba.rebate.activities.me_page.CashGetActivity;
import com.ascba.rebate.activities.me_page.RedScoreUpdateActivity;
import com.ascba.rebate.activities.me_page.TicketActivity;
import com.ascba.rebate.activities.me_page.WhiteScoreActivity;
import com.ascba.rebate.activities.me_page.bank_card_child.AddCardActivity;
import com.ascba.rebate.activities.me_page.business_center_child.BCProcessActivity;
import com.ascba.rebate.activities.me_page.business_center_child.BusinessCenterActivity;
import com.ascba.rebate.activities.me_page.business_center_child.child.BusinessDataActivity;
import com.ascba.rebate.activities.me_page.settings.child.RealNameCofirmActivity;
import com.ascba.rebate.fragments.base.Base2Fragment;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

/**
 * 财富
 */
public class MoneyFragment extends Base2Fragment implements SuperSwipeRefreshLayout.OnPullRefreshListener,View.OnClickListener
,Base2Fragment.Callback{


    private SuperSwipeRefreshLayout refreshLayout;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private TextView tvAllCash;
    private TextView tvRed;
    private TextView tvWhite;
    private TextView tvRate;
    private TextView tvDaiFan;
    private TextView tvDuiHuan;
    private TextView tvJiaoYi;
    private TextView tvDjq;
    private TextView tvGrzh;
    private TextView tvSjzh;
    private TextView tvBank;
    private View accountView;
    private static final int REQUEST_RED=5;
    private static final int REQUEST_PAY=2;
    private static final int REQUEST_APPLY=3;
    private static final int REQUEST_CASH_GET=4;
    private int finalScene;

    public MoneyFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_money, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        refreshLayout = ((SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_layout));
        refreshLayout.setOnPullRefreshListener(this);

        accountView = view.findViewById(R.id.me_account);
        accountView.setOnClickListener(this);

        tvAllCash = ((TextView) view.findViewById(R.id.cash_left));

        tvRed = ((TextView) view.findViewById(R.id.me_tv_red_score));
        tvWhite = ((TextView) view.findViewById(R.id.me_tv_white_score));
        tvRate = ((TextView) view.findViewById(R.id.me_tv_rate));

        tvDaiFan = ((TextView) view.findViewById(R.id.me_tv_daifan));
        tvDuiHuan = ((TextView) view.findViewById(R.id.me_tv_duihuan));
        tvJiaoYi = ((TextView) view.findViewById(R.id.me_tv_jiaoyi));
        tvDjq = ((TextView) view.findViewById(R.id.me_tv_djq));
        tvGrzh = ((TextView) view.findViewById(R.id.me_tv_grzh));
        tvSjzh = ((TextView) view.findViewById(R.id.me_tv_sjzh));
        tvBank = ((TextView) view.findViewById(R.id.me_tv_bank));

        View viewDaiFan = view.findViewById(R.id.me_lat_daifan);
        viewDaiFan.setOnClickListener(this);
        View viewDuiHuan = view.findViewById(R.id.me_lat_duihuan);
        viewDuiHuan.setOnClickListener(this);
        View viewJiaoYi = view.findViewById(R.id.me_lat_jiaoyi);
        viewJiaoYi.setOnClickListener(this);
        View viewDjq = view.findViewById(R.id.me_lat_djq);
        viewDjq.setOnClickListener(this);
        View viewGrzh = view.findViewById(R.id.me_lat_grzh);
        viewGrzh.setOnClickListener(this);
        View viewSjzh = view.findViewById(R.id.me_lat_sjzh);
        viewSjzh.setOnClickListener(this);
        View viewChongzhi = view.findViewById(R.id.me_lat_chongzhi);
        viewChongzhi.setOnClickListener(this);
        View viewBank= view.findViewById(R.id.me_lat_bank);
        viewBank.setOnClickListener(this);

        requestMyData(0);
    }

    @Override
    public void onRefresh() {
        requestMyData(0);
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.me_account:
                Intent intent=new Intent(getActivity(), AllAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.me_lat_daifan:
                Intent intent1=new Intent(getActivity(), WhiteScoreActivity.class);
                startActivityForResult(intent1,WhiteScoreActivity.REQUEST_EXCHANGE);
                break;
            case R.id.me_lat_duihuan:
                Intent intent3 = new Intent(getActivity(), RedScoreUpdateActivity.class);
                startActivityForResult(intent3, REQUEST_RED);
                break;
            case R.id.me_lat_jiaoyi:

                break;
            case R.id.me_lat_djq:
                Intent intent8 = new Intent(getActivity(), TicketActivity.class);
                startActivity(intent8);
                break;
            case R.id.me_lat_grzh://提现
                requestMyData(3);//检查是否实名
                break;
            case R.id.me_lat_sjzh://商户申请
                requestMyData(1);
                break;
            case R.id.me_lat_chongzhi:
                Intent intent2 = new Intent(getActivity(), AccountRechargeActivity.class);
                startActivityForResult(intent2, REQUEST_PAY);
                break;
            case R.id.me_lat_bank:
                requestMyData(2);//检查是否实名
                break;
        }
    }

    private void requestMyData(int scene) {
        finalScene=scene;
        if(scene==2){
            Request<JSONObject> request = buildNetRequest(UrlUtils.checkCardId, 0, true);
            executeNetWork(request, "请稍后");
            setCallback(this);
        }else if(scene==1){
            Request<JSONObject> request = buildNetRequest(UrlUtils.getCompany, 0, true);
            executeNetWork(request, "请稍后");
            setCallback(this);
        }else if(scene==0){
            Request<JSONObject> request = buildNetRequest(UrlUtils.wealth, 0, true);
            executeNetWork(request, "请稍后");
            setCallback(this);
        }else if(scene==3){
            Request<JSONObject> request = buildNetRequest(UrlUtils.checkCardId, 0, true);
            executeNetWork(request, "请稍后");
            setCallback(this);
        }
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if(finalScene==2){
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
        }else if(finalScene==1){
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
        }else if(finalScene==0){
            if(refreshLayout!=null &&refreshLayout.isRefreshing()){
                refreshLayout.setRefreshing(false);
            }
            JSONObject infoObj = dataObj.optJSONObject("myInfo");
            tvAllCash.setText(infoObj.optString("money_count"));
            tvRed.setText(infoObj.optInt("yesterday_red_score")+"");
            tvWhite.setText(infoObj.optInt("white_score")+"");
            tvDaiFan.setText(infoObj.optInt("white_score")+"");
            tvDuiHuan.setText(infoObj.optInt("red_score")+"");
            tvDjq.setText(infoObj.optInt("vouchers")+"");
            tvGrzh.setText(infoObj.optString("money"));
            tvSjzh.setText(infoObj.optString("account_money"));
            tvBank.setText(infoObj.optInt("banks")+"张");
        }else if (finalScene == 3) {//检查是否实名，点击提现前
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
                Intent intent = new Intent(getActivity(), CashGetActivity.class);
                intent.putExtra("bank_card_number", isBankCard);
                intent.putExtra("realname", cardObj.optString("realname"));
                startActivityForResult(intent,REQUEST_CASH_GET);
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
    public void handleNoNetWork() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case REQUEST_PAY:
                    if (Activity.RESULT_OK == resultCode) {
                        requestMyData(0);
                    }
                    break;
                /*case REQUEST_CLOSE:
                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    break;*/
                case REQUEST_APPLY:
                    requestMyData(0);
                    break;
                case REQUEST_CASH_GET:
                    requestMyData(0);
                    break;
                case WhiteScoreActivity.REQUEST_EXCHANGE:
                    requestMyData(0);
                    break;
                case REQUEST_RED:
                    requestMyData(0);
                    break;
            }

        }
    }
}
