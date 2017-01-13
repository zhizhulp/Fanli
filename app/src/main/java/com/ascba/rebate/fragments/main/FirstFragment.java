package com.ascba.rebate.fragments.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.BusinessDetailsActivity;
import com.ascba.rebate.activities.CityList;
import com.ascba.rebate.activities.HotActivity;
import com.ascba.rebate.activities.PayActivity;
import com.ascba.rebate.activities.RecQRActivity;
import com.ascba.rebate.activities.SweepActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.password_loss.PasswordLossWithCodeActivity;
import com.ascba.rebate.activities.splash.SplashActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.beans.Business;
import com.ascba.rebate.fragments.base.BaseFragment;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.MySqliteOpenHelper;
import com.ascba.rebate.utils.NetUtils;
import com.ascba.rebate.utils.SharedPreferencesUtil;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ScrollViewWithListView;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.download.DownloadListener;
import com.yolanda.nohttp.download.DownloadQueue;
import com.yolanda.nohttp.download.DownloadRequest;
import com.yolanda.nohttp.error.NetworkError;
import com.yolanda.nohttp.error.ServerError;
import com.yolanda.nohttp.error.StorageReadWriteError;
import com.yolanda.nohttp.error.StorageSpaceNotEnoughError;
import com.yolanda.nohttp.error.TimeoutError;
import com.yolanda.nohttp.error.URLError;
import com.yolanda.nohttp.error.UnKnownHostError;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
/**
 * 扫一扫主页
 */
public class FirstFragment extends BaseFragment {

