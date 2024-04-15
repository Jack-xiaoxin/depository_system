package com.example.depository_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.depository_system.service.TestClass;

public class MainActivity extends AppCompatActivity {

    Button loginBtn;
    EditText username;
    EditText password;
    Button testBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

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

        testBtn = findViewById(R.id.test_btn);
        testBtn.setOnClickListener(view->{
            String usernameText = username.getText().toString();
            String passwordText = password.getText().toString();
            testRequest(usernameText, passwordText);
        });
    }

    private boolean login(String username, String password) {
        if(username == null || password == null || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if(username.equals("test") && password.equals("test")) {
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "用户名或密码不对", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean testRequest(String username, String password) {

        TestClass tc = new TestClass();
        tc.test(username);
        return true;
    }
}