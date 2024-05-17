package com.example.depository_system.view;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.depository_system.R;
import com.example.depository_system.informs.MaterialInform;
import com.example.depository_system.informs.UserInform;
import com.example.depository_system.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class PersonActivity extends AppCompatActivity {

    private Button reviewBtn;
    private Button logoutBtn;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        reviewBtn = findViewById(R.id.my_review_btn);
        logoutBtn = findViewById(R.id.my_logout_btn);
        backButton = findViewById(R.id.my_back_btn);

        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<UserInform> userInformList = UserService.getNewUserList();
                List<String> list = new ArrayList<>();
                for(UserInform userInform : userInformList) {
                    list.add(userInform.userName + ": " + userInform.phoneNumber);
                }
                showListPopupWindow(list.toArray(new String[list.size()]), reviewBtn, userInformList);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showListPopupWindow(String[] list, Button button, List<UserInform> userInformList) {
        Log.d("java", "showpopupwindow");
        ListPopupWindow listPopupWindow= new ListPopupWindow(this);
        listPopupWindow.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, list));
        listPopupWindow.setAnchorView(button);
        listPopupWindow.setModal(true);
        listPopupWindow.setHeight(10*20*3);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("kevin", "item" + i + "clicked");
                new MaterialDialog.Builder(view.getContext())
                        .content("确定通过审核吗：" + list[i])
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                UserInform userInform = userInformList.get(i);
                                String result = UserService.userInfoModify(userInform.phoneNumber, userInform.userName, userInform.password, ""+2);
                                if(result.contains("修改成功")) {
                                    new MaterialDialog.Builder(view.getContext())
                                            .content("审核通过")
                                            .positiveText("确定")
                                            .show();
                                }
                            }
                        }).show();

                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.show();
    }
}