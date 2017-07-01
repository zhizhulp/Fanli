package com.ascba.rebate.activities.shop;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.LinerGoodsListAdapter;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.utils.StringUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.utils.ViewUtils;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShopSearchActivity extends BaseNetActivity implements View.OnClickListener {

    private EditText etSearch;
    private RecyclerView rv;
    private List<Goods> beanList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_search);
        initViews();
        requestNetwork(UrlUtils.searchGoods,0,etSearch.getText().toString());
    }

    private void initViews() {
        findViewById(R.id.back_icon).setOnClickListener(this);
        findViewById(R.id.tv_search).setOnClickListener(this);
        etSearch = ((EditText) findViewById(R.id.goods_et_search));

        loadRequestor=new LoadRequestor() {
            @Override
            public void loadMore() {
                requestNetwork(UrlUtils.searchGoods,0,etSearch.getText().toString());
            }

            @Override
            public void pullToRefresh() {
                requestNetwork(UrlUtils.searchGoods,0,etSearch.getText().toString());
            }
        };
        initRefreshLayout();
        initLoadMoreRequest();
        initRecyclerView();
    }

    private void requestNetwork(String url,int what,String keywords){
        if(StringUtils.isEmpty(keywords)){
            showToast("请输入要搜索的关键字");
            return;
        }
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        request.add("now_page",now_page);
        request.add("keywords",keywords);
        executeNetWork(what,request,"请稍后");
    }

    private void initRecyclerView() {
        rv = ((RecyclerView) findViewById(R.id.recyclerview));
        rv.setLayoutManager(new LinearLayoutManager(this));
        baseAdapter=new LinerGoodsListAdapter(R.layout.goods_list_layout_linear,beanList);
        baseAdapter.setEmptyView(ViewUtils.getEmptyView(this,"暂无商品信息"));
        rv.setAdapter(baseAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_icon://返回
                finish();
                break;
            case R.id.tv_search://搜索

                break;
        }
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        super.mhandle200Data(what, object, dataObj, message);
        if(what==0){
            refreshGoodsList(dataObj);
        }
    }

    private void refreshGoodsList(JSONObject dataObj) {
        JSONArray array = dataObj.optJSONArray("mallGoods");
        if(array!=null && array.length() >0){
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.optJSONObject(i);
                Goods goods=new Goods();
                goods.setTitleId(object.optInt("id"));
                goods.setGoodsTitle(object.optString("title"));
            }

        }
    }
}
