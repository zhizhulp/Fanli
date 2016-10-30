package com.ascba.fanli.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.fanli.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CashAccountFragment extends Fragment {


    public CashAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cash_account, container, false);
    }

}
