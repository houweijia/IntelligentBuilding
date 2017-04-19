package com.example.veigar.intelligentbuilding.base;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by veigar on 2017/3/7.
 */

public abstract class BaseTabFragment extends BaseFragment{

    protected List<Fragment> fragmentList = new ArrayList<>();
    protected List<String> titleList = new ArrayList<>();
    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_tab,container,false);
    }

    @Override
    public void init(View rootView) {
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        initFragmentList(fragmentList,titleList);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(),
                fragmentList, titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    protected abstract void initFragmentList(List<Fragment> fragmentList, List<String> titleList);
}
