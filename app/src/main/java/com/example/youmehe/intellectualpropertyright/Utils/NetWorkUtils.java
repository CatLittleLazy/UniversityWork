package com.example.youmehe.intellectualpropertyright.Utils;

import android.util.Log;
import com.example.youmehe.intellectualpropertyright.Constants;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * my
 * Created by youmehe on 2017/4/24.
 */

public class NetWorkUtils {

  private static NetWorkUtils OkHttpUtil = new NetWorkUtils();
  private static OkHttpClient client;

  public static NetWorkUtils getInstance() {
    client = new OkHttpClient().newBuilder().build();
    return OkHttpUtil;
  }

  public static boolean isConnect() {
    return true;
  }

  /**
   * 检测用户名是否存在
   *
   * @param userName 用户名
   */
  public String checkName(String userName) {
    OkHttpClient client = new OkHttpClient();
    FormBody formBody = new FormBody.Builder()
        .add("user_name", userName)
        .build();

    Request request = new Request.Builder()
        .url(Constants.checkName)
        .post(formBody)
        .build();

    Response response = null;
    try {
      response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        return response.body().string();
      } else {
        throw new IOException("Unexpected code " + response);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 注册用户
   *
   * @param userName 用户名
   * @param userPwd 用户密码
   * @param userType 用户类型
   */
  public String signUser(String userName, String userPwd, String userType) {
    OkHttpClient client = new OkHttpClient();
    FormBody formBody = new FormBody.Builder()
        .add("user_name", userName)
        .add("user_pwd", userPwd)
        .add("user_type", userType)
        .build();

    Request request = new Request.Builder()
        .url(Constants.sign)
        .post(formBody)
        .build();

    Response response = null;
    try {
      response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        return response.body().string();
      } else {
        throw new IOException("Unexpected code " + response);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 登录方法
   *
   * @param userName 用户名
   * @param userPwd 用户密码
   */
  public String login(String userName, String userPwd) {
    OkHttpClient client = new OkHttpClient();
    FormBody formBody = new FormBody.Builder()
        .add("user_name", userName)
        .add("user_pwd", userPwd)
        .build();

    Request request = new Request.Builder()
        .url(Constants.login)
        .post(formBody)
        .build();

    Response response = null;
    try {
      response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        return response.body().string();
      } else {
        throw new IOException("Unexpected code " + response);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 获取知识产权服务列表
   *
   * @param pageSize 每页多少
   * @param pageNo 当前页
   */
  public String getProductList(String pageSize, String pageNo) {
    OkHttpClient client = new OkHttpClient();
    FormBody formBody = new FormBody.Builder()
        .add("page_size", pageSize)
        .add("page_no", pageNo)
        .build();

    Request request = new Request.Builder()
        .url(Constants.producList)
        .post(formBody)
        .build();

    Response response = null;
    try {
      response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        return response.body().string();
      } else {
        throw new IOException("Unexpected code " + response);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 清空购物车
   *
   * @param productId 知识产权服务Id
   */
  public String removeGoods(String productId) {
    OkHttpClient client = new OkHttpClient();
    FormBody formBody = new FormBody.Builder()
        .add("product_id", productId)
        .build();

    Request request = new Request.Builder()
        .url(Constants.removeGoodsId)
        .post(formBody)
        .build();

    Response response = null;
    try {
      response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        return response.body().string();
      } else {
        throw new IOException("Unexpected code " + response);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 结算购物车
   *
   * @param userName 用户昵称
   * @param productId 知识产权服务Id
   */
  public String bookGoods(String userName, String productId) {
    OkHttpClient client = new OkHttpClient();
    FormBody formBody = new FormBody.Builder()
        .add("user_name", userName)
        .add("product_id", productId)
        .build();

    Request request = new Request.Builder()
        .url(Constants.bookGoods)
        .post(formBody)
        .build();

    Response response = null;
    try {
      response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        return response.body().string();
      } else {
        throw new IOException("Unexpected code " + response);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 获取知识产权服务详情
   *
   * @param productId 知识产权服务Id
   */
  public String getMoreInfo(String productId) {
    OkHttpClient client = new OkHttpClient();
    FormBody formBody = new FormBody.Builder()
        .add("product_id", productId)
        .build();

    Request request = new Request.Builder()
        .url(Constants.getMoreInfo)
        .post(formBody)
        .build();

    Response response = null;
    try {
      response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        return response.body().string();
      } else {
        throw new IOException("Unexpected code " + response);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 更改用户信息
   *
   * @param userName 用户名
   * @param userSexual 用户性别
   * @param userBirthday 用户生日
   * @param userArea 用户地址
   */
  public String updateSelf(String userName, String userSexual, String userBirthday,
      String userArea) {
    OkHttpClient client = new OkHttpClient();
    FormBody formBody = new FormBody.Builder()
        .add("user_name", userName)
        .add("user_sexual", userSexual)
        .add("user_birthday", userBirthday)
        .add("user_area", userArea)
        .build();

    Request request = new Request.Builder()
        .url(Constants.updateSelf)
        .post(formBody)
        .build();

    Response response = null;
    try {
      response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        return response.body().string();
      } else {
        throw new IOException("Unexpected code " + response);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 上传图片以及内容
   *
   * @param serverurl 地址
   * @param filepath 图片路径
   */
  public String uploadFileToPhpServer(String serverurl, String filepath) {
    //Log.d("uploadFile", "xxx " + filepath);
    String end = "\r\n";
    String twoHyphens = "--";
    String boundary = "******";

    Log.v("filepath", filepath + "???" + serverurl);
    try {
      URL url = new URL(serverurl);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      if (conn == null) {
        return null;
      }
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setChunkedStreamingMode(1024 * 1024);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("connection", "Keep-Alive");
      conn.setRequestProperty("Charsert", "UTF-8");

      conn.setConnectTimeout(8000);
      conn.setReadTimeout(8000);

      File file = new File(filepath);

      conn.setRequestProperty("Content-Type",
          "multipart/form-data;boundary=" + boundary);

      DataOutputStream dos = new DataOutputStream(
          conn.getOutputStream());
      dos.writeBytes(twoHyphens + boundary + end);
      dos.writeBytes("Content-Disposition: form-data; name=\"uploadfile\"; filename=\""
          + filepath.substring(filepath.lastIndexOf("/") + 1) + "\"" + end);
      dos.writeBytes(end);

      DataInputStream in = new DataInputStream(new FileInputStream(file));
      Log.e("tag", "uploadFileToPhpServer: " + file.getName());
      int bytes = 0;
      byte[] bufferOut = new byte[1024];
      while ((bytes = in.read(bufferOut)) != -1) {
        dos.write(bufferOut, 0, bytes);
      }
      in.close();
      dos.writeBytes(end);
      dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
      dos.flush();

      dos.close();

      BufferedReader br = new BufferedReader(new InputStreamReader(
          conn.getInputStream(), "UTF-8"));
      String line = null;
      StringBuffer sb = new StringBuffer();
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }
      String str = sb.toString();

      return str;
    } catch (Exception e) {

      e.printStackTrace();
      return null;
    }
  }

  //public String uploadFileToPhpServer(String serverurl, String filepath) {
  //  //Log.d("uploadFile", "xxx " + filepath);
  //  String end = "\r\n";
  //  String twoHyphens = "--";
  //  String boundary = "******";
  //
  //  Log.v("filepath", filepath + "???" + serverurl);
  //  try {
  //    URL url = new URL(serverurl);
  //    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
  //    if (conn == null) {
  //      return null;
  //    }
  //    conn.setDoOutput(true);
  //    conn.setDoInput(true);
  //    conn.setChunkedStreamingMode(1024 * 1024);
  //    conn.setRequestMethod("POST");
  //    conn.setRequestProperty("connection", "Keep-Alive");
  //    conn.setRequestProperty("Charsert", "UTF-8");
  //
  //    conn.setConnectTimeout(8000);
  //    conn.setReadTimeout(8000);
  //
  //    File file = new File(filepath);
  //
  //    conn.setRequestProperty("Content-Type",
  //        "multipart/form-data;boundary=" + boundary);
  //
  //    DataOutputStream dos = new DataOutputStream(
  //        conn.getOutputStream());
  //    dos.writeBytes(twoHyphens + boundary + end);
  //    dos.writeBytes("Content-Disposition: form-data; name=\"uploadfile\"; filename=\""
  //        + filepath.substring(filepath.lastIndexOf("/") + 1) + "\"" + end);
  //    dos.writeBytes(end);
  //
  //    DataInputStream in = new DataInputStream(new FileInputStream(file));
  //
  //    int bytes = 0;
  //    byte[] bufferOut = new byte[1024];
  //    while ((bytes = in.read(bufferOut)) != -1) {
  //      dos.write(bufferOut, 0, bytes);
  //    }
  //    in.close();
  //    dos.writeBytes(end);
  //    dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
  //    dos.flush();
  //
  //    dos.close();
  //
  //    BufferedReader br = new BufferedReader(new InputStreamReader(
  //        conn.getInputStream(), "UTF-8"));
  //    String line = null;
  //    StringBuffer sb = new StringBuffer();
  //    while ((line = br.readLine()) != null) {
  //      sb.append(line);
  //    }
  //    String str = sb.toString();
  //
  //    return str;
  //  } catch (Exception e) {
  //
  //    e.printStackTrace();
  //    return null;
  //  }
  //}
}