package com.example.veigar.intelligentbuilding.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.BaseActivity;
import com.example.veigar.intelligentbuilding.bean.EnergyData;
import com.example.veigar.intelligentbuilding.bean.EnvironmentData;
import com.example.veigar.intelligentbuilding.network.RequestManager;
import com.example.veigar.intelligentbuilding.util.JSONParseUtils;
import com.example.veigar.intelligentbuilding.util.L;
import com.example.veigar.intelligentbuilding.util.MD5Utils;
import com.example.veigar.intelligentbuilding.util.MyUtils;
import com.example.veigar.intelligentbuilding.util.RequestAPI;
import com.example.veigar.intelligentbuilding.util.SPUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EnergyActivity extends BaseActivity implements OnChartValueSelectedListener{

    private Random random;
    private BarChart mBarChart,mPowerBarChart;
    private String[] str = {"设备一","设备二","设备三","设备四","设备五","设备六","设备七","设备八","设备九","设备十","设备十一","设备十二","设备十三","设备十四","设备十五","设备十六"};
    private PieChart mPieChart;
    private List<EnergyData> list;
    private RelativeLayout mProgress;
    private LinearLayout linearLayout;
    private String url = "dev_data";
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_energy);

        random = new Random();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    setEnergyData(mBarChart,ColorTemplate.LIBERTY_COLORS);
                }
            }
        };
        initView();

    }

    private void initView(){
        mProgress = (RelativeLayout) findViewById(R.id.my_progress);
        load();
        initToolBar();
        setPageTitle("工业能耗管理");

        linearLayout = (LinearLayout) findViewById(R.id.linear_main);

        //mEnergyBarChart = (BarChart) findViewById(R.id.my_bar_chart);
        //mPowerBarChart = (BarChart) findViewById(R.id.power_bar_chart);
        //loadPieChart();

        /*initBarChart(mEnergyBarChart);
        setEnergyData(11, 50,mEnergyBarChart,ColorTemplate.MATERIAL_COLORS);
        mEnergyBarChart.setVisibleXRangeMaximum(8f);//设置圆柱的最大显示数量*/
        initPowerChart();
        loadPieChart();



        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                int b = R.id.action1;
                L.e(itemId+"-----"+b);
                if(itemId==b){
                    load();
                }else{
                    Intent intent = new Intent(context,EachEnergyActivity.class);
                    intent.putExtra("id",item.getItemId()+"");
                    intent.putExtra("name",item.getTitle());
                    startActivity(intent);
                }


                return true;
            }
        });

    }


    /**
     * 初始化柱状图
     */
    private void initPowerChart(){
        mBarChart = (BarChart) findViewById(R.id.power_bar_chart);
        //setEnergyData(12,100,mBarChart,ColorTemplate.LIBERTY_COLORS);

        //设置高亮显示
        mBarChart.setHighlightFullBarEnabled(true);
        mBarChart.setDrawValueAboveBar(true);
        //设置支持触控
        mBarChart.setTouchEnabled(true);
        //设置是否支持拖拽
        mBarChart.setDragEnabled(true);
        //设置能否缩放
        mBarChart.setScaleEnabled(true);
        //设置true支持两个指头向X、Y轴的缩放，如果为false，只能支持X或者Y轴的当方向缩放
        mBarChart.setPinchZoom(true);
        //获取图表右下角的描述性文字，setEnable（）默认为true
        mBarChart.getDescription().setEnabled(true);
        /*Description description=new Description();
        description.setText("description");
        //设置右下角的描述文字
        mBarChart.setDescription(description);*/
        mBarChart.getDescription().setEnabled(false);
        //设置阴影
        mBarChart.setDrawBarShadow(false);
        //设置所有的数值在图形的上面,而不是图形上
        mBarChart.setDrawValueAboveBar(true);
        //设置最大的能够在图表上显示数字的图数
        mBarChart.setMaxVisibleValueCount(60);
        //设置背景是否网格显示
        mBarChart.setDrawGridBackground(false);
        //mBarChart.setOnChartValueSelectedListener(this);

//X轴的数据格式
        //IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);
        //得到X轴，设置X轴的样式
        XAxis xAxis = mBarChart.getXAxis();
        //设置位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置特定的标签类型
        //xAxis.setTypeface(mTfLight);
        //设置是否绘制网格线
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(-70);
        //设置最小的区间，避免标签的迅速增多
        xAxis.setGranularity(1f); // only intervals of 1 day
        //设置进入时的标签数量
        xAxis.setLabelCount(12);
        //设置数据格式
        //handler.sendEmptyMessageDelayed(1,1000);
        //setEnergyData(mBarChart,ColorTemplate.LIBERTY_COLORS);

        xAxis.setValueFormatter(new EnergyActivity.MyIAxisValueFormatter());

        //setEnergyData(12,100,mBarChart,ColorTemplate.LIBERTY_COLORS);

        //IAxisValueFormatter custom = new MyAxisValueFormatter();
        YAxis leftAxis = mBarChart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        //leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //Sets the top axis space in percent of the full range. Default 10f
        leftAxis.setSpaceTop(15f);
        //设置Y轴最小的值
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        //rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
        // rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setEnabled(false);
        //设置图例样式，默认可以显示，设置setEnabled（false）；可以不绘制
        Legend l = mBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        //设置X轴和Y轴显示的刻度
        //setEnergyData(mBarChart,ColorTemplate.LIBERTY_COLORS);
        mBarChart.setOnChartValueSelectedListener(this);

    }

    /**
     * 给柱状图设置数据
     * @param mBarCharts
     * @param color
     */
    private void setEnergyData(BarChart mBarCharts,int[] color) {
        float start = 1f;
        ArrayList<BarEntry> yVals1 = new ArrayList<>();
       /* for (int i = (int) start; i < start + count + 1; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * 100);
            yVals1.add(new BarEntry(i, val));
        }*/

        /*for(int i = 0;i < list.size();i++){
            Float aFloat = Float.valueOf(list.get(i).getEnergydata().get(5));
            yVals1.add(new BarEntry(i+1,aFloat));
            L.e("i=="+list.get(i).getEnergydata().get(5));
        }
        BarDataSet set1;
        if (mBarChart.getData() != null &&
                mBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "设备");
            set1.setColors(color);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);

            data.setValueTextSize(10f);
            //data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);
            mBarChart.setData(data);
        }*/
        List<BarEntry> entries = new ArrayList<>();;
        for(int i = 0;i < list.size();i++){
            Float aFloat = Float.valueOf(list.get(i).getEnergydata().get(5));
            //yVals1.add(new BarEntry(i+1,aFloat));
            L.e("i=="+list.get(i).getEnergydata().get(5));


            entries.add(new BarEntry((float)i, aFloat));
        }

        BarDataSet set = new BarDataSet(entries, "设备");
        //在上面的示例中，BarEntry创建了五个对象并将其添加到a BarDataSet。注意，在第四和第五条目之间的x位置上存在间隙“2”。

        BarData data = new BarData(set);
        //data.setBarWidth(0.9f); //设置自定义条形宽度
        data.setValueTextSize(10f);
        //data.setValueTypeface(mTfLight);
        data.setBarWidth(0.9f);
        mBarCharts.setData(data);
        mBarCharts.invalidate();

    }


    //protected RectF mOnValueSelectedRectF = new RectF();

    /**
     * 柱形图点击回调函数
     * @param e
     * @param h
     */
    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        L.e("id"+(int)h.getX());
        int position = (int)h.getX();
        Intent intent = new Intent(context,EachEnergyActivity.class);
        intent.putExtra("id",list.get(position).getEnergydata().get(0));
        intent.putExtra("name",MyUtils.toUtf8(list.get(position).getEnergydata().get(1)));
        intent.putExtra("data",list.get(position).getEnergydata().get(5));
        startActivity(intent);
        /*if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        mEnergyBarChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mEnergyBarChart.getPosition(e, YAxis.AxisDependency.LEFT);

        L.e("bounds"+bounds.toString());
        L.e("position"+position.toString());

        L.e("low: " + mEnergyBarChart.getLowestVisibleX() + ", high: "
                        + mEnergyBarChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);*/

        /*switch ((int)(e.getData())){
            case 0:
                L.e("正常打卡");
                break;
            case 1:
                L.e("缺卡");
                break;
            case 2:
                L.e("早退");
                break;
            case 3:
                L.e("旷课");
                break;
            case 4:
                L.e("迟到");
                break;
            case 5:
                L.e("请假");
                break;
        }*/
    }


    @Override
    public void onNothingSelected() {

    }

    class MyIAxisValueFormatter implements IAxisValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if(list==null){

                return "";
            }
            L.e("value="+value);
            return str[(int)value];
            //String s = list.get((int) value - 1).getEnergydata().get(1);
            //return "设备"+((int)value+1);
        }

    }


    /**
     * 初始化饼图
     */
    private void loadPieChart(){
        mPieChart = (PieChart) findViewById(R.id.my_pie_chart);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);
        mPieChart.setDragDecelerationFrictionCoef(0.95f);

        //设置中间文件
       //mPieChart.setCenterText(generateCenterSpannableText());

        mPieChart.setDrawHoleEnabled(false);
        mPieChart.setHoleColor(Color.BLUE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        // 触摸旋转
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);


        /*ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0;i < list.size();i++){
            String s = list.get(i).getEnergydata().get(8);
            String name = MyUtils.toUtf8(list.get(i).getEnergydata().get(1));
            entries.add(new PieEntry(Float.valueOf(s),name,i));
        }

        //设置数据
        setData(entries);*/

        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        //变化监听
        //mPieChart.setOnChartValueSelectedListener(this);
    }

    //设置数据
    private void setData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0;i < list.size();i++){
            String s = list.get(i).getEnergydata().get(8);
            String name = MyUtils.toUtf8(list.get(i).getEnergydata().get(1));
            entries.add(new PieEntry(Float.valueOf(s),name,i));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        //dataSet.setDrawValues(false);

        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);
        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
    }


    //设置中间文字
    private SpannableString generateCenterSpannableText() {
        //原文：MPAndroidChart\ndeveloped by Philipp Jahoda
        SpannableString s = new SpannableString("veigar\n的考勤图");
        //s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        //s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        // s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        //s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        // s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        // s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.real_time_menu,menu);
        L.e("oncreate menu");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(menu!=null){
            menu.clear();
        }

        getMenuInflater().inflate(R.menu.real_time_menu,menu);

        /*if(list!=null){
            for(int i = 0;i < list.size();i++){
                String title = MyUtils.toUtf8(list.get(i).getEnergydata().get(1));//电表名字
                //String title = list.get(i).getEnergydata().get(1);//电表名字
                String id = list.get(i).getEnergydata().get(0);
                menu.add(Menu.NONE,Integer.valueOf(id),i+1,title);
                L.e("menu2");
            }
        }else{
            L.e("menu1");
        }*/

        return true;
    }

    /**
     * 获取电表数据
     */
    private void load(){
        mProgress.setVisibility(View.VISIBLE);
        Map<String,String> map = new HashMap<>();
        //map.put("devid","00000001");
        //map.put("sensorid",id);//修改
        map.put("type","3");//修改
        map.put("flag","4");
        String random = String.valueOf(SystemClock.currentThreadTimeMillis());
        String userInformation = String.valueOf(SPUtils.get(context,"userInformation","1111"));
        L.e(userInformation+"---user---");
            map.put("code",userInformation);
        map.put("random",random);
        RequestManager.getInstance().get(RequestAPI.URL+url,new RequestManager.ResponseListener() {
            @Override
            public void onResponse(String s) {
                mProgress.setVisibility(View.GONE);
                L.e("s======="+s);
                try {
                    JSONObject object = new JSONObject(s);
                    Object object2 = object.get("result");
                    list = JSONParseUtils.parseArray(String.valueOf(object2), EnergyData.class);
                    //L.e(MyUtils.toUtf8(list.get(0).getEnergydata().get(1))+list.size());
                    //loadPieChart();
                    //initPowerChart();
                    setData();
                    setEnergyData(mBarChart,ColorTemplate.LIBERTY_COLORS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new RequestManager.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mProgress.setVisibility(View.GONE);
            }
        },map);


    }


}
