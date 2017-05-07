/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.youmehe.intellectualpropertyright.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.youmehe.intellectualpropertyright.Bean.ProductListEntity;
import com.example.youmehe.intellectualpropertyright.R;
import com.example.youmehe.intellectualpropertyright.Utils.OnItemClickListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YOLANDA on 2016/7/22.
 */
public class MainItemAdapter extends RecyclerView.Adapter<MainItemAdapter.DefaultViewHolder> {

  public List<ProductListEntity.RetResultBean> getmData() {
    return mData;
  }

  public void setmData(
      List<ProductListEntity.RetResultBean> data) {
    mData.addAll(data);
  }

  private List<ProductListEntity.RetResultBean> mData;

  private OnItemClickListener mOnItemClickListener;

  private Context mContext;

  public MainItemAdapter(Context context) {
    mContext = context;
    mData = new ArrayList<>();
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.mOnItemClickListener = onItemClickListener;
  }

  @Override
  public int getItemCount() {
    return mData == null ? 0 : mData.size();
  }

  @Override
  public MainItemAdapter.DefaultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    DefaultViewHolder viewHolder =
        new DefaultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
            .item_main, parent, false));
    viewHolder.mOnItemClickListener = mOnItemClickListener;
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(MainItemAdapter.DefaultViewHolder holder, int position) {
    holder.setData(mContext, mData.get(position).getIcon(), mData.get(position).getTitle(),
        String.format(mContext.getString(R.string.price), " " + mData.get(position).getPrice()));
  }

  static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView imageViewIcon;
    TextView tvTitle;
    TextView tvDescription;
    OnItemClickListener mOnItemClickListener;

    public DefaultViewHolder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(this);
      imageViewIcon = (ImageView) itemView.findViewById(R.id.image_icon);
      tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
      tvDescription = (TextView) itemView.findViewById(R.id.tv_des);
    }

    public void setData(Context context, String iconUrl, String title, String des) {
      Picasso.with(context)
          .load(iconUrl)
          .resize(200, 200)
          .into(this.imageViewIcon);
      this.tvTitle.setText(title);
      this.tvDescription.setText(des);
    }

    @Override
    public void onClick(View v) {
      if (mOnItemClickListener != null) {
        mOnItemClickListener.onItemClick(getAdapterPosition());
      }
    }
  }
}
