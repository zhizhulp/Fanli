package com.ascba.rebate.fragments.me;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.me_page.AccountRechargeActivity;
import com.ascba.rebate.activities.me_page.RecommActivity;
import com.ascba.rebate.activities.me_page.bank_card_child.AddCardActivity;
import com.ascba.rebate.activities.me_page.AllAccountActivity;
import com.ascba.rebate.activities.me_page.business_center_child.BCProcessActivity;
import com.ascba.rebate.activities.me_page.business_center_child.BusinessCenterActivity;
import com.ascba.rebate.activities.me_page.business_center_child.child.BusinessDataActivity;
import com.ascba.rebate.activities.me_page.CardActivity;
import com.ascba.rebate.activities.me_page.CashGetActivity;
import com.ascba.rebate.activities.me_page.settings.child.PersonalDataActivity;
import com.ascba.rebate.activities.me_page.settings.child.RealNameCofirmActivity;
import com.ascba.rebate.activities.me_page.RedScoreUpdateActivity;
import com.ascba.rebate.activities.me_page.settings.SettingActivity;
import com.ascba.rebate.activities.me_page.TicketActivity;
import com.ascba.rebate.activities.me_page.UserUpdateActivity;
import com.ascba.rebate.activities.me_page.WhiteScoreActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.fragments.base.BaseFragment;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.NetUtils;
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
 * 我的中心
 */
public class FourthFragment extends BaseFragment implements View.OnClickListener, BaseFragment.Callback {

    public static final int REQUEST_PAY = 3;
    private static final int REQUEST_CLOSE = 4;
    public static final int REQUEST_APPLY = 5;
    public static final int REQUEST_CASH_GET = 6;
    public static final int REQUEST_RED = 7;
    private CircleImageView goUserCenterView;
    private TextView tvWhiteScore;
    private TextView tvNickName;
    private TextView tvMoney;
    private TextView tvBanks;
    private TextView tvTicket;
    private LinearLayout imgsContainer;
    private SuperSwipeRefreshLayout refreshLayout;
    private int finalScene;
    private TextView tvRedScore;
    private TextView tvRecNum;
    private TextView tvBusiStatus;
    private DialogManager dm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fourth_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        //初始化8个小图标parent
        imgsContainer = ((LinearLayout) view.findViewById(R.id.container_imgs));
        requestMyData(0);//请求我的数据

