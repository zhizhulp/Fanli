package com.ascba.rebate.activities.me_page.settings.child.personal_data_child;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.beans.AddressDtailsEntity;
import com.ascba.rebate.beans.AddressModel;
import com.ascba.rebate.utils.JsonUtil;
import com.ascba.rebate.utils.Utils;
import com.ascba.rebate.view.ChooseAddressWheel;
import com.ascba.rebate.view.MoneyBar;
import com.ascba.rebate.view.listener.OnAddressChangeListener;
import com.jaeger.library.StatusBarUtil;

public class LocationActivity extends BaseNetWorkActivity implements OnAddressChangeListener,MoneyBar.CallBack {
    private ChooseAddressWheel chooseAddressWheel = null;
    private TextView tvLocation;
    private MoneyBar mb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.moneyBarColor));
        init();
    }
    //城市选择
    public void goLocation(View view) {
        Utils.hideKeyBoard(this);
        chooseAddressWheel.show(view);
    }
    public void init(){
        tvLocation = ((TextView) findViewById(R.id.location_tv));
        mb = ((MoneyBar) findViewById(R.id.mb));
        mb.setCallBack(this);
        initWheel();
        initData();
    }
    private void initWheel() {
        chooseAddressWheel = new ChooseAddressWheel(this);
        chooseAddressWheel.setOnAddressChangeListener(this);
    }

    private void initData() {
        String address = Utils.readAssert(this, "address.txt");
        AddressModel model = JsonUtil.parseJson(address, AddressModel.class);
        if (model != null) {
            AddressDtailsEntity data = model.Result;
            if (data.ProvinceItems != null && data.ProvinceItems.Province != null) {
                chooseAddressWheel.setProvince(data.ProvinceItems.Province);
                chooseAddressWheel.defaultValue(data.Province, data.City, data.Area);
            }
        }
    }

    @Override
    public void onAddressChange(String province, String city, String district) {
        tvLocation.setText(province+ city +  district);
    }


    @Override
    public void clickImage(View im) {

    }

    @Override
    public void clickComplete(View tv) {
        Intent intent = getIntent();
        intent.putExtra("location",tvLocation.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }
}
