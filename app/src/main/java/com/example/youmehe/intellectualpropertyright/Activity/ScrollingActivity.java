package com.example.youmehe.intellectualpropertyright.Activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.youmehe.intellectualpropertyright.Bean.SaveEntity;
import com.example.youmehe.intellectualpropertyright.R;
import com.example.youmehe.intellectualpropertyright.Utils.NetWorkUtils;
import com.example.youmehe.intellectualpropertyright.Utils.T;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.text.DecimalFormat;

public class ScrollingActivity extends AppCompatActivity {

  private static final int YOU_HAVE_ALREADY_SAVED = 0x001;
  private static final int YOU_NO_SAVE_THIS_PRODUCT = 0x002;
  private static final int SAVE_SUCCESS = 0x003;
  private static final int DELETE_SAVE = 0x004;
  private static final int SERVICE_ERROR = 0x005;
  private static final int YOU_NO_CAR_THIS_PRODUCT = 0x006;
  private static final int YOU_HAVE_ALREADY_CAR = 0x007;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;
  @Bind(R.id.app_bar) AppBarLayout appBar;
  @Bind(R.id.txt_title) TextView txtTitle;
  @Bind(R.id.txt_price) TextView txtPrice;
  @Bind(R.id.txt_saled) TextView txtSaled;
  @Bind(R.id.rating_num) RatingBar ratingNum;
  @Bind(R.id.txt_content) TextView txtContent;
  @Bind(R.id.linear_chat) LinearLayout linearChat;
  @Bind(R.id.linear_save) LinearLayout linearSave;
  @Bind(R.id.btn_add_car) Button btnAddCar;
  @Bind(R.id.btn_buy_now) Button btnBuyNow;
  @Bind(R.id.image_save) ImageView imageSave;
  private String type;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scrolling);
    ButterKnife.bind(this);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });

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
    txtTitle.setText(getIntent().getStringExtra("title"));
    DecimalFormat df = new DecimalFormat("###0.0##");//最多保留几位小数，就用几个#，最少位就用0来确定
    txtPrice.setText(df.format(getIntent().getDoubleExtra("price",0.0)));
    txtSaled.setText(
        String.format(getString(R.string.saled), "  " + getIntent().getStringExtra("saled")));
    txtContent.setText(getIntent().getStringExtra("content"));
    ratingNum.setRating(Float.valueOf(getIntent().getStringExtra("rating")));
    new Thread(new Runnable() {
      @Override public void run() {
        String checkStateJson =
            NetWorkUtils.getInstance()
                .userSave(MainActivity.userName, getIntent().getStringExtra("id"), "check");
        Gson gson = new Gson();
        Log.e("tag", "run: " + checkStateJson);
        SaveEntity saveEntity = gson.fromJson(checkStateJson, SaveEntity.class);
        if (saveEntity.getResult() == 0) {
          mHandler.sendEmptyMessage(YOU_NO_SAVE_THIS_PRODUCT);
        } else {
          mHandler.sendEmptyMessage(YOU_HAVE_ALREADY_SAVED);
        }
      }
    }).start();
  }

  @OnClick(R.id.linear_chat) void chat() {
    T.shortToast(ScrollingActivity.this, "该功能尚未实现");
  }

  @OnClick(R.id.linear_save) void save() {
    Log.e("tag", "save: " + type);
    if (type.equals("add")) {
      imageSave.setImageResource(R.drawable.saved);
      T.shortToast(this, "收藏成功");
    } else {
      imageSave.setImageResource(R.drawable.save);
      T.shortToast(this, "取消收藏成功");
    }
    new Thread(new Runnable() {
      @Override public void run() {
        NetWorkUtils.getInstance()
            .userSave(MainActivity.userName, getIntent().getStringExtra("id"), type);
        mHandler.sendEmptyMessage(SAVE_SUCCESS);
      }
    }).start();
  }

  @OnClick(R.id.btn_add_car) void addCar() {
    T.shortToast(ScrollingActivity.this, "加入购物车");
    new Thread(new Runnable() {
      @Override public void run() {
        String checkStateJson =
            NetWorkUtils.getInstance()
                .userCar(MainActivity.userName, getIntent().getStringExtra("id"), "check");
        Gson gson = new Gson();
        SaveEntity saveEntity = gson.fromJson(checkStateJson, SaveEntity.class);
        if (saveEntity.getResult() != 0) {
          String addNumJson =
              NetWorkUtils.getInstance()
                  .userCar(MainActivity.userName, getIntent().getStringExtra("id"), "num_add");
        } else {
          String addNumJson =
              NetWorkUtils.getInstance()
                  .userCar(MainActivity.userName, getIntent().getStringExtra("id"), "add");
        }
      }
    }).start();
  }

  @OnClick(R.id.btn_buy_now) void buyNow() {
    T.shortToast(ScrollingActivity.this, "立即结算");
  }

  private Handler mHandler = new Handler() {
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case YOU_HAVE_ALREADY_SAVED:
          imageSave.setImageResource(R.drawable.saved);
          type = "del";
          break;

        case YOU_NO_SAVE_THIS_PRODUCT:
          type = "add";
          break;

        case SAVE_SUCCESS:
          if (type.equals("add")) {
            type = "del";
          } else {
            type = "add";
          }
          break;

        case DELETE_SAVE:

          break;
      }
    }
  };
}