package com.example.depository_system.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.depository_system.R;
import com.example.depository_system.informs.ChukuRecordItemInform;
import com.example.depository_system.informs.RukuRecordInform;
import com.example.depository_system.informs.RukuRecordItemInform;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChukuMaterialAdapter extends BaseRecycleAdapter{

    private List<ChukuRecordItemInform> mList;

    private Context context;

    private Handler handler;

    public ChukuMaterialAdapter(Context context, List<ChukuRecordItemInform> list, Handler handler) {
        this.context = context;
        this.mList = list;
        this.handler = handler;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChukuMaterialAdapter.ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_chuku_material, parent, false));
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
        itemHolder.materialType.setText("物资类型：" + mList.get(position).materialModel);
        itemHolder.materialNum.setText("物资数量：" + mList.get(position).number);
        itemHolder.materialFactory.setText("厂家：" + mList.get(position).factoryName);
        itemHolder.time.setText(mList.get(position).outboundTime);
        itemHolder.receiverName.setText("领用人：" + mList.get(position).applier);
        itemHolder.projectName.setText("项目名称：" + mList.get(position).projectName);
        itemHolder.projectMajor.setText("项目负责人：" + mList.get(position).projectMajor);
        itemHolder.departmentName.setText("领用单位：" + mList.get(position).departmentName);
        itemHolder.depositoryName.setText("仓库：" + mList.get(position).depositoryName);
        itemHolder.recyclerView.setAdapter(new ImageAdapter_display(mList.get(position).images, handler));
        itemHolder.recyclerView.setLayoutManager(new GridLayoutManager(context,5));
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
        @BindView(R.id.chuku_order_material_factory)
        TextView materialFactory;
        @BindView(R.id.chuku_order_material_num)
        TextView materialNum;
        @BindView(R.id.chuku_department_name)
        TextView departmentName;
        @BindView(R.id.chuku_time)
        TextView time;
        @BindView(R.id.chuku_project_name)
        TextView projectName;
        @BindView(R.id.chuku_project_major)
        TextView projectMajor;
        @BindView(R.id.chuku_accepter_name)
        TextView receiverName;
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
