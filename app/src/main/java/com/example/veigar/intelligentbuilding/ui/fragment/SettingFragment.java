package com.example.veigar.intelligentbuilding.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.BaseFragment;

/**
 * Created by veigar on 2017/3/7.
 */

public class SettingFragment extends BaseFragment{
    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting,container,false);
    }

    @Override
    public void init(View rootView) {

    }
}
