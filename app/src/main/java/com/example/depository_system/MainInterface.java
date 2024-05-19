package com.example.depository_system;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.depository_system.fragments.*;
import com.example.depository_system.view.BatchChukuActivity;
import com.example.depository_system.view.BatchRukuActivity;
import com.example.depository_system.view.PersonActivity;

public class MainInterface extends AppCompatActivity {

    private View ruKuView;
    private View chaRukuView;
    private View chaChukuView;
    private View chuKuView;
    private View kuCunView;

    private Fragment rukuFragment;
    private Fragment chukuFragment;
    private Fragment charukuFragment;
    private Fragment chachukuFragment;
    private Fragment kucunFragment;

    private FragmentManager fragmentManager;

    private Fragment displayedFragment;
    private View displayedView;

    private Toolbar titleToolbar;

    private Button myButton;

    private Button kucunExportButton;

    private Button batchRukuButton;

    private Button batchChukuButton;

    private static final int CAMERA_PERMISSIOS_REQUEST_CODE = 100;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface);
        this.context = context;

        initUI();
    }

    private void initUI() {
        ruKuView = findViewById(R.id.ruku);
        chaRukuView = findViewById(R.id.charuku);
        chaChukuView = findViewById(R.id.chachuku);
        kuCunView = findViewById(R.id.kucun);
        chuKuView = findViewById(R.id.chuku);

        fragmentManager = getSupportFragmentManager();
        rukuFragment = fragmentManager.findFragmentById(R.id.rukuFragment);
        chukuFragment = fragmentManager.findFragmentById(R.id.chukuFragment);
        charukuFragment = fragmentManager.findFragmentById(R.id.charukuFragment);
        chachukuFragment = fragmentManager.findFragmentById(R.id.chachukuFragment);
        kucunFragment = fragmentManager.findFragmentById(R.id.kucunFragment);

        titleToolbar = findViewById(R.id.tb_base_title);

        myButton = findViewById(R.id.myself_btn);
        kucunExportButton = findViewById(R.id.kucun_export_btn);
        batchRukuButton = findViewById(R.id.batch_ruku);
        batchChukuButton = findViewById(R.id.batch_chuku);

        ruKuView.setOnClickListener(view -> {
            if(DataManagement.userInform.category >=2) {
                Toast.makeText(view.getContext(), "无权限，请联系管理员", Toast.LENGTH_SHORT).show();
                return;
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(displayedFragment);
            fragmentTransaction.show(rukuFragment);
            fragmentTransaction.commit();
            setBackGroundColor(displayedView, ruKuView);
            displayedFragment = rukuFragment;
            titleToolbar.setTitle("入库信息填写");
            kucunExportButton.setVisibility(View.GONE);
            batchRukuButton.setVisibility(View.VISIBLE);
            batchChukuButton.setVisibility(View.GONE);
        });
        chuKuView.setOnClickListener(view -> {
            if(DataManagement.userInform.category >= 2) {
                Toast.makeText(view.getContext(), "无权限，请联系管理员", Toast.LENGTH_SHORT).show();
                return;
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(displayedFragment);
            fragmentTransaction.show(chukuFragment);
            fragmentTransaction.commit();
            setBackGroundColor(displayedView, chuKuView);
            displayedFragment = chukuFragment;
            titleToolbar.setTitle("出库信息填写");
            kucunExportButton.setVisibility(View.GONE);
            batchRukuButton.setVisibility(View.GONE);
            batchChukuButton.setVisibility(View.VISIBLE);
        });
        chaRukuView.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(displayedFragment);
            fragmentTransaction.show(charukuFragment);
            fragmentTransaction.commit();
            setBackGroundColor(displayedView, chaRukuView);
            displayedFragment = charukuFragment;
            titleToolbar.setTitle("入库信息查询");
            kucunExportButton.setVisibility(View.GONE);
            batchRukuButton.setVisibility(View.GONE);
            batchChukuButton.setVisibility(View.GONE);
        });
        chaChukuView.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(displayedFragment);
            fragmentTransaction.show(chachukuFragment);
            fragmentTransaction.commit();
            setBackGroundColor(displayedView, chaChukuView);
            displayedFragment = chachukuFragment;
            titleToolbar.setTitle("出库信息查询");
            kucunExportButton.setVisibility(View.GONE);
            batchRukuButton.setVisibility(View.GONE);
            batchChukuButton.setVisibility(View.GONE);
        });
        kuCunView.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(displayedFragment);
            fragmentTransaction.show(kucunFragment);
            fragmentTransaction.commit();
            setBackGroundColor(displayedView, kuCunView);
            displayedFragment = kucunFragment;
            titleToolbar.setTitle("库存信息查询");
            kucunExportButton.setVisibility(View.VISIBLE);
            batchRukuButton.setVisibility(View.GONE);
            batchChukuButton.setVisibility(View.GONE);
        });

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(rukuFragment);
        fragmentTransaction.hide(chukuFragment);
        fragmentTransaction.hide(charukuFragment);
        fragmentTransaction.hide(chachukuFragment);
        fragmentTransaction.hide(kucunFragment);
        fragmentTransaction.show(kucunFragment);
        fragmentTransaction.commit();
        kuCunView.setBackgroundColor(Color.argb(255, 208, 208, 208));
        displayedFragment = kucunFragment;
        displayedView = kuCunView;
        titleToolbar.setTitle("库存信息查询");
        kucunExportButton.setVisibility(View.VISIBLE);
        myButton.setVisibility(View.VISIBLE);
        batchRukuButton.setVisibility(View.GONE);
        batchChukuButton.setVisibility(View.GONE);

        kucunExportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.example.depository_system.fragments.kucunFragment kucunFragment1 = (com.example.depository_system.fragments.kucunFragment) kucunFragment;
                Message msg = new Message();
                msg.obj = -3;
                kucunFragment1.handler.sendMessage(msg);
            }
        });

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("kevin", "mybutton clicked");
                Intent intent = new Intent(view.getContext(), PersonActivity.class);
                startActivity(intent);
            }
        });

        batchChukuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("kevin", "batchChukuButton clicked");
                Intent intent = new Intent(view.getContext(), BatchChukuActivity.class);
                startActivity(intent);
            }
        });

        batchRukuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("kevin", "batchRukuButton clicked");
                Intent intent = new Intent(view.getContext(), BatchRukuActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setBackGroundColor(View originView, View dstView) {
        originView.setBackgroundColor(Color.argb(255, 255,255,255));
        dstView.setBackgroundColor(Color.argb(255, 208,208,208));
        this.displayedView = dstView;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSIOS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                rukuFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            } else {

            }
        }
    }
}