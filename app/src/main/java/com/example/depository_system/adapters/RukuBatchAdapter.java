package com.example.depository_system.adapters;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.depository_system.DataManagement;
import com.example.depository_system.R;
import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.RukuInform;
import com.example.depository_system.informs.RukuRecordInform;
import com.example.depository_system.informs.RukuRecordItemInform;
import com.example.depository_system.util.NumberUtil;
import com.example.depository_system.view.ImageActivity;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.LogRecord;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RukuBatchAdapter extends BaseRecycleAdapter{

    private List<RukuInform> mList;

    private Context context;

    private Handler handler;

    public RukuBatchAdapter(Context context, List<RukuInform> list, Handler handler) {
        this.context = context;
        this.mList = list;
        this.handler = handler;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RukuBatchAdapter.ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_ruku_material, parent, false));
    }

    @Override
    public int getItemCount() {
        return this.mList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemHolder itemHolder = (ItemHolder) holder;
        itemHolder.materialIdentifier.setText("物资编码：" + mList.get(position).materialIdentifier);
        itemHolder.materialName.setText("物资名称：" + mList.get(position).materialName);
        itemHolder.materialType.setText("物资型号：" + mList.get(position).materialModel);
        itemHolder.materialNum.setText("物资数量：" + NumberUtil.DoubleToString(mList.get(position).number));
        itemHolder.materialUnit.setText("计量单位：" + mList.get(position).materialUnit);
        itemHolder.factoryName.setText("厂家名称：" + mList.get(position).factoryName);
        itemHolder.time.setText("时间：" + mList.get(position).time);
        itemHolder.receiver.setText("收货人：" + mList.get(position).receiver);
        itemHolder.accepter.setText("验收人：" + mList.get(position).acceptor);
        itemHolder.depositoryName.setText("仓库：" + mList.get(position).depotName);
        itemHolder.projectName.setText("入库项目：" + mList.get(position).projectName);
        itemHolder.recyclerView.setAdapter(new ImageAdapter_display(mList.get(position).images, handler));
        itemHolder.recyclerView.setLayoutManager(new GridLayoutManager(context, 5));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.itemView, Gravity.BOTTOM);
                popupMenu.getMenuInflater().inflate(R.menu.bottom_menu_2, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == R.id.menu_item_modify) {
                            Message msg = new Message();
                            msg.obj = position;
                            msg.arg1 = 100;
                            handler.sendMessage(msg);
                        } else if(menuItem.getItemId() == R.id.menu_item_delete) {
                            Message msg = new Message();
                            msg.obj = position;
                            msg.arg1 = 101;
                            handler.sendMessage(msg);
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return false;
            }
        });
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ruku_image)
        ImageView imageView;
        @BindView(R.id.ruku_order_material_name)
        TextView materialName;
        @BindView(R.id.ruku_order_material_type)
        TextView materialType;
        @BindView(R.id.ruku_order_material_identifier)
        TextView materialIdentifier;
        @BindView(R.id.ruku_order_material_num)
        TextView materialNum;
        @BindView(R.id.ruku_order_material_unit)
        TextView materialUnit;
        @BindView(R.id.ruku_factory_name)
        TextView factoryName;
        @BindView(R.id.ruku_time)
        TextView time;
        @BindView(R.id.ruku_receiver_name)
        TextView receiver;
        @BindView(R.id.ruku_accepter_name)
        TextView accepter;
        @BindView(R.id.ruku_project)
        TextView projectName;
        @BindView(R.id.ruku_depository_name)
        TextView depositoryName;
        @BindView(R.id.charuku_image_list)
        RecyclerView recyclerView;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
