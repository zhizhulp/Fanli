package com.ascba.rebate.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.MoneyBar;

public class BusinessTimeActivity extends Activity {

    private TimePicker startTime;
    private TimePicker endTime;
    private TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_time);
        initViews();
    }

    private void initViews() {
        startTime = ((TimePicker) findViewById(R.id.timePicker1));
        startTime.setIs24HourView(true);
        endTime = ((TimePicker) findViewById(R.id.timePicker2));
        endTime.setIs24HourView(true);
        tvTime = ((TextView) findViewById(R.id.tv_time));

        ((MoneyBar) findViewById(R.id.mb_time)).setCallBack(new MoneyBar.CallBack() {
            @Override
            public void clickImage(View im) {

            }

            @Override
            public void clickComplete(View tv) {
                String s = tvTime.getText().toString();
                Intent intent=getIntent();
                intent.putExtra("business_data_time",s);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }
    //时间设置完成
    public void timeComplete(View view) {
        Integer startHour = startTime.getCurrentHour();
        Integer startMinute = startTime.getCurrentMinute();
        Integer endHour = endTime.getCurrentHour();
        Integer endMinute = endTime.getCurrentMinute();
        tvTime.setText(startHour+":"+startMinute+"~"+endHour+":"+endMinute);
    }

}
