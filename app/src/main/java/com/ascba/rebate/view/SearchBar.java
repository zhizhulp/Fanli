package com.ascba.rebate.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascba.rebate.R;

/**
 * Created by Administrator on 2016/12/24 0024.
 */

public class SearchBar extends LinearLayout implements View.OnClickListener,AdapterView.OnItemClickListener{
    private ImageView imBack;
    private MyAutoCompleteTextView tvSearch;
    private TextView tvSearchString;
    private Callback callback;

    public MyAutoCompleteTextView getTvSearch() {
        return tvSearch;
    }

    public void setTvSearch(MyAutoCompleteTextView tvSearch) {
        this.tvSearch = tvSearch;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(callback!=null){
            callback.onItemSelect(adapterView,view,i,l);
        }
    }

    public interface Callback{
        public void onItemSelect(AdapterView<?> adapterView, View view, int i, long l);
        public void onSearchClick(View v);
    }
    public SearchBar(Context context) {
        super(context);
        initView(context);
    }

    public SearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.search_bar_layout, this,true);
        imBack = ((ImageView) findViewById(R.id.search_bar_back));
        tvSearch = ((MyAutoCompleteTextView) findViewById(R.id.tv_search_bar));
        tvSearch.setOnItemClickListener(this);
        tvSearchString=((TextView) findViewById(R.id.tv_search_bar_search));
        imBack.setOnClickListener(this);
        tvSearchString.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch ((view.getId())){
            case R.id.search_bar_back:
                ((Activity) getContext()).finish();
                break;
            case R.id.tv_search_bar_search:
                if(callback!=null){
                    callback.onSearchClick(view);
                }
                break;
        }
    }
}
