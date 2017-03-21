package com.ascba.rebate.activities.me_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.activities.me_page.recmmennd_child.FirstRecActivity;
import com.ascba.rebate.activities.me_page.recmmennd_child.SecondRecActivity;
import com.ascba.rebate.adapter.RecAdapter;
import com.ascba.rebate.beans.FirstRec;
import com.ascba.rebate.utils.UrlUtils;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/21 0021.
 * 推荐奖励
 */

public class RecommActivity extends BaseNetWorkActivity implements View.OnClickListener, BaseNetWorkActivity.Callback {

    private Context context;
    private ListView recommendListView;
    private RecAdapter recAdapter;
    private List<FirstRec> mList;
    private TextView tvTitle;
    private String title;
    private TextView tvAll;
    private View tvNoView;
    private TextView tvFirNum;
    private TextView tvSecNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomm);
        context = this;
        initView();
    }

    private void initView() {
        tvNoView = findViewById(R.id.no_view);
        tvFirNum = ((TextView) findViewById(R.id.first_rec_num));
        tvSecNum = ((TextView) findViewById(R.id.first_sec_num));
        initTitle();
        initListView();
        initFirstRec();
        initSecondRec();
        requestRecList();//获取推荐列表
    }

    private void requestRecList() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.getMyReferee, 0, true);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    private void initTitle() {
        tvTitle = ((TextView) findViewById(R.id.money_money));
        if (title != null) {
            tvTitle.setText(title);
        }

    }

    private void initSecondRec() {
        findViewById(R.id.recommend_first).setOnClickListener(this);
    }

    private void initFirstRec() {
        findViewById(R.id.recommend_second).setOnClickListener(this);
    }


    private void initListView() {
        recommendListView = ((ListView) findViewById(R.id.recommend_list));
        tvAll = ((TextView) findViewById(R.id.tv_rec_all_money));
        initData();
        recAdapter = new RecAdapter(mList, context);
        recommendListView.setAdapter(recAdapter);
    }

    private void initData() {
        mList = new ArrayList<>();
        /*for (int i = 0; i < 20; i++) {
            FirstRec firstRec=new FirstRec("推荐-王朋(VIP)",R.mipmap.me_user_img,"推荐5人","+1000元兑现券","2016.12.31");
            mList.add(firstRec);
        }*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recommend_first:
                Intent intent = new Intent(context, FirstRecActivity.class);
                startActivity(intent);
                break;
            case R.id.recommend_second:
                Intent intent2 = new Intent(context, SecondRecActivity.class);
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
        tvFirNum.setText(p_referee_count + "笔奖励/" + p_referee + "人");
        tvSecNum.setText(pp_referee_count + "笔奖励/" + pp_referee + "人");
        JSONArray list = recObj.optJSONArray("p_member_list");
        if (list == null || list.length() == 0) {
            tvNoView.setVisibility(View.VISIBLE);
        } else {
            tvNoView.setVisibility(View.GONE);
            for (int i = 0; i < list.length(); i++) {
                JSONObject memObj = list.optJSONObject(i);
                int member_id = memObj.optInt("member_id");
                String realname = memObj.optString("m_realname");
                String group_name = memObj.optString("m_group_name");
                String cashing_money1 = memObj.optString("cashing_money");
                long create_time = memObj.optLong("create_time");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                String time = sdf.format(new Date(create_time * 1000));
                FirstRec firstRec = new FirstRec(member_id, realname, group_name, cashing_money1, time);
                mList.add(firstRec);
            }
            recAdapter.notifyDataSetChanged();
        }

    }
}
