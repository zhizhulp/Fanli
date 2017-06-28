package com.ascba.rebate.activities.offline_business;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.PayPsdSettingActivity;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.PayTypeAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.beans.PayType;
import com.ascba.rebate.handlers.OnPasswordInput;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.PsdDialog;
import com.ascba.rebate.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 扫一扫-付款
 */
public class OfflinePayActivity extends BaseNetActivity implements View.OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener {

    private RoundImageView busiIcon;
    private TextView tvBusiName;
    private EditTextWithCustomHint etMoney;
    private Button btnPay;
    private PsdDialog psdDialog;
    private BottomSheetDialog payTypeDialog;
    private String payType = "balance";//默认支付方式
    private CheckBox checkBoxRemainder, checkBoxCash;
    private boolean isChecked1 = true;
    private boolean isChecked2 = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_pay);

        initViews();
    }

    private void initViews() {
        busiIcon = ((RoundImageView) findViewById(R.id.im_busi_icon));
        tvBusiName = ((TextView) findViewById(R.id.tv_busi_name));
        etMoney = ((EditTextWithCustomHint) findViewById(R.id.et_busi_money));
        btnPay = ((Button) findViewById(R.id.btn_pay));
        etMoney.addTextChangedListener(this);
        setBtnStatus(R.drawable.ticket_no_shop_bg, false);
        setSuperString("账户余额");
        checkBoxRemainder.setOnCheckedChangeListener(this);


    }

    //点击去付款
    public void goPay(View view) {
        showPsdDialog();
    }

    //点击更换支付方式
    @Override
    public void onClick(View v) {
        showPayTypeDialog();
    }

    //支付方式dialog
    private void showPayTypeDialog() {
        payTypeDialog = new BottomSheetDialog(this, R.style.AlertDialog);
        payTypeDialog.setContentView(R.layout.layout_pay_pop_offline);
        //列表
        RecyclerView rvTypes = (RecyclerView) payTypeDialog.findViewById(R.id.pay_type_list);
        List<PayType> types = new ArrayList<>();
        initPayTypesData(types);
        PayTypeAdapter pt = new PayTypeAdapter(R.layout.pay_type_item, types);
        pt.setCallback(new PayTypeAdapter.Callback() {
            @Override
            public void onClicked(String payType) {
                payTypeDialog.dismiss();
                OfflinePayActivity.this.payType = payType;
                if ("balance".equals(payType)) {
                    setSuperString("账户余额支付方式");
                } else if ("cash".equals(payType)) {
                    setSuperString("现金支付方式");
                }
            }
        });
        rvTypes.setLayoutManager(new LinearLayoutManager(this));
        rvTypes.setAdapter(pt);
        //显示对话框
        payTypeDialog.show();
    }

    private void initPayTypesData(List<PayType> types) {
        types.add(new PayType(true, R.mipmap.pay_left, "账户余额支付方式", "快捷支付 账户余额￥" + 28, "balance"));
        types.add(new PayType(false, R.mipmap.pay_cash, "现金支付方式", "通过app使用现金支付可以返积分喔！", "cash"));
        for (int i = 0; i < types.size(); i++) {
            PayType payType = types.get(i);
            String type = payType.getType();
            if (this.payType.equals(type)) {
                payType.setSelect(true);
            } else {
                payType.setSelect(false);
            }
        }
    }

    //设置切换后的文字
    private void setSuperString(String payType) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("使用");
        spannableString.append(payType);
        spannableString.append("，");
        spannableString.append("更换");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.main_text_blue));
        spannableString.setSpan(colorSpan, spannableString.length() - 2, spannableString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    }

    @Override
    public void afterTextChanged(Editable s) {
        String finalString = s.toString();
        if ("".equals(finalString)) {
            setBtnStatus(R.drawable.ticket_no_shop_bg, false);
            return;
        }
        double v = Double.parseDouble(finalString);
        if (v == 0) {
            setBtnStatus(R.drawable.ticket_no_shop_bg, false);
        } else {
            setBtnStatus(R.drawable.register_btn_bg, true);
        }
    }

    //设置button状态
    private void setBtnStatus(int id, boolean enable) {
        btnPay.setBackgroundDrawable(getResources().getDrawable(id));
        btnPay.setEnabled(enable);
    }

    //显示密码框
    private void showPsdDialog() {
        psdDialog = new PsdDialog(this, R.style.AlertDialog);
        psdDialog.setOnPasswordInputFinish(new OnPasswordInput() {
            @Override
            public void inputFinish(String number) {
                psdDialog.dismiss();
            }

            @Override
            public void inputCancel() {
                psdDialog.dismiss();
                showToast("支付取消");
            }

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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.account_remain_check:
                if (isChecked1) {
                    isChecked2 = false;

                } else {

                }

                break;
            case R.id.keep_accounts_check://记账方式
                if (isChecked2) {
                    isChecked1 = false;
                } else {

                }
                break;
        }
        checkBoxRemainder.setChecked(isChecked1);
        checkBoxCash.setChecked(isChecked2);

    }
}
