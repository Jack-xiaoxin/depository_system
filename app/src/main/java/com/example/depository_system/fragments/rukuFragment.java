package com.example.depository_system.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.depository_system.MainActivity;
import com.example.depository_system.MainInterface;
import com.example.depository_system.R;
import com.example.depository_system.frontInforms.FrontRukuInform;
import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.MaterialInform;
import com.example.depository_system.informs.RukuInform;
import com.example.depository_system.informs.UserInform;
import com.example.depository_system.service.DepositoryService;
import com.example.depository_system.service.MaterialService;
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

    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;

    private ImageButton imageButton1;
    private ImageButton imageButton2;
    private ImageButton imageButton3;
    private ImageButton imageButton4;
    private ImageButton imageButton5;

    private ImageButton depotName_btn;
    private ImageButton materialName_btn;
    private ImageButton materialIdentifier_btn;
    private ImageButton materialType_btn;
    private ImageButton factoryName_btn;
    private ImageButton time_btn;
    private ImageButton receiver_btn;
    private ImageButton accepter_btn;

    List<Uri> list = new ArrayList<>();

    List<DepositoryInform> depositoryInforms;
    List<MaterialInform> materialInforms;
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
        imageView1 = root.findViewById(R.id.ruku_image1);
        imageView2 = root.findViewById(R.id.ruku_image2);
        imageView3 = root.findViewById(R.id.ruku_image3);
        imageView4 = root.findViewById(R.id.ruku_image4);
        imageView5 = root.findViewById(R.id.ruku_image5);
        imageButton1 = root.findViewById(R.id.ruku_cha1);
        imageButton2 = root.findViewById(R.id.ruku_cha2);
        imageButton3 = root.findViewById(R.id.ruku_cha3);
        imageButton4 = root.findViewById(R.id.ruku_cha4);
        imageButton5 = root.findViewById(R.id.ruku_cha5);
        depotName_btn = root.findViewById(R.id.image_btn_depotName);
        materialIdentifier_btn = root.findViewById(R.id.image_btn_materialIdentifier);
        materialName_btn = root.findViewById(R.id.image_btn_materialName);
        materialType_btn = root.findViewById(R.id.image_btn_materialType);
        factoryName_btn = root.findViewById(R.id.image_btn_factoryName);
        time_btn = root.findViewById(R.id.image_btn_time);
        receiver_btn = root.findViewById(R.id.image_btn_receiver);
        accepter_btn = root.findViewById(R.id.image_btn_accepter);
