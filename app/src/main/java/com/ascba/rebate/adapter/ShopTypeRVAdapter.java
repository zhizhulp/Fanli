package com.ascba.rebate.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.ShopBaseItem;
import com.ascba.rebate.beans.ShopItemType;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.view.pagerWithTurn.ShufflingViewPager;
import com.ascba.rebate.view.pagerWithTurn.ShufflingViewPagerAdapter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 商城recyclerView适配器
 */

public class ShopTypeRVAdapter extends BaseMultiItemQuickAdapter<ShopBaseItem,BaseViewHolder> {
    private Context context;
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ShopTypeRVAdapter(List<ShopBaseItem> data, Context context) {
        super(data);
        this.context=context;
        for (int i = 0; i < data.size(); i++) {
            ShopBaseItem sbI = data.get(i);
            addItemType(sbI.getItemType(),sbI.getResLat());
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopBaseItem item) {
        switch (helper.getItemViewType()) {
            case ShopItemType.TYPE_PAGER:
                ShufflingViewPager pager = helper.getView(R.id.shop_pager);
                ShufflingViewPagerAdapter adapter=new ShufflingViewPagerAdapter(context,item.getPagerUrls());
                pager.setAdapter(adapter);
                pager.start();
                break;
            case ShopItemType.TYPE_NAVIGATION:
                helper.setText(R.id.item_type1_text, item.getDesc());
                helper.setImageResource(R.id.item_type1_img, item.getResId());
                break;
            case ShopItemType.TYPE_IMG:
                Glide.with(context).load(item.getUrl()).centerCrop().into((ImageView) helper.getView(R.id.item_type3_img));
                break;
            case ShopItemType.TYPE_HOT:
                helper.setText(R.id.item_type_4_text3, item.getUrl());
                break;
            case ShopItemType.TYPE_CHEAP:

                break;
            case ShopItemType.TYPE_OTHER:
                helper.setText(R.id.item_type6_text1, item.getTitle());
                helper.setText(R.id.item_type6_text2, item.getDesc());
                Glide.with(context).load(item.getUrl()).centerCrop().into((ImageView) helper.getView(R.id.item_type6_img));
                break;
            case ShopItemType.TYPE_TITLE:
                TextView view = helper.getView(R.id.tv_shop_title);
                view.setText(item.getDesc());
                Drawable dL=context.getResources().getDrawable(item.getResId());
                dL.setBounds(ScreenDpiUtils.dip2px(context,140),0, dL.getMinimumWidth()+ScreenDpiUtils.dip2px(context,140),dL.getMinimumHeight());
                view.setCompoundDrawables(dL,null,null,null);
                view.setCompoundDrawablePadding(12);
                break;
            case ShopItemType.TYPE_GOODS:
                Glide.with(context).load(item.getUrl()).centerCrop().into((ImageView) helper.getView(R.id.item_type9_img));
                helper.setText(R.id.item_type9_name,item.getTitle());
                helper.setText(R.id.item_type9_price,item.getDesc());
                helper.setText(R.id.item_type9_selled,item.getSaled());
                break;
            case ShopItemType.TYPE_LINE:
                View view1 = helper.getView(R.id.view_shop_line);
                view1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenDpiUtils.dip2px(context,item.getLineWidth())));
                break;
        }
    }
}
