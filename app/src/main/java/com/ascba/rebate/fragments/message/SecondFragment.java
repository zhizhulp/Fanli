package com.ascba.rebate.fragments.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.fragments.RecommendFragment;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.view.SwitchButton;



public class SecondFragment extends Fragment {


    private SwitchButton sbtn;
    private SystemMsgFragment rf;
    private NoticeMsgFragment rf2;
    private FragmentManager fm;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.second_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        fm=getChildFragmentManager();
        sbtn = ((SwitchButton) view.findViewById(R.id.message_switch_btn));
        sbtn.setLeftText("系统消息");
        sbtn.setRightText("通知消息");
        sbtn.setIsBack(false);
        switchFragment();
    }
    private void switchFragment() {
        rf= new SystemMsgFragment();
        rf2=new NoticeMsgFragment();
        fm.beginTransaction()
                .add(R.id.recommend_fragment,rf)
                .commit();
        sbtn.setCallback(new SwitchButton.Callback() {
            @Override
            public void onLeftClick() {
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.show(rf);
                if(rf2.isAdded()){
                    ft.hide(rf2);
                }
                ft.commit();
            }

            @Override
            public void onRightClick() {
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                if(rf2.isAdded()){
                    ft.show(rf2);
                }else {
                    ft.add(R.id.recommend_fragment,rf2);
                }
                ft.hide(rf);
                ft.commit();
            }
            @Override
            public void onCenterClick() {

            }
        });
    }
}