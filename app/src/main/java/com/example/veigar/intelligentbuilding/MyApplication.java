package com.example.veigar.intelligentbuilding;

import android.app.Application;
import android.content.Context;

import com.android.volley.VolleyError;
import com.example.veigar.intelligentbuilding.network.RequestManager;
import com.example.veigar.intelligentbuilding.util.L;
import com.ezvizuikit.open.EZUIKit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by veigar on 2017/3/11.
 */

public class MyApplication extends Application{
    public static MyApplication instance;
    private  String appkey = "3e25e4f413bb4c499db99be2ce42ede2";
    private  String accessToken = "at.07adkyx52vpv38w94b7c9tzmcbpa7lc0-4u82hkrdtf-03frwbw-gjmoqz2qj";
    private String url2 = "https://open.ys7.com/api/lapp/token/get";



    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
       //init();
        RequestManager.getInstance().init(this);
        getSerialNO();
        //EZUIKit.initWithAppKey(instance,appkey);
        //EZUIKit.setAccessToken(accessToken);

    }

    private void init() {

        RequestManager.getInstance().init(this);

        //EZUIKit.setAccessToken(accessToken);

    }


    private void getSerialNO(){
        Map<String,String> map = new HashMap<>();
        map.put("appKey",appkey);
        map.put("appSecret","6e55d3845236a26e54f6f4806bd5a527");
        RequestManager.getInstance().post(url2, new RequestManager.ResponseListener() {
            @Override
            public void onResponse(String s) {

                //MonitorData monitorData = JSONParseUtils.parseObject(s, MonitorData.class);
                try {
                    JSONObject object = new JSONObject(s);
                    JSONObject data = (JSONObject) object.get("data");
                    Object accessToken = data.get("accessToken");
                    if(String.valueOf(accessToken)!=null){
                        L.e(String.valueOf(accessToken));
                        EZUIKit.initWithAppKey(instance,appkey);
                        EZUIKit.setAccessToken(String.valueOf(accessToken));

                        //prePlay();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new RequestManager.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        },map);
    }



}
