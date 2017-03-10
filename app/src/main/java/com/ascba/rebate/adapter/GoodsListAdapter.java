package com.ascba.rebate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.Goods;
import com.bumptech.glide.Glide;

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
        Glide.with(context).load(goodsBean.getImgUrl()).into(viewHolder.imageView);
        viewHolder.name.setText(goodsBean.getGoodsTitle());
        viewHolder.price.setText(goodsBean.getGoodsPrice());
        viewHolder.selled.setText(goodsBean.getGoodsSelled());

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
        private TextView selled;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.goods_list_name);
            price = (TextView) itemView.findViewById(R.id.goods_list_price);
            selled = (TextView) itemView.findViewById(R.id.goods_list_selled);
            imageView = (ImageView) itemView.findViewById(R.id.goods_list_img);
        }
    }

}
