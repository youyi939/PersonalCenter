package com.example.personalcenter.personal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import com.example.personalcenter.R;
import com.example.personalcenter.personal.adapter.OrderAdapter;
import com.example.personalcenter.personal.pojo.Order;
import com.example.personalcenter.utils.KenUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private ListView orderListView;
    private SharedPreferences sharedPreferences;
    private List<Order> orderList = new ArrayList<>();
    private OrderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        actionBar = getSupportActionBar();
        actionBar.setTitle("订单列表");
        orderListView = findViewById(R.id.orderListView);
        sharedPreferences = getSharedPreferences("data",0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //此时userID为1，比赛时应该会有提供的userID
                    String url = "http://124.93.196.45:10002/userinfo/orders/list?pageNum=1&pageSize=30&userId=1";
                    String json = KenUtils.senGet_T(url,sharedPreferences.getString("token","123"));
                    Log.i("Ken", "run: "+json);
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String orderNum = object.getString("orderNum");
                        String createTime = object.getString("createTime");
                        orderList.add(new Order(orderNum,createTime));
                    }
                    handler.sendEmptyMessage(1);


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     *
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    adapter = new OrderAdapter(OrderListActivity.this,R.layout.order_item,orderList);
                    orderListView.setAdapter(adapter);
                    break;
                default:
                    break;
            }
        }
    };

}