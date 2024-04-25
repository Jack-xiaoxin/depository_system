package com.example.depository_system.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.depository_system.R;
import com.example.depository_system.informs.RukuRecordInform;
import com.example.depository_system.informs.RukuRecordItemInform;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RukuMaterialAdapter extends BaseRecycleAdapter{

    private List<RukuRecordItemInform> mList;

    private Context context;

    public RukuMaterialAdapter(Context context, List<RukuRecordItemInform> list) {
        this.context = context;
        this.mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RukuMaterialAdapter.ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_ruku_material, parent, false));
    }



    @Override
    public int getItemCount() {
        return this.mList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemHolder itemHolder = (ItemHolder) holder;
        itemHolder.materialIdentifier.setText(mList.get(position).materialId);
        itemHolder.materialName.setText(mList.get(position).materialName);
        itemHolder.materialType.setText(mList.get(position).materialModel);
        itemHolder.materialNum.setText("物资数量：" + mList.get(position).number);
        itemHolder.factoryName.setText(mList.get(position).factoryName);
        itemHolder.time.setText(mList.get(position).inboundTime);
        itemHolder.receiver.setText(mList.get(position).receiver);
        itemHolder.accepter.setText(mList.get(position).checker);
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
        @BindView(R.id.ruku_factory_name)
        TextView factoryName;
        @BindView(R.id.ruku_time)
        TextView time;
        @BindView(R.id.ruku_receiver_name)
        TextView receiver;
        @BindView(R.id.ruku_accepter_name)
        TextView accepter;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
