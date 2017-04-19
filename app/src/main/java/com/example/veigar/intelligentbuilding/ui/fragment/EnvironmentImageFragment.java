package com.example.veigar.intelligentbuilding.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.print.PrintHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.BaseFragment;
import com.example.veigar.intelligentbuilding.bean.EnvironmentData;
import com.example.veigar.intelligentbuilding.network.RequestManager;
import com.example.veigar.intelligentbuilding.util.JSONParseUtils;
import com.example.veigar.intelligentbuilding.util.L;
import com.example.veigar.intelligentbuilding.util.MD5Utils;
import com.example.veigar.intelligentbuilding.util.RequestAPI;
import com.example.veigar.intelligentbuilding.util.SPUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by veigar on 2017/3/30.
 */

public class EnvironmentImageFragment extends BaseFragment {
    private LineChart mChart;
    private Thread thread;
    private String getUrl = "dev_data";
    private Bundle bundle;
    private String id,type;
    private String sensorData;
    private float a = 1;
    private Timer timer;
    private List<Entry> entryList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        id = bundle.getString("id");
        type = bundle.getString("type");
        SPUtils.put(activity,"id",id);
        SPUtils.put(activity,"type",type);



    }

    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_one,container,false);
    }

    @Override
    public void init(View rootView) {

        L.e("id===="+ id);
        timingFresh();
        mChart = (LineChart)rootView.findViewById(R.id.line_chart);
        initChart();

    }

    private void initChart() {


        //mChart.setOnChartValueSelectedListener();
        mChart.setNoDataText("暂无数据");
        mChart.setTouchEnabled(true);// 设置是否可以触摸

        mChart.setDragEnabled(true);// 是否可以拖拽
        mChart.setScaleEnabled(true);// 是否可以缩放
        mChart.setDrawGridBackground(false);// 是否显示表格颜色

        mChart.setPinchZoom(true);
        mChart.setBackgroundColor(Color.TRANSPARENT);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);//设置一个空的data

        Legend l = mChart.getLegend();
        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);// 设置X轴的数据显示在报表的下方

        //设置左边y轴的样式
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLUE);
        leftAxis.setAxisMaximum(130f);
        leftAxis.setAxisMinimum(0f);

        leftAxis.setDrawGridLines(true);
        //leftAxis.setDrawAxisLine(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void addEntry() {

        LineData data = mChart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            //data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 40) + 30f), 0);
            float f = Float.parseFloat(sensorData);

            data.addEntry(new Entry(a,f),0);

            data.notifyDataChanged();

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(120);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewToX(data.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // mChart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private LineDataSet createSet() {
        String name = null;
        switch (type){
            case "8":
                name = "温度传感器";
                break;
            case "9":
                name = "湿度传感器";
                break;
            case "0a":
                name = "红外传感器";
                break;
            case "0b":
                name = "光敏传感器";
                break;
            case "0c":
                name = "继电器";
                break;
            case "0d":
                name = "烟雾传感器";
                break;
            case "0e":
                name = "气压传感器";
                break;
            case "0f":
                name = "海拔传感器";
                break;
            case "10":
                name = "震动传感器";
                break;
            case "11":
                name = "声音传感器";
                break;
        }
        if(name==null){
            name = "无";
        }
        LineDataSet set = new LineDataSet(entryList, name);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    private void feedMultiple() {

        if (thread != null)
            thread.interrupt();

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                addEntry();
            }
        };

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                /*for (int i = 0; i < 10; i++) {

                    // Don't generate garbage runnables inside the loop.
                    runOnUiThread(runnable);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }*/
                while (true){
                    activity.runOnUiThread(runnable);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (thread != null) {
            thread.interrupt();
            mChart.clear();
            L.e("onPause");
        }
        timer.cancel();
    }

    private void load(){
        Map<String,String> map = new HashMap<>();
        //map.put("devid","00000001");
        map.put("sensorid",id);//修改
        map.put("type",type);//修改
        map.put("flag","1");
        String random = String.valueOf(SystemClock.currentThreadTimeMillis());
        String userInformation = String.valueOf(SPUtils.get(activity,"userInformation","1111"));
        L.e(userInformation+"---user---");
        if(!userInformation.equals(-1)){
            String code = MD5Utils.md5(userInformation+random);
            map.put("code",code);
        }
        RequestManager.getInstance().get(RequestAPI.URL + getUrl, new RequestManager.ResponseListener() {
            @Override
            public void onResponse(String s) {
                L.e("s======="+s);
                try {
                    JSONObject object = new JSONObject(s);
                    Object object2 = object.get("result");
                    List<EnvironmentData> environmentDatas = JSONParseUtils.parseArray(String.valueOf(object2), EnvironmentData.class);
                    if(environmentDatas.get(0).getNodedata().get(3)!=null){
                        sensorData = environmentDatas.get(0).getNodedata().get(3);
                        Entry entryY =  new Entry();
                        entryY.setY(Float.valueOf(sensorData));
                        entryList = new ArrayList<Entry>();
                        entryList.add(entryY);
                    }
                    feedMultiple();
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

    private void timingFresh(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                a = a + 1;
                L.e("a==="+a);
                load();


            }
        };
        timer = new Timer();
        timer.schedule(timerTask,0,2000);
    }



}
