package com.example.veigar.intelligentbuilding.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.BaseActivity;
import com.example.veigar.intelligentbuilding.bean.EnvironmentData;
import com.example.veigar.intelligentbuilding.network.RequestManager;
import com.example.veigar.intelligentbuilding.util.AppConst;
import com.example.veigar.intelligentbuilding.util.JSONParseUtils;
import com.example.veigar.intelligentbuilding.weight.CircleMenuLayout;
import com.example.veigar.intelligentbuilding.util.L;
import com.example.veigar.intelligentbuilding.util.MD5Utils;
import com.example.veigar.intelligentbuilding.util.MyUtils;
import com.example.veigar.intelligentbuilding.util.RequestAPI;
import com.example.veigar.intelligentbuilding.util.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DetailActivity2 extends BaseActivity {
    private String[] mItemTexts = new String[]{"工业能耗管理", "工厂环境监测",
            "工业安防监控", "工业设备监控", "NB-IOT应用"};
    private int[] mItemImgs = new int[]{R.mipmap.energy_img, R.mipmap.environment_img,
            R.mipmap.security_img, R.mipmap.factory_img, R.mipmap.img_internet_of_things
    };
    private CircleMenuLayout mCircleMenuLayout;

    private String url = "get_node";
    private LinearLayout linear;
    private String name;
    private Bitmap background;
    private Bitmap mohu;
    private String getUrl = "dev_data";
    private String mData;
    private String aaa = "1";
    private String bb = "2";
    private String cc = "3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main02);
        Intent intent = getIntent();
        AppConst.EQUIPMENT_ID = intent.getStringExtra("id");
        String a = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        L.e("Detail_id===" + AppConst.EQUIPMENT_ID);
        initView();
        //getNodeType();

    }

    private void initView() {
        initToolBar();
        if (name != null) {
            setPageTitle(MyUtils.toUtf8(name));
        }

        linear = (LinearLayout) findViewById(R.id.linear);
        background = BitmapFactory.decodeResource(getResources(), R.mipmap.snow);
        mohu = MyUtils.fastblur(background, 40);
        BitmapDrawable drawable = new BitmapDrawable(mohu);
        linear.setBackground(drawable);
        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
        mCircleMenuLayout.setOnMenuItemClickListener(listener);
        long l = System.currentTimeMillis();
        Log.e("main", l + "");
        getCameraData();

    }

    CircleMenuLayout.OnMenuItemClickListener listener = new CircleMenuLayout.OnMenuItemClickListener() {

        @Override
        public void itemClick(View view, int pos) {
            L.e("" + pos);
            Intent intent;
            switch (pos) {
                case 0:
                    intent = new Intent(context, EnergyActivity.class);
                    startActivity(intent);

                    L.e("0");
                    break;
                case 1:
                    intent = new Intent(context, EnvironmentActivity2.class);
                    intent.putExtra(AppConst.MODULE_ID, AppConst.ENVIRONMENT_ID);
                    intent.putExtra(AppConst.EQUIPMENT, AppConst.EQUIPMENT_ID);
                    startActivity(intent);

                    L.e("1");
                    break;
                case 2:
                    intent = new Intent(context, MonitorActivity.class);
                    intent.putExtra(AppConst.MODULE_ID, AppConst.ENVIRONMENT_ID);
                    intent.putExtra(AppConst.EQUIPMENT, AppConst.EQUIPMENT_ID);
                    intent.putExtra("camera",mData);
                    startActivity(intent);
                    L.e("2");
                    break;
                case 3:
                    intent = new Intent(context, IntelligentFactoryActivity.class);
                    intent.putExtra(AppConst.EQUIPMENT, AppConst.EQUIPMENT_ID);
                    startActivity(intent);
                    L.e("3");
                    break;
                case 4:
                    intent = new Intent(context,NbIotActivity.class);
                    startActivity(intent);
                    break;

            }
        }

        @Override
        public void itemCenterClick(View view) {

        }
    };

    private void getNodeType() {
        Map<String, String> map = new HashMap<>();
        String random = String.valueOf(SystemClock.currentThreadTimeMillis());
        String userInformation = String.valueOf(SPUtils.get(context, "userInformation", "1111"));
        L.e(userInformation + "---user---");
        map.put("code", userInformation);
        map.put("random", random);
        map.put("devid", AppConst.EQUIPMENT_ID);
        RequestManager.getInstance().get(RequestAPI.URL + url, new RequestManager.ResponseListener() {
            @Override
            public void onResponse(String s) {
                L.e("detail_s===" + s);
            }
        }, new RequestManager.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

// 先判断是否已经回收
        if(background != null && !background.isRecycled()){
            // 回收并且置为null
            background.recycle();
            background = null;
        }

        if(mohu != null && !mohu.isRecycled()){
            // 回收并且置为null
            mohu.recycle();
            mohu = null;
        }
        System.gc();


    }

    private void getCameraData(){
        Map<String, String> map = new HashMap<>();
        map.put("devid", AppConst.EQUIPMENT_ID);
        map.put("flag", "5");
        String random = String.valueOf(SystemClock.currentThreadTimeMillis());
        String userInformation = String.valueOf(SPUtils.get(context, "userInformation", "1111"));
        L.e(userInformation + "---user---");
        map.put("code", userInformation);
        map.put("random", random);
        RequestManager.getInstance().get(RequestAPI.URL + getUrl, new RequestManager.ResponseListener() {
            @Override
            public void onResponse(String s) {
                L.e("cameraData"+s);
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                    Object object2 = object.get("result");
                    List<EnvironmentData> environmentDatas = JSONParseUtils.parseArray(String.valueOf(object2), EnvironmentData.class);
                    if (environmentDatas != null) {
                        if (environmentDatas.size() != 0) {
                            mData = environmentDatas.get(1).getCamera().get(5);
                            L.e("data===" + mData);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new RequestManager.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                L.e(volleyError.toString());
            }
        },map);
    }
}
