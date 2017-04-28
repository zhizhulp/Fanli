package com.ascba.rebate.handlers;

import org.json.JSONObject;

/**
 * Created by 李平 on 2017/4/28 0028.9:46
 */

public interface BillNetworker {
    void executeNetworkRefresh();//数据请求
    void parseDataAndRefreshUI(int what,  JSONObject dataObj, String message);//数据解析
}
