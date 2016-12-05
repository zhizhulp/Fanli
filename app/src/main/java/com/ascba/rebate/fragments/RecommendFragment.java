package com.ascba.rebate.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.FirstRecActivity;
import com.ascba.rebate.activities.SecondRecActivity;
import com.ascba.rebate.activities.main.APSTSViewPager;
import com.ascba.rebate.adapter.RecAdapter;
import com.ascba.rebate.beans.FirstRec;
import com.lhh.apst.library.CustomPagerSlidingTabStrip;
import com.lhh.apst.library.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends Fragment implements View.OnClickListener {

    private ListView recommendListView;
    private RecAdapter recAdapter;
    private List<FirstRec> mList;
    private TextView tvTitle;
    private String title;

    public RecommendFragment() {
        // Required empty public constructor
    }
    public static RecommendFragment getInstance(String title){
        RecommendFragment fragment=new RecommendFragment();
        Bundle b = new Bundle();
        b.putString("title", title);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle b=getArguments();
        if(b!=null){
            title=b.getString("title");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recommend, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        initTitle(view);
        initListView(view);
        initFirstRec(view);
        initSecondRec(view);
    }

    private void initTitle(View view) {
        tvTitle = ((TextView) view.findViewById(R.id.money_money));
        if(title!=null){
            tvTitle.setText(title);
        }

    }

    private void initSecondRec(View view) {
        view.findViewById(R.id.recommend_first).setOnClickListener(this);
    }

    private void initFirstRec(View view) {
        view.findViewById(R.id.recommend_second).setOnClickListener(this);
    }


    private void initListView(View view) {
        recommendListView = ((ListView) view.findViewById(R.id.recommend_list));
        initData();
        recAdapter = new RecAdapter(mList,getActivity());
        recommendListView.setAdapter(recAdapter);
    }

    private void initData() {
        mList=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            FirstRec firstRec=new FirstRec("钱来钱往(金钻会员)",R.mipmap.me_user_img,"推荐5人","获得1000积分","2016.12.31");
            mList.add(firstRec);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.recommend_first:
                Intent intent=new Intent(getActivity(), FirstRecActivity.class);
                startActivity(intent);
                break;
            case R.id.recommend_second:
                Intent intent2=new Intent(getActivity(), SecondRecActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
