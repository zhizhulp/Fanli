package com.ascba.rebate.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.inputmethodservice.KeyboardView;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.ascba.rebate.R;
import com.ascba.rebate.handlers.OnPasswordInput;
import com.ascba.rebate.view.keyboard.NumKeyboardUtil;
import com.ascba.rebate.view.keyboard.PasswordInputView;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by 李平 on 2017/5/9 0009.13:34
 * 密码框
 */

public class PsdDialog extends Dialog implements View.OnFocusChangeListener, View.OnTouchListener {
    private OnPasswordInput onPasswordInputFinish;
    private PasswordInputView edtPwd;
    private NumKeyboardUtil keyboardUtil;

    public void setOnPasswordInputFinish(OnPasswordInput onPasswordInputFinish) {
        this.onPasswordInputFinish = onPasswordInputFinish;
    }

    public PsdDialog(@NonNull Context context,int theme) {
        super(context,theme);
        init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.psd_lat);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        edtPwd = (PasswordInputView) findViewById(R.id.trader_pwd_set_pwd_edittext);
        edtPwd.setInputType(InputType.TYPE_NULL); // 屏蔽系统软键盘
        KeyboardView keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
        if (keyboardUtil == null)
            keyboardUtil = new NumKeyboardUtil(keyboardView, context, edtPwd);
        edtPwd.setOnTouchListener(this);
        keyboardUtil.getEd().setWatcher(new PasswordInputView.TextWatcher() {
            @Override
            public void complete(String number) {
                if (onPasswordInputFinish != null) {
                    //接口中要实现的方法，完成密码输入完成后的响应逻辑
                    onPasswordInputFinish.inputFinish(number);
                }
            }
        });
        edtPwd.setOnFocusChangeListener(this);
        //取消输入密码
        findViewById(R.id.im_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPasswordInputFinish != null) {
                    //接口中要实现的方法，完成密码输入完成后的响应逻辑
                    onPasswordInputFinish.inputCancel();
                }
            }
        });
        //点击忘记密码
        findViewById(R.id.tv_forget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPasswordInputFinish != null) {
                    //接口中要实现的方法，完成密码输入完成后的响应逻辑
                    onPasswordInputFinish.forgetPsd();
                }
            }
        });
        //去除Holo主题的蓝色线条
        try {
            int dividerID = context.getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = findViewById(dividerID);
            divider.setBackgroundColor(Color.TRANSPARENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMyDialog() {
        show();
        Window window = getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.goods_profile_anim);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams wlp = window.getAttributes();
            Display d = window.getWindowManager().getDefaultDisplay();
            wlp.width = d.getWidth();
            wlp.gravity = Gravity.BOTTOM;
            window.setAttributes(wlp);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            // 如果系统键盘是弹出状态，先隐藏
            ((InputMethodManager) (getContext()).getSystemService(INPUT_METHOD_SERVICE))
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
}