    private ScrollViewWithListView recBusiness;
    private List<ImageView> imageList;
    private RecBusinessAdapter mAdapter;
    private List<Business> mList;
    private TextView location_text;
    private ViewPager vp;
    private int msgWhat = 0;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    vp.setCurrentItem(vp.getCurrentItem() + 1);//收到消息，指向下一个页面
                    handler.sendEmptyMessageDelayed(msgWhat, 2500);//2S后在发送一条消息，由于在handleMessage()方法中，造成死循环。
                    break;
                case 1:
                    pD.setProgress(msg.arg1);
                    break;
            }

        }
    };
    private View goBusinessList;
    private ImageView goSweepActiviIcon;
    private ImageView imGoRec;
    private ProgressDialog pD;
    private DownloadQueue downloadQueue;
    private TextView tvAllScore;
    private SuperSwipeRefreshLayout refreshLayout;
    private TextView tvRedScore;
    private DialogManager dm;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_fragment, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(msgWhat, 2500);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler.hasMessages(msgWhat)) {
            handler.removeMessages(msgWhat);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(downloadQueue!=null){
            downloadQueue.cancelAll();
        }
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dm=new DialogManager(getActivity());
        tvAllScore = ((TextView) view.findViewById(R.id.score_all));
        tvRedScore = ((TextView) view.findViewById(R.id.tv_red_score));
        initRecBusiness(view);//初始化ListView
        initViewPager(view);//初始化viewpager
        initLocation(view);//地址显示
        initRefreshLayout(view);//界面刷新
        goHotList(view);//进入热门推荐的页面
        goSweepActivity(view);//进入扫一扫的界面
        goRecommend(view);//进入推荐页面
        requestMainData();
    }

    private void requestMainData() {

        String version = getPackageVersion();
        Request<JSONObject> request = buildNetRequest(UrlUtils.index, 0, true);
        request.add("version_code",version);
        executeNetWork(request,"请稍候");
        setCallback(new Callback() {
            @Override
            public void handle200Data(JSONObject dataObj, String message) {
                refreshLayout.setRefreshing(false);
                JSONObject rebate = dataObj.optJSONObject("rebate");
                int white_score = rebate.optInt("white_score");
                int red_score = rebate.optInt("red_score");
                tvAllScore.setText(white_score+"");
                tvRedScore.setText(red_score+"");
                //app更新
                JSONObject verObj = dataObj.optJSONObject("version");
                int isUpdate = verObj.optInt("isUpdate");
                if(isUpdate==1){
                    String apk_url = verObj.optString("apk_url");
                    downLoadApp(apk_url);
                }
                //商家列表
                JSONArray optJSONArray = dataObj.optJSONArray("pushBusinessList");
                if(optJSONArray!=null && optJSONArray.length()!=0){
                    for (int i = 0; i < optJSONArray.length(); i++) {
                        JSONObject busObj = optJSONArray.optJSONObject(i);
                        String bus_icon = busObj.optString("seller_cover_logo");
                        String seller_taglib = busObj.optString("seller_taglib");
                        String seller_name = busObj.optString("seller_name");
                        int id = busObj.optInt("id");
                        Business b=new Business(UrlUtils.baseWebsite + bus_icon,seller_name,seller_taglib,0,"0个评论","0m");
                        b.setId(id);
                        mList.add(b);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initRefreshLayout(View view) {
        refreshLayout = ((SuperSwipeRefreshLayout) view.findViewById(R.id.main_superlayout));
        refreshLayout
                .setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {

                    @Override
                    public void onRefresh() {
                        boolean netAva = NetUtils.isNetworkAvailable(getActivity());
                        if(!netAva){
                            dm.buildAlertDialog("请打开网络！");
                            refreshLayout.setRefreshing(false);
                            return;
                        }
                        requestMainData();
                        mList.clear();
                    }

                    @Override
                    public void onPullDistance(int distance) {

                    }

                    @Override
                    public void onPullEnable(boolean enable) {

                    }
                });

    }


    private void goRecommend(View view) {
        imGoRec = ((ImageView) view.findViewById(R.id.recommend_main));
        imGoRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecQRActivity.class);
                startActivity(intent);
            }
        });
    }

    private void goSweepActivity(View view) {
        goSweepActiviIcon = ((ImageView) view.findViewById(R.id.main_sweep_icon));
        goSweepActiviIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), SweepActivity.class), 0);
            }
        });
    }

    private void goHotList(View view) {
        goBusinessList = view.findViewById(R.id.main_business_go_more);
        goBusinessList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HotActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initLocation(View view) {

        location_text = (TextView) view.findViewById(R.id.home_location_text);
        location_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CityList.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        Bundle bundle = data.getExtras();
        if (bundle == null) {
            return;
        }
        switch (resultCode) {
            case 2:
                location_text.setText(data.getStringExtra("city") + data.getIntExtra("id", -1));
                break;
            case -1:
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String uuid = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(getActivity(), "解析结果:" + uuid, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), PayActivity.class);
                    intent.putExtra("result", uuid);
                    startActivity(intent);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }




    /**
     * 商家列表初始化
     * @param view
     */
    private void initRecBusiness(View view) {
        recBusiness = (ScrollViewWithListView) view.findViewById(R.id.main_business_list);
        recBusiness.setFocusable(false);//解决直接滑动到listview以上部分的问题
        initList();
        mAdapter = new RecBusinessAdapter(mList, getContext());
        recBusiness.setAdapter(mAdapter);
        recBusiness.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BusinessDetailsActivity.class);
                Business business = mList.get(position);
                intent.putExtra("business_id",business.getId());
                startActivity(intent);
            }
        });

    }

    /**
     * viewPager初始化
     */
    private void initViewPager(View view) {
        vp = ((ViewPager) view.findViewById(R.id.main_pager));
        initImageList();
        vp.setAdapter(new MyAdapter());
        vp.setCurrentItem(1000 * 5);//当前页是第5000页
    }

    /**
     * 初始化首页轮播图片
     */
    private void initImageList() {
        imageList = new ArrayList<>();
        imageList.clear();


        ImageView ivb = new ImageView(getContext());
        ivb.setBackgroundResource(R.mipmap.banner01);
        ivb.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ImageView ivc = new ImageView(getContext());
        ivc.setBackgroundResource(R.mipmap.banner02);
        ivc.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ImageView ivd = new ImageView(getContext());
        ivd.setBackgroundResource(R.mipmap.banner_03);
        ivd.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageList.add(ivb);
        imageList.add(ivc);
        imageList.add(ivd);
    }

    //初始化商家列表
    private void initList() {
        mList = new ArrayList<>();
    }

    public class MyAdapter extends PagerAdapter {
        //表示viewpager共存放了多少个页面
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;//我们设置viewpager中有Integer.MAX_VALUE个页面
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * position % imageList.size() 而不是position，是为了防止角标越界异常
         * 因为我们设置了viewpager子页面的数量有Integer.MAX_VALUE，而imageList的数量只是5。
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //对ViewPager页号求模取出View列表中要显示的项
            position %= imageList.size();
            if (position < 0) {
                position = imageList.size() + position;
            }
            ImageView view = imageList.get(position);
            //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
            ViewParent vp = view.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(view);
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }
    }

    private String getPackageVersion() {
        PackageManager packageManager = getActivity().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getActivity().getPackageName(),0);
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
        public void onDownloadError(int what, Exception exception) {
            Logger.e(exception);

            if (exception instanceof ServerError) {
                Toast.makeText(getActivity(), "后台错误", Toast.LENGTH_SHORT).show();
            } else if (exception instanceof NetworkError) {
                Toast.makeText(getActivity(), "网络有问题", Toast.LENGTH_SHORT).show();
            } else if (exception instanceof StorageReadWriteError) {

            } else if (exception instanceof StorageSpaceNotEnoughError) {
                Toast.makeText(getActivity(), "没有足够空间", Toast.LENGTH_SHORT).show();
            } else if (exception instanceof TimeoutError) {
                Toast.makeText(getActivity(), "请求超时", Toast.LENGTH_SHORT).show();
            } else if (exception instanceof UnKnownHostError) {

            } else if (exception instanceof URLError) {
                Toast.makeText(getActivity(), "网址错误", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "未知错误", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onProgress(int what, int progress, long fileCount) {
            pD.setProgress(progress);
        }

        @Override
        public void onFinish(int what, String filePath) {
            pD.setMessage("下载成功");
            pD.dismiss();
            if(filePath!=null){
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
                getActivity().startActivity(i);
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

        pD.setButton(DialogInterface.BUTTON_POSITIVE, "下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                pD.show();
                downloadQueue = NoHttp.newDownloadQueue(2);
                DownloadRequest downloadRequest = NoHttp.createDownloadRequest(url, getDiskCacheDir(getActivity()), true);
                downloadQueue.add(0, downloadRequest, downloadListener);
            }
        });
        pD.show();

/*        pD.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });*/

    }


    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }


}
