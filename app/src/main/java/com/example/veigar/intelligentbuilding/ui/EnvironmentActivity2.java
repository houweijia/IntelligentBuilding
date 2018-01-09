package com.example.veigar.intelligentbuilding.ui;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.baoyz.widget.PullRefreshLayout;
import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.adapter.EnvironmentRecyclerAdapter;
import com.example.veigar.intelligentbuilding.base.BaseActivity;
import com.example.veigar.intelligentbuilding.bean.EnvironmentData;
import com.example.veigar.intelligentbuilding.network.RequestManager;
import com.example.veigar.intelligentbuilding.util.AppConst;
import com.example.veigar.intelligentbuilding.weight.DialChartView;
import com.example.veigar.intelligentbuilding.util.JSONParseUtils;
import com.example.veigar.intelligentbuilding.util.L;
import com.example.veigar.intelligentbuilding.util.MD5Utils;
import com.example.veigar.intelligentbuilding.util.RequestAPI;
import com.example.veigar.intelligentbuilding.util.SPUtils;
import com.example.veigar.intelligentbuilding.weight.DialChartViewHumidity;
import com.example.veigar.intelligentbuilding.weight.DialChartViewHeight;
import com.example.veigar.intelligentbuilding.weight.DialChartViewPM;
import com.example.veigar.intelligentbuilding.weight.DialChartViewSmoke;
import com.example.veigar.intelligentbuilding.weight.DividerGridItemDecoration;
import com.example.veigar.intelligentbuilding.weight.RecyclerViewDivider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class EnvironmentActivity2 extends BaseActivity {

    private RelativeLayout mProgress;
    private PullRefreshLayout swipeRefreshLayout;
    private String equipmentID;
    private String getUrl = "dev_data";

    private PopupWindow popup;
    private RelativeLayout relative;
    private EditText sensorName;

    private String url = "config_name";
    private String a = "8,9,11,13,15,17,19,20,21,22,23,52,53";
    private List<EnvironmentData> environmentDatas = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private EnvironmentRecyclerAdapter adapter;
    private RelativeLayout relativeTem;
    private RelativeLayout relativeHum, relativeHei;
    private DialChartView chart;
    private DialChartViewHumidity chart2;
    private TextView sensorData2;
    private TextView sensorData;
    private TextView tvName;
    private TextView sensorData3;
    private DialChartViewHeight chart3;
    private DialChartViewSmoke chart4;
    private TextView sensorData4;
    private RelativeLayout relativeSmo;
    private LinearLayout linearSwi;
    private AnimationDrawable animationDrawable;
    private ImageView iv;
    private TextView status;
    private DialChartViewPM chart5;
    private RelativeLayout relativePM;
    private TextView sensorData5;
    private int position = 0;
    private LinearLayout linearMain;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private LinearLayout linearWind;
    private TextView windDirection;
    private ImageView ivWind;
    private Animation circle_anim;
    private Handler handler;
    private TimerTask task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviroment2);
        initView();
        //tvShow.setText(Integer.toString(i++));
        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    mProgress.setVisibility(View.VISIBLE);
                    //Toast.makeText(context,"正在刷新",Toast.LENGTH_SHORT).show();
                    loadData();
                    adapter.notifyDataSetChanged();
                }
                if (msg.what == 2) {
                    L.e("handler------------------");
                    mProgress.setVisibility(View.INVISIBLE);
                }
                super.handleMessage(msg);
            }

            ;
        };
        Timer timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);

            }
        };
        timer.schedule(task, 0, 10000);


    }

    private void initView() {

        initToolBar();
        Intent intent = getIntent();
        equipmentID = intent.getStringExtra(AppConst.EQUIPMENT) + "";
        //moduleID = intent.getIntExtra(AppConst.MODULE_ID,-1)+"";
        L.e("equipmentID=" + equipmentID);

        initToolBar();
        setPageTitle("工厂环境监测");

        linearMain = (LinearLayout) findViewById(R.id.linear_main);
        relative = (RelativeLayout) findViewById(R.id.activity_enviroment);
        mProgress = (RelativeLayout) findViewById(R.id.my_progress);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(context));
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        /*swipeRefreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);*/
        relativeTem = (RelativeLayout) findViewById(R.id.relative_tem);
        relativeHum = (RelativeLayout) findViewById(R.id.relative_humidity);
        chart = (DialChartView) findViewById(R.id.circle_view1);//温度
        sensorData = (TextView) findViewById(R.id.sensor_data);
        chart.invalidate();

        chart2 = (DialChartViewHumidity) findViewById(R.id.circle_view2);//湿度、光照
        sensorData2 = (TextView) findViewById(R.id.sensor_data2);
        tvName = (TextView) findViewById(R.id.tv_name);
        chart.setCurrentStatus(0);
        chart2.setCurrentStatus(0);


        chart3 = (DialChartViewHeight) findViewById(R.id.circle_view3);//海拔
        sensorData3 = (TextView) findViewById(R.id.sensor_data3);
        relativeHei = (RelativeLayout) findViewById(R.id.relative_height);


        //烟雾浓度
        chart4 = (DialChartViewSmoke) findViewById(R.id.circle_view4);
        sensorData4 = (TextView) findViewById(R.id.sensor_data4);
        relativeSmo = (RelativeLayout) findViewById(R.id.relative_smoke);


        //开关量

        linearSwi = (LinearLayout) findViewById(R.id.linear_switch);
        iv = (ImageView) findViewById(R.id.iv);
        status = (TextView) findViewById(R.id.tv_status);

        //PM2.5
        chart5 = (DialChartViewPM) findViewById(R.id.circle_view5);
        relativePM = (RelativeLayout) findViewById(R.id.relative_pm);
        sensorData5 = (TextView) findViewById(R.id.sensor_data5);

        //风向
        linearWind = (LinearLayout) findViewById(R.id.linear_wind_direction);
        windDirection = (TextView) findViewById(R.id.tv_wind_direction);
        ivWind = (ImageView) findViewById(R.id.iv_wind2);

        adapter = new EnvironmentRecyclerAdapter(environmentDatas, context);
        mRecyclerView.setAdapter(adapter);
        //chart.setCurrentStatus(Float.valueOf((20-15)/80f));

        //loadData();
        /*mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adapter!=null){
                    String sensorId = adapter.getItem(position).getNodedata().get(0);
                    String sensorType = adapter.getItem(position).getNodedata().get(2);
                    Intent intent1 = new Intent(context,EnvironmentDetailActivity.class);
                    intent1.putExtra("sensorid",sensorId);
                    intent1.putExtra("sensortype",sensorType);
                    startActivity(intent1);
                }
            }
        });*/


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action1:
                        loadData();
                        adapter.notifyDataSetChanged();


                        break;
                    case R.id.action2:
                        String sensorId = environmentDatas.get(position).getNodedata().get(0);
                        String sensorType = environmentDatas.get(position).getNodedata().get(2);
                        Intent intent = new Intent(context, EnvironmentDetailActivity.class);
                        intent.putExtra("sensorid", sensorId);
                        intent.putExtra("sensortype", sensorType);
                        intent.putExtra("list", (Serializable) environmentDatas);
                        startActivity(intent);
                        break;
                }

                return true;
            }
        });

        /*circle_anim = AnimationUtils.loadAnimation(context, R.anim.anim_round_rotate);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);*/


    }


    /**
     * 加载数据
     */
    private void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put("devid", equipmentID);
        //map.put("sensorid","0000000100000001");//修改
        map.put("type", a);//修改
        map.put("flag", "2");
        String random = String.valueOf(SystemClock.currentThreadTimeMillis());
        String userInformation = String.valueOf(SPUtils.get(context, "userInformation", "1111"));
        L.e(userInformation + "---user---");
        map.put("code", userInformation);
        map.put("random", random);
        RequestManager.getInstance().get(RequestAPI.URL + getUrl, new RequestManager.ResponseListener() {
            @Override
            public void onResponse(String s) {
                //mProgress.setVisibility(View.INVISIBLE);
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessageDelayed(msg, 1000);
                linearMain.setVisibility(View.VISIBLE);
                // swipeRefreshLayout.setRefreshing(false);

                try {
                    L.e("sssssss" + s);
                    JSONObject object = new JSONObject(s);
                    Object object2 = object.get("result");
                    if (environmentDatas != null) {
                        environmentDatas.clear();
                    }
                    environmentDatas.addAll(JSONParseUtils.parseArray(String.valueOf(object2), EnvironmentData.class));
                    if (environmentDatas != null) {
                        if (environmentDatas.size() != 0) {
                            String data = environmentDatas.get(0).getNodedata().get(1);
                            L.e("data===" + data);
                        }

                    }

                    /*adapter = new EnvironmentRecyclerAdapter(environmentDatas,context);
                    mRecyclerView.setAdapter(adapter);*/
                    adapter.setOnItemClickListener(listener);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //environmentDataList = JSONParseUtils.parseArray(s, EnvironmentData.class);
                //EnvironmentAdapter adapter = new EnvironmentAdapter(context, environmentDataList,R.layout.enviroment_list_item);
                //mList.setAdapter(adapter);
            }
        }, new RequestManager.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mProgress.setVisibility(View.INVISIBLE);
            }
        }, map);
    }

    private EnvironmentRecyclerAdapter.OnItemClickListener listener = new EnvironmentRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view) {
            position = mRecyclerView.getChildAdapterPosition(view);
            String type = environmentDatas.get(position).getNodedata().get(2);
            iv.clearAnimation();
            switch (type) {
                case "8"://温度
                    relativeTem.setVisibility(View.VISIBLE);
                    relativeHum.setVisibility(View.GONE);
                    relativeHei.setVisibility(View.GONE);
                    relativePM.setVisibility(View.GONE);
                    relativeSmo.setVisibility(View.GONE);
                    linearSwi.setVisibility(View.GONE);
                    linearWind.setVisibility(View.GONE);
                    L.e(environmentDatas.get(position).getNodedata().get(3));

                    chart.invalidate();
                    String s = environmentDatas.get(position).getNodedata().get(3);
                    String[] split = s.split(",");
                    if (split != null) {
                        L.e("split=" + split[0]);
                        chart.setCurrentStatus((Integer.valueOf(split[0]) + 20) / 90f);
                        sensorData.setText(getString(R.string.temperature1, split[0]));


                    }
                    //Toast.makeText(context,type,Toast.LENGTH_SHORT).show();

                    break;

                case "9"://相对湿度
                    linearWind.setVisibility(View.GONE);
                    relativeHei.setVisibility(View.GONE);
                    relativeSmo.setVisibility(View.GONE);
                    relativeTem.setVisibility(View.GONE);
                    relativeHum.setVisibility(View.VISIBLE);
                    linearSwi.setVisibility(View.GONE);
                    relativePM.setVisibility(View.GONE);
                    chart2.invalidate();
                    chart2.setCurrentStatus((Integer.valueOf(environmentDatas.get(position).getNodedata().get(3))) / 100f);
                    sensorData2.setText(environmentDatas.get(position).getNodedata().get(3) + getString(R.string.percent));
                    tvName.setText(getString(R.string.humidity));
                    relativeHum.setVisibility(View.VISIBLE);
                    L.e(type);

                    break;

                case "11"://光照强度
                    linearWind.setVisibility(View.GONE);
                    relativeTem.setVisibility(View.GONE);
                    relativeHum.setVisibility(View.VISIBLE);
                    relativeSmo.setVisibility(View.GONE);
                    relativeHei.setVisibility(View.GONE);
                    linearSwi.setVisibility(View.GONE);
                    relativePM.setVisibility(View.GONE);
                    chart2.invalidate();
                    chart2.setCurrentStatus((Integer.valueOf(environmentDatas.get(position).getNodedata().get(3))) / 100f);
                    sensorData2.setText(environmentDatas.get(position).getNodedata().get(3));
                    tvName.setText(getString(R.string.sun));
                    relativeHum.setVisibility(View.VISIBLE);
                    break;

                case "13"://烟雾
                    linearWind.setVisibility(View.GONE);
                    relativeTem.setVisibility(View.GONE);
                    relativeHum.setVisibility(View.GONE);
                    relativeSmo.setVisibility(View.VISIBLE);
                    relativeHei.setVisibility(View.GONE);
                    relativePM.setVisibility(View.GONE);
                    linearSwi.setVisibility(View.GONE);
                    chart4.invalidate();
                    String a = environmentDatas.get(position).getNodedata().get(3);
                    chart4.setCurrentStatus((Integer.valueOf(a)) / 200f);
                    sensorData4.setText(environmentDatas.get(position).getNodedata().get(3));
                    break;

                case "15"://海拔
                    linearWind.setVisibility(View.GONE);
                    relativeTem.setVisibility(View.GONE);
                    relativeHum.setVisibility(View.GONE);
                    relativeSmo.setVisibility(View.GONE);
                    relativePM.setVisibility(View.GONE);
                    relativeHei.setVisibility(View.VISIBLE);
                    linearSwi.setVisibility(View.GONE);
                    sensorData3.setText(getString(R.string.height, environmentDatas.get(position).getNodedata().get(3)));
                    break;

                case "12":
                    linearWind.setVisibility(View.GONE);
                    relativeTem.setVisibility(View.GONE);
                    relativeHum.setVisibility(View.VISIBLE);
                    relativeSmo.setVisibility(View.GONE);
                    relativeHei.setVisibility(View.GONE);
                    linearSwi.setVisibility(View.GONE);
                    relativePM.setVisibility(View.GONE);
                    chart2.invalidate();
                    chart2.setCurrentStatus(20 / 100f);
                    sensorData2.setText("20");
                    tvName.setText(getString(R.string.co));
                    break;

                case "17":
                    linearWind.setVisibility(View.GONE);
                    relativeTem.setVisibility(View.GONE);
                    relativeHum.setVisibility(View.GONE);
                    relativeSmo.setVisibility(View.GONE);
                    relativePM.setVisibility(View.GONE);
                    relativeHei.setVisibility(View.GONE);
                    linearSwi.setVisibility(View.VISIBLE);
                    iv.setImageResource(R.drawable.video_frame);
                    String data = environmentDatas.get(position).getNodedata().get(3);
                    if (data.equals("0")) {
                        iv.setImageResource(R.mipmap.voicesearch_feedback001);
                        status.setText("状态：无声");
                    } else {
                        animationDrawable = (AnimationDrawable) iv.getDrawable();
                        status.setText("状态：有声");
                        animationDrawable.start();
                    }

                    break;

                case "19":
                    linearWind.setVisibility(View.GONE);
                    relativeTem.setVisibility(View.GONE);
                    relativeHum.setVisibility(View.GONE);
                    relativeSmo.setVisibility(View.GONE);
                    relativeHei.setVisibility(View.GONE);
                    relativePM.setVisibility(View.GONE);
                    linearSwi.setVisibility(View.VISIBLE);
                    if (environmentDatas.get(position).getNodedata().get(3).equals("0")) {
                        iv.setImageResource(R.mipmap.flame_no);
                        status.setText("状态：无");
                    } else {
                        iv.setImageResource(R.mipmap.flame_yes);
                        status.setText("状态：有");
                    }

                    /*relativeHei.setVisibility(View.GONE);
                    relativeSmo.setVisibility(View.GONE);
                    relativeTem.setVisibility(View.GONE);
                    relativeHum.setVisibility(View.GONE);
                    linearSwi.setVisibility(View.GONE);
                    relativePM.setVisibility(View.GONE);
                    linearWind.setVisibility(View.VISIBLE);
                    windDirection.setText(getString(R.string.wind_direction,environmentDatas.get(position).getNodedata().get(3)));
                    ivWind.startAnimation(circle_anim);*/

                    break;

                case "20":
                    linearWind.setVisibility(View.GONE);
                    relativeTem.setVisibility(View.GONE);
                    relativeHum.setVisibility(View.GONE);
                    relativeSmo.setVisibility(View.GONE);
                    relativeHei.setVisibility(View.GONE);
                    relativePM.setVisibility(View.GONE);
                    linearSwi.setVisibility(View.VISIBLE);
                    if (environmentDatas.get(position).getNodedata().get(3).equals("0")) {
                        iv.setImageResource(R.mipmap.sun);
                        status.setText("状态：没雨");
                    } else {
                        iv.setImageResource(R.mipmap.rain);
                        status.setText("状态：有雨");
                    }
                    break;

                case "22"://pm2.5
                    linearWind.setVisibility(View.GONE);
                    relativeTem.setVisibility(View.GONE);
                    relativeHum.setVisibility(View.GONE);
                    relativeSmo.setVisibility(View.GONE);
                    relativeHei.setVisibility(View.GONE);
                    linearSwi.setVisibility(View.GONE);
                    relativePM.setVisibility(View.VISIBLE);
                    chart5.setCurrentStatus((Integer.valueOf(environmentDatas.get(position).getNodedata().get(3))) / 300f);
                    sensorData5.setText(environmentDatas.get(position).getNodedata().get(3));
                    break;

                case "23"://甲醛浓度
                    linearWind.setVisibility(View.GONE);
                    relativeHei.setVisibility(View.GONE);
                    relativeSmo.setVisibility(View.GONE);
                    relativeTem.setVisibility(View.GONE);
                    relativeHum.setVisibility(View.VISIBLE);
                    linearSwi.setVisibility(View.GONE);
                    relativePM.setVisibility(View.GONE);
                    chart2.invalidate();
                    chart2.setCurrentStatus((Integer.valueOf(environmentDatas.get(position).getNodedata().get(3))) / 100f);
                    sensorData2.setText(environmentDatas.get(position).getNodedata().get(3) + getString(R.string.percent));
                    tvName.setText(getString(R.string.formaldehyde));

                    break;

                case "21"://一氧化碳
                    linearWind.setVisibility(View.GONE);
                    relativeTem.setVisibility(View.GONE);
                    relativeHum.setVisibility(View.VISIBLE);
                    relativeSmo.setVisibility(View.GONE);
                    relativeHei.setVisibility(View.GONE);
                    linearSwi.setVisibility(View.GONE);
                    relativePM.setVisibility(View.GONE);
                    chart2.invalidate();
                    chart2.setCurrentStatus((Integer.valueOf(environmentDatas.get(position).getNodedata().get(3))) / 100f);
                    sensorData2.setText(environmentDatas.get(position).getNodedata().get(3) + "%");
                    tvName.setText(getString(R.string.co));
                    break;
                case "52":
                    linearWind.setVisibility(View.GONE);
                    relativeTem.setVisibility(View.GONE);
                    relativeHum.setVisibility(View.VISIBLE);
                    relativeSmo.setVisibility(View.GONE);
                    relativeHei.setVisibility(View.GONE);
                    linearSwi.setVisibility(View.GONE);
                    relativePM.setVisibility(View.GONE);
                    chart2.invalidate();
                    if (environmentDatas.get(position).getNodedata().get(3) != null &&
                            !environmentDatas.get(position).getNodedata().get(3).equals("")) {
                        L.e("52" + "不为空");
                        chart2.setCurrentStatus((Float.valueOf(environmentDatas.get(position).getNodedata().get(3))) / 100f);
                        sensorData2.setText(environmentDatas.get(position).getNodedata().get(3) + "m/s");
                    } else {
                        L.e("52" + "空");
                        chart2.setCurrentStatus(0 / 100f);
                        sensorData2.setText(0 + "m/s");
                    }

                    tvName.setText(getString(R.string.wind_speed));

                    break;
                case "53":
                    relativeHei.setVisibility(View.GONE);
                    relativeSmo.setVisibility(View.GONE);
                    relativeTem.setVisibility(View.GONE);
                    relativeHum.setVisibility(View.GONE);
                    linearSwi.setVisibility(View.GONE);
                    relativePM.setVisibility(View.GONE);
                    linearWind.setVisibility(View.VISIBLE);
                    windDirection.setText(getString(R.string.wind_direction, environmentDatas.get(position).getNodedata().get(3)) + "°");
                    String angel = environmentDatas.get(position).getNodedata().get(3);
                    if (angel != null) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(ivWind, "rotation", 0f, Float.valueOf(angel));
                        anim.setDuration(3000);
                        anim.start();
                    }

                    break;
            }


        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task.cancel();
    }
}
