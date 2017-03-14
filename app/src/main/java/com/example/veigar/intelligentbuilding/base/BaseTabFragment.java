package com.example.veigar.intelligentbuilding.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by veigar on 2017/3/7.
 */

public class BaseTabFragment extends BaseFragment{

    protected List<Fragment> fragmentList = new ArrayList<>();
    protected List<String> title = new ArrayList<>();
    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void init(View rootView) {

    }
}
