package com.ascba.fanli.fragments.me;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.AccountActivity;
import com.ascba.fanli.activities.AccountRechargeActivity;
import com.ascba.fanli.activities.BusinessCenterActivity;
import com.ascba.fanli.activities.CardActivity;
import com.ascba.fanli.activities.CashGetActivity;
import com.ascba.fanli.activities.PersonalDataActivity;
import com.ascba.fanli.activities.RecommendActivity;
import com.ascba.fanli.activities.RedScoreActivity;
import com.ascba.fanli.activities.SettingActivity;
import com.ascba.fanli.activities.TicketActivity;
import com.ascba.fanli.activities.UserUpdateActivity;
import com.ascba.fanli.activities.WSAccountActivity;
import com.ascba.fanli.activities.login.LoginActivity;


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
    private View goCardView;
    private View goCashAcView;
    private View goRecView;
    private View goTicketView;
    private ImageView goUserCenterView;

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
                startActivityForResult(intent,1);
            }
        });
        //点击用户头像进入个人中心
        goUserCenterView = ((ImageView) view.findViewById(R.id.me_user_img));
        goUserCenterView.setOnClickListener(this);
        //点击提现进入提现界面
        goGetCashView = view.findViewById(R.id.tv_go_get_cash);
        goGetCashView.setOnClickListener(this);
        //点击预付款进入充值界面
        goRechargeView = view.findViewById(R.id.me_pre_go_recharge);
        goRechargeView.setOnClickListener(this);
        //点击银行卡进入银行卡界面
        goCardView = view.findViewById(R.id.me_go_card);
        goCardView.setOnClickListener(this);
        //点击现金账单进入现金账单界面
        goCashAcView = view.findViewById(R.id.me_go_cash_account);
        goCashAcView.setOnClickListener(this);
        //点击现金账单进入现金账单界面
        goTicketView = view.findViewById(R.id.me_go_ticket);
        goTicketView.setOnClickListener(this);
        //点击白积分进入白积分账单详情
        whiteScoreView = view.findViewById(R.id.go_white_score_account);
        whiteScoreView.setOnClickListener(this);
        //点击红积分进入红积分界面
        goRed = view.findViewById(R.id.me_go_red_score);
        goRed.setOnClickListener(this);
        //点击会员升级进入升级界面
        vipView = view.findViewById(R.id.me_go_vip);
        vipView.setOnClickListener(this);
        //点击会员升级进入升级界面
        goRecView = view.findViewById(R.id.me_go_recommend);
        goRecView.setOnClickListener(this);

        //点击商户中心进入商户中心界面
        goCenterView = view.findViewById(R.id.me_go_business_center);
        goCenterView.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id= v.getId();
        switch (id){
            case R.id.me_user_img:
                Intent intentUser=new Intent(getActivity(), PersonalDataActivity.class);
                startActivity(intentUser);
                break;
            case R.id.go_white_score_account:
                Intent intent=new Intent(getActivity(), WSAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.me_go_card:
                Intent intentCard=new Intent(getActivity(), CardActivity.class);
                startActivity(intentCard);
                break;
            case R.id.me_go_cash_account:
                Intent intentCash=new Intent(getActivity(), AccountActivity.class);
                startActivity(intentCash);
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
                Intent intent4=new Intent(getActivity(), UserUpdateActivity.class);
                startActivity(intent4);
                break;
            case R.id.me_go_business_center:
                Intent intent5=new Intent(getActivity(), BusinessCenterActivity.class);
                startActivity(intent5);
                break;
            case R.id.tv_go_get_cash:
                Intent intent6=new Intent(getActivity(), CashGetActivity.class);
                startActivity(intent6);
                break;
            case R.id.me_go_recommend:
                Intent intent7=new Intent(getActivity(), RecommendActivity.class);
                startActivity(intent7);
                break;
            case R.id.me_go_ticket:
                Intent intent8=new Intent(getActivity(), TicketActivity.class);
                startActivity(intent8);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            getActivity().finish();
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }
}