package com.example.iani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class DeliveryActivity extends AppCompatActivity {

    private RecyclerView deliveryRecycerView;
    private Button changeORaddNewAddressBtn;
    public static final int SELECT_ADDRESS=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");

        deliveryRecycerView=findViewById(R.id.delivery_recyclerview);
        changeORaddNewAddressBtn=findViewById(R.id.change_or_add_address_btn);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecycerView.setLayoutManager(layoutManager);

        List<CartItemModel> cartItemModelList=new ArrayList<>();

        cartItemModelList.add(new CartItemModel(0,R.mipmap.potatoes, "Potatoes", "PHP 150.00", 1 ));
        cartItemModelList.add(new CartItemModel(0,R.mipmap.potatoes, "Potatoes", "PHP 150.00", 1 ));
        cartItemModelList.add(new CartItemModel(0,R.mipmap.potatoes, "Potatoes", "PHP 150.00", 1 ));
        cartItemModelList.add(new CartItemModel(1, "Price (3 items)","PHP 450.00","PHP 20.00","PHP 270.00"));

        CartAdapter cartAdapter = new CartAdapter(cartItemModelList);
        deliveryRecycerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        changeORaddNewAddressBtn.setVisibility(View.VISIBLE);
        changeORaddNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myAddressesIntent = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
                myAddressesIntent.putExtra("MODE", SELECT_ADDRESS);
                startActivity(myAddressesIntent);
            }
        });
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}