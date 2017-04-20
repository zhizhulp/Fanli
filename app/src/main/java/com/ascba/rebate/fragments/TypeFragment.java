package com.ascba.rebate.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ascba.rebate.R;
import com.ascba.rebate.fragments.base.BaseFragment;

/**
 * 商城分类
 */
public class TypeFragment extends BaseFragment {


    public TypeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_type, container, false);
    }

}
