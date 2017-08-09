package com.ascba.rebate.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.ShopBaseItem;
import com.ascba.rebate.beans.ShopItemType;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.StringUtils;
import com.ascba.rebate.view.RadiusBackgroundSpan;
import com.ascba.rebate.view.pagerWithTurn.ShufflingViewPager;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by 李平 on 2017/8/9.
 */

public class ShopAdapter extends MultiItemTypeAdapter<ShopBaseItem> {
    public ShopAdapter(Context context, List<ShopBaseItem> datas) {
        super(context, datas);
        addItemViewDelegate(new BannerItemDelagate());
        addItemViewDelegate(new NavItemDelagate());
        addItemViewDelegate(new GuessItemDelagate());
        addItemViewDelegate(new LineItemDelagate());
        addItemViewDelegate(new GoodsItemDelagate());
    }

    private class BannerItemDelagate implements ItemViewDelegate<ShopBaseItem> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.shop_pager;
        }

        @Override
        public boolean isForViewType(ShopBaseItem item, int position) {
            return item.getItemType()==ShopItemType.TYPE_PAGER;
        }

        @Override
        public void convert(ViewHolder helper, ShopBaseItem item, int position) {
            ShufflingViewPager pager = helper.getView(R.id.shop_pager);
            ShufflingViewPagerAdapter adapter = pager.getAdapter();
            if (adapter == null) {
                adapter = new ShufflingViewPagerAdapter(mContext, item.getBanners());
                pager.setAdapter(adapter);
                pager.start();
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    private class NavItemDelagate implements ItemViewDelegate<ShopBaseItem> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.shop_navigation;
        }

        @Override
        public boolean isForViewType(ShopBaseItem item, int position) {
            return item.getItemType()==ShopItemType.TYPE_NAVIGATION;
        }

        @Override
        public void convert(ViewHolder helper, ShopBaseItem item, int position) {
            helper.setText(R.id.item_type1_text, item.getDesc());
            Picasso.with(mContext).load(item.getUrl()).placeholder(R.mipmap.busi_loading).into((ImageView) helper.getView(R.id.item_type1_img));
        }
    }

    private class GuessItemDelagate implements ItemViewDelegate<ShopBaseItem> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.shop_title;
        }

        @Override
        public boolean isForViewType(ShopBaseItem item, int position) {
            return item.getItemType()==ShopItemType.TYPE_GUESS;
        }

        @Override
        public void convert(ViewHolder helper, ShopBaseItem item, int position) {
            helper.setText(R.id.tv_shop_title, item.getTitle());
            helper.setTextColor(R.id.tv_shop_title, item.getColor());
            Picasso.with(mContext).load(item.getUrl()).into((ImageView) helper.getView(R.id.im_shop_title));
        }
    }

    private class LineItemDelagate implements ItemViewDelegate<ShopBaseItem> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.shop_line;
        }

        @Override
        public boolean isForViewType(ShopBaseItem item, int position) {
            return item.getItemType()==ShopItemType.TYPE_LINE;
        }

        @Override
        public void convert(ViewHolder helper, ShopBaseItem item, int position) {
            View view1 = helper.getView(R.id.view_shop_line);
            view1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenDpiUtils.dip2px(mContext, item.getLineWidth())));
        }
    }

    private class GoodsItemDelagate implements ItemViewDelegate<ShopBaseItem> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.shop_goods;
        }

        @Override
        public boolean isForViewType(ShopBaseItem item, int position) {
            return item.getItemType()==ShopItemType.TYPE_GOODS;
        }

        @Override
        public void convert(ViewHolder helper, ShopBaseItem item, int position) {
            Picasso.with(mContext).load(item.getUrl()).placeholder(R.mipmap.shop_goods_loading).into((ImageView) helper.getView(R.id.goods_list_img));
            //helper.setText(R.id.goods_list_name, item.getTitle());
            helper.setText(R.id.goods_list_price, item.getDesc());
            //helper.addOnClickListener(R.id.goods_list_cart);

            helper.setVisible(R.id.tv_use_ticket_reduce, !StringUtils.isEmpty(item.getUseTicketToReduce()));
            helper.setText(R.id.tv_use_ticket_reduce,item.getUseTicketToReduce());

            //set goods name
            TextView tvName = helper.getView(R.id.goods_list_name);
            String teiHui = item.getTeiHui();
            if(!StringUtils.isEmpty(teiHui)){
                SpannableString ss=new SpannableString(teiHui+item.getTitle());
                ss.setSpan(new RadiusBackgroundSpan(mContext,0xfffa5e5f,2,11),0,teiHui.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvName.setText(ss);
            }else {
                tvName.setText(item.getTitle());
            }
        }
    }
}
