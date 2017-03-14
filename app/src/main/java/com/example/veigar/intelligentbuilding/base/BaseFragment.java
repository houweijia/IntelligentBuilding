package com.example.veigar.intelligentbuilding.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by veigar on 2017/3/6.
 */

public  abstract class BaseFragment extends Fragment{
    protected Activity activity;
    protected View rootView;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            rootView = getRootView(inflater,container,savedInstanceState);
            init(rootView);
        }else{
            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            if(viewGroup != null){
                viewGroup.removeAllViewsInLayout();
            }
        }

        return rootView;
    }

    /**
     * 获取根视图
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public abstract View getRootView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) ;

    /**
     * 初始化
     * @param rootView
     */
    public abstract void init(View rootView);
}
