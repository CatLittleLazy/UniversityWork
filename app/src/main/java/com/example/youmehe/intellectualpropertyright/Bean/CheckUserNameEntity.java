package com.example.youmehe.intellectualpropertyright.Bean;

/**
 * Created by youmehe on 2017/4/30.
 */

public class CheckUserNameEntity {
  /**
   * ret_code : 0
   * return_user_name : test
   * code : QWVQ13
   */

  private int ret_code;
  private String return_user_name;
  private String code;

  public int getRet_code() {
    return ret_code;
  }

  public void setRet_code(int ret_code) {
    this.ret_code = ret_code;
  }

  public String getReturn_user_name() {
    return return_user_name;
  }

  public void setReturn_user_name(String return_user_name) {
    this.return_user_name = return_user_name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
