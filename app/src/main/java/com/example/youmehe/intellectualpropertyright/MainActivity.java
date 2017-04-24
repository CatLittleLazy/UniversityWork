package com.example.youmehe.intellectualpropertyright;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  private TextView mTextMessage;
  private FrameLayout frameLayout;

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
      = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
        case R.id.navigation_home:
          mTextMessage.setText(R.string.title_home);
          return true;
        case R.id.navigation_dashboard:
          mTextMessage.setText(R.string.title_dashboard);
          frameLayout.removeAllViews();
          frameLayout.addView(View.inflate(MainActivity.this, R.layout.activity_signup, null));
          return true;
        case R.id.navigation_notifications:
          mTextMessage.setText(R.string.title_notifications);
          frameLayout.removeAllViews();
          frameLayout.addView(View.inflate(MainActivity.this, R.layout.user_fragment, null));
          return true;
      }
      return false;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mTextMessage = (TextView) findViewById(R.id.message);
    frameLayout = (FrameLayout) findViewById(R.id.content);
    frameLayout.addView(View.inflate(MainActivity.this, R.layout.user_fragment, null));
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
  }
}