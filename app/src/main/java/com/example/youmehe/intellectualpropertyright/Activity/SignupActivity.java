package com.example.youmehe.intellectualpropertyright.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.youmehe.intellectualpropertyright.Bean.CheckUserNameEntity;
import com.example.youmehe.intellectualpropertyright.Bean.ShortResponseEntity;
import com.example.youmehe.intellectualpropertyright.R;
import com.example.youmehe.intellectualpropertyright.Utils.NetWorkUtils;
import com.example.youmehe.intellectualpropertyright.Utils.T;
import com.google.gson.Gson;

public class SignupActivity extends AppCompatActivity {
  private static final String TAG = "SignupActivity";

  @Bind(R.id.input_name) EditText _nameText;
  @Bind(R.id.input_password) EditText _passwordText;
  @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
  @Bind(R.id.btn_signup) Button _signupButton;
  @Bind(R.id.link_login) TextView _loginLink;
  @Bind(R.id.normal_user) RadioButton normalUser;
  @Bind(R.id.unnormal_user) RadioButton unnormalUser;

  ProgressDialog progressDialog;
  String name, password, userType = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);
    ButterKnife.bind(this);

    _signupButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        signup();
      }
    });

    _loginLink.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Finish the registration screen and return to the Login activity
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
      }
    });

    normalUser.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        userType = "0";
      }
    });

    unnormalUser.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        userType = "1";
      }
    });
  }

  public void signup() {
    if (!validate()) {
      Toast.makeText(this, "输入有误", Toast.LENGTH_SHORT).show();
      return;
    }

    if (userType == null) {
      Toast.makeText(this, "未选择身份", Toast.LENGTH_SHORT).show();
      return;
    }
    progressDialog = new ProgressDialog(SignupActivity.this,
        R.style.AppTheme_Dark_Dialog);
    progressDialog.setIndeterminate(true);
    progressDialog.setMessage("Creating Account...");
    progressDialog.show();

    _signupButton.setEnabled(false);

    new Thread(new Runnable() {
      @Override public void run() {
        Gson gson = new Gson();
        Log.e(TAG, "run: " + name);
        String checkNameJson = NetWorkUtils.getInstance().checkName(name);
        CheckUserNameEntity mCheckUserNameEntity =
            gson.fromJson(checkNameJson, CheckUserNameEntity.class);
        Log.e(TAG, "run: " + checkNameJson);
        if (checkNameJson == null) {
          mHandler.sendEmptyMessage(10101);
          return;
        }
        if (mCheckUserNameEntity.getRet_code() == 0) {//判断用户名是否唯一
          String signJson = NetWorkUtils.getInstance().signUser(name, password, userType);
          ShortResponseEntity mShortResponseEntity =
              gson.fromJson(signJson, ShortResponseEntity.class);
          Log.e(TAG, "run: " + signJson);
          if (mShortResponseEntity.getRet_code() == 0) {//注册成功
            Message msg = Message.obtain();
            msg.what = 102;
            mHandler.sendEmptyMessage(102);
          }
        } else {
          mHandler.sendEmptyMessage(101);
        }
      }
    }).start();
  }

  //判定输入是否合理
  public boolean validate() {
    boolean valid = true;

    name = _nameText.getText().toString();
    password = _passwordText.getText().toString();
    String reEnterPassword = _reEnterPasswordText.getText().toString();

    if (name.isEmpty() || name.length() < 3) {
      _nameText.setError("at least 3 characters");
      valid = false;
    } else {
      _nameText.setError(null);
    }

    if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
      _passwordText.setError("between 4 and 10 alphanumeric characters");
      valid = false;
    } else {
      _passwordText.setError(null);
    }

    if (reEnterPassword.isEmpty()
        || reEnterPassword.length() < 4
        || reEnterPassword.length() > 10
        || !(reEnterPassword.equals(password))) {
      _reEnterPasswordText.setError("密码不匹配");
      valid = false;
    } else {
      _reEnterPasswordText.setError(null);
    }

    return valid;
  }

  private Handler mHandler = new Handler(new Handler.Callback() {
    @Override public boolean handleMessage(Message msg) {
      switch (msg.what) {
        case 101:
          progressDialog.dismiss();
          Toast.makeText(SignupActivity.this, "昵称已注册", Toast.LENGTH_SHORT).show();
          _signupButton.setEnabled(true);
          break;
        case 102:
          progressDialog.dismiss();
          Toast.makeText(SignupActivity.this, "恭喜您注册成功", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
          intent.putExtra("userName", name);
          startActivity(intent);
          finish();
          break;
        case 10101:
          progressDialog.dismiss();
          T.shortToast(SignupActivity.this, "请检查您的网络");
          break;
      }
      return false;
    }
  });
}