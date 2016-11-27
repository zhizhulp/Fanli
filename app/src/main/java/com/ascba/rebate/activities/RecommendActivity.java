package com.ascba.rebate.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.fragments.BackMoneyFragment;
import com.ascba.rebate.fragments.RecommendFragment;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.view.SwitchButton;

public class RecommendActivity extends BaseActivity {

    private SwitchButton sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        switchFragment();
    }

    private void switchFragment() {
        final FragmentManager fm = getSupportFragmentManager();
        final RecommendFragment rf=new RecommendFragment();
        final BackMoneyFragment rf2=new BackMoneyFragment();
        fm.beginTransaction().add(R.id.recommend_fragment,rf).add(R.id.recommend_fragment,rf2).commit();
        fm.beginTransaction().show(rf).hide(rf2).commit();
        sb = ((SwitchButton) findViewById(R.id.switch_action_bar));
        sb.setCallback(new SwitchButton.Callback() {
            @Override
            public void onLeftClick() {
                LogUtils.PrintLog("123","left is click");
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.show(rf);
                ft.hide(rf2);
                ft.commit();
            }

            @Override
            public void onRightClick() {
                LogUtils.PrintLog("123","right is click");
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.show(rf2);
                ft.hide(rf);
                ft.commit();
            }
        });
    }
}
