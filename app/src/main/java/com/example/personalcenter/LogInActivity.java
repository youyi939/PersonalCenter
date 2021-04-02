package com.example.personalcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.personalcenter.utils.KenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LogInActivity extends AppCompatActivity {


    private ActionBar actionBar;
    private Button login;
    private EditText userName_login;
    private EditText password_login;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        actionBar = getSupportActionBar();
        actionBar.setTitle("用户登陆");
        login = findViewById(R.id.login);
        userName_login = findViewById(R.id.userName_login);
        password_login = findViewById(R.id.password_login);

        editor = getSharedPreferences("data",0).edit();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(password_login.getText()) || TextUtils.isEmpty(userName_login.getText())){
                    Toast.makeText(LogInActivity.this,"输入不得为空！",Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String userName = userName_login.getText().toString();
                                String password = password_login.getText().toString();
                                JSONObject login_json = new JSONObject();
                                login_json.put("username",userName);
                                login_json.put("password",password);
                                String json = KenUtils.logIn("http://124.93.196.45:10002/login",login_json.toString());
                                JSONObject jsonObject = new JSONObject(json);
                                int code = jsonObject.getInt("code");
                                String token = jsonObject.getString("token");
                                if (code == 200){
                                    editor.putString("token",token);
                                    editor.putString("username",userName);
                                    editor.putString("password",password);
                                    editor.commit();

                                    Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                                    intent.putExtra("login",true);
                                    startActivity(intent);
                                }else if (code == 500){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LogInActivity.this,"登陆失败",Toast.LENGTH_SHORT).show();
                                        }
                                    });
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
}