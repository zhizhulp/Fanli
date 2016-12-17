package com.ascba.rebate.fragments.me;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.AccountRechargeActivity;
import com.ascba.rebate.activities.AddCardActivity;
import com.ascba.rebate.activities.BusinessCenter2Activity;
import com.ascba.rebate.activities.BusinessCenterActivity;
import com.ascba.rebate.activities.BusinessDataActivity;
import com.ascba.rebate.activities.CardActivity;
import com.ascba.rebate.activities.CashGetActivity;
import com.ascba.rebate.activities.PersonalDataActivity;
import com.ascba.rebate.activities.RealNameCofirmActivity;
import com.ascba.rebate.activities.RecommendActivity;
import com.ascba.rebate.activities.RedScoreUpdateActivity;
import com.ascba.rebate.activities.SettingActivity;
import com.ascba.rebate.activities.TicketActivity;
import com.ascba.rebate.activities.UserUpdateActivity;
import com.ascba.rebate.activities.WSExchangeActivity;
import com.ascba.rebate.activities.WhiteScoreActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.fragments.base.BaseFragment;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.NetUtils;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.squareup.picasso.Picasso;
import com.yolanda.nohttp.rest.Request;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 *  我的中心
 */
public class FourthFragment extends BaseFragment implements View.OnClickListener,BaseFragment.Callback{

    private static final int REQUEST_PAY = 3;
    private static final int REQUEST_CLOSE = 4;
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
    private TextView tvWhiteScore;
    private TextView tvNickName;
    private TextView tvMoney;
    private TextView tvBanks;
    private TextView tvTicket;
    private LinearLayout imgsContainer;
    private int merchant;
    private View carView;
    private View houseView;
    private String noOpen="近期开放，工程师努力升级中";

    private SuperSwipeRefreshLayout refreshLayout;
    private ProgressBar progressBar;
    private ImageView imageView;
    private TextView textView;
    private int finalScene;
    private TextView tvRedScore;
    private TextView tvRecNum;


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
        requestMyData(0);//请求我的数据

