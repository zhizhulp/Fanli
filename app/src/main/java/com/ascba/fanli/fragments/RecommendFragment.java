package com.ascba.fanli.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.main.APSTSViewPager;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends Fragment {
    public AdvancedPagerSlidingTabStrip mAPSTS;
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
        mAPSTS= (AdvancedPagerSlidingTabStrip) view.findViewById(R.id.tabs_recommend);
        mVP= (APSTSViewPager) view.findViewById(R.id.vp_recommend);
        mVP.setAdapter(new RecommendItemAdapter(getFragmentManager()));
    }

    public class RecommendItemAdapter extends FragmentStatePagerAdapter{

        public RecommendItemAdapter(FragmentManager fm) {
            super(fm);
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
                        return  "一级推广";
                    case  1:
                        return  "二级推广";
                    default:
                        break;
                }
            }
            return null;
        }
    }
}
