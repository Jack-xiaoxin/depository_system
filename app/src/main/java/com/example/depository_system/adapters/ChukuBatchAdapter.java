package com.example.depository_system.adapters;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.depository_system.DataManagement;
import com.example.depository_system.R;
import com.example.depository_system.informs.ChukuRecordItemInform;
import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.RukuInform;
import com.example.depository_system.informs.RukuRecordInform;
import com.example.depository_system.informs.RukuRecordItemInform;
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

public class ChukuBatchAdapter extends BaseRecycleAdapter{

    private List<ChukuRecordItemInform> mList;

    private Context context;

    private Handler handler;

    public ChukuBatchAdapter(Context context, List<ChukuRecordItemInform> list, Handler handler) {
        this.context = context;
        this.mList = list;
        this.handler = handler;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChukuBatchAdapter.ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_chuku_material, parent, false));
    }

    @Override
    public int getItemCount() {
        return this.mList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        super.onBindViewHolder(holder, position);
        ItemHolder itemHolder = (ItemHolder) holder;
        itemHolder.materialIdentifier.setText("物资编码：" + mList.get(position).materialIdentifier);
        itemHolder.materialName.setText("物资名称：" + mList.get(position).materialName);
        itemHolder.materialType.setText("物资型号：" + mList.get(position).materialModel);
        itemHolder.materialNum.setText("物资数量：" + mList.get(position).number);
        itemHolder.factoryName.setText("厂家名称：" + mList.get(position).factoryName);
        itemHolder.time.setText("时间：" + mList.get(position).time);
        itemHolder.receiver.setText("领用人：" + mList.get(position).applier);
        itemHolder.projectMajor.setText("项目负责人：" + mList.get(position).projectMajor);
        itemHolder.depositoryName.setText("仓库：" + mList.get(position).depositoryName);
        itemHolder.projectName.setText("领用项目：" + mList.get(position).projectName);
        itemHolder.depositoryName.setText("仓库：" + mList.get(position).depositoryName);
        itemHolder.departmentName.setText("领用单位：" + mList.get(position).departmentName);
        itemHolder.recyclerView.setAdapter(new ImageAdapter_display(mList.get(position).images, handler));
        itemHolder.recyclerView.setLayoutManager(new GridLayoutManager(context, 5));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.obj = position;
                handler.sendMessage(msg);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.obj = position;
                handler.sendMessage(msg);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.obj = position;
                handler.sendMessage(msg);
            }
        });
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chuku_image)
        ImageView imageView;
        @BindView(R.id.chuku_order_material_name)
        TextView materialName;
        @BindView(R.id.chuku_order_material_type)
        TextView materialType;
        @BindView(R.id.chuku_order_material_identifier)
        TextView materialIdentifier;
        @BindView(R.id.chuku_order_material_num)
        TextView materialNum;
        @BindView(R.id.chuku_order_material_factory)
        TextView factoryName;
        @BindView(R.id.chuku_department_name)
        TextView departmentName;
        @BindView(R.id.chuku_time)
        TextView time;
        @BindView(R.id.chuku_accepter_name)
        TextView receiver;
        @BindView(R.id.chuku_project_major)
        TextView projectMajor;
        @BindView(R.id.chuku_project_name)
        TextView projectName;
        @BindView(R.id.chuku_depository)
        TextView depositoryName;
        @BindView(R.id.chuku_material_imagelist)
        RecyclerView recyclerView;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
