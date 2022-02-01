package com.example.iani;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyCartFragment extends Fragment {


    public MyCartFragment() {
        // Required empty public constructor
    }

    private RecyclerView cartItemsRecyclerView;
    private Button continueBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        cartItemsRecyclerView=view.findViewById(R.id.cart_items_recyclerview);
        continueBtn=view.findViewById(R.id.card_continue_btn);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(layoutManager);

        List<CartItemModel> cartItemModelList=new ArrayList<>();

        cartItemModelList.add(new CartItemModel(0,R.mipmap.potatoes, "Potatoes", "PHP 150.00", 1 ));
        cartItemModelList.add(new CartItemModel(0,R.mipmap.potatoes, "Potatoes", "PHP 150.00", 1 ));
        cartItemModelList.add(new CartItemModel(0,R.mipmap.potatoes, "Potatoes", "PHP 150.00", 1 ));
        cartItemModelList.add(new CartItemModel(1, "Price (3 items)","PHP 450.00","PHP 20.00","PHP 270.00"));

        CartAdapter cartAdapter = new CartAdapter(cartItemModelList);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        continueBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent deliveryIntent = new Intent(getContext(),AddAddressActivity.class);
                getContext().startActivity(deliveryIntent);
            }
        });
        return view;
    }
}