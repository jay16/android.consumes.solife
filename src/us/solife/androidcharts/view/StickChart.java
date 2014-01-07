package us.solife.androidcharts.view;
/*
 * StickChart.java
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


import java.util.ArrayList;
import java.util.List;

import us.solife.androidcharts.entity.StickEntity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * <p>
 * StickChart is a kind of graph that draw the sticks on a GridChart if you want
 * display some moving average lines on this graph, please use see MAStickChart
 * for more information.
 * </p>
 * <p>
 * StickChart��GridChart�α���ǥ��ƥ��å���������饤�����`�ɤǤ����Ƅ�ƽ�����ʤ�
 * ����������ʹ�����������ϡ�MAStickChart�ˤ����դ���������
 * </p>
 * <p>
 * StickChart����GridChart�ϻ�����״���ݵ�ͼ�������Ҫ֧����ʾ���ߣ������ MAStickChart��
 * </p>
 * 
 * @author limc
 * @version v1.0 2011/05/30 14:58:59
 * 
 */
public class StickChart extends GridChart {

	/**
	 * <p>
	 * default color for display stick border
	 * </p>
	 * <p>
	 * ��ʾ���ƥ��å��Υܩ`���`��ɫ�Υǥե���Ȃ�
	 * </p>
	 * <p>
	 * Ĭ�ϱ�ʾ�����ı߿���ɫ
	 * </p>
	 */
	public static final int DEFAULT_STICK_BORDER_COLOR = Color.RED;

	/**
	 * <p>
	 * default color for display stick
	 * </p>
	 * <p>
	 * ��ʾ���ƥ��å���ɫ�Υǥե���Ȃ�
	 * </p>
	 * <p>
	 * Ĭ�ϱ�ʾ�����������ɫ
	 * </p>
	 */
	public static final int DEFAULT_STICK_FILL_COLOR = Color.RED;

	/**
	 * <p>
	 * Color for display stick border
	 * </p>
	 * <p>
	 * ��ʾ���ƥ��å��Υܩ`���`��ɫ
	 * </p>
	 * <p>
	 * ��ʾ�����ı߿���ɫ
	 * </p>
	 */
	private int stickBorderColor = DEFAULT_STICK_BORDER_COLOR;

	/**
	 * <p>
	 * Color for display stick
	 * </p>
	 * <p>
	 * ��ʾ���ƥ��å���ɫ
	 * </p>
	 * <p>
	 * ��ʾ�����������ɫ
	 * </p>
	 */
	private int stickFillColor = DEFAULT_STICK_FILL_COLOR;

	/**
	 * <p>
	 * data to draw sticks
	 * </p>
	 * <p>
	 * ���ƥ��å�������åǩ`��
	 * </p>
	 * <p>
	 * ���������õ�����
	 * </p>
	 */
	private List<StickEntity> StickData;

	/**
	 * <p>
	 * max number of sticks
	 * </p>
	 * <p>
	 * ���ƥ��å�������ʾ��
	 * </p>
	 * <p>
	 * ����������ʾ��
	 * </p>
	 */
	private int maxSticksNum;

	/**
	 * <p>
	 * max value of Y axis
	 * </p>
	 * <p>
	 * Y�S�����
	 * </p>
	 * <p>
	 * Y������ʾֵ
	 * </p>
	 */
	protected float maxValue;

	/**
	 * <p>
	 * min value of Y axis
	 * </p>
	 * <p>
	 * Y�S����С��
	 * </p>
	 * <p>
	 * Y����С��ʾֵ
	 * </p>
	 */
	protected float minValue;

