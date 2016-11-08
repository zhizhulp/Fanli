package com.ascba.fanli.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.main.APSTSViewPager;
import com.ascba.fanli.utils.LogUtils;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.lhh.apst.library.CustomPagerSlidingTabStrip;
import com.lhh.apst.library.ViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends Fragment {
    public CustomPagerSlidingTabStrip mAPSTS;
    public APSTSViewPager mVP;

    public RecommendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recommend, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAPSTS= (CustomPagerSlidingTabStrip) view.findViewById(R.id.tabs_recommend);
        mVP= (APSTSViewPager) view.findViewById(R.id.vp_recommend);
        FragmentAdapter fragmentAdapter=new FragmentAdapter(getChildFragmentManager());
        mVP.setAdapter(fragmentAdapter);
        mAPSTS.setViewPager(mVP);
    }

    public class FragmentAdapter extends FragmentStatePagerAdapter implements CustomPagerSlidingTabStrip.CustomTabProvider{

        protected LayoutInflater mInflater;

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
            mInflater = LayoutInflater.from(getContext());
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new RecommendListFragment();
                case 1:
                    return new RecommendListFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position >= 0 && position < 2){
                switch (position){
                    case  0:
                        return  "一级推广(人)\n32";
                    case  1:
                        return  "二级推广(人)\n48";
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public View getSelectTabView(int position, View convertView) {
            if (convertView == null){
                convertView = mInflater.inflate(R.layout.custom_select_tab, null);
            }

            TextView tv = ViewHolder.get(convertView, R.id.recommend_select);
            String replace = getPageTitle(position).toString().replace("\\n", "\n");
            tv.setText(replace);

            return convertView;
        }

        @Override
        public View getDisSelectTabView(int position, View convertView) {
            if (convertView == null){
                convertView = mInflater.inflate(R.layout.custom_disselect_tab, null);
            }

            TextView tv = ViewHolder.get(convertView, R.id.recommend_no_select);
            String replace = getPageTitle(position).toString().replace("\\n", "\n");

            tv.setText(replace);

            return convertView;
        }
    }
}
