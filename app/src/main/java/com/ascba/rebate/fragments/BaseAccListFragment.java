package com.ascba.rebate.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.ascba.rebate.R;
import com.ascba.rebate.adapter.WhiteAccountAdapter;
import com.ascba.rebate.beans.CashAccount;
import com.ascba.rebate.beans.CashAccountType;
import com.ascba.rebate.fragments.base.BaseFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BaseAccListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BaseAccListFragment extends BaseFragment {
    private ListView cashAccountListView;
    private WhiteAccountAdapter adapter;
    private ArrayList<CashAccount> selectList;
    private ArrayList<CashAccount> allList;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private View noView;


    public BaseAccListFragment() {

    }

    public static BaseAccListFragment newInstance(String param1, ArrayList<CashAccount> list) {
        BaseAccListFragment fragment = new BaseAccListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putParcelableArrayList(ARG_PARAM2,list);
        fragment.setArguments(args);
        return fragment;
    }
    //对arraylist进行筛选
    private void getSelectList(List<CashAccount> list,String type){
        selectList.clear();
        for (int i = 0; i < list.size(); i++) {
            CashAccount ca = list.get(i);
            CashAccountType type1 = ca.getType();
            if("0".equals(type)){
                if(type1==CashAccountType.EMPLOYEE){
                    selectList.add(ca);
                }
            }else if(type.equals("1")){
                if(type1==CashAccountType.CASH_GET){
                    selectList.add(ca);
                }
            }else if(type.equals("2")){
                if(type1==CashAccountType.EXCHANGE){
                    selectList.add(ca);
                }
            }else if(type.equals("3")){
                if(type1==CashAccountType.RECHARGE){
                    selectList.add(ca);
                }
            }else if(type.equals("4")){
                if(type1==CashAccountType.COST){
                    selectList.add(ca);
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectList=new ArrayList<>();
            mParam1 = getArguments().getString(ARG_PARAM1);
            allList=getArguments().getParcelableArrayList(ARG_PARAM2);
            getSelectList(allList,mParam1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base_acc_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

    }

    private void init(View view) {
        noView = view.findViewById(R.id.no_view);
        initListView(view);
    }
    private void initListView(View view) {
        cashAccountListView = ((ListView) view.findViewById(R.id.cash_account_list));
        if(selectList.size()==0){
            noView.setVisibility(View.VISIBLE);
        }else {
            noView.setVisibility(View.GONE);
        }
        adapter = new WhiteAccountAdapter(getActivity(),selectList);
        cashAccountListView.setAdapter(adapter);
    }

    private void initData() {

        /*for (int i = 0; i < 10; i++) {
            CashAccount ca=new CashAccount("12.16","12.30","3000","老家消费",CashAccountType.EMPLOYEE);
            selectList.add(ca);
        }*/
    }
}
