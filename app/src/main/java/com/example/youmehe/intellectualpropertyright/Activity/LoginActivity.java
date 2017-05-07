package com.example.youmehe.intellectualpropertyright.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.youmehe.intellectualpropertyright.Bean.CheckUserNameEntity;
import com.example.youmehe.intellectualpropertyright.Bean.UserInfoEntity;
import com.example.youmehe.intellectualpropertyright.R;
import com.example.youmehe.intellectualpropertyright.Utils.NetWorkUtils;
import com.example.youmehe.intellectualpropertyright.Utils.SPUtils;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

  private final String TAG = "LoginActivity";
  private final int _LOGIN_SUCCESS_ = 0x000;
  private final int _LOGIN_FAILED_ = 0x001;
  private final int _WRONG_USER_NAME_ = 0x002;
  private final int _WRONG_USER_PWD_ = 0x003;

  @Bind(R.id.input_email) EditText _userNameText;
  @Bind(R.id.input_password) EditText _passwordText;
  @Bind(R.id.btn_login) Button _loginButton;
  @Bind(R.id.link_signup) TextView _signupLink;

  String userName, userPwd;
  ProgressDialog progressDialog;
  private SPUtils mSputils;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    ButterKnife.bind(this);

    //弹出加载框
    progressDialog = new ProgressDialog(LoginActivity.this,
        R.style.AppTheme_Dark_Dialog);

    SharedPreferences sp = getSharedPreferences("myInfo", MODE_PRIVATE);
    if (sp != null) {
      String tempName = sp.getString("userName", null);
      String tempPwd = sp.getString("userPwd", null);
      Log.e(TAG, "onCreate: " + tempName + tempPwd);
      if (!TextUtils.isEmpty(tempName) && !TextUtils.isEmpty(tempPwd)) {
        loginByNameAndPwd(tempName, tempPwd);
      } else {
        mSputils = new SPUtils(this, "myInfo");
      }
    }

    //登录按钮点击事件
    _loginButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        login();
      }
    });

    //注册账号点击时间
    _signupLink.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        finish();
      }
    });

    String name = getIntent().getStringExtra("userName");
    _userNameText.setText(name);
  }

  /*登录方法*/
  public void login() {
    //判断是否有网络t
    if (!NetWorkUtils.isConnect()) {
      Toast.makeText(this, "未检测到网络", Toast.LENGTH_SHORT).show();
      return;
    }
    if (!validate()) {
      Toast.makeText(this, "登陆失败", Toast.LENGTH_SHORT).show();
      return;
    }

    _loginButton.setEnabled(false);

    loginByNameAndPwd(userName, userPwd);
  }

  //点击退出后关闭App
  @Override
  public void onBackPressed() {
    moveTaskToBack(true);
  }

  //验证密码输入是否合法
  public boolean validate() {
    boolean valid = true;
    userName = _userNameText.getText().toString();
    userPwd = _passwordText.getText().toString();

    if (userPwd.isEmpty() || userPwd.length() < 4 || userPwd.length() > 10) {
      _passwordText.setError("密码长度规定为4-10位");
      valid = false;
    } else {
      _passwordText.setError(null);
    }

    return valid;
  }

  private Handler mHandler = new Handler(new Handler.Callback() {
    @Override public boolean handleMessage(Message msg) {
      _loginButton.setEnabled(true);
      progressDialog.dismiss();
      switch (msg.what) {
        //登录失败时弹出提示
        case _LOGIN_FAILED_:
          Toast.makeText(LoginActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
          break;
        //登录成功时进入主页
        case _LOGIN_SUCCESS_:
          Intent intent = new Intent(LoginActivity.this, MainActivity.class);
          intent.putExtra("userIcon", ((UserInfoEntity) msg.obj).getUser_icon());
          intent.putExtra("userName", ((UserInfoEntity) msg.obj).getUser_name());
          intent.putExtra("userType", ((UserInfoEntity) msg.obj).getUser_type());
          startActivity(intent);
          finish();
          break;
        //用户名不存在
        case _WRONG_USER_NAME_:
          Toast.makeText(LoginActivity.this, "用户名称不存在", Toast.LENGTH_SHORT).show();
          break;
        //用户密码错误
        case _WRONG_USER_PWD_:
          Toast.makeText(LoginActivity.this, "用户密码错误", Toast.LENGTH_SHORT).show();
          break;
      }
      return false;
    }
  });

  private void loginByNameAndPwd(final String userName, final String userPwd) {
    progressDialog.setIndeterminate(true);
    progressDialog.setMessage("正在登录，请稍后...");
    progressDialog.show();

    //网络访问数据库完成登录操作
    new Thread(new Runnable() {
      @Override public void run() {
        Gson gson = new Gson();
        String checkNameJson = NetWorkUtils.getInstance().checkName(userName);
        CheckUserNameEntity mCheckUserNameEntity =
            gson.fromJson(checkNameJson, CheckUserNameEntity.class);
        Log.e(TAG, "run: " + checkNameJson);
        //返回-1表示用户名存在
        if (mCheckUserNameEntity.getRet_code() == -1) {
          String loginJson = NetWorkUtils.getInstance().login(userName, userPwd);
          Log.e(TAG, "run: " + loginJson);
          UserInfoEntity mUserInfoEntity = gson.fromJson(loginJson, UserInfoEntity.class);
          if (mUserInfoEntity.getRet_code() == -1) {
            mHandler.sendEmptyMessage(_WRONG_USER_PWD_);
          } else {
            Message msg = Message.obtain();
            msg.what = _LOGIN_SUCCESS_;
            msg.obj = mUserInfoEntity;
            if (mSputils == null) {
              mSputils = new SPUtils(LoginActivity.this, "myInfo");
            }
            mSputils.put("userId", mUserInfoEntity.getUser_id());
            mSputils.put("userName", mUserInfoEntity.getUser_name());
            mSputils.put("userPwd", userPwd);
            mSputils.put("userIcon", mUserInfoEntity.getUser_icon());
            mSputils.put("userSexual", mUserInfoEntity.getUser_sexual());
            mSputils.put("userBirthday", mUserInfoEntity.getUser_birthday());
            mSputils.put("userType", mUserInfoEntity.getUser_type());
            mSputils.put("userArea", mUserInfoEntity.getUser_area());
            mHandler.sendMessage(msg);
          }
        } else {
          //登录失败时获取服务返回的错误码
          mHandler.sendEmptyMessage(_WRONG_USER_NAME_);
        }
      }
    }).start();
  }
}