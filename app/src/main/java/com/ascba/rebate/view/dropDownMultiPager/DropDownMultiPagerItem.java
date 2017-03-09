package com.ascba.rebate.view.dropDownMultiPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.GoodsDetailsItem;
import com.bumptech.glide.Glide;

import java.util.List;


@SuppressLint("ViewConstructor")
public class DropDownMultiPagerItem extends LinearLayout {
    public DropDownMultiPagerItem(Context context, int num, List<GoodsDetailsItem> beanList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_dropdownfootprint, this);

        //描述
        TextView textDes = (TextView) findViewById(R.id.item_dropdownfootprint_desc);
        textDes.setText(beanList.get(num).getGoods_desc());

        //价格
        TextView textPrice = (TextView) findViewById(R.id.item_dropdownfootprint_price);
        textPrice.setText(beanList.get(num).getPrice_new());

        //图文
        ImageView img = (ImageView) findViewById(R.id.item_dropdownfootprint_img);
        Glide.with(context).load(beanList.get(num).getImgUrl()).into(img);

    }

}
