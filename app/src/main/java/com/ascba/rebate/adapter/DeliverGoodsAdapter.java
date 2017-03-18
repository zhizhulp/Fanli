package com.ascba.rebate.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.beans.OrderBean;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/14 0014.
 * 待发货订单
 */

public class DeliverGoodsAdapter extends BaseMultiItemQuickAdapter<OrderBean, BaseViewHolder> {

    private Context context;
    public static final int TYPE1 = 1;//订单头
    public static final int TYPE2 = 2;//订单商品
    public static final int TYPE3 = 3;//订单尾

    public DeliverGoodsAdapter(List<OrderBean> data, Context context) {
        super(data);
        this.context = context;
        if (data != null && data.size() > 0) {
            for (OrderBean bean : data) {
                addItemType(bean.getItemType(), bean.getLayout());
            }
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderBean item) {
        switch (helper.getItemViewType()) {
            case TYPE1:
                /**
                 * 订单头部信息
                 */
                helper.setText(R.id.item_goods_order_time, item.getTime());
                helper.setText(R.id.item_goods_order_state, item.getState());
                break;
            case TYPE2:
                /**
                 * 商品信息
                 */
                Goods goods = item.getGoods();
                //商品缩略图
                ImageView imageView = helper.getView(R.id.item_goods_img);
                Glide.with(context).load(goods.getImgUrl()).into(imageView);

                //商品名称
                helper.setText(R.id.item_goods_name, goods.getGoodsTitle());

                //商品规格
                helper.setText(R.id.item_goods_standard, goods.getGoodsStandard());

                //商品价格
                helper.setText(R.id.item_goods_price, "￥" + goods.getGoodsPrice());

                //商品原价
                TextView priceOld = helper.getView(R.id.item_goods_price_old);
                priceOld.setText("￥" + goods.getGoodsPriceOld());
                priceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                //购买数量
                helper.setText(R.id.item_goods_price_num, "x" + goods.getUserQuy());

                //点击查看订单详情
                helper.addOnClickListener(R.id.item_goods_rl);
                break;
            case TYPE3:
                /**
                 * 订单尾部信息
                 */
                helper.setText(R.id.item_goods_order_freight, item.getFreight());
                helper.setText(R.id.item_goods_order_total_price, item.getOrderPrice());
                helper.setText(R.id.item_goods_order_total_num, item.getGoodsNum());
                break;
        }
    }
}
