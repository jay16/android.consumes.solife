package us.solife.consumes.entity;

import java.io.Serializable;

public class ConsumeInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long id;
	public int consume_id, user_id, klass;
	public double value;
	public String ymdhms, remark, created_at, updated_at, tags_list;
	public String state;
	public Long sync; //Boolean ���ΪLong��true => 1, flase => 0

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
	
	public int get_klass() {
		return klass;
	}
	public void set_klass(int klass) {
		this.klass = klass;
	}

	public int get_consume_id() {
		return consume_id;
	}

	public void set_consume_id(int l) {
		
		this.consume_id = l;
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
	
	public String get_updated_at() {
		return updated_at;
	}
	public void set_updated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	
	public String get_tags_list() {
		return tags_list;
	}
	public void set_tags_list(String tags_list) {
		this.tags_list = tags_list;
	}

	public String to_string() {
		return "id:" + this.id +
	    "user_id:" + this.user_id + 
		",consume_id:" + this.consume_id + 
		",value:" + this.value + 
		",remark:" + this.remark + 
		",ymdhms:" + this.ymdhms + 
		",klass:" + this.klass + 
		",tags_list:" + this.tags_list + 
		",created_at:" + this.created_at + 
		",updated_at:" + this.updated_at + 
		",sync:" + this.sync +
		",state:" + this.state;
	}
}
