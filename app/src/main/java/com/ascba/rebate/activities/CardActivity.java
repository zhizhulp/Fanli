package com.ascba.rebate.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.adapter.CardListAdapter;
import com.ascba.rebate.beans.Card;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MoneyBar;
import com.ascba.rebate.view.deletelistview.ListViewCompat;
import com.ascba.rebate.view.deletelistview.SlideView;
import com.yolanda.nohttp.rest.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardActivity extends BaseNetWorkActivity implements AdapterView.OnItemClickListener,BaseNetWorkActivity.Callback {

    private MoneyBar CardMB;
    private ListViewCompat cardListView;
    private CardListAdapter cardListAdapter;
    private List<Card> mList;
    private int positionL;
    private View noCardVeiw;
    private int finalScene;
    private DialogManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        findView();
    }

    private void findView() {
        dm=new DialogManager(this);
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
        netRequest(0);
    }

    private void netRequest(int scene) {
        finalScene=scene;
        if(finalScene==0){
            Request<JSONObject> request = buildNetRequest(UrlUtils.getBankList, 0, true);
            executeNetWork(request,"请稍后");
            setCallback(this);
        }else if(finalScene==1){
            Request<JSONObject> request = buildNetRequest(UrlUtils.delBanks, 0, true);
            StringBuilder sb=new StringBuilder();
            Card card = mList.get(positionL);
            sb.append(card.getId()+"");
            request.add("bankIds",sb.toString());
            executeNetWork(request,"请稍后");
            setCallback(this);

        }
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

    private void deleteSuccess() {
        mList.remove(cardListView.getPosition());
        cardListAdapter.notifyDataSetChanged();
    }

    public void getIntentFromAddCard() {
        Intent intent = getIntent();
        if(intent!=null){
            Serializable card1 = intent.getSerializableExtra("card");
            if(card1!=null){
                Card card = (Card) card1;
                mList.add(card);
                cardListAdapter.notifyDataSetChanged();
            }

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
                        netRequest(1);

                    }
                });
                builder.show();
            }
        });
        /*SlideView slideView =  mList.get(position).slideView;
        if(slideView.ismIsMoveClick()){//如果是滑动中触发的点击事件，不做处理
            return;
        }
        if (slideView.close() && slideView.getScrollX() == 0) {
            if (cardListAdapter.mLastSlideViewWithStatusOn == null || cardListAdapter.mLastSlideViewWithStatusOn.getScrollX() == 0) {
                //此处添加item的点击事件
            }
        }*/
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if(finalScene==0){
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
        }else if(finalScene==1){
            deleteSuccess();
            dm.buildAlertDialog(message);
        }
    }
}
