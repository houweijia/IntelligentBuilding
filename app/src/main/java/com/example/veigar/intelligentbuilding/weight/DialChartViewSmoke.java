package com.example.veigar.intelligentbuilding.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import org.xclcharts.chart.DialChart;
import org.xclcharts.view.GraphicalView;

import java.util.ArrayList;
import java.util.List;

public class DialChartViewSmoke extends GraphicalView {

	private String TAG = "DialChart05View";
	private DialChart chart = new DialChart();
	private float mPercentage = 0.1f;

	float mP1 = 0.0f;
	float mP2 = 0.0f;

	public DialChartViewSmoke(Context context) {
		super(context);
		initView();
	}

	public DialChartViewSmoke(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public DialChartViewSmoke(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		chartRender();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		chart.setChartRange(w, h);
	}

	public void chartRender() {
		try {

			// 设置标题背景
			chart.setApplyBackgroundColor(false);
			chart.setBackgroundColor(Color.rgb(255, 255, 255));
			// 绘制边框
			//chart.showRoundBorder();

			// 设置当前百分比
			chart.getPointer().setPercentage(mPercentage);

			// 设置指针长度
			chart.getPointer().setLength(0.6f);

			// 增加轴
			addAxis();
			// ///////////////////////////////////////////////////////////
			addPointer();
			// 设置附加信息
			addAttrInfo();
			// ///////////////////////////////////////////////////////////

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}

	}

	public void addAxis() {
		//绘制外圈数字
		List<String> rlabels = new ArrayList<String>();
		int j = 0;
		for (int i = 0; i <= 200;) {
			if (0 == i || j == 2) {
				rlabels.add(Integer.toString(i));
				j = 0;
			} else {
				rlabels.add("");
				j++;
			}

			i += 5;
		}
		chart.addOuterTicksAxis(0.7f, rlabels);

		// 环形颜色轴
		List<Float> ringPercentage = new ArrayList<Float>();
		ringPercentage.add(0.33f);
		ringPercentage.add(0.33f);
		ringPercentage.add(1 - 2 * 0.33f);

		List<Integer> rcolor = new ArrayList<Integer>();
		rcolor.add(Color.rgb(133, 206, 130));
		rcolor.add(Color.rgb(252, 210, 9));
		rcolor.add(Color.rgb(229, 63, 56));
		chart.addStrokeRingAxis(0.7f, 0.6f, ringPercentage, rcolor);

		//绘制内圈数字
		/*List<String> rlabels2 = new ArrayList<String>();
		for (int i = 0; i <= 8; i++) {
			rlabels2.add(Integer.toString(i) + "M");
		}
		chart.addInnerTicksAxis(0.6f, rlabels2);

		chart.getPlotAxis().get(1).getFillAxisPaint()
				.setColor(Color.rgb(28, 129, 243));

		chart.getPlotAxis().get(0).hideAxisLine();
		chart.getPlotAxis().get(2).hideAxisLine();
		chart.getPlotAxis().get(0).getTickMarksPaint().setColor(Color.YELLOW);
		chart.getPlotAxis().get(2).getTickMarksPaint().setColor(Color.WHITE);
		chart.getPlotAxis().get(2).getTickLabelPaint().setColor(Color.WHITE);*/

	}

	private void addAttrInfo() {

	}

	public void addPointer() {

	}

	public void setCurrentStatus(float percentage) {
		// 清理
		chart.clearAll();

		mPercentage = percentage;
		// 设置当前百分比
		chart.getPointer().setPercentage(mPercentage);
		addAxis();
		addPointer();
		addAttrInfo();
	}

	@Override
	public void render(Canvas canvas) {
		try {
			chart.render(canvas);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

}
