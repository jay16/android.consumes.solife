package us.solife.consumes.entity;

import java.io.Serializable;

public class ConsumeInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long id;
	public int consume_id, user_id;
	public double volue;
	public String  msg, created_at, updated_at,state;
	public Long sync; //Boolean ´æ·ÅÎªLong£¬true => 1, flase => 0

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
	public double get_volue() {
		return volue;
	}

	public void set_volue(double volue) {
		this.volue = volue;
	}

	public String get_msg() {
		return msg;
	}

	public void set_msg(String msg) {
		this.msg = msg;
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

	public String get_updated_at() {
		return updated_at;
	}

	public void set_updated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	
	public String to_string() {
		return "id:" + this.id +
	    "user_id:" + this.user_id + 
		",consume_id:" + this.consume_id + 
		",volue:" + this.volue + 
		",msg:" + this.msg + 
		",created_at:" + this.created_at + 
		",updated_at:" + this.updated_at +
		",sync:" + this.sync +
		",state:" + this.state;
	}
}