	/*
	 * (non-Javadoc)
	 * 
	 * @param context
	 * 
	 * @see cn.limc.androidcharts.view.BaseChart#BaseChart(Context)
	 */
	public StickChart(Context context) {
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
	 * @see cn.limc.androidcharts.view.BaseChart#BaseChart(Context,
	 * AttributeSet, int)
	 */
	public StickChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @param context
	 * 
	 * @param attrs
	 * 
	 * @see cn.limc.androidcharts.view.BaseChart#BaseChart(Context,
	 * AttributeSet)
	 */
	public StickChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * <p>Called when is going to draw this chart<p> <p>����`�Ȥ����ǰ���᥽�åɤ����<p>
	 * <p>����ͼ��ʱ����<p>
	 * 
	 * @param canvas
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		initAxisY();
		initAxisX();
		super.onDraw(canvas);

		drawSticks(canvas);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @param value
	 * 
	 * @see cn.limc.androidcharts.view.GridChart#getAxisXGraduate(Object)
	 */
	@Override
	public String getAxisXGraduate(Object value) {
		float graduate = Float.valueOf(super.getAxisXGraduate(value));
		int index = (int) Math.floor(graduate * maxSticksNum);

		if (index >= maxSticksNum) {
			index = maxSticksNum - 1;
		} else if (index < 0) {
			index = 0;
		}

		return String.valueOf(StickData.get(index).getDate());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @param value
	 * 
	 * @see cn.limc.androidcharts.view.GridChart#getAxisYGraduate(Object)
	 */
	@Override
	public String getAxisYGraduate(Object value) {
		float graduate = Float.valueOf(super.getAxisYGraduate(value));
		return String.valueOf((int) Math.floor(graduate * (maxValue - minValue)
				+ minValue));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @param value
	 * 
	 * @see
	 * cn.limc.androidcharts.event.ITouchEventResponse#notifyEvent(GridChart)
	 */
	@Override
	public void notifyEvent(GridChart chart) {
		CandleStickChart candlechart = (CandleStickChart) chart;

		this.maxSticksNum = candlechart.getMaxSticksNum();

		super.setDisplayCrossYOnTouch(false);
		// notifyEvent
		super.notifyEvent(chart);
		// notifyEventAll
		super.notifyEventAll(this);
	}

	/**
	 * <p>
	 * initialize degrees on Y axis
	 * </p>
	 * <p>
	 * Y�S��Ŀʢ����ڻ�
	 * </p>
	 * <p>
	 * ��ʼ��Y�������ֵ
	 * </p>
	 */
	protected void initAxisX() {
		List<String> TitleX = new ArrayList<String>();
		if (null != StickData) {
			float average = maxSticksNum / this.getLongitudeNum();
			for (int i = 0; i < this.getLongitudeNum(); i++) {
				int index = (int) Math.floor(i * average);
				if (index > maxSticksNum - 1) {
					index = maxSticksNum - 1;
				}
				TitleX.add(String.valueOf(StickData.get(index).getDate())
						.substring(4));
			}
			TitleX.add(String
					.valueOf(StickData.get(maxSticksNum - 1).getDate())
					.substring(4));
		}
		super.setAxisXTitles(TitleX);
	}

	/**
	 * <p>
	 * get current selected data index
	 * </p>
	 * <p>
	 * �x�k�������ƥ��å��Υ���ǥå���
	 * </p>
	 * <p>
	 * ��ȡ��ǰѡ�е�������index
	 * </p>
	 * 
	 * @return int
	 *         <p>
	 *         index
	 *         </p>
	 *         <p>
	 *         ����ǥå���
	 *         </p>
	 *         <p>
	 *         index
	 *         </p>
	 */
	public int getSelectedIndex() {
		if (null == super.getTouchPoint()) {
			return 0;
		}
		float graduate = Float.valueOf(super.getAxisXGraduate(super
				.getTouchPoint().x));
		int index = (int) Math.floor(graduate * maxSticksNum);

		if (index >= maxSticksNum) {
			index = maxSticksNum - 1;
		} else if (index < 0) {
			index = 0;
		}

		return index;
	}

	/**
	 * <p>
	 * initialize degrees on Y axis
	 * </p>
	 * <p>
	 * Y�S��Ŀʢ����ڻ�
	 * </p>
	 * <p>
	 * ��ʼ��Y�������ֵ
	 * </p>
	 */
	protected void initAxisY() {
		List<String> TitleY = new ArrayList<String>();
		float average = (int) ((maxValue - minValue) / this.getLatitudeNum()) / 100 * 100;
		;
		// calculate degrees on Y axis
		for (int i = 0; i < this.getLatitudeNum(); i++) {
			String value = String.valueOf((int) Math.floor(minValue + i
					* average));
			if (value.length() < super.getAxisYMaxTitleLength()) {
				while (value.length() < super.getAxisYMaxTitleLength()) {
					value = new String(" ") + value;
				}
			}
			TitleY.add(value);
		}
		// calculate last degrees by use max value
		String value = String.valueOf((int) Math
				.floor(((int) maxValue) / 100 * 100));
		if (value.length() < super.getAxisYMaxTitleLength()) {
			while (value.length() < super.getAxisYMaxTitleLength()) {
				value = new String(" ") + value;
			}
		}
		TitleY.add(value);

		super.setAxisYTitles(TitleY);
	}

	/**
	 * <p>
	 * draw sticks
	 * </p>
	 * <p>
	 * ���ƥ��å������
	 * </p>
	 * <p>
	 * ��������
	 * </p>
	 * 
	 * @param canvas
	 */
	protected void drawSticks(Canvas canvas) {
		float stickWidth = ((super.getWidth() - super.getAxisMarginLeft() - super
				.getAxisMarginRight()) / maxSticksNum) - 1;
		float stickX = super.getAxisMarginLeft() + 1;

		Paint mPaintStick = new Paint();
		mPaintStick.setColor(stickFillColor);

		if (null != StickData) {

			for (int i = 0; i < StickData.size(); i++) {
				StickEntity ohlc = StickData.get(i);

				float highY = (float) ((1f - (ohlc.getHigh() - minValue)
						/ (maxValue - minValue))
						* (super.getHeight() - super.getAxisMarginBottom()) - super
						.getAxisMarginTop());
				float lowY = (float) ((1f - (ohlc.getLow() - minValue)
						/ (maxValue - minValue))
						* (super.getHeight() - super.getAxisMarginBottom()) - super
						.getAxisMarginTop());

				// stick or line?
				if (stickWidth >= 2f) {
					canvas.drawRect(stickX, highY, stickX + stickWidth, lowY,
							mPaintStick);
				} else {
					canvas.drawLine(stickX, highY, stickX, lowY, mPaintStick);
				}

				// next x
				stickX = stickX + 1 + stickWidth;
			}
		}
	}

	/**
	 * <p>
	 * add a new stick data to sticks and refresh this chart
	 * </p>
	 * <p>
	 * �¤������ƥ��å��ǩ`����׷�Ӥ��룬�ե�դ��ե쥷��`����
	 * </p>
	 * <p>
	 * ׷��һ�������ݲ�ˢ�µ�ǰͼ��
	 * </p>
	 * 
	 * @param entity
	 *            <p>
	 *            data
	 *            </p>
	 *            <p>
	 *            �ǩ`��
	 *            </p>
	 *            <p>
	 *            ������
	 *            </p>
	 */
	public void pushData(StickEntity entity) {
		if (null != entity) {
			addData(entity);
			super.postInvalidate();
		}
	}

	/**
	 * <p>
	 * add a new stick data to sticks
	 * </p>
	 * <p>
	 * �¤������ƥ��å��ǩ`����׷�Ӥ���
	 * </p>
	 * <p>
	 * ׷��һ��������
	 * </p>
	 * 
	 * @param entity
	 *            <p>
	 *            data
	 *            </p>
	 *            <p>
	 *            �ǩ`��
	 *            </p>
	 *            <p>
	 *            ������
	 *            </p>
	 */
	public void addData(StickEntity entity) {
		if (null != entity) {
			// data is null or empty
			if (null == StickData || 0 == StickData.size()) {
				StickData = new ArrayList<StickEntity>();
				this.maxValue = ((int) entity.getHigh()) / 100 * 100;
			}

			// add
			this.StickData.add(entity);

			if (this.maxValue < entity.getHigh()) {
				this.maxValue = 100 + ((int) entity.getHigh()) / 100 * 100;
			}

			if (StickData.size() > maxSticksNum) {
				maxSticksNum = maxSticksNum + 1;
			}
		}
	}

	/**
	 * @return the stickBorderColor
	 */
	public int getStickBorderColor() {
		return stickBorderColor;
	}

	/**
	 * @param stickBorderColor
	 *            the stickBorderColor to set
	 */
	public void setStickBorderColor(int stickBorderColor) {
		this.stickBorderColor = stickBorderColor;
	}

	/**
	 * @return the stickFillColor
	 */
	public int getStickFillColor() {
		return stickFillColor;
	}

	/**
	 * @param stickFillColor
	 *            the stickFillColor to set
	 */
	public void setStickFillColor(int stickFillColor) {
		this.stickFillColor = stickFillColor;
	}

	/**
	 * @return the stickData
	 */
	public List<StickEntity> getStickData() {
		return StickData;
	}

	/**
	 * @param stickData
	 *            the stickData to set
	 */
	public void setStickData(List<StickEntity> stickData) {
		if (null != StickData) {
			StickData.clear();
		}
		for (StickEntity e : stickData) {
			addData(e);
		}
	}

	/**
	 * @return the maxSticksNum
	 */
	public int getMaxSticksNum() {
		return maxSticksNum;
	}

	/**
	 * @param maxSticksNum
	 *            the maxSticksNum to set
	 */
	public void setMaxSticksNum(int maxSticksNum) {
		this.maxSticksNum = maxSticksNum;
	}

	/**
	 * @return the maxValue
	 */
	public float getMaxValue() {
		return maxValue;
	}

	/**
	 * @param maxValue
	 *            the maxValue to set
	 */
	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	/**
	 * @return the minValue
	 */
	public float getMinValue() {
		return minValue;
	}

	/**
	 * @param minValue
	 *            the minValue to set
	 */
	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}
}

