package com.ascba.rebate.fragments.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 李鹏 on 2017/04/13 0013.
 * Fragment预加载问题的解决方案：
 * 1.可以懒加载的Fragment
 * 2.切换到其他页面时停止加载数据（可选）
 */

public abstract class LazyBaseFragment extends BaseNetFragment {
    /**
     * 视图是否已经初初始化
     */
    protected boolean isLoad = false;
    private View view;
    private boolean debug=false;
    private String TAG="LazyBaseFragment";

    @Override
    public void onAttachFragment(Fragment childFragment) {
        if(debug)
            Log.d(TAG, "onAttachFragment: ");
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onAttach(Context context) {
        if(debug)
            Log.d(TAG, "onAttach: ");
        super.onAttach(context);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(debug)
            Log.d(TAG, "setUserVisibleHint: ");
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public boolean getUserVisibleHint() {
        if(debug)
            Log.d(TAG, "getUserVisibleHint: ");
        return super.getUserVisibleHint();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if(debug)
            Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        if(debug)
            Log.d(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(debug)
            Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(debug)
            Log.d(TAG, "onConfigurationChanged: ");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onStop() {
        if(debug)
            Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        if(debug)
            Log.d(TAG, "onLowMemory: ");
        super.onLowMemory();
    }

    @Override
    public void onDetach() {
        if(debug)
            Log.d(TAG, "onDetach: ");
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(debug)
            Log.d(TAG, "onCreateView: ");
        view = inflater.inflate(setContentView(), container, false);
        /**初始化的时候去加载数据**/
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(debug)
            Log.d(TAG, "onHiddenChanged: ");
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isCanLoad();
        }
    }


    @Override
    public void onResume() {
        if(debug)
            Log.d(TAG, "onResume: ");
        super.onResume();
        isCanLoad();
    }

    @Override
    public void onAttach(Activity activity) {
        if(debug)
            Log.d(TAG, "onAttach: ");
        super.onAttach(activity);
    }

    private void isCanLoad() {
        lazyLoad();
    }
    /**
     * 视图销毁的时候讲Fragment是否初始化的状态变为false
     */
    @Override
    public void onDestroyView() {
        if(debug)
            Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
        isLoad = false;
    }

    @Override
    public void setRetainInstance(boolean retain) {
        if(debug)
            Log.d(TAG, "setRetainInstance: ");
        super.setRetainInstance(retain);
    }

    @Override
    public void onDestroy() {
        if(debug)
            Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if(debug)
            Log.d(TAG, "onPause: ");
        super.onPause();
        isLoad = false;
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
     * 找出对应的控件
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findViewById(int id) {

        return (T) getContentView().findViewById(id);
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract void lazyLoad();

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected void stopLoad() {
    }
}

