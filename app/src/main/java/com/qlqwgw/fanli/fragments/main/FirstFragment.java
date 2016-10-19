package com.qlqwgw.fanli.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.qlqwgw.fanli.R;
import com.qlqwgw.fanli.beans.Business;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


/**
 * 扫一扫主页
 */
public class FirstFragment extends Fragment {

    private ListView recBusiness;
    private View headView;
    private RecBusinessAdapter mAdapter;
    private List<Business> mList;

    public static FirstFragment instance() {
        FirstFragment view = new FirstFragment();
		return view;
	}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment, null);
        headView=inflater.inflate(R.layout.main_bussiness_list_head,null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecBusiness(view);//初始化ListView
    }


    private void initRecBusiness(View view) {
        recBusiness = ((ListView) view.findViewById(R.id.main_recommend_business_list));
        recBusiness.addHeaderView(headView);
        initList();
        mAdapter=new RecBusinessAdapter(mList,getContext());
        recBusiness.setAdapter(mAdapter);
        //downRecBusinessData("www.wuhan.com?name=");
    }
    //模拟商家列表
    private void initList() {
        mList=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Business business=new Business(R.mipmap.logo,"测试"+i,"金牌会员",R.mipmap.logo,(20+i)+"个好评",(200+i*100)+"m");
            mList.add(business);
        }
    }

    /**
     * 网络请求主页商户列表
     * @param url
     */
    private void downRecBusinessData(String url) {
        //Map<String, String> params = new HashMap<String, String>();
        //params.put("name", "zhy");
        //String url1 = mBaseUrl + "user!getUsers";
        OkHttpUtils//
                .post()//
                .url(url)//
//                .params(params)//
                .build()//
                .execute(new ListUserCallback()//
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                    }

                    @Override
                    public void onResponse(List<Business> response, int id)
                    {

                    }
                });
    }
}