        //点击设置进入设置页面
        TextView mSettingText = ((TextView) view.findViewById(R.id.me_setting_text));
        mSettingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivityForResult(intent, REQUEST_CLOSE);
            }
        });
        //点击用户头像进入个人中心
        goUserCenterView = ((CircleImageView) view.findViewById(R.id.me_user_img));
        goUserCenterView.setOnClickListener(this);
        //点击提现进入提现界面
        View goGetCashView = view.findViewById(R.id.tv_go_get_cash);
        goGetCashView.setOnClickListener(this);
        //点击预付款进入充值界面
        View goRechargeView = view.findViewById(R.id.me_pre_go_recharge);
        goRechargeView.setOnClickListener(this);
        //点击银行卡进入银行卡界面
        View goCardView = view.findViewById(R.id.me_go_card);
        goCardView.setOnClickListener(this);
        //点击现金账单进入现金账单界面
        View goCashAcView = view.findViewById(R.id.me_go_cash_account);
        goCashAcView.setOnClickListener(this);
        //点击代金券进入代金券界面
        View goTicketView = view.findViewById(R.id.me_go_ticket);
        goTicketView.setOnClickListener(this);
        //点击白积分进入白积分账单详情
        View whiteScoreView = view.findViewById(R.id.go_white_score_account);
        whiteScoreView.setOnClickListener(this);
        //点击红积分进入红积分界面
        View goRed = view.findViewById(R.id.me_go_red_score);
        goRed.setOnClickListener(this);
        //点击会员升级进入升级界面
        View vipView = view.findViewById(R.id.me_go_vip);
        vipView.setOnClickListener(this);
        //点击会员升级进入升级界面
        View goRecView = view.findViewById(R.id.me_go_recommend);
        goRecView.setOnClickListener(this);

        //点击商户中心进入商户中心界面
        View goCenterView = view.findViewById(R.id.me_go_business_center);
        goCenterView.setOnClickListener(this);
        //点击商户中心进入商户中心界面
        View goProxyCenterView = view.findViewById(R.id.me_go_proxy_center);
        goProxyCenterView.setOnClickListener(this);
        //点击汽车
        View carView = view.findViewById(R.id.qlqw_car);
        carView.setOnClickListener(this);
        //点击房产
        View houseView = view.findViewById(R.id.qlqw_house);
        houseView.setOnClickListener(this);

    }

    private void requestMyData(final int scene) {
        finalScene = scene;
        if (scene == 0) {
            Request<JSONObject> request = buildNetRequest(UrlUtils.user, 0, true);
            executeNetWork(request, "请稍后");
            setCallback(this);
        } else if (scene == 1) {
            Request<JSONObject> request = buildNetRequest(UrlUtils.getCompany, 0, true);
            executeNetWork(request, "请稍后");
            setCallback(this);
        } else if (scene == 2) {
            Request<JSONObject> request = buildNetRequest(UrlUtils.checkCardId, 0, true);
            executeNetWork(request, "请稍后");
            setCallback(this);
        } else if (scene == 3) {
            Request<JSONObject> request = buildNetRequest(UrlUtils.checkCardId, 0, true);
            executeNetWork(request, "请稍后");
            setCallback(this);
        }

    }

    private void initViews(View view) {
        dm=new DialogManager(getActivity());
        tvWhiteScore = ((TextView) view.findViewById(R.id.me_tv_white_score));
        tvNickName = ((TextView) view.findViewById(R.id.me_tv_nick_name));
        tvMoney = ((TextView) view.findViewById(R.id.me_tv_money));
        tvBanks = ((TextView) view.findViewById(R.id.me_tv_banks));
        tvTicket = ((TextView) view.findViewById(R.id.me_tv_ticket));
        tvRedScore = ((TextView) view.findViewById(R.id.me_tv_red_score));
        tvRecNum = ((TextView) view.findViewById(R.id.rec_num));
        tvBusiStatus = ((TextView) view.findViewById(R.id.tv_busi_status));
        initRefreshLayout(view);

    }

    private void initRefreshLayout(View view) {
        refreshLayout = ((SuperSwipeRefreshLayout) view.findViewById(R.id.four_superlayout));
        refreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                boolean netAva = NetUtils.isNetworkAvailable(getActivity());
                if(!netAva){
                    dm.buildAlertDialog("请打开网络！");
                    refreshLayout.setRefreshing(false);
                    return;
                }
                requestMyData(0);
            }

            @Override
            public void onPullDistance(int distance) {

            }

            @Override
            public void onPullEnable(boolean enable) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        String noOpen = "近期开放，工程师努力升级中";
        int id = v.getId();
        DialogManager dm = new DialogManager(getActivity());
        switch (id) {
            case R.id.me_user_img:
                Intent intentUser = new Intent(getActivity(), PersonalDataActivity.class);
                startActivity(intentUser);
                break;
            case R.id.go_white_score_account:
                Intent intent = new Intent(getActivity(), WhiteScoreActivity.class);
                startActivityForResult(intent,WhiteScoreActivity.REQUEST_EXCHANGE);
                break;
            case R.id.me_go_card:
                requestMyData(2);//检查是否实名
                break;
            case R.id.me_go_cash_account:
                Intent intentCash = new Intent(getActivity(), AllAccountActivity.class);
                startActivity(intentCash);
                break;
            case R.id.me_pre_go_recharge:
                Intent intent2 = new Intent(getActivity(), AccountRechargeActivity.class);
                startActivityForResult(intent2, REQUEST_PAY);
                break;
            case R.id.me_go_red_score:
                Intent intent3 = new Intent(getActivity(), RedScoreUpdateActivity.class);
                startActivityForResult(intent3, REQUEST_RED);
                break;
            case R.id.me_go_vip:
                Intent intent4 = new Intent(getActivity(), UserUpdateActivity.class);
                startActivity(intent4);
                break;
            case R.id.me_go_business_center:
                requestMyData(1);
                break;
            case R.id.tv_go_get_cash:
                requestMyData(3);//检查是否实名
                break;
            case R.id.me_go_recommend:
                Intent intent7 = new Intent(getActivity(), RecommActivity.class);
                startActivity(intent7);
                break;
            case R.id.me_go_ticket:
                Intent intent8 = new Intent(getActivity(), TicketActivity.class);
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
        if (data != null) {
            switch (requestCode) {
                case REQUEST_PAY:
                    if (Activity.RESULT_OK == resultCode) {
                        requestMyData(0);
                    }
                    break;
                case REQUEST_CLOSE:
                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    break;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void handle200Data(final JSONObject dataObj, String message) {
        if (finalScene == 0) {//我的数据
            refreshLayout.setRefreshing(false);
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
            int referee_bonuses = infoObj.optInt("referee_bonuses");//推荐人笔数
            int merchant = infoObj.optInt("merchant");
            if (merchant == 0) {
                tvBusiStatus.setText("尚未申请");
            } else if (merchant == 1) {
                tvBusiStatus.setText("已申请，审核中");
            } else if (merchant == 2) {
                tvBusiStatus.setText("未通过，重新申请");
            } else if (merchant == 3) {
                tvBusiStatus.setText("已通过审核");
            }
            Picasso.with(getActivity()).load(UrlUtils.baseWebsite + avatar).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE).error(R.mipmap.logo).noPlaceholder().into(goUserCenterView);
            //MemoryPolicy.NO_STORE图片出现问题，禁用掉
            JSONArray group_type = infoObj.optJSONArray("group_type");
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
                imgsContainer.addView(imageView);
            }
            tvWhiteScore.setText(white_score + "");
            tvRedScore.setText(red_score + "");
            tvNickName.setText(nickname);
            tvMoney.setText("账户余额：" + money);
            tvBanks.setText(banks + "张");
            tvTicket.setText(vouchers + "张");
            tvRecNum.setText(referee_bonuses + "笔/" + referee_num + "人");
        } else if (finalScene == 1) {//点击商户中心
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
                startActivityForResult(intent,REQUEST_CASH_GET);
            }
        }
    }

    @Override
    public void handleReqFailed() {
        if(refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
    }
}