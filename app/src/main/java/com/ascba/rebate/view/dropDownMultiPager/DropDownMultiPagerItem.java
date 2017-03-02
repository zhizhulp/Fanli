package com.ascba.rebate.view.dropDownMultiPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.GoodsFootprint;
import com.bumptech.glide.Glide;

import java.util.List;


/**
 * Created by YaphetZhao
 * on 2016/12/12.
 */

@SuppressLint("ViewConstructor")
public class DropDownMultiPagerItem extends LinearLayout {
    public DropDownMultiPagerItem(Context context, int num, List<GoodsFootprint> beanList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_dropdownfootprint, this);

        //当前位置
        TextView textNum = (TextView) findViewById(R.id.item_num);
        textNum.setText("我的足迹（" + (num + 1) + "/" + beanList.size() + ")");

        //描述
        TextView textDes = (TextView) findViewById(R.id.item_des);
        textDes.setText(beanList.get(num).getName());

        //价格
        TextView textPrice = (TextView) findViewById(R.id.item_price);
        textPrice.setText(beanList.get(num).getPrice());

        //图文
        ImageView img = (ImageView) findViewById(R.id.item_img);
        Glide.with(context).load(beanList.get(num).getImg()).into(img);

    }

}
