package com.ascba.rebate.activities.me_page.recmmennd_child;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.adapter.RecAdapter;
import com.ascba.rebate.beans.FirstRec;
import com.ascba.rebate.utils.UrlUtils;
import com.jaeger.library.StatusBarUtil;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SecondRecActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback {
    private ListView secondRecListView;
    private RecAdapter recAdapter;
    private List<FirstRec> mList;
    private View tvNoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_rec);
        //StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
        requestRecList();//获取推荐列表
    }
    private void requestRecList() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.getMyPReferee, 0, true);
        executeNetWork(request,"请稍后");
        setCallback(this);
    }

    private void initViews() {
        secondRecListView = ((ListView) findViewById(R.id.second_rec_list));
        initData();
        recAdapter = new RecAdapter(mList,this);
        secondRecListView.setAdapter(recAdapter);
        tvNoView =  findViewById(R.id.tv_no_view);
    }

    private void initData() {
        mList=new ArrayList<>();
        /*for (int i = 0; i < 20; i++) {
            FirstRec firstRec=new FirstRec("钱来钱往(金钻会员)",R.mipmap.me_user_img,"推荐5人","获得1000积分","2016.12.31");
            mList.add(firstRec);
        }*/
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) throws JSONException {
        JSONObject recObj = dataObj.optJSONObject("getCashingMoney");
        String cashing_money = recObj.optString("cashing_money");
        int p_referee_count = recObj.optInt("p_referee_count");//一级人数
        int pp_referee_count = recObj.optInt("pp_referee_count");
        int p_referee = recObj.optInt("p_referee");//一级笔数
        int pp_referee = recObj.optInt("pp_referee");
        JSONArray list = recObj.optJSONArray("pp_member_list");
        if(list==null || list.length()==0){
            tvNoView.setVisibility(View.VISIBLE);
        }else{
            tvNoView.setVisibility(View.GONE);
            for (int i = 0; i < list.length(); i++) {
                JSONObject memObj = list.optJSONObject(i);
                int member_id = memObj.optInt("member_id");
                String realname = memObj.optString("m_realname");
                String group_name = memObj.optString("m_group_name");
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
}
