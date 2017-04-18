package com.ascba.rebate.fragments.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 李鹏 on 2017/04/13 0013.
 * Fragment预加载问题的解决方案：
 * 1.可以懒加载的Fragment
 * 2.切换到其他页面时停止加载数据（可选）
 */

public abstract class LazyFragment extends Base2Fragment {
    /**
     * 视图是否已经初初始化
     */
    protected boolean isOnResume = false;
    protected boolean isShow = false;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(setContentView(), container, false);
        return view;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isShow && !hidden) {
            lazyLoad();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        lazyLoad();
        isOnResume = true;
        isShow = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isOnResume = false;
        isShow = false;
    }

    /**
     * 设置Fragment要显示的布局
     *
     * @return 布局的layoutId
     */
    protected abstract int setContentView();

    /**
     * 获取设置的布局
     *
     * @return
     */
    protected View getContentView() {
        return view;
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract void lazyLoad();


}

