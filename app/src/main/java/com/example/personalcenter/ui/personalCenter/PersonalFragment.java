package com.example.personalcenter.ui.personalCenter;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.style.IconMarginSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.personalcenter.R;
import com.example.personalcenter.personal.PersonalInfoActivity;
import com.example.personalcenter.pojo.Personal;
import com.example.personalcenter.utils.KenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


// TODO: 4/1/21 退出登陆按钮，删除token，各种按钮不允许点击，头像换成默认图片，昵称位置显示未登录
public class PersonalFragment extends Fragment {

    private PersonalViewModel mViewModel;
    private ImageView avatar_img;
    private Personal personal;
    private TextView txt_nickName;


    private TextView personal_info_txt;             //个人中心
    private TextView txt_orderList;                 //订单列表
    private TextView txt_changePassword;            //修改密码
    private TextView txt_feedback;                  //意见反馈

    public static PersonalFragment newInstance() {
        return new PersonalFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.personal_fragment, container, false);
        personal_info_txt = root.findViewById(R.id.personalInfo_txt);
        avatar_img = root.findViewById(R.id.avatar_img);
        txt_nickName = root.findViewById(R.id.txt_nickName);
        txt_orderList = root.findViewById(R.id.txt_orderList);
        txt_changePassword = root.findViewById(R.id.txt_changePassword);

        personal_info_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return root;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    // TODO: 3/31/21 应该加入token登陆失败的情况判断
                    String t_json = KenUtils.sendPost("http://124.93.196.45:10002/login");
                    JSONObject jsonObject = new JSONObject(t_json);
                    int code = jsonObject.getInt("code");
                    if (code==200){
                        String token = jsonObject.getString("token");           //登陆成功后返回Token

                        String user_info_json = KenUtils.senGet_T("http://124.93.196.45:10002/getInfo",token);
                        JSONObject jsonObject1 = new JSONObject(user_info_json);
                        JSONObject object = jsonObject1.getJSONObject("user");
                        String userName = object.getString("userName");
                        String nickName = object.getString("nickName");
                        String phonenumber = object.getString("phonenumber");
                        int sex = object.getInt("sex");
                        String avatar = "http://124.93.196.45:10002"+object.getString("avatar");

                        Log.i("Ken", "run: "+avatar);
                        personal = new Personal(userName,nickName,phonenumber,sex,avatar);

                        //存储token
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",0).edit();
                        editor.putString("token",token);
                        editor.apply();

                        handler.sendEmptyMessage(1);
                    }else if (code == 500){
                        String msg = jsonObject.getString("msg");
                        Message message = new Message();
                        message.what = 2;
                        message.obj = msg;
                        handler.sendMessage(message);
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PersonalViewModel.class);
        // TODO: Use the ViewModel
    }


    /**
     * 1:登陆成功
     * 2:登陆失败，密码/账户名错误
     * 3:
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(getContext(),"登陆成功",Toast.LENGTH_SHORT).show();
                    Glide.with(getContext()).load(personal.getAvatar()).into(avatar_img);
                    txt_nickName.setText(personal.getNickName());
                    break;
                case 2:
                    Toast.makeText(getContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    break;
                default:
                    break;
            }
        }
    };



}