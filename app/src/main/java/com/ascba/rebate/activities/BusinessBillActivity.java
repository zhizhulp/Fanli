package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.activities.offline_business.SellerOrderDetailActivity;
import com.ascba.rebate.beans.CashAccount;
import com.ascba.rebate.handlers.BillNetworker;
import com.ascba.rebate.utils.TimeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 商家流水
 */

public class BusinessBillActivity extends BaseBillActivity {
    private int  order_id;
    private int pay_type;
    private int is_money;
    public static final int CONE_RESULT=101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        type = 5;
        setBillNetworker(new BillNetworker() {
            @Override
            public void executeNetworkRefresh() {
                requestNetwork(UrlUtils.merchantWaterLog, 0);
            }

            @Override
            public void parseDataAndRefreshUI(int what, JSONObject dataObj, String message) {
                if (what == 0) {
                    JSONArray array = dataObj.optJSONArray("scoreList");
                    if (array != null && array.length() != 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.optJSONObject(i);
                            long create_time = obj.optLong("create_time");
                            String day = TimeUtils.milliseconds2String(create_time * 1000, new SimpleDateFormat("MM.dd"));
                            String time = TimeUtils.milliseconds2String(create_time * 1000, new SimpleDateFormat("HH:mm"));
                            String score = obj.optString("money_num");
                            String pic = obj.optString("pic");
                            String filterText = obj.optString("remarks");
                            String month = obj.optString("month");
                            String year = obj.optString("year");
                            String is_money_tip = obj.optString("is_money_tip");
                             is_money = obj.optInt("is_money");
                           // pay_type=obj.optInt("pay_type");
                             order_id=obj.optInt("fivepercent_log_id");


                            if (i != 0) {
                                if (yearLast != Integer.parseInt(year)) {
                                    addHead(month, year);
                                } else {
                                    if (monthLast != Integer.parseInt(month)) {
                                        addHead(month, year);
                                    }
                                }

                            } else {
                                if (loadmore) {
                                    if (yearLast != Integer.parseInt(year)) {
                                        addHead(month, year);
                                    } else {
                                        if (monthLast != Integer.parseInt(month)) {
                                            addHead(month, year);
                                        }
                                    }
                                } else {
                                    addHead(month, year);
                                }

                            }
                            yearLast = Integer.parseInt(year);
                            monthLast = Integer.parseInt(month);

                            CashAccount ca = new CashAccount();
                            ca.setDay(day);
                            ca.setTime(time);
                            ca.setMoney(score);
                            ca.setImgUrl(UrlUtils.baseWebsite + pic);
                            ca.setFilterText(filterText);
                            ca.setItemType(1);
                            ca.setIs_money_tip(is_money_tip);
                            ca.setFivepercent_log_id(order_id);
                            data.add(ca);
                        }
                        billAdapter.notifyDataSetChanged();
                    } else {
                        getViewHead().setVisibility(View.GONE);
                    }
                }
            }
        });
        super.onCreate(savedInstanceState);
        setMoneyBar("流水记录", false, null);

        billAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(data.get(position).getItemType()!=0){//不是头部就自己跳入

                    Intent intent=new Intent(BusinessBillActivity.this, SellerOrderDetailActivity.class);
                    intent.putExtra("order_id",data.get(position).getFivepercent_log_id());
                    intent.putExtra("into_type",1);
                    startActivityForResult(intent,CONE_RESULT);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CONE_RESULT:
                if(resultCode==RESULT_OK){
                    reset();
                    requestNetwork(UrlUtils.merchantWaterLog, 0);
                }
                break;
        }
    }


    private void addHead(String month, String year) {
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

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        super.mhandle200Data(what, object, dataObj, message);

    }

    private void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        if (what == 0) {
            request.add("now_page", now_page);
        }
        executeNetWork(what, request, "请稍后");
    }
}
