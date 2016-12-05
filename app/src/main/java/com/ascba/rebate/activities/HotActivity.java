package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.beans.Business;
import com.ascba.rebate.beans.ConfigAreaDTO;
import com.ascba.rebate.beans.ConfigsDTO;
import com.ascba.rebate.beans.ConfigsMessageDTO;
import com.ascba.rebate.fragments.main.RecBusinessAdapter;
import com.warmtel.expandtab.ExpandPopTabView;
import com.warmtel.expandtab.KeyValueBean;
import com.warmtel.expandtab.PopOneListView;
import com.warmtel.expandtab.PopTwoListView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HotActivity extends BaseActivity {

    private ExpandPopTabView expandTabView;
    private List<KeyValueBean> mParentLists = new ArrayList<>();
    private List<ArrayList<KeyValueBean>> mChildrenListLists = new ArrayList<>();
    private List<KeyValueBean> mPriceLists;
    private List<KeyValueBean> mSortLists;
    private List<KeyValueBean> mFavorLists;
    private ListView listView;
    private RecBusinessAdapter recBusinessAdapter;
    private List<Business> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot);
        listView = ((ListView) findViewById(R.id.hot_list));
        initData();
        recBusinessAdapter = new RecBusinessAdapter(mList,this);
        listView.setAdapter(recBusinessAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HotActivity.this, BusinessDetailsActivity.class);
                startActivity(intent);
            }
        });
        //setConfigsDatas();

        //expandTabView = (ExpandPopTabView) findViewById(R.id.expandtab_view);
        //addItem(expandTabView, mPriceLists, "", "全部");
        //addItem(expandTabView, mParentLists, mChildrenListLists, "锦江区", "合江亭", "附近");
        //addItem(expandTabView, mFavorLists, "默认", "排序");
        //addItem(expandTabView, mSortLists, "优惠最多", "智能排序");

//        addItem(expandTabView, mParentLists, mChildrenListLists, null, null, "区域");
    }

    private void initData() {
        mList=new ArrayList<>();

    }

    public void addItem(ExpandPopTabView expandTabView, List<KeyValueBean> lists, String defaultSelect, String defaultShowText) {
        PopOneListView popOneListView = new PopOneListView(this);
        popOneListView.setDefaultSelectByValue(defaultSelect);
        //popViewOne.setDefaultSelectByKey(defaultSelect);
        popOneListView.setCallBackAndData(lists, expandTabView, new PopOneListView.OnSelectListener() {
            @Override
            public void getValue(String key, String value) {
                Log.e("tag", "key :" + key + " ,value :" + value);
            }
        });
        expandTabView.addItemToExpandTab(defaultShowText, popOneListView);
    }

    public void addItem(ExpandPopTabView expandTabView, List<KeyValueBean> parentLists,
                        List<ArrayList<KeyValueBean>> childrenListLists, String defaultParentSelect, String defaultChildSelect, String defaultShowText) {
        PopTwoListView popTwoListView = new PopTwoListView(this);
        popTwoListView.setDefaultSelectByValue(defaultParentSelect, defaultChildSelect);
        //distanceView.setDefaultSelectByKey(defaultParent, defaultChild);
        popTwoListView.setCallBackAndData(expandTabView, parentLists, childrenListLists, new PopTwoListView.OnSelectListener() {
            @Override
            public void getValue(String showText, String parentKey, String childrenKey) {
                Log.e("tag", "showText :" + showText + " ,parentKey :" + parentKey + " ,childrenKey :" + childrenKey);
            }
        });
        expandTabView.addItemToExpandTab(defaultShowText, popTwoListView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(expandTabView != null){
            expandTabView.onExpandPopView();
        }
    }

    private void setConfigsDatas() {

        try {

            InputStream is = getAssets().open("searchType.txt");
            String searchTypeJson = readStream(is);
            ConfigsMessageDTO messageDTO = JSONObject.parseObject(searchTypeJson, ConfigsMessageDTO.class);
            ConfigsDTO configsDTO = messageDTO.getInfo();

            mPriceLists = configsDTO.getPriceType();
            mSortLists = configsDTO.getSortType();
            mFavorLists = configsDTO.getSortType();

            List<ConfigAreaDTO> configAreaListDTO = configsDTO.getCantonAndCircle();
            for (ConfigAreaDTO configAreaDTO : configAreaListDTO) {
                KeyValueBean keyValueBean = new KeyValueBean();
                keyValueBean.setKey(configAreaDTO.getKey());
                keyValueBean.setValue(configAreaDTO.getValue());
                mParentLists.add(keyValueBean);

                ArrayList<KeyValueBean> childrenLists = new ArrayList<>();
                for (KeyValueBean keyValueBean1 : configAreaDTO.getBusinessCircle()) {
                    childrenLists.add(keyValueBean1);
                }
                mChildrenListLists.add(childrenLists);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while (i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }
}
