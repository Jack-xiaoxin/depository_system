package com.example.depository_system.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.depository_system.MainActivity;
import com.example.depository_system.R;
import com.example.depository_system.view.ImageActivity;


import java.util.List;
import android.os.Handler;
import android.widget.PopupMenu;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<String> imageList;

    private Handler handler;

    public ImageAdapter(List<String> imageList, Handler handler) {
        this.imageList = imageList;
        this.handler = handler;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String imageUri = imageList.get(position);
        Glide.with(holder.itemView).load(imageUri).into(holder.imageView);
        holder.imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.imageView, Gravity.BOTTOM);

                // 添加菜单项
                popupMenu.getMenuInflater().inflate(R.menu.bottom_menu, popupMenu.getMenu());

                // 设置菜单项点击监听器
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // 处理菜单项的点击事件
                        switch (item.getItemId()) {
                            case R.id.menu_item_display:
                                Message msg1 = new Message();
//                                msg1.obj = 100+position;
                                msg1.obj = imageUri;
                                handler.sendMessage(msg1);
                                return true;
                            case R.id.menu_item_save:
                                Message msg2 = new Message();
                                msg2.obj = 1000+position;
                                handler.sendMessage(msg2);
                                return true;
                            // 添加其他菜单项的处理逻辑
                            case R.id.menu_item_delete:
                                Message msg3 = new Message();
                                msg3.obj = 10000+position;
                                handler.sendMessage(msg3);
                                return true;
                        }
                        return false;
                    }
                });

                // 显示弹出式菜单
                popupMenu.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
