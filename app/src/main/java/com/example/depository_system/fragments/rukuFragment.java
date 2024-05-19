package com.example.depository_system.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.example.depository_system.DataManagement;
import com.example.depository_system.MainActivity;
import com.example.depository_system.MainInterface;
import com.example.depository_system.R;
import com.example.depository_system.adapters.ImageAdapter;
import com.example.depository_system.frontInforms.FrontRukuInform;
import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.MaterialInform;
import com.example.depository_system.informs.ProjectInform;
import com.example.depository_system.informs.RukuInform;
import com.example.depository_system.informs.UserInform;
import com.example.depository_system.service.DepositoryService;
import com.example.depository_system.service.MaterialService;
import com.example.depository_system.service.ProjectService;
import com.example.depository_system.service.RukuService;
import com.example.depository_system.service.ServiceBase;
import com.example.depository_system.service.UserService;
import com.example.depository_system.view.ImageActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class rukuFragment extends Fragment {

    private View root;

    private EditText depotNameEditText;

    private EditText materialIdentifierEditText;

    private EditText materialNameEditText;

    private EditText materialTypeEditText;

    private EditText materialNumEditText;

    private EditText factoryNameEditText;

    private EditText timeEditText;

    private EditText projectNameEditText;

    private EditText receiverEditText;

    private EditText acceptorEditText;

    private Button photoBtn;

    private Button uploadBtn;

    private Uri photoUri;

    private static final int CAMERA_PERMISSIOS_REQUEST_CODE = 100;

    private ImageButton depotName_btn;
    private ImageButton materialName_btn;
    private ImageButton materialIdentifier_btn;
    private ImageButton materialType_btn;
    private ImageButton factoryName_btn;
    private ImageButton time_btn;
    private ImageButton receiver_btn;
    private ImageButton accepter_btn;
    private ImageButton project_btn;
    private RecyclerView recyclerView;
    private Handler handler;

    List<String> list = new ArrayList<>();
    List<Uri> imageUriList = new ArrayList<>();
    List<UserInform> userInforms;

    int count = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(root == null) {
            root = inflater.inflate(R.layout.ruku_fragment, container, false);
        }

        depotNameEditText = root.findViewById(R.id.depot_name);
        materialIdentifierEditText = root.findViewById(R.id.material_identifier);
        materialNameEditText = root.findViewById(R.id.material_name);
        materialTypeEditText = root.findViewById(R.id.material_type);
        materialNumEditText = root.findViewById(R.id.material_num);
        factoryNameEditText = root.findViewById(R.id.factory_name);
        timeEditText = root.findViewById(R.id.ruku_time);
        projectNameEditText = root.findViewById(R.id.project_name);
        receiverEditText = root.findViewById(R.id.receiver_name);
        acceptorEditText = root.findViewById(R.id.accepter_name);
        depotName_btn = root.findViewById(R.id.image_btn_depotName);
        materialIdentifier_btn = root.findViewById(R.id.image_btn_materialIdentifier);
        materialName_btn = root.findViewById(R.id.image_btn_materialName);
        materialType_btn = root.findViewById(R.id.image_btn_materialType);
        factoryName_btn = root.findViewById(R.id.image_btn_factoryName);
        time_btn = root.findViewById(R.id.image_btn_time);
        receiver_btn = root.findViewById(R.id.image_btn_receiver);
        accepter_btn = root.findViewById(R.id.image_btn_accepter);
        project_btn = root.findViewById(R.id.image_btn_project);
        recyclerView = root.findViewById(R.id.ruku_image_recylerview);
//
        photoBtn = root.findViewById(R.id.bt_photo);
        uploadBtn = root.findViewById(R.id.bt_next);

        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission();
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

        factoryName_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Set<String> set = new HashSet<>();

                    for(MaterialInform materialInform : DataManagement.materialInforms) {
                        if(!materialIdentifierEditText.getText().toString().isEmpty()) {
                            String str = materialIdentifierEditText.getText().toString();
                            if(!materialInform.materialIdentifier.equals(str)) continue;
                        }
                        if(!materialNameEditText.getText().toString().isEmpty()) {
                            String str = materialNameEditText.getText().toString();
                            if(!materialInform.materialName.equals(str)) continue;
                        }
                        if(!materialTypeEditText.getText().toString().isEmpty()) {
                            String str = materialTypeEditText.getText().toString();
                            if(!materialInform.materialModel.equals(str)) continue;
                        }
                        set.add(materialInform.factoryName);
                    }
                    showListPopupWindow(set.toArray(new String[set.size()]), factoryNameEditText);
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

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int indexMsg = (int)msg.obj;
                if(indexMsg >= 100 && indexMsg <= 100+list.size()-1) {
                    Intent intent = new Intent(requireActivity(), ImageActivity.class);
                    intent.putExtra("imageUri", list.get(indexMsg-100));
                    requireActivity().startActivity(intent);
                } else if(indexMsg >= 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                              && indexMsg <= 10000+list.size()-1){
                    int index = indexMsg - 10000;
                    Uri uri = imageUriList.get(index);
                    ContentResolver contentResolver = requireContext().getContentResolver();
                    int rowsDeleted = contentResolver.delete(uri, null, null);
                    if(rowsDeleted > 0) {
                        Toast.makeText(requireContext(), "图片已删除："+list.get(index), Toast.LENGTH_SHORT).show();
                    }
                    list.remove(index);
                    imageUriList.remove(index);
                    updateImages();
                } else if(indexMsg >= 1000 && indexMsg <= 1000+list.size()-1) {
                    int index = indexMsg - 1000;
                    save(index);
                }
            }
        };
