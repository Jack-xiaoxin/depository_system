package com.example.depository_system.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListPopupWindow;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.depository_system.DataManagement;
import com.example.depository_system.R;
import com.example.depository_system.adapters.KucunAdapter;
import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.KucunInform;
import com.example.depository_system.informs.MaterialInform;
import com.example.depository_system.service.KucunService;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class kucunFragment extends Fragment {

    private View root;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FrameLayout emptyFrameLayout;
    @BindView(R.id.depository_name_btn)
    public Button depotNameBtn;
    private String depotName = "";
    @BindView(R.id.alert_btn)
    public Button alertBtn;
    private String alertValue = "";
    @BindView(R.id.material_name_btn)
    public Button materialNameBtn;
    private String materialName = "";
    @BindView(R.id.factory_name_btn)
    public Button factoryNameBtn;
    private String factoryName = "";

    public Handler handler;

    private List<KucunInform> kucunInformList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(root == null) {
            root = inflater.inflate(R.layout.kucun_fragment, container, false);
        }
        depotNameBtn = root.findViewById(R.id.depository_name_btn);
        alertBtn = root.findViewById(R.id.alert_btn);
        materialNameBtn = root.findViewById(R.id.material_name_btn);
        factoryNameBtn = root.findViewById(R.id.factory_name_btn);
        progressBar = root.findViewById(R.id.progress);
        emptyFrameLayout = root.findViewById(R.id.empty);
        progressBar.setVisibility(View.GONE);
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                int index = (int)msg.obj;
                if(index == -1) {
                    progressBar.setVisibility(View.GONE);
                    if(kucunInformList.isEmpty()) {
                        emptyFrameLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        emptyFrameLayout.setVisibility(View.GONE);
                        recyclerView.setAdapter(new KucunAdapter(requireContext(), kucunInformList, handler));
                        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                } else if(index == -2) {
                    progressBar.setVisibility(View.VISIBLE);
                } else if(index == -3) {
                    exportKucun();
                } else if(index == 1001) {
                    recyclerView.setAdapter(new KucunAdapter(requireContext(), DataManagement.kucunInforms, handler));
                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                    recyclerView.setVisibility(View.VISIBLE);
                } else if(index == -4) {
                    progressBar.setVisibility(View.GONE);
                    if(kucunInformList.isEmpty()) {
                        emptyFrameLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        emptyFrameLayout.setVisibility(View.GONE);
                        DataManagement.updateKucunInfo();
                        recyclerView.setAdapter(new KucunAdapter(requireContext(), DataManagement.kucunInforms, handler));
                        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
        recyclerView = root.findViewById(R.id.recyclerView);
        if(DataManagement.kucunInforms.isEmpty()) {
            emptyFrameLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyFrameLayout.setVisibility(View.GONE);
            recyclerView.setAdapter(new KucunAdapter(requireContext(), DataManagement.kucunInforms, handler));
            recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
            recyclerView.setVisibility(View.VISIBLE);
        }
        depotNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<>();
                set.add("仓库：空");
                for(DepositoryInform depositoryInform : DataManagement.depositoryInforms) {
                    set.add(depositoryInform.depotName);
                }
                showListPopupWindow(set.toArray(new String[set.size()]), depotNameBtn);
            }
        });

        alertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<>();
                set.add("预警：空");
                set.add("预警：是");
                set.add("预警：否");
                showListPopupWindow(set.toArray(new String[set.size()]), alertBtn);
            }
        });

        materialNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<>();
                set.add("空");
                for(MaterialInform materialInform : DataManagement.materialInforms) {
                    set.add(materialInform.materialName);
                }
                showListPopupWindow(set.toArray(new String[set.size()]), materialNameBtn);
            }
        });

        factoryNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<>();
                set.add("厂家：空");
                for(MaterialInform materialInform : DataManagement.materialInforms) {
                    set.add(materialInform.factoryName);
                }
                showListPopupWindow(set.toArray(new String[set.size()]), factoryNameBtn);
            }
        });
        return root;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg1 = new Message();
                    msg1.obj = -2;
                    handler.sendMessage(msg1);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    update();
                    Message msg2 = new Message();
                    msg2.obj = -4;
                    handler.sendMessage(msg2);
                }
            }).start();
        }
    }

    private void showListPopupWindow(String[] list, Button button) {
        Log.d("java", "showpopupwindow");
        ListPopupWindow listPopupWindow= new ListPopupWindow(requireContext());
        listPopupWindow.setAdapter(new ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, list));
        listPopupWindow.setAnchorView(button);
        listPopupWindow.setModal(true);
        listPopupWindow.setHeight(10*20*3);
        listPopupWindow.setWidth(10*20*2);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("kevin", "item" + i + "clicked");
                if(button.getId() == R.id.depository_name_btn) {
                    depotNameBtn.setText(list[i]);
                    depotName = list[i];
                    if(list[i].equals("仓库：空")) {
                        depotName = "";
                    }
                } else if(button.getId() == R.id.alert_btn) {
                    alertBtn.setText(list[i]);
                    alertValue = list[i];
                    if(list[i].equals("预警：空")) {
                        alertValue = "";
                    }
                } else if(button.getId() == R.id.material_name_btn) {
                    materialNameBtn.setText(list[i]);
                    materialName = list[i];
                    if(list[i].equals("空")) {
                        materialName = "";
                        materialNameBtn.setText("物料名称：空");
                    }
                }  else if(button.getId() == R.id.factory_name_btn) {
                    factoryNameBtn.setText(list[i]);
                    factoryName = list[i];
                    if(list[i].equals("厂家：空")) {
                        factoryName = "";
                    }
                }
                listPopupWindow.dismiss();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg1 = new Message();
                        msg1.obj = -2;
                        handler.sendMessage(msg1);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        update();
                        Message msg2 = new Message();
                        msg2.obj = -1;
                        handler.sendMessage(msg2);
                    }
                }).start();
            }
        });
        listPopupWindow.show();
    }

    private void update() {
        String depotId = "";
        if(!depotName.isEmpty()) {
            for(DepositoryInform depositoryInform : DataManagement.depositoryInforms) {
                if(depositoryInform.depotName.equals(depotName)) {
                    depotId = depositoryInform.depotId;
                }
            }
        }
        List<String> materialIdList = new ArrayList<>();
        if(!materialName.isEmpty() || !factoryName.isEmpty()) {
            for(MaterialInform materialInform : DataManagement.materialInforms) {
                if(!materialName.isEmpty()) {
                    if(!materialName.equals(materialInform.materialName)) {
                        continue;
                    }
                }
                if(!factoryName.isEmpty()){
                    if(!factoryName.equals(materialInform.factoryName)) {
                        continue;
                    }
                }
                materialIdList.add(materialInform.materialId);
            }
        }
        kucunInformList.clear();
        for(KucunInform kucunInform : DataManagement.kucunInforms) {
            if(!depotId.isEmpty()) {
                if(!kucunInform.depotId.equals(depotId)) continue;
            }
            if(!materialIdList.isEmpty()) {
                if(!materialIdList.contains(kucunInform.materialId)) continue;
            }
            if((!materialName.isEmpty() || !factoryName.isEmpty()) && materialIdList.isEmpty()) continue;
            if(!alertValue.isEmpty()) {
                if(alertValue.equals("预警：是")){
                    if(kucunInform.kucunNumber >= kucunInform.alarmNumber) continue;
                } else {
                    if(kucunInform.kucunNumber < kucunInform.alarmNumber) continue;
                }
            }
            kucunInformList.add(kucunInform);
        }
    }

    private void exportKucun() {
        update();
        try {
            //创建excel xlsx格式
            Workbook wb = new XSSFWorkbook();
            //创建工作表
            Sheet sheet = wb.createSheet();
            String[] title = {"仓库", "入库项目", "物资名称", "物资编码", "物资类型", "物资数量", "预警数量", "厂家"};
            //创建行对象
            Row row = sheet.createRow(0);
            //设置有效数据的行数和列数
            int colNum = title.length;
            for(int i = 0; i < colNum; i++) {
                sheet.setColumnWidth(i, 20*256);
                Cell cell1 = row.createCell(i);
                //第一行
                cell1.setCellValue(title[i]);
            }
            //导入数据
            for(int rowNum = 0; rowNum < kucunInformList.size(); rowNum++) {
                row = sheet.createRow(rowNum+1);
                //设置单元格显示宽度
                row.setHeightInPoints(28f);
                KucunInform kucunInform = kucunInformList.get(rowNum);
                for(int j = 0; j < title.length; j++) {
                    Cell cell = row.createCell(j);
                    switch (j) {
                        case 0:
                            cell.setCellValue(kucunInform.depotName);
                            break;
                        case 1:
                            cell.setCellValue(kucunInform.projectName);
                            break;
                        case 2:
                            cell.setCellValue(kucunInform.materialName);
                            break;
                        case 3:
                            cell.setCellValue(kucunInform.materialIdentifier);
                            break;
                        case 4:
                            cell.setCellValue(kucunInform.materialType);
                            break;
                        case 5:
                            cell.setCellValue(kucunInform.kucunNumber);
                            break;
                        case 6:
                            cell.setCellValue(kucunInform.alarmNumber);
                            break;
                        case 7:
                            cell.setCellValue(kucunInform.factoryName);
                            break;
                        default:
                            break;
                    }
                }
            }
            String mSDCardFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/库存数据";
            File dir = new File(mSDCardFolderPath);
            //判断文件是否存在
            if (!dir.isFile()) {
                //不存在则创建
                dir.mkdir();
            }
            File excel = new File(dir, convertTime(System.currentTimeMillis(), "yyyy-MM-dd-HH-mm-ss") + ".xlsx");
            FileOutputStream fos = new FileOutputStream(excel);
            wb.write(fos);
            fos.flush();
            fos.close();
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(requireContext());
            normalDialog.setTitle("导出库存");
            normalDialog.setMessage("导出成功！文件名：" + excel.getPath());
            normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            normalDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String convertTime(long time, String patter) {
        SimpleDateFormat sdf = new SimpleDateFormat(patter);
        return sdf.format(new Date(time));
    }
}
