package us.solife.consumes.api;

import java.io.*;
import java.security.*;

/**
 * String email = "someone@somewhere.com";
 * String hash = Gravatar.get_url(email);
 * @author Administrator
 *
 */
public class Gravatar {
  public static String hex(byte[] array) {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < array.length; ++i) {
      sb.append(Integer.toHexString((array[i]
          & 0xFF) | 0x100).substring(1,3));       
      }
      return sb.toString();
  }
  public static String get_url (String email) {
      try {
      MessageDigest md =
          MessageDigest.getInstance("MD5");
      return hex (md.digest(email.getBytes("CP1252")));
      } catch (NoSuchAlgorithmException e) {
      } catch (UnsupportedEncodingException e) {
      }
      return null;
  }
}