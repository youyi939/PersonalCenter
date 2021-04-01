package com.example.personalcenter.personal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.personalcenter.R;
import com.example.personalcenter.utils.KenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActionBar actionBar;

    private EditText old_Password;
    private EditText new_Password;
    private Button changePassword;
    private SharedPreferences sharedPreferences ;
    private SharedPreferences.Editor editor;

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
        editor = getSharedPreferences("data",0).edit();


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(old_Password.getText()) || TextUtils.isEmpty(new_Password.getText())){
                    Toast.makeText(ChangePasswordActivity.this,"输入不得为空",Toast.LENGTH_SHORT).show();
                }else if (!old_Password.getText().toString().equals(sharedPreferences.getString("password", "123"))){
                    Toast.makeText(ChangePasswordActivity.this,"原密码错误，请重新输入",Toast.LENGTH_SHORT).show();
                }else {
                    //发请求，修改密码
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String token = sharedPreferences.getString("token","");
                                JSONObject json_password = new JSONObject();
                                json_password.put("userId",32213);
                                json_password.put("oldPwd",Integer.parseInt(old_Password.getText().toString()));
                                json_password.put("password",Integer.parseInt(new_Password.getText().toString()));

                                String json = KenUtils.changePassword(token,json_password.toString());
                                JSONObject jsonObject = new JSONObject(json);
                                int code = jsonObject.getInt("code");
                                if (code == 200){
                                    editor.putString("password",new_Password.getText().toString());
                                    handler.sendEmptyMessage(1);
                                }else {
                                    handler.sendEmptyMessage(2);
                                }


                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

            }
        });

    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(ChangePasswordActivity.this,"密码修改成功",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(ChangePasswordActivity.this,"密码修改失败",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };



}









