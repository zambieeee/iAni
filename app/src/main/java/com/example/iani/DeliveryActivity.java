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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DeliveryActivity extends AppCompatActivity {

    public static List<CartItemModel> cartItemModelList;
    private RecyclerView deliveryRecycerView;
    private Button changeORaddNewAddressBtn;
    public static final int SELECT_ADDRESS=0;
    private TextView totalAmount;
    private TextView fullname;
    private TextView fullAddress;
    private TextView zipcode;


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
        totalAmount = findViewById(R.id.total_cart_amount);
        fullname = findViewById(R.id.shipping_fullname);
        fullAddress = findViewById(R.id.shipping_address);
        zipcode = findViewById(R.id.shipping_zipcode);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecycerView.setLayoutManager(layoutManager);


        CartAdapter cartAdapter = new CartAdapter(cartItemModelList,totalAmount, false);
        deliveryRecycerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        changeORaddNewAddressBtn.setVisibility(View.VISIBLE);
        changeORaddNewAddressBtn.setOnClickListener(v -> {
            Intent myAddressesIntent = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
            myAddressesIntent.putExtra("MODE", SELECT_ADDRESS);
            startActivity(myAddressesIntent);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        fullname.setText(DBqueries.addressesModelList.get(DBqueries.selectAddress).getFullname());
        fullAddress.setText(DBqueries.addressesModelList.get(DBqueries.selectAddress).getAddress());
        zipcode.setText(DBqueries.addressesModelList.get(DBqueries.selectAddress).getZipcode());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}