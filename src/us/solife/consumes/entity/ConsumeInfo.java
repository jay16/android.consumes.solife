package us.solife.consumes.entity;

import java.io.Serializable;

public class ConsumeInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long id;
	public int consume_id, user_id;
	public double value;
	public String ymdhms, remark, created_at;
	public String state;
	public Long sync; //Boolean ´æ·ÅÎªLong£¬true => 1, flase => 0

	public String get_ymdhms() {
		return ymdhms;
	}

	public void set_ymdhms(String ymdhms) {
		this.ymdhms = ymdhms;
	}
	
	public long get_id() {
		return id;
	}

	public void set_id(long id) {
		this.id = id;
	}
	public int get_user_id() {
		return user_id;
	}

	public void set_user_id(int user_id) {
		this.user_id = user_id;
	}

	public int get_consume_id() {
		return consume_id;
	}

	public void set_consume_id(int consume_id) {
		
		this.consume_id = consume_id;
	}
	public double get_value() {
		return value;
	}

	public void set_value(double value) {
		this.value = value;
	}

	public String get_remark() {
		return remark;
	}

	public void set_remark(String remark) {
		this.remark = remark;
	}


	public Long get_sync() {
		return sync;
	}

	public void set_sync(Long sync) {
		this.sync = sync;
	}


	public String get_state() {
		return this.state;
	}

	public void set_state(String state) {
		this.state = state;
	}
	public String get_created_at() {
		return created_at;
	}

	public void set_created_at(String created_at) {
		this.created_at = created_at;
	}

	public String to_string() {
		return "id:" + this.id +
	    "user_id:" + this.user_id + 
		",consume_id:" + this.consume_id + 
		",value:" + this.value + 
		",remark:" + this.remark + 
		",created_at:" + this.created_at + 
		",sync:" + this.sync +
		",state:" + this.state;
	}
}
