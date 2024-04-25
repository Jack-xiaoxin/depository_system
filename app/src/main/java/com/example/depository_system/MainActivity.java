package com.example.depository_system;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button loginBtn;
    EditText username;
    EditText password;

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
    }

    private boolean login(String username, String password) {
        if(username == null || password == null) {
            Toast.makeText(this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if((username.equals("test") && password.equals("test")) || (username.equals("") && password.equals(""))) {
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            DataManagement.updateAll();
            return true;
        } else {
            Toast.makeText(this, "用户名或密码不对", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}