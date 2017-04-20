package com.ascba.rebate.activities.me_page.settings.child.real_name_confirm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;

public class RealNameSuccessWorkActivity extends BaseNetWorkActivity {

    private TextView tvName;
    private TextView tvAge;
    private TextView tvSex;
    private TextView tvIdCa;
    private TextView tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name_success);
        //StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        if(intent!=null){
            String realname = intent.getStringExtra("realname");
            String cardid = intent.getStringExtra("cardid");
            int sex = intent.getIntExtra("sex", 2);
            int age = intent.getIntExtra("age",18);
            String location = intent.getStringExtra("location");
            tvName.setText("姓名："+realname);
            tvAge.setText("年龄："+age);
            if(sex==0){
                tvSex.setText("性别：女");
            }else if(sex==1){
                tvSex.setText("性别：男");
            }else if(sex==2){
                tvSex.setText("性别：保密");
            }
            tvIdCa.setText("身份证号："+cardid);
            tvAddress.setText("户口所在地："+location);
        }
    }

    private void initViews() {
        tvName = ((TextView) findViewById(R.id.user_name));
        tvAge = ((TextView) findViewById(R.id.user_age));
        tvSex = ((TextView) findViewById(R.id.user_sex));
        tvIdCa = ((TextView) findViewById(R.id.user_id_card));
        tvAddress = (TextView) findViewById(R.id.user_address);
    }
}
