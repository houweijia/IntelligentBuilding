package com.example.veigar.intelligentbuilding.ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.adapter.ViewPagerAdapter;
import com.example.veigar.intelligentbuilding.base.BaseActivity;
import com.example.veigar.intelligentbuilding.ui.fragment.EnvironmentImageFragment;
import com.example.veigar.intelligentbuilding.ui.fragment.EnvironmentTableFragment;
import com.example.veigar.intelligentbuilding.util.L;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EnvironmentDetailActivity extends BaseActivity {
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private LineChart mChart;
    private String id,type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment_detail);
        Intent intent = getIntent();
        id = intent.getStringExtra("sensorid");
        L.e("id="+id);
        type = intent.getStringExtra("sensortype");
        L.e("type="+type);
        initView();
    }

    private void initView() {
        initToolBar();
        setPageTitle("数据展示");
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        EnvironmentImageFragment environmentImageFragment = new EnvironmentImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putString("type",type);
        environmentImageFragment.setArguments(bundle);
        fragmentList.add(environmentImageFragment);
        fragmentList.add(new EnvironmentTableFragment());
        titleList.add("实时数据图");
        titleList.add("历史数据表");
        ViewPagerAdapter fragmentPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragmentList,titleList);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }


}
