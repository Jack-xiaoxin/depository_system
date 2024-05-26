package com.example.depository_system.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.depository_system.DataManagement;
import com.example.depository_system.R;
import com.example.depository_system.adapters.ChukuBatchAdapter;
import com.example.depository_system.adapters.ImageAdapter;
import com.example.depository_system.adapters.RukuBatchAdapter;
import com.example.depository_system.informs.ChukuActionInform;
import com.example.depository_system.informs.ChukuRecordInform;
import com.example.depository_system.informs.ChukuRecordItemInform;
import com.example.depository_system.informs.DepartmentInform;
import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.KucunInform;
import com.example.depository_system.informs.MaterialInform;
import com.example.depository_system.informs.ProjectInform;
import com.example.depository_system.informs.RukuInform;
import com.example.depository_system.informs.UserInform;
import com.example.depository_system.service.ChukuService;
import com.example.depository_system.service.KucunService;
import com.example.depository_system.service.ServiceBase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BatchChukuActivity extends AppCompatActivity {

    private LinearLayout addAllLinearLayout;
    private LinearLayout addMaterialLinearLayout;
    private Button finishButton;
    private Button backButton;
    private Button addMaterialButton;
    private Button photoButton;
    private Button uploadButton;
    private Button saveMaterialButton;
    private List<ChukuRecordItemInform> chukuRecordItemInforms = new ArrayList<>();
    private Context context;

    private EditText materialIdentifierEditText;
    private EditText materialNameEditText;
    private EditText materialTypeEditText;
    private EditText materialNumEditText;
    private EditText factoryNameEditText;
    private EditText depositoryEditText;
    private EditText projectEditText;
    private EditText projectMajorEditText;
    private EditText departmentEditText;
    private EditText receiverEditText;
    private EditText timeEditText;

    private ImageButton materialIdentifierImageButton;
    private ImageButton materialNameImageButton;
    private ImageButton materialTypeImageButton;
    private ImageButton factoryNameImageButton;
    private ImageButton depositoryImageButton;
    private ImageButton projectImageButton;
    private ImageButton projectMajorImageButton;
    private ImageButton applyierImageButton;
    private ImageButton departmentImageButton;
    private ImageButton timeImageButton;
    private RecyclerView materialRecylerView;
    private RecyclerView imageRecylerView;
    private List<String> images = new ArrayList<>();
    private List<Uri> imageUris = new ArrayList<>();
    private Uri photoUri;
    private static final int CAMERA_PERMISSIOS_REQUEST_CODE = 104;
    private Handler handler;
    private int indexFixing = -1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_chuku);
        context = this;

        addAllLinearLayout = findViewById(R.id.batch_chuku_add_all);
        addMaterialLinearLayout = findViewById(R.id.batch_chuku_add_material);
        addAllLinearLayout.setVisibility(View.VISIBLE);
        addMaterialLinearLayout.setVisibility(View.GONE);
        finishButton = addAllLinearLayout.findViewById(R.id.back);
        backButton = addMaterialLinearLayout.findViewById(R.id.save_material_back);
        addMaterialButton = addAllLinearLayout.findViewById(R.id.add_material_btn);
        photoButton = addMaterialLinearLayout.findViewById(R.id.save_material_photo);
        saveMaterialButton = addMaterialLinearLayout.findViewById(R.id.save_material_btn);
        uploadButton = addAllLinearLayout.findViewById(R.id.upload);

        materialIdentifierEditText = addMaterialLinearLayout.findViewById(R.id.material_identifier);
        materialTypeEditText = addMaterialLinearLayout.findViewById(R.id.material_type);
        materialNameEditText = addMaterialLinearLayout.findViewById(R.id.material_name);
        factoryNameEditText = addMaterialLinearLayout.findViewById(R.id.factory_name);
        materialNumEditText = addMaterialLinearLayout.findViewById(R.id.material_num);
        projectEditText = addAllLinearLayout.findViewById(R.id.project_name);
        departmentEditText = addAllLinearLayout.findViewById(R.id.chuku_user_organization);
        timeEditText = addAllLinearLayout.findViewById(R.id.chuku_time);
        depositoryEditText = addAllLinearLayout.findViewById(R.id.depot_name);
        projectMajorEditText = addAllLinearLayout.findViewById(R.id.project_major);
        receiverEditText = addAllLinearLayout.findViewById(R.id.receiver_name);

        materialIdentifierImageButton = addMaterialLinearLayout.findViewById(R.id.image_btn_materialIdentifier);
        materialNameImageButton = addMaterialLinearLayout.findViewById(R.id.image_btn_materialName);
        materialTypeImageButton = addMaterialLinearLayout.findViewById(R.id.image_btn_materialType);
        factoryNameImageButton = addMaterialLinearLayout.findViewById(R.id.image_btn_factoryName);
        depositoryImageButton = addAllLinearLayout.findViewById(R.id.image_btn_depotName);
        projectImageButton = addAllLinearLayout.findViewById(R.id.image_btn_chuku_project);
        departmentImageButton = addAllLinearLayout.findViewById(R.id.image_btn_chuku_department);
        timeImageButton = addAllLinearLayout.findViewById(R.id.image_btn_time);
        projectMajorImageButton = addAllLinearLayout.findViewById(R.id.image_btn_project_major);
        applyierImageButton = addAllLinearLayout.findViewById(R.id.image_btn_receiver);

        imageRecylerView = addMaterialLinearLayout.findViewById(R.id.chuku_imageList);
        materialRecylerView = addAllLinearLayout.findViewById(R.id.material_list);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                return ;
            }
        };

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chukuRecordItemInforms.size() > 0) {
                    new MaterialDialog.Builder(context)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    finish();
                                }
                            })
                            .content("保存的记录将会丢失，确认返回吗？")
                            .positiveText("确定")
                            .show();
                } else {
                    finish();
                }
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCameraPermission();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMaterialLinearLayout.setVisibility(View.GONE);
                addAllLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        saveMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMaterialButton.setClickable(false);
                boolean isOk = true;

                isOk = checkEditTextIsEmpty(materialIdentifierEditText)
                        & checkEditTextIsEmpty(materialNameEditText)
                        & checkEditTextIsEmpty(materialTypeEditText)
                        & checkEditTextIsEmpty(factoryNameEditText)
                        & checkEditTextIsEmpty(materialNumEditText);

                if(!isOk) {
                    Toast.makeText(getApplicationContext(), "请填写必填项", Toast.LENGTH_SHORT).show();
                    depositoryEditText.requestFocus();
                    saveMaterialButton.setClickable(true);
                    return ;
                }

                ChukuRecordItemInform chukuRecordItemInform = new ChukuRecordItemInform();
                chukuRecordItemInform.depositoryName = depositoryEditText.getText().toString();
                chukuRecordItemInform.materialIdentifier = materialIdentifierEditText.getText().toString();
                chukuRecordItemInform.materialName = materialNameEditText.getText().toString();
                chukuRecordItemInform.materialModel = materialTypeEditText.getText().toString();
                chukuRecordItemInform.number = Integer.parseInt(materialNumEditText.getText().toString());
                chukuRecordItemInform.projectName = projectEditText.getText().toString();
                chukuRecordItemInform.projectMajor = projectEditText.getText().toString();
                chukuRecordItemInform.applier = receiverEditText.getText().toString();
                chukuRecordItemInform.factoryName = factoryNameEditText.getText().toString();
                chukuRecordItemInform.time = timeEditText.getText().toString();
                chukuRecordItemInform.departmentName = departmentEditText.getText().toString();
                chukuRecordItemInform.images = new ArrayList<>(images);
                chukuRecordItemInform.imageUriList = new ArrayList<>(imageUris);

                String depositoryId = "";
                String projectId = "";
                String materialId = "";
                for(DepositoryInform depositoryInform : DataManagement.depositoryInforms) {
                    if(depositoryInform.depotName.equals(chukuRecordItemInform.depositoryName)) {
                        depositoryId = depositoryInform.depotId;
                    }
                }
                if(depositoryId == "") {
                    showAlertDialog("无此仓库，请检查");
                    saveMaterialButton.setClickable(true);
                    return;
                }
                for(ProjectInform projectInform : DataManagement.projectInforms) {
                    if(projectInform.projectName.equals(chukuRecordItemInform.projectName)) {
                        projectId = projectInform.projectId;
                    }
                }
                if(projectId == "") {
                    showAlertDialog("无此项目，请检查");
                    saveMaterialButton.setClickable(true);
                    return;
                }
                for(MaterialInform materialInform : DataManagement.materialInforms) {
                    if(materialInform.materialName.equals(chukuRecordItemInform.materialName)
                        && materialInform.materialIdentifier.equals(chukuRecordItemInform.materialIdentifier)
                        && materialInform.materialModel.equals(chukuRecordItemInform.materialModel)
                        && materialInform.factoryName.equals(chukuRecordItemInform.factoryName)) {
                        materialId = materialInform.materialId;
                    }
                }
                if(materialId == "") {
                    showAlertDialog("无此物料，请检查");
                    saveMaterialButton.setClickable(true);
                    return;
                }
                List<KucunInform> kucunInformList = KucunService.getKucunList(null, materialId, depositoryId,projectId);
                if(kucunInformList.size() < 1) {
                    showAlertDialog("仓库、物料、项目的对应关系有问题，请检查");
                    saveMaterialButton.setClickable(true);
                    return;
                }

                String message = "";
                if(saveMaterialButton.getText().equals("保存")) {
                    chukuRecordItemInforms.add(chukuRecordItemInform);
                    message = "保存成功";
                } else if(saveMaterialButton.getText().equals("修改")) {
                    chukuRecordItemInforms.set(indexFixing, chukuRecordItemInform);
                }

                new MaterialDialog.Builder(context)
                        .content(message)
                        .positiveText("确定")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                clearEditText();
                                clearImages();
                                saveMaterialButton.setClickable(true);
                                addAllLinearLayout.setVisibility(View.VISIBLE);
                                addMaterialLinearLayout.setVisibility(View.GONE);
                                updateMaterialAdapters();
                            }})
                        .show();
            }
        });

        addMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkEditTextIsEmpty(depositoryEditText)
                & checkEditTextIsEmpty(projectEditText)
                & checkEditTextIsEmpty(projectMajorEditText)
                & checkEditTextIsEmpty(departmentEditText)
                & checkEditTextIsEmpty(receiverEditText)
                & checkEditTextIsEmpty(timeEditText)) {
                    addAllLinearLayout.setVisibility(View.GONE);
                    addMaterialLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    showAlertDialog("请填写 基础信息后 再添加 物料信息 ");
                }
            }
        });

        materialIdentifierImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<>();
                for(MaterialInform materialInform : DataManagement.materialInforms) {
                    set.add(materialInform.materialIdentifier);
                }
                showListPopupWindow(set.toArray(new String[set.size()]), materialIdentifierEditText);
            }
        });

        materialNameImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<>();
                for(MaterialInform materialInform : DataManagement.materialInforms) {
                    set.add(materialInform.materialName);
                }
                showListPopupWindow(set.toArray(new String[set.size()]), materialNameEditText);
            }
        });

        materialTypeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<>();
                for(MaterialInform materialInform : DataManagement.materialInforms) {
                    set.add(materialInform.materialModel);
                }
                showListPopupWindow(set.toArray(new String[set.size()]), materialTypeEditText);
            }
        });

        factoryNameImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<>();
                for(MaterialInform materialInform : DataManagement.materialInforms) {
                    set.add(materialInform.factoryName);
                }
                showListPopupWindow(set.toArray(new String[set.size()]), factoryNameEditText);
            }
        });

        projectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<>();
                for(ProjectInform projectInform : DataManagement.projectInforms) {
                    set.add(projectInform.projectName);
                }
                showListPopupWindow(set.toArray(new String[set.size()]), projectEditText);
            }
        });

        depositoryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<>();
                for(DepositoryInform depositoryInform : DataManagement.depositoryInforms) {
                    set.add(depositoryInform.depotName);
                }
                showListPopupWindow(set.toArray(new String[set.size()]), depositoryEditText);
            }
        });

        projectMajorImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<>();
                for(UserInform inform : DataManagement.userInforms) {
                    set.add(inform.userName);
                }
                showListPopupWindow(set.toArray(new String[set.size()]), projectMajorEditText);
            }
        });

        applyierImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<>();
                for(UserInform inform : DataManagement.userInforms) {
                    set.add(inform.userName);
                }
                showListPopupWindow(set.toArray(new String[set.size()]), receiverEditText);
            }
        });


        departmentImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<>();
                for(DepartmentInform departmentInform : DataManagement.departmentInforms) {
                    set.add(departmentInform.department_name);
                }
                showListPopupWindow(set.toArray(new String[set.size()]), departmentEditText);
            }
        });

        timeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(date);
                timeEditText.setText(formattedDate);
                for(ChukuRecordItemInform chukuRecordItemInform : chukuRecordItemInforms) {
                    chukuRecordItemInform.time = timeEditText.getText().toString();
                }
                updateMaterialAdapters();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(ChukuRecordItemInform chukuRecordItemInform : new ArrayList<>(chukuRecordItemInforms)) {
                    chukuOnce(chukuRecordItemInform);
                }
            }
        });
    }


    private void showListPopupWindow(String[] list, EditText editText) {
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
                } else if(editText.getId() == depositoryEditText.getId()) {
                    for(ChukuRecordItemInform chukuRecordItemInform : chukuRecordItemInforms) {
                        chukuRecordItemInform.depositoryName = depositoryEditText.getText().toString();
                    }
                    updateMaterialAdapters();
                } else if(editText.getId() == timeEditText.getId()) {
                    for(ChukuRecordItemInform chukuRecordItemInform : chukuRecordItemInforms) {
                        chukuRecordItemInform.time = timeEditText.getText().toString();
                    }
                    updateMaterialAdapters();
                } else if(editText.getId() == projectEditText.getId()) {
                    for(ChukuRecordItemInform chukuRecordItemInform : chukuRecordItemInforms) {
                        chukuRecordItemInform.projectName = projectEditText.getText().toString();
                    }
                    updateMaterialAdapters();
                } else if(editText.getId() == departmentEditText.getId()) {
                    for(ChukuRecordItemInform chukuRecordItemInform : chukuRecordItemInforms) {
                        chukuRecordItemInform.departmentName = departmentEditText.getText().toString();
                    }
                    updateMaterialAdapters();
                } else if(editText.getId() == receiverEditText.getId()) {
                    for(ChukuRecordItemInform chukuRecordItemInform : chukuRecordItemInforms) {
                        chukuRecordItemInform.applier = receiverEditText.getText().toString();
                    }
                    updateMaterialAdapters();
                } else if(editText.getId() == projectMajorEditText.getId()) {
                    for(ChukuRecordItemInform chukuRecordItemInform : chukuRecordItemInforms) {
                        chukuRecordItemInform.projectMajor = projectMajorEditText.getText().toString();
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
                photoUri = FileProvider.getUriForFile(context, "com.example.android.fileprovider", photoFile);
                //设置相机应用保存照片的输出路径
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                //启动相机应用
                startActivityForResult(takePictureIntent, 64);
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
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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
            Toast.makeText(context, "没有获得相机权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 64 && resultCode == -1) {
            Log.d("kevin", "拍照成功");
            Uri contentUri = new Uri.Builder()
                    .scheme(ContentResolver.SCHEME_CONTENT)
                    .authority("com.example.android.fileprovider")
                    .path(photoUri.getPath())
                    .build();
            Log.d("kevin", contentUri.toString());
            Log.d("kevin", "url" + photoUri);
            images.add(contentUri.toString());
            imageUris.add(photoUri);
            imageRecylerView.setLayoutManager(new GridLayoutManager(this, 5));
            imageRecylerView.setAdapter(new ImageAdapter(images, handler));
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

    private void clearEditText() {
        materialIdentifierEditText.setText("");
        materialTypeEditText.setText("");
        materialNumEditText.setText("");
        materialNameEditText.setText("");
    }

    private void clearImages() {
        images.clear();
        imageUris.clear();
        updateImageRecylerView();
    }

    private void updateImageRecylerView() {
        imageRecylerView.setAdapter(new ImageAdapter(images, handler));
        imageRecylerView.setLayoutManager(new GridLayoutManager(this, 5));
    }

    private void updateMaterialAdapters() {
        materialRecylerView.setAdapter(new ChukuBatchAdapter(context, chukuRecordItemInforms, handler));
        materialRecylerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void chukuOnce(ChukuRecordItemInform chukuRecordItemInform) {
        uploadButton.setClickable(false);
        boolean isOk = true;

        isOk = checkEditTextIsEmpty(depositoryEditText)
                & checkEditTextIsEmpty(departmentEditText)
                & checkEditTextIsEmpty(projectMajorEditText)
                & checkEditTextIsEmpty(projectEditText)
                & checkEditTextIsEmpty(timeEditText)
                & checkEditTextIsEmpty(receiverEditText);

        if(!isOk) {
            Toast.makeText(context, "请填写必填项", Toast.LENGTH_SHORT).show();
            depositoryEditText.requestFocus();
        } else {
            ChukuActionInform backChukuInform = new ChukuActionInform();
            backChukuInform.images = new ArrayList<>(chukuRecordItemInform.images);
            //设置depotId
            for(DepositoryInform depositoryInform : DataManagement.depositoryInforms) {
                if(depositoryInform.depotName.equals(depositoryEditText.getText().toString())) {
                    backChukuInform.depotId = depositoryInform.depotId;
                    break;
                }
            }
            if(backChukuInform.depotId == null) {
                showAlertDialog("没有此仓库，请重新输入");
                uploadButton.setClickable(true);
                return;
            }
            backChukuInform.materialIdentifier = chukuRecordItemInform.materialIdentifier;
            backChukuInform.applyProjectName = chukuRecordItemInform.projectName;
            backChukuInform.applyDepartmentName = chukuRecordItemInform.departmentName;
            backChukuInform.applier = chukuRecordItemInform.applier;
            backChukuInform.director = chukuRecordItemInform.projectMajor;
            backChukuInform.number = chukuRecordItemInform.number;
            backChukuInform.factoryName = chukuRecordItemInform.factoryName;
            backChukuInform.time = chukuRecordItemInform.time;
            //设置materialId
            for(MaterialInform materialInform : DataManagement.materialInforms) {
                if(materialInform.materialIdentifier.equals(backChukuInform.materialIdentifier)
                        && materialInform.factoryName.equals(backChukuInform.factoryName)
                        && materialInform.materialModel.equals(chukuRecordItemInform.materialModel)
                        && materialInform.materialName.equals(chukuRecordItemInform.materialName)) {
                    backChukuInform.materialId = materialInform.materialId;
                    break;
                }
            }
            if(backChukuInform.materialId == null) {
                showAlertDialog("没有此物料，请重新输入");
                uploadButton.setClickable(true);
                return;
            }
            String projectId = "";
            for(ProjectInform projectInform : DataManagement.projectInforms) {
                if(projectInform.projectName.equals(backChukuInform.applyProjectName)) {
                    projectId = projectInform.projectId;
                }
            }
            if(projectId == null) {
                showAlertDialog("没有此项目，请重新输入");
                uploadButton.setClickable(true);
                return;
            }
            if(backChukuInform.number <= 0) {
                showAlertDialog("出库数量有问题：" + backChukuInform.number);
                uploadButton.setClickable(true);
                return;
            }
            //检查数量够不够
            List<KucunInform> kucunInformList = KucunService.getKucunList(null, backChukuInform.materialId, backChukuInform.depotId, projectId);
            KucunInform kucunInform = kucunInformList.get(0);
            if(backChukuInform.number > kucunInform.kucunNumber) {
                showAlertDialog("库存不够, 当前库存数量：" + chukuRecordItemInform.depositoryName + ": " + kucunInform.kucunNumber);
            } else {
                if(chukuRecordItemInform.images.size() > 0) {
                    backChukuInform.images = chukuRecordItemInform.images;
                    String imageResult = ServiceBase.uploadImage(chukuRecordItemInform.imageUriList, getContentResolver());
                    if(!imageResult.contains("成功")) {
                        new MaterialDialog.Builder(context)
                                .positiveText("确定")
                                .content("图片上传失败，请重新提交入库")
                                .show();
                        uploadButton.setClickable(true);
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
                }
                String result = ChukuService.action(backChukuInform);
                if(result.length() == 38) {
                    DataManagement.updateAll();
                    showAlertDialog("出库成功。出库后库存数量：" + chukuRecordItemInform.departmentName + ": " + (kucunInform.kucunNumber - backChukuInform.number));
                    chukuRecordItemInforms.remove(chukuRecordItemInform);
                    updateMaterialAdapters();
                } else {
                    showAlertDialog("出问题啦，请联系开发者");
                }
            }
            uploadButton.setClickable(true);
        }
    }

    public void showAlertDialog(String message) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(message.contains("成功")) {
//                    setEditTextEmpty();
                }
            }
        });
        normalDialog.show();
    }

    private void updateImages() {
        imageRecylerView.setLayoutManager(new GridLayoutManager(context, 5));
        imageRecylerView.setAdapter(new ImageAdapter(images, handler));
    }

    private void deleteImages() {
        for(Uri uri : imageUris) {
            ContentResolver contentResolver = context.getContentResolver();
            int rowsDeleted = contentResolver.delete(uri, null, null);
        }
        images.clear();
        imageUris.clear();
        updateImages();
    }
}
