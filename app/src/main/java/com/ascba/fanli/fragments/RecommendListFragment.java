package com.ascba.fanli.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ascba.fanli.R;
import com.ascba.fanli.adapter.FirstRecAdapter;
import com.ascba.fanli.beans.FirstRec;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendListFragment extends ListFragment {
    private List<FirstRec> list;
    private FirstRecAdapter firstRecAdapter;


    public RecommendListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initData();
        firstRecAdapter = new FirstRecAdapter(list,getContext());
        setListAdapter(firstRecAdapter);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recommend_list, container, false);
    }

    private void initData() {
        list=new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            FirstRec firstRec=new FirstRec("某某某",R.mipmap.me_business_center,"推荐5人","获得100元奖励","2016.03.15");
            list.add(firstRec);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }
}
