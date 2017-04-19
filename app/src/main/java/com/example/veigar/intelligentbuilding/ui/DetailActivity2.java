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
import com.example.veigar.intelligentbuilding.network.RequestManager;
import com.example.veigar.intelligentbuilding.util.AppConst;
import com.example.veigar.intelligentbuilding.util.CircleMenuLayout;
import com.example.veigar.intelligentbuilding.util.L;
import com.example.veigar.intelligentbuilding.util.MD5Utils;
import com.example.veigar.intelligentbuilding.util.MyUtils;
import com.example.veigar.intelligentbuilding.util.RequestAPI;
import com.example.veigar.intelligentbuilding.util.SPUtils;

import java.util.HashMap;
import java.util.Map;


public class DetailActivity2 extends BaseActivity{
    private String[] mItemTexts = new String[] { "智能门禁考勤", "建筑能耗监控", "建筑环境监测",
            "智能楼宇安防", "智能音响灯光"};
    private int[] mItemImgs = new int[] { R.mipmap.attendance_img,
            R.mipmap.energy_img, R.mipmap.environment_img,
            R.mipmap.security_img, R.mipmap.sound_img,
    };
    private CircleMenuLayout mCircleMenuLayout;

    private String url = "get_node";
    private LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main02);
        Intent intent = getIntent();
        AppConst.EQUIPMENT_ID = intent.getStringExtra("id");
        String a = intent.getStringExtra("id");
        L.e("Detail_id==="+ a);
        initView();
        getNodeType();
    }

    private void initView() {
        initToolBar();
        setPageTitle("功能模块");
        linear = (LinearLayout) findViewById(R.id.linear);
        Bitmap background = BitmapFactory.decodeResource(getResources(),R.mipmap.snow);
        Bitmap mohu = MyUtils.fastblur(background,40);
        BitmapDrawable drawable = new BitmapDrawable(mohu);
        linear.setBackground(drawable);
        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
        mCircleMenuLayout.setOnMenuItemClickListener(listener);
        long l = System.currentTimeMillis();
        Log.e("main",l+"");

    }

    CircleMenuLayout.OnMenuItemClickListener listener = new CircleMenuLayout.OnMenuItemClickListener(){

        @Override
        public void itemClick(View view, int pos) {
            L.e(""+pos);
            Intent intent;
            switch (pos){
                case 0:
                    intent = new Intent(context,PersonAttendanceActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    break;
                case 2:
                    intent = new Intent(context,EnvironmentActivity.class);
                    intent.putExtra(AppConst.MODULE_ID,AppConst.ENVIRONMENT_ID);
                    intent.putExtra(AppConst.EQUIPMENT,AppConst.EQUIPMENT_ID);
                    startActivity(intent);
                    break;
                case 3:
                    intent = new Intent(context,MonitorActivity.class);
                    startActivity(intent);
                    break;

                case 4:

                    break;
            }
        }

        @Override
        public void itemCenterClick(View view) {

        }
    };

    private void getNodeType(){
        Map<String,String> map = new HashMap<>();
        String random = String.valueOf(SystemClock.currentThreadTimeMillis());
        String userInformation = String.valueOf(SPUtils.get(context,"userInformation","1111"));
        L.e(userInformation+"---user---");
        if(!userInformation.equals(-1)){
            String code = MD5Utils.md5(userInformation+random);
            map.put("code",code);
        }

        map.put("random",random);
        map.put("devid",AppConst.EQUIPMENT_ID);
        RequestManager.getInstance().get(RequestAPI.URL + url, new RequestManager.ResponseListener() {
            @Override
            public void onResponse(String s) {
                L.e("detail_s==="+s);
            }
        }, new RequestManager.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        },map);
    }
}
