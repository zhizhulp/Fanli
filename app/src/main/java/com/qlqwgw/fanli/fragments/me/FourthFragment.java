package com.qlqwgw.fanli.fragments.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import com.qlqwgw.fanli.R;
import com.qlqwgw.fanli.activities.SettingActivity;
import com.qlqwgw.fanli.beans.GridBean;

import java.util.ArrayList;
import java.util.List;


/**
 *  我的中心
 */
public class FourthFragment extends Fragment {

    private TextView mSettingText;

    public static FourthFragment instance() {
        FourthFragment view = new FourthFragment();
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            View view = inflater.inflate(R.layout.fourth_fragment_status, null);
            return view;
        }else{
            View view = inflater.inflate(R.layout.fourth_fragment, null);
            return view;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //点击设置进入设置页面
        mSettingText = ((TextView) view.findViewById(R.id.me_setting_text));
        mSettingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
    }


}