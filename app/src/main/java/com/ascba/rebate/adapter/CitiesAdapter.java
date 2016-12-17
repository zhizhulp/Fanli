package com.ascba.rebate.adapter;

import android.content.Context;
import android.support.annotation.Dimension;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */

public class CitiesAdapter extends BaseAdapter {
//    private ArrayFilter mFilter;
    private List<City> mList;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<City> mUnfilteredData;

    public CitiesAdapter(List<City> mList, Context context) {
        this.mList = mList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList==null? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= inflater.inflate(android.R.layout.simple_list_item_1,null);
        }
        City city = mList.get(position);
        ((TextView) convertView).setText(city.getCascade_name());
        //((TextView) convertView).setTextColor(context.getResources().getColor(R.color.black));
        ((TextView) convertView).setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        return convertView;
    }


   /* @Override
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
                mUnfilteredData = new ArrayList<City>(mList);
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<City> list = mUnfilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<City> unfilteredValues = mUnfilteredData;
                int count = unfilteredValues.size();

                ArrayList<City> newValues = new ArrayList<City>(count);

                for (int i = 0; i < count; i++) {
                    City pc = unfilteredValues.get(i);
                    if (pc != null) {

                        if(pc.getCityName()!=null && pc.getCityName().startsWith(prefixString)){

                            newValues.add(pc);
                        }*//*else if(pc.getEmail()!=null && pc.getEmail().startsWith(prefixString)){

                            newValues.add(pc);
                        }*//*
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
            mList = (List<City>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }*/
}
