package com.ascba.fanli.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;
import com.ascba.fanli.fragments.RecommendFragment;
import com.ascba.fanli.view.SwitchButton;

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
        final RecommendFragment rf2=new RecommendFragment();
        fm.beginTransaction().add(R.id.recommend_fragment,rf).commit();
        sb = ((SwitchButton) findViewById(R.id.switch_action_bar));
        sb.setCallback(new SwitchButton.Callback() {
            @Override
            public void onLeftClick() {

                if(!rf.isAdded()){
                    fm.beginTransaction().add(R.id.recommend_fragment,rf).commit();
                    if(rf2.isVisible()){
                        fm.beginTransaction().hide(rf2);
                    }
                }

            }

            @Override
            public void onRightClick() {
                if(!rf2.isAdded()){
                    fm.beginTransaction().add(R.id.recommend_fragment,rf2).commit();
                    if(rf.isVisible()){
                        fm.beginTransaction().hide(rf);
                    }
                }
            }
        });
    }
}
