package com.example.depository_system;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.depository_system.informs.UserInform;
import com.example.depository_system.service.UserService;
import com.example.depository_system.view.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    Button loginBtn;
    EditText username;
    EditText password;
    TextView registertext;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        context = this;

//        ImageAdapter.imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(this).build();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        registertext = findViewById(R.id.login_to_regist);

        loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(view->{
            String usernameText = username.getText().toString();
            String passwordText = password.getText().toString();
            if(login(usernameText, passwordText)) {
                Log.d("java", "login success");
                Intent intent = new Intent(this, MainInterface.class);
                startActivity(intent);
            } else {
                Log.d("java", "login failed");
            }
        });

        registertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean login(String username, String password) {
        if(username == null || password == null) {
            Toast.makeText(this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        DataManagement.updateUserInfo();
        String result = UserService.userLogin(username, password);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        if(result.contains("登录成功")) {
            DataManagement.updateAll();
            for(UserInform userInform : DataManagement.userInforms) {
                if(userInform.phoneNumber.equals(username)
                && userInform.password.equals(password)) {
                    DataManagement.userInform = userInform;
                }
            }
            return true;
        }
        return false;
    }
}