package com.ascba.rebate.view;

import android.app.Activity;

import com.ascba.rebate.task.InitAddressTask;

import java.util.ArrayList;

import cn.qqtheme.framework.beans.Province;
import cn.qqtheme.framework.picker.AddressPicker;

/**
 * Created by 李鹏 on 2017/04/22 0022.
 */

public class AddressPickerView {

    private Activity activity;
    private InitAddressTask.Callback callback;
    private String selectedProvince = null, selectedCity = null, selectedCounty = null;
    private boolean hideProvince = false;
    private boolean hideCounty = false;
    private ArrayList<Province> result;
    private AddressPicker picker;

    public void setHideProvince(boolean hideProvince) {
        this.hideProvince = hideProvince;
    }

    public void setHideCounty(boolean hideCounty) {
        this.hideCounty = hideCounty;
    }

    public void setCallback(InitAddressTask.Callback callback) {
        this.callback = callback;
    }

    public AddressPickerView(Activity activity, ArrayList<Province> result) {
        this.activity = activity;
        this.result = result;
    }

    public void setRegion(String selectedProvince, String selectedCity, String selectedCounty) {
        this.selectedProvince = selectedProvince;
        this.selectedCity = selectedCity;
        this.selectedCounty = selectedCounty;
    }

    public void showPicker() {
        if (result.size() > 0) {
            if (picker == null) {
                picker = new AddressPicker(activity, result);
                picker.setHideProvince(hideProvince);
                picker.setHideCounty(hideCounty);
                if (hideCounty) {
                    picker.setColumnWeight(1 / 3.0, 2 / 3.0);//将屏幕分为3份，省级和地级的比例为1:2
                } else {
                    picker.setColumnWeight(2 / 8.0, 3 / 8.0, 3 / 8.0);//省级、地级和县级的比例为2:3:3
                }
            }
            picker.setOnAddressPickListener(callback);
            picker.setSelectedItem(selectedProvince, selectedCity, selectedCounty);
            picker.show();
        } else {
            callback.onAddressInitFailed();
        }
    }
}
