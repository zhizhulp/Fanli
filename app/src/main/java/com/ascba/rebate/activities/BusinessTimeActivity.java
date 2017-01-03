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
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            String seller_business_hours = intent.getStringExtra("seller_business_hours");
            if(seller_business_hours!=null&& !"".equals(seller_business_hours)){
                String[] split = seller_business_hours.split("~");
                String start = split[0];
                String end = split[1];
                String[] splitStart = start.split(":");
                String startHour= splitStart[0];
                String startMin= splitStart[1];
                String[] splitEnd = end.split(":");
                String endHour= splitEnd[0];
                String endMin= splitEnd[1];
                startTime.setCurrentHour(Integer.valueOf(startHour));
                startTime.setCurrentMinute(Integer.valueOf(startMin));
                endTime.setCurrentHour(Integer.valueOf(endHour));
                endTime.setCurrentMinute(Integer.valueOf(endMin));
            }
        }
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
                StringBuilder sb=new StringBuilder();
                int startHour = startTime.getCurrentHour();
                if(needExchange(startHour)){
                    sb.append(0);
                }
                sb.append(startHour);
                sb.append(":");
                int startMinute = startTime.getCurrentMinute();
                if(needExchange(startMinute)){
                    sb.append(0);
                }
                sb.append(startMinute);
                sb.append("~");
                int endHour = endTime.getCurrentHour();
                if(needExchange(endHour)){
                    sb.append(0);
                }
                sb.append(endHour);
                sb.append(":");
                int endMinute = endTime.getCurrentMinute();
                if(needExchange(endMinute)){
                    sb.append(0);
                }
                sb.append(endMinute);
                Intent intent=getIntent();
                intent.putExtra("business_data_time",sb.toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }
    //判断时间是否是一下几个
    //00 01 02 03 04 05 06 07 08 09实际显示为0 1 2 3 4 5 6 7 8 9
    private boolean needExchange(int time){
        if(time==0||time==1||time==2||time==3||time==4||time==5||time==6||time==7||time==8||time==9){
           return true;
        }
        return false;
    }

}
