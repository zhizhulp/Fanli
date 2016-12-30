package com.ascba.rebate.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.CashAccount;
import com.ascba.rebate.beans.CashAccountType;
import com.ascba.rebate.fragments.base.BaseFragment;
import com.ascba.rebate.utils.UrlUtils;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CashAccountFragment extends BaseFragment implements BaseFragment.Callback,View.OnClickListener {
/*    private ListView cashAccountListView;
    private WhiteAccountAdapter adapter;*/
    private ArrayList<CashAccount> mList;
    private View empView;
    private View cashGetView;
    private View exchangeView;
    private View rechargeView;
    private View costView;
    private TextView tvEmployee;
    private TextView tvCashGet;
    private TextView tvExchange;
    private TextView tvRecharge;
    private TextView tvCost;
    private FragmentManager fm;
    private List<Fragment> fragments;
    private int position=-1;


    public CashAccountFragment() {
        // Required empty public constructor
    }
    public static CashAccountFragment getInstance(int position){
        CashAccountFragment fragment=new CashAccountFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position=getArguments().getInt("position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cash_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        requestServer();
    }

    private void requestServer() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.billList, 0, true);
        executeNetWork(request,"请稍后");
        setCallback(this);
    }

    private void init(View view) {
        mList=new ArrayList<>();
        initFragments();
        empView = view.findViewById(R.id.acc_employee);
        empView.setOnClickListener(this);
        cashGetView = view.findViewById(R.id.acc_cash_get);
        cashGetView.setOnClickListener(this);
        exchangeView = view.findViewById(R.id.acc_exchange);
        exchangeView.setOnClickListener(this);
        rechargeView = view.findViewById(R.id.acc_recharge);
        rechargeView.setOnClickListener(this);
        costView = view.findViewById(R.id.acc_cost);
        costView.setOnClickListener(this);
        tvEmployee = ((TextView) view.findViewById(R.id.tv_employee));
        tvCashGet = (TextView)  view.findViewById(R.id.tv_cash_get);
        tvExchange = (TextView)  view.findViewById(R.id.tv_exchange);
        tvRecharge = (TextView)  view.findViewById(R.id.tv_recharge);
        tvCost = (TextView)  view.findViewById(R.id.tv_cost);
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
                }else{
                    ft.hide(f);
                }
            }
            ft.commit();
        }
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        String cashing_money = dataObj.optString("cashing_money");//佣金
        String money_tills = dataObj.optString("money_tills");//提现
        String voucher_money = dataObj.optString("voucher_money");//返利
        String recharge_money = dataObj.optString("recharge_money");//充值
        String fivepercent_money = dataObj.optString("fivepercent_money");//消费
        tvEmployee.setText(cashing_money);
        tvCashGet.setText(money_tills);
        tvExchange.setText(voucher_money);
        tvRecharge.setText(recharge_money);
        tvCost.setText(fivepercent_money);

        JSONArray empList = dataObj.optJSONArray("cashing_list");//佣金
        if(empList!=null&&empList.length()!=0){
            for (int i = 0; i < empList.length(); i++) {
                JSONObject empObj = empList.optJSONObject(i);
                long create_time = empObj.optLong("create_time");
                SimpleDateFormat sdf=new SimpleDateFormat("MM.dd/HH:mm");
                String format = sdf.format(new Date(create_time * 1000));
                String[] split = format.split("/");
                String cashing_money1 = empObj.optString("cashing_money");
                String day=split[0];
                String time=split[1];
                String remark = empObj.optString("remark");
                CashAccount ca=new CashAccount(day,time,cashing_money1,remark, CashAccountType.EMPLOYEE);
                mList.add(ca);
            }
        }
        JSONArray getList = dataObj.optJSONArray("money_tills_list");//提现
        if(getList!=null&&getList.length()!=0){
            for (int i = 0; i < getList.length(); i++) {
                JSONObject getObj = getList.optJSONObject(i);
                long create_time = getObj.optLong("create_time");
                SimpleDateFormat sdf=new SimpleDateFormat("MM.dd/HH:mm");
                String format = sdf.format(new Date(create_time * 1000));
                String[] split = format.split("/");
                String money = getObj.optString("money");
                String day=split[0];
                String time=split[1];
                String remark = getObj.optString("remark");
                String status=getObj.optString("status_tip");
                CashAccount ca=new CashAccount(day,time,money,remark, status,CashAccountType.CASH_GET);
                mList.add(ca);
            }
        }
        JSONArray chaList = dataObj.optJSONArray("voucher_list");//返利
        if(chaList!=null&&chaList.length()!=0){
            for (int i = 0; i < chaList.length(); i++) {
                JSONObject chaObj = chaList.optJSONObject(i);
                long create_time = chaObj.optLong("create_time");
                SimpleDateFormat sdf=new SimpleDateFormat("MM.dd/HH:mm");
                String format = sdf.format(new Date(create_time * 1000));
                String[] split = format.split("/");
                String ratio_money = chaObj.optString("ratio_money");
                String day=split[0];
                String time=split[1];
                String remark = chaObj.optString("remark");
                CashAccount ca=new CashAccount(day,time,ratio_money,remark,CashAccountType.EXCHANGE);
                mList.add(ca);
            }
        }
        JSONArray recList = dataObj.optJSONArray("recharge_list");//充值
        if(recList!=null&&recList.length()!=0){
            for (int i = 0; i < recList.length(); i++) {
                JSONObject recObj = recList.optJSONObject(i);
                long create_time = recObj.optLong("create_time");
                SimpleDateFormat sdf=new SimpleDateFormat("MM.dd/HH:mm");
                String format = sdf.format(new Date(create_time * 1000));
                String[] split = format.split("/");
                String money = recObj.optString("money");
                String day=split[0];
                String time=split[1];
                String remark = recObj.optString("remark");
                CashAccount ca=new CashAccount(day,time,money,remark, CashAccountType.RECHARGE);
                mList.add(ca);
            }
        }
        JSONArray costList = dataObj.optJSONArray("fivepercent_list");//消费
        if(costList!=null&&costList.length()!=0){
            for (int i = 0; i < costList.length(); i++) {
                JSONObject costObj = costList.optJSONObject(i);
                long create_time = costObj.optLong("create_time");
                SimpleDateFormat sdf=new SimpleDateFormat("MM.dd/HH:mm");
                String format = sdf.format(new Date(create_time * 1000));
                String[] split = format.split("/");
                String money = costObj.optString("money");
                String day=split[0];
                String time=split[1];
                String remark = costObj.optString("remark");
                CashAccount ca=new CashAccount(day,time,money,remark,CashAccountType.COST);
                mList.add(ca);
            }
        }

        FragmentTransaction ft = fm.beginTransaction();
        Fragment f0= BaseAccListFragment.newInstance("0",mList);
        Fragment f1= BaseAccListFragment.newInstance("1",mList);
        Fragment f2= BaseAccListFragment.newInstance("2",mList);
        Fragment f3= BaseAccListFragment.newInstance("3",mList);
        Fragment f4= BaseAccListFragment.newInstance("4",mList);
        fragments.add(f0);
        fragments.add(f1);
        fragments.add(f2);
        fragments.add(f3);
        fragments.add(f4);
        ft.add(R.id.fragment_layout,f0)
                .add(R.id.fragment_layout,f1)
                .add(R.id.fragment_layout,f2)
                .add(R.id.fragment_layout,f3)
                .add(R.id.fragment_layout,f4)
                .commit();
        if(position==-1){
            showFragment(0);//默认显示第一个
        }else{
            showFragment(position);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.acc_employee:
                showFragment(0);
                break;
            case R.id.acc_cash_get:
                showFragment(1);
                break;
            case R.id.acc_exchange:
                showFragment(2);
                break;
            case R.id.acc_recharge:
                showFragment(3);
                break;
            case R.id.acc_cost:
                showFragment(4);
                break;
        }
    }
}
