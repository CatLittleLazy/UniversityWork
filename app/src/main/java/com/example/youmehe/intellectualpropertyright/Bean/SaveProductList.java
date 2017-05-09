package com.example.youmehe.intellectualpropertyright.Bean;

import java.util.List;

/**
 * Created by youmehe on 2017/5/9.
 */

public class SaveProductList {

  /**
   * ret_code : 0 result : [{"id":21,"price":1.25,"rating":"1","product_icon":"http://youmehe.wang/product_icon/测试账号_1494148126.png","title":"还没供","saled":1,"content":"打工","owner":"测试账号"},{"id":20,"price":3.69,"rating":"1","product_icon":"http://youmehe.wang/product_icon/测试账号_1494147912.png","title":"沉迷红楼梦","saled":0,"content":"咯倚天屠龙记","owner":"测试账号"},{"id":19,"price":3.69,"rating":"0","product_icon":"http://youmehe.wang/product_icon/测试账号_1494147136.png","title":"沉迷红楼梦","saled":0,"content":"咯倚天屠龙记","owner":"测试账号"},{"id":21,"price":1.25,"rating":"1","product_icon":"http://youmehe.wang/product_icon/测试账号_1494148126.png","title":"还没供","saled":1,"content":"打工","owner":"测试账号"}]
   */

  private int ret_code;
  private List<ResultBean> result;

  public int getRet_code() {
    return ret_code;
  }

  public void setRet_code(int ret_code) {
    this.ret_code = ret_code;
  }

  public List<ResultBean> getResult() {
    return result;
  }

  public void setResult(List<ResultBean> result) {
    this.result = result;
  }

  public static class ResultBean {
    /**
     * id : 21
     * price : 1.25
     * rating : 1
     * product_icon : http://youmehe.wang/product_icon/测试账号_1494148126.png
     * title : 还没供
     * saled : 1
     * content : 打工
     * owner : 测试账号
     */

    private int id;
    private double price;
    private String rating;
    private String product_icon;
    private String title;
    private int saled;
    private String content;
    private String owner;

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public double getPrice() {
      return price;
    }

    public void setPrice(double price) {
      this.price = price;
    }

    public String getRating() {
      return rating;
    }

    public void setRating(String rating) {
      this.rating = rating;
    }

    public String getProduct_icon() {
      return product_icon;
    }

    public void setProduct_icon(String product_icon) {
      this.product_icon = product_icon;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public int getSaled() {
      return saled;
    }

    public void setSaled(int saled) {
      this.saled = saled;
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
  }
}
