package com.qlqwgw.fanli.fragments.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import com.qlqwgw.fanli.R;
import com.qlqwgw.fanli.activities.AccountActivity;
import com.qlqwgw.fanli.activities.AccountRechargeActivity;
import com.qlqwgw.fanli.activities.BusinessCenterActivity;
import com.qlqwgw.fanli.activities.CashGetActivity;
import com.qlqwgw.fanli.activities.RedScoreActivity;
import com.qlqwgw.fanli.activities.SettingActivity;
import com.qlqwgw.fanli.beans.GridBean;

import java.util.ArrayList;
import java.util.List;


/**
 *  我的中心
 */
public class FourthFragment extends Fragment implements View.OnClickListener{

    private TextView mSettingText;
    private View whiteScoreView;
    private View goRechargeView;
    private View goRed;
    private View vipView;
    private View goCenterView;
    private View goGetCashView;

    public static FourthFragment instance() {
        FourthFragment view = new FourthFragment();
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            View view = inflater.inflate(R.layout.fourth_fragment_status, null);
            return view;
        }else{
            View view = inflater.inflate(R.layout.fourth_fragment, null);
            return view;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //点击设置进入设置页面
        mSettingText = ((TextView) view.findViewById(R.id.me_setting_text));
        mSettingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
        //点击白积分进入账单详情
        whiteScoreView = view.findViewById(R.id.go_white_score_account);
        whiteScoreView.setOnClickListener(this);
        //点击预付款进入充值界面
        goRechargeView = view.findViewById(R.id.me_pre_go_recharge);
        goRechargeView.setOnClickListener(this);
        //点击红积分进入红积分界面
        goRed = view.findViewById(R.id.me_go_red_score);
        goRed.setOnClickListener(this);
        //点击会员升级进入升级界面
        vipView = view.findViewById(R.id.me_go_vip);
        vipView.setOnClickListener(this);
        //点击商户中心进入商户中心界面
        goCenterView = view.findViewById(R.id.me_go_business_center);
        goCenterView.setOnClickListener(this);
        //点击提现进入提现界面
        goGetCashView = view.findViewById(R.id.tv_go_get_cash);
        goGetCashView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id= v.getId();
        switch (id){
            case R.id.go_white_score_account:
                Intent intent=new Intent(getActivity(), AccountActivity.class);
                startActivity(intent);
                break;
            case R.id.me_pre_go_recharge:
                Intent intent2=new Intent(getActivity(), AccountRechargeActivity.class);
                startActivity(intent2);
                break;
            case R.id.me_go_red_score:
                Intent intent3=new Intent(getActivity(), RedScoreActivity.class);
                startActivity(intent3);
                break;
            case R.id.me_go_vip:
                //Intent intent4=new Intent(getActivity(), .class);
                //startActivity(intent4);
                break;
            case R.id.me_go_business_center:
                Intent intent5=new Intent(getActivity(), BusinessCenterActivity.class);
                startActivity(intent5);
                break;
            case R.id.tv_go_get_cash:
                Intent intent6=new Intent(getActivity(), CashGetActivity.class);
                startActivity(intent6);
                break;
        }
    }
}