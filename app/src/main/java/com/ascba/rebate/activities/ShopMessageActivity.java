package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.ShopMessageAdapter;
import com.ascba.rebate.beans.MessageBean;
import com.ascba.rebate.utils.StringUtils;
import com.ascba.rebate.utils.TimeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ShopABarText;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by 李鹏 on 2017/03/27 0027.
 * 消息
 */

public class ShopMessageActivity extends BaseNetActivity {

    private ShopABarText bar;
    private RecyclerView recyclerView;
    private Context context;
    private List<MessageBean> beanList = new ArrayList<>();
    private ShopMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_message);
        context = this;
        initView();
        requstData();
    }

    public static void startIntent(Context context) {
        Intent intent = new Intent(context, ShopMessageActivity.class);
        context.startActivity(intent);
    }

    private void initView() {
        bar = (ShopABarText) findViewById(R.id.shopBar);
        bar.setBtnEnable(false);
        bar.setCallback(new ShopABarText.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkBtn(View v) {
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ShopMessageAdapter(context, R.layout.item_message, beanList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                MessageBean messageBean = beanList.get(position);
                int type = messageBean.getType();
                String content = messageBean.getContent();
                if(content!=null){
                    if(type==1){//文章模板
                        SystemMsgActivity.startIntent(ShopMessageActivity.this,type);
                    }
                }

            }
        });
    }

    private void requstData() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.getNoticeClass, 1, true);
        executeNetWork(1, request, "请稍后");
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        JSONArray newsArray = dataObj.optJSONArray("systemNotice");
        if (newsArray != null && newsArray.length() > 0) {
            for (int i = 0; i < newsArray.length(); i++) {
                try {
                    JSONObject newsObject = newsArray.getJSONObject(i);
                    MessageBean bean = new MessageBean();
                    String id = newsObject.optString("id");
                    bean.setId(id);
                    int count = newsObject.optInt("article_count");
                    bean.setCount(count);
                    String title = newsObject.optString("title");
                    bean.setTitle(title);
                    int templet = newsObject.optInt("templet");
                    bean.setType(templet);
                    String img = UrlUtils.baseWebsite + newsObject.optString("pic");
                    bean.setImg(img);
                    JSONObject jsonObject = newsObject.optJSONObject("notice_index");
                    if (jsonObject != null && jsonObject.length() > 0) {
                        String timeUnix = jsonObject.optString("create_time");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd", Locale.getDefault());
                        String time = TimeUtils.milliseconds2String(Long.parseLong(timeUnix) * 1000, simpleDateFormat);
                        bean.setTime(time);
                        String content = jsonObject.optString("contents");
                        bean.setContent(content);
                    }else {
                        bean.setContent("暂无消息");
                    }
                    beanList.add(bean);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void mhandleFailed(int what, Exception e) {
        getDm().buildAlertDialog(getString(R.string.no_response));
    }
}
