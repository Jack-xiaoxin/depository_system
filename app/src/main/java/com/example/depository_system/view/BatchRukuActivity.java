package com.example.depository_system.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.depository_system.DataManagement;
import com.example.depository_system.R;
import com.example.depository_system.adapters.ImageAdapter;
import com.example.depository_system.adapters.RukuBatchAdapter;
import com.example.depository_system.adapters.RukuMaterialAdapter;
import com.example.depository_system.frontInforms.FrontRukuInform;
import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.MaterialInform;
import com.example.depository_system.informs.ProjectInform;
import com.example.depository_system.informs.RukuInform;
import com.example.depository_system.informs.RukuRecordItemInform;
import com.example.depository_system.informs.UserInform;
import com.example.depository_system.service.DepositoryService;
import com.example.depository_system.service.MaterialService;
import com.example.depository_system.service.ProjectService;
import com.example.depository_system.service.RukuService;
import com.example.depository_system.service.ServiceBase;
import com.example.depository_system.service.UserService;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BatchRukuActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSIOS_REQUEST_CODE = 100;

    private LinearLayout add_btn_frameLayout;
    private LinearLayout add_material_frameLayout;
    private ImageButton imgFactoryBtn;
    private Button addMaterialBtn;
    private Button backBtn;

    private EditText factoryEditText;
    private String factoryName;

    private EditText depotNameEditText;
    private EditText materialIdentifierEditText;
    private EditText materialNameEditText;
    private EditText materialTypeEditText;
    private EditText materialNumEditText;
    private EditText timeEditText;
    private EditText projectNameEditText;
    private EditText receiverEditText;
    private EditText acceptorEditText;
    private ImageButton depotName_btn;
    private ImageButton materialName_btn;
    private ImageButton materialIdentifier_btn;
    private ImageButton materialType_btn;
    private ImageButton time_btn;
    private ImageButton receiver_btn;
    private ImageButton accepter_btn;
    private ImageButton project_btn;
    private RecyclerView recyclerView;
    private RecyclerView materialRecyclerView;

    private Button photoBtn;

    private Button saveBtn;
    private Button materialBackBtn;

    private Handler handler;

    private Button uploadBtn;

    List<String> list = new ArrayList<>();
    List<Uri> imageUriList = new ArrayList<>();

    private Uri photoUri;

    private List<RukuInform> rukuInforms = new ArrayList<>();

    private Context context;

    private int indexFixing = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_ruku);

        context = this;

        add_btn_frameLayout = findViewById(R.id.add_factory_framelayout);
        add_material_frameLayout = findViewById(R.id.add_material_framelayout);

        imgFactoryBtn = add_btn_frameLayout.findViewById(R.id.image_btn_factoryName);
        factoryEditText = add_btn_frameLayout.findViewById(R.id.editText_factory_name);
        addMaterialBtn = add_btn_frameLayout.findViewById(R.id.add_material_btn);
        time_btn = add_btn_frameLayout.findViewById(R.id.image_btn_time);
        timeEditText = add_btn_frameLayout.findViewById(R.id.ruku_time);
        uploadBtn = add_btn_frameLayout.findViewById(R.id.upload);

        depotNameEditText = add_material_frameLayout.findViewById(R.id.depot_name);
        materialIdentifierEditText = add_material_frameLayout.findViewById(R.id.material_identifier);
        materialNameEditText = add_material_frameLayout.findViewById(R.id.material_name);
        materialTypeEditText = add_material_frameLayout.findViewById(R.id.material_type);
        materialNumEditText = add_material_frameLayout.findViewById(R.id.material_num);
        projectNameEditText = add_material_frameLayout.findViewById(R.id.project_name);
        receiverEditText = add_material_frameLayout.findViewById(R.id.receiver_name);
        acceptorEditText = add_material_frameLayout.findViewById(R.id.accepter_name);

        depotName_btn = add_material_frameLayout.findViewById(R.id.image_btn_depotName);
        materialIdentifier_btn = add_material_frameLayout.findViewById(R.id.image_btn_materialIdentifier);
        materialName_btn = add_material_frameLayout.findViewById(R.id.image_btn_materialName);
        materialType_btn = add_material_frameLayout.findViewById(R.id.image_btn_materialType);
        receiver_btn = add_material_frameLayout.findViewById(R.id.image_btn_receiver);
        accepter_btn = add_material_frameLayout.findViewById(R.id.image_btn_accepter);
        project_btn = add_material_frameLayout.findViewById(R.id.image_btn_project);
        recyclerView = add_material_frameLayout.findViewById(R.id.batch_add_material_image_recyclerView);

        saveBtn = add_material_frameLayout.findViewById(R.id.save_material_btn);
        photoBtn = add_material_frameLayout.findViewById(R.id.save_material_photo);
        materialBackBtn = add_material_frameLayout.findViewById(R.id.save_material_back);
        materialRecyclerView = add_btn_frameLayout.findViewById(R.id.material_list);

        add_btn_frameLayout.setVisibility(View.VISIBLE);
        add_material_frameLayout.setVisibility(View.GONE);

        imgFactoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<>();
                for(MaterialInform materialInform : DataManagement.materialInforms) {
                    set.add(materialInform.factoryName);
                }
                showListPopupWindow(set.toArray(new String[set.size()]), factoryEditText);
            }
        });

        addMaterialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_btn_frameLayout.setVisibility(View.GONE);
                add_material_frameLayout.setVisibility(View.VISIBLE);
                clearEditText();
                clearImages();
                saveBtn.setText("保存");
            }
        });

        depotName_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Set<String> set = new HashSet<>();
                    //查询仓库
                    for(int i = 0; i < DataManagement.depositoryInforms.size(); i++) {
                        set.add(DataManagement.depositoryInforms.get(i).depotName);
                    }
                    if(set.isEmpty()) {
                        set.add("空");
                    }
                    showListPopupWindow(set.toArray(new String[set.size()]),depotNameEditText);
                }
                return true;
            }
        });

        materialIdentifier_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Set<String> set = new HashSet<>();
                    for(MaterialInform materialInform : DataManagement.materialInforms) {
                        set.add(materialInform.materialIdentifier);
                    }
                    showListPopupWindow(set.toArray(new String[set.size()]), materialIdentifierEditText);
                }
                return true;
            }
        });

        materialName_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Set<String> set = new HashSet<>();
                    for(MaterialInform materialInform : DataManagement.materialInforms) {
                        set.add(materialInform.materialName);
                    }
                    showListPopupWindow(set.toArray(new String[set.size()]), materialNameEditText);
                }
                return true;
            }
        });

        materialType_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Set<String> set = new HashSet<>();
                    for(MaterialInform materialInform : DataManagement.materialInforms) {
                        set.add(materialInform.materialModel);
                    }
                    showListPopupWindow(set.toArray(new String[set.size()]), materialTypeEditText);
                }
                return true;
            }
        });

        project_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Set<String> set = new HashSet<>();
                    for(ProjectInform projectInform : DataManagement.projectInforms) {
                        set.add(projectInform.projectName);
                    }
                    showListPopupWindow(set.toArray(new String[set.size()]), projectNameEditText);
                }
                return true;
            }
        });

        time_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = sdf.format(date);
                    timeEditText.setText(formattedDate);
                    for(RukuInform rukuInform : rukuInforms) {
                        rukuInform.time = timeEditText.getText().toString();
                    }
                    updateMaterialAdapters();
                }
                return true;
            }
        });

        receiver_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Set<String> set = new HashSet<>();
                    for(UserInform userInform : DataManagement.userInforms) {
                        set.add(userInform.userName);
                    }
                    showListPopupWindow(set.toArray(new String[set.size()]), receiverEditText);
                }
                return true;
            }
        });

        accepter_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Set<String> set = new HashSet<>();
                    for(UserInform userInform : DataManagement.userInforms) {
                        set.add(userInform.userName);
                    }
                    showListPopupWindow(set.toArray(new String[set.size()]), acceptorEditText);
                }
                return true;
            }
        });

        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCameraPermission();
            }
        });

        materialBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_btn_frameLayout.setVisibility(View.VISIBLE);
                add_material_frameLayout.setVisibility(View.GONE);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBtn.setClickable(false);
                boolean isOk = true;

                isOk = checkEditTextIsEmpty(depotNameEditText)
                        & checkEditTextIsEmpty(materialIdentifierEditText)
                        & checkEditTextIsEmpty(materialNameEditText)
                        & checkEditTextIsEmpty(materialTypeEditText)
                        & checkEditTextIsEmpty(materialNumEditText);

                if (!isOk) {
                    Toast.makeText(getApplicationContext(), "请填写必填项", Toast.LENGTH_SHORT).show();
                    depotNameEditText.requestFocus();
                    saveBtn.setClickable(true);
                    return ;
                }

                RukuInform rukuInform = new RukuInform();
                rukuInform.depotName = depotNameEditText.getText().toString();
                rukuInform.materialName = materialNameEditText.getText().toString();
                rukuInform.materialModel = materialTypeEditText.getText().toString();
                rukuInform.materialIdentifier = materialIdentifierEditText.getText().toString();
                rukuInform.number = Integer.parseInt(materialNumEditText.getText().toString());
                rukuInform.time = timeEditText.getText().toString();
                rukuInform.projectName = projectNameEditText.getText().toString();
                rukuInform.receiver = receiverEditText.getText().toString();
                rukuInform.acceptor = receiverEditText.getText().toString();
                rukuInform.factoryName = factoryEditText.getText().toString();
                rukuInform.images = new ArrayList<>(list);
                rukuInform.imageUriList = new ArrayList<>(imageUriList);

                String message = "";
                if(saveBtn.getText().equals("保存")) {
                    rukuInforms.add(rukuInform);
                    message = "保存成功";
                } else if(saveBtn.getText().equals("修改")) {
                    rukuInforms.set(indexFixing, rukuInform);
                    message = "修改成功";
                }
                new MaterialDialog.Builder(context)
                        .content(message)
                        .positiveText("确定")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                clearEditText();
                                clearImages();
                                saveBtn.setClickable(true);
                                add_material_frameLayout.setVisibility(View.GONE);
                                add_btn_frameLayout.setVisibility(View.VISIBLE);
                                updateMaterialAdapters();
                            }
                        })
                        .show();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(RukuInform rukuInform : rukuInforms) {
                    uploadOnce(rukuInform);
                }
            }
        });

        initHandler();
    }

    private void showListPopupWindow(String[] list, EditText editText) {
        Log.d("java", "showpopupwindow");
        ListPopupWindow listPopupWindow= new ListPopupWindow(this);
        listPopupWindow.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, list));
        listPopupWindow.setAnchorView(editText);
        listPopupWindow.setModal(true);
        listPopupWindow.setHeight(10*20*3);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("kevin", "item" + i + "clicked");
                editText.setText(list[i]);
                if(editText.getId() == materialIdentifierEditText.getId()) {
                    for(MaterialInform materialInform : DataManagement.materialInforms) {
                        if(editText.getText().toString().equals(materialInform.materialIdentifier)) {
                            materialNameEditText.setText(materialInform.materialName);
                            materialTypeEditText.setText(materialInform.materialModel);
                        }
                    }
                } else if(editText.getId() == factoryEditText.getId()) {
                    for(RukuInform rukuInform : rukuInforms) {
                        rukuInform.factoryName = factoryEditText.getText().toString();
                    }
                    updateMaterialAdapters();
                } else if(editText.getId() == timeEditText.getId()) {
                    for(RukuInform rukuInform : rukuInforms) {
                        rukuInform.time = timeEditText.getText().toString();
                    }
                    updateMaterialAdapters();
                }
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.show();
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //创建一个临时文件来保存拍照的照片
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(photoFile != null) {
                //获取文件的URI
                photoUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                //设置相机应用保存照片的输出路径
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                //启动相机应用
                startActivityForResult(takePictureIntent, 61);
            }
        }
    }

    private File createImageFile() throws IOException {
        //创建一个唯一的文件名
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        return imageFile;
    }

    private void requestCameraPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSIOS_REQUEST_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSIOS_REQUEST_CODE) {
            dispatchTakePictureIntent();
        } else {
            Toast.makeText(this, "没有获得相机权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 61 && resultCode == -1) {
            Log.d("kevin", "拍照成功");
            Uri contentUri = new Uri.Builder()
                    .scheme(ContentResolver.SCHEME_CONTENT)
                    .authority("com.example.android.fileprovider")
                    .path(photoUri.getPath())
                    .build();
            Log.d("kevin", contentUri.toString());
            Log.d("kevin", "url" + photoUri);
            list.add(contentUri.toString());
            imageUriList.add(photoUri);
            updateRecylerView();
        }
    }

    private boolean checkEditTextIsEmpty(EditText editText) {
        if(editText.getText().toString().isEmpty()) {
            editText.setBackground(getDrawable(R.drawable.border_red));
            editText.setPadding(0, 0, 35*(int)getResources().getDisplayMetrics().density, 0);
            return false;
        } else {
            editText.setBackground(null);
            return true;
        }
    }

    private void updateMaterialAdapters() {
        materialRecyclerView.setAdapter(new RukuBatchAdapter(context, rukuInforms, handler));
        materialRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void updateRecylerView() {
        recyclerView.setAdapter(new ImageAdapter(list, handler));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
    }

    private void showConfirmDialog(String message) {
        new MaterialDialog.Builder(context)
                .content(message)
                .positiveText("确定")
                .show();
    }

    private void deleteImages() {

    }

    private void clearEditText() {
        materialIdentifierEditText.setText("");
        materialNameEditText.setText("");
        materialNumEditText.setText("");
        materialTypeEditText.setText("");
        depotNameEditText.setText("");
        projectNameEditText.setText("");
        acceptorEditText.setText("");
        receiverEditText.setText("");
    }

    private void clearImages() {
        list.clear();
        imageUriList.clear();
        updateRecylerView();
    }

    private void setMaterialInformAndDisplay(RukuInform rukuInform) {
        materialIdentifierEditText.setText(rukuInform.materialIdentifier);
        materialNameEditText.setText(rukuInform.materialName);
        materialNumEditText.setText(String.valueOf(rukuInform.number));
        materialTypeEditText.setText(rukuInform.materialModel);
        depotNameEditText.setText(rukuInform.depotName);
        timeEditText.setText(rukuInform.time);
        projectNameEditText.setText(rukuInform.projectName);
        acceptorEditText.setText(rukuInform.acceptor);
        receiverEditText.setText(rukuInform.receiver);
        list = rukuInform.images;
        add_btn_frameLayout.setVisibility(View.GONE);
        add_material_frameLayout.setVisibility(View.VISIBLE);
        updateRecylerView();
        saveBtn.setText("修改");
    }

    private void initHandler() {
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.obj instanceof Integer) {
                    int index = (int)msg.obj;
                    if(index >= 0 && index <= rukuInforms.size()) {
                        setMaterialInformAndDisplay(rukuInforms.get(index));
                        indexFixing = index;
                    } else if(index >= 10000 && index <= 10000+list.size()-1) {
                        ContentResolver contentResolver = getContentResolver();
                        int rowsDeleted = contentResolver.delete(imageUriList.get(index-10000), null, null);
                        list.remove(index-10000);
                        imageUriList.remove(index-10000);
                        updateRecylerView();
                    }
                } else {
                    String imageUri = String.valueOf(msg.obj);
                    Intent intent = new Intent(context, ImageActivity.class);
                    intent.putExtra("imageUri", imageUri);
                    startActivity(intent);
                }
            }
        };
    }

    private void uploadOnce(RukuInform rukuInform) {
        uploadBtn.setClickable(false);
        if (!checkRukuInform(rukuInform)) {
            Toast.makeText(context, "请填写必填项", Toast.LENGTH_SHORT).show();
            depotNameEditText.requestFocus();
            uploadBtn.setClickable(true);
            return ;
        }

        //设置depotId
        for (DepositoryInform depositoryInform : DataManagement.depositoryInforms) {
            if (depositoryInform.depotName.equals(rukuInform.depotName)) {
                rukuInform.depotId = depositoryInform.depotId;
                break;
            }
        }

        if (rukuInform.depotId == null) {
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(context);
            normalDialog.setTitle("新增仓库");
            normalDialog.setMessage("发现新的仓库名，是否添加?");
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DepositoryInform depositoryInform = DepositoryService.insertDepot(rukuInform.depotName);
                            DataManagement.updateDepository();
                            Toast.makeText(context, "添加仓库" + rukuInform.depotName + "成功", Toast.LENGTH_SHORT).show();
                        }
                    });
            normalDialog.show();
            uploadBtn.setClickable(true);
            return;
        }
        for(MaterialInform materialInform : DataManagement.materialInforms) {
            if(materialInform.materialName.equals(rukuInform.materialName)
                    && materialInform.materialIdentifier.equals(rukuInform.materialIdentifier)
                    && materialInform.materialModel.equals(rukuInform.materialModel)
                    && materialInform.factoryName.equals(rukuInform.factoryName)) {
                rukuInform.materialId = materialInform.materialId;
                break;
            }
        }
        if(rukuInform.materialId == null) {
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(context);
            normalDialog.setTitle("新增物料");
            normalDialog.setMessage("发现新的物料，是否添加?\n" +
                    "物料名称：" + rukuInform.materialName + "\n" +
                    "物料编码：" + rukuInform.materialIdentifier + "\n" +
                    "物料类型：" + rukuInform.materialModel + "\n" +
                    "厂家名称：" + rukuInform.factoryName + "\n");
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean result = MaterialService.insertMaterial(rukuInform.materialName, rukuInform.materialIdentifier, rukuInform.materialModel, rukuInform.factoryName);
                            if(result) {
                                DataManagement.updateMaterial();
                                Toast.makeText(context, "添加物料成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "添加物料失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            normalDialog.show();
            uploadBtn.setClickable(true);
            return;
        }

        if(rukuInform.number <= 0) {
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(context);
            normalDialog.setMessage("入库数量有问题：" + rukuInform.number);
            normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            normalDialog.show();
            uploadBtn.setClickable(true);
            return ;
        }

        boolean isNewProject = true;
        for(ProjectInform projectInform : DataManagement.projectInforms) {
            if(projectInform.projectName.equals(rukuInform.projectName)) {
                isNewProject = false;
            }
        }
        if(isNewProject) {
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(context);
            normalDialog.setTitle("新增项目");
            normalDialog.setMessage("发现新的项目，是否添加? \n" +
                    "项目名称：" + rukuInform.projectName + "\n");
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean result = ProjectService.insertProject(rukuInform.projectName, null);
                            if(result) {
                                DataManagement.updateProjectInfo();
                                Toast.makeText(context, "添加项目成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "添加项目失败", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
            normalDialog.show();
            uploadBtn.setClickable(true);
            return;
        }
        final String[] result = {""};
        rukuInform.isNew = false;
        String imageResult = ServiceBase.uploadImage(rukuInform.imageUriList, context.getContentResolver());
        if(!imageResult.contains("成功")) {
            new MaterialDialog.Builder(context)
                    .positiveText("确定")
                    .content("图片上传失败，请重新提交入库")
                    .show();
            return ;
        } else {
            new MaterialDialog.Builder(context)
                    .positiveText("确定")
                    .content("图片上传成功")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            deleteImages();
                        }
                    })
                    .show();
        }
        result[0] = RukuService.action(rukuInform);
        if (result[0].contains("0")) {
            DataManagement.updateAll();
            MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                    .positiveText("确定")
                    .content("入库成功")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            clearEditText();
                            rukuInforms.remove(rukuInform);
                            updateMaterialAdapters();
                        }
                    })
                    .show();
        }
        uploadBtn.setClickable(true);
    }

    private boolean checkRukuInform(RukuInform rukuInform) {
        if(rukuInform.depotName == null || rukuInform.depotName.isEmpty()) return false;
        if(rukuInform.materialIdentifier == null || rukuInform.materialIdentifier.isEmpty()) return false;
        if(rukuInform.materialName == null || rukuInform.materialName.isEmpty()) return false;
        if(rukuInform.materialModel == null || rukuInform.materialModel.isEmpty()) return false;
        if(rukuInform.factoryName == null || rukuInform.factoryName.isEmpty()) return false;
        if(rukuInform.projectName == null || rukuInform.projectName.isEmpty()) return false;
        return true;
    }
}