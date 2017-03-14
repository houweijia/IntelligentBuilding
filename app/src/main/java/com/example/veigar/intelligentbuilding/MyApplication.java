package com.example.veigar.intelligentbuilding;

import android.app.Application;
import android.content.Context;

import com.example.veigar.intelligentbuilding.network.RequestManager;

/**
 * Created by veigar on 2017/3/11.
 */

public class MyApplication extends Application{
    public static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();

    }

    private void init() {
        RequestManager.getInstance().init(this);
    }

}
