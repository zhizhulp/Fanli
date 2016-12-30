package com.ascba.rebate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.SearchBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/26 0026.
 */

public class SearchAdapter extends BaseAdapter implements Filterable {
    private ArrayFilter mFilter;
    private List<SearchBean> mList;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<SearchBean> mUnfilteredData;

    public SearchAdapter(List<SearchBean> mList, Context context) {
        this.mList = mList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.search_item_list, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) view.getTag();
        SearchBean searchBean = mList.get(i);
        viewHolder.tvName.setText(searchBean.getName());
        viewHolder.tvAddress.setText(searchBean.getAddress());
        return view;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvAddress;

        public ViewHolder(View root) {
            tvName = (TextView) root.findViewById(R.id.search_name);
            tvAddress = (TextView) root.findViewById(R.id.search_address);
        }
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mUnfilteredData == null) {
                mUnfilteredData = new ArrayList<SearchBean>(mList);
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<SearchBean> list = mUnfilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<SearchBean> unfilteredValues = mUnfilteredData;
                int count = unfilteredValues.size();

                ArrayList<SearchBean> newValues = new ArrayList<SearchBean>(count);

                for (int i = 0; i < count; i++) {
                    SearchBean pc = unfilteredValues.get(i);
                    if (pc != null) {

                        if (pc.getName() != null && pc.getName().startsWith(prefixString)) {

                            newValues.add(pc);
                        } /*else if (pc.getEmail() != null && pc.getEmail().startsWith(prefixString)) {

                            newValues.add(pc);
                        }*/
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            //noinspection unchecked
            mList = (List<SearchBean>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

}
