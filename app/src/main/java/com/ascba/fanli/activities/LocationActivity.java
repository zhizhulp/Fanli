package com.ascba.fanli.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;
import com.ascba.fanli.beans.AddressDtailsEntity;
import com.ascba.fanli.beans.AddressModel;
import com.ascba.fanli.utils.JsonUtil;
import com.ascba.fanli.utils.LogUtils;
import com.ascba.fanli.utils.Utils;
import com.ascba.fanli.view.ChooseAddressWheel;
import com.ascba.fanli.view.listener.OnAddressChangeListener;

public class LocationActivity extends BaseActivity implements OnAddressChangeListener {
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
        LogUtils.PrintLog("123LocationActivity",address);
        AddressModel model = JsonUtil.parseJson(address, AddressModel.class);
        if (model != null) {
            AddressDtailsEntity data = model.Result;
            LogUtils.PrintLog("123LocationActivity",data.toString());
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
}
