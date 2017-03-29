package com.ascba.rebate.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.PopRecAdapter;
import com.ascba.rebate.beans.RecType;
import com.ascba.rebate.fragments.base.Base2Fragment;
import com.ascba.rebate.fragments.recommend.BaseRecFragment;
import com.ascba.rebate.utils.UrlUtils;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyRecActivity extends BaseNetWork4Activity implements
        AdapterView.OnItemClickListener,
        View.OnClickListener,
        BaseNetWork4Activity.Callback {

    private RadioGroup recRg;
    private RadioButton rbOne;
    private RadioButton rbTwo;
    private int position;//当前位置
    private Base2Fragment fragsOne;
    private Base2Fragment fragsTwo;
    private Listener listener;//监听数据类型
    private PopupWindow pop;
    private List<RecType> popData = new ArrayList<>();
    private ListView listView;
    private PopRecAdapter adapter;
    private TextView tvName;
    private TextView tvMobile;
    private TextView tvClass;
    private View viewRec;
    private int finalScene;
    private int type;//0一级筛选  一级筛选
    private int number;//一级推荐全部人数
    private int number2;//二级级推荐全部人数
    private ImageView imgOne,imgTwo;
    private int is_referee;
    private int index;
    private boolean isFirstComing=true;//是否是第一次进入界面  解决被挤掉，2次登录的问题


    public interface Listener {
        void onDataTypeClick(int id, int type);
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rec);
        initViews();

        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            is_referee = intent.getIntExtra("is_referee",0);//有无推荐人
            if(is_referee==0){
                viewRec.setVisibility(View.GONE);
            }else {
                viewRec.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initViews() {
        recRg = ((RadioGroup) findViewById(R.id.rec_rg));
        recRg.setOnClickListener(this);
        rbOne = ((RadioButton) findViewById(R.id.rec_gb_one));
        rbOne.setOnClickListener(this);
        rbTwo = ((RadioButton) findViewById(R.id.rec_gb_two));
        rbTwo.setOnClickListener(this);

        tvName = ((TextView) findViewById(R.id.rec_tv_name));
        tvMobile = ((TextView) findViewById(R.id.rec_tv_mobile));
        tvClass = ((TextView) findViewById(R.id.rec_tv_classes));

        viewRec = findViewById(R.id.rec_man_lat);

        imgOne = (ImageView) findViewById(R.id.rec_gb_img_one);
        imgTwo = (ImageView) findViewById(R.id.rec_gb_img_two);



        requestData(UrlUtils.getMyPspread, 0);
    }

    private void addAllFragments() {
        fragsOne = BaseRecFragment.getInstance(0, "全部");
        fragsTwo = BaseRecFragment.getInstance(1, "全部");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frags_layout, fragsOne).commit();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.rec_gb_one:
                if (position == 0) {//重复点击一次
                    type = 0;
                    requestData(UrlUtils.getGroupPspread, 1);

                } else {
                    imgOne.setVisibility(View.VISIBLE);
                    imgTwo.setVisibility(View.INVISIBLE);
                    ft.add(R.id.frags_layout, fragsOne).remove(fragsTwo).commit();
                    position = 0;
                    index = 0;
                }
                break;
            case R.id.rec_gb_two:
                if (position == 1) {//重复点击一次
                    type = 1;
                    requestData(UrlUtils.getGroupPpspread, 1);

                } else {
                    imgOne.setVisibility(View.INVISIBLE);
                    imgTwo.setVisibility(View.VISIBLE);
                    ft.add(R.id.frags_layout, fragsTwo).remove(fragsOne).commit();
                    position = 1;
                    index = 0;
                }
                break;
        }
    }

    private void requestData(String url, int scene) {
        finalScene = scene;
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    private void showPopList() {
        if (pop == null) {
            pop = new PopupWindow(this);
            pop.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
            pop.setOutsideTouchable(true);
            View popView = getLayoutInflater().inflate(R.layout.city_list, null);
            listView = ((ListView) popView.findViewById(R.id.listView));
            pop.setContentView(popView);
            pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            pop.setFocusable(true);
        }
        pop.showAsDropDown(recRg);
        if (adapter == null && popData.size() != 0) {
            adapter = new PopRecAdapter(popData, this);
            adapter.setCallback(new PopRecAdapter.Callback() {
                @Override
                public void clickItem(int position, View view) {
                    if (pop != null && pop.isShowing()) {
                        pop.dismiss();
                    }
                    RadioButton rb = (RadioButton) view.findViewById(R.id.pop_rb);
                    rb.setChecked(true);
                    RecType recType = popData.get(position);
                    if (listener != null) {
                        listener.onDataTypeClick(recType.getId(), type);
                    }
                    index = position;
                    for (int i = 0; i < popData.size(); i++) {
                        if (i == index) {
                            popData.get(index).setSelect(true);
                        }
                    }
                }
            });
            listView.setAdapter(adapter);
        }else if(adapter!=null){
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void handle200Data(JSONObject dataObj, String message)  {
        if(isFirstComing){
            addAllFragments();
            isFirstComing=false;
        }
        if (finalScene == 0) {
            JSONObject obj1 = dataObj.optJSONObject("p_referee");
            if (obj1 != null) {
                if (obj1.optInt("is_referee") == 1) {//有推荐人
                    viewRec.setVisibility(View.VISIBLE);
                    tvMobile.setText("电话：" + obj1.optString("mobile"));
                    tvClass.setText("级别：" + obj1.optString("referee_group"));
                    tvName.setText(obj1.optString("realname"));
                }
            }
            number = dataObj.optInt("p_referee_count");
            number2 = dataObj.optInt("pp_referee_count");
            rbOne.setText(number + "人\n一级推荐");
            rbTwo.setText(number2 + "人\n二级推荐");
        } else if (finalScene == 1) {
            JSONArray array = null;
            if (type == 0) {
                array = dataObj.optJSONArray("getMyPspread_data");
            } else {
                array = dataObj.optJSONArray("getMyPpspread_data");
            }
            if (array != null && array.length() != 0) {
                if (popData.size() != 0) {
                    popData.clear();
                }
                popData.add(new RecType(false, "全部  ("+number+")", 0));

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.optJSONObject(i);
                    RecType rt = new RecType(false, obj.optString("name") + "  (" + obj.optInt("count") + ")", obj.optInt("id"));
                    popData.add(rt);
                }

                for (int i = 0; i < popData.size(); i++) {
                    if (i == index) {
                        popData.get(index).setSelect(true);
                    }
                }

                showPopList();
            } else {
                Toast.makeText(this, "无分类", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void handle404(String message) {

    }

    @Override
    public void handleNoNetWork() {

    }
}
