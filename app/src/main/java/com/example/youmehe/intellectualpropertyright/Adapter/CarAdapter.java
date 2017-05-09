package com.example.youmehe.intellectualpropertyright.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.youmehe.intellectualpropertyright.Activity.MainActivity;
import com.example.youmehe.intellectualpropertyright.Bean.CarProductListEntity;
import com.example.youmehe.intellectualpropertyright.Bean.DeleteProductEntity;
import com.example.youmehe.intellectualpropertyright.Bean.SaveEntity;
import com.example.youmehe.intellectualpropertyright.R;
import com.example.youmehe.intellectualpropertyright.Utils.LoadingDialog;
import com.example.youmehe.intellectualpropertyright.Utils.NetWorkUtils;
import com.example.youmehe.intellectualpropertyright.Utils.ShowDialog;
import com.example.youmehe.intellectualpropertyright.Utils.T;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by youmehe on 2017/5/9.
 */

public class CarAdapter extends BaseAdapter {

  private final int DEL_SUCCESS = 0x005;
  private final int UPDATE_NUM_FAILED = 0x004;
  private final int UPDATE_NUM_SUCCESS = 0x003;
  private final int LOAD_DATA_SUCCESS = 0x001;
  private final int LOAD_DATA_FAILED = 0x002;
  private ShowDialog showDialog;
  private LoadingDialog loadingDialog;

  public void setIsSelcet(boolean select) {
    for (int i = 0; i < mdata.size(); i++) {
      mdata.get(i).setSelect(select);
    }
  }

  public List<CarProductListEntity.ResultBean> getMdata() {
    return mdata;
  }

  public void setMdata(
      List<CarProductListEntity.ResultBean> mdata) {
    Log.e("tag", "setMdata: " + mdata + "|" + this.mdata);
    this.mdata = mdata;
  }

  private Handler mHandler = new Handler() {
    public void handleMessage(Message msg) {
      loadingDialog.dismiss();
      switch (msg.what) {
        case LOAD_DATA_SUCCESS:
          parentView.findViewById(R.id.relative_no_data).setVisibility(View.GONE);
          parentView.findViewById(R.id.relative_list).setVisibility(View.VISIBLE);
          setMdata((List<CarProductListEntity.ResultBean>) msg.obj);
          notifyDataSetChanged();
          MainActivity.change(CarAdapter.this);
          break;

        case LOAD_DATA_FAILED:
          parentView.findViewById(R.id.relative_no_data).setVisibility(View.VISIBLE);
          parentView.findViewById(R.id.relative_list).setVisibility(View.GONE);
          break;

        case UPDATE_NUM_SUCCESS:
          mdata.get(msg.arg2).setNum(msg.arg1);
          notifyDataSetChanged();
          if (mdata.get(msg.arg2).isSelect()) {
            MainActivity.change(CarAdapter.this);
          }
          break;
        case UPDATE_NUM_FAILED:
          T.shortToast(mContext, "请检查网络");
          break;
        case DEL_SUCCESS:
          if (msg.arg1 == 1) {
            mdata.remove(msg.arg2);
          }
          if (msg.arg1 > 1) {
            mdata.clear();
          }
          notifyDataSetChanged();
          if (mdata.size() == 0) {
            parentView.findViewById(R.id.relative_no_data).setVisibility(View.VISIBLE);
            parentView.findViewById(R.id.relative_list).setVisibility(View.GONE);
          }
          break;
      }
    }
  };

  List<CarProductListEntity.ResultBean> mdata;
  Context mContext;
  private View parentView;

  public CarAdapter(Context context, View view) {
    mContext = context;
    mdata = new ArrayList<>();
    parentView = view;
  }

  @Override public int getCount() {
    return mdata.size();
  }

  public void getData() {
    showDialog = new ShowDialog();
    loadingDialog = new LoadingDialog(mContext);

    loadingDialog.show();
    loadingDialog.setMessage("正在加载，请稍后");

    new Thread(new Runnable() {
      @Override public void run() {
        String saveStateJson =
            NetWorkUtils.getInstance().userCar(MainActivity.userName, "test", "select");
        Gson gson = new Gson();
        CarProductListEntity mCarProductListEntity =
            gson.fromJson(saveStateJson, CarProductListEntity.class);
        Log.e("tag", "run: " + saveStateJson);
        if (mCarProductListEntity.getResult() != null) {
          Message msg = Message.obtain();
          msg.what = LOAD_DATA_SUCCESS;
          msg.obj = mCarProductListEntity.getResult();
          mHandler.sendMessage(msg);
        } else {
          mHandler.sendEmptyMessage(LOAD_DATA_FAILED);
        }
      }
    }).start();
  }

  @Override public Object getItem(int position) {
    return null;
  }

  @Override public long getItemId(int position) {
    return 0;
  }

