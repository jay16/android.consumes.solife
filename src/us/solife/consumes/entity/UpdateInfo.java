package us.solife.consumes.entity;


public class UpdateInfo {

	private String version;
	private String url;
	private String description;
	private String apk_name;

	public String get_apk_name() {
		return apk_name;
	}
	public void set_apk_name(String apk_name) {
		this.apk_name = apk_name;
	}
	
	public String get_version() {
		return version;
	}
	public void set_version(String versoin) {
		this.version = versoin;
	}
	public String get_url() {
		return url;
	}
	public void set_url(String url) {
		this.url = url;
	}
	public String get_description() {
		return description;
	}
	public void set_description(String description) {
		this.description = description;
	}
	public String to_string() {
		return "Version-Info:"+
	    "version:" + version +
	    "apk_name:" + apk_name +
	    "url:" + url +
	    "description" + description;
	}
}
