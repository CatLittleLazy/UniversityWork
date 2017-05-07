package com.example.youmehe.intellectualpropertyright.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import com.example.youmehe.intellectualpropertyright.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ScrollingActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scrolling);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);


    //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
    final CollapsingToolbarLayout mCollapsingToolbarLayout =
        (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
    mCollapsingToolbarLayout.setTitle("知识产权服务");
    Log.e("tag", "onCreate: " + getIntent().getStringExtra("bg"));
    Picasso.with(this)
        .load(getIntent().getStringExtra("bg"))
        .resize(400, 400)
        .into(new Target() {
          @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mCollapsingToolbarLayout.setBackground(new BitmapDrawable(bitmap));
          }

          @Override public void onBitmapFailed(Drawable errorDrawable) {

          }

          @Override public void onPrepareLoad(Drawable placeHolderDrawable) {

          }
        });
    //通过CollapsingToolbarLayout修改字体颜色
    mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);//设置还没收缩时状态下字体颜色
    mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
  }

  /**
   * string转成bitmap
   */
  public Bitmap convertStringToIcon(String st) {
    // OutputStream out;
    Bitmap bitmap = null;
    try {
      // out = new FileOutputStream("/sdcard/aa.jpg");
      byte[] bitmapArray;
      bitmapArray = Base64.decode(st, Base64.DEFAULT);
      bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
      // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
      return bitmap;
    } catch (Exception e) {
      return null;
    }
  }
}
