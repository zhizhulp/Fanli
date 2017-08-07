package com.ascba.rebate.activities.base;

import android.os.Bundle;

import com.ascba.rebate.R;
import com.ascba.rebate.view.EasyStatusView;

public class BaseDefaultUIActivity extends BaseUIActivity {
    /**
     * 状态控制view
     */
    protected EasyStatusView statusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int bindLayout() {
        return 0;
    }

    @Override
    protected void initViewss() {
        initStatusView();
    }

    /**
     * 状态控制布局初始化
     */
    private void initStatusView() {
        statusView = fv(R.id.statusView);
    }
}
