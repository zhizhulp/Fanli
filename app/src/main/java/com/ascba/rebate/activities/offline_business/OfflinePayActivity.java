package com.ascba.rebate.activities.offline_business;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.PayPsdSettingActivity;
import com.ascba.rebate.activities.base.BaseNetActivity1;
import com.ascba.rebate.activities.me_page.AccountRechargeActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.beans.sweep.KeepAccountSubmitEntity;
import com.ascba.rebate.beans.sweep.SubmitEntity;
import com.ascba.rebate.handlers.OnPasswordInput;
import com.ascba.rebate.utils.DialogHome;
import com.ascba.rebate.utils.NumberFormatUtils;
import com.ascba.rebate.utils.PsdUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.PsdDialog;
import com.ascba.rebate.view.RoundImageView;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

/**
 * 扫一扫-付款
 */
public class OfflinePayActivity extends BaseNetActivity1 implements View.OnClickListener, TextWatcher {

    private RoundImageView busiIcon;
    private TextView tvBusiName, sweepRemainder;
    private EditTextWithCustomHint etMoney;
    private TextView offline_tv_pay;
    private PsdDialog psdDialog;
    private BottomSheetDialog payTypeDialog;
    private boolean isReminderPay = true;
    private RadioButton rbReminder, rbOther;
    private int payType = 2;
    private double self_money;
    private double importMoney;
    private int seller;
    private String password;
    //付款成功界面所需要的属性
    private String seller_logo, seller_name, seller_cover_logo;
    public static final int RESULT_CODE = 1;
    Intent intent1;
    private String preChangeTxt = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_pay);
        initViews();

    }


    private void initViews() {
        busiIcon = ((RoundImageView) findViewById(R.id.im_busi_icon));
        tvBusiName = ((TextView) findViewById(R.id.tv_busi_name));
        sweepRemainder = (TextView) findViewById(R.id.sweep_remainder);
        etMoney = ((EditTextWithCustomHint) findViewById(R.id.et_busi_money));
        offline_tv_pay = (TextView) findViewById(R.id.offline_tv_pay);
        etMoney.addTextChangedListener(this);
        setBtnStatus(R.color.submit_gray, false);
        rbReminder = (RadioButton) findViewById(R.id.rb_offline_reminder);
        rbOther = (RadioButton) findViewById(R.id.rb_offline_other);
        rbReminder.setOnClickListener(this);
        rbOther.setOnClickListener(this);
        rbReminder.setChecked(true);
        offline_tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPay();

            }
        });

        Intent intent = getIntent();
        int type=intent.getIntExtra("type",0);

        if(type==0){//从captureactivity的页面
           seller_cover_logo = intent.getStringExtra("seller_cover_logo");
            seller_name = intent.getStringExtra("seller_name");
            seller = intent.getIntExtra("seller", 0);
            self_money = Double.parseDouble(intent.getStringExtra("self_money"));//余额
            seller_logo = UrlUtils.baseWebsite + seller_cover_logo;
            Picasso.with(this).load(seller_logo).into(busiIcon);
            tvBusiName.setText("向 " + seller_name + " 付款");
            sweepRemainder.setText("可用余额" + NumberFormatUtils.getNewDouble(self_money)+ "元");
        }else if(type==1){

        }

    }

    //点击更换支付方式
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rb_offline_reminder) {//余额支付
            isReminderPay = true;
            rbReminder.setChecked(true);
            rbOther.setChecked(false);
            payType = 2;
        } else if (v.getId() == R.id.rb_offline_other) {//其他支付方式
            isReminderPay = false;
            rbReminder.setChecked(false);
            rbOther.setChecked(true);
            payType = 1;
        }
    }


    @Override
    public void afterTextChanged(Editable s) {
        String finalString = s.toString();
        if ("".equals(finalString) || finalString.substring(0,1).equals(".")) {//未输入金额的情况
            setBtnStatus(R.color.submit_gray, false);
            return;
        }
        double v = Double.parseDouble(finalString);
        if (v == 0) {//输入金额为0的情况
            setBtnStatus(R.color.submit_gray, false);
        } else {
            setBtnStatus(R.color.main_red_normal, true);
        }
    }

    //设置button状态
    private void setBtnStatus(int id, boolean enable) {
        offline_tv_pay.setBackgroundColor(getResources().getColor(id));
        offline_tv_pay.setEnabled(enable);
    }


    //点击去付款
    private void goPay() {
        if (payType == 2) {//余额支付的方式
            if(AppConfig.getInstance().getInt("is_level_pwd", 0) == 0){

                getDm().buildAlertDialogSure("请设置支付密码！", "设置", "取消", new DialogHome.Callback() {
                    @Override
                    public void handleSure() {//取消
                    }
                    @Override
                    public void handleCancel() {
                        Intent intent = new Intent(OfflinePayActivity.this, PayPsdSettingActivity.class);
                        startActivity(intent);
                    }
                });

            }else{
                showPsdDialog();
            }

        } else if (payType == 1) {//记账的方式支付
            requestKeepAccounts(UrlUtils.submit, 0);

        }

    }

    //余额支付请求方式
    public void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        request.add("seller", seller);
        request.add("money", importMoney);
        request.add("pay_type", payType);
        request.add("pay_password", password);
        request.add("scenetype", 2);
        executeNetWork(what, request, "请稍后");
    }

    //记账的方式支付方式
    public void requestKeepAccounts(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        importMoney = Double.parseDouble(etMoney.getText().toString());//输入的金额
        request.add("seller", seller);
        request.add("money", importMoney);
        request.add("pay_type", payType);
        request.add("scenetype", 2);
        executeNetWork(what, request, "请稍后");
    }

    @Override
    protected void mhandle404(int what, JSONObject dataObj, String message) {
        // error_status:1余额不足（消费者），2密码错误（消费者），3未设置支付密码（消费者），4商家余额不足

        int error_status = dataObj.optInt("error_status");
        if (error_status == 1) {//
            getDm().buildAlertDialogSure("账户余额不足，请先充值，再进行支付！", "重新选择", "立即充值", new DialogHome.Callback() {
                @Override
                public void handleSure() {
                    startActivity(new Intent(OfflinePayActivity.this, AccountRechargeActivity.class));
                }
            });
        } else if (error_status == 2) {//密码错误
            getDm().buildAlertDialogSure(message, "重新输入", "忘记密码", new DialogHome.Callback() {
                @Override
                public void handleSure() {
                    Intent intent = new Intent(OfflinePayActivity.this, PayPsdSettingActivity.class);
                    intent.putExtra("type",1);
                    startActivity(intent);
                }
                @Override
                public void handleCancel() {
                    super.handleCancel();
                    showPsdDialog();

                }
            });

        } /*else if (error_status == 3) {//未设置密码

        }*/ else if (error_status == 4) {
            showToast(message);
        }

    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        super.mhandle200Data(what, object, dataObj, message);
        if (payType == 2) {//余额支付
            if (importMoney < self_money) {//余额足的情况下
                SubmitEntity submitEntity = JSON.parseObject(dataObj.toString(), SubmitEntity.class);
                SubmitEntity.InfoBean info = submitEntity.getInfo();

                int accumulate_points = info.getScore();
                String seller_mobile = info.getMember_username();
                String order_number = info.getOrder_number();
                String pay_type_text = info.getPay_type_text();
                Intent intent = new Intent(OfflinePayActivity.this, OfflinePaySuccedActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("pay_type", info.getPay_type());

                bundle.putString("importMoney", info.getMoney());
                bundle.putString("seller_cover_logo", seller_cover_logo);
                bundle.putString("seller_name", seller_name);
                bundle.putString("order_status_text", info.getOrder_status_text());
                bundle.putString("pay_type_text", pay_type_text);

                bundle.putString("member_username", seller_mobile);
                bundle.putString("accumulate_points", accumulate_points + "");
                bundle.putLong("create_time", info.getCreate_time());
                bundle.putString("order_number", order_number);

                intent.putExtras(bundle);
                startActivityForResult(intent, RESULT_CODE);
                finish();

            } //余额不足在404处理


        } else {//记账的方式支付
            KeepAccountSubmitEntity keepAccountSubmitEntity = JSON.parseObject(dataObj.toString(), KeepAccountSubmitEntity.class);
            KeepAccountSubmitEntity.InfoBean infoBean = keepAccountSubmitEntity.getInfo();
            Intent intent = new Intent(this, OfflinePaySuccedActivity.class);

            Bundle bundle = new Bundle();//有11项
            bundle.putInt("pay_type", infoBean.getPay_type());
            bundle.putString("importMoney", infoBean.getMoney() + "");
            bundle.putString("seller_cover_logo", seller_cover_logo);
            bundle.putString("seller_name", infoBean.getName());
            bundle.putString("order_status_text", infoBean.getOrder_status_text());
            bundle.putString("pay_type_text", infoBean.getPay_type_text());
            bundle.putString("member_username", infoBean.getMember_username());
            bundle.putString("accumulate_points", infoBean.getScore() + "");
            bundle.putLong("create_time", infoBean.getCreate_time());
            bundle.putString("order_number", infoBean.getOrder_number());
            bundle.putString("seller_contact", infoBean.getSeller_contact());

            intent.putExtras(bundle);
            startActivityForResult(intent, RESULT_CODE);
            finish();
            // showToast("待商家确认，请稍后");
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_CODE:
                if (resultCode == 3) {
                    setResult(100);
                    finish();
                }
                break;


        }

    }

    //显示密码框
    private void showPsdDialog() {
        psdDialog = new PsdDialog(this, R.style.AlertDialog);
        psdDialog.setOnPasswordInputFinish(new OnPasswordInput() {
            @Override
            public void inputFinish(String number) {
                password = PsdUtils.getPayPsd(number);
                judgeRemainder();
                psdDialog.dismiss();
            }

            //支付取消
            @Override
            public void inputCancel() {
                psdDialog.dismiss();
                showToast("支付取消");
            }

            //忘记密码
            @Override
            public void forgetPsd() {
                AppConfig.getInstance().putInt("is_level_pwd", 0);
                Intent intent = new Intent(OfflinePayActivity.this, PayPsdSettingActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });
        psdDialog.showMyDialog();
    }

    //判断是否余额充足
    private void judgeRemainder() {           //凡茜
        if (isReminderPay) {//如果余额支付
            importMoney = Double.parseDouble(etMoney.getText().toString());//输入的金额
            requestNetwork(UrlUtils.submit, 0);

        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //只要前面的三位数
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(".") + 3);
                etMoney.setText(s);
                etMoney.setSelection(s.length());
            }
        }
        //开够输入.自动变为0.x的类型
        if (s.toString().trim().substring(0).equals(".")) {
            if(preChangeTxt.toString().trim().length() == 0){
                s = "0" + s;
                etMoney.setText(s);
                etMoney.setSelection(2);
            }
        }

        if(s.toString().trim().substring(0).equals("0")){
            if(preChangeTxt.toString().trim().length() == 0){
                s = s + ".";
                etMoney.setText(s);
                etMoney.setSelection(2);
            }
        }

        if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                etMoney.setText(s.subSequence(0, 1));
                etMoney.setSelection(1);
                return;
            }
        }
        preChangeTxt = s.toString().trim();
    }

}