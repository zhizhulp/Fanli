package com.ascba.rebate.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.PayViewAdp;
import com.ascba.rebate.handlers.OnPasswordInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 密码输入框
 */
@SuppressLint("CommitPrefEdits")
public class PayPopWindow implements OnDismissListener, OnClickListener {
    private PopupWindow popupWindow;
    private Context context;
    private String strPassword;     //输入的密码
    private TextView[] tvList;      //用数组保存6个TextView，为什么用数组？
    //因为就6个输入框不会变了，用数组内存申请固定空间，比List省空间（自己认为）
    private GridView gridView;    //用GrideView布局键盘，其实并不是真正的键盘，只是模拟键盘的功能
    private ArrayList<Map<String, String>> valueList;    //有人可能有疑问，为何这里不用数组了？
    //因为要用Adapter中适配，用数组不能往adapter中填充

    private View imgCancel;
    private TextView tvForget;
    private int currentIndex = -1;    //用于记录当前输入密码格位置

    private LinearLayout line;
    private ImageView pic;
    private PayViewAdp adapter;
    private OnPasswordInput onPasswordInputFinish;

    public void setOnPasswordInputFinish(OnPasswordInput onPasswordInputFinish) {
        this.onPasswordInputFinish = onPasswordInputFinish;
    }

    public PayPopWindow(final Context context, View backgroundView) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_popup_bottom, null);
        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        //设置popwindow的动画效果
        popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
        popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        valueList = new ArrayList<>();
        tvList = new TextView[6];
        setView();

        imgCancel = view.findViewById(R.id.img_cance);
        tvForget = (TextView) view.findViewById(R.id.tv_forgetPwd);

        line = (LinearLayout) view.findViewById(R.id.pay_lin);
        pic = (ImageView) view.findViewById(R.id.pay_status);

        tvList[0] = (TextView) view.findViewById(R.id.tv_pass1);
        tvList[1] = (TextView) view.findViewById(R.id.tv_pass2);
        tvList[2] = (TextView) view.findViewById(R.id.tv_pass3);
        tvList[3] = (TextView) view.findViewById(R.id.tv_pass4);
        tvList[4] = (TextView) view.findViewById(R.id.tv_pass5);
        tvList[5] = (TextView) view.findViewById(R.id.tv_pass6);

        gridView = (GridView) view.findViewById(R.id.gv_keybord);
        adapter = new PayViewAdp(context, valueList);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 11 && position != 9) {    //点击0~9按钮
                    if (currentIndex >= -1 && currentIndex < 5) {      //判断输入位置————要小心数组越界
                        tvList[++currentIndex].setText(valueList.get(position).get("name"));
                    }
                } else {
                    if (position == 11) {      //点击退格键
                        if (currentIndex - 1 >= -1) {      //判断是否删除完毕————要小心数组越界
                            tvList[currentIndex--].setText("");
                        }
                    }
                }
            }
        });
        imgCancel.setOnClickListener(this);
        tvForget.setOnClickListener(this);
        setOnFinishInput();
    }

    //当popWindow消失时响应
    @Override
    public void onDismiss() {
        Window window = ((Activity) context).getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = 1.0f;
        window.setAttributes(params);
        popupWindow.dismiss();
    }


    /**
     * 弹窗显示的位置
     */
    public void showAsDropDown(View position) {
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(position, Gravity.BOTTOM, 0, 0);
        //设置背景变暗
        Window window = ((Activity) context).getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = 0.5f;
        window.setAttributes(params);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cance:
                if (onPasswordInputFinish != null) {
                    onPasswordInputFinish.inputCancel();
                }
                break;
            case R.id.tv_forgetPwd:
                Toast.makeText(context, "前往忘记密码界面", Toast.LENGTH_SHORT).show();
                if (onPasswordInputFinish != null) {
                    onPasswordInputFinish.forgetPsd();
                }
                break;
        }
    }

    //设置监听方法，在第6位输入完成后触发
    public void setOnFinishInput() {
        tvList[5].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1) {
                    strPassword = "";     //每次触发都要先将strPassword置空，再重新获取，避免由于输入删除再输入造成混乱
                    for (int i = 0; i < 6; i++) {
                        strPassword += tvList[i].getText().toString().trim();
                    }
                    if (onPasswordInputFinish != null) {
                        //接口中要实现的方法，完成密码输入完成后的响应逻辑
                        onPasswordInputFinish.inputFinish();
                    }

                }
            }
        });
    }

    /* 获取输入的密码 */
    public String getStrPassword() {
        return strPassword;
    }


    private void setView() {
        /* 初始化按钮上应该显示的数字 */
        for (int i = 1; i < 13; i++) {
            Map<String, String> map = new HashMap<String, String>();
            if (i < 10) {
                map.put("name", String.valueOf(i));
            } else if (i == 10) {
                map.put("name", "");
            } else if (i == 11) {
                map.put("name", String.valueOf(0));
            } else if (i == 12) {
                map.put("name", "<");
            }
            valueList.add(map);
        }
    }
}
