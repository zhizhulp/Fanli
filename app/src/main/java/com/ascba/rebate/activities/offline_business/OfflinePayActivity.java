package com.ascba.rebate.activities.offline_business;

import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.RoundImageView;
import com.ta.utdid2.android.utils.StringUtils;

/**
 * 扫一扫-付款
 */
public class OfflinePayActivity extends BaseNetActivity implements View.OnClickListener,TextWatcher {

    private RoundImageView busiIcon;
    private TextView tvBusiName;
    private EditTextWithCustomHint etMoney;
    private TextView tvPayType;

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
        tvPayType = ((TextView) findViewById(R.id.tv_pay_type));

        setSuperString("账户余额");
        tvPayType.setOnClickListener(this);
        etMoney.addTextChangedListener(this);
    }

    //点击去付款
    public void goPay(View view) {

    }
    //点击更换支付方式
    @Override
    public void onClick(View v) {
        showToast("点我，小样儿");
    }
    //设置切换后的文字
    private void setSuperString(String payType){
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("使用");
        spannableString.append(payType);
        spannableString.append("，");
        spannableString.append("更换");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.main_text_blue));
        spannableString.setSpan(colorSpan, spannableString.length()-2, spannableString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tvPayType.setText(spannableString);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d(TAG, "beforeTextChanged: ");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d(TAG, "onTextChanged: ");
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d(TAG, "afterTextChanged: ");
        s.toString().trim()
    }
}
