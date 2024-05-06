package com.example.depository_system.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.depository_system.DataManagement;
import com.example.depository_system.R;
import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.KucunInform;
import com.example.depository_system.informs.MaterialInform;
import com.example.depository_system.informs.RukuInform;
import com.example.depository_system.informs.RukuRecordInform;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

// 一个RecyclerViewd的适配器，用于显示入库单
public class KucunAdapter extends BaseRecycleAdapter{

    private static final int TYPE_ITEM_ALERT = 1;
    private static final int TYPE_ITEM = 0;
    private static int TYPE = TYPE_ITEM;
    private List<KucunInform> mList;

    private Context context;

    private Handler handler;

    public KucunAdapter(Context context, List<KucunInform> list, Handler handler) {
        this.context = context;
        this.mList = list;
        this.handler = handler;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM) {
            return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_kucun, parent, false));
        } else if(viewType == TYPE_ITEM_ALERT) {
            return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_kucun_alert, parent, false));
        }
        return null;
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        super.onBindViewHolder(holder, position);
        ItemHolder itemHolder = (ItemHolder) holder;
        MaterialInform myMaterialInform = new MaterialInform();
        DepositoryInform myDepositoryInform = new DepositoryInform();
        for(MaterialInform materialInform : DataManagement.materialInforms) {
            if(materialInform.materialId.equals(mList.get(position).materialId)) {
                myMaterialInform = materialInform;
            }
        }
        for(DepositoryInform depositoryInform : DataManagement.depositoryInforms) {
            if(depositoryInform.depotId.equals(mList.get(position).depotId)) {
                myDepositoryInform = depositoryInform;
            }
        }
        Drawable drawable = Drawable.createFromPath("/Users/kevinchen/Documents/projects/depository_system/app/src/main/res/drawable/package.png");
        itemHolder.imageView.setImageDrawable(drawable);
        itemHolder.materialName.setText("物资名称：" + myMaterialInform.materialName);
        itemHolder.materialIdentifier.setText("物资编码：" + myMaterialInform.materialIdentifier);
        itemHolder.materialType.setText("物资类型：" + myMaterialInform.materialModel);
        itemHolder.depositoryName.setText("仓库：" + myDepositoryInform.depotName);
        itemHolder.materialNum.setText("物资数量：" + String.valueOf(mList.get(position).kucunNumber));
        itemHolder.alertNumber.setText("预警数量：" + String.valueOf(mList.get(position).alarmNumber));
        itemHolder.factoryName.setText("厂家：" + myMaterialInform.factoryName);
        itemHolder.projectName.setText("入库项目：" + "XXXX");
    }

    public int getItemCount() {
        return mList.size();
    }

    public int getItemViewType(int position) {
        if(mList.get(position).kucunNumber >= mList.get(position).alarmNumber) {
            TYPE = TYPE_ITEM;
        } else {
            TYPE = TYPE_ITEM_ALERT;
        }
        return TYPE;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.kucun_material_name)
        TextView materialName;
        @BindView(R.id.kucun_material_identifier)
        TextView materialIdentifier;
        @BindView(R.id.kucun_material_type)
        TextView materialType;
        @BindView(R.id.kucun_material_num)
        TextView materialNum;
        @BindView(R.id.kucun_alert_number)
        TextView alertNumber;
        @BindView(R.id.depository_name)
        TextView depositoryName;
        @BindView(R.id.kucun_image)
        ImageView imageView;
        @BindView(R.id.factory_name)
        TextView factoryName;
        @BindView(R.id.ruku_project)
        TextView projectName;

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
