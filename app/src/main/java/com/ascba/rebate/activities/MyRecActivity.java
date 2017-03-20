package com.ascba.rebate.activities;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.CitiesAdapter;
import com.ascba.rebate.adapter.PopRecAdapter;
import com.ascba.rebate.beans.RecType;
import com.ascba.rebate.fragments.base.Base2Fragment;
import com.ascba.rebate.fragments.recommend.BaseRecFragment;

import java.util.ArrayList;
import java.util.List;

public class MyRecActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, AdapterView.OnItemClickListener {

    private RadioGroup recRg;
    private RadioButton rbOne;
    private RadioButton rbTwo;
    private int position;//当前位置
    private Base2Fragment fragsOne;
    private Base2Fragment fragsTwo;
    private Callback callback;//监听数据类型
    private PopupWindow pop;
    private List<RecType> popData=new ArrayList<>();
    private ListView listView;
    private PopRecAdapter adapter;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public interface Callback {
        void onDataTypeClick();
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rec);

        initViews();
    }

    private void initViews() {
        recRg = ((RadioGroup) findViewById(R.id.rec_rg));
        recRg.setOnCheckedChangeListener(this);

        rbOne = ((RadioButton) findViewById(R.id.rec_gb_one));
        rbTwo = ((RadioButton) findViewById(R.id.rec_gb_two));


        addAllFragments();
    }

    private void addAllFragments() {
        fragsOne = BaseRecFragment.getInstance(0, "全部");
        fragsTwo = BaseRecFragment.getInstance(1, "全部");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frags_layout, fragsOne).add(R.id.frags_layout, fragsTwo).hide(fragsTwo).commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (checkedId) {
            case R.id.rec_gb_one:
                if (position == 0) {//重复点击一次
                    showPopList(0);
                } else {
                    ft.show(fragsOne).hide(fragsTwo).commit();
                    position = 0;
                }
                break;
            case R.id.rec_gb_two:
                if (position == 1) {//重复点击一次
                    showPopList(1);
                } else {
                    ft.show(fragsTwo).hide(fragsOne).commit();
                    position = 1;
                }
                break;
        }
    }

    private void showPopList(int pos) {
        if (pop == null) {
            pop = new PopupWindow(this);
            pop.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
            pop.setOutsideTouchable(false);
            View popView = getLayoutInflater().inflate(R.layout.city_list, null);
            listView = ((ListView) popView.findViewById(R.id.listView));
            listView.setOnItemClickListener(this);
            pop.setContentView(popView);
            pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if (adapter == null && popData.size() != 0) {
            adapter = new PopRecAdapter(popData, this);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        pop.showAsDropDown(recRg);

    }
}
