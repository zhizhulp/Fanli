package com.ascba.rebate.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.ascba.rebate.utils.ConvertUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.beans.Province;
import cn.qqtheme.framework.picker.AddressPicker;

/**
 * Created by 李鹏 on 2017/04/21 0021.
 */

public class AddressPickTask extends AsyncTask<String, Void, ArrayList<Province>> {
    private Activity activity;
    private ProgressDialog dialog;
    private Callback callback;
    private String selectedProvince = "", selectedCity = "", selectedCounty = "";
    private boolean hideProvince = false;
    private boolean hideCounty = false;

    public AddressPickTask(Activity activity) {
        this.activity = activity;
    }

    public void setHideProvince(boolean hideProvince) {
        this.hideProvince = hideProvince;
    }

    public void setHideCounty(boolean hideCounty) {
        this.hideCounty = hideCounty;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(activity, null, "正在初始化数据...", true, true);
    }

    @Override
    protected ArrayList<Province> doInBackground(String... params) {
        if (params != null) {
            switch (params.length) {
                case 1:
                    selectedProvince = params[0];
                    break;
                case 2:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    break;
                case 3:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    selectedCounty = params[2];
                    break;
                default:
                    break;
            }
        }

        try {
            String json = ConvertUtils.inputStream2String(activity.getAssets().open("region.json"), "utf-8");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("province");

            return parseJson(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Province> parseJson(JSONArray p) {
        ArrayList<Province> data = new ArrayList<>();
        for (int i = 0; i < p.length(); i++) {
            JSONObject pObj = p.optJSONObject(i);
            int id = pObj.optInt("id");
            String name = pObj.optString("name");
            JSONArray c = pObj.optJSONArray("city");
            List<Province.City> citys = new ArrayList<>();
            for (int j = 0; j < c.length(); j++) {
                JSONObject dObj = c.optJSONObject(j);
                int cid = dObj.optInt("id");
                String cname = dObj.optString("name");
                JSONArray d = dObj.optJSONArray("district");
                List<Province.City.District> districts = new ArrayList<>();
                for (int k = 0; k < d.length(); k++) {
                    JSONObject kObj = d.optJSONObject(k);
                    int did = kObj.optInt("id");
                    String dname = kObj.optString("name");
                    Province.City.District district = new Province.City.District(did, dname);
                    districts.add(district);
                }
                Province.City city = new Province.City(cid, cname, districts);
                citys.add(city);
            }
            Province province = new Province(id, name, citys);
            data.add(province);
        }
        Log.d("AddressPickTask", data.get(0).toString());
        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<Province> result) {
        dialog.dismiss();
        if (result.size() > 0) {
            AddressPicker picker = new AddressPicker(activity, result);
            picker.setHideProvince(hideProvince);
            picker.setHideCounty(hideCounty);
            if (hideCounty) {
                picker.setColumnWeight(1 / 3.0, 2 / 3.0);//将屏幕分为3份，省级和地级的比例为1:2
            } else {
                picker.setColumnWeight(2 / 8.0, 3 / 8.0, 3 / 8.0);//省级、地级和县级的比例为2:3:3
            }
            picker.setSelectedItem(selectedProvince, selectedCity, selectedCounty);
            picker.setOnAddressPickListener(callback);
            picker.show();
        } else {
            callback.onAddressInitFailed();
        }
    }

    public interface Callback extends AddressPicker.OnAddressPickListener {

        void onAddressInitFailed();

    }

    public interface initData {
        void initState(boolean isFinish, ArrayList<Province> data);
    }
}

