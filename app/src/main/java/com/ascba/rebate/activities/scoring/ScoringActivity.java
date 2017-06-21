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
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.scoring.widget.NewCreditSesameView;
import com.ascba.rebate.activities.scoring.widget.ShareDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;


public class ScoringActivity extends BaseNetActivity implements View.OnClickListener {
    private RecyclerView recycler;
    private ImageButton ibNotifyCancel;
    private LinearLayout llNotify;
    private NewCreditSesameView creditSesameView;
    private LinearLayout raiseScores,llPK,growthFootprint;
    private TextView showOne,knowStrategy;

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


    private void initView() {
//        recycler= (RecyclerView) findViewById(R.id.score_recycler);
//        recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        ibNotifyCancel = (ImageButton) findViewById(R.id.scroing_notify_cancel);
        llNotify = (LinearLayout) findViewById(R.id.scroing_ll_notify);
        creditSesameView = (NewCreditSesameView) findViewById(R.id.scroing_creditsesame);
        showOne= (TextView) findViewById(R.id.scoring_showOne);
        knowStrategy= (TextView) findViewById(R.id.scoring_knowStrategy);

        raiseScores= (LinearLayout) findViewById(R.id.raise_scores);
        llPK= (LinearLayout) findViewById(R.id.scoring_pk);
        growthFootprint= (LinearLayout) findViewById(R.id.scoring_growth_footprint);
        raiseScores.setOnClickListener(this);
        llPK.setOnClickListener(this);
        growthFootprint.setOnClickListener(this);
        showOne.setOnClickListener(this);
        knowStrategy.setOnClickListener(this);



        ibNotifyCancel.setOnClickListener(this);
        creditSesameView.setSesameValues(500);
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
        Intent intent;

        switch (v.getId()){
            case R.id.scroing_notify_cancel:
                llNotify.setVisibility(View.GONE);
                break;
            case R.id.scoring_showOne:
                showScoringDialog();

                break;
            case R.id.scoring_knowStrategy://了解攻略
                 intent=new Intent(this, KnowStrategyActivity.class);
                startActivity(intent);
                break;
            case R.id.raise_scores://提高分数
                showToast("功能待完成中！");
                break;
            case R.id.scoring_pk://pk榜
                 intent=new Intent(this,PKActivity.class);
                startActivity(intent);
                break;
            case R.id.scoring_growth_footprint://成长足迹
                showToast("功能待完成中！");
                break;


        }

    }

    private void showScoringDialog() {
        ShareDialog dialog=new ShareDialog(this);

    }
}

