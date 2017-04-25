package com.ascba.rebate.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.TransactionRecordsActivity;
import com.ascba.rebate.activities.me_page.AccountRechargeActivity;
import com.ascba.rebate.activities.me_page.AllAccountActivity;
import com.ascba.rebate.activities.me_page.CardActivity;
import com.ascba.rebate.activities.me_page.CashGetActivity;
import com.ascba.rebate.activities.me_page.RedScoreUpdateActivity;
import com.ascba.rebate.activities.me_page.TicketActivity;
import com.ascba.rebate.activities.me_page.WhiteBillActivity;
import com.ascba.rebate.activities.me_page.WhiteScoreActivity;
import com.ascba.rebate.activities.me_page.bank_card_child.AddCardActivity;
import com.ascba.rebate.activities.me_page.settings.child.RealNameCofirmActivity;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.fragments.base.BaseNetFragment;
import com.ascba.rebate.fragments.base.LazyBaseFragment;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.NetUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

/**
 * 财富
 */
public class MoneyFragment extends LazyBaseFragment implements SuperSwipeRefreshLayout.OnPullRefreshListener, View.OnClickListener
        , BaseNetFragment.Callback {

    private SuperSwipeRefreshLayout refreshLayout;
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
    private int finalScene;

    @Override
    protected int setContentView() {
        return R.layout.fragment_money;
    }

    @Override
    protected void lazyLoad() {
        if (MyApplication.isLoad) {
            requestMyData(0);
        }
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
        View viewfyzh = view.findViewById(R.id.me_lat_fyzh);
        viewfyzh.setOnClickListener(this);
        View viewChongzhi = view.findViewById(R.id.me_lat_chongzhi);
        viewChongzhi.setOnClickListener(this);
        View viewBank = view.findViewById(R.id.me_lat_bank);
        viewBank.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {
        if( NetUtils.isNetworkAvailable(getActivity())){
            requestMyData(0);
        }else {
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
            case R.id.me_account:
                Intent intent = new Intent(getActivity(), AllAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.me_lat_daifan://白积分
                Intent intent4=new Intent(getActivity(), WhiteBillActivity.class);
                startActivity(intent4);
                break;
            case R.id.me_lat_duihuan:
                Intent intent3 = new Intent(getActivity(), RedScoreUpdateActivity.class);
                startActivity(intent3);
                break;
            case R.id.me_lat_jiaoyi://兑现券
                Intent intent1 = new Intent(getActivity(), WhiteScoreActivity.class);
                startActivity(intent1);
                break;
            case R.id.me_lat_djq:
                Intent intent8 = new Intent(getActivity(), TicketActivity.class);
                startActivity(intent8);
                break;
            case R.id.me_lat_grzh://提现
                requestMyData(3);//检查是否实名
                break;
            case R.id.me_lat_fyzh://返佣账户
                /*Intent intent4=new Intent(getActivity(), CommissionActivity.class);
                startActivity(intent4);*/
                Intent intent6=new Intent(getActivity(),TransactionRecordsActivity.class);
                startActivity(intent6);
                break;
            case R.id.me_lat_chongzhi:
                Intent intent2 = new Intent(getActivity(), AccountRechargeActivity.class);
                startActivity(intent2);
                break;
            case R.id.me_lat_bank:
                requestMyData(2);//检查是否实名
                break;
        }
    }

    private void requestMyData(int scene) {
        finalScene = scene;
        if (scene == 2) {
            Request<JSONObject> request = buildNetRequest(UrlUtils.checkCardId, 0, true);
            executeNetWork(request, "请稍后");
            setCallback(this);
        } else if (scene == 0) {
            Request<JSONObject> request = buildNetRequest(UrlUtils.wealth, 0, true);
            executeNetWork(request, "请稍后");
            setCallback(this);
        } else if (scene == 3) {
            Request<JSONObject> request = buildNetRequest(UrlUtils.checkCardId, 0, true);
            executeNetWork(request, "请稍后");
            setCallback(this);
        }
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if (finalScene == 2) {
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
        } else if (finalScene == 0) {
            LogUtils.PrintLog("财富数据","data-->"+dataObj);
            if (refreshLayout != null && refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
            }
            JSONObject infoObj = dataObj.optJSONObject("myInfo");
            tvAllCash.setText(infoObj.optString("money_count"));
            tvRed.setText(infoObj.optInt("yesterday_red_score") + "");
            tvWhite.setText(infoObj.optInt("white_score") + "");
            tvDaiFan.setText(infoObj.optInt("white_score") + "");
            tvDuiHuan.setText(infoObj.optInt("red_score") + "");
            tvDjq.setText(infoObj.optInt("vouchers") + "张");
            tvGrzh.setText(infoObj.optString("money"));
            tvSjzh.setText(infoObj.optString("account_money"));
            tvBank.setText(infoObj.optInt("banks") + "张");
        } else if (finalScene == 3) {//检查是否实名，点击提现前
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
                startActivity(intent);
            }
        }
    }

    @Override
    public void handleReqFailed() {
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void handle404(String message, JSONObject dataObj) {
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

    }

}