  @Override public View getView(final int position, View convertView, ViewGroup parent) {

    final ViewHolder viewHolder;
    if (convertView == null) {
      convertView = View.inflate(mContext, R.layout.list_item_car_buy, null);
      viewHolder = new ViewHolder(convertView);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    final CarProductListEntity.ResultBean temp = mdata.get(position);

    viewHolder.add.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        loadingDialog.show();
        updateNum(position, MainActivity.userName, temp.getId() + "", "num_add");
      }
    });
    viewHolder.del.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (temp.getNum() == 1) {
          T.shortToast(mContext, "已到达最低个数");
          return;
        }
        loadingDialog.show();
        updateNum(position, MainActivity.userName, temp.getId() + "", "num_del");
      }
    });

    Picasso.with(mContext)
        .load(temp.getProduct_icon())
        .resize(200, 200)
        .into(viewHolder.imageProduct);
    viewHolder.txtTitle.setText(temp.getTitle());
    DecimalFormat df = new DecimalFormat("###0.0##");//最多保留几位小数，就用几个#，最少位就用0来确定
    viewHolder.txtPrice.setText(
        String.format(mContext.getString(R.string.price), df.format(temp.getPrice())));
    viewHolder.num.setText(temp.getNum() + "");
    Log.e("tag", "getView: " + temp.isSelect());
    viewHolder.itemCarCheck.setChecked(temp.isSelect());
    viewHolder.itemCarCheck.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mdata.get(position).setSelect(viewHolder.itemCarCheck.isChecked());
        MainActivity.change(CarAdapter.this);
      }
    });
    convertView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override public boolean onLongClick(View v) {
        boolean isSelectAll = true;
        if (mdata.size() == 1) {
          isSelectAll = true;
        } else {
          for (CarProductListEntity.ResultBean temp : mdata) {
            isSelectAll &= temp.isSelect();
            if (!isSelectAll) {
              break;
            }
          }
        }
        Log.e("tag", "onLongClick: " + isSelectAll);
        final boolean finalIsSelectAll = isSelectAll;
        showDialog.show(mContext, "", isSelectAll ? "是否删除所有服务?" : "是否确认删除该服务？",
            new ShowDialog.OnBottomClickListener() {
              @Override
              public void positive() {
                loadingDialog.show();
                loadingDialog.setMessage("正在删除，请稍后");
                delProduct(position, MainActivity.userName, temp.getId() + "",
                    finalIsSelectAll ? "del_all" : "del");
              }

              @Override
              public void negtive() {

              }
            });
        return false;
      }
    });
    return convertView;
  }

  class ViewHolder {
    @Bind(R.id.item_car_check) AppCompatCheckBox itemCarCheck;
    @Bind(R.id.image_product) ImageView imageProduct;
    @Bind(R.id.txt_title) TextView txtTitle;
    @Bind(R.id.txt_price) TextView txtPrice;
    @Bind(R.id.del) TextView del;
    @Bind(R.id.num) TextView num;
    @Bind(R.id.add) TextView add;

    ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }

  private void updateNum(final int position, final String userName,
      final String productId,
      final String type) {
    new Thread(new Runnable() {
      @Override public void run() {
        String saveStateJson =
            NetWorkUtils.getInstance().userCar(userName, productId, type);
        Gson gson = new Gson();
        SaveEntity mSaveEntity =
            gson.fromJson(saveStateJson, SaveEntity.class);
        Log.e("tag", "run: " + saveStateJson);
        if (mSaveEntity == null) {
          mHandler.sendEmptyMessage(UPDATE_NUM_FAILED);
          return;
        }
        if (mSaveEntity.getResult() != 0) {
          Message msg = Message.obtain();
          msg.what = UPDATE_NUM_SUCCESS;
          msg.arg1 = mSaveEntity.getResult();
          msg.arg2 = position;
          mHandler.sendMessage(msg);
        } else {
          mHandler.sendEmptyMessage(UPDATE_NUM_FAILED);
        }
      }
    }).start();
  }

  private void delProduct(final int position, final String userName, final String productId,
      final String type) {
    new Thread(new Runnable() {
      @Override public void run() {
        String delProductEntity =
            NetWorkUtils.getInstance().userCar(userName, productId, type);
        Gson gson = new Gson();
        DeleteProductEntity deleteProductEntity =
            gson.fromJson(delProductEntity, DeleteProductEntity.class);
        Log.e("tag", "run: " + delProductEntity + userName + productId + type);
        if (deleteProductEntity == null) {
          mHandler.sendEmptyMessage(UPDATE_NUM_FAILED);
          return;
        }
        if (deleteProductEntity.getResult() != 0) {
          Message msg = Message.obtain();
          msg.what = DEL_SUCCESS;
          msg.arg1 = deleteProductEntity.getResult();
          msg.arg2 = position;
          mHandler.sendMessage(msg);
        } else {
          mHandler.sendEmptyMessage(UPDATE_NUM_FAILED);
        }
      }
    }).start();
  }
}