package com.ascba.fanli.activities;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;
import com.ascba.fanli.adapter.CardListAdapter;
import com.ascba.fanli.beans.Card;
import com.ascba.fanli.utils.LogUtils;
import com.ascba.fanli.view.MoneyBar;

import java.util.ArrayList;
import java.util.List;

public class CardActivity extends BaseActivity {

    private MoneyBar CardMB;
    private ListView cardListView;
    private CardListAdapter cardListAdapter;
    private List<Card> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        findView();
    }
    //点击+号，出现弹出框
    private void findView() {
        CardMB = ((MoneyBar) findViewById(R.id.card_money_bar));
        CardMB.setCallBack(new MoneyBar.CallBack() {
            private TextView deleteTextView;
            private TextView addTextView;

            @Override
            public void clickImage(View im) {
                PopupWindow p=new PopupWindow(CardActivity.this);
                View view = LayoutInflater.from(CardActivity.this).inflate(R.layout.card_add_pop, null);
                p.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                p.setOutsideTouchable(true);
                p.setContentView(view);
                p.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                p.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                p.showAsDropDown(CardMB.tailIcon);
                addCardEvent(view,p);
                deleteCardEvent(view);


            }
            //添加银行卡事件
            private void deleteCardEvent(View view) {

                addTextView = ((TextView) view.findViewById(R.id.card_add));
            }
            //删除银行卡事件
            private void addCardEvent(View view,final PopupWindow p) {

                deleteTextView = ((TextView) view.findViewById(R.id.card_delete));
                deleteTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        p.dismiss();
                        for (int i = 0; i <cardListAdapter.getCount(); i++) {
                            RelativeLayout relativeLayout = (RelativeLayout) cardListView.getChildAt(i);
                            ObjectAnimator.ofFloat(relativeLayout,"TranslationX",0.0f,50.0f)
                                    .setDuration(1000)
                                    .start();
                            CheckBox checkBox = (CheckBox) relativeLayout.findViewById(R.id.cb);
                            checkBox.setPadding(50,0,0,0);
                        }
                    }
                });
            }

        });


        cardListView = ((ListView) findViewById(R.id.card_list));
        mList=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if(i%3==0){
                Card card=new Card("中国农业银行","储蓄卡","**** **** **** 1234",false);
                mList.add(card);
            }else if(i%3==1){
                Card card=new Card("中国招商银行","信用卡","**** **** **** 3659",false);
                mList.add(card);
            }else{
                Card card=new Card("中国人民银行","储蓄卡","**** **** **** 4568",false);
                mList.add(card);
            }

        }
        cardListAdapter = new CardListAdapter(mList,this);
        cardListView.setAdapter(cardListAdapter);
    }
}
