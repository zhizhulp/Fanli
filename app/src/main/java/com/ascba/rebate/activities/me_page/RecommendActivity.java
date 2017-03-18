package com.ascba.rebate.activities.me_page;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.fragments.recommend.RecScoreFragment;
import com.ascba.rebate.fragments.recommend.RecommendFragment;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.view.SwitchButton;
import com.jaeger.library.StatusBarUtil;

public class RecommendActivity extends BaseNetWorkActivity {
    private SwitchButton sb;
    private RecommendFragment rf;
    private RecScoreFragment rf2;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        //StatusBarUtil.setColor(this, 0xffe52020);
        fm = getSupportFragmentManager();
        switchFragment();

    }

    private void switchFragment() {
        rf=RecommendFragment.getInstance("历史累计兑现券(元)");
        rf2=RecScoreFragment.getInstance("历史累计积分(分)");
        fm.beginTransaction()
                .add(R.id.recommend_fragment,rf)
                .add(R.id.recommend_fragment,rf2)
                .commit();
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
            @Override
            public void onCenterClick() {
                LogUtils.PrintLog("123","center is click");
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.hide(rf);
                ft.hide(rf2);
                ft.commit();
            }
        });
    }
}
