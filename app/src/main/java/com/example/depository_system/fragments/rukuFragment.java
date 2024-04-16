package com.example.depository_system.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.depository_system.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class rukuFragment extends Fragment {

    private View root;

    private EditText depotNameEditText;

    private EditText materialIdentifierEditText;

    private EditText factoryNameEditText;

    private EditText timeEditText;

    private EditText projectNameEditText;

    private EditText receiverEditText;

    private EditText acceptorEditText;

    private Button photoBtn;

    private Button uploadBtn;

    private Uri photoUri;

    private static final int CAMERA_PERMISSIOS_REQUEST_CODE = 100;

    private ImageView imageView;

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

//        depotNameEditText = root.findViewById(R.id.depotName);
//        materialIdentifierEditText = root.findViewById(R.id.material_identifier);
//        factoryNameEditText = root.findViewById(R.id.factory_name);
//        timeEditText = root.findViewById(R.id.ruku_time);
//        projectNameEditText = root.findViewById(R.id.project_name);
//        receiverEditText = root.findViewById(R.id.receiver);
//        acceptorEditText = root.findViewById(R.id.acceptor);
//        imageView = root.findViewById(R.id.imageview_photo);
//
//        photoBtn = root.findViewById(R.id.photo);
//        uploadBtn = root.findViewById(R.id.upload);

//        photoBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                requestCameraPermission();
//            }
//        });
//
//        uploadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Todo 判断必选项
//                RukuInform rukuInform = new RukuInform();
//                rukuInform.depotName = depotNameEditText.getText().toString();
//                rukuInform.materialIdentifer = materialIdentifierEditText.getText().toString();
//                rukuInform.factoryName = factoryNameEditText.getText().toString();
//                rukuInform.time = timeEditText.getText().toString();
//                rukuInform.projectName = projectNameEditText.getText().toString();
//                rukuInform.receiver = receiverEditText.getText().toString();
//                rukuInform.acceptor = acceptorEditText.getText().toString();
//            }
//        });

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
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageURI(photoUri);
            photoBtn.setText("重拍");
        }
    }
}
