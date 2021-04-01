package com.example.personalcenter.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.personalcenter.R;
import com.example.personalcenter.personal.pojo.Order;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<Order> {

    private int resourceId;

    public OrderAdapter(@NonNull Context context, int resource, @NonNull List<Order> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        }
        Order order = getItem(position);
        TextView orderNum = convertView.findViewById(R.id.order_num);
        TextView createTime = convertView.findViewById(R.id.order_createTime);

        orderNum.setText("订单号："+order.getOrderNum());
        createTime.setText("创建日期："+order.getCreateTime());

        return convertView;
    }
}
