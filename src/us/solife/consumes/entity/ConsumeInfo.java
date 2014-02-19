package us.solife.consumes.entity;

import java.io.Serializable;

public class ConsumeInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id, consume_id, user_id;
	private double volue;
	private String  msg, created_at, updated_at,user_name;
	private Long sync; //Boolean ´æ·ÅÎªLong£¬true => 1, flase => 0

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getConsume_id() {
		return consume_id;
	}

	public void setConsume_id(int consume_id) {
		
		this.consume_id = consume_id;
	}
	public double getVolue() {
		return volue;
	}

	public void setVolue(double volue) {
		this.volue = volue;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


	public Long getSync() {
		return sync;
	}

	public void setSync(Long sync) {
		this.sync = sync;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}
	public void setUserName(String user_name) {
		this.user_name = user_name;
	}

	public String getUserName() {
        return user_name.length()==0 ? "unset" : user_name;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	
	public String to_string() {
		return "user_id:" + this.user_id + 
				",consume_id:" + this.consume_id + 
				",volue:" + this.volue + 
				",msg:" + this.msg + 
				",created_at:" + this.created_at + 
				",updated_at:" + this.updated_at;
	}
}
