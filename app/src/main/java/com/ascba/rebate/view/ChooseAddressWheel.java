package com.ascba.rebate.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.AreaWheelAdapter;
import com.ascba.rebate.adapter.CityWheelAdapter;
import com.ascba.rebate.adapter.ProvinceWheelAdapter;
import com.ascba.rebate.beans.AddressDtailsEntity;
import com.ascba.rebate.utils.Utils;
import com.ascba.rebate.view.listener.OnAddressChangeListener;
import com.ascba.rebate.view.wheelview.MyOnWheelChangedListener;
import com.ascba.rebate.view.wheelview.MyWheelView;

import java.util.List;


public class ChooseAddressWheel implements MyOnWheelChangedListener {

    private MyWheelView provinceWheel;
    private MyWheelView cityWheel;
    private MyWheelView districtWheel;

    private Activity context;
    private View parentView;
    private PopupWindow popupWindow = null;
    private WindowManager.LayoutParams layoutParams = null;
    private LayoutInflater layoutInflater = null;

    private List<AddressDtailsEntity.ProvinceEntity> province = null;

    private OnAddressChangeListener onAddressChangeListener = null;
    private TextView confirmTV;
    private TextView cancelTV;

    public ChooseAddressWheel(Activity context) {
        this.context = context;
        init();
    }

    private void init() {
        layoutParams = context.getWindow().getAttributes();
        layoutInflater = context.getLayoutInflater();
        initView();
        initPopupWindow();
    }

    private void initView() {
        parentView = layoutInflater.inflate(R.layout.choose_city_layout, null);
//        ButterKnife.bind(this, parentView);
        provinceWheel= (MyWheelView) parentView.findViewById(R.id.province_wheel);
        cityWheel= (MyWheelView) parentView.findViewById(R.id.city_wheel);
        districtWheel= (MyWheelView) parentView.findViewById(R.id.district_wheel);
        confirmTV = ((TextView) parentView.findViewById(R.id.confirm_button));
        cancelTV = ((TextView) parentView.findViewById(R.id.cancel_button));
        confirmTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        provinceWheel.setVisibleItems(7);
        cityWheel.setVisibleItems(7);
        districtWheel.setVisibleItems(7);

        provinceWheel.addChangingListener(this);
        cityWheel.addChangingListener(this);
        districtWheel.addChangingListener(this);
    }

    private void initPopupWindow() {
        popupWindow = new PopupWindow(parentView, WindowManager.LayoutParams.MATCH_PARENT, (int) (Utils.getScreenHeight(context) * (2.0 / 5)));
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setAnimationStyle(R.style.anim_push_bottom);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                layoutParams.alpha = 1.0f;
                context.getWindow().setAttributes(layoutParams);
                popupWindow.dismiss();
            }
        });
    }

    private void bindData() {
        provinceWheel.setViewAdapter(new ProvinceWheelAdapter(context, province));
        updateCitiy();
        updateDistrict();
    }

    @Override
    public void onChanged(MyWheelView wheel, int oldValue, int newValue) {
        if (wheel == provinceWheel) {
            updateCitiy();//省份改变后城市和地区联动
        } else if (wheel == cityWheel) {
            updateDistrict();//城市改变后地区联动
        } else if (wheel == districtWheel) {
        }
    }

    private void updateCitiy() {
        int index = provinceWheel.getCurrentItem();
        List<AddressDtailsEntity.ProvinceEntity.CityEntity> citys = province.get(index).City;
        if (citys != null && citys.size() > 0) {
            cityWheel.setViewAdapter(new CityWheelAdapter(context, citys));
            cityWheel.setCurrentItem(0);
            updateDistrict();
        }
    }

    private void updateDistrict() {
        int provinceIndex = provinceWheel.getCurrentItem();
        List<AddressDtailsEntity.ProvinceEntity.CityEntity> citys = province.get(provinceIndex).City;
        int cityIndex = cityWheel.getCurrentItem();
        List<AddressDtailsEntity.ProvinceEntity.AreaEntity> districts = citys.get(cityIndex).Area;
        if (districts != null && districts.size() > 0) {
            districtWheel.setViewAdapter(new AreaWheelAdapter(context, districts));
            districtWheel.setCurrentItem(0);
        }

    }

    public void show(View v) {
        layoutParams.alpha = 0.6f;
        context.getWindow().setAttributes(layoutParams);
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    public void setProvince(List<AddressDtailsEntity.ProvinceEntity> province) {
        this.province = province;
        bindData();
    }

    public void defaultValue(String provinceStr, String city, String arae) {
        if (TextUtils.isEmpty(provinceStr)) return;
        for (int i = 0; i < province.size(); i++) {
            AddressDtailsEntity.ProvinceEntity provinces = province.get(i);
            if (provinces != null && provinces.Name.equalsIgnoreCase(provinceStr)) {
                provinceWheel.setCurrentItem(i);
                if (TextUtils.isEmpty(city)) return;
                List<AddressDtailsEntity.ProvinceEntity.CityEntity> citys = provinces.City;
                for (int j = 0; j < citys.size(); j++) {
                    AddressDtailsEntity.ProvinceEntity.CityEntity cityEntity = citys.get(j);
                    if (cityEntity != null && cityEntity.Name.equalsIgnoreCase(city)) {
                        cityWheel.setViewAdapter(new CityWheelAdapter(context, citys));
                        cityWheel.setCurrentItem(j);
                        if (TextUtils.isEmpty(arae)) return;
                        List<AddressDtailsEntity.ProvinceEntity.AreaEntity> areas = cityEntity.Area;
                        for (int k = 0; k < areas.size(); k++) {
                            AddressDtailsEntity.ProvinceEntity.AreaEntity areaEntity = areas.get(k);
                            if (areaEntity != null && areaEntity.Name.equalsIgnoreCase(arae)) {
                                districtWheel.setViewAdapter(new AreaWheelAdapter(context, areas));
                                districtWheel.setCurrentItem(k);
                            }
                        }
                    }
                }
            }
        }
    }

    public void confirm() {
        if (onAddressChangeListener != null) {
            int provinceIndex = provinceWheel.getCurrentItem();
            int cityIndex = cityWheel.getCurrentItem();
            int areaIndex = districtWheel.getCurrentItem();

            String provinceName = null, cityName = null, areaName = null;

            List<AddressDtailsEntity.ProvinceEntity.CityEntity> citys = null;
            if (province != null && province.size() > provinceIndex) {
                AddressDtailsEntity.ProvinceEntity provinceEntity = province.get(provinceIndex);
                citys = provinceEntity.City;
                provinceName = provinceEntity.Name;
            }
            List<AddressDtailsEntity.ProvinceEntity.AreaEntity> districts = null;
            if (citys != null && citys.size() > cityIndex) {
                AddressDtailsEntity.ProvinceEntity.CityEntity cityEntity = citys.get(cityIndex);
                districts = cityEntity.Area;
                cityName = cityEntity.Name;
            }

            if (districts != null && districts.size() > areaIndex) {
                AddressDtailsEntity.ProvinceEntity.AreaEntity areaEntity = districts.get(areaIndex);
                areaName = areaEntity.Name;
            }

            onAddressChangeListener.onAddressChange(provinceName, cityName, areaName);
        }
        cancel();
    }

    public void cancel() {
        popupWindow.dismiss();
    }

    public void setOnAddressChangeListener(OnAddressChangeListener onAddressChangeListener) {
        this.onAddressChangeListener = onAddressChangeListener;
    }
}