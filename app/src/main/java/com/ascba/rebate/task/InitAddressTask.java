package com.ascba.rebate.task;

import android.app.Activity;
import android.os.AsyncTask;

import com.ascba.rebate.utils.ConvertUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.beans.Province;


/**
 * Created by 李鹏 on 2017/04/22 0022.
 */

public class InitAddressTask extends AsyncTask<Void, Void, ArrayList<Province>> {

    private Activity activity;
    private InitData initData;
    private int[] regions;
    private Province province;
    private Province.City city;
    private Province.City.District district;

    public InitAddressTask(Activity activity) {
        this.activity = activity;
    }

    public void setInitData(InitData initData) {
        this.initData = initData;
    }

    public void setRegionId(int arg0, int arg1, int arg2) {
        regions = new int[]{arg0,arg1,arg2};
    }

    @Override
    protected ArrayList<Province> doInBackground(Void... params) {

        try {
            String json = ConvertUtils.inputStream2String(activity.getAssets().open("region.json"), "utf-8");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("province");
            ArrayList<Province> provinces = parseJson(jsonArray);
            return provinces;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Province> provinces) {
        if (provinces.size() > 0) {
            initData.onSuccess(provinces, province, city, district);
        } else {
            initData.onFailed();
        }
    }

    public interface InitData {
        void onSuccess(ArrayList<Province> data, Province arg0, Province.City arg1, Province.City.District arg2);

        void onFailed();
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
                    if (regions != null && did == regions[2]) {
                        this.district = district;
                    }
                    districts.add(district);
                }
                Province.City city = new Province.City(cid, cname, districts);
                if (regions != null && cid == regions[1]) {
                    this.city = city;
                }
                citys.add(city);
            }
            Province province = new Province(id, name, citys);
            if (regions != null && id == regions[0]) {
                this.province = province;
            }
            data.add(province);
        }
        return data;
    }
}
