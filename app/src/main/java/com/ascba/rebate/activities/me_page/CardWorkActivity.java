package com.ascba.rebate.activities.me_page;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.activities.me_page.bank_card_child.AddCardWorkActivity;
import com.ascba.rebate.adapter.CardListAdapter;
import com.ascba.rebate.beans.Card;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.SwipeMenuListView2;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CardWorkActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback,SwipeMenuListView.OnMenuItemClickListener {

    private SwipeMenuListView2 cardListView;
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
        //StatusBarUtil.setColor(this, 0xffe52020);
        findView();
    }

    private void findView() {
        dm=new DialogManager(this);
        noCardVeiw = findViewById(R.id.no_card_view);
        initListView();
    }
    private SwipeMenuCreator creator = new SwipeMenuCreator() {
        @Override
        public void create(SwipeMenu menu) {

            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
            deleteItem.setWidth(ScreenDpiUtils.dip2px(CardWorkActivity.this,110));
            deleteItem.setIcon(R.mipmap.bank_card_delete);
            menu.addMenuItem(deleteItem);
        }
    };

    private void initListView() {
        cardListView = ((SwipeMenuListView2) findViewById(R.id.card_list));
        cardListView.setMenuCreator(creator);
        cardListView.setOnMenuItemClickListener(this);
        mList=new ArrayList<>();
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

        }else if(finalScene==2){
            Request<JSONObject> request = buildNetRequest(UrlUtils.checkCardId, 0, true);
            executeNetWork(request,"请稍后");
            setCallback(this);
        }
    }


    private void deleteSuccess() {
        mList.remove(positionL);
        cardListAdapter.notifyDataSetChanged();
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if(finalScene==0){
            JSONArray bank_list = dataObj.optJSONArray("bank_list");
            if(bank_list!=null && bank_list.length()!=0){
                noCardVeiw.setVisibility(View.GONE);
                mList.clear();
                for (int i = 0; i < bank_list.length(); i++) {
                    JSONObject jsonObject = bank_list.optJSONObject(i);
                    int id = jsonObject.optInt("id");
                    String bank = jsonObject.optString("bank");
                    String bank_card = jsonObject.optString("bank_card");
                    String type = jsonObject.optString("type");
                    //String bank_logo = jsonObject.optString("bank_logo");
                    Card card=new Card(bank,type,bank_card);
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
        }else if(finalScene==2){
            int isCardId = dataObj.optInt("isCardId");
            if(isCardId==1){
                JSONObject cardObj = dataObj.optJSONObject("cardInfo");
                String realname = cardObj.optString("realname");
                Intent intent=new Intent(CardWorkActivity.this,AddCardWorkActivity.class);
                intent.putExtra("realname",realname);
                startActivity(intent);
            }
        }
    }

    @Override
    public void handle404(String message) {

    }

    @Override
    public void handleNoNetWork() {

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
    //添加银行卡
    public void goAddCard(View view) {
        netRequest(2);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        netRequest(0);
    }
}
