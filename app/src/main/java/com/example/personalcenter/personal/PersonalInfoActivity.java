package com.example.personalcenter.personal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.personalcenter.R;

public class PersonalInfoActivity extends AppCompatActivity {

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        actionBar = getSupportActionBar();
        actionBar.setTitle("个人信息详情");


        SharedPreferences editor = getSharedPreferences("data",0);
        String token = editor.getString("token","失败");
        Toast.makeText(PersonalInfoActivity.this,token,Toast.LENGTH_SHORT).show();


    }
}