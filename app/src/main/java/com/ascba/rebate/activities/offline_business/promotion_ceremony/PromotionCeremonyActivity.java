package com.ascba.rebate.activities.offline_business.promotion_ceremony;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

import static com.ascba.rebate.R.style.dialog;

public class PromotionCeremonyActivity extends BaseNetActivity {
    private RecyclerView recycler;
    private TextView promotion_weixin,promotion_friends_circle,promotion_invite_friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_ceremony);
        requestNetWork(UrlUtils.courtesyRecommended,0);
        initView();


    }

    public void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        executeNetWork(what,request,"请稍后");
    }

    private void initView() {
        recycler= (RecyclerView) findViewById(R.id.promotion_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        promotion_weixin= (TextView) findViewById(R.id.promotion_weixin);
        promotion_friends_circle= (TextView) findViewById(R.id.promotion_friends_circle);
        promotion_invite_friends= (TextView) findViewById(R.id.promotion_invite_friends);
        //邀请好友弹出dialog出来
        promotion_invite_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(PromotionCeremonyActivity.this, dialog)
                        .setView(R.layout.promotion_qr_dialog)
                        .create();
                alertDialog.show();

                Window dialogWindow = alertDialog.getWindow();
                WindowManager m = getWindowManager();
                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
                WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                 p.height = (int) (d.getHeight() * 0.62); // 高度设置为屏幕
                p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕
                dialogWindow.setAttributes(p);


            }
        });





    }

    private void requestNetWork(String url, int what){
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        executeNetWork(what,request,"请稍后");

    }
}
