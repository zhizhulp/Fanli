package com.ascba.rebate.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.FirstRecActivity;
import com.ascba.rebate.activities.PersonalDataActivity;
import com.ascba.rebate.activities.SecondRecActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.main.APSTSViewPager;
import com.ascba.rebate.activities.password_loss.PasswordLossActivity;
import com.ascba.rebate.activities.password_loss.PasswordLossWithCodeActivity;
import com.ascba.rebate.adapter.RecAdapter;
import com.ascba.rebate.beans.FirstRec;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.NetUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.lhh.apst.library.CustomPagerSlidingTabStrip;
import com.lhh.apst.library.ViewHolder;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.JsonObjectRequest;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends Fragment implements View.OnClickListener {

    private ListView recommendListView;
    private RecAdapter recAdapter;
    private List<FirstRec> mList;
    private TextView tvTitle;
    private String title;
    private PhoneHandler phoneHandler;
    private CheckThread checkThread;
    private RequestQueue requestQueue;
    private SharedPreferences sf;

    public RecommendFragment() {

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
        sf=getActivity().getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
        initTitle(view);
        initListView(view);
        initFirstRec(view);
        initSecondRec(view);
        requestForServer(UrlUtils.getReferee);//获取推荐列表
    }

    private void requestForServer(String baseUrl) {
        boolean netAva = NetUtils.isNetworkAvailable(getActivity());
        if(!netAva){
            Toast.makeText(getActivity(), "请打开网络", Toast.LENGTH_SHORT).show();
            return;
        }
        int uuid = sf.getInt("uuid", -1000);
        String token = sf.getString("token", "");
        long expiring_time = sf.getLong("expiring_time", -2000);
        requestQueue= NoHttp.newRequestQueue();
        final ProgressDialog dialog = new ProgressDialog(getActivity(),R.style.dialog);
        dialog.setMessage("请稍后");
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest(baseUrl, RequestMethod.POST);
        objRequest.add("sign", UrlEncodeUtils.createSign(baseUrl));
        objRequest.add("uuid", uuid);
        objRequest.add("token", token);
        objRequest.add("expiring_time", expiring_time);
        phoneHandler=new PhoneHandler(getActivity());
        phoneHandler.setCallback(new PhoneHandler.Callback() {
            @Override
            public void getMessage(Message msg) {
                dialog.dismiss();
                JSONObject jObj= (JSONObject) msg.obj;
                try {
                    int status = jObj.getInt("status");
                    String message = jObj.getString("msg");
                    if(status==200){
                        JSONObject dataObj = jObj.getJSONObject("data");
                        JSONObject rebateObj = dataObj.getJSONObject("rebate");
                        if(rebateObj!=null){
                            JSONArray pArray = rebateObj.getJSONArray("pReferee");
                            if(pArray!=null && pArray.length()!=0){
                                for (int i = 0; i < pArray.length(); i++) {
                                    JSONObject jsonObject = pArray.getJSONObject(i);
                                    int userCount = jsonObject.getInt("userCount");
                                    String nickname = jsonObject.getString("nickname");
                                    String avatar = jsonObject.getString("avatar");
                                    int score = jsonObject.getInt("score");
                                    int create_time = jsonObject.getInt("create_time");
                                    String remark = jsonObject.getString("remark");
                                    FirstRec firstRec=new FirstRec(nickname,R.mipmap.me_user_img,userCount+"",score+"",create_time+"");
                                    mList.add(firstRec);
                                }
                            }
                            recAdapter.notifyDataSetChanged();
                        }
                    } else if(status==1||status==2||status==3||status == 4||status==5){//缺少sign参数
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        sf.edit().putInt("uuid", -1000).apply();
                        startActivity(intent);
                        getActivity().finish();
                    } else if(status==404){
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    } else if(status==500){
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        checkThread=new CheckThread(requestQueue,phoneHandler,objRequest);
        checkThread.start();
        dialog.show();
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
        /*for (int i = 0; i < 20; i++) {
            FirstRec firstRec=new FirstRec("钱来钱往(金钻会员)",R.mipmap.me_user_img,"推荐5人","获得1000积分","2016.12.31");
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
}
