package com.ascba.rebate.activities.main_page.sweep;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.ascba.rebate.R;
import com.jaeger.library.StatusBarUtil;

public class TradeResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_result);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.moneyBarColor));
    }

    public void goComplete(View view) {
        finish();
    }
}
