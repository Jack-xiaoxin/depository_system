package com.example.depository_system.adapters;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

// 基类的RecyclerView适配器
public abstract class BaseRecycleAdapter extends RecyclerView.Adapter {
    private OnItemClickListener mItemClickListener;

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(holder, holder.getLayoutPosition());
                    mItemClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });
        }
        //convert(holder, mDatas.get(position), position);
    }

    public void onItemClick(RecyclerView.ViewHolder holder, int layoutPosition) {
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}