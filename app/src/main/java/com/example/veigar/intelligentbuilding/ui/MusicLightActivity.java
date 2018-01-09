package com.example.veigar.intelligentbuilding.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.adapter.ViewPagerAdapter;
import com.example.veigar.intelligentbuilding.base.BaseActivity;
import com.example.veigar.intelligentbuilding.ui.fragment.LightFragment;
import com.example.veigar.intelligentbuilding.ui.fragment.MusicFragment;

import java.util.ArrayList;
import java.util.List;

public class MusicLightActivity extends BaseActivity {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_light);
        initView();
    }

    private void initView(){
        initToolBar();
        setPageTitle("灯光音响");
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        LightFragment lightFragment = new LightFragment();
        MusicFragment musicFragment = new MusicFragment();
        fragmentList.add(lightFragment);
        fragmentList.add(musicFragment);
        titleList.add("灯光");
        titleList.add("音响");
        ViewPagerAdapter fragmentPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragmentList,titleList);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);



    }
}
