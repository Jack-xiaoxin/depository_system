package com.example.depository_system.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.depository_system.R;
import com.example.depository_system.informs.RukuInform;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// 一个RecyclerViewd的适配器，用于显示入库单
public class RukuOrderAdapter extends BaseRecycleAdapter{

    private static final int TYPE_END = 1;
    private static final int TYPE_ITEM = 0;
    private static int TYPE = TYPE_ITEM;
    private final List<RukuInform> mList;

    private Context context;

    public RukuOrderAdapter(Context context, List<RukuInform> list) {
        this.context = context;
        this.mList = list;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM) {
            return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_ruku_order, parent, false));
        }
        return null;
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        super.onBindViewHolder(holder, position);
        switch(getItemViewType(position)) {
            case TYPE_ITEM:
                ItemHolder itemHolder = (ItemHolder) holder;
                itemHolder.imageView.setImageURI(Uri.parse("D:\\projects\\depository_system\\app\\src\\main\\res\\drawable\\alarm_on.png"));
                break;
        }
    }

    public int getItemCount() {
        return mList.size();
    }

    public int getItemViewType(int position) {
        TYPE=TYPE_ITEM;
        return TYPE;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ruku_image)
        ImageView imageView;
        @BindView(R.id.ruku_order_id_value)
        TextView orderId;
        @BindView(R.id.ruku_factory_name)
        TextView factory_name;
        @BindView(R.id.ruku_time)
        TextView time;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class EndHoleder extends RecyclerView.ViewHolder {
        public EndHoleder(View itemView) {
            super(itemView);
        }
    }

}
