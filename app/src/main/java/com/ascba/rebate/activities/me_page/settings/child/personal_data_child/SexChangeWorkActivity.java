package com.ascba.rebate.activities.me_page.settings.child.personal_data_child;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.activities.me_page.settings.child.PersonalDataWorkActivity;

public class SexChangeWorkActivity extends BaseNetWorkActivity implements View.OnClickListener {

    private View manParent;
    private View womanParent;
    private ImageView ivMan;
    private ImageView ivWoman;
    public static final int SELECT_MAN = 0x08;
    public static final int SELECT_WOMAN = 0x09;
    public static final int SELECT_PRI=0x10;
    private ImageView ivPri;
    private View priParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex_change);
        //StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
        handleIntent();
        clickEvent();
    }

    private void handleIntent() {
        String tag = getIntent().getStringExtra("tag");
        if (tag.equals("男")) {
            ivMan.setVisibility(View.VISIBLE);
            ivWoman.setVisibility(View.GONE);
            ivPri.setVisibility(View.GONE);
        } else if (tag.equals("女")) {
            ivWoman.setVisibility(View.VISIBLE);
            ivMan.setVisibility(View.GONE);
            ivPri.setVisibility(View.GONE);
        } else {
            ivPri.setVisibility(View.VISIBLE);
            ivMan.setVisibility(View.GONE);
            ivWoman.setVisibility(View.GONE);
        }
    }

    private void clickEvent() {
        manParent.setOnClickListener(this);
        womanParent.setOnClickListener(this);
        priParent.setOnClickListener(this);
    }

    private void initViews() {
        manParent = findViewById(R.id.select_man);
        womanParent = findViewById(R.id.select_woman);
        priParent = findViewById(R.id.select_pri);
        ivMan = (ImageView) findViewById(R.id.iv_sex_man);
        ivWoman = (ImageView) findViewById(R.id.iv_sex_woman);
        ivPri = ((ImageView) findViewById(R.id.iv_sex_pri));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_man:
                ivMan.setVisibility(View.VISIBLE);
                ivWoman.setVisibility(View.GONE);
                ivPri.setVisibility(View.GONE);
                Intent intent = new Intent(this, PersonalDataWorkActivity.class);
                setResult(SELECT_MAN, intent);
                finish();
                break;
            case R.id.select_woman:
                ivWoman.setVisibility(View.VISIBLE);
                ivMan.setVisibility(View.GONE);
                ivPri.setVisibility(View.GONE);
                Intent intent1 = new Intent(this, PersonalDataWorkActivity.class);
                setResult(SELECT_WOMAN, intent1);
                finish();
                break;
            case R.id.select_pri:
                ivPri.setVisibility(View.VISIBLE);
                ivWoman.setVisibility(View.GONE);
                ivMan.setVisibility(View.GONE);
                Intent intent2 = new Intent(this, PersonalDataWorkActivity.class);
                setResult(SELECT_PRI, intent2);
                finish();
                break;
        }
    }
}
