package us.solife.consumes.entity;

import java.io.Serializable;

public class UserInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long id = (long) -1, sync;
	public int user_id = -1;
	public String  name, email,gravatar,area,info;
	public String created_at,updated_at,state;

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

    public void set_info(String info){
    	this.info = info;
    }
    
    public String get_info(){
    	return this.info;
    }
    
    public void set_area(String area){
    	this.area = area;
    }
    
    public String get_area(){
    	return this.area;
    }
    
    public void set_updated_at(String updated_at){
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
    
    public String to_string() {
    	return "user_id:"+ this.user_id +
        "id:" + this.id +
        "name:" + this.name +
        "email:" + this.email + 
        "created_at:" + this.created_at+
        "updated_at:" + this.updated_at+
        "info:" + this.info;
        
    }
}
