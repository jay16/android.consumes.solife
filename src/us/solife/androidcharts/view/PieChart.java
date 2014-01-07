/*
 * PieChart.java
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

/**
 * 
 */
package us.solife.androidcharts.view;

import java.util.List;

import us.solife.androidcharts.entity.TitleValueColorEntity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;

/**
 * 
 * <p>
 * PieChart is a kind of graph that display all in a pie-like graph, each of the
 * data will get a part of the pie. another kind of pie chart you can refer from
 * PizzaChart
 * </p>
 * <p>
 * PieChart�σҥ���դ�һ�N�Ǥ����ָ��ʾ��ʹ�����ϡ�PizzaChart�����ä��Ƥ�������
 * </p>
 * <p>
 * PieChart����򵥵ı�ͼ���������Ҫ���Էָ��ʾ�ı�ͼ�������PizzaChart
 * </p>
 * 
 * @author limc
 * @version v1.0 2011/05/29 15:15:03
 * 
 */
public class PieChart extends BaseChart {

	/**
	 * <p>
	 * default title
	 * </p>
	 * <p>
	 * �����ȥ�Υǥե���Ȃ�
	 * </p>
	 * <p>
	 * Ĭ��ͼ�����
	 * </p>
	 */
	public static final String DEFAULT_TITLE = "Pie Chart";

	/**
	 * <p>
	 * default should display longitude lines
	 * </p>
	 * <p>
	 * �U�����ʾ����
	 * </p>
	 * <p>
	 * Ĭ���Ƿ���ʾ����
	 * </p>
	 */
	public static final boolean DEFAULT_DISPLAY_RADIUS = true;

	/**
	 * <p>
	 * default radius length
	 * </p>
	 * <p>
	 * �뾶���L���Υǥե���Ȃ�
	 * </p>
	 * <p>
	 * Ĭ�ϰ뾶����
	 * </p>
	 */
	public static final int DEFAULT_RADIUS_LENGTH = 80;

	/**
	 * <p>
	 * default color for longitude lines
	 * </p>
	 * <p>
	 * �U����ɫ�Υǥե���Ȃ�
	 * </p>
	 * <p>
	 * Ĭ�Ͼ�����ɫ
	 * </p>
	 */
	public static final int DEFAULT_RADIUS_COLOR = Color.WHITE;

	/**
	 * <p>
	 * default color for circle's border
	 * </p>
	 * <p>
	 * �һ���ɫ�Υǥե���Ȃ�
	 * </p>
	 * <p>
	 * Ĭ��Բ������ɫ
	 * </p>
	 */
	public static final int DEFAULT_CIRCLE_BORDER_COLOR = Color.WHITE;

	/**
	 * <p>
	 * default position
	 * </p>
	 * <p>
	 * ���Ĥ�λ�äΥǥե���Ȃ�
	 * </p>
	 * <p>
	 * Ĭ�ϻ�ͼ����λ��
	 * </p>
	 */
	public static final Point DEFAULT_POSITION = new Point(0, 0);

	/**
	 * <p>
	 * Data
	 * </p>
	 * <p>
	 * �ǩ`��
	 * </p>
	 * <p>
	 * ͼ������
	 * </p>
	 */
	private List<TitleValueColorEntity> data;

	/**
	 * <p>
	 * title
	 * </p>
	 * <p>
	 * �����ȥ�
	 * </p>
	 * <p>
	 * ͼ�����
	 * </p>
	 */
	private String title = DEFAULT_TITLE;

	/**
	 * <p>
	 * position
	 * </p>
	 * <p>
	 * ����λ��
	 * </p>
	 * <p>
	 * ��ͼ����λ��
	 * </p>
	 */
	private Point position = DEFAULT_POSITION;

	/**
	 * <p>
	 * radius length
	 * </p>
	 * <p>
	 * �뾶���L��
	 * </p>
	 * <p>
	 * �뾶����
	 * </p>
	 */
	private int radiusLength = DEFAULT_RADIUS_LENGTH;

	/**
	 * <p>
	 * Color for longitude lines
	 * </p>
	 * <p>
	 * �U����ɫ
	 * </p>
	 * <p>
	 * ������ɫ
	 * </p>
	 */
	private int radiusColor = DEFAULT_RADIUS_COLOR;

	/**
	 * <p>
	 * Color for circle's border
	 * </p>
	 * <p>
	 * �һ���ɫ
	 * </p>
	 * <p>
	 * Բ������ɫ
	 * </p>
	 */
	private int circleBorderColor = DEFAULT_CIRCLE_BORDER_COLOR;

	/**
	 * <p>
	 * should display the longitude lines
	 * </p>
	 * <p>
	 * �U�����ʾ����?
	 * </p>
	 * <p>
	 * �Ƿ���ʾ����
	 * </p>
	 */
	private boolean displayRadius = DEFAULT_DISPLAY_RADIUS;

