package com.example.veigar.intelligentbuilding.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.adapter.ViewPagerAdapter;
import com.example.veigar.intelligentbuilding.base.BaseActivity;
import com.example.veigar.intelligentbuilding.bean.EnvironmentData;
import com.example.veigar.intelligentbuilding.ui.fragment.EnvironmentImageFragment;
import com.example.veigar.intelligentbuilding.ui.fragment.EnvironmentTableFragment;
import com.example.veigar.intelligentbuilding.util.L;
import com.github.mikephil.charting.charts.LineChart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EnvironmentDetailActivity extends BaseActivity {
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private String id,type;
    private List<EnvironmentData> list;
    private Intent intent;
    private LocalBroadcastManager manager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment_detail);
        Intent intent = getIntent();
        id = intent.getStringExtra("sensorid");
        L.e("id="+id);
        type = intent.getStringExtra("sensortype");
        L.e("type="+type);
        list = (List<EnvironmentData>) intent.getSerializableExtra("list");

        initView();
    }

    private void initView() {
        registerBroad();
        initToolBar();
        setPageTitle("数据展示");
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        EnvironmentImageFragment environmentImageFragment = new EnvironmentImageFragment();
        EnvironmentTableFragment environmentTableFragment = new EnvironmentTableFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putString("type",type);
        bundle.putSerializable("list",(Serializable) list);
        environmentImageFragment.setArguments(bundle);
        environmentTableFragment.setArguments(bundle);
        fragmentList.add(environmentImageFragment);
        fragmentList.add(environmentTableFragment);
        titleList.add("实时数据图");
        titleList.add("历史数据表");
        ViewPagerAdapter fragmentPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragmentList,titleList);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                intent.putExtra("id",item.getItemId()+"");
                manager.sendBroadcast(intent);
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.real_time_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //menu.add(Menu.NONE,3,0,"新建");
        String title = "";
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        getMenuInflater().inflate(R.menu.real_time_menu,menu);
        if(list!=null){
            for(int i = 0;i < list.size();i++){
                switch (list.get(i).getNodedata().get(2)){
                    case "8":
                        title = "温度";

                        break;
                    case "9":
                        title = "湿度";
                        //menu.add(Menu.NONE,i,i,title);
                        break;
                    case "12":
                        title = "继电器";
                        //menu.add(Menu.NONE,i,i,title);
                        break;
                    case "13":

                        break;
                    case "14":
                        break;
                    case "15":

                        break;
                    case "17":
                        break;
                    case "19":
                        break;
                    case "20":
                        break;
                    case "21":
                        break;
                    case "22":
                        break;
                    case "23":
                        break;
                }

                menu.add(Menu.NONE,Integer.valueOf(list.get(i).getNodedata().get(0)),i,title);

            }
        }

        return true;
    }


    private void registerBroad(){
        manager = LocalBroadcastManager.getInstance(context);
        intent = new Intent("com.veigar.LOCAL_BROADCAST");
    }



}
