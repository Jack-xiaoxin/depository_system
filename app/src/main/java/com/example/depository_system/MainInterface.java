package com.example.depository_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.depository_system.fragments.rukuFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface);

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

        ruKuView.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(displayedFragment);
            fragmentTransaction.show(rukuFragment);
            fragmentTransaction.commit();
            setBackGroundColor(displayedView, ruKuView);
            displayedFragment = rukuFragment;
        });
        chuKuView.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(displayedFragment);
            fragmentTransaction.show(chukuFragment);
            fragmentTransaction.commit();
            setBackGroundColor(displayedView, chuKuView);
            displayedFragment = chukuFragment;
        });
        chaRukuView.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(displayedFragment);
            fragmentTransaction.show(charukuFragment);
            fragmentTransaction.commit();
            setBackGroundColor(displayedView, chaRukuView);
            displayedFragment = charukuFragment;
        });
        chaChukuView.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(displayedFragment);
            fragmentTransaction.show(chachukuFragment);
            fragmentTransaction.commit();
            setBackGroundColor(displayedView, chaChukuView);
            displayedFragment = chachukuFragment;
        });
        kuCunView.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(displayedFragment);
            fragmentTransaction.show(kucunFragment);
            fragmentTransaction.commit();
            setBackGroundColor(displayedView, kuCunView);
            displayedFragment = kucunFragment;
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
    }

    private void setBackGroundColor(View originView, View dstView) {
        originView.setBackgroundColor(Color.argb(255, 255,255,255));
        dstView.setBackgroundColor(Color.argb(255, 208,208,208));
        this.displayedView = dstView;
    }
}