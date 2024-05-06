package com.example.depository_system.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.depository_system.R;
import com.example.depository_system.informs.RukuInform;
import com.example.depository_system.informs.RukuRecordInform;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

// 一个RecyclerViewd的适配器，用于显示入库单
public class RukuOrderAdapter extends BaseRecycleAdapter{

    private static final int TYPE_END = 1;
    private static final int TYPE_ITEM = 0;
    private static int TYPE = TYPE_ITEM;
    private List<RukuRecordInform> mList;

    private Context context;

    private Handler handler;

    public RukuOrderAdapter(Context context, List<RukuRecordInform> list, Handler handler) {
        this.context = context;
        this.mList = list;
        this.handler = handler;
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
//                itemHolder.imageView.setImageURI(Uri.parse("D:\\projects\\depository_system\\app\\src\\main\\res\\drawable\\alarm_on.png"));
                itemHolder.factory_name.setText("厂家：" + mList.get(position).factoryName);
                itemHolder.orderId.setText("入库单号：" + mList.get(position).inboundIdentifier);
                String originaldate = mList.get(position).inboundDate;
                if(originaldate.length() != 10) {
                    SimpleDateFormat originalFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
                    try {
                        Date date = originalFormat.parse(originaldate);
                        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        String formattedTime = targetFormat.format(date);
                        itemHolder.time.setText("时间：" + formattedTime);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    itemHolder.time.setText("时间：" + originaldate);
                }
                itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Message message = Message.obtain();
                        message.obj = itemHolder.getLayoutPosition();
                        handler.sendMessage(message);
                    }
                });
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
