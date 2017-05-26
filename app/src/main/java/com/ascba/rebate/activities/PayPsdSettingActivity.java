package com.ascba.rebate.activities;

import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.utils.DialogHome;
import com.ascba.rebate.utils.PsdUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MoneyBar;
import com.ascba.rebate.view.keyboard.NumKeyboardUtil;
import com.ascba.rebate.view.keyboard.PasswordInputView;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

/**
 *
 */
public class PayPsdSettingActivity extends BaseNetActivity implements View.OnFocusChangeListener
        ,View.OnTouchListener {
    private static final int REQUEST_FORGET = 1;
    private static final int REQUEST_PAY = 2;
    private PasswordInputView edtPwd;
    private NumKeyboardUtil keyboardUtil;
    private String firstNum;//第一次输入的密码
    private String secNum;//第二次输入的密码
    private String oriNum;//原先的密码
    private int type;//0  设置密码1 修改密码
    private int scene;//0 确认密码 1 验证密码
    private TextView tvDesc;
    private MoneyBar mb;
    private TextView tvForget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_psd_setting);
        initView();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            //type 0 从设置进来的 1 从支付时候进来的
            int type = intent.getIntExtra("type",0);
            if(type==1){
                AppConfig.getInstance().putInt("is_level_pwd",0);
                startActivityForResult(new Intent(this,FindPayPasswordActivity.class),REQUEST_PAY);
            }
        }
    }

    private void initMoneyBar() {
        mb = ((MoneyBar) findViewById(R.id.mb));
        if(type==0){
            mb.setTextTitle("设置支付密码");
        }else if(type==1) {
            mb.setTextTitle("修改支付密码");
        }

    }

    private void initView() {
        type=AppConfig.getInstance().getInt("is_level_pwd",0);
        tvForget = ((TextView) findViewById(R.id.tv_forget));
        if(type==1){
            scene=1;
            tvForget.setVisibility(View.VISIBLE);
        }else {
            tvForget.setVisibility(View.GONE);
        }
        edtPwd = (PasswordInputView) findViewById(R.id.trader_pwd_set_pwd_edittext);
        edtPwd.setInputType(InputType.TYPE_NULL); // 屏蔽系统软键盘
        KeyboardView keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
        if (keyboardUtil == null) keyboardUtil = new NumKeyboardUtil(keyboardView, this, edtPwd);
        edtPwd.setOnTouchListener(this);
        keyboardUtil.getEd().setWatcher(new PasswordInputView.TextWatcher() {
            @Override
            public void complete(String number) {
                etidComplete(number);
            }
        });
        edtPwd.setOnFocusChangeListener(this);

        tvDesc = (TextView) findViewById(R.id.trader_pwd_set_tips_textview);
        if(scene==0){
            tvDesc.setText("请设置6位支付密码");
        }else if(scene==1){
            tvDesc.setText("确认当前支付密码");
        }
        initMoneyBar();


    }

    private void requestNetwork(String url,String number){
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        request.add("level_pwd", PsdUtils.encryptPsd(number));
        request.add("relevel_pwd",scene);
        executeNetWork(scene,request,"请稍后");
    }


    private void etidComplete(String number) {
        if(type==0){//设置支付密码
            scene=0;
            if(firstNum==null){
                firstNum=number;
                tvDesc.setText("请再次设置6位支付密码");
            }else {
                if(secNum==null){
                    secNum=number;
                    if(isSame()){
                        requestNetwork(UrlUtils.setPayPassword,secNum);
                    }else {
                        showToast("2次密码不一致,请重新输入");
                        resetData();
                    }

                }
            }
        }else if(type==1){//修改支付密码
            if(oriNum==null){
                scene=1;
                oriNum=number;
                tvDesc.setText("请设置6位新支付密码");
                requestNetwork(UrlUtils.setPayPassword,oriNum);
            }else {
                scene=0;
                if(firstNum==null){
                    firstNum=number;
                    tvDesc.setText("请再次设置6位新支付密码");
                }else {
                    if(secNum==null){
                        secNum=number;
                        if(isSame()){
                            requestNetwork(UrlUtils.setPayPassword,secNum);
                        }else {
                            showToast("2次密码不一致,请重新输入");
                            resetData();
                        }
                    }
                }
            }
        }
    }

    //重置数据
    private void resetData(){
        if(type==0){
            tvDesc.setText("请设置6位支付密码");
        }else if(type==1){
            tvDesc.setText("确认当前支付密码");
        }
        firstNum=null;
        secNum=null;
        oriNum=null;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            // 如果系统键盘是弹出状态，先隐藏
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            keyboardUtil.showKeyboard();
        } else {
            keyboardUtil.hideKeyboard();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        keyboardUtil.showKeyboard();
        return false;
    }
    //判断2次密码是否相同
    private boolean isSame(){
        if(firstNum==null || secNum==null) {
            return false;
        }
        return firstNum.equals(secNum);
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        super.mhandle200Data(what, object, dataObj, message);
        if(what==0){//修改成功
            getDm().buildAlertDialog2(message, new DialogHome.Callback() {
                @Override
                public void handleSure() {
                    AppConfig.getInstance().putInt("is_level_pwd",1);
                    setResult(RESULT_OK,getIntent());
                    finish();
                }
            });
        }else if(what==1){//验证成功
            showToast(message);
        }

    }

    @Override
    protected void mhandle404(int what, JSONObject object, String message) {
        super.mhandle404(what, object, message);
        resetData();
    }

    @Override
    protected void mhandleFailed(int what, Exception e) {
        super.mhandleFailed(what, e);
        resetData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_FORGET:
                if(resultCode==RESULT_OK){
                    type=0;
                    resetData();
                }
                break;
            case REQUEST_PAY:
                if(resultCode==RESULT_OK){
                    type=0;
                    resetData();
                }else {
                    finish();
                }
                break;
        }
    }

    //点击忘记密码
    public void forgetPassword(View view) {
        Intent intent=new Intent(this,FindPayPasswordActivity.class);
        startActivityForResult(intent,REQUEST_FORGET);
    }
}
