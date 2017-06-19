package com.ascba.rebate.activities.base;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.loadmore.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import static com.chad.library.adapter.base.loadmore.LoadMoreView.STATUS_DEFAULT;

/**
 * Created by 李鹏 on 2017/04/20 0020.
 * 权限 吐司 下拉刷新 上拉加载
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected PermissionCallback requestPermissionAndBack;
    protected SwipeRefreshLayout refreshLayout;
    protected CustomLoadMoreView loadMoreView;
    private static final int LOAD_MORE_END = 2017;
    private static final int LOAD_MORE_ERROR = 2018;
    private BaseQuickAdapter adapter;
    private int now_page = 1;//当前页数 用于分页
    private int total_page = 0;//总页数
    protected boolean isRefreshing = true;//true 下拉刷新 false 加载更多
    @SuppressLint("HandlerLeak")
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

    protected abstract void loadMore();

    protected abstract void pullToRefresh();

    //初始化下拉刷新
    protected void initRefreshLayout() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        //改变加载显示的颜色
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshing = true;
                pullToRefresh();
            }
        });
    }

    //初始化上拉加载
    protected void initLoadMore() {
        if (loadMoreView == null) {
            loadMoreView = new CustomLoadMoreView();
            adapter.setLoadMoreView(loadMoreView);
        }
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isRefreshing = false;
                if (now_page > total_page && total_page != 0) {
                    handler.sendEmptyMessage(LOAD_MORE_END);
                } else if (total_page == 0) {
                    handler.sendEmptyMessage(LOAD_MORE_END);
                } else {
                    loadMore();
                }
            }
        });
    }

    //停止下拉刷新rere
    protected void stopRefresh() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    //停止上拉加载
    protected void stopLoadMore() {
        if (adapter != null) {
            adapter.loadMoreComplete();
        }
        if (loadMoreView != null) {
            loadMoreView.setLoadMoreStatus(STATUS_DEFAULT);
        }
    }
    //重置页数
    protected void resetPage(){
        now_page=1;
        total_page=0;
    }

    protected void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    public interface PermissionCallback {
        void requestPermissionAndBack(boolean isOk);
    }

    /**
     * 申请权限
     */
    protected void checkAndRequestAllPermission(String[] permissions, PermissionCallback requestPermissionAndBack) {
        this.requestPermissionAndBack = requestPermissionAndBack;
        if (permissions == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(permissions, 1);
        }
    }

    public void setRequestPermissionAndBack(PermissionCallback requestPermissionAndBack) {
        this.requestPermissionAndBack = requestPermissionAndBack;
    }

    /**
     * 申请权限
     */
    protected void checkAndRequestAllPermission(String[] permissions) {
        if (permissions == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(permissions, 1);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] per,
                                           @NonNull int[] grantResults) {
        boolean isAll = true;
        for (int i = 0; i < per.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isAll = false;
                break;
            }
        }
        if (!isAll) {
            showToast(getResources().getString(R.string.no_permission));
        }
        if (requestPermissionAndBack != null) {
            requestPermissionAndBack.requestPermissionAndBack(isAll);//isAll 用户是否拥有所有权限
        }
        super.onRequestPermissionsResult(requestCode, per, grantResults);
    }
}
