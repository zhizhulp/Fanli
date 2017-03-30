package com.ascba.rebate.activities.me_page;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.fragments.award.FirstAwardFragment;
import com.ascba.rebate.fragments.award.SecAwardFragment;
import com.ascba.rebate.fragments.base.Base2Fragment;
import com.ascba.rebate.fragments.recommend.BaseRecFragment;
import com.ascba.rebate.utils.UrlUtils;
import com.yolanda.nohttp.rest.Request;
import org.json.JSONObject;

/**
 * Created by 李鹏 on 2017/03/21 0021.
 * 推荐奖励
 */

public class MyAwardActivity extends BaseNetWorkActivity implements View.OnClickListener, BaseNetWorkActivity.Callback {

    private RadioGroup recRg;
    private RadioButton rbOne;
    private RadioButton rbTwo;
    private Base2Fragment fragsOne;
    private Base2Fragment fragsTwo;
    private ImageView imgOne, imgTwo;
    private TextView tvAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomm);
        initViews();
    }

    private void initViews() {
        tvAll = (TextView) findViewById(R.id.tv_rec_all_money);

        recRg = ((RadioGroup) findViewById(R.id.rec_rg));
        recRg.setOnClickListener(this);
        rbOne = ((RadioButton) findViewById(R.id.rec_gb_one));
        rbOne.setOnClickListener(this);
        rbTwo = ((RadioButton) findViewById(R.id.rec_gb_two));
        rbTwo.setOnClickListener(this);

        imgOne = (ImageView) findViewById(R.id.rec_gb_img_one);
        imgTwo = (ImageView) findViewById(R.id.rec_gb_img_two);

        requestData(UrlUtils.getMyReferee);
        addAllFragments();
    }

    private void addAllFragments() {
        fragsOne = new FirstAwardFragment();
        fragsTwo = new SecAwardFragment();

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
                imgOne.setVisibility(View.VISIBLE);
                imgTwo.setVisibility(View.INVISIBLE);
                ft.show(fragsOne).hide(fragsTwo).commit();
                break;
            case R.id.rec_gb_two:
                imgOne.setVisibility(View.INVISIBLE);
                imgTwo.setVisibility(View.VISIBLE);
                ft.show( fragsTwo).hide(fragsOne).commit();
                break;
        }
    }

    private void requestData(String url) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        JSONObject recObj = dataObj.optJSONObject("getCashingMoney");
        String cashing_money = recObj.optString("cashing_money");
        int p_referee_count = recObj.optInt("p_referee_count");//一级人数
        int pp_referee_count = recObj.optInt("pp_referee_count");
        tvAll.setText(cashing_money);
        rbOne.setText(p_referee_count + "笔奖励\n一级推广");
        rbTwo.setText(pp_referee_count + "笔奖励\n二级级推广");
    }
}
