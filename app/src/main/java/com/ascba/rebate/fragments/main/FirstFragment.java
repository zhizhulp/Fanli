package com.ascba.rebate.fragments.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.BusinessDetailsActivity;
import com.ascba.rebate.activities.CityList;
import com.ascba.rebate.activities.HotActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.beans.Business;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.handlers.ReceiveThread;
import com.ascba.rebate.handlers.SendThread;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.view.ScrollViewWithListView;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.zhy.http.okhttp.OkHttpUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static android.content.Context.MODE_PRIVATE;


/**
 * 扫一扫主页
 */
public class FirstFragment extends Fragment {

    private ScrollViewWithListView recBusiness;
    private List<ImageView> imageList;
    private RecBusinessAdapter mAdapter;
    private List<Business> mList;
    private TextView location_text;
    private ViewPager vp;
    int msgWhat = 0;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            vp.setCurrentItem(vp.getCurrentItem() + 1);//收到消息，指向下一个页面
            handler.sendEmptyMessageDelayed(msgWhat, 2500);//2S后在发送一条消息，由于在handleMessage()方法中，造成死循环。
        }
    };
    private View goBusinessList;
    private ImageView goSweepActiviIcon;
    private PhoneHandler phoneHandler;
    private CheckThread checkThread;
    private RequestQueue requestQueue;
    private SharedPreferences sf;
    private ReceiveThread thread;
    private SendThread sendThread;

    public static FirstFragment instance() {
        FirstFragment view = new FirstFragment();
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sf = getActivity().getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View view = inflater.inflate(R.layout.first_fragment_status, null);
            return view;
        } else {
            View view = inflater.inflate(R.layout.first_fragment, null);
            return view;
        }
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecBusiness(view);//初始化ListView
        initViewPager(view);//初始化viewpager
        initLocation(view);//地址显示
        goHotList(view);//进入热门推荐的页面
        goSweepActivity(view);//进入扫一扫的界面
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Socket s=new Socket("test.qlqwgw.com",2346);
//                    sendThread=new SendThread(s,"你好，赵俊锋");
//                    sendThread.start();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
        thread=new ReceiveThread();
        //thread.start();

        sendMsgToSevr("http://api.qlqwgw.com/v1/index");

    }

    private void goSweepActivity(View view) {
        goSweepActiviIcon = ((ImageView) view.findViewById(R.id.main_sweep_icon));
        goSweepActiviIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), CaptureActivity.class), 0);
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
        Bundle bundle=data.getExtras();
        if(bundle==null){
            return;
        }
        switch (resultCode) {
            case 2:
                location_text.setText(data.getStringExtra("city"));
                break;
            case -1:
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(getActivity(), "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /**
     * 商家列表初始化
     *
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
        ImageView iva = new ImageView(getContext());
        iva.setBackgroundResource(R.mipmap.main_pager);
        iva.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ImageView ivb = new ImageView(getContext());
        ivb.setBackgroundResource(R.mipmap.main_pager);
        ivb.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ImageView ivc = new ImageView(getContext());
        ivc.setBackgroundResource(R.mipmap.main_pager);
        ivc.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ImageView ivd = new ImageView(getContext());
        ivd.setBackgroundResource(R.mipmap.main_pager);
        ivd.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ImageView ive = new ImageView(getContext());
        ive.setBackgroundResource(R.mipmap.main_pager);
        ive.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imageList.add(iva);
        imageList.add(ivb);
        imageList.add(ivc);
        imageList.add(ivd);
        imageList.add(ive);
    }

    //模拟商家列表
    private void initList() {
        mList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                Business business = new Business(R.mipmap.main_business_01, "利信快捷金融", "金牌商家", R.mipmap.main_business_category, (20 + i) + "个好评", (200 + i * 100) + "m");
                mList.add(business);
            } else {
                Business business = new Business(R.mipmap.main_business_logo, "华融典当", "金牌商家", R.mipmap.main_business_category, (20 + i) + "个好评", (200 + i * 100) + "m");
                mList.add(business);
            }


        }
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
            //add listeners here if necessary
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }
    }

    private void sendMsgToSevr(String baseUrl) {
        int uuid = sf.getInt("uuid", -1000);
        String token = sf.getString("token", "");
        Long expiring = sf.getLong("expiring_time", -2000);
        requestQueue = NoHttp.newRequestQueue();
        final ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.dialog);
        dialog.setMessage("请稍后");
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest(baseUrl + "?", RequestMethod.POST);
        objRequest.add("sign", UrlEncodeUtils.createSign(baseUrl));
        objRequest.add("uuid", uuid);
        objRequest.add("token", token);
        objRequest.add("expiring_time", expiring);
        phoneHandler = new PhoneHandler(getActivity());
        phoneHandler.setCallback(new PhoneHandler.Callback() {
            @Override
            public void getMessage(Message msg) {
                dialog.dismiss();
                JSONObject jObj = (JSONObject) msg.obj;
                LogUtils.PrintLog("123FirstFragment", jObj.toString());
                try {
                    int status = jObj.optInt("status");
                    JSONObject dataObj = jObj.optJSONObject("data");
                    int update_status = dataObj.optInt("update_status");
                    if (status == 200) {
                        if (update_status == 1) {
                            sf.edit()
                                    .putString("token", dataObj.getString("token"))
                                    .putLong("expiring_time", dataObj.getLong("expiring_time"))
                                    .apply();
                        }
                    } else if (status == 5) {
                        Toast.makeText(getActivity(), jObj.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else if (status == 3) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        sf.edit().putInt("uuid", -1000).apply();
                        startActivity(intent);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        checkThread = new CheckThread(requestQueue, phoneHandler, objRequest);
        checkThread.start();
        dialog.show();
    }

    /**
     * 网络请求主页商户列表
     *
     * @param url
     */
    private void downRecBusinessData(String url) {
        //Map<String, String> params = new HashMap<String, String>();
        //params.put("name", "zhy");
        //String url1 = mBaseUrl + "user!getUsers";
        OkHttpUtils//
                .post()//
                .url(url)//
//                .params(params)//
                .build()//
                .execute(new ListUserCallback()//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(List<Business> response, int id) {

                    }
                });
    }
}
