package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.base.WebViewBaseActivity;
import com.ascba.rebate.adapter.MessageLatestAdapter;
import com.ascba.rebate.beans.NewsBean;
import com.ascba.rebate.utils.TimeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ShopABarText;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yanzhenjie.nohttp.rest.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 系统消息
 */
public class SystemMsgActivity extends BaseNetActivity implements BaseNetActivity.Callback,
        SwipeRefreshLayout.OnRefreshListener {
    private ShopABarText shopBar;
    private Context context;
    private RecyclerView recyclerView;
    private List<NewsBean> beanList = new ArrayList<>();
    private MessageLatestAdapter adapter;
    private static String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_msg);
        context = this;
        requstData();
        initView();
    }

    private void requstData() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.getNoticeList, 0, true);
        request.add("article_class",id);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    public static void startIntent(Context context,String id) {
        Intent intent = new Intent(context, MessageLatestActivity.class);
        SystemMsgActivity.id =id;
        context.startActivity(intent);
    }

    private void initView() {
        shopBar = (ShopABarText) findViewById(R.id.shopBar);
        shopBar.setBtnEnable(false);
        shopBar.setCallback(new ShopABarText.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkBtn(View v) {
            }
        });

        initRefreshLayout();
        refreshLayout.setOnRefreshListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MessageLatestAdapter(R.layout.item_message_latest, beanList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(context, WebViewBaseActivity.class);
                intent.putExtra("name", beanList.get(position).getTitle());
                intent.putExtra("url", beanList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void initData(JSONObject dataObj) {
        try {
            JSONArray jsonArray = dataObj.getJSONArray("systemNotice");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject newsObject = jsonArray.getJSONObject(i);
                    NewsBean bean = new NewsBean();
                    bean.setTitle(newsObject.optString("title"));
                    String time = newsObject.optString("create_time");
                    time = TimeUtils.milliseconds2String((Long.parseLong(time) * 1000));
                    bean.setTime(time);
                    bean.setId(newsObject.optString("article_url"));
                    String date = getDate((Long.parseLong(newsObject.optString("create_time")) * 1000));
                    bean.setDate(date);
                    bean.setContent(newsObject.optString("contents"));
                    beanList.add(bean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        refreshLayout.setRefreshing(false);
        if (beanList.size() != 0) {
            beanList.clear();
        }
        initData(dataObj);
    }

    @Override
    public void handle404(String message) {
        getDm().buildAlertDialog(message);
    }

    @Override
    public void handleNoNetWork() {
        refreshLayout.setRefreshing(false);
    }


    @Override
    public void onRefresh() {
        if (beanList.size() != 0) {
            beanList.clear();
        }
        requstData();
    }


    private String getDate(long time) {
        Date date = new Date(time);
        // 获取日期实例
        Calendar calendar = Calendar.getInstance();
        // 将日历设置为指定的时间
        calendar.setTime(date);
        // 这里要注意，月份是从0开始。
        int month = calendar.get(Calendar.MONTH) + 1;
        // 获取天
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return month + "月" + day + "日";
    }
}
