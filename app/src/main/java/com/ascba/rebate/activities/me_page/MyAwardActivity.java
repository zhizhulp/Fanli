package com.ascba.rebate.activities.me_page;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.fragments.award.FirstAwardFragment;
import com.ascba.rebate.fragments.award.SecAwardFragment;
import com.ascba.rebate.fragments.base.BaseNetFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/21 0021.
 * 我的奖励
 */

public class MyAwardActivity extends BaseNetActivity implements View.OnClickListener{

    private RadioGroup recRg;
    public RadioButton rbOne;
    public RadioButton rbTwo;
    private BaseNetFragment fragsOne;
    private BaseNetFragment fragsTwo;
    private ImageView imgOne, imgTwo;
    public TextView tvAll;
    private List<Fragment> fragments=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomm);
        initViews();
    }

    private void initViews() {
        tvAll = (TextView) findViewById(R.id.tv_rec_all_money);

        recRg = ((RadioGroup) findViewById(R.id.rec_rg));
        recRg.setOnClickListener(this);
        rbOne = ((RadioButton) findViewById(R.id.rec_gb_one));
        rbOne.setOnClickListener(this);
        rbTwo = ((RadioButton) findViewById(R.id.rec_gb_two));
        rbTwo.setOnClickListener(this);

        imgOne = (ImageView) findViewById(R.id.rec_gb_img_one);
        imgTwo = (ImageView) findViewById(R.id.rec_gb_img_two);

        fragsOne = new FirstAwardFragment();
        fragsTwo = new SecAwardFragment();
        addFragment(fragsOne);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rec_gb_one:
                imgOne.setVisibility(View.VISIBLE);
                imgTwo.setVisibility(View.INVISIBLE);
                addFragment(fragsOne);
                break;
            case R.id.rec_gb_two:
                imgOne.setVisibility(View.INVISIBLE);
                imgTwo.setVisibility(View.VISIBLE);
                addFragment(fragsTwo);
                break;
        }
    }

    private void addFragment(Fragment f){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(!fragments.contains(f)){
            ft.add(R.id.frags_layout, f);
            fragments.add(f);
        }
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            if(fragment!=f){
                ft.hide(fragment);
            }else {
                ft.show(fragment);
            }
        }
        ft.commit();
    }
}
