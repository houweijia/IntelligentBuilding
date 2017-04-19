package com.example.veigar.intelligentbuilding;

import android.app.Application;
import android.content.Context;

import com.example.veigar.intelligentbuilding.network.RequestManager;
import com.ezvizuikit.open.EZUIKit;

/**
 * Created by veigar on 2017/3/11.
 */

public class MyApplication extends Application{
    public static MyApplication instance;
    private  String appkey = "3e25e4f413bb4c499db99be2ce42ede2";
    private  String accessToken = "at.a6e02yfd835ssgjt2vwjcemq4bmrrkxs-67ufrfscqa-0zrbuds-ooobyfdwl";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();

    }

    private void init() {

        RequestManager.getInstance().init(this);
        EZUIKit.initWithAppKey(instance,appkey);
        EZUIKit.setAccessToken(accessToken);

    }

}
