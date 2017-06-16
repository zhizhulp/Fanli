package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.ascba.rebate.view.loadmore.CustomLoadMoreView;
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

import static com.chad.library.adapter.base.loadmore.LoadMoreView.STATUS_DEFAULT;

/**
 * Created by 李平on 2017/03/31 0031.
 * 主页-最新公告
 */

public class MessageChildActivity extends BaseNetActivity implements BaseNetActivity.Callback,
        SwipeRefreshLayout.OnRefreshListener {

    private ShopABarText shopBar;
    private Context context;
    private RecyclerView recyclerView;
    private List<NewsBean> beanList = new ArrayList<>();
    private MessageLatestAdapter adapter;
    private int nowPage = 1, totalPage;

    private static final int LOAD_MORE_END = 0;
    private static final int LOAD_MORE_ERROR = 1;
    private boolean isRefresh = true;//true 下拉刷新 false 上拉加载
    private CustomLoadMoreView loadMoreView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_MORE_END:
                    if (adapter != null) {
                        adapter.loadMoreEnd(false);
                    }

                    break;
                case LOAD_MORE_ERROR:
                    if (adapter != null) {
                        adapter.loadMoreFail();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_latest);
        context = this;
        requstData();
        initView();
    }

    private void requstData() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.moreNews, 0, false);
        request.add("now_page", nowPage);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    public static void startIntent(Context context) {
        Intent intent = new Intent(context, MessageChildActivity.class);
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
                intent.putExtra("name", "公告详情");
                intent.putExtra("url", beanList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void initData(JSONObject dataObj) {
        try {
            JSONArray jsonArray = dataObj.getJSONArray("article_list");
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
                    bean.setContent(newsObject.optString("description"));
                    beanList.add(bean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }


    private void initLoadMore() {

        if (isRefresh) {
            isRefresh = false;
        }
        if (loadMoreView == null) {
            loadMoreView = new CustomLoadMoreView();
            adapter.setLoadMoreView(loadMoreView);
        }
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (nowPage > totalPage && totalPage != 0) {
                    handler.sendEmptyMessage(LOAD_MORE_END);
                } else {
                    requstData();
                }
            }
        });


    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        refreshLayout.setRefreshing(false);
        if (adapter != null) {
            adapter.loadMoreComplete();
        }
        if (loadMoreView != null) {
            loadMoreView.setLoadMoreStatus(STATUS_DEFAULT);
        }
        //分页
        getPageCount(dataObj);

        if (isRefresh) {//下拉刷新
            if (beanList.size() != 0) {
                beanList.clear();
            }
            initData(dataObj);
            initLoadMore();
        } else {//上拉加载
            initData(dataObj);
        }
    }

    @Override
    public void handle404(String message) {
    }

    @Override
    public void handleNoNetWork() {
        refreshLayout.setRefreshing(false);
        handler.sendEmptyMessage(LOAD_MORE_ERROR);
    }


    @Override
    public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;
        }
        nowPage = 1;
        if (beanList.size() != 0) {
            beanList.clear();
        }
        requstData();
    }


    private void getPageCount(JSONObject dataObj) {
        totalPage = dataObj.optInt("total_page");
        nowPage++;
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
