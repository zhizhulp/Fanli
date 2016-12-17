package com.ascba.rebate.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.adapter.CardListAdapter;
import com.ascba.rebate.beans.Card;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MoneyBar;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.yolanda.nohttp.rest.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback,SwipeMenuListView.OnMenuItemClickListener {

    private MoneyBar CardMB;
    private SwipeMenuListView cardListView;
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
    private SwipeMenuCreator creator = new SwipeMenuCreator() {
        @Override
        public void create(SwipeMenu menu) {

            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
            deleteItem.setTitle("删除");
            deleteItem.setWidth(ScreenDpiUtils.dip2px(CardActivity.this,110));
            deleteItem.setIcon(R.mipmap.bank_card_delete);
            menu.addMenuItem(deleteItem);
        }
    };

    private void initListView() {
        cardListView = ((SwipeMenuListView) findViewById(R.id.card_list));
        cardListView.setMenuCreator(creator);
        cardListView.setOnMenuItemClickListener(this);
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
        mList.remove(positionL);
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

    @Override
    public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
        switch (index){
            case 0:
                dm.buildAlertDialog1("确定要删除银行卡吗？");
                dm.setCallback(new DialogManager.Callback() {
                    @Override
                    public void handleSure() {
                        dm.dismissDialog();
                        positionL=position;
                        netRequest(1);
                    }
                });
                break;
        }
        return false;
    }
}
