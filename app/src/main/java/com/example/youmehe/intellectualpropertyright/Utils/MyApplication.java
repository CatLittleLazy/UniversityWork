package com.example.youmehe.intellectualpropertyright.Utils;

import android.app.Application;
import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;

/**
 * Created by youmehe on 2017/4/25.
 */
public class MyApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    // 初始化应用信息
    AVOSCloud.initialize(this, "wpzcp6bfrdv8zra4b91vp97fmy8qqlez6scpi9wk6sonfr6e",
        "6l7o79cwvh5es564h9sjp48xkpeeaw3x3xbqp12kgwjqvkyj");
    // 启用崩溃错误统计
    AVAnalytics.enableCrashReport(this.getApplicationContext(), true);
    AVOSCloud.setLastModifyEnabled(true);
    AVOSCloud.setDebugLogEnabled(true);
  }
}