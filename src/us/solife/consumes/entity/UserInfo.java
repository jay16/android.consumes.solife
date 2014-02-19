package us.solife.consumes.entity;

import java.io.Serializable;

public class UserInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id, user_id;
	private String  name, email,gravatar;
	private String created_at,updated_at;

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

    public void setName(String name) {
    	this.name = name;
    }
    public String getName(){
    	return this.name;
    }
    
    public void setEmail(String email){
    	this.email = email;
    }
    
    public String getEmail(){
    	return this.email;
    }
    
    public void setUpdated_at(String created_at){
    	this.updated_at = updated_at;
    }
    
    public String getUpdated_at(){
    	return this.updated_at;
    }
    
    public void setCreated_at(String created_at){
    	this.created_at = created_at;
    }
    
    public String getCreated_at(){
    	return this.created_at;
    }
    
    public void setGravatar(String gravatar) {
    	this.gravatar = gravatar;
    }
    
    public String getGravatar(){
    	return this.gravatar;
    }
}
