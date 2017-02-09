package com.ascba.rebate.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.me_page.recmmennd_child.FirstRecActivity;
import com.ascba.rebate.activities.me_page.recmmennd_child.SecondRecActivity;
import com.ascba.rebate.adapter.RecAdapter;
import com.ascba.rebate.beans.FirstRec;
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
public class RecScoreFragment extends BaseFragment implements View.OnClickListener,BaseFragment.Callback {

    private ListView recommendListView;
    private RecAdapter recAdapter;
    private List<FirstRec> mList;
    private TextView tvTitle;
    private String title;
    private TextView tvAll;
    private View tvNoView;
    private TextView tvFirNum;
    private TextView tvSecNum;

    public RecScoreFragment() {

    }
    public static RecScoreFragment getInstance(String title){
        RecScoreFragment fragment=new RecScoreFragment();
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
        return inflater.inflate(R.layout.fragment_recommend, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        tvNoView = view.findViewById(R.id.no_view);
        tvFirNum = ((TextView) view.findViewById(R.id.first_rec_num));
        tvSecNum = ((TextView) view.findViewById(R.id.first_sec_num));
        initTitle(view);
        initListView(view);
        initFirstRec(view);
        initSecondRec(view);
        //requestRecList();//获取推荐列表
    }

    private void requestRecList() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.getMyReferee, 0, true);
        executeNetWork(request,"请稍后");
        setCallback(this);
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
        tvAll = ((TextView) view.findViewById(R.id.tv_rec_all_money));
        initData();
        recAdapter = new RecAdapter(mList,getActivity());
        recommendListView.setAdapter(recAdapter);
    }

    private void initData() {
        mList=new ArrayList<>();
        /*for (int i = 0; i < 20; i++) {
            FirstRec firstRec=new FirstRec("推荐-王朋(VIP)",R.mipmap.me_user_img,"推荐5人","+1000元兑现券","2016.12.31");
            mList.add(firstRec);
        }*/

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

    @SuppressLint("SetTextI18n")
    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        JSONObject recObj = dataObj.optJSONObject("getCashingMoney");
        String cashing_money = recObj.optString("cashing_money");
        int p_referee_count = recObj.optInt("p_referee_count");//一级人数
        int pp_referee_count = recObj.optInt("pp_referee_count");
        int p_referee = recObj.optInt("p_referee");//一级笔数
        int pp_referee = recObj.optInt("pp_referee");
        tvAll.setText(cashing_money);
        tvFirNum.setText(p_referee+"笔奖励/"+p_referee_count+"人");
        tvSecNum.setText(pp_referee+"笔奖励/"+pp_referee_count+"人");
        JSONArray list = recObj.optJSONArray("p_member_list");
        if(list==null || list.length()==0){
            tvNoView.setVisibility(View.VISIBLE);
        }else{
            tvNoView.setVisibility(View.GONE);
            for (int i = 0; i < list.length(); i++) {
                JSONObject memObj = list.optJSONObject(i);
                int member_id = memObj.optInt("member_id");
                String realname = memObj.optString("realname");
                String group_name = memObj.optString("group_name");
                String cashing_money1 = memObj.optString("cashing_money");
                long create_time = memObj.optLong("create_time");
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd");
                String time = sdf.format(new Date(create_time * 1000));
                FirstRec firstRec=new FirstRec(member_id,realname,group_name,cashing_money1,time);
                mList.add(firstRec);
            }
            recAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void handleReqFailed() {

    }
}
