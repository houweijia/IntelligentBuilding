package com.example.veigar.intelligentbuilding.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.service.NetWorkStateService;
import com.example.veigar.intelligentbuilding.ui.MainActivity;
import com.example.veigar.intelligentbuilding.ui.WelcomeActivity;
import com.example.veigar.intelligentbuilding.util.DialogUtils;
import com.example.veigar.intelligentbuilding.util.MyUtils;
import com.example.veigar.intelligentbuilding.util.NetworkUtils;
import com.example.veigar.intelligentbuilding.util.UiHelper;

/**
 * Created by veigar on 2017/3/6.
 */

public class BaseActivity extends AppCompatActivity{
    protected Context context;
    protected Toolbar toolbar;
    //上一个Fragment
    protected Fragment mFragmentContent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initView();
    }

    private void initView() {
        startService(new Intent(this, NetWorkStateService.class));
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setNetWork();
                    }
                });
            }
        }).start();

    }

    protected void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.textColor));
        setSupportActionBar(toolbar);
        if(!(this instanceof MainActivity)){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    /**
     * 切换Fragment
     * @param to
     */
    protected void switchFragmentContent(int resId, Fragment to) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(mFragmentContent!=null){
            if (mFragmentContent != to) {
                if (!to.isAdded()) { // 先判断是否被add过
                    transaction.hide(mFragmentContent).add(resId, to, to.getClass().getName()); // 隐藏当前的fragment，add下一个到Activity中
                } else {
                    transaction.hide(mFragmentContent).show(to); // 隐藏当前的fragment，显示下一个
                }
            }
        }else{
            transaction.add(resId, to, to.getClass().getName());
        }
        transaction.commitAllowingStateLoss();  //推荐使用此方法，更安全，更方便
        mFragmentContent = to;
    }

    /**
     * 设置标题
     * @param titleName
     */
    public void setPageTitle(String titleName){
        getSupportActionBar().setTitle(titleName);
    }


    private void setNetWork(){
        if(!NetworkUtils.isHaveNetWork){
            DialogUtils.showDialog(context, R.string.setnetwork, R.string.setting_yes, R.string.setting_no,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //设置网络
                            dialog.dismiss();
                            UiHelper.forwardSetNet(BaseActivity.this);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            MyUtils.exitApp(context);
                        }
                    }, false);
        }

    }
}
