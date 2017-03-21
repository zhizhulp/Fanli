package com.ascba.rebate.activities;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import org.json.JSONException;
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


    public interface Listener {
        void onDataTypeClick(int id);
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

        addAllFragments();

        requestData(UrlUtils.getMyPspread, 0);
    }

    private void addAllFragments() {
        fragsOne = BaseRecFragment.getInstance(0, "全部");
        fragsTwo = BaseRecFragment.getInstance(1, "全部");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frags_layout, fragsOne).add(R.id.frags_layout, fragsTwo).hide(fragsTwo).commit();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.rec_gb_one:
                if (position == 0) {//重复点击一次
                    requestData(UrlUtils.getGroupPspread, 1);
                } else {
                    ft.show(fragsOne).hide(fragsTwo).commit();
                    position = 0;
                }
                break;
            case R.id.rec_gb_two:
                if (position == 1) {//重复点击一次
                    requestData(UrlUtils.getGroupPpspread, 1);
                } else {
                    ft.show(fragsTwo).hide(fragsOne).commit();
                    position = 1;
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

    private void showPopList(int pos) {
        if(pop==null){
            pop = new PopupWindow(this);
            pop.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
            pop.setOutsideTouchable(true);
            View popView = getLayoutInflater().inflate(R.layout.city_list, null);
            listView = ((ListView) popView.findViewById(R.id.listView));
            listView.setOnItemClickListener(this);
            pop.setContentView(popView);
            pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            pop.setFocusable(true);

        }
        pop.showAsDropDown(recRg);
        if(adapter==null && popData.size() !=0){
            adapter = new PopRecAdapter(popData, this);
            listView.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
        }
        RecType recType = popData.get(position);
        if (listener != null) {
            listener.onDataTypeClick(recType.getId());
        }
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) throws JSONException {
        if (finalScene == 0) {
            JSONObject obj1 = dataObj.optJSONObject("p_referee");
            if (obj1 != null) {
                if (obj1.optInt("is_referee") == 1) {//有推荐人
                    viewRec.setVisibility(View.VISIBLE);
                    tvMobile.setText("电话：" + obj1.optString("mobile"));
                    tvClass.setText("级别：" + obj1.optString("referee_group"));
                    tvName.setText(obj1.optString("realname"));
                } else {
                    viewRec.setVisibility(View.GONE);
                }
            }
            rbOne.setText(dataObj.optInt("p_referee_count") + "人\n一级推荐");
            rbTwo.setText(dataObj.optInt("pp_referee_count") + "人\n二级推荐");
        } else if (finalScene == 1) {
            JSONArray array = dataObj.optJSONArray("getMyPspread_data");
            if (array != null && array.length() != 0) {
                if (popData.size() != 0) {
                    popData.clear();
                }
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.optJSONObject(i);
                    if (i == 0) {
                        RecType rt = new RecType(false, obj.optString("name") + "  (" + obj.optInt("count") + ")", obj.optInt("id"));
                        popData.add(rt);
                    } else {
                        RecType rt = new RecType(false, obj.optString("name") + "  (" + obj.optInt("count") + ")", obj.optInt("id"));
                        popData.add(rt);
                    }
                }
                showPopList(0);
            }else {
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
