package com.ascba.rebate.activities.me_page;

import android.annotation.SuppressLint;
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
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.PopRecAdapter;
import com.ascba.rebate.beans.RecType;
import com.ascba.rebate.fragments.recommend.FirstReccFragment;
import com.ascba.rebate.fragments.recommend.SecReccFragment;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.rest.Request;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MyRecActivity extends BaseNetActivity implements
        AdapterView.OnItemClickListener,
        View.OnClickListener,
        BaseNetActivity.Callback {

    private RadioGroup recRg;
    private RadioButton rbOne;
    private RadioButton rbTwo;
    private int position;//当前位置
    private FirstReccFragment fragsOne;
    private SecReccFragment fragsTwo;
    private Listener1 listener1;//监听一级数据类型
    private Listener2 listener2;//监听二级数据类型
    private TextView tvName;
    private TextView tvMobile;
    private TextView tvClass;
    private View viewRec;
    private int finalScene;
    private int number;//一级推荐全部人数
    private int number2;//二级级推荐全部人数
    private ImageView imgOne, imgTwo;
    private int is_referee;//是否有推荐人
    private int index1;//一级推荐索引
    private int index2;//二级推荐索引
    private boolean isFirstComing = true;//是否是第一次进入界面  解决被挤掉，2次登录的问题


    public interface Listener1 {
        void onDataTypeClick(int id);
    }

    public interface Listener2 {
        void onDataTypeClick(int id);
    }


    public void setListener2(Listener2 listener2) {
        this.listener2 = listener2;
    }

    public void setListener1(Listener1 listener1) {
        this.listener1 = listener1;
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
        if (intent != null) {
            is_referee = intent.getIntExtra("is_referee", 0);//有无推荐人
            if (is_referee == 0) {
                viewRec.setVisibility(View.GONE);
            } else {
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
        fragsOne = new FirstReccFragment();
        fragsTwo = new SecReccFragment();

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
                    imgOne.setVisibility(View.VISIBLE);
                    imgTwo.setVisibility(View.INVISIBLE);
                    ft.show(fragsOne).hide(fragsTwo).commit();
                    position = 0;
                }
                break;
            case R.id.rec_gb_two:
                if (position == 1) {//重复点击一次
                    requestData(UrlUtils.getGroupPpspread, 1);
                } else {
                    imgOne.setVisibility(View.INVISIBLE);
                    imgTwo.setVisibility(View.VISIBLE);
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

    private void showPopList(final int type, final List<RecType> data) {

        final PopupWindow pop = new PopupWindow(this);
        pop.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        pop.setOutsideTouchable(true);
        View popView = getLayoutInflater().inflate(R.layout.city_list, null);
        ListView listView = ((ListView) popView.findViewById(R.id.listView));
        pop.setContentView(popView);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setFocusable(true);
        pop.showAsDropDown(recRg);
        PopRecAdapter adapter = new PopRecAdapter(data, this);
        adapter.setCallback(new PopRecAdapter.Callback() {
            @Override
            public void clickItem(int position, View view) {
                if (pop.isShowing()) {
                    pop.dismiss();
                }
                RadioButton rb = (RadioButton) view.findViewById(R.id.pop_rb);
                rb.setChecked(true);
                RecType recType = data.get(position);
                if (type == 0) {
                    index1 = position;
                    rbOne.setText(recType.getName() + "\n一级推荐");
                    if (listener1 != null) {
                        listener1.onDataTypeClick(recType.getId());
                    }
                } else {
                    index2 = position;
                    rbTwo.setText(recType.getName() + "\n二级推荐");
                    if (listener2 != null) {
                        listener2.onDataTypeClick(recType.getId());
                    }
                }
            }

        });
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if (isFirstComing) {
            addAllFragments();
            isFirstComing = false;
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
            rbOne.setText("全部\n一级推荐");
            rbTwo.setText("全部\n二级推荐");
        } else if (finalScene == 1) {
            JSONArray array;
            array = dataObj.optJSONArray("getMyPpspread_data");
            if (array != null && array.length() != 0) {
                List<RecType> popData = new ArrayList<>();
                if (popData.size() != 0) {
                    popData.clear();
                }
                if (position == 0) {
                    RecType recType = new RecType(false, "全部  (" + number + "人)", 0);
                    recType.setName("全部");
                    popData.add(recType);
                } else {
                    RecType recType = new RecType(false, "全部  (" + number2 + "人)", 0);
                    recType.setName("全部");
                    popData.add(recType);
                }

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.optJSONObject(i);
                    RecType rt = new RecType(false, obj.optString("name") + "  (" + obj.optInt("count") + "人)", obj.optInt("id"));
                    rt.setNum(obj.optInt("count"));
                    rt.setName(obj.optString("name"));
                    popData.add(rt);
                }
                if (position == 0) {
                    for (int i = 0; i < popData.size(); i++) {
                        if (i == index1) {
                            popData.get(i).setSelect(true);
                        }
                    }
                } else {
                    for (int i = 0; i < popData.size(); i++) {
                        if (i == index2) {
                            popData.get(i).setSelect(true);
                        }
                    }
                }

                showPopList(position, popData);
            } else {
                Toast.makeText(this, "无分类", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void handle404(String message) {
        getDm().buildAlertDialog(message);
    }

    @Override
    public void handleNoNetWork() {
        getDm().buildAlertDialog(getResources().getString(R.string.no_network));
    }
}
