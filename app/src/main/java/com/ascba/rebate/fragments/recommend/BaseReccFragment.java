package com.ascba.rebate.fragments.recommend;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ascba.rebate.R;
import com.ascba.rebate.adapter.TuiGAdapter;
import com.ascba.rebate.beans.FirstRec;
import com.ascba.rebate.fragments.base.BaseNetFragment;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * 推广基类
 */
public class BaseReccFragment extends BaseNetFragment {
    private RecyclerView rv;
    private SwipeRefreshLayout refreshLat;
    private List<FirstRec> data=new ArrayList<>();
    private TuiGAdapter adapter;

    public BaseReccFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_recc, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = ((RecyclerView) view.findViewById(R.id.recc_list));
        refreshLat = ((SwipeRefreshLayout) view.findViewById(R.id.refresh_layout));
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TuiGAdapter(data, R.layout.rec_list_item_rec);
        rv.setAdapter(adapter);
    }

    public RecyclerView getRv() {
        return rv;
    }

    public void setRv(RecyclerView rv) {
        this.rv = rv;
    }

    public SwipeRefreshLayout getRefreshLat() {
        return refreshLat;
    }

    public void setRefreshLat(SwipeRefreshLayout refreshLat) {
        this.refreshLat = refreshLat;
    }

    public List<FirstRec> getData() {
        return data;
    }

    public void setData(List<FirstRec> data) {
        this.data = data;
    }

    public TuiGAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(TuiGAdapter adapter) {
        this.adapter = adapter;
    }

}
