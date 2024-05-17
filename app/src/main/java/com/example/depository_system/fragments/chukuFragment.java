package com.example.depository_system.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.depository_system.DataManagement;
import com.example.depository_system.R;
import com.example.depository_system.frontInforms.FrontRukuInform;
import com.example.depository_system.informs.ChukuActionInform;
import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.KucunInform;
import com.example.depository_system.informs.MaterialInform;
import com.example.depository_system.informs.ProjectInform;
import com.example.depository_system.informs.RukuInform;
import com.example.depository_system.informs.UserInform;
import com.example.depository_system.service.ChukuService;
import com.example.depository_system.service.DepositoryService;
import com.example.depository_system.service.KucunService;
import com.example.depository_system.service.MaterialService;
import com.example.depository_system.service.ProjectService;
import com.example.depository_system.service.RukuService;
import com.example.depository_system.service.UserService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class chukuFragment extends Fragment {

    @BindView(R.id.depot_name)
    EditText depotNameEditText;
    @BindView(R.id.material_identifier)
    EditText materialIdentifierEditText;
    @BindView(R.id.material_name)
    EditText materialNameEditText;
    @BindView(R.id.material_type)
    EditText materialTypeEditText;
    @BindView(R.id.material_num)
    EditText materialNumEditText;
    @BindView(R.id.factory_name)
    EditText factoryNamEditText;
    @BindView(R.id.project_name)
    EditText projectNameEditText;
    @BindView(R.id.project_major)
    EditText projectMajorEditText;
    @BindView(R.id.chuku_user_organization)
    EditText userOrganizationEditText;
    @BindView(R.id.receiver_name)
    EditText receiverNameEditText;
    @BindView(R.id.chuku_time)
    EditText timeEditText;
    @BindView(R.id.image_btn_depotName)
    ImageButton depotNameBtn;
    @BindView(R.id.image_btn_materialIdentifier)
    ImageButton materialIdentifierBtn;
    @BindView(R.id.image_btn_materialName)
    ImageButton materialNameBtn;
    @BindView(R.id.image_btn_materialType)
    ImageButton materialTypeBtn;
    @BindView(R.id.image_btn_factoryName)
    ImageButton factoryNameBtn;
    @BindView(R.id.image_btn_chuku_project)
    ImageButton projectBtn;
    @BindView(R.id.image_btn_project_major)
    ImageButton projectMajorNameBtn;
    @BindView(R.id.image_btn_receiver)
    ImageButton receiverNameBtn;
    @BindView(R.id.image_btn_time)
    ImageButton timeBtn;
    @BindView(R.id.upload)
    Button uploadButton;
    @BindView(R.id.photo)
    Button photoButton;

    @BindView(R.id.chuku_image1)
    ImageView imageView1;
    @BindView(R.id.chuku_image2)
    ImageView imageView2;
    @BindView(R.id.chuku_image3)
    ImageView imageView3;
    @BindView(R.id.chuku_image4)
    ImageView imageView4;
    @BindView(R.id.chuku_image5)
    ImageView imageView5;

    @BindView(R.id.chuku_cha1)
    ImageButton imageButton1;
    @BindView(R.id.chuku_cha2)
    ImageButton imageButton2;
    @BindView(R.id.chuku_cha3)
    ImageButton imageButton3;
    @BindView(R.id.chuku_cha4)
    ImageButton imageButton4;
    @BindView(R.id.chuku_cha5)
    ImageButton imageButton5;

    private View root;
    private Context context;
    private Activity activity;
    List<Uri> imageList = new ArrayList<>();
    Uri photoUri;

    private static final int CAMERA_PERMISSIOS_REQUEST_CODE = 100;

    private int count = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireContext();
        activity = requireActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(root == null) {
            root = inflater.inflate(R.layout.chuku_fragment, container, false);
        }
        ButterKnife.bind(this, root);
        depotNameBtn.setOnTouchListener(new View.OnTouchListener() {
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

        materialIdentifierBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Set<String> set = new HashSet<>();
                    for(MaterialInform materialInform : DataManagement.materialInforms) {
                        set.add(materialInform.materialIdentifier);
                    }
                    if(set.size() == 0) {
                        set.add("空");
                    }
                    showListPopupWindow(set.toArray(new String[set.size()]), materialIdentifierEditText);
                }
                return true;
            }
        });

        materialNameBtn.setOnTouchListener(new View.OnTouchListener() {
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

        materialTypeBtn.setOnTouchListener(new View.OnTouchListener() {
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

        factoryNameBtn.setOnTouchListener(new View.OnTouchListener() {
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
                    showListPopupWindow(set.toArray(new String[set.size()]), factoryNamEditText);
                }
                return true;
            }
        });

        projectBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Set<String> set = new HashSet();
                    for(ProjectInform projectInform : DataManagement.projectInforms) {
                        set.add(projectInform.projectName);
                    }
                    showListPopupWindow(set.toArray(new String[set.size()]), projectNameEditText);
                }

                return true;
            }
        });

        timeBtn.setOnTouchListener(new View.OnTouchListener() {
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

        receiverNameBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Set<String> set = new HashSet<>();
                    for(UserInform userInform : DataManagement.userInforms) {
                        set.add(userInform.userName);
                    }
                    showListPopupWindow(set.toArray(new String[set.size()]), receiverNameEditText);
                }
                return true;
            }
        });

        projectMajorNameBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Set<String> set = new HashSet<>();
                    for(UserInform userInform : DataManagement.userInforms) {
                        set.add(userInform.userName);
                    }
                    showListPopupWindow(set.toArray(new String[set.size()]), projectMajorEditText);
                }
                return true;
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadButton.setClickable(false);
                boolean isOk = true;

                isOk = checkEditTextIsEmpty(depotNameEditText)
                        & checkEditTextIsEmpty(materialIdentifierEditText)
                        & checkEditTextIsEmpty(materialNameEditText)
                        & checkEditTextIsEmpty(materialTypeEditText)
                        & checkEditTextIsEmpty(materialNumEditText)
                        & checkEditTextIsEmpty(factoryNamEditText)
                        & checkEditTextIsEmpty(userOrganizationEditText)
                        & checkEditTextIsEmpty(projectMajorEditText)
                        & checkEditTextIsEmpty(projectNameEditText)
                        & checkEditTextIsEmpty(timeEditText);

                if(!isOk) {
                    Toast.makeText(requireContext(), "请填写必填项", Toast.LENGTH_SHORT).show();
                    depotNameEditText.requestFocus();
                } else {
                    ChukuActionInform backChukuInform = new ChukuActionInform();
                    backChukuInform.images = new ArrayList<>();
                    backChukuInform.images.add("/sdf/sdf;");
                    //设置depotId
                    for(DepositoryInform depositoryInform : DataManagement.depositoryInforms) {
                        if(depositoryInform.depotName.equals(depotNameEditText.getText().toString())) {
                            backChukuInform.depotId = depositoryInform.depotId;
                            break;
                        }
                    }
                    if(backChukuInform.depotId == null) {
                        showAlertDialog("没有此仓库，请重新输入");
                        uploadButton.setClickable(true);
                        return;
                    }
                    backChukuInform.materialIdentifier = materialIdentifierEditText.getText().toString();
                    backChukuInform.applyProjectName = projectNameEditText.getText().toString();
                    backChukuInform.applyDepartmentName = userOrganizationEditText.getText().toString();
                    backChukuInform.applier = receiverNameEditText.getText().toString();
                    backChukuInform.director = projectMajorEditText.getText().toString();
                    backChukuInform.number = Integer.parseInt(materialNumEditText.getText().toString());
                    backChukuInform.factoryName = factoryNamEditText.getText().toString();
                    //设置materialId
                    for(MaterialInform materialInform : DataManagement.materialInforms) {
                        if(materialInform.materialIdentifier.equals(backChukuInform.materialIdentifier)
                        && materialInform.factoryName.equals(backChukuInform.factoryName)
                        && materialInform.materialModel.equals(materialTypeEditText.getText().toString())
                        && materialInform.factoryName.equals(materialNameEditText.getText().toString())) {
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
                        showAlertDialog("库存不够, 当前库存数量：" + factoryNamEditText.getText() + ": " + kucunInform.kucunNumber);
                    } else {
                        String result = ChukuService.action(backChukuInform);
                        if(result.length() == 38) {
                            DataManagement.updateAll();
                            showAlertDialog("出库成功。出库后库存数量：" + factoryNamEditText.getText() + ": " + (kucunInform.kucunNumber - backChukuInform.number));
                        } else {
                            showAlertDialog("出问题啦，请联系开发者");
                        }
                    }
                    uploadButton.setClickable(true);
                }
            }
        });

        return root;
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
                if(editText.getId() == materialIdentifierEditText.getId()) {
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

    private void setEditTextEmpty() {
        depotNameEditText.setText("");
        materialIdentifierEditText.setText("");
        materialNameEditText.setText("");
        materialTypeEditText.setText("");
        materialNumEditText.setText("");
        factoryNamEditText.setText("");
        timeEditText.setText("");
        projectNameEditText.setText("");
        projectMajorEditText.setText("");
        receiverNameEditText.setText("");
        userOrganizationEditText.setText("");
    }

    private ImageButton getImageButtonFromCount(int count) {
        if(count == 1) {
            return imageButton1;
        } else if(count == 2) {
            return imageButton2;
        } else if(count == 3) {
            return imageButton3;
        } else if(count == 4) {
            return imageButton4;
        } else if(count == 5) {
            return imageButton5;
        }
        return null;
    }

    private ImageView getImageViewFromCount(int count) {
        if(count == 1) {
            return imageView1;
        } else if(count == 2) {
            return imageView2;
        } else if(count == 3) {
            return imageView3;
        } else if(count == 4) {
            return imageView4;
        } else if(count == 5) {
            return imageView5;
        }
        return null;
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

    private void requestCameraPermission() {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSIOS_REQUEST_CODE);
        } else {
            dispatchTakePictureIntent();
        }
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
                startActivityForResult(takePictureIntent, 80);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 80 && resultCode == -1) {
            Log.d("kevin", "拍照成功");
            imageList.add(photoUri);

            InputStream inputStream = null;
            try {
                inputStream = requireContext().getContentResolver().openInputStream(photoUri);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            Bitmap rotatedBitmap = BitmapFactory.decodeStream(inputStream);
            Matrix matrix = new Matrix();
            matrix.postRotate(90); // Rotate 90 degrees clockwise

            Bitmap originalBitmap = Bitmap.createBitmap(rotatedBitmap, 0, 0, rotatedBitmap.getWidth(), rotatedBitmap.getHeight(), matrix, true);
            float density = getResources().getDisplayMetrics().density;
            int maxWidth = (int) (60 * density);
            int maxHeight = (int) (80 * density);
            int originalWidth = originalBitmap.getWidth();
            int originalHeight = originalBitmap.getHeight();
            float scale = Math.min((float) maxWidth / originalWidth, (float) maxHeight / originalHeight);
            int scaledWidth = Math.round(originalWidth * scale);
            int scaledHeight = Math.round(originalHeight * scale);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, scaledWidth, scaledHeight, false);

            ImageView imageView = getImageViewFromCount(count);
            ImageButton imageButton = getImageButtonFromCount(count);
            ImageButton preImageButton = getImageButtonFromCount(count-1);
            if(preImageButton != null) preImageButton.setVisibility(View.GONE);
            count++;
            imageView.setImageBitmap(scaledBitmap);
            imageView.setVisibility(View.VISIBLE);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //删除图片
                    Uri uri = imageList.get(count-2);
                    new File(uri.getPath()).delete();
                    Toast.makeText(requireContext(), "已删除图片"+uri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
                    //不再显示
                    imageView.setImageBitmap(null);
                    view.setVisibility(View.GONE);
                    count--;
                    if(preImageButton != null) preImageButton.setVisibility(View.VISIBLE);
                    if(count <= 5) {
                        photoButton.setClickable(true);
                        photoButton.setBackground(requireContext().getDrawable(R.drawable.conor_button_green));
                    }
                }
            });
            imageButton.setVisibility(View.VISIBLE);

            if(count >= 6) {
                photoButton.setClickable(false);
                photoButton.setBackground(requireContext().getDrawable(R.drawable.conor_button_grey));
            }

        }
    }

    public void showAlertDialog(String message) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(requireContext());
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(message.contains("成功")) {
                    setEditTextEmpty();
                }
            }
        });
        normalDialog.show();
    }
}