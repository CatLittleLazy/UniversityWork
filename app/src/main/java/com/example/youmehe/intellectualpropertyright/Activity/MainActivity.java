package com.example.youmehe.intellectualpropertyright.Activity;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.youmehe.intellectualpropertyright.Adapter.MainItemAdapter;
import com.example.youmehe.intellectualpropertyright.Bean.ProductListEntity;
import com.example.youmehe.intellectualpropertyright.R;
import com.example.youmehe.intellectualpropertyright.Utils.NetWorkUtils;
import com.example.youmehe.intellectualpropertyright.Utils.OnItemClickListener;
import com.example.youmehe.intellectualpropertyright.Utils.SPUtils;
import com.example.youmehe.intellectualpropertyright.Utils.T;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

  private TextView mTextMessage;
  private FrameLayout frameLayout;
  private MainItemAdapter mAdapter;
  private RecyclerView recyclerView;
  private SwipeRefreshLayout mSwipeRefreshLayout;
  private int currentPage = 0;
  private int totalCount = 0;
  private View viewProductList;
  private View userView;

  /**
   * 刷新监听。
   */
  private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener =
      new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
          currentPage = 0;
          mAdapter.getmData().clear();
          getClassData(++currentPage);
        }
      };

  /**
   * 加载更多
   */
  private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
      if (!recyclerView.canScrollVertically(1)) {// 手指不能向上滑动了
        // TODO 这里有个注意的地方，如果你刚进来时没有数据，但是设置了适配器，这个时候就会触发加载更多，需要开发者判断下是否有数据，如果有数据才去加载更多。
        getClassData(++currentPage);
        //Toast.makeText(ClassManagerActivity.this, "滑到最底部了，去加载更多吧！", Toast.LENGTH_SHORT).show();
      }
    }
  };

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
      = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
        case R.id.navigation_home:
          View view = View.inflate(MainActivity.this, R.layout.car_fragment, null);
          frameLayout.removeAllViews();
          frameLayout.addView(view);
          return true;
        case R.id.navigation_dashboard:
          mTextMessage.setText(R.string.title_dashboard);
          if (viewProductList == null) {
            initProductList();
          }
          frameLayout.removeAllViews();
          frameLayout.addView(viewProductList);
          return true;
        case R.id.navigation_notifications:
          mTextMessage.setText(R.string.title_notifications);
          if (userView == null) {
            initUserView();
          }
          frameLayout.removeAllViews();
          frameLayout.addView(userView);
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
    initProductList();
    frameLayout.addView(viewProductList);
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
  }

  @Override public void onItemClick(int position) {
    Toast.makeText(this, mAdapter.getmData().get(position).getIcon(), Toast.LENGTH_SHORT).show();
    Intent intent = new Intent(MainActivity.this, ScrollingActivity.class);
    intent.putExtra("bg", mAdapter.getmData().get(position).getIcon());
    startActivity(intent);
  }

  private void getClassData(final int i) {
    new Thread(new Runnable() {
      @Override public void run() {
        Gson gson = new Gson();
        String productListJson = NetWorkUtils.getInstance().getProductList("5", String.valueOf(i));
        ProductListEntity mProductListEntity =
            gson.fromJson(productListJson, ProductListEntity.class);
        Log.e("tag", "run: " + productListJson);
        if (mProductListEntity.getRet_code() == 0) {//有结果
          totalCount = mProductListEntity.getTotal_count();
          Message msg = Message.obtain();
          List<ProductListEntity.RetResultBean> productListData =
              mProductListEntity.getRet_result();
          Log.e("tag", "run: " + productListData.size());
          msg.obj = productListData;
          if (mAdapter.getmData().size() + productListData.size() <= totalCount) {
            msg.what = 102;
            mHandler.sendMessage(msg);
          } else {
            mHandler.sendEmptyMessage(101);
          }
        } else {
          mHandler.sendEmptyMessage(103);
        }
      }
    }).start();
  }

  private android.os.Handler mHandler = new android.os.Handler(new android.os.Handler.Callback() {
    @Override public boolean handleMessage(Message msg) {
      switch (msg.what) {
        case 101:
          mSwipeRefreshLayout.setRefreshing(false);
          Toast.makeText(MainActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
          break;
        case 102:
          mSwipeRefreshLayout.setRefreshing(false);
          mAdapter.setmData((List<ProductListEntity.RetResultBean>) msg.obj);
          mAdapter.notifyDataSetChanged();
          Toast.makeText(MainActivity.this, "获取数据成功", Toast.LENGTH_SHORT).show();
          break;
        case 103:
          mSwipeRefreshLayout.setRefreshing(false);
          Toast.makeText(MainActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
          break;
      }
      return false;
    }
  });

  //知识产权服务列表
  private void initProductList() {
    viewProductList = View.inflate(MainActivity.this, R.layout.service_list_fragment, null);

    mSwipeRefreshLayout =
        (SwipeRefreshLayout) viewProductList.findViewById(R.id.swipe_layout);
    mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

    recyclerView = (RecyclerView) viewProductList.findViewById(R.id.recycler_view);
    // 添加滚动监听。
    recyclerView.addOnScrollListener(mOnScrollListener);

    recyclerView.setLayoutManager(
        new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
      @Override
      public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
          RecyclerView.State state) {
        outRect.set(10, 10, 10, 15);
      }
    });
    mAdapter = new MainItemAdapter(MainActivity.this);
    mAdapter.setOnItemClickListener(MainActivity.this);
    recyclerView.setAdapter(mAdapter);
    getClassData(++currentPage);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 22 && resultCode == 23) {
      String iconUrl = getSharedPreferences("myInfo", MODE_PRIVATE).getString("userIcon", "");
      Log.e("tac", "onActivityResult: " + iconUrl);
      if (!TextUtils.isEmpty(iconUrl)) {
        ImageView temp = (ImageView) userView.findViewById(R.id.iv_user_icon);
        if (null != temp) {
          //如果本地有头像缓存则直接设置
          final File file = new File(getExternalCacheDir(), "/userCropTemp.png");
          if (file.exists()) {
            temp.setImageURI(Uri.fromFile(file));
          }
        }
      }
    }
  }

  private void initUserView() {
    userView = View.inflate(MainActivity.this, R.layout.user_fragment, null);
    userView.findViewById(R.id.txt_edit_my_info).setOnClickListener(
        new View.OnClickListener() {
          @Override public void onClick(View v) {
            MainActivity.this.startActivityForResult(
                new Intent(MainActivity.this, EditUserInfoActivity.class), 22);
          }
        });

    if (TextUtils.isEmpty(getIntent().getStringExtra("userIcon"))) {
      ((ImageView) userView.findViewById(R.id.iv_user_icon)).setImageResource(
          R.drawable.default_head);
    } else {
      Log.e("tag", "onNavigationItemSelected: " + getIntent().getStringExtra("userIcon"));
      Picasso.with(MainActivity.this)
          .load(getIntent().getStringExtra("userIcon"))
          .resize(200, 200)
          .into((ImageView) userView.findViewById(R.id.iv_user_icon));
    }
    ((TextView) userView.findViewById(R.id.user_nickname)).setText(
        getIntent().getStringExtra("userName"));
    //退出登录
    userView.findViewById(R.id.realtive_exit).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        SPUtils sp = new SPUtils(MainActivity.this, "myInfo");
        sp.put("userPwd", "");
        finish();
      }
    });
    //关于我们
    userView.findViewById(R.id.relative_about_us).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        T.shortToast(MainActivity.this, "关于我们");
        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
      }
    });
    //版本更新
    userView.findViewById(R.id.relative_new_version).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        T.shortToast(MainActivity.this, "您已是最新版本");
      }
    });
    if (!getIntent().getStringExtra("userType").equals("0")) {
      //上传服务
      userView.findViewById(R.id.relative_update_server)
          .setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
              T.shortToast(MainActivity.this, "上传服务");
              startActivity(new Intent(MainActivity.this, UpdateServerActivity.class));
            }
          });
    } else {
      userView.findViewById(R.id.relative_update_server).setVisibility(View.GONE);
    }
    //我的收藏
    userView.findViewById(R.id.relative_my_save).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        T.shortToast(MainActivity.this, "我的收藏");
      }
    });
    //意见反馈
    userView.findViewById(R.id.relative_feed_back).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        T.shortToast(MainActivity.this, "意见反馈");
        startActivity(new Intent(MainActivity.this, UserFeedBackActivity.class));
      }
    });
  }
}