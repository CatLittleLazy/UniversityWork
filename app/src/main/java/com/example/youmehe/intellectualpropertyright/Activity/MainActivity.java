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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.youmehe.intellectualpropertyright.Adapter.CarAdapter;
import com.example.youmehe.intellectualpropertyright.Adapter.MainItemAdapter;
import com.example.youmehe.intellectualpropertyright.Bean.ProductListEntity;
import com.example.youmehe.intellectualpropertyright.R;
import com.example.youmehe.intellectualpropertyright.Utils.NetWorkUtils;
import com.example.youmehe.intellectualpropertyright.Utils.OnItemClickListener;
import com.example.youmehe.intellectualpropertyright.Utils.SPUtils;
import com.example.youmehe.intellectualpropertyright.Utils.ShowDialog;
import com.example.youmehe.intellectualpropertyright.Utils.T;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

  private FrameLayout frameLayout;
  private MainItemAdapter mAdapter;
  private RecyclerView recyclerView;
  private SwipeRefreshLayout mSwipeRefreshLayout;
  private int currentPage = 0;
  private int totalCount = 0;
  private View viewProductList;
  private View userView;
  private String sort = "", key = "";
  private CarAdapter carAdapter;
  private static Button btn_pay;
  private static TextView txt_total_price, txt_total_price_b;
  private static CheckBox checkAll;
  private ShowDialog showDialog;
  private View carView;
  private SearchView searchView;
  public static String userName;
  /**
   * 刷新监听。
   */
  private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener =
      new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
          currentPage = 0;
          mAdapter.getmData().clear();
          key = "";
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
          if (carView == null) {
            initCarView();
          }
          carAdapter.getData();
          frameLayout.removeAllViews();
          frameLayout.addView(carView);
          return true;
        case R.id.navigation_dashboard:
          if (viewProductList == null) {
            initProductList();
          }
          frameLayout.removeAllViews();
          frameLayout.addView(viewProductList);
          return true;
        case R.id.navigation_notifications:
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
    userName = getIntent().getStringExtra("userName");
    frameLayout = (FrameLayout) findViewById(R.id.content);
    initProductList();
    frameLayout.addView(viewProductList);
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
  }

  @Override public void onItemClick(int position) {
    ProductListEntity.RetResultBean temp = mAdapter.getmData().get(position);
    Intent intent = new Intent(MainActivity.this, ScrollingActivity.class);
    intent.putExtra("bg", temp.getIcon());
    intent.putExtra("title", temp.getTitle());
    intent.putExtra("content", temp.getContent());
    intent.putExtra("price", temp.getPrice());
    intent.putExtra("saled", temp.getSaled() + "");
    intent.putExtra("rating", temp.getRating());
    intent.putExtra("owner", temp.getOwner());
    intent.putExtra("id", temp.getId() + "");
    startActivity(intent);
  }

  private void getClassData(final int i) {
    new Thread(new Runnable() {
      @Override public void run() {
        Gson gson = new Gson();
        String productListJson =
            NetWorkUtils.getInstance().getProductList("5", String.valueOf(i), sort, key);
        Log.e("tag", "run: " + sort + "|" + key);
        ProductListEntity mProductListEntity =
            gson.fromJson(productListJson, ProductListEntity.class);
        Log.e("tag", "run: " + productListJson);
        if (mProductListEntity.getRet_result() == null) {
          mHandler.sendEmptyMessage(101);
        } else {
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
      }
    }).start();
  }

  private android.os.Handler mHandler = new android.os.Handler(new android.os.Handler.Callback() {
    @Override public boolean handleMessage(Message msg) {
      switch (msg.what) {
        case 101:
          Log.e("tag", "handleMessage: " + currentPage);
          mSwipeRefreshLayout.setRefreshing(false);
          if (currentPage == 1) {
            mAdapter.getmData().clear();
            noSearchData.setVisibility(View.VISIBLE);
          }
          mAdapter.notifyDataSetChanged();
          Toast.makeText(MainActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
          break;
        case 102:
          mSwipeRefreshLayout.setRefreshing(false);
          noSearchData.setVisibility(View.GONE);
          if (currentPage == 1) {
            mAdapter.getmData().clear();
          }
          mAdapter.setmData((List<ProductListEntity.RetResultBean>) msg.obj);
          mAdapter.notifyDataSetChanged();
          Toast.makeText(MainActivity.this, "获取数据成功", Toast.LENGTH_SHORT).show();
          break;
        case 103:
          mSwipeRefreshLayout.setRefreshing(false);
          Toast.makeText(MainActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
          break;
        case 1:
          carAdapter.setIsSelcet(checkAll.isChecked());
          carAdapter.notifyDataSetChanged();
          change(carAdapter);
          break;
      }
      return false;
    }
  });

  private RelativeLayout noSearchData;

  //知识产权服务列表
  private void initProductList() {
    viewProductList = View.inflate(MainActivity.this, R.layout.service_list_fragment, null);
    noSearchData = (RelativeLayout) viewProductList.findViewById(R.id.relative_no_data);
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
    searchView = (SearchView) viewProductList.findViewById(R.id.search_view);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override public boolean onQueryTextSubmit(String query) {
        key = query;
        currentPage = 0;
        getClassData(++currentPage);
        Log.e("tag", "onQueryTextSubmit: " + query);
        return false;
      }

      @Override public boolean onQueryTextChange(String newText) {
        if (newText.equals("")) {
          mOnRefreshListener.onRefresh();
        }
        return false;
      }
    });
    //根据评价
    viewProductList.findViewById(R.id.linear_rating).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (mAdapter.getmData().size() == 0) {
          T.shortToast(MainActivity.this, "无搜索结果，无法排序");
          return;
        }
        if (mAdapter.getmData().size() == 1) {
          T.shortToast(MainActivity.this, "只有一条数据，无需排序");
          return;
        }
        sort = "product_rating";
        mOnRefreshListener.onRefresh();
      }
    });
    //根据销量
    viewProductList.findViewById(R.id.linear_saled).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (mAdapter.getmData().size() == 0) {
          T.shortToast(MainActivity.this, "无搜索结果，无法排序");
          return;
        }
        if (mAdapter.getmData().size() == 1) {
          T.shortToast(MainActivity.this, "只有一条数据，无需排序");
          return;
        }
        sort = "product_saled";
        mOnRefreshListener.onRefresh();
      }
    });
    //根据价格从低到高
    viewProductList.findViewById(R.id.linear_price_low_to_top)
        .setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            if (mAdapter.getmData().size() == 0) {
              T.shortToast(MainActivity.this, "无搜索结果，无法排序");
              return;
            }
            if (mAdapter.getmData().size() == 1) {
              T.shortToast(MainActivity.this, "只有一条数据，无需排序");
              return;
            }
            sort = "product_price_top_to_low";
            mOnRefreshListener.onRefresh();
          }
        });
    //根据价格从高到低
    viewProductList.findViewById(R.id.linear_price_top_to_low)
        .setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            if (mAdapter.getmData().size() == 0) {
              T.shortToast(MainActivity.this, "无搜索结果，无法排序");
              return;
            }
            if (mAdapter.getmData().size() == 1) {
              T.shortToast(MainActivity.this, "只有一条数据，无需排序");
              return;
            }
            sort = "product_price_low_to_top";
            mOnRefreshListener.onRefresh();
          }
        });
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
        showDialog = new ShowDialog();
        showDialog.show(MainActivity.this, "", "确定狠心退出?",
            new ShowDialog.OnBottomClickListener() {
              @Override
              public void positive() {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                SPUtils sp = new SPUtils(MainActivity.this, "myInfo");
                sp.put("userPwd", "");
                finish();
              }

              @Override
              public void negtive() {
              }
            });
      }
    });
    //关于我们
    userView.findViewById(R.id.relative_about_us).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
      }
    });
    //版本更新
    userView.findViewById(R.id.relative_new_version).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        showDialog = new ShowDialog();
        showDialog.show(MainActivity.this, "", "您已是最新版本",
            new ShowDialog.OnBottomClickListener() {
              @Override
              public void positive() {
              }

              @Override
              public void negtive() {
              }
            });
      }
    });
    if (!getIntent().getStringExtra("userType").equals("0")) {
      //上传服务
      userView.findViewById(R.id.relative_update_server)
          .setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
              startActivity(new Intent(MainActivity.this, UpdateServerActivity.class));
            }
          });
    } else {
      userView.findViewById(R.id.relative_update_server).setVisibility(View.GONE);
    }
    //我的收藏
    userView.findViewById(R.id.relative_my_save).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startActivity(new Intent(MainActivity.this, MySaveActivity.class));
      }
    });
    //意见反馈
    userView.findViewById(R.id.relative_feed_back).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startActivity(new Intent(MainActivity.this, UserFeedBackActivity.class));
      }
    });
  }

  public static void change(CarAdapter c) {
    double price = 0.0;
    boolean show = true;
    int count = 0;
    for (int i = 0; i < c.getCount(); i++) {
      Log.e("tag", "change: " + "|" + c.getMdata().get(i).isSelect());
      boolean temp = c.getMdata().get(i).isSelect();
      if (temp) {
        price += c.getMdata().get(i).getPrice() * c.getMdata().get(i).getNum();
        count++;
      }
      show = show & temp;
    }
    if (price > 0) {
      DecimalFormat df = new DecimalFormat("###0.0##");//最多保留几位小数，就用几个#，最少位就用0来确定
      txt_total_price.setText(df.format(price) + "元");
      txt_total_price_b.setVisibility(View.VISIBLE);
    } else {
      txt_total_price.setText("");
      txt_total_price_b.setVisibility(View.INVISIBLE);
    }
    Log.e("tag", "change: " + price);
    checkAll.setChecked((c.getMdata().size() == 0) != show);
    if (count != 0) {
      btn_pay.setText("结算(" + count + ")");
    } else {
      btn_pay.setText("结算");
    }
  }

  private void initCarView() {
    carView = View.inflate(MainActivity.this, R.layout.car_fragment, null);
    ListView carList = (ListView) carView.findViewById(R.id.list_car);
    carAdapter = new CarAdapter(MainActivity.this, carView);
    btn_pay = (Button) carView.findViewById(R.id.btn_pay);
    txt_total_price_b = (TextView) carView.findViewById(R.id.txt_car_total_price_b);
    txt_total_price = (TextView) carView.findViewById(R.id.txt_car_total_price);
    checkAll = (CheckBox) carView.findViewById(R.id.check_car);
    Log.e("tag", "onNavigationItemSelected: " + carAdapter.getCount());
    carList.setAdapter(carAdapter);
    carView.findViewById(R.id.check_car).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        boolean temp = checkAll.isChecked();
        if (temp) {
          btn_pay.setText(
              String.format(getString(R.string.buy_num), carAdapter.getCount() + ""));
        } else {
          btn_pay.setText("结算");
        }
        mHandler.sendEmptyMessage(1);
      }
    });
  }
}