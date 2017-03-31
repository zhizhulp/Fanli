package com.ascba.rebate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.Goods;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by muzi on 2017/03/09.
 */
public class GoodsListAdapter extends RecyclerView.Adapter<GoodsListAdapter.BaseViewHolder> {

    private int type = 0;
    private Context context;
    private LayoutInflater inflater;
    private List<Goods> list;

    public GoodsListAdapter(Context context, List<Goods> list) {

        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView;
        if (viewType == 0) {
            rootView = inflater.inflate(R.layout.goods_list_layout_linear, null, false);
        } else {
            rootView = inflater.inflate(R.layout.goods_list_layout_grid, null, false);
        }
        ViewHolder viewHolder = new ViewHolder(rootView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;
        Goods goodsBean = list.get(position);
        Picasso.with(context).load(goodsBean.getImgUrl()).placeholder(R.mipmap.loading_rect).error(R.mipmap.loading_rect).into(viewHolder.imageView);
        viewHolder.name.setText(goodsBean.getGoodsTitle());
        viewHolder.price.setText(goodsBean.getGoodsPrice());
        viewHolder.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"加入购物车",Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"商品详情",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class ViewHolder extends BaseViewHolder {

        private TextView name;
        private TextView price;
        private ImageView imageView;
        private ImageView addCart;
        private LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.goods_list_name);
            price = (TextView) itemView.findViewById(R.id.goods_list_price);
            addCart = (ImageView) itemView.findViewById(R.id.goods_list_cart);
            imageView = (ImageView) itemView.findViewById(R.id.goods_list_img);
            layout = (LinearLayout) itemView.findViewById(R.id.goods_list_ll);
        }
    }

}