        //点击设置进入设置页面
        mSettingText = ((TextView) view.findViewById(R.id.me_setting_text));
        mSettingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SettingActivity.class);
                startActivityForResult(intent,REQUEST_CLOSE);
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
        //点击代金券进入代金券界面
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
        //点击汽车
        carView = view.findViewById(R.id.qlqw_car);
        carView.setOnClickListener(this);
        //点击房产
        houseView = view.findViewById(R.id.qlqw_house);
        houseView.setOnClickListener(this);

    }

    private void requestMyData(final int scene) {
        finalScene=scene;
        if(scene==0){
            boolean netAva = NetUtils.isNetworkAvailable(getActivity());
            if(netAva){
                /*textView.setText("正在刷新");
                imageView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);*/
                Request<JSONObject> request = buildNetRequest(UrlUtils.user, 0, true);
                executeNetWork(request,"请稍后");
                setCallback(this);
            }else {
                DialogManager dm=new DialogManager(getActivity());
                dm.buildAlertDialog("请打开网络！");
                refreshLayout.setRefreshing(false);
//                progressBar.setVisibility(View.GONE);
            }
        }else if(scene==1){
            Request<JSONObject> request = buildNetRequest(UrlUtils.getCompany, 0, true);
            request.add("company_status",merchant+"");
            executeNetWork(request,"请稍后");
            setCallback(this);
        }else if(scene==2){
            Request<JSONObject> request = buildNetRequest(UrlUtils.checkCardId, 0, true);
            executeNetWork(request,"请稍后");
            setCallback(this);
        }else if(scene==3){
            Request<JSONObject> request = buildNetRequest(UrlUtils.checkCardId, 0, true);
            executeNetWork(request,"请稍后");
            setCallback(this);
        }

    }

    private void initViews(View view) {
        tvWhiteScore = ((TextView) view.findViewById(R.id.me_tv_white_score));
        tvNickName = ((TextView) view.findViewById(R.id.me_tv_nick_name));
        tvMoney = ((TextView) view.findViewById(R.id.me_tv_money));
        tvBanks = ((TextView) view.findViewById(R.id.me_tv_banks));
        tvTicket = ((TextView) view.findViewById(R.id.me_tv_ticket));
        tvRedScore = ((TextView) view.findViewById(R.id.me_tv_red_score));
        tvRecNum = ((TextView) view.findViewById(R.id.rec_num));
        initRefreshLayout(view);

    }

    private void initRefreshLayout(View view) {
        refreshLayout = ((SuperSwipeRefreshLayout) view.findViewById(R.id.four_superlayout));
        /*View child = LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_head, null);
        progressBar = (ProgressBar) child.findViewById(R.id.pb_view);
        textView = (TextView) child.findViewById(R.id.text_view);
        textView.setText("下拉刷新");
        imageView = (ImageView) child.findViewById(R.id.image_view);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.down_arrow);
        progressBar.setVisibility(View.GONE);
        refreshLayout.setHeaderView(child);*/
        refreshLayout
                .setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {

                    @Override
                    public void onRefresh() {
                        requestMyData(0);
                    }

                    @Override
                    public void onPullDistance(int distance) {
                        //myAdapter.updateHeaderHeight(distance);
                    }

                    @Override
                    public void onPullEnable(boolean enable) {
                        /*textView.setText(enable ? "松开刷新" : "下拉刷新");
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setRotation(enable ? 180 : 0);*/
                    }
                });

    }
    @Override
    public void onClick(View v) {
        int id= v.getId();
        DialogManager dm=new DialogManager(getActivity());
        switch (id){
            case R.id.me_user_img:
                Intent intentUser=new Intent(getActivity(), PersonalDataActivity.class);
                startActivity(intentUser);
                break;
            case R.id.go_white_score_account:
                if(true){
                    Intent intent=new Intent(getActivity(), WhiteScoreActivity.class);
                    startActivity(intent);
                }else{
                    dm.buildAlertDialog(noOpen);

                }
                break;
            case R.id.me_go_card:
                requestMyData(2);//检查是否实名
                break;
            case R.id.me_go_cash_account:
                dm.buildAlertDialog(noOpen);
                /*Intent intentCash=new Intent(getActivity(), AllAccountActivity.class);
                startActivity(intentCash);*/
                break;
            case R.id.me_pre_go_recharge:
                Intent intent2=new Intent(getActivity(), AccountRechargeActivity.class);
                startActivityForResult(intent2,REQUEST_PAY);
                break;
            case R.id.me_go_red_score:
//                dm.buildAlertDialog(noOpen);
                Intent intent3=new Intent(getActivity(), RedScoreUpdateActivity.class);
                startActivity(intent3);
                break;
            case R.id.me_go_vip:
                Intent intent4=new Intent(getActivity(), UserUpdateActivity.class);
                startActivity(intent4);
                break;
            case R.id.me_go_business_center:
                dm.buildAlertDialog(noOpen);
                //requestMyData(1);
                break;
            case R.id.tv_go_get_cash:
//                dm.buildAlertDialog(noOpen);
                requestMyData(3);//检查是否实名
                /*Intent intent6=new Intent(getActivity(), CashGetActivity.class);
                startActivity(intent6);*/
                break;
            case R.id.me_go_recommend:
                /*dm.buildAlertDialog(noOpen);
                return;*/
                Intent intent7=new Intent(getActivity(), RecommendActivity.class);
                startActivity(intent7);
                break;
            case R.id.me_go_ticket:
                Intent intent8=new Intent(getActivity(), TicketActivity.class);
                startActivity(intent8);
                break;
            case R.id.me_go_proxy_center:
                dm.buildAlertDialog(noOpen);
                break;
            case R.id.qlqw_car:
                dm.buildAlertDialog("敬请期待！");
                break;
            case R.id.qlqw_house:
                dm.buildAlertDialog("敬请期待！");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            switch (requestCode){
                case REQUEST_PAY:
                    requestMyData(0);
                    break;
                case REQUEST_CLOSE:
                    getActivity().finish();
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    break;
            }

        }
    }
    @Override
    public void handle200Data(final JSONObject dataObj, String message) {
        if(finalScene==0){//我的数据
            refreshLayout.setRefreshing(false);
//            progressBar.setVisibility(View.GONE);
            imgsContainer.removeAllViews();

            JSONObject infoObj = dataObj.optJSONObject("myInfo");
            String avatar = infoObj.optString("avatar");//头像url地址
            int white_score = infoObj.optInt("white_score");//白积分
            int red_score = infoObj.optInt("red_score");//红积分
            String nickname = infoObj.optString("nickname");//用户昵称
            String money = infoObj.optString("money");//用户余额
            int banks = infoObj.optInt("banks");//银行卡数量
            int vouchers = infoObj.optInt("vouchers");//代金券数量
            int referee_num = infoObj.optInt("referee_num");//推荐人数量

            merchant = infoObj.optInt("merchant");
            Picasso.with(getActivity()).load(avatar).into(goUserCenterView);
            JSONArray group_type = infoObj.optJSONArray("group_type");
            for (int i = 0; i < group_type.length(); i++) {
                JSONObject typeObj = group_type.optJSONObject(i);
                int isUpgraded = typeObj.optInt("isUpgraded");
                int id = typeObj.optInt("id");
                ImageView imageView=new ImageView(getActivity());
                int i1 = ScreenDpiUtils.dip2px(getActivity(), 14);
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(i1, i1);
                int dpLeft = ScreenDpiUtils.dip2px(getActivity(), 7);
                lp.leftMargin=dpLeft;
                imageView.setLayoutParams(lp);
                String baseUrl="http://api.qlqwgw.com";
                if(isUpgraded==1||id==1){
                    String upgraded_icon = typeObj.optString("upgraded_icon");
                    Picasso.with(getActivity()).load(baseUrl+upgraded_icon).into(imageView);
                }else{
                    String default_icon = typeObj.optString("default_icon");
                    Picasso.with(getActivity()).load(baseUrl+default_icon).into(imageView);
                }
                imgsContainer.addView(imageView);
            }
            tvWhiteScore.setText(white_score+"");
            tvRedScore.setText(red_score+"");
            tvNickName.setText(nickname);
            tvMoney.setText("账户余额："+money);
            tvBanks.setText(banks+"");
            tvTicket.setText(vouchers+"张");
            tvRecNum.setText(referee_num+"人");
        }else if(finalScene==1){//点击商户中心
            JSONObject company = dataObj.optJSONObject("company");
            if(merchant==3){
                String name = company.optString("name");
                String corporate = company.optString("corporate");
                String address = company.optString("address");
                String tel = company.optString("tel");
                Intent intent=new Intent(getActivity(), BusinessDataActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("corporate",corporate);
                intent.putExtra("tel",tel);
                intent.putExtra("address",address);
                startActivity(intent);
            }else if(merchant==0){
                Intent intent=new Intent(getActivity(),BusinessCenterActivity.class);
                startActivity(intent);
            }else if(merchant==1){
                Intent intent=new Intent(getActivity(),BusinessCenter2Activity.class);
                String name = company.optString("name");
                String corporate = company.optString("corporate");
                String address = company.optString("address");
                String tel = company.optString("tel");
                intent.putExtra("type",0);
                intent.putExtra("name_01",name);
                intent.putExtra("corporate_01",corporate);
                intent.putExtra("tel_01",tel);
                intent.putExtra("address_01",address);
                startActivity(intent);
            }else if(merchant==2){
                Intent intent=new Intent(getActivity(),BusinessCenter2Activity.class);
                String name = company.optString("name");
                String corporate = company.optString("corporate");
                String address = company.optString("address");
                String tel = company.optString("tel");
                intent.putExtra("type",1);
                intent.putExtra("name",name);
                intent.putExtra("corporate",corporate);
                intent.putExtra("tel",tel);
                intent.putExtra("address",address);
                startActivity(intent);
            }
        }else if(finalScene==2){//检查是否实名，点击银行卡前
            int isCardId = dataObj.optInt("isCardId");
            int isBankCard = dataObj.optInt("isBankCard");
            if(isCardId==0){
                final DialogManager dm=new DialogManager(getActivity());
                dm.buildAlertDialog1("暂未实名认证，是否立即实名认证？");
                dm.setCallback(new DialogManager.Callback() {
                    @Override
                    public void handleSure() {
                        dm.dismissDialog();
                        Intent intent=new Intent(getActivity(), RealNameCofirmActivity.class);
                        startActivity(intent);
                    }
                });
            }else {
                JSONObject cardObj = dataObj.optJSONObject("cardInfo");
                if(isBankCard==0){
                    Intent intent=new Intent(getActivity(),AddCardActivity.class);
                    intent.putExtra("realname",cardObj.optString("realname"));
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(getActivity(),CardActivity.class);
                    startActivity(intent);
                }
            }
        }else if(finalScene==3){//检查是否实名，点击提现前
            int isCardId = dataObj.optInt("isCardId");
            int isBankCard = dataObj.optInt("isBankCard");
            if(isCardId==0){
                final DialogManager dm=new DialogManager(getActivity());
                dm.buildAlertDialog1("暂未实名认证，是否立即实名认证？");
                dm.setCallback(new DialogManager.Callback() {
                    @Override
                    public void handleSure() {
                        dm.dismissDialog();
                        Intent intent=new Intent(getActivity(), RealNameCofirmActivity.class);
                        startActivity(intent);
                    }
                });
            }else {
                JSONObject cardObj = dataObj.optJSONObject("cardInfo");
                Intent intent=new Intent(getActivity(),CashGetActivity.class);
                intent.putExtra("bank_card_number",isBankCard);
                intent.putExtra("realname",cardObj.optString("realname"));
                startActivity(intent);
            }
        }
    }
}