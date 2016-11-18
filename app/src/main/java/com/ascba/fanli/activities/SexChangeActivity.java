package com.ascba.fanli.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;

public class SexChangeActivity extends BaseActivity implements View.OnClickListener{

    private View manParent;
    private View womanParent;
    private ImageView ivMan;
    private ImageView ivWoman;
    public static final int SELECT_MAN=0x08;
    public static final int SELECT_WOMAN=0x09;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex_change);
        initViews();
        handleIntent();
        clickEvent();
    }

    private void handleIntent() {
        int tag = getIntent().getIntExtra("tag", 0);
        switch (tag){
            case 1:
                ivMan.setVisibility(View.VISIBLE);
                ivWoman.setVisibility(View.GONE);
                break;
            case 2:
                ivWoman.setVisibility(View.VISIBLE);
                ivMan.setVisibility(View.GONE);
                break;
        }
    }

    private void clickEvent() {
        manParent.setOnClickListener(this);
        womanParent.setOnClickListener(this);
    }

    private void initViews() {
        manParent = findViewById(R.id.select_man);
        womanParent = findViewById(R.id.select_woman);
        ivMan = (ImageView) findViewById(R.id.iv_sex_man);
        ivWoman = (ImageView) findViewById(R.id.iv_sex_woman);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_man:
                ivMan.setVisibility(View.VISIBLE);
                ivWoman.setVisibility(View.GONE);
                Intent intent = new Intent(this,PersonalDataActivity.class);
                setResult(SELECT_MAN,intent);
                finish();
                break;
            case R.id.select_woman:
                ivWoman.setVisibility(View.VISIBLE);
                ivMan.setVisibility(View.GONE);
                Intent intent1 = new Intent(this,PersonalDataActivity.class);
                setResult(SELECT_WOMAN,intent1);
                finish();
                break;
        }
    }
}