	/*
	 * (non-Javadoc)
	 * 
	 * @param context
	 * 
	 * @see cn.limc.androidcharts.view.BaseChart#BaseChart(Context)
	 */
	public PieChart(Context context) {
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
	public PieChart(Context context, AttributeSet attrs, int defStyle) {
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
	public PieChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * <p>Called when is going to draw this chart</p>
	 * <p>����`�Ȥ����ǰ���᥽�åɤ����</p>
	 * <p>����ͼ��ʱ����</p>
	 * 
	 * @param canvas
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// get safe rect
		int rect = super.getWidth() > super.getHeight() ? super.getHeight()
				: super.getWidth();

		// calculate radius length
		radiusLength = (int) ((rect / 2f) * 0.90);

		// calculate position
		position = new Point((int) (getWidth() / 2f), (int) (getHeight() / 2f));

		// draw this chart
		drawCircle(canvas);

		// draw data on chart
		drawData(canvas);
	}

	/**
	 * <p>
	 * Draw a circle
	 * </p>
	 * <p>
	 * ��������
	 * </p>
	 * <p>
	 * ����һ��Բ
	 * </p>
	 * 
	 * @param canvas
	 */
	protected void drawCircle(Canvas canvas) {

		Paint mPaintCircleBorder = new Paint();
		mPaintCircleBorder.setColor(Color.WHITE);
		mPaintCircleBorder.setStyle(Style.STROKE);
		mPaintCircleBorder.setStrokeWidth(2);
		mPaintCircleBorder.setAntiAlias(true);

		// draw a circle
		canvas.drawCircle(position.x, position.y, radiusLength,
				mPaintCircleBorder);
	}

	/**
	 * <p>
	 * Draw the data
	 * </p>
	 * <p>
	 * ����`�Ȥǥǩ`�������
	 * </p>
	 * <p>
	 * �����ݻ�����ͼ����
	 * </p>
	 * 
	 * @param canvas
	 */
	protected void drawData(Canvas canvas) {
		if (null != data) {
			// sum all data's value
			float sum = 0;
			for (int i = 0; i < data.size(); i++) {
				sum = sum + data.get(i).getValue();
			}

			Paint mPaintFill = new Paint();
			mPaintFill.setStyle(Style.FILL);
			mPaintFill.setAntiAlias(true);

			Paint mPaintBorder = new Paint();
			mPaintBorder.setStyle(Style.STROKE);
			mPaintBorder.setColor(radiusColor);
			mPaintBorder.setAntiAlias(true);

			int offset = -90;
			// draw arcs of every piece
			for (int j = 0; j < data.size(); j++) {
				TitleValueColorEntity e = data.get(j);

				// get color
				mPaintFill.setColor(e.getColor());

				RectF oval = new RectF(position.x - radiusLength, position.y
						- radiusLength, position.x + radiusLength, position.y
						+ radiusLength);
				int sweep = Math.round(e.getValue() / sum * 360f);
				canvas.drawArc(oval, offset, sweep, true, mPaintFill);
				canvas.drawArc(oval, offset, sweep, true, mPaintBorder);
				offset = offset + sweep;
			}

			float sumvalue = 0f;
			for (int k = 0; k < data.size(); k++) {
				TitleValueColorEntity e = data.get(k);
				float value = e.getValue();
				sumvalue = sumvalue + value;
				float rate = (sumvalue - value / 2) / sum;
				mPaintFill.setColor(Color.BLUE);

				// percentage
				float percentage = (int) (value / sum * 10000) / 100f;

				float offsetX = (float) (position.x - radiusLength * 0.5
						* Math.sin(rate * -2 * Math.PI));
				float offsetY = (float) (position.y - radiusLength * 0.5
						* Math.cos(rate * -2 * Math.PI));

				Paint mPaintFont = new Paint();
				mPaintFont.setColor(Color.LTGRAY);

				// draw titles
				String title = e.getTitle();
				float realx = 0;
				float realy = 0;

				// TODO title position
				if (offsetX < position.x) {
					realx = offsetX - mPaintFont.measureText(title) - 5;
				} else if (offsetX > position.x) {
					realx = offsetX + 5;
				}

				if (offsetY > position.y) {
					if (value / sum < 0.2f) {
						realy = offsetY + 10;
					} else {
						realy = offsetY + 5;
					}
				} else if (offsetY < position.y) {
					if (value / sum < 0.2f) {
						realy = offsetY - 10;
					} else {
						realy = offsetY + 5;
					}
				}

				canvas.drawText(title, realx, realy, mPaintFont);
				canvas.drawText(String.valueOf(percentage) + "%", realx,
						realy + 12, mPaintFont);
			}
		}
	}

	/**
	 * @return the data
	 */
	public List<TitleValueColorEntity> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<TitleValueColorEntity> data) {
		this.data = data;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the position
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(Point position) {
		this.position = position;
	}

	/**
	 * @return the radiusLength
	 */
	public int getRadiusLength() {
		return radiusLength;
	}

	/**
	 * @param radiusLength
	 *            the radiusLength to set
	 */
	public void setRadiusLength(int radiusLength) {
		this.radiusLength = radiusLength;
	}

	/**
	 * @return the radiusColor
	 */
	public int getRadiusColor() {
		return radiusColor;
	}

	/**
	 * @param radiusColor
	 *            the radiusColor to set
	 */
	public void setRadiusColor(int radiusColor) {
		this.radiusColor = radiusColor;
	}

	/**
	 * @return the circleBorderColor
	 */
	public int getCircleBorderColor() {
		return circleBorderColor;
	}

	/**
	 * @param circleBorderColor
	 *            the circleBorderColor to set
	 */
	public void setCircleBorderColor(int circleBorderColor) {
		this.circleBorderColor = circleBorderColor;
	}

	/**
	 * @return the displayRadius
	 */
	public boolean isDisplayRadius() {
		return displayRadius;
	}

	/**
	 * @param displayRadius
	 *            the displayRadius to set
	 */
	public void setDisplayRadius(boolean displayRadius) {
		this.displayRadius = displayRadius;
	}
}