//
        photoBtn = root.findViewById(R.id.bt_photo);
        uploadBtn = root.findViewById(R.id.bt_next);

        if(depositoryInforms == null)
            depositoryInforms = DepositoryService.getDepostList(null, null);
        if(materialInforms == null)
            materialInforms = MaterialService.getMaterialList(null, null, null, null, null);

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
                    if(depositoryInforms == null)
                        depositoryInforms = DepositoryService.getDepostList(null, null);
                    for(int i = 0; i < depositoryInforms.size(); i++) {
                        set.add(depositoryInforms.get(i).depotName);
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
                    if(materialInforms == null) materialInforms = MaterialService.getMaterialList(null, null, null, null, null);
                    for(MaterialInform materialInform : materialInforms) {
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
                    if(materialInforms == null) materialInforms = MaterialService.getMaterialList(null, null, null, null, null);
                    for(MaterialInform materialInform : materialInforms) {
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
                    if(materialInforms == null) materialInforms = MaterialService.getMaterialList(null, null, null, null, null);
                    for(MaterialInform materialInform : materialInforms) {
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
                    if(materialInforms == null) materialInforms = MaterialService.getMaterialList(null, null, null, null, null);
                    for(MaterialInform materialInform : materialInforms) {
                        set.add(materialInform.factoryName);
                    }
                    showListPopupWindow(set.toArray(new String[set.size()]), factoryNameEditText);
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
                    if(userInforms == null) userInforms = UserService.getUserList(null, null, null, null);
                    for(UserInform userInform : userInforms) {
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
                    if(userInforms == null) userInforms = UserService.getUserList(null, null, null, null);
                    for(UserInform userInform : userInforms) {
                        set.add(userInform.userName);
                    }
                    showListPopupWindow(set.toArray(new String[set.size()]), acceptorEditText);
                }
                return true;
            }
        });
//
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isOk = true;

                isOk = checkEditTextIsEmpty(depotNameEditText)
                        & checkEditTextIsEmpty(materialIdentifierEditText)
                        & checkEditTextIsEmpty(materialNameEditText)
                        & checkEditTextIsEmpty(materialTypeEditText)
                        & checkEditTextIsEmpty(materialNumEditText)
                        & checkEditTextIsEmpty(factoryNameEditText)
                        & checkEditTextIsEmpty(timeEditText);

                if(!isOk) {
                    Toast.makeText(requireContext(), "请填写必填项", Toast.LENGTH_SHORT).show();
                    depotNameEditText.requestFocus();
                }

                if(isOk) {
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
                    for(DepositoryInform depositoryInform: depositoryInforms) {
                        if(depositoryInform.depotName.equals(rukuInform.depotName)) {
                            backRukuInform.depotId = depositoryInform.depotId;
                            break;
                        }
                    }
                    final String[] result = {""};
                    if(backRukuInform.depotId == null) {
                        final AlertDialog.Builder normalDialog =
                                new AlertDialog.Builder(requireContext());
                        normalDialog.setTitle("新增仓库");
                        normalDialog.setMessage("发现新的仓库名，是否添加?");
                        normalDialog.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DepositoryInform depositoryInform = DepositoryService.insertDepot(backRukuInform.depotName);
                                        backRukuInform.depotId = depositoryInform.depotId;
                                        depositoryInforms = DepositoryService.getDepostList(null, null);
                                        boolean isNew = true;
                                        for (MaterialInform materialInform : materialInforms) {
                                            if (materialInform.materialIdentifier.equals(backRukuInform.materialIdentifer)
                                                    && materialInform.materialName.equals(backRukuInform.materialName)
                                                    && materialInform.materialModel.equals(backRukuInform.materialModel)
                                                    && materialInform.factoryName.equals(backRukuInform.factoryName)) {
                                                isNew = false;
                                                backRukuInform.materialId = materialInform.materialId;
                                            }
                                        }
                                        backRukuInform.isNew = isNew;
                                        result[0] = RukuService.action(backRukuInform);
                                    }
                                });
                    } else {
                        boolean isNew = true;
                        for(MaterialInform materialInform: materialInforms) {
                            if(materialInform.materialIdentifier.equals(backRukuInform.materialIdentifer)
                                    && materialInform.materialName.equals(backRukuInform.materialName)
                                    && materialInform.materialModel.equals(backRukuInform.materialModel)
                                    && materialInform.factoryName.equals(backRukuInform.factoryName)) {
                                isNew = false;
                                backRukuInform.materialId = materialInform.materialId;
                            }

                        }
                        backRukuInform.isNew = isNew;
                        result[0] = RukuService.action(backRukuInform);
                    }
                    if(result[0].length() == 38) {
                        final AlertDialog.Builder normalDialog =
                                new AlertDialog.Builder(requireContext());
                        normalDialog.setMessage("入库成功");
                        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setEditTextEmpty();
                            }
                        });
                        normalDialog.show();
                    }
                }
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
            list.add(photoUri);

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
                    Uri uri = list.get(count-2);
                    new File(uri.getPath()).delete();
                    Toast.makeText(requireContext(), "已删除图片"+uri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
                    //不再显示
                    imageView.setImageBitmap(null);
                    view.setVisibility(View.GONE);
                    count--;
                    if(preImageButton != null) preImageButton.setVisibility(View.VISIBLE);
                    if(count <= 5) {
                        photoBtn.setClickable(true);
                        photoBtn.setBackground(requireContext().getDrawable(R.drawable.conor_button_green));
                    }
                }
            });
            imageButton.setVisibility(View.VISIBLE);

            if(count >= 6) {
                photoBtn.setClickable(false);
                photoBtn.setBackground(requireContext().getDrawable(R.drawable.conor_button_grey));
            }

        }
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
                    for(MaterialInform materialInform : materialInforms) {
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
        for(int i = 1; i <= 5; i++ ) {
            ImageView imageView = getImageViewFromCount(i);
            imageView.setImageBitmap(null);
        }
        for(int i = 1; i <= 5; i++) {
            ImageButton imgBtn = getImageButtonFromCount(i);
            imgBtn.setVisibility(View.GONE);
        }
    }
}
