package com.ascba.rebate.activities.scoring;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class ScoringActivity extends BaseNetActivity implements View.OnClickListener {
    private RecyclerView recycler;
    private ImageButton ibNotifyCancel;
    private LinearLayout llNotify;
    private NewCreditSesameView creditSesameView;
    private LinearLayout llPK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoring);
        initSystemBar();
        initView();
    }

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        // 自定义颜色
        tintManager.setTintColor(Color.parseColor("#02c2b8"));
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


    private void initView() {
//        recycler= (RecyclerView) findViewById(R.id.score_recycler);
//        recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        ibNotifyCancel = (ImageButton) findViewById(R.id.scroing_notify_cancel);
        llNotify = (LinearLayout) findViewById(R.id.scroing_ll_notify);
        creditSesameView = (NewCreditSesameView) findViewById(R.id.scroing_creditsesame);
        llPK= (LinearLayout) findViewById(R.id.scoring_pk);
        ibNotifyCancel.setOnClickListener(this);
        llPK.setOnClickListener(this);
        creditSesameView.setSesameValues(886);
        //startColorChangeAnim();
    }

//    public void startColorChangeAnim() {
//        ObjectAnimator animator = ObjectAnimator.ofInt(mLayout, "backgroundColor", mColors);
//        animator.setDuration(3000);
//        animator.setEvaluator(new ArgbEvaluator());
//        animator.start();
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scroing_notify_cancel:
                llNotify.setVisibility(View.GONE);
                break;
            case R.id.scoring_pk:
                Intent intent=new Intent(this,PKActivity.class);
                startActivity(intent);
                break;
        }
    }
}

