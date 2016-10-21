package com.qlqwgw.fanli.fragments.me;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;

import com.qlqwgw.fanli.R;
import com.qlqwgw.fanli.beans.GridBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by linhonghong on 2015/8/11.
 */
public class FourthFragment extends Fragment {

    private GridView gv;
    private MyCenterGridAdapter mAdapter;
    private List<GridBean> mList;

    public static FourthFragment instance() {
        FourthFragment view = new FourthFragment();
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fourth_fragment, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initGridView(view);
    }

    private void initGridView(View view) {
        gv = ((GridView) view.findViewById(R.id.me_gv));
        initMList();
        mAdapter=new MyCenterGridAdapter(mList,getContext());
        gv.setAdapter(mAdapter);
    }

    private void initMList() {
        mList=new ArrayList<>();
        GridBean gb=new GridBean(R.mipmap.me_white_score,"白积分","6821");
        GridBean gb1=new GridBean(R.mipmap.me_pre_cash,"预付款","110.00");
        GridBean gb2=new GridBean(R.mipmap.me_red,"红积分","486");
        GridBean gb3=new GridBean(R.mipmap.me_card,"银行卡","5");
        GridBean gb4=new GridBean(R.mipmap.me_vip,"会员升级","立即升级");
        GridBean gb5=new GridBean(R.mipmap.me_recommend,"我的推荐","0/0");
        GridBean gb6=new GridBean(R.mipmap.me_business,"商户中心","尚未开通");
        GridBean gb7=new GridBean(R.mipmap.me_employee_score,"佣金积分","7689");
        mList.add(gb);
        mList.add(gb1);
        mList.add(gb2);
        mList.add(gb3);
        mList.add(gb4);
        mList.add(gb5);
        mList.add(gb6);
        mList.add(gb7);
    }
}