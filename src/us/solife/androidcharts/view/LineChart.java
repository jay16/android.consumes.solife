package us.solife.androidcharts.view;

/*
 * LineChart.java
 * Android-Charts
 *
 * Created by limc on 2011/05/29.
 *
 * Copyright 2011 limc.cn All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.util.List;

import us.solife.androidcharts.entity.LineEntity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;

/**
 * 
 * <p>
 * LineChart is a kind of graph that draw some lines on a GridChart
 * </p>
 * <p>
 * LineChartはGridChartの燕中でラインを��いたラインチャ�`ドです。
 * </p>
 * <p>
 * LineChart頁壓GridChart貧紙崙匯訳賜謹訳�潴�議夕
 * </p>
 * 
 * @author limc
 * @version v1.0 2011/05/30 14:23:53
 * @see GridChart
 */
public class LineChart extends GridChart {
	/**
	 * <p>
	 * data to draw lines
	 * </p>
	 * <p>
	 * ラインを��く喘デ�`タ
	 * </p>
	 * <p>
	 * 紙崙�潴�喘議方象
	 * </p>
	 */
	private List<LineEntity> lineData;

	/**
	 * <p>
	 * max points of a single line
	 * </p>
	 * <p>
	 * ラインの恷寄ポイント方
	 * </p>
	 * <p>
	 * �潴�議恷寄燕幣泣方
	 * </p>
	 */
	private int maxPointNum;

	/**
	 * <p>
	 * min value of Y axis
	 * </p>
	 * <p>
	 * Y�Sの恷弌��
	 * </p>
	 * <p>
	 * Y議恷弌燕幣峙
	 * </p>
	 */
	private int minValue;

	/**
	 * <p>
	 * max value of Y axis
	 * </p>
	 * <p>
	 * Y�Sの恷寄��
	 * </p>
	 * <p>
	 * Y議恷寄燕幣峙
	 * </p>
	 */
	private int maxValue;

	/*
	 * (non-Javadoc)
	 * 
	 * @param context
	 * 
	 * @see cn.limc.androidcharts.view.GridChart#GridChart(Context)
	 */
	public LineChart(Context context) {
		super(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @param context
	 * 
	 * @param attrs
	 * 
	 * @param defStyle
	 * 
	 * @see cn.limc.androidcharts.view.GridChart#GridChart(Context,
	 * AttributeSet, int)
	 */
	public LineChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @param context
	 * 
	 * @param attrs
	 * 
	 * 
	 * 
	 * @see cn.limc.androidcharts.view.GridChart#GridChart(Context,
	 * AttributeSet)
	 */
	public LineChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * <p>Called when is going to draw this chart<p> <p>チャ�`トを��く念、メソッドを柵ぶ<p>
	 * <p>紙崙夕燕扮距喘<p>
	 * 
	 * @param canvas
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// draw lines
		if (null != this.lineData) {
			drawLines(canvas);
		}
	}

	/**
	 * <p>
	 * draw lines
	 * </p>
	 * <p>
	 * ラインを��く
	 * </p>
	 * <p>
	 * 紙崙�潴�
	 * </p>
	 * 
	 * @param canvas
	 */
	protected void drawLines(Canvas canvas) {
		// distance between two points
		float lineLength = ((super.getWidth() - super.getAxisMarginLeft() - this
				.getMaxPointNum()) / this.getMaxPointNum()) - 1;
		// start point｀s X
		float startX;

		// draw lines
		for (int i = 0; i < lineData.size(); i++) {
			LineEntity line = (LineEntity) lineData.get(i);
			if (line.isDisplay()) {
				Paint mPaint = new Paint();
				mPaint.setColor(line.getLineColor());
				mPaint.setAntiAlias(true);
				List<Float> lineData = line.getLineData();
				// set start point¨s X
				startX = super.getAxisMarginLeft() + lineLength / 2f;
				// start point
				PointF ptFirst = null;
				if (lineData != null) {
					for (int j = 0; j < lineData.size(); j++) {
						float value = lineData.get(j).floatValue();
						// calculate Y
						float valueY = (float) ((1f - (value - this
								.getMinValue())
								/ (this.getMaxValue() - this.getMinValue())) * (super
								.getHeight() - super.getAxisMarginBottom()));

						// if is not last point connect to previous point
						if (j > 0) {
							canvas.drawLine(ptFirst.x, ptFirst.y, startX,
									valueY, mPaint);
						}
						// reset
						ptFirst = new PointF(startX, valueY);
						startX = startX + 1 + lineLength;
					}
				}
			}
		}
	}

	/**
	 * @return the lineData
	 */
	public List<LineEntity> getLineData() {
		return lineData;
	}

	/**
	 * @param lineData
	 *            the lineData to set
	 */
	public void setLineData(List<LineEntity> lineData) {
		this.lineData = lineData;
	}

	/**
	 * @return the maxPointNum
	 */
	public int getMaxPointNum() {
		return maxPointNum;
	}

	/**
	 * @param maxPointNum
	 *            the maxPointNum to set
	 */
	public void setMaxPointNum(int maxPointNum) {
		this.maxPointNum = maxPointNum;
	}

	/**
	 * @return the minValue
	 */
	public int getMinValue() {
		return minValue;
	}

	/**
	 * @param minValue
	 *            the minValue to set
	 */
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	/**
	 * @return the maxValue
	 */
	public int getMaxValue() {
		return maxValue;
	}

	/**
	 * @param maxValue
	 *            the maxValue to set
	 */
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

}
