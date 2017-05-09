package com.example.youmehe.intellectualpropertyright.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.youmehe.intellectualpropertyright.Adapter.MySaveAdapter;
import com.example.youmehe.intellectualpropertyright.Bean.SaveProductList;
import com.example.youmehe.intellectualpropertyright.R;
import com.example.youmehe.intellectualpropertyright.Utils.NetWorkUtils;
import com.google.gson.Gson;
import java.util.List;

public class MySaveActivity extends AppCompatActivity {

  private final int LOAD_DATA_SUCCESS = 0x001;
  private final int LOAD_DATA_FAILED = 0x002;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.list_my_save) ListView listMySave;
  @Bind(R.id.activity_edit_my_info) LinearLayout activityEditMyInfo;
  @Bind(R.id.image_car) ImageView imageCar;
  @Bind(R.id.relative_no_data) RelativeLayout relativeNoData;
  private MySaveAdapter mAdapter;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_save);
    ButterKnife.bind(this);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });

    mAdapter = new MySaveAdapter(this);
    listMySave.setAdapter(mAdapter);
    listMySave.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SaveProductList.ResultBean temp = mAdapter.getMdata().get(position);
        Intent intent = new Intent(MySaveActivity.this, ScrollingActivity.class);
        intent.putExtra("bg", temp.getProduct_icon());
        intent.putExtra("title", temp.getTitle());
        intent.putExtra("content", temp.getContent());
        intent.putExtra("price", temp.getPrice());
        intent.putExtra("saled", temp.getSaled() + "");
        intent.putExtra("rating", temp.getRating());
        intent.putExtra("owner", temp.getOwner());
        intent.putExtra("id", temp.getId() + "");
        startActivity(intent);
      }
    });
  }

  private Handler mHandler = new Handler() {
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case LOAD_DATA_SUCCESS:
          mAdapter.setMdata((List<SaveProductList.ResultBean>) msg.obj);
          mAdapter.notifyDataSetChanged();
          break;

        case LOAD_DATA_FAILED:
          relativeNoData.setVisibility(View.VISIBLE);
          break;
      }
    }
  };

  @Override protected void onResume() {
    super.onResume();
    getData();
  }

  private void getData() {
    new Thread(new Runnable() {
      @Override public void run() {
        String saveStateJson =
            NetWorkUtils.getInstance().userSave(MainActivity.userName, "test", "select");
        Gson gson = new Gson();
        SaveProductList saveProductList = gson.fromJson(saveStateJson, SaveProductList.class);
        Log.e("tag", "run: " + saveStateJson);
        if (saveProductList.getResult() != null) {
          Message msg = Message.obtain();
          msg.what = LOAD_DATA_SUCCESS;
          msg.obj = saveProductList.getResult();
          mHandler.sendMessage(msg);
        } else {
          mHandler.sendEmptyMessage(LOAD_DATA_FAILED);
        }
      }
    }).start();
  }
}