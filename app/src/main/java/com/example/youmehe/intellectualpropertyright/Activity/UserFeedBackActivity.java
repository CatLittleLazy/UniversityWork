package com.example.youmehe.intellectualpropertyright.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.youmehe.intellectualpropertyright.R;
import com.example.youmehe.intellectualpropertyright.Utils.NetUtils;
import com.example.youmehe.intellectualpropertyright.Utils.T;
import java.util.ArrayList;

/**
 * 用户反馈
 * Created by Administrator on 2017/3/29.
 */

public class UserFeedBackActivity extends AppCompatActivity implements View.OnClickListener {

  //反馈类型以及提交按钮
  private Button mBtnProducAdvice, mBtnBug, mBtnWrongContent, mBtnOther, mBtnSubmit;
  //反馈详情
  private EditText mEditAdvice;
  private final int FEED_BACK_OK = 1000;
  private final int FEED_BACK_WRONG = 1001;
  private final int SERVICE_ERROR = 559;//服务器异常

  int type = 0;
  private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case FEED_BACK_OK:
          T.shortToast(UserFeedBackActivity.this, "添加意见反馈成功");
          break;
        case FEED_BACK_WRONG:
          T.shortToast(UserFeedBackActivity.this, "反馈失败");
          break;
        case SERVICE_ERROR://服务器异常
          T.longToast(UserFeedBackActivity.this, "网络连接异常~");
          break;
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_feed_back);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

    setSupportActionBar(toolbar);

    //绑定按钮
    init();
  }

  private void init() {
    mBtnProducAdvice = (Button) findViewById(R.id.btn_product_advice);
    mBtnProducAdvice.setOnClickListener(this);
    mBtnBug = (Button) findViewById(R.id.btn_bug);
    mBtnBug.setOnClickListener(this);
    mBtnWrongContent = (Button) findViewById(R.id.btn_wrong_content);
    mBtnWrongContent.setOnClickListener(this);
    mBtnOther = (Button) findViewById(R.id.btn_other);
    mBtnOther.setOnClickListener(this);
    mBtnSubmit = (Button) findViewById(R.id.btn_submit);
    mBtnSubmit.setOnClickListener(this);
    mEditAdvice = (EditText) findViewById(R.id.txt_advice);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_product_advice:
        btnSelect((Button) v);
        break;
      case R.id.btn_bug:
        btnSelect((Button) v);
        break;
      case R.id.btn_wrong_content:
        btnSelect((Button) v);
        break;
      case R.id.btn_other:
        btnSelect((Button) v);
        break;
      case R.id.btn_submit:
        if (type == 0) {
          T.shortToast(this, "请选择反馈类型");
        } else {
          if (!NetUtils.isConnected(UserFeedBackActivity.this)) {
            //网络未连接
            T.shortToast(UserFeedBackActivity.this, "请检查您的网络~");
            return;
          }
          final String adviceTxt = mEditAdvice.getText().toString().trim();
          if (TextUtils.isEmpty(adviceTxt)) {
            T.shortToast(this, "请输入建议内容");
          } else {
            T.shortToast(this, "您已反馈成功");
            finish();
          }
        }
        break;
    }
  }

  //点击按钮时设置点击后的样式，并将其余的设置为未选中样式  2017.4.12
  private void btnSelect(Button btn) {
    btn.setBackgroundResource(R.drawable.feed_back_text_view_selected_bg);
    btn.setTextColor(Color.RED);
    ArrayList<Button> btnList = new ArrayList<>();
    btnList.add(mBtnProducAdvice);
    btnList.add(mBtnBug);
    btnList.add(mBtnWrongContent);
    btnList.add(mBtnOther);
    for (Button temp : btnList) {
      if (temp.getId() != btn.getId()) {
        temp.setBackgroundResource(R.drawable.feed_back_text_view_bg);
        temp.setTextColor(Color.GRAY);
      } else {
        type = btnList.indexOf(temp) + 1;
      }
    }
  }
}