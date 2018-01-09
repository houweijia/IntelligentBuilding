package com.example.veigar.intelligentbuilding.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.adapter.IntelligentFactoryRecyclerAdapter;
import com.example.veigar.intelligentbuilding.adapter.RecyclerSwitchAdapter;
import com.example.veigar.intelligentbuilding.base.BaseActivity;
import com.example.veigar.intelligentbuilding.bean.EnvironmentData;
import com.example.veigar.intelligentbuilding.bean.SwitchTestData;
import com.example.veigar.intelligentbuilding.network.RequestManager;
import com.example.veigar.intelligentbuilding.util.AppConst;
import com.example.veigar.intelligentbuilding.util.JSONParseUtils;
import com.example.veigar.intelligentbuilding.util.L;
import com.example.veigar.intelligentbuilding.util.MyUtils;
import com.example.veigar.intelligentbuilding.util.RequestAPI;
import com.example.veigar.intelligentbuilding.util.SPUtils;
import com.example.veigar.intelligentbuilding.weight.DividerGridItemDecoration;
import com.github.glomadrian.dashedcircularprogress.DashedCircularProgress;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by veigar on 2017/7/17.
 */

public class IntelligentFactoryActivity extends BaseActivity {

    private DashedCircularProgress dashedCircularProgress;
    private String                url              = "control";
    private String                type             = "37,38,39";
    private String                sensorType       = "49,50,51,54";
    private String                getUrl           = "dev_data";
    private List<EnvironmentData> environmentDatas = new ArrayList<>();
    private RecyclerView recyclerSwitch;
    private List<SwitchTestData> list = new ArrayList<>();

    //private List<SwitchTestData> list2 = new ArrayList<>();
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private TextView                   sensorData;
    private boolean hasMeasured = false;
    private int a;
    private boolean boo = false;
    private String equipmentID;
    private List<EnvironmentData> sensorList = new ArrayList<>();
    private RecyclerView                      recyclerSensor;
    private RelativeLayout                    progress;
    private IntelligentFactoryRecyclerAdapter adapter;
    private TimerTask                         task;
    private int p = 0;
    private RecyclerSwitchAdapter recyclerSwitchAdapter;
    private Handler               handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intelligent_factory);
        //initData();
        equipmentID = getIntent().getStringExtra(AppConst.EQUIPMENT) + "";
        L.e("equipmentID" + equipmentID);
        loadSwitchData();
        initView();
        L.e("toolbar" + getHeight(toolbar));
        //loadSensorData();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    recyclerSwitchAdapter.notifyDataSetChanged();
                }
            }
        };
        Timer timer = new Timer();//定时刷新
        task = new TimerTask() {
            @Override
            public void run() {
                loadSensorData();
            }
        };
        timer.schedule(task, 0, 10000);

    }

    private void initView() {
        initToolBar();
        setPageTitle("工业设备监控");
        dashedCircularProgress = (DashedCircularProgress) findViewById(R.id.dash_progress);
        sensorData = (TextView) findViewById(R.id.tv_sensor_data);
        recyclerSwitch = (RecyclerView) findViewById(R.id.recycler_switch);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerSwitch.addItemDecoration(new DividerGridItemDecoration(context));
        recyclerSwitch.setLayoutManager(staggeredGridLayoutManager);
        progress = (RelativeLayout) findViewById(R.id.my_progress);
        /*final RecyclerSwitchAdapter recyclerSwitchAdapter = new RecyclerSwitchAdapter(context, list);
        recyclerSwitch.setAdapter(recyclerSwitchAdapter);
        recyclerSwitchAdapter.setOnItemClickListener(new RecyclerSwitchAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(boolean isChecked,int position) {
                L.e("位置"+position);
                //int position = recyclerSwitch.getChildAdapterPosition(v);

                    if(isChecked){
                        //list.get(position)
                        L.e("关");
                        cmd("0");
                    }else{
                        L.e("开");
                        cmd("1");
                    }



            }
        });*/


        recyclerSensor = (RecyclerView) findViewById(R.id.recycle_sensor);

        //L.e("高度"+height);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerSensor.addItemDecoration(new DividerGridItemDecoration(context));
        recyclerSensor.setLayoutManager(manager);

        WindowManager wm = getWindowManager();
        int sHeight = wm.getDefaultDisplay().getHeight();
        int height = sHeight - MyUtils.dip2px(context, 250 + 85) - MyUtils.getStatusHeight(context) - 168;//计算屏幕剩余高度
        adapter = new IntelligentFactoryRecyclerAdapter(context, sensorList, height);
        recyclerSensor.setAdapter(adapter);

    }

