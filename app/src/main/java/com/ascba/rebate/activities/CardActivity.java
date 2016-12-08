package com.ascba.rebate.activities;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.adapter.CardListAdapter;
import com.ascba.rebate.beans.Card;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.NetUtils;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MoneyBar;
import com.ascba.rebate.view.ScrollViewWithListView;
import com.ascba.rebate.view.deletelistview.ListViewCompat;
import com.ascba.rebate.view.deletelistview.SlideView;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CardActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private MoneyBar CardMB;
    private ListViewCompat cardListView;
    private CardListAdapter cardListAdapter;
    private List<Card> mList;
    private PhoneHandler phoneHandler;
    private CheckThread checkThread;
    private RequestQueue requestQueue;
    private SharedPreferences sf;
    private int positionL;
    private View noCardVeiw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        findView();
        //getIntentFromAddCard();
    }
    //点击+号，出现弹出框
    private void findView() {
        sf=getSharedPreferences("first_login_success_name_password",MODE_PRIVATE);
        noCardVeiw = findViewById(R.id.no_card_view);
        initMoneyBar();
        initListView();
    }

    private void initListView() {
        cardListView = ((ListViewCompat) findViewById(R.id.card_list));
        mList=new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            if(i%3==0){
//                Card card=new Card("中国农业银行","储蓄卡","**** **** **** 1234",false);
//                mList.add(card);
//            }else if(i%3==1){
//                Card card=new Card("中国招商银行","信用卡","**** **** **** 3659",false);
//                mList.add(card);
//            }else{
//                Card card=new Card("中国人民银行","储蓄卡","**** **** **** 4568",false);
//                mList.add(card);
//            }
//
//        }
        cardListAdapter = new CardListAdapter(mList,this);
        cardListView.setAdapter(cardListAdapter);
        cardListView.setOnItemClickListener(this);
        sendMsgToSevr(UrlUtils.getBankList,0);
    }

    private void initMoneyBar() {
        CardMB = ((MoneyBar) findViewById(R.id.card_money_bar));
        CardMB.setCallBack(new MoneyBar.CallBack() {
            @Override
            public void clickImage(View im) {
                Intent intent=new Intent(CardActivity.this,AddCardActivity.class);
                startActivityForResult(intent,1);
            }

            @Override
            public void clickComplete(View tv) {

            }
        });
    }

    private void sendMsgToSevr(String baseUrl, final int type) {
        boolean netAva = NetUtils.isNetworkAvailable(this);
        if(!netAva){
            Toast.makeText(this, "请打开网络", Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuilder sb=new StringBuilder();
        if(type==1){
            Card card = mList.get(positionL);
            sb.append(card.getId()+"");
        }
        int uuid = sf.getInt("uuid", -1000);
        String token = sf.getString("token", "");
        long expiring_time = sf.getLong("expiring_time", -2000);
        requestQueue = NoHttp.newRequestQueue();
        final ProgressDialog dialog = new ProgressDialog(this, R.style.dialog);
        dialog.setMessage("请稍后");
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest(baseUrl + "?", RequestMethod.POST);
        objRequest.add("sign", UrlEncodeUtils.createSign(baseUrl));
        objRequest.add("uuid", uuid);
        objRequest.add("token", token);
        objRequest.add("expiring_time", expiring_time);
        if(type==1){
            objRequest.add("bankIds",sb.toString());
        }
        phoneHandler = new PhoneHandler(this);
        phoneHandler.setCallback(new PhoneHandler.Callback() {
            @Override
            public void getMessage(Message msg) {
                dialog.dismiss();
                JSONObject jObj = (JSONObject) msg.obj;
                try {
                    int status = jObj.optInt("status");
                    String message = jObj.getString("msg");
                    if (status == 200) {
                        JSONObject dataObj = jObj.optJSONObject("data");
                        int update_status = dataObj.optInt("update_status");
                        if (update_status == 1) {
                            sf.edit()
                                    .putString("token", dataObj.optString("token"))
                                    .putLong("expiring_time", dataObj.optLong("expiring_time"))
                                    .apply();
                        }
                        if(type==0){
                            JSONArray bank_list = dataObj.optJSONArray("bank_list");
                            if(bank_list!=null && bank_list.length()!=0){
                                noCardVeiw.setVisibility(View.GONE);
                                for (int i = 0; i < bank_list.length(); i++) {
                                    JSONObject jsonObject = bank_list.optJSONObject(i);
                                    int id = jsonObject.optInt("id");
                                    String bank = jsonObject.optString("bank");
                                    String bank_card = jsonObject.optString("bank_card");
                                    String type = jsonObject.optString("type");
                                    String bank_logo = jsonObject.optString("bank_logo");
                                    Card card=new Card(bank,type,bank_card,false);
                                    card.setId(id);
                                    mList.add(card);
                                }
                                cardListAdapter.notifyDataSetChanged();
                            }else{
                                noCardVeiw.setVisibility(View.VISIBLE);
                            }

                        }else {//删除银行卡
                            deleteSuccess();
                            Toast.makeText(CardActivity.this, jObj.optString("msg"), Toast.LENGTH_SHORT).show();
                        }

                    }  else if(status==1||status==2||status==3||status == 4||status==5){//缺少sign参数
                        Intent intent = new Intent(CardActivity.this, LoginActivity.class);
                        sf.edit().putInt("uuid", -1000).apply();
                        startActivity(intent);
                        finish();
                    } else if(status==404){
                        Toast.makeText(CardActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if(status==500){
                        Toast.makeText(CardActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        checkThread = new CheckThread(requestQueue, phoneHandler, objRequest);
        checkThread.start();
        dialog.show();
    }

    private void deleteSuccess() {
        mList.remove(cardListView.getPosition());
        cardListAdapter.notifyDataSetChanged();
    }

    public void getIntentFromAddCard() {
        Intent intent = getIntent();
        if(intent!=null){
            Card card = (Card) intent.getSerializableExtra("card");
            mList.add(card);
            cardListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
        CardListAdapter.ViewHolder v = (CardListAdapter.ViewHolder) ((SlideView) view).getTag();
        v.rightHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this);
                builder.setTitle("提示").setIcon(R.drawable.ic_launcher).setMessage("确定删此条目？")
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        positionL=cardListView.getPosition();
                        sendMsgToSevr(UrlUtils.delBanks,1);

                    }
                });
                builder.show();
            }
        });
        SlideView slideView =  mList.get(position).slideView;
        if(slideView.ismIsMoveClick()){//如果是滑动中触发的点击事件，不做处理
            return;
        }
        if (slideView.close() && slideView.getScrollX() == 0) {
            if (cardListAdapter.mLastSlideViewWithStatusOn == null || cardListAdapter.mLastSlideViewWithStatusOn.getScrollX() == 0) {
                //此处添加item的点击事件
                //Toast.makeText(this, "onItemClick position=" + position, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
