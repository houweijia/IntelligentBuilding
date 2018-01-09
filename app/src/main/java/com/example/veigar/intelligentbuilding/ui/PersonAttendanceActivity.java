package com.example.veigar.intelligentbuilding.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.adapter.AttendanceAdapter;
import com.example.veigar.intelligentbuilding.base.BaseActivity;
import com.example.veigar.intelligentbuilding.bean.Result;
import com.example.veigar.intelligentbuilding.bean.Result1;
import com.example.veigar.intelligentbuilding.util.JSONParseUtils;
import com.example.veigar.intelligentbuilding.util.L;
import com.example.veigar.intelligentbuilding.weight.ListViewForScroll;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PersonAttendanceActivity extends BaseActivity implements OnChartValueSelectedListener{

    private LinearLayout mLinear;
    private List<Result1> personAttendanceDatas;
    private View view;
    private ListViewForScroll mList;
    private PieChart mPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_attendance);
        String s4 = "{\"resultcode\":\"200\",\"reason\":\"success\",\"result\":[{\"dev0\":[\"1\",\"1\"]}],\"error_code\":\"0\"}";
        String str = "{\"code\":\"0\",\"result1\":[{\"a\":\"1\"}],\"result2\":[{\"b\":1}]}";
        String s = "{\"result1\":[{\"time\":\"2017年03月06日(星期一)08:53\"},{\"time\":\"2017年03月07日(星期二)08:23\"},{\"time\":\"2017年03月08日(星期三)07:53\"},{\"time\":\"2017年03月18日(星期三)13:52\"}]}";
        String s2 = "{\"result1\":[{\"time\":\"2017年03月06日(星期一)08:53\"},{\"time\":\"2017年03月07日(星期二)08:23\"}]}";
        String s3 = "{\"result1\":[{\"time\":\"2017年03月06日(星期一)08:53\"},{\"time\":\"2017年03月07日(星期二)08:23\"},{\"time\":\"2017年03月08日(星期三)07:53\"}]}";
        try {
            JSONObject object = new JSONObject(s);
            Object result1 = object.get("result1");
            personAttendanceDatas = JSONParseUtils.parseArray(String.valueOf(result1), Result1.class);
            JSONObject object1 = new JSONObject(s4);
            Object result2 = object1.get("result");
            List<Result> results = JSONParseUtils.parseArray(String.valueOf(result2), Result.class);
            L.e("results====="+results.get(0).getDev());
            L.e("执行");
            L.e(personAttendanceDatas.size()+"");
        } catch (JSONException e) {
            L.e("未执行");
            e.printStackTrace();
        }

        //List<PersonAttendanceData> personAttendanceDatas = JSONParseUtils.parseArray(result1, PersonAttendanceData.class);
        //L.e("list===="+personAttendanceDatas.size());

        initView();
    }

    private void initView() {
        initToolBar();
        setPageTitle("门禁考勤");
        mLinear = (LinearLayout) findViewById(R.id.my_linear);
        mLinear.addView(addMyView());
        LayoutInflater inflater = LayoutInflater.from(context);
        View headView = inflater.inflate(R.layout.attendance_list_head_view,null);
        TextView headTv = (TextView) headView.findViewById(R.id.head_tv);

        mList.addHeaderView(headView);
        mList.setAdapter(new AttendanceAdapter(context,personAttendanceDatas,R.layout.layout));
        loadPieChart();

    }

    private View addMyView(){
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.attendance_listview,null);
        mList = (ListViewForScroll) view.findViewById(R.id.my_list);
        return view;
    }

    private void loadPieChart(){
        mPieChart = (PieChart) findViewById(R.id.mPieChart);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);
        mPieChart.setDragDecelerationFrictionCoef(0.95f);

        //设置中间文件
        mPieChart.setCenterText(generateCenterSpannableText());

        mPieChart.setDrawHoleEnabled(false);
        mPieChart.setHoleColor(Color.BLUE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        // 触摸旋转
        mPieChart.setRotationEnabled(false);
        mPieChart.setHighlightPerTapEnabled(false);


        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(20, "正常打卡",0));
        entries.add(new PieEntry(1, "缺卡",1));
        entries.add(new PieEntry(3, "早退",2));
        entries.add(new PieEntry(0, "旷课",3));
        entries.add(new PieEntry(4, "迟到",4));
        entries.add(new PieEntry(1, "请假",5));

        //设置数据
        setData(entries);

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
        mPieChart.setOnChartValueSelectedListener(this);
    }

    //设置数据
    private void setData(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "三年二班");
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
    public void onValueSelected(Entry e, Highlight h) {
        switch ((int)(e.getData())){
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
        }

    }

    @Override
    public void onNothingSelected() {
        L.e("onNothingSelected");
    }
}
