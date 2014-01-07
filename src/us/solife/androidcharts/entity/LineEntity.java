/*
 * LineEntity.java
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

package us.solife.androidcharts.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Entity data which is use for display a single line in LineChart</p>
 * <p>LineChart�ξ���ʾ�åǩ`���Ǥ����g���Ǥ���</p>
 * <p>������ͼ��ʾ�õ����ߵĶ��󡢶����ߵ�ʱ����ʹ����Ӧ�����ݽṹ��������</p>
 * @author limc 
 * @version v1.0 2011/05/29 12:24:49 
 */
public class LineEntity {
	
	/** 
	 * <p>Data for draw this line</p>
	 * <p>�饤����ʾ�åǩ`��</p>
	 * <p>�߱�ʾ����</p>
	 */
	private List<Float> lineData;
	
	/**
	 * <p>Title for this line</p>
	 * <p>�饤��α�ʾ�����ȥ�</p>
	 * <p>�ߵı��⣬���ڱ�ʶ��������</p>
	 */
	private String title;
	
	/**
	 * <p>Line Color</p>
	 * <p>�饤���ɫ</p>
	 * <p>�ߵ���ɫ</p>
	 */
	private int lineColor;
	
	/** 
	 * <p>Should display this line?</p>
	 * <p>�饤������`�ɤǱ���Ǳ�ʾ���뤫?</p>
	 * <p>�Ƿ���ͼ������ʾ����</p>
	 */
	private boolean display = true;
	
	/**
	 * <p>Constructor of LineEntity</p>
	 * <p>LineEntity�����Ĺ��캯��</p>
	 * <p>LineEntity�Υ��󥹥ȥ饯���`</p>
	 *
	 */
	public LineEntity() {
		super();
	}

	/**
	 * <p>Constructor of LineEntity</p>
	 * <p>LineEntity�����Ĺ��캯��</p>
	 * <p>LineEntity�Υ��󥹥ȥ饯���`</p>
	 *
	 * @param lineData
	 * <p>Data for draw this line</p>
	 * <p>�饤����ʾ�åǩ`��</p>
	 * <p>�߱�ʾ����</p>
	 * @param title
	 * <p>Title for this line</p>
	 * <p>�饤��α�ʾ�����ȥ�</p>
	 * <p>�ߵı��⣬���ڱ�ʶ��������</p>
	 * @param lineColor 
	 * <p>Line Color</p>
	 * <p>�饤���ɫ</p>
	 * <p>�ߵ���ɫ</p>
	 */
	public LineEntity(List<Float> lineData, String title, int lineColor) {
		super();
		this.lineData = lineData;
		this.title = title;
		this.lineColor = lineColor;
	}
	
	/**
	 * @param value 
	 */
	public void put(float value){
		if (null == lineData){
			lineData = new ArrayList<Float>();
		}
		lineData.add(value);
	}

	/**
	 * @return the lineData
	 */
	public List<Float> getLineData() {
		return lineData;
	}

	/**
	 * @param lineData the lineData to set
	 */
	public void setLineData(List<Float> lineData) {
		this.lineData = lineData;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the lineColor
	 */
	public int getLineColor() {
		return lineColor;
	}

	/**
	 * @param lineColor the lineColor to set
	 */
	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
	}

	/**
	 * @return the display
	 */
	public boolean isDisplay() {
		return display;
	}

	/**
	 * @param display the display to set
	 */
	public void setDisplay(boolean display) {
		this.display = display;
	}	
}
