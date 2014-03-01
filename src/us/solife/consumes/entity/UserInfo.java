package us.solife.consumes.entity;

import java.io.Serializable;

public class UserInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id, sync;
	private int user_id;
	private String  name, email,gravatar,area;
	private String created_at,updated_at,state;

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

    public void set_name(String name) {
    	this.name = name;
    }
    public String get_name(){
    	return this.name;
    }
    
    public void set_email(String email){
    	this.email = email;
    }
    
    public String get_email(){
    	return this.email;
    }

    public void set_area(String area){
    	this.area = area;
    }
    
    public String get_area(){
    	return this.area;
    }
    
    public void set_updated_at(String created_at){
    	this.updated_at = updated_at;
    }
    
    public String get_updated_at(){
    	return this.updated_at;
    }
    
    public void set_created_at(String created_at){
    	this.created_at = created_at;
    }
    
    public String get_created_at(){
    	return this.created_at;
    }
    
    public void set_gravatar(String gravatar) {
    	this.gravatar = gravatar;
    }
    
    public String get_gravatar(){
    	return this.gravatar;
    }
    
    public void set_sync(Long sync){
    	this.sync = sync;
    }
    
    public Long get_sync(){
    	return this.sync;
    }
    
    public void set_state(String state){
        this.state = state;
    }
    
    public String get_state(){
    	return this.state;
    }
}
