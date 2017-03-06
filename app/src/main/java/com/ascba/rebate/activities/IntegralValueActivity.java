package com.ascba.rebate.activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.beans.TypeWeight;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/06 0006.
 * 商品详情页-积分增值
 */

public class IntegralValueActivity extends BaseNetWork4Activity {

    private RecyclerView recyclerView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integralvale);
        recyclerView = (RecyclerView) findViewById(R.id.activity_integralvale_recyclerview);
        button = (Button) findViewById(R.id.activity_integralvale_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        IntegralValueAdapter integralValueAdapter = new IntegralValueAdapter(getData());

        final GridLayoutManager manager = new GridLayoutManager(this, TypeWeight.TYPE_SPAN_SIZE_MAX);
        recyclerView.setLayoutManager(manager);
        integralValueAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return getData().get(position).getSpanSize();
            }
        });
        recyclerView.setAdapter(integralValueAdapter);
    }

    private List<IntegralValueItem> getData() {
        List<IntegralValueItem> data = new ArrayList<>();
        data.add(new IntegralValueItem(IntegralValueItem.TYPE_1, "购买后送20积分", "购买后可获得20积分，会员等级越高购买商品送的积分越多"));
        data.add(new IntegralValueItem(IntegralValueItem.TYPE_2));
        data.add(new IntegralValueItem(IntegralValueItem.TYPE_1, "积分有什么用", "在购买商品时，可使用积分抵扣一部分现金"));
        return data;
    }

    public class IntegralValueItem implements MultiItemEntity {


        private int itemType;
        public static final int TYPE_1 = 1;
        public static final int TYPE_2 = 2;//分割线

        private int spanSize = TypeWeight.TYPE_SPAN_SIZE_MAX;

        public int getSpanSize() {
            return spanSize;
        }

        private String title;
        private String content;

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }


        public IntegralValueItem(int itemType) {
            this.itemType = itemType;
        }

        public IntegralValueItem(int itemType, String title, String content) {
            this.itemType = itemType;
            this.title = title;
            this.content = content;
        }

        @Override
        public int getItemType() {
            return itemType;
        }
    }


    public class IntegralValueAdapter extends BaseMultiItemQuickAdapter<IntegralValueItem, BaseViewHolder> {

        public IntegralValueAdapter(List<IntegralValueItem> data) {
            super(data);
            addItemType(IntegralValueItem.TYPE_1, R.layout.integral_value_item);
            addItemType(IntegralValueItem.TYPE_2, R.layout.goods_details_cuttingline);
        }

        @Override
        protected void convert(BaseViewHolder helper, IntegralValueItem item) {
            switch (item.getItemType()) {
                case 1:
                    helper.setText(R.id.activity_se_item1_text1, item.getTitle());
                    helper.setText(R.id.activity_se_item1_text2, item.getContent());
                    break;
            }
        }
    }
}
