package com.example.depository_system.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.example.depository_system.DataManagement;
import com.example.depository_system.R;
import com.example.depository_system.informs.UserInform;
import com.example.depository_system.service.UserService;

import org.apache.commons.codec.digest.Md5Crypt;

public class RegisterActivity extends AppCompatActivity {

    private Button registerBtn;

    private TextView loginText;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordSecendEditText;
    private EditText realNameEditText;
    private String username = "";
    private String password = "";
    private String passwordSecend = "";
    private String realName;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;

        registerBtn = findViewById(R.id.regist_btn_register);
        loginText = findViewById(R.id.register_goto_login);

        usernameEditText = findViewById(R.id.regist_username);
        passwordEditText = findViewById(R.id.regist_password);
        passwordSecendEditText = findViewById(R.id.regist_repassword);
        realNameEditText = findViewById(R.id.regist_realName);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                passwordSecend = passwordSecendEditText.getText().toString();
                realName = realNameEditText.getText().toString();

                if(username.isEmpty()) {
                    showToast("用户名不能为空");
                    usernameEditText.requestFocus();
                    return;
                }
                if(password.isEmpty()) {
                    showToast("密码不能为空");
                    passwordEditText.requestFocus();
                    return;
                }
                if(passwordSecend.isEmpty()) {
                    showToast("请再次输入密码");
                    return;
                }
                if(!password.equals(passwordSecend)) {
                    showToast("两次不一样，请重新输入");
                    return;
                }
                if(realName.isEmpty()) {
                    showToast("请输入姓名");
                    return;
                }
                String encryptPassword = MD5Encrypt32(password);
                String result = UserService.userRegister(realName, username, password);
                showToast(result);
                Log.d("kevin", result);
                if(result.contains("注册成功")) {
                    setEmptyEditText();
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public String MD5Encrypt32(String password)
    {
        try {
            // 创建 MessageDigest 实例并指定使用 MD5 算法
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 将密码转换为字节数组
            byte[] passwordBytes = password.getBytes();

            // 使用 MessageDigest 更新字节数组
            md.update(passwordBytes);

            // 执行哈希计算并获取结果
            byte[] hashBytes = md.digest();

            // 将哈希结果转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            // 返回加密后的密码
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }


    private void setEmptyEditText() {
        usernameEditText.setText("");
        passwordEditText.setText("");
        passwordSecendEditText.setText("");
        realNameEditText.setText("");
    }
}