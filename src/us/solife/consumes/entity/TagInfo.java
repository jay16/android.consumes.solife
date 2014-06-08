package us.solife.consumes.entity;

import java.io.Serializable;

public class TagInfo implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long id = (long) -1, sync;
	public int user_id = -1, tag_id = -1, klass = -1;
	public String label, created_at,updated_at,state;

	public long get_id()        { return id; }
	public void set_id(long id) { this.id = id; }
	
	public long get_klass()          { return klass; }
	public void set_klass(int klass) { this.klass = klass; }
	
	public int get_user_id()           { return user_id; }
	public void set_tag_id(int tag_id) { this.tag_id = tag_id; }
	
	public int get_tag_id()              { return tag_id; }
	public void set_user_id(int user_id) { this.user_id = user_id; }
    public void set_label(String label) { this.label = label; }
    public String get_label()           { return this.label;  }
    
    public void set_updated_at(String updated_at) { this.updated_at = updated_at; }
    public String get_updated_at()                { return this.updated_at; }
    
    public void set_created_at(String created_at) { this.created_at = created_at; }
    public String get_created_at()                { return this.created_at; }
    
    public void set_sync(Long sync) { this.sync = sync; }
    public Long get_sync()          { return this.sync; }
    
    public void set_state(String state) { this.state = state; }
    public String get_state()           { return this.state;  }
    
    public String to_string() {
    	return "id:" + this.id +
        "user_id:" + this.user_id +
        "tag_id:" + this.tag_id +
        "label:" + this.label +
        "klass:" + this.klass + 
        "created_at:" + this.created_at+
        "updated_at:" + this.updated_at+
        "sync:" + this.sync+
        "state:" + this.state;
    }
}