/*    @Override
    public void onClick(View v) {
        L.e("按钮");
        cmd("1");

    }*/

    /**
     * 继电器控制
     *
     * @param str
     * @param nodeId 节点id
     */
    private void cmd(String str, String nodeId) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("gwid", equipmentID + "");
        map.put("nodeid", nodeId);
        map.put("cmd", "1");
        map.put("param", str);


        RequestManager.getInstance().post(RequestAPI.URL + url, new RequestManager.ResponseListener() {
            @Override
            public void onResponse(String s) {
                L.e(s);


            }
        }, new RequestManager.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                L.e("volleyError" + volleyError.getMessage());

            }
        }, map);
    }

    /**
     * 获取继电器信息
     */
    private void loadSwitchData() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("devid", equipmentID + "");
        map.put("type", type);//修改
        map.put("flag", "2");
        String random = String.valueOf(SystemClock.currentThreadTimeMillis());
        String userInformation = String.valueOf(SPUtils.get(context, "userInformation", "1111"));
        map.put("code", userInformation);
        map.put("random", random);
        RequestManager.getInstance().get(RequestAPI.URL + getUrl, new RequestManager.ResponseListener() {
            @Override
            public void onResponse(String s) {
                L.e("s=====" + s);
                System.out.print(s);
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                    Object object2 = object.get("result");
                    L.e("object2=====" + object2.toString());
                    environmentDatas.addAll(JSONParseUtils.parseArray(String.valueOf(object2), EnvironmentData.class));
                    recyclerSwitchAdapter = new RecyclerSwitchAdapter(context, environmentDatas);
                    recyclerSwitch.setAdapter(recyclerSwitchAdapter);
                    recyclerSwitchAdapter.setOnItemClickListener(new RecyclerSwitchAdapter.OnItemClickListener() {

                        @Override
                        public void onItemClick(boolean isChecked, int position) {
                            String nodeId = environmentDatas.get(position).getNodedata().get(0);
                            L.e("-------nodeId-------" + nodeId);
                            if (isChecked) {
                                //list.get(position)
                                cmd("1", nodeId);
                                if (environmentDatas.size() != 0) {
                                    environmentDatas.get(position).getNodedata().set(3, "1");

                                }

                                //recyclerSwitchAdapter.notifyDataSetChanged();
                            } else {
                                cmd("0", nodeId);
                                if (environmentDatas.size() != 0) {
                                    environmentDatas.get(position).getNodedata().set(3, "0");
                                }

                                //recyclerSwitchAdapter.notifyDataSetChanged();
                            }
                            handler.sendEmptyMessage(1);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new RequestManager.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, map);
    }

    private void loadSensorData() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("devid", equipmentID + "");
        map.put("type", sensorType);//修改
        map.put("flag", "2");
        String random = String.valueOf(SystemClock.currentThreadTimeMillis());
        String userInformation = String.valueOf(SPUtils.get(context, "userInformation", "1111"));
        L.e(userInformation + "---user---");
        map.put("code", userInformation);
        map.put("random", random);

        RequestManager.getInstance().get(RequestAPI.URL + getUrl, new RequestManager.ResponseListener() {
            @Override
            public void onResponse(String s) {
                //L.e("Sensor=="+s);
                if (s != null) {


                    try {
                        JSONObject object = new JSONObject(s);
                        Object object2 = object.get("result");
                        L.e(String.valueOf(object2));
                        sensorList.clear();
                        sensorList.addAll(JSONParseUtils.parseArray(String.valueOf(object2), EnvironmentData.class));
                        WindowManager wm = getWindowManager();
                        int sHeight = wm.getDefaultDisplay().getHeight();

                        int height = sHeight - MyUtils.dip2px(context, 250 + 85) - MyUtils.getStatusHeight(context) - 168;//计算屏幕剩余高度
                        // adapter = new IntelligentFactoryRecyclerAdapter(context, sensorList,height);
                        adapter.notifyDataSetChanged();
                        float f = (float) (Math.round((Float.valueOf(sensorList.get(p).getNodedata().get(3))) * 1000) / 1000);
                        sensorData.setText(f + "");
                        if (sensorList.get(p).getNodedata().get(2) != null) {
                            switch (sensorList.get(p).getNodedata().get(2)) {
                                case "49":
                                    dashedCircularProgress.setMax(60);
                                    sensorData.append("kg");
                                    break;
                                case "50":
                                    dashedCircularProgress.setMax(15);
                                    sensorData.append("mm");
                                    break;
                                case "51":
                                    dashedCircularProgress.setMax(6000);
                                    sensorData.append("pa");
                                    break;
                                case "54":
                                    dashedCircularProgress.setMax(200);
                                    sensorData.append("℃");
                                    break;
                            }
                            dashedCircularProgress.setValue((Float.valueOf(sensorList.get(p).getNodedata().get(3))));
                        }

                        //recyclerSensor.setAdapter(adapter);
                        adapter.setOnItemClickListener(new IntelligentFactoryRecyclerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view) {
                                int position = recyclerSensor.getChildAdapterPosition(view);
                                p = position;
                                if (sensorList.get(position).getNodedata().get(2) != null) {
                                    float f = (float) (Math.round((Float.valueOf(sensorList.get(position).getNodedata().get(3))) * 1000) / 1000);
                                    sensorData.setText(f + "");
                                    //sensorData.setText(sensorList.get(position).getNodedata().get(3));
                                    switch (sensorList.get(position).getNodedata().get(2)) {
                                        case "49":
                                            dashedCircularProgress.setMax(60);
                                            sensorData.append("kg");
                                            break;
                                        case "50":
                                            dashedCircularProgress.setMax(15);
                                            sensorData.append("mm");
                                            break;
                                        case "51":
                                            dashedCircularProgress.setMax(6000);
                                            sensorData.append("pa");
                                            break;
                                        case "54":
                                            dashedCircularProgress.setMax(200);
                                            sensorData.append("℃");
                                            break;
                                    }
                                    dashedCircularProgress.setValue(0);
                                    dashedCircularProgress.setValue(Float.valueOf(sensorList.get(position).getNodedata().get(3)));
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }catch (Exception e){
                        L.e(e.getMessage());
                    }
                }
            }
        }, new RequestManager.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, map);
    }

    private int getHeight(final View view) {
        ViewTreeObserver vto = toolbar.getViewTreeObserver();

        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                if (hasMeasured == false) {

                    a = view.getMeasuredHeight();

                    hasMeasured = true;
                }
                return true;
            }


        });
        return a;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task.cancel();
    }
}
