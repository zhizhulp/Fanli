package com.ascba.rebate.view.keyboard;

/**
 * Created by 李平 on 2017/4/29 0029.9:38
 */

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.view.View;

import com.ascba.rebate.R;


/**
 * 数字软键盘工具类
 */
public class NumKeyboardUtil {
    private KeyboardView keyboardView;
    private Keyboard k;// 数字键盘
    private PasswordInputView ed;

    public PasswordInputView  getEd() {
        return ed;
    }

    public void setEd(PasswordInputView ed) {
        this.ed = ed;
    }

    public NumKeyboardUtil(KeyboardView keyboardView, Context ctx, PasswordInputView edit) {
        this.ed = edit;
        this.keyboardView=keyboardView;
        k = new Keyboard(ctx, R.xml.number);
        keyboardView.setKeyboard(k);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(listener);
    }

    private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }
        //一些特殊操作按键的codes是固定的比如完成、回退等
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = ed.getText();
            int start = ed.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            }else if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
                /*hideKeyboard();*/
            } else { //将要输入的数字现在编辑框中
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
    };

    public void showKeyboard() {
        keyboardView.setVisibility(View.VISIBLE);
    }

    public void hideKeyboard() {
        keyboardView.setVisibility(View.GONE);
    }

    public int getKeyboardVisible() {
        return keyboardView.getVisibility();
    }
}