package com.ascba.rebate.fragments.me;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.AccountActivity;
import com.ascba.rebate.activities.AccountRechargeActivity;
import com.ascba.rebate.activities.AllAccountActivity;
import com.ascba.rebate.activities.BusinessCenter2Activity;
import com.ascba.rebate.activities.BusinessCenterActivity;
import com.ascba.rebate.activities.BusinessDataActivity;
import com.ascba.rebate.activities.CardActivity;
import com.ascba.rebate.activities.CashGetActivity;
import com.ascba.rebate.activities.PersonalDataActivity;
import com.ascba.rebate.activities.RecommendActivity;
import com.ascba.rebate.activities.RedScoreActivity;
import com.ascba.rebate.activities.SettingActivity;
import com.ascba.rebate.activities.TicketActivity;
import com.ascba.rebate.activities.UserUpdateActivity;
import com.ascba.rebate.activities.WSAccountActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.squareup.picasso.Picasso;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


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
    private View goProxyCenterView;
    private CircleImageView goUserCenterView;
    private PhoneHandler phoneHandler;
    private CheckThread checkThread;
    private RequestQueue requestQueue;
    private SharedPreferences sf;
    private TextView tvWhiteScore;
    private TextView tvNickName;
    private TextView tvMoney;
    private TextView tvBanks;
    private TextView tvTicket;
    private LinearLayout imgsContainer;
    private int merchant;

