/*
 * OHLCEntity.java
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

/**
 * <p>
 * Entity data which is use for display a candleStick in CandleStickChart use
 * OHLC(Open,High,Low,Close) four values for a candle
 * </p>
 * <p>
 * CandleStickChartのロウソク��燕幣喘デ�`タです、膨云�､鮓饉{喘オブジェクトです。
 * </p>
 * <p>
 * 隠贋政幤�澑輅肖探�OHLC膨倖峙議糞悶斤��
 * </p>
 * 
 * @author limc
 * @version v1.0 2011/05/29 12:19:06
 */
public class OHLCEntity {

	/**
	 * <p>
	 * Open Value
	 * </p>
	 * <p>
	 * 兵��
	 * </p>
	 * <p>
	 * 蝕徒勺
	 * </p>
	 */
	private double open;

	/**
	 * <p>
	 * High Value
	 * </p>
	 * <p>
	 * 互��
	 * </p>
	 * <p>
	 * 恷互勺
	 * </p>
	 * 
	 */
	private double high;

	/**
	 * <p>
	 * Low Value
	 * </p>
	 * <p>
	 * 詰��
	 * </p>
	 * <p>
	 * 恷詰勺
	 * </p>
	 * 
	 */
	private double low;

	/**
	 * <p>
	 * Close Value
	 * </p>
	 * <p>
	 * �K��
	 * </p>
	 * <p>
	 * 辺徒勺
	 * </p>
	 * 
	 */
	private double close;

	/**
	 * <p>
	 * Date
	 * </p>
	 * <p>
	 * 晩原
	 * </p>
	 * <p>
	 * 晩豚
	 * </p>
	 * 
	 */
	private int date;

	/**
	 * 
	 * <p>
	 * Constructor of OHLCEntity
	 * </p>
	 * <p>
	 * OHLCEntity窃斤�鶺長行貂�方
	 * </p>
	 * <p>
	 * OHLCEntityのコンストラクタ�`
	 * </p>
	 * 
	 * @param open
	 *            <p>
	 *            Open Value
	 *            </p>
	 *            <p>
	 *            兵��
	 *            </p>
	 *            <p>
	 *            蝕徒勺
	 *            </p>
	 * @param high
	 *            <p>
	 *            High Value
	 *            </p>
	 *            <p>
	 *            互��
	 *            </p>
	 *            <p>
	 *            恷互勺
	 *            </p>
	 * @param low
	 *            <p>
	 *            Low Value
	 *            </p>
	 *            <p>
	 *            詰��
	 *            </p>
	 *            <p>
	 *            恷詰勺
	 *            </p>
	 * @param close
	 *            <p>
	 *            Close Value
	 *            </p>
	 *            <p>
	 *            �K��
	 *            </p>
	 *            <p>
	 *            辺徒勺
	 *            </p>
	 * @param date
	 *            <p>
	 *            Date
	 *            </p>
	 *            <p>
	 *            晩原
	 *            </p>
	 *            <p>
	 *            晩豚
	 *            </p>
	 */
	public OHLCEntity(double open, double high, double low, double close,
			int date) {
		super();
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.date = date;
	}

	/**
	 * <p>
	 * Constructor of OHLCEntity
	 * </p>
	 * <p>
	 * OHLCEntity窃斤�鶺長行貂�方
	 * </p>
	 * <p>
	 * OHLCEntityのコンストラクタ�`
	 * </p>
	 * 
	 */
	public OHLCEntity() {
		super();
	}

	/**
	 * @return the open
	 */
	public double getOpen() {
		return open;
	}

	/**
	 * @param open
	 *            the open to set
	 */
	public void setOpen(double open) {
		this.open = open;
	}

	/**
	 * @return the high
	 */
	public double getHigh() {
		return high;
	}

	/**
	 * @param high
	 *            the high to set
	 */
	public void setHigh(double high) {
		this.high = high;
	}

	/**
	 * @return the low
	 */
	public double getLow() {
		return low;
	}

	/**
	 * @param low
	 *            the low to set
	 */
	public void setLow(double low) {
		this.low = low;
	}

	/**
	 * @return the close
	 */
	public double getClose() {
		return close;
	}

	/**
	 * @param close
	 *            the close to set
	 */
	public void setClose(double close) {
		this.close = close;
	}

	/**
	 * @return the date
	 */
	public int getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(int date) {
		this.date = date;
	}
}
