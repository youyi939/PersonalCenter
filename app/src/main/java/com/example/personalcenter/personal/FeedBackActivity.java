package com.example.personalcenter.personal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.personalcenter.R;
import com.example.personalcenter.utils.KenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FeedBackActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private Button submit_feedback;
    private EditText edit_feedBack;
    private SharedPreferences sharedPreferences;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        actionBar = getSupportActionBar();
        actionBar.setTitle("意见反馈");
        submit_feedback = findViewById(R.id.submit_feedback);
        edit_feedBack = findViewById(R.id.edit_feedBack);
        sharedPreferences = getSharedPreferences("data",0);

        submit_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edit_feedBack.getText())){
                    Toast.makeText(FeedBackActivity.this,"输入不得为空！",Toast.LENGTH_SHORT).show();
                }else {
                    // TODO: 4/1/21 提交意见反馈内容
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String token = sharedPreferences.getString("token","");
                                JSONObject msg = new JSONObject();
                                msg.put("content",edit_feedBack.getText().toString());
                                msg.put("userId","32213");
                                String json = KenUtils.postFeedBack(token,msg.toString());
                                JSONObject jsonObject = new JSONObject(json);
                                int code = jsonObject.getInt("code");
                                if (code == 200){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(FeedBackActivity.this,"新增意见反馈成功",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(FeedBackActivity.this,"新增意见反馈失败",Toast.LENGTH_SHORT).show();
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