//    public static FourthFragment instance() {
//        FourthFragment view = new FourthFragment();
//        return view;
//    }

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
        initViews(view);
        //初始化8个小图标parent
        imgsContainer = ((LinearLayout) view.findViewById(R.id.container_imgs));
        sf=getActivity().getSharedPreferences("first_login_success_name_password",MODE_PRIVATE);
        sendMsgToSevr("http://api.qlqwgw.com/v1/user",0);


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
        goUserCenterView = ((CircleImageView) view.findViewById(R.id.me_user_img));
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
        //点击商户中心进入商户中心界面
        goProxyCenterView = view.findViewById(R.id.me_go_proxy_center);
        goProxyCenterView.setOnClickListener(this);


    }

    private void initViews(View view) {
        tvWhiteScore = ((TextView) view.findViewById(R.id.me_tv_white_score));
        tvNickName = ((TextView) view.findViewById(R.id.me_tv_nick_name));
        tvMoney = ((TextView) view.findViewById(R.id.me_tv_money));
        tvBanks = ((TextView) view.findViewById(R.id.me_tv_banks));
        tvTicket = ((TextView) view.findViewById(R.id.me_tv_ticket));
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
                if(true){
                    final PopupWindow pop=new PopupWindow(getActivity());
                    pop.setBackgroundDrawable(new ColorDrawable(0xE8E8E7));
                    pop.setOutsideTouchable(false);
                    View view=getActivity().getLayoutInflater().inflate(R.layout.pop_white_no_permission,null);
                    view.findViewById(R.id.cry_close_icon).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pop.dismiss();
                        }
                    });
                    pop.setContentView(view);
                    pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                            params.alpha = 1.0f;
                            getActivity().getWindow().setAttributes(params);
                        }
                    });
                    DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
                    int widthPixels = dm.widthPixels;
                    pop.setWidth(widthPixels/2);
                    pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                    pop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER,0,0);
                    //设置背景变暗
                    WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                    params.alpha = 0.5f;
                    getActivity().getWindow().setAttributes(params);
                }else{
                    Intent intent=new Intent(getActivity(), WSAccountActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.me_go_card:
                Intent intentCard=new Intent(getActivity(), CardActivity.class);
                startActivity(intentCard);
                break;
            case R.id.me_go_cash_account:
                Intent intentCash=new Intent(getActivity(), AllAccountActivity.class);
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
                sendMsgToSevr("http://api.qlqwgw.com/v1/getCompany",1);
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
            case R.id.me_go_proxy_center:
                Toast.makeText(getActivity(), "稍后开放", Toast.LENGTH_SHORT).show();
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
    private void sendMsgToSevr(String baseUrl, final int scene) {
        int uuid = sf.getInt("uuid",-1000 );
        String token = sf.getString("token", "");
        long expiring_time = sf.getLong("expiring_time", -2000);
        if(requestQueue==null){
            requestQueue= NoHttp.newRequestQueue();
        }

        final ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.dialog);
        dialog.setMessage("请稍后");
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest(baseUrl+"?", RequestMethod.POST);
        objRequest.add("sign", UrlEncodeUtils.createSign(baseUrl));
        objRequest.add("uuid",uuid);
        objRequest.add("token",token);
        objRequest.add("expiring_time",expiring_time);
        if(scene==1){
            objRequest.add("company_stauts",merchant);
        }
        phoneHandler=new PhoneHandler(getActivity());
        phoneHandler.setCallback(new PhoneHandler.Callback() {


            @Override
            public void getMessage(Message msg) {
                dialog.dismiss();
                JSONObject jObj= (JSONObject) msg.obj;
                LogUtils.PrintLog("123FourthFragment",jObj.toString());
                try {
                    int status = jObj.getInt("status");
                    JSONObject dataObj = jObj.optJSONObject("data");
                    int update_status = dataObj.optInt("update_status");
                    if(status==200){
                        if(scene==0){
                            if(update_status==1){
                                sf.edit()
                                        .putString("token",dataObj.getString("token"))
                                        .putLong("expiring_time",dataObj.getLong("expiring_time"))
                                        .apply();
                            }
                            JSONObject infoObj = dataObj.optJSONObject("myInfo");
                            String avatar = infoObj.optString("avatar");//头像url地址
                            int white_score = infoObj.optInt("white_score");//白积分
                            String nickname = infoObj.optString("nickname");//用户昵称
                            String money = infoObj.getString("money");//用户余额
                            int banks = infoObj.getInt("banks");//银行卡数量
                            int vouchers = infoObj.getInt("vouchers");//代金券数量
                            merchant = infoObj.getInt("merchant");
                            Picasso.with(getActivity()).load(avatar).into(goUserCenterView);
                            JSONArray group_type = infoObj.getJSONArray("group_type");
                            for (int i = 0; i < group_type.length(); i++) {
                                JSONObject typeObj = group_type.getJSONObject(i);
                                int isUpgraded = typeObj.getInt("isUpgraded");
                                int id = typeObj.getInt("id");
                                ImageView imageView=new ImageView(getActivity());
                                int i1 = ScreenDpiUtils.dip2px(getActivity(), 18);
                                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(i1, i1);
                                int dpLeft = ScreenDpiUtils.dip2px(getActivity(), 7);
                                lp.leftMargin=dpLeft;
                                imageView.setLayoutParams(lp);
                                String baseUrl="http://api.qlqwgw.com";

                                if(isUpgraded==1||id==1){
                                    String upgraded_icon = typeObj.getString("upgraded_icon");
                                    Picasso.with(getActivity()).load(baseUrl+upgraded_icon).into(imageView);
                                }else{
                                    String default_icon = typeObj.getString("default_icon");
                                    Picasso.with(getActivity()).load(baseUrl+default_icon).into(imageView);
                                }
                                imgsContainer.addView(imageView);
                            }
                            tvWhiteScore.setText(white_score+"");
                            tvNickName.setText(nickname);
                            tvMoney.setText("账户余额："+money);
                            tvBanks.setText(banks+"");
                            tvTicket.setText(vouchers+"张");
                        }else{
                            if(merchant==3){
                                JSONObject company = dataObj.getJSONObject("company");
                                Intent intent=new Intent(getActivity(), BusinessDataActivity.class);
                                String name = company.optString("name");
                                String corporate = company.optString("corporate");
                                String address = company.optString("address");
                                String tel = company.optString("tel");
                                intent.putExtra("name",name);
                                intent.putExtra("corporate",corporate);
                                intent.putExtra("tel",tel);
                                intent.putExtra("address",address);
                                startActivity(intent);
                            }
                        }


                    } else if(status==404){
                        JSONObject comObj = dataObj.optJSONObject("company");

                        if(merchant==0){
                            Intent intent=new Intent(getActivity(),BusinessCenterActivity.class);
                            startActivity(intent);
                        }else if(merchant==1){
                            Intent intent=new Intent(getActivity(),BusinessCenter2Activity.class);
                            String name = comObj.optString("name");
                            String corporate = comObj.optString("corporate");
                            String address = comObj.optString("address");
                            String tel = comObj.optString("tel");
                            intent.putExtra("name_01",name);
                            intent.putExtra("corporate_01",corporate);
                            intent.putExtra("tel_01",tel);
                            intent.putExtra("address_01",address);
                            startActivity(intent);
                        }else if(merchant==2){
                            Intent intent=new Intent(getActivity(),BusinessCenter2Activity.class);
                            String name = comObj.optString("name");
                            String corporate = comObj.optString("corporate");
                            String address = comObj.optString("address");
                            String tel = comObj.optString("tel");
                            intent.putExtra("name",name);
                            intent.putExtra("corporate",corporate);
                            intent.putExtra("tel",tel);
                            intent.putExtra("address",address);
                            startActivity(intent);
                        }

                    } else if(status==5){
                        Intent intent=new Intent(getActivity(),LoginActivity.class);
                        sf.edit().putInt("uuid",-1000).apply();
                        startActivity(intent);
                        getActivity().finish();
                    }else if(status==3){
                        Intent intent=new Intent(getActivity(),LoginActivity.class);
                        sf.edit().putInt("uuid",-1000).apply();
                        startActivity(intent);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        checkThread=new CheckThread(requestQueue,phoneHandler,objRequest);
        checkThread.start();
        dialog.show();
    }
}