package us.solife.consumes.api;

import java.io.*;
import java.security.*;

/**
 * Gravatar.gravatar_path(email)
 * Gravatar.gravatar_url(email)
 *
 */
public class Gravatar {
  public static String hex(byte[] array) {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < array.length; ++i) {
      sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));       
      }
      return sb.toString();
  }
  public static String get_md5 (String email) {
      try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      return hex (md.digest(email.getBytes("CP1252")));
      } catch (NoSuchAlgorithmException e) {
      } catch (UnsupportedEncodingException e) {
      }
      return "error";
  }
  
  public static String gravatar_url (String email) {  
      return URLs.GRAVATAR_BASE_URL+get_md5(email);
  }
  
  public static String gravatar_path(String email) {
	  File path = new File(URLs.STORAGE_GRAVATAR);
	  if(!path.exists())  path.mkdir();
	  
	  return URLs.STORAGE_GRAVATAR + "/" + get_md5(email) + ".jpg";
  }
}