//
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadBtn.setClickable(false);
                boolean isOk = true;

                isOk = checkEditTextIsEmpty(depotNameEditText)
                        & checkEditTextIsEmpty(materialIdentifierEditText)
                        & checkEditTextIsEmpty(materialNameEditText)
                        & checkEditTextIsEmpty(materialTypeEditText)
                        & checkEditTextIsEmpty(materialNumEditText)
                        & checkEditTextIsEmpty(factoryNameEditText)
                        & checkEditTextIsEmpty(timeEditText);

                if (!isOk) {
                    Toast.makeText(requireContext(), "请填写必填项", Toast.LENGTH_SHORT).show();
                    depotNameEditText.requestFocus();
                    uploadBtn.setClickable(true);
                    return ;
                }

                FrontRukuInform rukuInform = new FrontRukuInform();
                rukuInform.depotName = depotNameEditText.getText().toString();
                rukuInform.materialIdentifier = materialIdentifierEditText.getText().toString();
                rukuInform.materialName = materialNameEditText.getText().toString();
                rukuInform.materialType = materialTypeEditText.getText().toString();
                rukuInform.materialNum = Integer.parseInt(materialNumEditText.getText().toString());
                rukuInform.factoryName = factoryNameEditText.getText().toString();
                rukuInform.time = timeEditText.getText().toString();
                rukuInform.project = projectNameEditText.getText().toString();
                rukuInform.receiver = receiverEditText.getText().toString();
                rukuInform.accepter = acceptorEditText.getText().toString();

                RukuInform backRukuInform = rukuInform.convetToRukuInform();
                backRukuInform.images = new ArrayList<>();
                backRukuInform.images.add("/sdf/sdf;");
                //设置depotId
                for (DepositoryInform depositoryInform : DataManagement.depositoryInforms) {
                    if (depositoryInform.depotName.equals(rukuInform.depotName)) {
                        backRukuInform.depotId = depositoryInform.depotId;
                        break;
                    }
                }

                if (backRukuInform.depotId == null) {
                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(requireContext());
                    normalDialog.setTitle("新增仓库");
                    normalDialog.setMessage("发现新的仓库名，是否添加?");
                    normalDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DepositoryInform depositoryInform = DepositoryService.insertDepot(backRukuInform.depotName);
                                    DataManagement.updateDepository();
                                    Toast.makeText(requireContext(), "添加仓库" + backRukuInform.depotName + "成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                    normalDialog.show();
                    uploadBtn.setClickable(true);
                    return;
                }
                for(MaterialInform materialInform : DataManagement.materialInforms) {
                    if(materialInform.materialName.equals(backRukuInform.materialName)
                            && materialInform.materialIdentifier.equals(backRukuInform.materialIdentifier)
                            && materialInform.materialModel.equals(backRukuInform.materialModel)
                            && materialInform.factoryName.equals(backRukuInform.factoryName)) {
                        backRukuInform.materialId = materialInform.materialId;
                        break;
                    }
                }
                if(backRukuInform.materialId == null) {
                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(requireContext());
                    normalDialog.setTitle("新增物料");
                    normalDialog.setMessage("发现新的物料，是否添加?\n" +
                            "物料名称：" + backRukuInform.materialName + "\n" +
                            "物料编码：" + backRukuInform.materialIdentifier + "\n" +
                            "物料类型：" + backRukuInform.materialModel + "\n" +
                            "厂家名称：" + backRukuInform.factoryName + "\n");
                    normalDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    boolean result = MaterialService.insertMaterial(backRukuInform.materialName, backRukuInform.materialIdentifier, backRukuInform.materialModel, backRukuInform.factoryName);
                                    if(result) {
                                        DataManagement.updateMaterial();
                                        Toast.makeText(requireContext(), "添加物料成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(requireContext(), "添加物料失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    normalDialog.show();
                    uploadBtn.setClickable(true);
                    return;
                }

                if(backRukuInform.number <= 0) {
                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(requireContext());
                    normalDialog.setMessage("入库数量有问题：" + backRukuInform.number);
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
                    if(projectInform.projectName.equals(backRukuInform.projectName)) {
                        isNewProject = false;
                    }
                }
                if(isNewProject) {
                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(requireContext());
                    normalDialog.setTitle("新增项目");
                    normalDialog.setMessage("发现新的项目，是否添加? \n" +
                            "项目名称：" + backRukuInform.projectName + "\n");
                    normalDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    boolean result = ProjectService.insertProject(backRukuInform.projectName, null);
                                    if(result) {
                                        DataManagement.updateProjectInfo();
                                        Toast.makeText(requireContext(), "添加项目成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(requireContext(), "添加项目失败", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                    normalDialog.show();
                    uploadBtn.setClickable(true);
                    return;
                }
                final String[] result = {""};
                backRukuInform.isNew = false;
                String imageResult = ServiceBase.uploadImage(imageUriList, getContext().getContentResolver());
                if(!imageResult.contains("成功")) {
                    new MaterialDialog.Builder(requireContext())
                            .positiveText("确定")
                            .content("图片上传失败，请重新提交入库")
                            .show();
                    return ;
                } else {
                    new MaterialDialog.Builder(requireContext())
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
                backRukuInform.images = list;
                result[0] = RukuService.action(backRukuInform);
                if (result[0].contains("0")) {
                    DataManagement.updateAll();
                    MaterialDialog materialDialog = new MaterialDialog.Builder(requireContext())
                            .positiveText("确定")
                            .content("入库成功")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    setEditTextEmpty();
                                }
                            })
                            .show();
                }
                uploadBtn.setClickable(true);
            }
        });
        return root;
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            //创建一个临时文件来保存拍照的照片
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(photoFile != null) {
                //获取文件的URI
                photoUri = FileProvider.getUriForFile(getContext(), "com.example.android.fileprovider", photoFile);
                //设置相机应用保存照片的输出路径
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                //启动相机应用
                startActivityForResult(takePictureIntent, 60);
            }
        }
    }

    private File createImageFile() throws IOException {
        //创建一个唯一的文件名
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        return imageFile;
    }

    private void requestCameraPermission() {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSIOS_REQUEST_CODE);
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
            Toast.makeText(getContext(), "没有获得相机权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 60 && resultCode == -1) {
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
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 5));
            recyclerView.setAdapter(new ImageAdapter(list, handler));
        }
    }

    private boolean checkEditTextIsEmpty(EditText editText) {
        if(editText.getText().toString().isEmpty()) {
            editText.setBackground(requireContext().getDrawable(R.drawable.border_red));
            editText.setPadding(0, 0, 35*(int)getResources().getDisplayMetrics().density, 0);
            return false;
        } else {
            editText.setBackground(null);
            return true;
        }
    }

    private void showListPopupWindow(String[] list, EditText editText) {
        Log.d("java", "showpopupwindow");
        ListPopupWindow listPopupWindow= new ListPopupWindow(requireContext());
        listPopupWindow.setAdapter(new ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, list));
        listPopupWindow.setAnchorView(editText);
        listPopupWindow.setModal(true);
        listPopupWindow.setHeight(10*20*3);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("kevin", "item" + i + "clicked");
                editText.setText(list[i]);
                if(editText.getId() == R.id.material_identifier) {
                    for(MaterialInform materialInform : DataManagement.materialInforms) {
                        if(materialInform.materialIdentifier == list[i]) {
                            materialNameEditText.setText(materialInform.materialName);
                            materialTypeEditText.setText(materialInform.materialModel);
                        }
                    }
                }
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.show();
    }

    private void setEditTextEmpty() {
        depotNameEditText.setText("");
        materialIdentifierEditText.setText("");
        materialNameEditText.setText("");
        materialTypeEditText.setText("");
        materialNumEditText.setText("");
        factoryNameEditText.setText("");
        timeEditText.setText("");
        projectNameEditText.setText("");
        receiverEditText.setText("");
        acceptorEditText.setText("");
    }

    private void updateImages() {
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 5));
        recyclerView.setAdapter(new ImageAdapter(list, handler));
    }

    private void save(int index) {
        Uri imageUri = imageUriList.get(index);
        ContentResolver contentResolver = requireContext().getContentResolver();
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = contentResolver.openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            if (bitmap != null) {
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String imageFileName = "IMG_" + timestamp + ".jpg";

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM);

                Uri imageUriSaved = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                if (imageUriSaved != null) {
                    outputStream = contentResolver.openOutputStream(imageUriSaved);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();
                    // 图片保存到相册成功
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 图片保存到相册失败
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteImages() {
        for(Uri uri : imageUriList) {
            ContentResolver contentResolver = requireContext().getContentResolver();
            int rowsDeleted = contentResolver.delete(uri, null, null);
        }
        list.clear();
        imageUriList.clear();
        updateImages();
    }
}
