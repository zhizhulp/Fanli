package com.ascba.rebate.activities.clothes;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;

public class TypeClothActivity extends BaseNetWork4Activity {

    private RecyclerView rv;
    private SuperSwipeRefreshLayout refreshLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_cloth);
        initViews();
    }

    private void initViews() {
        rv = ((RecyclerView) findViewById(R.id.list_clothes));
        refreshLat = ((SuperSwipeRefreshLayout) findViewById(R.id.refresh_layout));
    }
}
