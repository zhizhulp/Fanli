package com.ascba.rebate.activities.me_page.business_center_child.child;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.view.MoneyBar;
import com.jaeger.library.StatusBarUtil;

public class BusinessTagActivity extends BaseNetWorkActivity {
    private TextView edType;
    private RadioGroup rg;
    private RadioButton rbEat;
    private RadioButton rbPlay;
    private RadioButton rbRest;
    private RadioButton rbCloth;
    private RadioButton rbHouse;
    private RadioButton rbMedi;
    private RadioButton rbToy;
    private RadioButton rbSoftEat;
    private RadioButton rbOffice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_tag);
        //StatusBarUtil.setColor(this,getResources().getColor(R.color.moneyBarColor));
        initViews();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            String seller_taglib = intent.getStringExtra("seller_taglib");
            if(seller_taglib!=null){
                edType.setText(seller_taglib);
                if(seller_taglib.equals(rbEat.getText().toString())){
                    rg.check(R.id.rb_eat);
                }else if(seller_taglib.equals(rbPlay.getText().toString())){
                    rg.check(R.id.rb_eat);
                }else if(seller_taglib.equals(rbRest.getText().toString())){
                    rg.check(R.id.rb_rest);
                }else if(seller_taglib.equals(rbCloth.getText().toString())){
                    rg.check(R.id.rb_cloth);
                }else if(seller_taglib.equals(rbHouse.getText().toString())){
                    rg.check(R.id.rb_house);
                }else if(seller_taglib.equals(rbMedi.getText().toString())){
                    rg.check(R.id.rb_medicine);
                }else if(seller_taglib.equals(rbToy.getText().toString())){
                    rg.check(R.id.rb_toy);
                }else if(seller_taglib.equals(rbSoftEat.getText().toString())){
                    rg.check(R.id.rb_soft_eat);
                }else if(seller_taglib.equals(rbOffice.getText().toString())){
                    rg.check(R.id.rb_office);
                }
            }
        }
    }

    private void initViews() {
        edType = ((TextView) findViewById(R.id.ed_business_data_type));
        rbEat = ((RadioButton) findViewById(R.id.rb_eat));
        rbPlay = ((RadioButton) findViewById(R.id.rb_play));
        rbRest = ((RadioButton) findViewById(R.id.rb_rest));
        rbCloth = ((RadioButton) findViewById(R.id.rb_cloth));
        rbHouse = ((RadioButton) findViewById(R.id.rb_house));
        rbMedi = ((RadioButton) findViewById(R.id.rb_medicine));
        rbToy = ((RadioButton) findViewById(R.id.rb_toy));
        rbSoftEat = ((RadioButton) findViewById(R.id.rb_soft_eat));
        rbOffice = ((RadioButton) findViewById(R.id.rb_office));
        ((MoneyBar) findViewById(R.id.mb_type)).setCallBack(new MoneyBar.CallBack() {
            @Override
            public void clickImage(View im) {

            }

            @Override
            public void clickComplete(View tv) {
                String s = edType.getText().toString();
                Intent intent=getIntent();
                if(!s.equals("请选择商家标签")){
                    intent.putExtra("business_data_type",s);
                }
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        rg = ((RadioGroup) findViewById(R.id.rg));
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(rbEat.getId()==checkedId){
                    edType.setText(rbEat.getText().toString());
                }else if(rbPlay.getId()==checkedId){
                    edType.setText(rbPlay.getText().toString());
                }else if(rbRest.getId()==checkedId){
                    edType.setText(rbRest.getText().toString());
                }else if(rbCloth.getId()==checkedId){
                    edType.setText(rbCloth.getText().toString());
                }else if(rbHouse.getId()==checkedId){
                    edType.setText(rbHouse.getText().toString());
                }else if(rbMedi.getId()==checkedId){
                    edType.setText(rbMedi.getText().toString());
                }else if(rbToy.getId()==checkedId){
                    edType.setText(rbToy.getText().toString());
                }else if(rbSoftEat.getId()==checkedId){
                    edType.setText(rbSoftEat.getText().toString());
                }else if(rbOffice.getId()==checkedId){
                    edType.setText(rbOffice.getText().toString());
                }
            }
        });


    }
}
