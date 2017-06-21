package com.ascba.rebate.activities.scoring;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import static com.ascba.rebate.R.id;
import static com.ascba.rebate.R.layout;

public class KnowStrategyActivity extends BaseNetActivity {
    private LinearLayout llName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_know_strategy);
        initSystemBar();
        initView();
        setState(860);

    }

    private void initView() {
        llName = (LinearLayout) findViewById(id.ll_level_name);
    }

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        // 自定义颜色
        tintManager.setTintColor(Color.parseColor("#469BA2"));
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
//根据分数判断位置
private  void setState(int score){
    TextView tvName = (TextView) llName.getChildAt(getPosition(score));
    tvName.setBackgroundResource(R.mipmap.score_level_bg);
    tvName.setTextColor(Color.WHITE);


}


    private int getPosition(int score) {
        if (score < 0 || score > 1000) return 1;
        if (0 <= score && score < 300) {
            return 1;

        } else if (300 <= score && score < 500) {
            return 3;

        } else if (500 <= score && score < 750) {
            return 5;
        } else if (750 <= score && score < 800) {
            return 7;

        } else if (800 <= score && score < 1000) {
            return 9;

        }
        return 1;
    }




}
