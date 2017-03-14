package com.example.veigar.intelligentbuilding.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.BaseActivity;
import com.example.veigar.intelligentbuilding.ui.fragment.HomePageFragment;
import com.example.veigar.intelligentbuilding.ui.fragment.SettingFragment;
import com.example.veigar.intelligentbuilding.util.L;

public class MainActivity extends BaseActivity {

    private LinearLayout mainContent;
    private int index = 0;
    private Fragment[] fragments = new Fragment[2];
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    //菜单是否点击
    private boolean menuClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        initToolBar();
        mainContent = (LinearLayout) findViewById(R.id.main_content);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                if(menuClick){
                    showFragment();
                    menuClick = false;
                }
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        showDefaultMenu();
    }

    /**
     * 显示默认菜单
     */
    private void showDefaultMenu() {
        //进入页面默认显示发现Fragment
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.home_page);
        menuItem.setChecked(true);
        setPageTitle(menuItem.getTitle().toString());
        index = 0;
        showFragment();
    }

    private void itemClick(MenuItem item){
        switch (item.getItemId()){
            case R.id.home_page:
                index = 0;
                break;
            case R.id.setting:
                index = 1;
                break;
        }
        setPageTitle(item.getTitle().toString());

    }

    /**
     * 显示fragment
     */
    private void showFragment(){
        Fragment fragment = null;
        if(index == 0 || index == 1){
            fragment = fragments[index];
        }

        if(fragment == null){
            switch (index){
                case 0:
                    fragment = new HomePageFragment();
                    fragments[index] = fragment;
                    break;
                case 1:
                    fragment = new SettingFragment();
                    fragments[index] = fragment;
                    break;
            }


        }
        switchFragmentContent(R.id.main_content,fragment);


    }


    NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            L.e("item======"+item);
            menuClick = true;
            itemClick(item);
            closeDrawer();
            return true;
        }
    };

    /**
     * 判断侧滑菜单是否打开
     */
    private void closeDrawer() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(navigationView);
        }
    }

}
