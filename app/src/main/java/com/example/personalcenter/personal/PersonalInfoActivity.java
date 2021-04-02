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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.personalcenter.R;
import com.example.personalcenter.pojo.Personal;
import com.example.personalcenter.utils.KenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class PersonalInfoActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private Personal personal;
    private ImageView img_personalInfo;

    private EditText nikeName_personalInfo;
    private EditText phoneNumber_personalInfo;
    private EditText email_personalInfo;
    private TextView idCard_personalInfo;
    private RadioGroup radioGroup_Personal;
    private RadioButton mail;
    private RadioButton fmail;
    private Button change_personalInfo;

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
        setContentView(R.layout.activity_personal_info);
        actionBar = getSupportActionBar();
        actionBar.setTitle("个人信息详情");
        img_personalInfo = findViewById(R.id.img_personalInfo);
        email_personalInfo = findViewById(R.id.email_personalInfo);
        nikeName_personalInfo = findViewById(R.id.nikeName_personalInfo);
        phoneNumber_personalInfo = findViewById(R.id.phoneNumber_personalInfo);
        idCard_personalInfo = findViewById(R.id.idCard_personalInfo);
        radioGroup_Personal = findViewById(R.id.radioGroup_Personal);
        mail = findViewById(R.id.mail_personal);
        fmail = findViewById(R.id.fmail_personal);
        change_personalInfo = findViewById(R.id.change_personalInfo);

        change_personalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //判空
                if (TextUtils.isEmpty(nikeName_personalInfo.getText()) || TextUtils.isEmpty(phoneNumber_personalInfo.getText()) || TextUtils.isEmpty(email_personalInfo.getText())) {
                    Toast.makeText(PersonalInfoActivity.this, "输入不得为空！", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                //获取修改后的信息
                                String nikeName = nikeName_personalInfo.getText().toString();
                                String phoneNumber = phoneNumber_personalInfo.getText().toString();
                                String email = email_personalInfo.getText().toString();
                                int sex = 0;
                                if (mail.isChecked()) {
                                    sex = 0;
                                } else if (fmail.isChecked()) {
                                    sex = 1;
                                }


                                //发请求修改个人信息
                                SharedPreferences editor = getSharedPreferences("data", 0);
                                String token = editor.getString("token", "失败");
                                String url = "http://124.93.196.45:10002/system/user/updata?userId=32213&nickName=" + nikeName + "&email=" + email + "&phonenumber=" + phoneNumber + "&sex=" + sex;
                                String json = KenUtils.ChangeUser(url, token);
                                Log.i("Ken", "run: " + json);


                                // TODO: 4/1/21 修改登陆
                                //重新获取token
                                JSONObject userObject = new JSONObject();
                                userObject.put("username","KenChen");
                                userObject.put("password",editor.getString("password","123"));
                                String t_json = KenUtils.logIn("http://124.93.196.45:10002/login",userObject.toString());
                                JSONObject jsonObject = new JSONObject(t_json);
                                int code = jsonObject.getInt("code");
                                if (code == 200) {
                                    String token_new = jsonObject.getString("token");           //登陆成功后返回Token
                                    //存储token
                                    SharedPreferences.Editor editor1 = getSharedPreferences("data", 0).edit();
                                    editor1.putString("token", token_new);
                                    editor1.apply();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(PersonalInfoActivity.this, "获取新Token成功", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else if (code == 500) {
                                    String msg = jsonObject.getString("msg");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(PersonalInfoActivity.this, "获取Token失败 " + msg, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                }
            }
        });


        //请求并解析个人详情信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences editor = getSharedPreferences("data", 0);
                String token = editor.getString("token", "失败");
                try {
                    String user_info_json = KenUtils.senGet_T("http://124.93.196.45:10002/getInfo", token);
                    JSONObject jsonObject1 = new JSONObject(user_info_json);
                    JSONObject object = jsonObject1.getJSONObject("user");
                    String userName = object.getString("userName");
                    String nickName = object.getString("nickName");
                    String phonenumber = object.getString("phonenumber");
                    String idCard = object.getString("idCard");
                    String email = object.getString("email");
                    int sex = object.getInt("sex");
                    String avatar = "http://124.93.196.45:10002" + object.getString("avatar");
                    personal = new Personal(userName, nickName, phonenumber, sex, avatar, idCard, email);

                    handler.sendEmptyMessage(1);

                } catch (SocketTimeoutException e1) {
                    e1.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PersonalInfoActivity.this, "登陆超时，请检查网络设置", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (SocketException e2) {
                    e2.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PersonalInfoActivity.this, "没有网络", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    /**
     * 1:请求个人详情完毕，渲染页面
     * 2:
     * 3:
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    Glide.with(PersonalInfoActivity.this).load(personal.getAvatar()).into(img_personalInfo);
                    nikeName_personalInfo.setText(personal.getNickName());
                    email_personalInfo.setText(personal.getEmail());
                    phoneNumber_personalInfo.setText(personal.getPhonenumber());

                    //idCard只显示前两位和后四位，其余用星号代替，进行字符串切割
                    String idCard2 = personal.getIdCard().substring(0, 2);
                    String length = personal.getIdCard();
                    String idCard4 = personal.getIdCard().substring(length.length() - 4, length.length());
                    idCard_personalInfo.setText(idCard2 + "*************" + idCard4);

//                    //设置一堆EditText不允许输入
//                    nikeName_personalInfo.setFocusable(false);
//                    phoneNumber_personalInfo.setFocusable(false);
//                    email_personalInfo.setFocusable(false);

                    //判断sex值，设置性别单选框
                    if (personal.getSex() == 0) {
                        mail.setChecked(true);
                        fmail.setChecked(false);
                    } else if (personal.getSex() == 1) {
                        fmail.setChecked(true);
                        mail.setChecked(false);
                    }
//                    //设置性别单选框不允许点击
//                    radioGroup_Personal.getChildAt(0).setEnabled(false);
//                    radioGroup_Personal.getChildAt(1).setEnabled(false);

                    break;
                case 2:
                    break;
                case 3:
                    break;
                default:
                    break;
            }
        }
    };


}