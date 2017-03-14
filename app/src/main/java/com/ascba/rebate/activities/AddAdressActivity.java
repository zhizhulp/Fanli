package com.ascba.rebate.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.view.ShopABarText;

/**
 * Created by 李鹏 on 2017/03/14 0014.
 * 新增联系人
 */

public class AddAdressActivity extends BaseNetWork4Activity {

    private ShopABarText bar;
    private RelativeLayout btn_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        initView();
    }

    private void initView() {
        bar = (ShopABarText) findViewById(R.id.add_address_bar);
        bar.setBtnText("保存");
        bar.setCallback(new ShopABarText.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkBtn(View v) {
                //保存
            }
        });

        //选择联系人
        btn_contact= (RelativeLayout) findViewById(R.id.activity_add_rl_contact);
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
