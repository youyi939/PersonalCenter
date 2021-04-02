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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.personalcenter.LogInActivity;
import com.example.personalcenter.R;
import com.example.personalcenter.personal.ChangePasswordActivity;
import com.example.personalcenter.personal.FeedBackActivity;
import com.example.personalcenter.personal.OrderListActivity;
import com.example.personalcenter.personal.PersonalInfoActivity;
import com.example.personalcenter.pojo.Personal;
import com.example.personalcenter.utils.KenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class PersonalFragment extends Fragment {

    private PersonalViewModel mViewModel;
    private ImageView avatar_img;
    private Personal personal;
    private TextView txt_nickName;

    private TextView personal_info_txt;             //个人中心
    private TextView txt_orderList;                 //订单列表
    private TextView txt_changePassword;            //修改密码
    private TextView txt_feedback;                  //意见反馈
    private Button logout;

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
        txt_feedback = root.findViewById(R.id.txt_feedback);
        logout = root.findViewById(R.id.logout);

        //个人信息点击跳转
        personal_info_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
                getActivity().startActivity(intent);
            }
        });

        //订单列表点击跳转
        txt_orderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OrderListActivity.class);
                getActivity().startActivity(intent);
            }
        });

        //修改密码点击跳转
        txt_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                getActivity().startActivity(intent);
            }
        });

        //意见反馈点击跳转
        txt_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FeedBackActivity.class);
                getActivity().startActivity(intent);
            }
        });


        //退出登陆点击事件
        // TODO: 4/1/21 删除存储的token，设置哪些跳转按钮禁止点击
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txt_nickName.setText("未登录");
                avatar_img.setImageResource(R.drawable.ic_launcher_background);
                txt_changePassword.setClickable(false);
                txt_feedback.setClickable(false);
                txt_orderList.setClickable(false);
                personal_info_txt.setClickable(false);

                //删除数据
                boolean clear = getActivity().getSharedPreferences("data",0).edit().clear().commit();
                Toast.makeText(getContext(),"退出登陆成功"+clear,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), LogInActivity.class);
                getActivity().startActivity(intent);
            }
        });


        return root;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i("Ken", "onStart: ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //存储password
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",0).edit();
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data",0);

                    if (sharedPreferences.getString("username", "k").equals("k") || sharedPreferences.getString("password","0").equals("0")){
                        Log.i("Ken", "run: 此时未登录");
                        Intent intent = new Intent(getActivity(),LogInActivity.class);
                        getActivity().startActivity(intent);
                    }

                    String username = sharedPreferences.getString("username","k");
                    String password = sharedPreferences.getString("password","0");

                    Log.i("Ken", "run: "+username+password);

                    JSONObject userObject = new JSONObject();
                    userObject.put("username",username);
                    userObject.put("password",password);
                    String json = userObject.toString();

                    String t_json = KenUtils.logIn("http://124.93.196.45:10002/login",json);
                    Log.i("Ken", "run: "+t_json);
                    JSONObject jsonObject = new JSONObject(t_json);
                    int code = jsonObject.getInt("code");
                    if (code==200){
                        String token = jsonObject.getString("token");           //登陆成功后返回Token
                        editor.putString("token",token);
                        editor.apply();
                        editor.commit();

                        String user_info_json = KenUtils.senGet_T("http://124.93.196.45:10002/getInfo",token);
                        JSONObject jsonObject1 = new JSONObject(user_info_json);
                        JSONObject object = jsonObject1.getJSONObject("user");
                        String userName = object.getString("userName");
                        String nickName = object.getString("nickName");
                        String phonenumber = object.getString("phonenumber");
                        String idCard = object.getString("idCard");
                        int sex = object.getInt("sex");
                        String avatar = "http://124.93.196.45:10002"+object.getString("avatar");
                        String email = object.getString("email");
                        Log.i("Ken", "run: "+avatar);
                        personal = new Personal(userName,nickName,phonenumber,sex,avatar,idCard,email);

                        handler.sendEmptyMessage(1);
                    }else if (code == 500){
                        String msg = jsonObject.getString("msg");
                        Message message = new Message();
                        message.what = 2;
                        message.obj = msg;
                        handler.sendMessage(message);
                    }


                } catch (SocketTimeoutException e1){
                    e1.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"登陆超时，请检查网络设置",Toast.LENGTH_SHORT).show();
                        }
                    });

                }catch (SocketException e2){
                    e2.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"没有网络",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                catch (IOException | JSONException e) {
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
                    txt_changePassword.setClickable(true);
                    txt_feedback.setClickable(true);
                    txt_orderList.setClickable(true);
                    personal_info_txt.setClickable(true);
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