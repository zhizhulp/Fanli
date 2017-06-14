package com.ascba.rebate.fragments.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.ASKCollegeActivity;
import com.ascba.rebate.activities.MessageLatestActivity;
import com.ascba.rebate.activities.ShopMessageActivity;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.base.WebViewBaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.main.MainActivity;
import com.ascba.rebate.activities.main_page.RecQRActivity;
import com.ascba.rebate.activities.offline_business.OfflinePayActivity;
import com.ascba.rebate.activities.shop.ShopActivity;
import com.ascba.rebate.adapter.HomePageAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.beans.HomePageMultiItemItem;
import com.ascba.rebate.beans.NewsBean;
import com.ascba.rebate.beans.VideoBean;
import com.ascba.rebate.fragments.base.BaseNetFragment;
import com.ascba.rebate.qr.CaptureActivity;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.SharedPreferencesUtil;
import com.ascba.rebate.utils.TimeUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MsgView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadQueue;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.ServerError;
import com.yanzhenjie.nohttp.error.StorageReadWriteError;
import com.yanzhenjie.nohttp.error.StorageSpaceNotEnoughError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.ascba.rebate.activities.main.MainActivity.CAIFU;
import static com.ascba.rebate.activities.main.MainActivity.HOMEPAGE;
import static com.ascba.rebate.activities.main.MainActivity.REQUEST_LOGIN_CAIFU;

/**
 * Created by 李鹏 on 2017/03/10 0010.
 * 首页
 */

public class HomePageFragment extends BaseNetFragment implements BaseNetFragment.Callback {

    private static final int REQUEST_LOGIN = 0;
    private static final int POLICY = 1;
    private Context context;

    private RecyclerView recylerview;
    private HomePageAdapter homePageAdapter;

    /*
     * 头部
     */
    private RelativeLayout homepage_head;
    private View homepage_head_line;

    private int mDistanceY = 0;
    private TextView floatButton;

