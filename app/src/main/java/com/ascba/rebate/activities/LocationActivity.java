package com.ascba.rebate.activities;

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
import com.ascba.rebate.view.listener.OnAddressChangeListener;

public class LocationActivity extends BaseNetWorkActivity implements OnAddressChangeListener {
    private ChooseAddressWheel chooseAddressWheel = null;
    private TextView tvLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        init();
    }
    //城市选择
    public void goLocation(View view) {
        Utils.hideKeyBoard(this);
        chooseAddressWheel.show(view);
    }
    public void init(){
        tvLocation = ((TextView) findViewById(R.id.location_tv));
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

    public void saveLocation(View view) {
        Intent intent = getIntent();
        intent.putExtra("location",tvLocation.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }
}
