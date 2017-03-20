package com.ascba.rebate.fragments.recommend;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ascba.rebate.R;
import com.ascba.rebate.fragments.base.Base2Fragment;

/**
 * 一级推荐碎片
 */
public class BaseRecFragment extends Base2Fragment {


    private ListView recListView;

    public BaseRecFragment() {
    }

    /**
     * @param classes 级别（一级？ 二级？）
     * @param type 类别
     * @return BaseRecFragment
     */
    public static BaseRecFragment getInstance(int classes,String type){
        BaseRecFragment fragment=new BaseRecFragment();
        Bundle b = new Bundle();
        b.putInt("classes", classes);
        b.putString("type", type);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_rec, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recListView = ((ListView) view.findViewById(R.id.recommend_list));
    }
}
