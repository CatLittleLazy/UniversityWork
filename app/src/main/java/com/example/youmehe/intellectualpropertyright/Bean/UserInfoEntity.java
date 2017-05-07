package com.example.youmehe.intellectualpropertyright.Bean;

/**
 * Created by youmehe on 2017/5/4.
 */

public class UserInfoEntity {
  /**
   * ret_code : 0
   * user_id : 47
   * user_name : 测试账号
   * user_icon : youmehe.wang/universityWork/user_icon/test.png
   * user_sexual : 男
   * user_birthday : 1993-3-13
   * user_type : 0
   * user_area : 陕西省 西安市 莲湖区
   */

  private int ret_code;
  private int user_id;
  private String user_name;
  private String user_icon;
  private String user_sexual;
  private String user_birthday;
  private String user_type;
  private String user_area;

  public int getRet_code() {
    return ret_code;
  }

  public void setRet_code(int ret_code) {
    this.ret_code = ret_code;
  }

  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }

  public String getUser_name() {
    return user_name;
  }

  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }

  public String getUser_icon() {
    return user_icon;
  }

  public void setUser_icon(String user_icon) {
    this.user_icon = user_icon;
  }

  public String getUser_sexual() {
    return user_sexual;
  }

  public void setUser_sexual(String user_sexual) {
    this.user_sexual = user_sexual;
  }

  public String getUser_birthday() {
    return user_birthday;
  }

  public void setUser_birthday(String user_birthday) {
    this.user_birthday = user_birthday;
  }

  public String getUser_type() {
    return user_type;
  }

  public void setUser_type(String user_type) {
    this.user_type = user_type;
  }

  public String getUser_area() {
    return user_area;
  }

  public void setUser_area(String user_area) {
    this.user_area = user_area;
  }
}
