package com.qlqwgw.fanli.fragments.main;

import com.google.gson.Gson;
import com.qlqwgw.fanli.beans.Business;
import com.zhy.http.okhttp.callback.Callback;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 网络获取用户列表
 */

public abstract class ListUserCallback extends Callback<List<Business>> {


    @Override
    public List<Business> parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        List<Business> users = new Gson().fromJson(string, List.class);
        return users;
    }

}
