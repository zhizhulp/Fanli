package com.ascba.rebate.adapter.order;

import android.content.Context;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.beans.OrderBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/14 0014.
 * 待发货订单
 */

public class PayOrderAdapter extends BaseMultiItemQuickAdapter<OrderBean, BaseViewHolder> {

    private Context context;
    public static final int TYPE1 = 1;//订单头
    public static final int TYPE2 = 2;//订单商品
    public static final int TYPE3 = 3;//订单尾——等待卖家发货
    public static final int TYPE4 = 4;//订单尾——交易关闭

    public PayOrderAdapter(List<OrderBean> data, Context context) {
        super(data);
        this.context = context;
        for (OrderBean bean : data) {
            addItemType(bean.getItemType(), bean.getLayout());
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, final OrderBean item) {
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
                Picasso.with(context).load(goods.getImgUrl()).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).into(imageView);

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
                 * 订单尾部信息——等待卖家付款
                 */
                helper.setText(R.id.item_goods_order_freight, item.getFreight());
                helper.setText(R.id.item_goods_order_total_price, item.getOrderPrice());
                helper.setText(R.id.item_goods_order_total_num, item.getGoodsNum());
                helper.addOnClickListener(R.id.item_goods_order_total_pay);//付款
                helper.addOnClickListener(R.id.item_goods_order_total_cancel);//取消订单
                helper.addOnClickListener(R.id.item_goods_order_total_call);//联系卖家
                break;
            case TYPE4:
                /**
                 * 订单尾部信息——交易关闭
                 */
                helper.setText(R.id.item_goods_order_freight, item.getFreight());
                helper.setText(R.id.item_goods_order_total_price, item.getOrderPrice());
                helper.setText(R.id.item_goods_order_total_num, item.getGoodsNum());
                helper.addOnClickListener(R.id.item_goods_order_total_delete);//删除订单
                break;
        }
    }
}