    private FrameLayout btnAdd, btnMessage;
    private ImageView imgAdd;
    private List<HomePageMultiItemItem> items = new ArrayList<>();
    private PopupWindow popupWindow;
    private int finalScene;
    private static final long newTime = 24 * 60 * 60 * 1000;//新文章变为旧文章的时间(ms)
    private MsgView msgView;
    private DownloadQueue downloadQueue;
    private ProgressDialog pD;
    private boolean isFirstComing=true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        initView(view);
        requestData(UrlUtils.index, 0);
    }

    private void requestData(String url, int scene) {
        finalScene = scene;
        Request<JSONObject> request = null;
        if (scene == 0) {
            request = buildNetRequest(url, 0, false);
            request.add("version_code",getPackageVersion());
        } else if (scene == 1) {
            request = buildNetRequest(url, 0, true);
        } else if (scene == 2) {
            request = buildNetRequest(url, 0, true);
        }
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    private void initView(View view) {
        /**
         * 悬浮球
         */
        floatButton = (TextView) view.findViewById(R.id.floatButton);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("点我了");
            }
        });
        String text = "本月任务\n已完成30%";
        SpannableString textSpan = new SpannableString(text);
        textSpan.setSpan(new RelativeSizeSpan(1.0f), 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textSpan.setSpan(new RelativeSizeSpan(0.7f), 4, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        floatButton.setText(textSpan);
        floatButton.setVisibility(View.GONE);

        /**
         * 刷新
         */
        initRefreshLayout(view);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData(UrlUtils.index, 0);
            }
        });

        /**
         * 头部信息
         */
        homepage_head = (RelativeLayout) view.findViewById(R.id.homepage_head);
        homepage_head_line = view.findViewById(R.id.homepage_head_view);
        //+号
        btnAdd = (FrameLayout) view.findViewById(R.id.homepage_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
            }
        });
        imgAdd = (ImageView) view.findViewById(R.id.homepage_img_add);

        //消息
        btnMessage = (FrameLayout) view.findViewById(R.id.homepage_message);
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopMessageActivity.startIntent(context);
            }
        });
        msgView = (MsgView) view.findViewById(R.id.msgView);


        /**
         * 初始化recylerview
         */
        recylerview = (RecyclerView) view.findViewById(R.id.homepage_recylerview);


        /**
         * 滑动标题栏渐变
         */
        recylerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //滑动的距离
                mDistanceY += dy;
                //toolbar的高度
                int toolbarHeight = homepage_head.getBottom();
                float maxAlpha = 229.5f;//最大透明度80%
                //当滑动的距离 <= toolbar高度的时候，改变Toolbar背景色的透明度，达到渐变的效果
                if (mDistanceY <= toolbarHeight) {
                    float scale = (float) mDistanceY / toolbarHeight;
                    float alpha = scale * maxAlpha;
                    homepage_head.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    homepage_head_line.setAlpha(alpha);
                }
            }
        });

        recylerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomePageMultiItemItem item = items.get(position);
                //新闻详情
                if (item.getItemType() == HomePageMultiItemItem.TYPE12) {
                    Intent intent = new Intent(context, WebViewBaseActivity.class);
                    intent.putExtra("name", item.getBean().getTitle());
                    intent.putExtra("url", item.getBean().getUrl());
                    startActivity(intent);
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.homepage_btn_speedmon:
                        startActivity(new Intent(getActivity(), ShopActivity.class));
                        break;
                    case R.id.homepage_btn_makemon://切到财富
                        MainActivity activity = (MainActivity) getActivity();
                        if (AppConfig.getInstance().getInt("uuid", -1000) != -1000) {//登录
                            activity.selFrgByPos(CAIFU);
                            BaseNetActivity.setRequestCode(REQUEST_LOGIN_CAIFU);
                            activity.getAppTabs().statusChaByPosition(CAIFU,HOMEPAGE);
                            activity.getAppTabs().setFilPos(CAIFU);
                            MyApplication.isLoad = true;
                        } else {
                            Intent intent = new Intent(activity, LoginActivity.class);
                            activity.startActivityForResult(intent, REQUEST_LOGIN_CAIFU);
                        }
                        break;
                    case R.id.homepage_btn_policy:
                        //创业扶持
                        if (AppConfig.getInstance().getInt("uuid", -1000) != -1000) {//登录
                            requestData(UrlUtils.getDataUrl, 2);
                        } else {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivityForResult(intent, POLICY);
                        }
                        break;
                    case R.id.homepage_btn_college:
                        //ASK商学院
                        showToast("暂未开放");
                        /*Intent college = new Intent(getActivity(), ASKCollegeActivity.class);
                        startActivity(college);*/
                        break;
                    case R.id.homepage_text_more_news:
                        //最新动态——更多
                        MessageLatestActivity.startIntent(context);
                        break;
                }
            }
        });
    }

    // 弹窗
    private void showPopWindow() {
        if (popupWindow == null) {
            View view = LayoutInflater.from(context).inflate(R.layout.popwindow_homepage, null);

            //付款
            LinearLayout btnPay = (LinearLayout) view.findViewById(R.id.pop_hm_pay);
            btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), CaptureActivity.class));
                    popupWindow.dismiss();
                }
            });

            //收款
            LinearLayout btnRece = (LinearLayout) view.findViewById(R.id.pop_hm_rece);
            btnRece.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    if (AppConfig.getInstance().getInt("uuid", -1000) != -1000) {//登录
                        requestData(UrlUtils.receivables, 1);
                    } else {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivityForResult(intent, REQUEST_LOGIN);
                    }
                }
            });

            //推广码
            LinearLayout btnCode = (LinearLayout) view.findViewById(R.id.pop_hm_code);
            btnCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), RecQRActivity.class));
                    popupWindow.dismiss();
                }
            });

            popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            // 设置可以获取焦点
            popupWindow.setFocusable(true);
            // 设置可以触摸弹出框以外的区域
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            // 更新popupwindow的状态
            popupWindow.update();
        }
        // 以下拉的方式显示，并且可以设置显示的位置
        popupWindow.showAtLocation(imgAdd, Gravity.TOP | Gravity.RIGHT, ScreenDpiUtils.dip2px(context, 15), ScreenDpiUtils.dip2px(context, 75));

    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if (finalScene == 0) {
            updateApp(dataObj.optJSONObject("version"));
            clearData();
            stopRefresh();
            initPagerTurn(dataObj);//广告轮播

            //花钱赚钱
            items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE2, R.layout.home_page_makemoney));
            //ASK商学院  创业扶持
            items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE3, R.layout.home_page_college));
            //分割线
            //items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE4, R.layout.item_divider1));
            //券购商城
            //items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE5, R.layout.home_page_more_shop, "券购商城"));
            //分割线
            //items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE4, R.layout.item_divider1));
            //全球券购 天天特价 品牌精选
            //items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE6, R.layout.home_page_comm));
            //宽分割线
            items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE7, R.layout.goods_details_cuttingline_wide));

            //视频
            initVideoTurn(dataObj);

            initNews(dataObj);

            //消息数量
            int messageNum = dataObj.optInt("msg_tip_count");
            if (messageNum > 0) {
                msgView.setIsIndicator(true);
            } else {
                msgView.setIsIndicator(false);
            }
            initAdapterAndRefresh();
        } else if (finalScene == 1) {
            JSONObject obj = dataObj.optJSONObject("receivables");
            String url = obj.optString("url");
            Intent intent = new Intent(getActivity(), WebViewBaseActivity.class);
            intent.putExtra("name", "收款");
            intent.putExtra("url", url);
            startActivity(intent);
        } else if (finalScene == 2) {
            JSONObject obj = dataObj.optJSONObject("receivables");
            String url = obj.optString("url");
            Intent intent = new Intent(getActivity(), WebViewBaseActivity.class);
            intent.putExtra("name", "数据统计");
            intent.putExtra("url", url);
            startActivity(intent);
        }
    }

    private void updateApp(JSONObject verObj) {
        if(isFirstComing){
            int isUpdate = verObj.optInt("isUpdate");
            if (isUpdate == 1) {
                String apk_url = verObj.optString("apk_url");
                downLoadApp(apk_url);
            }
        }
        isFirstComing=false;
    }

    /*
    解析新闻内容
     */
    private void initNews(JSONObject dataObj) {
        items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE4, R.layout.item_divider1));
        //最新动态
        items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE10, R.layout.home_page_more_news, "最新动态"));
        //分割线
        items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE4, R.layout.item_divider1));

        JSONArray jsonArray = dataObj.optJSONArray("article_list");
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.optString("title");
                    String id = jsonObject.optString("id");
                    String time = jsonObject.optString("create_time");
                    long create_time = Long.parseLong(time);
                    time = TimeUtils.milli2String((create_time * 1000));
                    String url = jsonObject.optString("article_url");
                    NewsBean newsBean = new NewsBean(id, title, time, url);
                    newsBean.setIcon((System.currentTimeMillis() - (create_time * 1000)) <= newTime);
                    items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE12, newsBean, R.layout.home_page_news2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
   解析视频消息
    */
    private void initVideoTurn(JSONObject dataObj) {
        JSONArray video_list = dataObj.optJSONArray("video_list");
        if (video_list != null && video_list.length() != 0) {
            //ASK资讯
            items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE8, R.layout.home_page_title, "ASK资讯"));
            //分割线
            items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE4, R.layout.item_divider1));
            List<VideoBean> videoBeans = new ArrayList<>();
            for (int i = 0; i < video_list.length(); i++) {
                JSONObject obj = video_list.optJSONObject(i);
                String img = UrlUtils.baseWebsite + obj.optString("thumb");
                String video_url = obj.optString("video_url");
                String title = obj.optString("title");
                VideoBean videoBean = new VideoBean(img, video_url, title);
                videoBeans.add(videoBean);
            }
            items.add(new HomePageMultiItemItem(videoBeans, HomePageMultiItemItem.TYPE9, R.layout.home_page_videopage));
        }

    }

    /*
    广告轮播
     */
    private void initPagerTurn(JSONObject dataObj) {
        JSONArray banner = dataObj.optJSONArray("banner");
        if (banner != null && banner.length() != 0) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < banner.length(); i++) {
                String string = banner.optString(i);
                list.add(UrlUtils.baseWebsite + string);
            }
            items.add(new HomePageMultiItemItem(HomePageMultiItemItem.TYPE1, R.layout.home_page_viewpager, list));
        }

    }

    public void stopRefresh() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    private void clearData() {
        if (items.size() != 0) {
            items.clear();
        }
    }

    private void initAdapterAndRefresh() {
        if (homePageAdapter == null) {
            homePageAdapter = new HomePageAdapter(items, context);
            recylerview.setLayoutManager(new LinearLayoutManager(context));
            recylerview.setAdapter(homePageAdapter);
        } else {
            homePageAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void handleReqFailed() {
        stopRefresh();
    }

    @Override
    protected void mhandleFinish(int what) {
        super.mhandleFinish(what);

    }

    @Override
    public void handle404(String message, JSONObject dataObj) {
        stopRefresh();
    }

    @Override
    public void handleReLogin() {
        stopRefresh();
    }

    @Override
    public void handleNoNetWork() {
        stopRefresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOGIN:
                if (resultCode == Activity.RESULT_OK) {
                    requestData(UrlUtils.receivables, 1);
                }
                break;
            case POLICY:
                if (resultCode == Activity.RESULT_OK) {
                    requestData(UrlUtils.getDataUrl, 2);
                }
                break;
        }
    }
    private String getPackageVersion() {
        PackageManager packageManager = getActivity().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void downLoadApp(String url) {
        createDownloadDialog(url);
    }

    /**
     * 下载监听
     */
    private DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onStart(int what, boolean isResume, long beforeLength, Headers headers, long allCount) {
            pD.show();
        }

        @Override
        public void onProgress(int what, int progress, long fileCount, long speed) {
            pD.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
            pD.setProgress(progress);
        }

        @Override
        public void onDownloadError(int what, Exception exception) {

            if (exception instanceof ServerError) {
                Toast.makeText(getActivity(), "后台错误", Toast.LENGTH_SHORT).show();
            } else if (exception instanceof NetworkError) {
                Toast.makeText(getActivity(), "网络有问题", Toast.LENGTH_SHORT).show();
            } else if (exception instanceof StorageReadWriteError) {
                Toast.makeText(getActivity(), "无读写权限", Toast.LENGTH_SHORT).show();
            } else if (exception instanceof StorageSpaceNotEnoughError) {
                Toast.makeText(getActivity(), "没有足够空间", Toast.LENGTH_SHORT).show();
            } else if (exception instanceof TimeoutError) {
                Toast.makeText(getActivity(), "请求超时", Toast.LENGTH_SHORT).show();
            } else if (exception instanceof UnKnownHostError) {
                Toast.makeText(getActivity(), "未知主机", Toast.LENGTH_SHORT).show();
            } else if (exception instanceof URLError) {
                Toast.makeText(getActivity(), "网址错误", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "未知错误", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFinish(int what, String filePath) {
            pD.setMessage("下载成功");
            pD.dismiss();
            if (filePath != null) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT > 23) {
                    Uri uri = FileProvider.getUriForFile(getActivity(), "com.ascba.rebate.provider", new File(filePath));
                    i.setDataAndType(uri, "application/vnd.android.package-archive");
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    getActivity().startActivity(i);
                } else {
                    i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
                    getActivity().startActivity(i);
                }

                SharedPreferencesUtil.putBoolean(getActivity(), SharedPreferencesUtil.FIRST_OPEN, true);
            }
        }

        @Override
        public void onCancel(int what) {

        }

    };

    private void createDownloadDialog(final String url) {
        pD = new ProgressDialog(getContext(), R.style.dialog);
        pD.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pD.setMessage("您有新的更新，点击下载");
        pD.setCancelable(false);
        pD.setButton(DialogInterface.BUTTON_POSITIVE, "下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadQueue = NoHttp.newDownloadQueue();
                DownloadRequest downloadRequest = NoHttp.createDownloadRequest(url, getDiskCacheDir().getPath(), true);
                downloadQueue.add(0, downloadRequest, downloadListener);
            }
        });
        /*pD.setButton(DialogInterface.BUTTON_NEGATIVE, "后台下载", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });*/
        pD.show();
    }
    public File getDiskCacheDir() {
        File dst;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dst = new File(Environment.getExternalStorageDirectory(), "com.ascba.rebate");
            if (!dst.exists()) {
                dst.mkdirs();
            }

        } else {
            dst = getContext().getFilesDir();
        }
        return dst;
    }
}
