package com.example.personalcenter.personal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.personalcenter.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActionBar actionBar;

    private EditText old_Password;
    private EditText new_Password;
    private Button changePassword;
    private SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        actionBar = getSupportActionBar();
        actionBar.setTitle("修改密码");
        old_Password = findViewById(R.id.old_Password);
        new_Password = findViewById(R.id.newPassword);
        changePassword = findViewById(R.id.changePassword);
        sharedPreferences = getSharedPreferences("data",0);


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(old_Password.getText()) || TextUtils.isEmpty(new_Password.getText())){
                    Toast.makeText(ChangePasswordActivity.this,"输入不得为空",Toast.LENGTH_SHORT).show();
                }else if (!old_Password.getText().toString().equals(sharedPreferences.getString("password", "123"))){
                    Toast.makeText(ChangePasswordActivity.this,"原密码错误，请重新输入",Toast.LENGTH_SHORT).show();
                }




            }
        });

    }



}









