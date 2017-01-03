package com.ascba.rebate.fragments.account;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.WhiteAccountAdapter;
import com.ascba.rebate.beans.CashAccount;

import java.util.ArrayList;
import java.util.List;


public class WhiteAccountFragment extends Fragment implements View.OnClickListener {

    private ListView mWSAccountList;
    private WhiteAccountAdapter mAdapter;
    private ArrayList<CashAccount> mList;
    private View giveView;
    private View backView;
    private View consumeView;
    private View changeView;
    private List<Fragment> fragments;
    private FragmentManager fm;
    private int position=0;
    private TextView tvGiveTop;
    private TextView tvGiveBo;
    private TextView tvBackTop;
    private TextView tvBackBo;
    private TextView tvConTop;
    private TextView tvConBo;
    private TextView tvChaTop;
    private TextView tvChaBo;
    private List<TextView> listTop=new ArrayList<>();
    private List<TextView> listBottom=new ArrayList<>();

    public static WhiteAccountFragment getInstance(int position){
        WhiteAccountFragment fragment=new WhiteAccountFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        position=getArguments().getInt("position");
        return inflater.inflate(R.layout.fragment_white_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        requestServer();
    }

    private void requestServer() {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment f0= BaseAccListFragment.newInstance("4",mList);
        Fragment f1= BaseAccListFragment.newInstance("5",mList);
        Fragment f2= BaseAccListFragment.newInstance("6",mList);
        Fragment f3= BaseAccListFragment.newInstance("7",mList);
        fragments.add(f0);
        fragments.add(f1);
        fragments.add(f2);
        fragments.add(f3);
        ft.add(R.id.fragment_layout,f0)
                .add(R.id.fragment_layout,f1)
                .add(R.id.fragment_layout,f2)
                .add(R.id.fragment_layout,f3)
                .commit();
        showFragment(0);//默认显示第一个
    }

    private void init(View view) {
        initList();
        initFragments();
        giveView = view.findViewById(R.id.click_give);
        giveView.setOnClickListener(this);
        backView = view.findViewById(R.id.click_back);
        backView.setOnClickListener(this);
        consumeView = view.findViewById(R.id.click_consume);
        consumeView.setOnClickListener(this);
        changeView = view.findViewById(R.id.click_change);
        changeView.setOnClickListener(this);

        tvGiveTop = ((TextView) view.findViewById(R.id.tv_give_top));
        tvGiveBo = ((TextView) view.findViewById(R.id.tv_give_bottom));
        tvBackTop = ((TextView) view.findViewById(R.id.tv_back_top));
        tvBackBo = ((TextView) view.findViewById(R.id.tv_back_bottom));
        tvConTop = ((TextView) view.findViewById(R.id.tv_consume_top));
        tvConBo = ((TextView) view.findViewById(R.id.tv_consume_bottom));
        tvChaTop = ((TextView) view.findViewById(R.id.tv_change_top));
        tvChaBo = ((TextView) view.findViewById(R.id.tv_change_bottom));

        listTop.add(tvGiveTop);
        listTop.add(tvBackTop);
        listTop.add(tvConTop);
        listTop.add(tvChaTop);
        listBottom.add(tvGiveBo);
        listBottom.add(tvBackBo);
        listBottom.add(tvConBo);
        listBottom.add(tvChaBo);
    }
    private void initFragments() {
        fm=getChildFragmentManager();
        fragments=new ArrayList<>();
    }
    private void showFragment(int position){
        if(fragments!=null && fragments.size()!=0){
            FragmentTransaction ft = fm.beginTransaction();
            for (int i = 0; i < fragments.size(); i++) {
                Fragment f = fragments.get(i);
                if(i==position){
                    ft.show(f);
                    listBottom.get(i).setTextColor(getResources().getColor(R.color.moneyBarColor));
                    listTop.get(i).setTextColor(getResources().getColor(R.color.moneyBarColor));
                }else{
                    ft.hide(f);
                    listBottom.get(i).setTextColor(getResources().getColor(R.color.black));
                    listTop.get(i).setTextColor(getResources().getColor(R.color.black));
                }
            }
            ft.commit();
        }
    }


    private void initList() {
        mList=new ArrayList<>();
        /*for (int i = 0; i < 21; i++) {
            if(i %3==0){
                CashAccount whiteAccount=new CashAccount("昨天","21:14",11,"+456.12","登录注册-赠送");
                mList.add(whiteAccount);
            }else if(i %3==1){
                CashAccount whiteAccount=new CashAccount("今天","13:14",11,"+456.12","老家肉饼-消费");
                mList.add(whiteAccount);
            }else{
                CashAccount whiteAccount=new CashAccount("3天之前","10:14",11,"+456.12","推荐会员-返利");
                mList.add(whiteAccount);
            }
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.click_give:
                showFragment(0);
                break;
            case R.id.click_back:
                showFragment(1);
                break;
            case R.id.click_consume:
                showFragment(2);
                break;
            case R.id.click_change:
                showFragment(3);
                break;
        }
    }
}
