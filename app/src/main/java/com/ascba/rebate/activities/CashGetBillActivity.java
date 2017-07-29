package com.ascba.rebate.activities;

import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.beans.CashAccount;
import com.ascba.rebate.handlers.BillNetworker;
import com.ascba.rebate.utils.TimeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by 李平 on 2017/4/28 0028.13:51
 */

public class CashGetBillActivity extends BaseBillActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        type=3;
        setBillNetworker(new BillNetworker() {
            @Override
            public void executeNetworkRefresh() {
                requestNetwork(UrlUtils.moneyTillsLog,0);
            }
            @Override
            public void parseDataAndRefreshUI(int what, JSONObject dataObj, String message) {
                if(what==0){
                    JSONArray array = dataObj.optJSONArray("scoreList");
                    if(array!=null && array.length()!=0){
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.optJSONObject(i);
                            long create_time = obj.optLong("create_time");
                            String day = TimeUtils.milliseconds2String(create_time * 1000, new SimpleDateFormat("MM.dd"));
                            String time = TimeUtils.milliseconds2String(create_time * 1000, new SimpleDateFormat("HH:mm"));
                            String score = obj.optString("tills_money_num");
                            String pic = obj.optString("pic");
                            String filterText = obj.optString("remarks");
                            String month = obj.optString("month");
                            String year = obj.optString("year");
                            if (i != 0) {
                                if (yearLast != Integer.parseInt(year)) {
                                    addHead(month,year);

                                } else {
                                    if (monthLast != Integer.parseInt(month)) {
                                        addHead(month,year);
                                    }
                                }

                            } else {
                                if(loadmore){
                                    if (yearLast != Integer.parseInt(year)) {
                                        addHead(month,year);
                                    } else {
                                        if (monthLast != Integer.parseInt(month)) {
                                            addHead(month,year);
                                        }
                                    }
                                }else {
                                    addHead(month,year);
                                }

                            }
                            yearLast = Integer.parseInt(year);
                            monthLast = Integer.parseInt(month);

                            CashAccount ca = new CashAccount();
                            ca.setDay(day);
                            ca.setTime(time);
                            ca.setMoney(score);
                            ca.setImgUrl(UrlUtils.getNewUrl(pic) );
                            ca.setFilterText(filterText);
                            ca.setItemType(1);
                            data.add(ca);
                        }
                        billAdapter.notifyDataSetChanged();
                    }else {
                        getViewHead().setVisibility(View.GONE);
                    }
                }
            }
        });
        super.onCreate(savedInstanceState);
        setMoneyBar("提现账单",false,null);
    }

    private void addHead(String month,String year) {
        CashAccount ca = new CashAccount();
        int calendarYear = dateAndTime.get(Calendar.YEAR);
        if (!(calendarYear + "").equals(year)) {
            ca.setMonth(year + "年" + month + "月");
        } else {
            ca.setMonth(month + "月");
        }
        ca.setItemType(0);
        data.add(ca);
    }

    private void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        if(what==0){
            request.add("now_page",now_page);
        }
        executeNetWork(what,request,"请稍后");
    }
}
