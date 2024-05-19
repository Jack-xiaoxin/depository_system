package com.example.depository_system.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.depository_system.DataManagement;
import com.example.depository_system.R;
import com.example.depository_system.adapters.ChukuMaterialAdapter;
import com.example.depository_system.adapters.ChukuOrderAdapter;
import com.example.depository_system.adapters.RukuMaterialAdapter;
import com.example.depository_system.adapters.RukuOrderAdapter;
import com.example.depository_system.informs.ChukuRecordInform;
import com.example.depository_system.informs.ChukuRecordItemInform;
import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.KucunInform;
import com.example.depository_system.informs.MaterialInform;
import com.example.depository_system.informs.RukuRecordItemInform;
import com.example.depository_system.service.ChukuService;
import com.example.depository_system.service.KucunService;
import com.example.depository_system.view.ImageActivity;
import com.loper7.date_time_picker.DateTimeConfig;
import com.loper7.date_time_picker.dialog.CardDatePickerDialog;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class chachukuFragment extends Fragment {

    private View root;
    private RecyclerView recyclerView;
    private String date = null;
    private String departmentName = null;
    private String project;
    private Button departmentNameBtn;
    private Button projectBtn;
    private Button timeButton;
    private Button backButton;
    private Button exportButton;
    private FrameLayout frameLayout;

    private FrameLayout progressBar;

    private FrameLayout emptyLayout;

    private TextView orderTextView;

    private RecyclerView recyclerView_material;

    private List<ChukuRecordInform> chukuRecordInforms;
    private List<ChukuRecordInform> chukuRecordInforms1;

    private Handler handler;

    private int myIndex;

    private static final int REQUEST_CODE = 11;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(root == null) {
            root = inflater.inflate(R.layout.chachuku_fragment, container, false);
        }
        departmentNameBtn = root.findViewById(R.id.department_name_btn);
        projectBtn = root.findViewById(R.id.project_btn);
        timeButton = root.findViewById(R.id.time_btn);
        frameLayout = root.findViewById(R.id.order_detail);
        emptyLayout = root.findViewById(R.id.empty);
        progressBar = root.findViewById(R.id.progress_circular);
        orderTextView = root.findViewById(R.id.outbound_indentifier);
        recyclerView_material = root.findViewById(R.id.recyclerView_material);
        backButton = root.findViewById(R.id.go_back);
        exportButton = root.findViewById(R.id.export);
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                int arg1 = (int)msg.arg1;
                if(arg1 == 100 || arg1 == 101) {
                    if(arg1 == 100) {
                        String imageUri = (String)msg.obj;
                        Intent intent = new Intent(requireActivity(), ImageActivity.class);
                        intent.putExtra("imageUri", imageUri);
                        startActivity(intent);
                    } else if(arg1 == 101) {
                        //Todo save image
                    }
                } else {
                    int index = (int)msg.obj;
                    if(index >= 0) {
                        Log.d("kevin", ""+index);
                        showOrderDetail(index);
                    } else if(index == -1) {
                        ChukuOrderAdapter adapter = new ChukuOrderAdapter(root.getContext(), chukuRecordInforms, handler);
                        recyclerView.setAdapter(adapter);
                        showOrder();
                    } else if(index == -2) {
                        showEmpty();
                    }
                }
            }
        };

        date = getTodayDate();
        timeButton.setText(date);
        recyclerView = root.findViewById(R.id.recyclerView);
        chukuRecordInforms = ChukuService.getChukuRecordList(date, null, null, null);
        recyclerView.setAdapter(new ChukuOrderAdapter(root.getContext(), chukuRecordInforms, handler));
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        departmentNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<>();
                set.add("空");
                for(ChukuRecordInform chukuRecordInform : chukuRecordInforms1) {
                    set.add(chukuRecordInform.applyDepartmentName);
                }
                showListPopupWindow(set.toArray(new String[]{}), departmentNameBtn);
            }
        });

        projectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<>();
                set.add("空");
                for(ChukuRecordInform chukuRecordInform : chukuRecordInforms1) {
                    set.add(chukuRecordInform.applyProjectName);
                }
                showListPopupWindow(set.toArray(new String[]{}), projectBtn);
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CardDatePickerDialog.Builder(root.getContext())
                        .setTitle("选择时间")
                        .showBackNow(false)
                        .setDisplayType(new int[]{DateTimeConfig.YEAR,DateTimeConfig.MONTH,DateTimeConfig.DAY})
                        .setOnChoose("确定", aLong -> {
                            String time = getDateFromMill(aLong);
                            date = time;
                            timeButton.setText(time);
                            update();
                            return null;
                        }).build().show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
                showOrder();
            }
        });
        return root;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden) {
            update();
        }
    }

    public void update() {
        super.onResume();
        showProgressBar();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                chukuRecordInforms = ChukuService.getChukuRecordList(date, departmentName, project, null);
                chukuRecordInforms1 = ChukuService.getChukuRecordList(date, null, null, null);
                Message msg = new Message();
                if(!chukuRecordInforms.isEmpty()) {
                    msg.obj = -1;
                } else {
                    msg.obj = -2;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    public String getTodayDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public String getDateFromMill(long mill) {
        Date date = new Date(mill);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    private void showListPopupWindow(String[] list, Button button) {
        Log.d("java", "showpopupwindow");
        ListPopupWindow listPopupWindow= new ListPopupWindow(requireContext());
        listPopupWindow.setAdapter(new ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, list));
        listPopupWindow.setAnchorView(button);
        listPopupWindow.setModal(true);
        listPopupWindow.setHeight(10*20*3);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("kevin", "item" + i + "clicked");
                button.setText(list[i]);
                if(button.getId() == R.id.department_name_btn) {
                    departmentName = list[i];
                    if(list[i] == "空") {
                        departmentName = null;
                        departmentNameBtn.setText("领用单位：空");
                    }
                } else if(button.getId() == R.id.time_btn) {
                    date = list[i];
                    if(list[i] == "空") {
                        button.setText("时间：空");
                        date = null;
                    }
                } else if(button.getId() == R.id.project_btn) {
                    project = list[i];
                    if(list[i] == "空") {
                        button.setText("领用项目：空");
                        project = null;
                    }
                }
                update();
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.show();
    }

    private void showOrderDetail(int index) {
        ChukuRecordInform chukuRecordInform = chukuRecordInforms.get(index);
        departmentNameBtn.setVisibility(View.GONE);
        projectBtn.setVisibility(View.GONE);
        timeButton.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        orderTextView.setText("出库单号：" + chukuRecordInform.outboundIdentifier);
        recyclerView_material.setAdapter(new ChukuMaterialAdapter(root.getContext(), chukuRecordInform.itemList, handler));
        recyclerView_material.setLayoutManager(new LinearLayoutManager(root.getContext()));
        frameLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myIndex = index;
                checkPermission();
            }
        });
    }

    private void showOrder() {
        departmentNameBtn.setVisibility(View.VISIBLE);
        projectBtn.setVisibility(View.VISIBLE);
        timeButton.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showEmpty() {
        departmentNameBtn.setVisibility(View.VISIBLE);
        projectBtn.setVisibility(View.VISIBLE);
        timeButton.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        frameLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
    }

    public void checkPermission() {
        try {
            String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            int permission = ActivityCompat.checkSelfPermission(requireContext(), "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS_STORAGE, REQUEST_CODE);
            } else {
                exportExcel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            exportExcel();
        } else {
            Toast.makeText(requireContext(), "未获得文件权限", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportExcel() {
        ChukuRecordInform chukuRecordInform = chukuRecordInforms.get(myIndex);
        List<ChukuRecordItemInform> chukuRecordItemInformList = chukuRecordInform.itemList;
        try {
            // 创建excel xlsx格式
            Workbook wb = new XSSFWorkbook();
//            // 创建工作表
            Sheet sheet = wb.createSheet();
            String[] title = {"出库单号", "仓库", "物料编码", "物资名称", "物资型号", "物资数量", "厂家名称", "领用人", "领用项目", "项目负责人","出库时间"};
            //创建行对象
            Row row = sheet.createRow(0);
            // 设置有效数据的行数和列数
            int colNum = title.length;
            for (int i = 0; i < colNum; i++) {
                sheet.setColumnWidth(i, 20 * 256);  // 每列20个字符宽
                Cell cell1 = row.createCell(i);
                //第一行
                cell1.setCellValue(title[i]);
            }
            //导入数据
            for (int rowNum = 0; rowNum < chukuRecordItemInformList.size(); rowNum++) {
                // 之所以rowNum + 1 是因为要设置第二行单元格
                row = sheet.createRow(rowNum + 1);
                // 设置单元格显示宽度
                row.setHeightInPoints(28f);
                ChukuRecordItemInform chukuRecordItemInform = chukuRecordItemInformList.get(rowNum);
                for (int j = 0; j < title.length; j++) {
                    Cell cell = row.createCell(j);
                    switch (j) {
                        case 0:
                            cell.setCellValue(chukuRecordInform.outboundIdentifier);
                            break;
                        case 1:
                            for(DepositoryInform depositoryInform : DataManagement.depositoryInforms) {
                                if(chukuRecordItemInform.depository_id.equals(depositoryInform.depotId)){
                                    cell.setCellValue(depositoryInform.depotName);
                                }
                            }
                            break;
                        case 2:
                            cell.setCellValue(chukuRecordItemInform.materialIdentifier);
                            break;
                        case 3:
                            cell.setCellValue(chukuRecordItemInform.materialName);
                            break;
                        case 4:
                            cell.setCellValue(chukuRecordItemInform.materialModel);
                            break;
                        case 5:
                            cell.setCellValue(chukuRecordItemInform.number);
                            break;
                        case 6:
                            cell.setCellValue(chukuRecordItemInform.factoryName);
                            break;
                        case 7:
                            cell.setCellValue(chukuRecordItemInform.applier);
                            break;
                        case 8:
                            cell.setCellValue(chukuRecordItemInform.projectName);
                            break;
                        case 9:
                            cell.setCellValue(chukuRecordItemInform.projectMajor);
                            break;
                        case 10:
                            cell.setCellValue(chukuRecordItemInform.outboundTime);
                            break;
                        default:
                            break;
                    }
                }
            }
            String mSDCardFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/出库数据";
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
            normalDialog.setTitle("导出出库单");
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

    public String convertTime(long time, String patter) {
        SimpleDateFormat sdf = new SimpleDateFormat(patter);
        return sdf.format(new Date(time));
    }

}