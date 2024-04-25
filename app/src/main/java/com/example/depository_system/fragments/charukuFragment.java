package com.example.depository_system.fragments;

import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.depository_system.DataManagement;
import com.example.depository_system.R;
import com.example.depository_system.adapters.RukuMaterialAdapter;
import com.example.depository_system.adapters.RukuOrderAdapter;
import com.example.depository_system.informs.MaterialInform;
import com.example.depository_system.informs.RukuRecordInform;
import com.example.depository_system.service.RukuService;
import com.loper7.date_time_picker.DateTimeConfig;
import com.loper7.date_time_picker.dialog.CardDatePickerDialog;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class charukuFragment extends Fragment {

    private View root;
    private RecyclerView recyclerView;

    private String date;
    private String factoryName;

    private Button factoryNameBtn;

    private Button timeButton;

    private Button backButton;

    private Button exportButton;

    private FrameLayout frameLayout;

    private FrameLayout progressBar;

    private FrameLayout emptyLayout;

    private TextView orderTextView;

    private RecyclerView recyclerView_material;

    private List<RukuRecordInform> rukuRecordInforms;

    private Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(root == null) {
            root = inflater.inflate(R.layout.charuku_fragment, container, false);
        }
        factoryNameBtn = root.findViewById(R.id.factory_name_btn);
        timeButton = root.findViewById(R.id.time_btn);
        frameLayout = root.findViewById(R.id.order_detail);
        emptyLayout = root.findViewById(R.id.empty);
        progressBar = root.findViewById(R.id.progress_circular);
        orderTextView = root.findViewById(R.id.order_id);
        recyclerView_material = root.findViewById(R.id.recyclerView_material);
        backButton = root.findViewById(R.id.go_back);
        exportButton = root.findViewById(R.id.export);
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                int index = (int)msg.obj;
                if(index >= 0) {
                    Log.d("kevin", ""+index);
                    showOrderDetail(index);
                } else if(index == -1) {
                    RukuOrderAdapter adapter = new RukuOrderAdapter(root.getContext(), rukuRecordInforms, handler);
                    recyclerView.setAdapter(adapter);
                    showOrder();
                } else if(index == -2) {
                    showEmpty();
                }

            }
        };
        date = getTodayDate();
        timeButton.setText(date);
        recyclerView = root.findViewById(R.id.recyclerView);
        rukuRecordInforms = RukuService.getRukuRecordListByDateOrFactory(date, null);
        recyclerView.setAdapter(new RukuOrderAdapter(root.getContext(), rukuRecordInforms, handler));
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        factoryNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<>();
                set.add("空");
                for(MaterialInform materialInform : DataManagement.materialInforms) {
                    set.add(materialInform.factoryName);
                }
                showListPopupWindow(set.toArray(new String[]{}), factoryNameBtn);
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
            Log.d("kevin", "show charuku fragment");
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
                rukuRecordInforms = RukuService.getRukuRecordListByDateOrFactory(date, factoryName);
                Message msg = new Message();
                if(!rukuRecordInforms.isEmpty()) {
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
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("kevin", "item" + i + "clicked");
                button.setText(list[i]);
                if(button.getId() == R.id.factory_name_btn) {
                    factoryName = list[i];
                    if(list[i] == "空") {
                        factoryName = null;
                        factoryNameBtn.setText("厂家：空");
                    }
                } else if(button.getId() == R.id.time_btn) {
                    date = list[i];
                    if(list[i] == "空") {
                        button.setText("时间：空");
                        date = null;
                    }
                }
                update();
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.show();
    }

    private void showOrderDetail(int index) {
        RukuRecordInform rukuRecordInform = rukuRecordInforms.get(index);
        factoryNameBtn.setVisibility(View.GONE);
        timeButton.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        orderTextView.setText("入库单号：" + rukuRecordInform.inboundId);
        recyclerView_material.setAdapter(new RukuMaterialAdapter(root.getContext(), rukuRecordInform.itemList));
        recyclerView_material.setLayoutManager(new LinearLayoutManager(root.getContext()));
        frameLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void showOrder() {
        factoryNameBtn.setVisibility(View.VISIBLE);
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
        factoryNameBtn.setVisibility(View.VISIBLE);
        timeButton.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        frameLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
    }

}