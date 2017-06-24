package com.ascba.rebate.activities.scoring;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.scoring.widget.KnowScoringDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import static com.ascba.rebate.R.id;

public class KnowStrategyActivity extends BaseNetActivity implements View.OnClickListener {
    private LinearLayout llName;
    private RelativeLayout rlScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_strategy);
        initSystemBar();
        initView();
        setState(860);

    }

    private void initView() {
        llName = (LinearLayout) findViewById(id.ll_level_name);
        rlScore= (RelativeLayout) findViewById(id.rl_know_score);

        rlScore.setOnClickListener(this);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case id.rl_know_score:
                new KnowScoringDialog(this,getSupportFragmentManager());

                break;


        }


    }
}
