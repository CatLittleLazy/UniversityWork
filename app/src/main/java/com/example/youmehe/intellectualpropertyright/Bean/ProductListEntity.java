package com.example.youmehe.intellectualpropertyright.Bean;

import java.util.List;

/**
 * Created by youmehe on 2017/5/1.
 */

public class ProductListEntity {

  /**
   * ret_code : 0 total_count : 19 page_count : 4 page_no : 1 ret_result :
   * [{"id":1,"icon":"http://youmehe.wang/universityWork/product_icon/icon_1.jpg","title":"【新知青】专利申请/代理
   * 发明实用新型外观专利商标注册版权登","price":"20.00","content":"oeuaoeouaoeuaoeu","owner":"测试账号","rating":"","saled":0},{"id":2,"icon":"http://youmehe.wang/universityWork/product_icon/icon_2.jpg","title":"外观专利/著作版权/实用性新型专利/发明/申请/保护/维权/","price":"850.00","content":"uoeauaoeuaoeuaoe","owner":"测试账号","rating":"","saled":0},{"id":3,"icon":"http://youmehe.wang/universityWork/product_icon/icon_3.jpg","title":"专利申请/专利转让-实用新型专利/外观设计/软件著作权/版权","price":"295.00","content":"uoeuaoeuoeauaoeuoeu","owner":"测试账号","rating":"","saled":0},{"id":4,"icon":"http://youmehe.wang/universityWork/product_icon/icon_4.jpg","title":"包拿证书专利申请/专利代理/（实用新型、外观设计）","price":"75.00","content":"aoueoeauoeauoeauaoeu","owner":"测试账号","rating":"","saled":0},{"id":5,"icon":"http://youmehe.wang/universityWork/product_icon/icon_5.jpg","title":"专利申请发明实用新型外观专利代理转让代写撰写商标专利申请加急","price":"100.00","content":"aoeuoaeuaoeuoaeuoeuoaeu","owner":"测试账号","rating":"","saled":0}]
   */

  private int ret_code;
  private int total_count;
  private int page_count;
  private String page_no;
  private List<RetResultBean> ret_result;

  public int getRet_code() {
    return ret_code;
  }

  public void setRet_code(int ret_code) {
    this.ret_code = ret_code;
  }

  public int getTotal_count() {
    return total_count;
  }

  public void setTotal_count(int total_count) {
    this.total_count = total_count;
  }

  public int getPage_count() {
    return page_count;
  }

  public void setPage_count(int page_count) {
    this.page_count = page_count;
  }

  public String getPage_no() {
    return page_no;
  }

  public void setPage_no(String page_no) {
    this.page_no = page_no;
  }

  public List<RetResultBean> getRet_result() {
    return ret_result;
  }

  public void setRet_result(List<RetResultBean> ret_result) {
    this.ret_result = ret_result;
  }

  public static class RetResultBean {
    /**
     * id : 1
     * icon : http://youmehe.wang/universityWork/product_icon/icon_1.jpg
     * title : 【新知青】专利申请/代理 发明实用新型外观专利商标注册版权登
     * price : 20.00
     * content : oeuaoeouaoeuaoeu
     * owner : 测试账号
     * rating :
     * saled : 0
     */

    private int id;
    private String icon;
    private String title;
    private String price;
    private String content;
    private String owner;
    private String rating;
    private int saled;

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getIcon() {
      return icon;
    }

    public void setIcon(String icon) {
      this.icon = icon;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getPrice() {
      return price;
    }

    public void setPrice(String price) {
      this.price = price;
    }

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public String getOwner() {
      return owner;
    }

    public void setOwner(String owner) {
      this.owner = owner;
    }

    public String getRating() {
      return rating;
    }

    public void setRating(String rating) {
      this.rating = rating;
    }

    public int getSaled() {
      return saled;
    }

    public void setSaled(int saled) {
      this.saled = saled;
    }
  }
}