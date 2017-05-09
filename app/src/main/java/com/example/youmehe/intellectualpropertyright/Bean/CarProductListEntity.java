package com.example.youmehe.intellectualpropertyright.Bean;

import java.util.List;

/**
 * Created by youmehe on 2017/5/9.
 */

public class CarProductListEntity {

  /**
   * ret_code : 0 result : [{"num":4,"id":19,"price":3.69,"rating":"0","product_icon":"http://youmehe.wang/product_icon/测试账号_1494147136.png","title":"沉迷红楼梦","saled":0},{"num":12,"id":5,"price":100,"rating":"3","product_icon":"http://youmehe.wang/universityWork/product_icon/icon_5.jpg","title":"专利申请发明实用新型外观专利代理转让代写撰写商标专利申请加急","saled":0}]
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

  public boolean isAllSelect() {
    if (result.size() == 1) {
      return true;
    }
    boolean select = true;
    for (ResultBean temp : result) {
      select &= temp.isSelect;
      if (!select) {
        break;
      }
    }
    return select;
  }

  public static class ResultBean {
    /**
     * num : 4
     * id : 19
     * price : 3.69
     * rating : 0
     * product_icon : http://youmehe.wang/product_icon/测试账号_1494147136.png
     * title : 沉迷红楼梦
     * saled : 0
     */

    private int num;
    private int id;
    private double price;
    private String rating;
    private String product_icon;
    private String title;
    private int saled;

    public boolean isSelect() {
      return isSelect;
    }

    public void setSelect(boolean select) {
      isSelect = select;
    }

    private boolean isSelect;

    public int getNum() {
      return num;
    }

    public void setNum(int num) {
      this.num = num;
    }

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
  }
}
