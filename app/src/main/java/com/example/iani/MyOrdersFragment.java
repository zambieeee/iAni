package com.example.iani;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class MyOrdersFragment extends Fragment {

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    private RecyclerView myOrdersRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_orders, container,false);

        myOrdersRecyclerView = view.findViewById(R.id.my_orders_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myOrdersRecyclerView.setLayoutManager(layoutManager);

        List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();
        myOrderItemModelList.add(new MyOrderItemModel(R.mipmap.potatoes,2,"Potatoes","Delivered on Mon, 16th NOV 2021"));
        myOrderItemModelList.add(new MyOrderItemModel(R.mipmap.banner,1,"Potatoes","Delivered on Mon, 16th NOV 2021"));
        myOrderItemModelList.add(new MyOrderItemModel(R.mipmap.potatoes,0,"Potatoes","Cancelled"));
        myOrderItemModelList.add(new MyOrderItemModel(R.mipmap.banner,4,"Potatoes","Delivered on Mon, 16th NOV 2021"));

        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(myOrderItemModelList);
        myOrdersRecyclerView.setAdapter(myOrderAdapter);
        myOrderAdapter.notifyDataSetChanged();
        
        return view;
    }

}