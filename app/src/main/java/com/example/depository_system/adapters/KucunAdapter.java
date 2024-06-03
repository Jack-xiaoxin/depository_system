package com.example.depository_system.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.example.depository_system.DataManagement;
import com.example.depository_system.R;
import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.KucunInform;
import com.example.depository_system.informs.MaterialInform;
import com.example.depository_system.service.KucunService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

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
        itemHolder.materialType.setText("物资型号：" + myMaterialInform.materialModel);
        itemHolder.depositoryName.setText("仓库：" + myDepositoryInform.depotName);
        itemHolder.materialNum.setText("物资数量：" + String.valueOf(mList.get(position).kucunNumber));
        itemHolder.alertNumber.setText("预警数量：" + String.valueOf(mList.get(position).alarmNumber));
        itemHolder.factoryName.setText("厂家：" + myMaterialInform.factoryName);
        itemHolder.projectName.setText("入库项目：" + mList.get(position).projectName);
        itemHolder.materialUnit.setText("计量单位：" + mList.get(position).materialUnit);

        itemHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.itemView, Gravity.BOTTOM);
                popupMenu.getMenuInflater().inflate(R.menu.kucun_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == R.id.menu_item_modify_kucun) {
                            new MaterialDialog.Builder(itemHolder.itemView.getContext())
                                    .title("库存数量")
                                    .inputType(InputType.TYPE_CLASS_NUMBER)
                                    .input(null, String.valueOf(mList.get(position).kucunNumber), false, new MaterialDialog.InputCallback() {
                                        @Override
                                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                            String str = dialog.getInputEditText().getText().toString();
                                            KucunService.updateStoredNumber(mList.get(position).kucunId, str);
                                            DataManagement.updateKucunInfo();
                                            Message msg = new Message();
                                            msg.obj = 1001;
                                            handler.sendMessage(msg);
                                        }
                                    })
                                    .content("请输入修改后的库存数量：")
                                    .negativeText("取消")
                                    .positiveText("修改")
                                    .theme(Theme.LIGHT)
                                    .show();
                        } else if(menuItem.getItemId() == R.id.menu_item_modify_alert) {
                            new MaterialDialog.Builder(itemHolder.itemView.getContext())
                                    .title("预警数量")
                                    .inputType(InputType.TYPE_CLASS_NUMBER)
                                    .input(null, String.valueOf(mList.get(position).alarmNumber), false, new MaterialDialog.InputCallback() {
                                        @Override
                                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                            String str = dialog.getInputEditText().getText().toString();
                                            KucunService.updateAlarmedNumber(mList.get(position).kucunId, str);
                                            DataManagement.updateKucunInfo();
                                            Message msg = new Message();
                                            msg.obj = 1001;
                                            handler.sendMessage(msg);
                                        }
                                    })
                                    .content("请输入修改后的预警数量：")
                                    .negativeText("取消")
                                    .positiveText("修改")
                                    .theme(Theme.LIGHT)
                                    .show();
                        } else if(menuItem.getItemId() == R.id.menu_item_modify_delete) {
                            new MaterialDialog.Builder(itemHolder.itemView.getContext())
                                    .title("删除库存记录")
                                    .content("确认删除此库存记录吗？")
                                    .positiveText("确认")
                                    .negativeText("取消")
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            KucunService.deleteStoredInfo(mList.get(position).kucunId);
                                            DataManagement.updateKucunInfo();
                                            Message msg = new Message();
                                            msg.obj = 1001;
                                            handler.sendMessage(msg);
                                        }
                                    })
                                    .theme(Theme.LIGHT)
                                    .show();
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return false;
            }
        });
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
        @BindView(R.id.kucun_material_unit)
        TextView materialUnit;

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
