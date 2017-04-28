package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.PayViewAdp;
import com.ascba.rebate.handlers.OnPasswordInput;
import com.ascba.rebate.view.ShopABarText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 李鹏 on 2017/04/28 0028.
 * 再次输入支付密码
 */

public class SettingPayPsdAgainActivity extends BaseNetActivity {

    private Context context;
    private ShopABarText shopABar;
    private String strPassword;     //输入的密码
    private String psd;//第一次密码
    private TextView tvForget;//忘记密码
    private TextView[] tvList;      //用数组保存6个TextView
    private GridView gridView;    //用GrideView布局键盘，其实并不是真正的键盘，只是模拟键盘的功能
    private ArrayList<Map<String, String>> valueList;
    private int currentIndex = -1;    //用于记录当前输入密码格位置
    private PayViewAdp adapter;
    private OnPasswordInput onPasswordInput;

    public void setOnPasswordInput(OnPasswordInput onPasswordInput) {
        this.onPasswordInput = onPasswordInput;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pay_psd);
        context = this;
        initUI();
        getPsd();
    }

    public static void startIndent(Context context, String psd) {
        Intent intent = new Intent(context, SettingPayPsdAgainActivity.class);
        intent.putExtra("psd", psd);
        context.startActivity(intent);
    }

    private void getPsd() {
        Intent intent = getIntent();
        psd = intent.getStringExtra("psd");
    }

    private void initUI() {
        shopABar = (ShopABarText) findViewById(R.id.shopBar);
        shopABar.setBtnEnable(true);
        shopABar.setTitle("再次输入密码");
        shopABar.setCallback(new ShopABarText.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkBtn(View v) {

            }
        });

        valueList = new ArrayList<Map<String, String>>();
        tvList = new TextView[6];
        setView();
        tvForget = (TextView) findViewById(R.id.tv_forgetPwd);

        tvList[0] = (TextView) findViewById(R.id.tv_pass1);
        tvList[1] = (TextView) findViewById(R.id.tv_pass2);
        tvList[2] = (TextView) findViewById(R.id.tv_pass3);
        tvList[3] = (TextView) findViewById(R.id.tv_pass4);
        tvList[4] = (TextView) findViewById(R.id.tv_pass5);
        tvList[5] = (TextView) findViewById(R.id.tv_pass6);

        gridView = (GridView) findViewById(R.id.gv_keybord);
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

        //忘记密码
        tvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setOnFinishInput();
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
                    strPassword = "";   //每次触发都要先将strPassword置空，再重新获取，避免由于输入删除再输入造成混乱
                    for (int i = 0; i < 6; i++) {
                        strPassword += tvList[i].getText().toString().trim();
                    }
                    if (psd.equals(strPassword)) {
                        showToast("密码设置成功");
                        finish();
                        SettingPayPsdActivity activity=new SettingPayPsdActivity();
                        activity.finish();
                    } else {
                        showToast("密码输入不一致");
                        clear();
                    }
                }
            }
        });
    }

    /* 初始化按钮上应该显示的数字 */
    private void setView() {
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

    /*
       清空密码
     */
    private void clear() {
        currentIndex = -1;
        strPassword = "";
        for (TextView textView : tvList) {
            textView.setText("");
        }
    }
}
