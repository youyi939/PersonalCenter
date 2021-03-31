package com.example.personalcenter.ui.personalCenter;

import androidx.lifecycle.ViewModelProvider;

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
import android.widget.Toast;

import com.example.personalcenter.R;
import com.example.personalcenter.utils.KenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class PersonalFragment extends Fragment {

    private PersonalViewModel mViewModel;

    public static PersonalFragment newInstance() {
        return new PersonalFragment();
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
                        Log.i("Ken", "run: "+token);

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.personal_fragment, container, false);

        return root;
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