package com.example.youmehe.intellectualpropertyright.Bean;

import java.util.List;

/**
 * Created by youmehe on 2017/5/1.
 */

public class ProductListEntity {

  /**
   * ret_code : 0 total_count : 19 page_count : 4 page_no : 1 ret_result :
   * [{"id":21,"icon":"http://youmehe.wang/product_icon/测试账号_1494148126.png","title":"还没供","price":1.25,"content":"打工","owner":"测试账号","rating":"1","saled":1},{"id":20,"icon":"http://youmehe.wang/product_icon/测试账号_1494147912.png","title":"沉迷红楼梦","price":3.69,"content":"咯倚天屠龙记","owner":"测试账号","rating":"1","saled":0},{"id":19,"icon":"http://youmehe.wang/product_icon/测试账号_1494147136.png","title":"沉迷红楼梦","price":3.69,"content":"咯倚天屠龙记","owner":"测试账号","rating":"0","saled":0},{"id":18,"icon":"http://youmehe.wang/product_icon/测试账号_1494146310.png","title":"测试账号","price":0,"content":"aouoaeuaoeuaoeuoeuoaeuaoeu","owner":"测试账号","rating":"1","saled":0},{"id":17,"icon":"http://youmehe.wang/imgs/测试账号_1494146235.png","title":"测试账号","price":0,"content":"aoeuaoeuaoeuaoeuoeauaoeuoaeuaoe","owner":"测试账号","rating":"2","saled":0}]
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
     * id : 21
     * icon : http://youmehe.wang/product_icon/测试账号_1494148126.png
     * title : 还没供
     * price : 1.25
     * content : 打工
     * owner : 测试账号
     * rating : 1
     * saled : 1
     */

    private int id;
    private String icon;
    private String title;
    private double price;
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

    public double getPrice() {
      return price;
    }

    public void setPrice(double price) {
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