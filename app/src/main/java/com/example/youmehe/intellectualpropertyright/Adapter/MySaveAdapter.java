package com.example.youmehe.intellectualpropertyright.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.youmehe.intellectualpropertyright.Bean.SaveProductList;
import com.example.youmehe.intellectualpropertyright.R;
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by youmehe on 2017/5/9.
 */

public class MySaveAdapter extends BaseAdapter {

  public List<SaveProductList.ResultBean> getMdata() {
    return mdata;
  }

  public void setMdata(
      List<SaveProductList.ResultBean> mdata) {
    this.mdata = mdata;
  }

  List<SaveProductList.ResultBean> mdata;
  Context mContext;

  public MySaveAdapter(Context context) {
    mContext = context;
    mdata = new ArrayList<>();
  }

  @Override public int getCount() {
    return mdata.size();
  }

  @Override public Object getItem(int position) {
    return null;
  }

  @Override public long getItemId(int position) {
    return 0;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {

    ViewHolder viewHolder;
    if (convertView == null) {
      convertView = View.inflate(mContext, R.layout.list_item_my_save, null);
      viewHolder = new ViewHolder(convertView);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    SaveProductList.ResultBean temp = mdata.get(position);
    Picasso.with(mContext)
        .load(temp.getProduct_icon())
        .resize(200, 200)
        .into(viewHolder.imageProduct);
    viewHolder.txtTitle.setText(temp.getTitle());
    viewHolder.ratingNum.setRating(Float.valueOf(temp.getRating()));
    DecimalFormat df = new DecimalFormat("###0.0##");//最多保留几位小数，就用几个#，最少位就用0来确定
    viewHolder.txtPrice.setText(
        String.format(mContext.getString(R.string.price), df.format(temp.getPrice())));
    viewHolder.txtSaled.setText(
        String.format(mContext.getString(R.string.saled), temp.getSaled() + ""));
    return convertView;
  }

  class ViewHolder {
    @Bind(R.id.image_product) ImageView imageProduct;
    @Bind(R.id.txt_title) TextView txtTitle;
    @Bind(R.id.rating_num) RatingBar ratingNum;
    @Bind(R.id.txt_price) TextView txtPrice;
    @Bind(R.id.txt_saled) TextView txtSaled;

    ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
