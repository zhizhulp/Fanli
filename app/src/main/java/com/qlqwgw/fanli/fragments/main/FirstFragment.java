package com.qlqwgw.fanli.fragments.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import com.qlqwgw.fanli.R;
import com.qlqwgw.fanli.beans.Business;
import com.zhy.http.okhttp.OkHttpUtils;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;


/**
 * 扫一扫主页
 */
public class FirstFragment extends Fragment {

    private ListView recBusiness;
    private List<ImageView> imageList;
    private RecBusinessAdapter mAdapter;
    private List<Business> mList;
    private ViewPager vp;
    int msgWhat = 0;
    private ScrollView mScrollView;
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            vp.setCurrentItem(vp.getCurrentItem() + 1);//收到消息，指向下一个页面
            handler.sendEmptyMessageDelayed(msgWhat, 3);//2S后在发送一条消息，由于在handleMessage()方法中，造成死循环。
        };
    };
    //private ScrollView mScrollView;

    public static FirstFragment instance() {
        FirstFragment view = new FirstFragment();
		return view;
	}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment, null);
        mScrollView = ((ScrollView) view.findViewById(R.id.main_scroll));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //handler.sendEmptyMessageDelayed(msgWhat, 2000);
        mScrollView.smoothScrollTo(0,0);
}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecBusiness(view);//初始化ListView
        initViewPager(view);//初始化viewpager

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        //handler.removeMessages(msgWhat);
    }


    /**
     * 商家列表初始化
     * @param view
     */
    private void initRecBusiness(View view) {
        recBusiness= (ListView) view.findViewById(R.id.main_business_list);
        recBusiness.setFocusable(false);//解决直接滑动到listview以上部分的问题
        initList();
        mAdapter=new RecBusinessAdapter(mList,getContext());
        recBusiness.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(recBusiness);
    }
    /**
     * viewPager初始化
     */
    private void initViewPager(View view){
        vp=((ViewPager) view.findViewById(R.id.main_pager));
        initImageList();
        vp.setAdapter(new MyAdapter());
        vp.setCurrentItem(1000*5);//当前页是第5000页
    }
    /**
     * 初始化首页轮播图片
     */
    private void initImageList() {
        imageList=new ArrayList<>();
        imageList.clear();
        ImageView iva = new ImageView(getContext());
        iva.setBackgroundResource(R.mipmap.main_pager);

        ImageView ivb = new ImageView(getContext());
        ivb.setBackgroundResource(R.mipmap.main_pager);

        ImageView ivc = new ImageView(getContext());
        ivc.setBackgroundResource(R.mipmap.main_pager);

        ImageView ivd = new ImageView(getContext());
        ivd.setBackgroundResource(R.mipmap.main_pager);

        ImageView ive = new ImageView(getContext());
        ive.setBackgroundResource(R.mipmap.main_pager);

        imageList.add(iva);
        imageList.add(ivb);
        imageList.add(ivc);
        imageList.add(ivd);
        imageList.add(ive);
    }

    //模拟商家列表
    private void initList() {
        mList=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Business business=new Business(R.mipmap.logo,"测试"+i,"金牌会员",R.mipmap.main_business_category,(20+i)+"个好评",(200+i*100)+"m");
            mList.add(business);
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
            container.addView(imageList.get(position % imageList.size()));
            return imageList.get(position % imageList.size());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }

    /**
     * 动态设置ListView的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 网络请求主页商户列表
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
                    public void onError(Call call, Exception e, int id)
                    {
                    }

                    @Override
                    public void onResponse(List<Business> response, int id)
                    {

                    }
                });
    }
}
