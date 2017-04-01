package com.ascba.rebate.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.ShopBaseItem;
import com.ascba.rebate.beans.ShopItemType;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.view.pagerWithTurn.ShufflingViewPager;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 商城recyclerView适配器
 */

public class ShopTypeRVAdapter extends BaseMultiItemQuickAdapter<ShopBaseItem, BaseViewHolder> {
    private Context context;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ShopTypeRVAdapter(List<ShopBaseItem> data, Context context) {
        super(data);
        this.context = context;
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                ShopBaseItem sbI = data.get(i);
                addItemType(sbI.getItemType(), sbI.getResLat());
            }
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopBaseItem item) {
        switch (helper.getItemViewType()) {
            case ShopItemType.TYPE_PAGER:
                ShufflingViewPager pager = helper.getView(R.id.shop_pager);
                ShufflingViewPagerAdapter adapter = pager.getAdapter();

                if(adapter==null){
                    adapter = new ShufflingViewPagerAdapter(context, item.getPagerUrls());
                    pager.setAdapter(adapter);
                    pager.start();
                }else {
                    adapter.notifyDataSetChanged();
                }
                break;
            case ShopItemType.TYPE_NAVIGATION:
                helper.setText(R.id.item_type1_text, item.getDesc());
                Picasso.with(context).load(item.getUrl()).placeholder(R.mipmap.busi_loading).into((ImageView) helper.getView(R.id.item_type1_img));
                break;
            case ShopItemType.TYPE_IMG:
                Picasso.with(context).load(item.getUrl()).centerCrop().placeholder(R.mipmap.busi_loading).into((ImageView) helper.getView(R.id.item_type3_img));
                break;
            case ShopItemType.TYPE_HOT:
                helper.setText(R.id.item_type_4_text3, item.getUrl());
                break;
            case ShopItemType.TYPE_RUSH:
                helper.setText(R.id.tv_rush_title1, item.getTitles().get(0));
                helper.setText(R.id.tv_rush_content1, item.getContents().get(0));
                helper.setText(R.id.tv_rush_desc1, item.getDescs().get(0));
                ImageView img1 = helper.getView(R.id.tv_rush_img1);
                Picasso.with(context).load(item.getPagerUrls().get(0)).placeholder(R.mipmap.busi_loading).into(img1);

                helper.setText(R.id.tv_rush_title2, item.getTitles().get(1));
                helper.setText(R.id.tv_rush_content2, item.getContents().get(1));
                helper.setText(R.id.tv_rush_desc2, item.getDescs().get(1));
                ImageView img2 = helper.getView(R.id.tv_rush_img2);
                Picasso.with(context).load(item.getPagerUrls().get(1)).placeholder(R.mipmap.busi_loading).into(img2);

                helper.setText(R.id.tv_rush_title3, item.getTitles().get(2));
                helper.setText(R.id.tv_rush_content3, item.getContents().get(2));
                helper.setText(R.id.tv_rush_desc3, item.getDescs().get(2));
                ImageView img3 = helper.getView(R.id.tv_rush_img3);
                Picasso.with(context).load(item.getPagerUrls().get(2)).placeholder(R.mipmap.busi_loading).into(img3);
                break;

            case ShopItemType.TYPE_CHEAP:

                break;
            case ShopItemType.TYPE_OTHER:
                helper.setText(R.id.item_type6_text1, item.getTitle());
                helper.setText(R.id.item_type6_text2, item.getDesc());
                Picasso.with(context).load(item.getUrl()).centerCrop().into((ImageView) helper.getView(R.id.item_type6_img));
                helper.setBackgroundColor(R.id.item_type6_ll, item.getColor());
                break;
            case ShopItemType.TYPE_TITLE:
                helper.setText(R.id.tv_shop_title, item.getTitle());
                helper.setTextColor(R.id.tv_shop_title, item.getColor());
                Picasso.with(context).load(item.getUrl()).into((ImageView) helper.getView(R.id.im_shop_title));
                break;
            case ShopItemType.TYPE_GOODS:
                Picasso.with(context).load(item.getUrl()).placeholder(R.mipmap.busi_loading).into((ImageView) helper.getView(R.id.goods_list_img));
                helper.setText(R.id.goods_list_name, item.getTitle());
                helper.setText(R.id.goods_list_price, item.getDesc());
                break;
            case ShopItemType.TYPE_LINE:
                View view1 = helper.getView(R.id.view_shop_line);
                view1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenDpiUtils.dip2px(context, item.getLineWidth())));
                break;
            case ShopItemType.TYPE_GOODS_STYLE2:
                Picasso.with(context).load(item.getUrl()).into((ImageView) helper.getView(R.id.shop_im_goods_style2));
                helper.setText(R.id.shop_tv_goods_title_style2, item.getTitle());
                helper.setText(R.id.shop_tv_goods_price_style2, item.getDesc());
                TextView view = helper.getView(R.id.shop_tv_goods_price_old_style2);
                view.setText(item.getTitle());
                view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                break;
        }
    }

    //数据刷新
    public void refreshData(){

        notifyDataSetChanged();

    }
}
