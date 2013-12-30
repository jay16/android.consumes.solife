package com.yyx.mconsumes.entity;

import java.io.Serializable;

public class ConsumeInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String created_at, msg, updated_at;
	private int consume_id, user_id;
	private double volue;
	private Boolean sync;

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public int getUserId() {
		return user_id;
	}

	public void setUserId(int id) {
		this.user_id = id;
	}

	public int getConsumeId() {
		return consume_id;
	}

	public void setConsumeId(int id) {
		this.consume_id = id;
	}

	public double getVolue() {
		return volue;
	}

	public void setVolue(double volue) {
		this.volue = volue;
	}

	public Boolean getSync() {
		return sync;
	}

	public void setSync(boolean sync) {
		this.sync = sync;
	}

}
