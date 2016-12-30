package com.ascba.rebate.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.TimePicker;
import com.ascba.rebate.R;
import com.ascba.rebate.view.MoneyBar;
import com.jaeger.library.StatusBarUtil;

public class BusinessTimeActivity extends Activity {

    private TimePicker startTime;
    private TimePicker endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_time);
        initViews();
        StatusBarUtil.setColor(this, 0xffe52020);
    }


    private void initViews() {
        startTime = ((TimePicker) findViewById(R.id.timePicker1));
        startTime.setIs24HourView(true);
        endTime = ((TimePicker) findViewById(R.id.timePicker2));
        endTime.setIs24HourView(true);

        ((MoneyBar) findViewById(R.id.mb_time)).setCallBack(new MoneyBar.CallBack() {
            @Override
            public void clickImage(View im) {

            }

            @Override
            public void clickComplete(View tv) {
                Integer startHour = startTime.getCurrentHour();
                Integer startMinute = startTime.getCurrentMinute();
                Integer endHour = endTime.getCurrentHour();
                Integer endMinute = endTime.getCurrentMinute();
                Intent intent=getIntent();
                intent.putExtra("business_data_time",startHour+":"+startMinute+"~"+endHour+":"+endMinute);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }


}
