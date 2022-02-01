package com.example.iani;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import static com.example.iani.DeliveryActivity.SELECT_ADDRESS;

public class MyAddressesActivity extends AppCompatActivity {

    private RecyclerView myAddressesRecyclerView;
    private Button deliverHereBtn;
    private static AddressesAdapter addressesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Addresses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myAddressesRecyclerView = findViewById(R.id.addresses_recycler_view);
        deliverHereBtn = findViewById(R.id.deliver_here_btn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myAddressesRecyclerView.setLayoutManager(layoutManager);

        List<AddressesModel> addressesModelList = new ArrayList<>();
        addressesModelList.add(new AddressesModel("Zam David", "Landayan, San Pedro, Laguna", "4023",true));
        addressesModelList.add(new AddressesModel("Vencer Roncales", "Landayan, San Pedro, Laguna", "4023",false));
        addressesModelList.add(new AddressesModel("Josef Varquez", "Pacita Complex, San Pedro, Laguna", "4023",false));
        addressesModelList.add(new AddressesModel("Jewel Angeles", "Pacita Complex, San Pedro, Laguna", "4023",false));
        addressesModelList.add(new AddressesModel("Phil Adriel", "Pacita Complex, San Pedro, Laguna", "4023",false));
        addressesModelList.add(new AddressesModel("Lou Diamond", "Taguig City", "3069",false ));
        addressesModelList.add(new AddressesModel("Jose Arboleda", "Taguig City", "6930",false));

        int mode = getIntent().getIntExtra("MODE", -1);
        if(mode==SELECT_ADDRESS){
            deliverHereBtn.setVisibility(View.VISIBLE);
        }else {
            deliverHereBtn.setVisibility(View.GONE);
        }
        addressesAdapter = new AddressesAdapter(addressesModelList,mode);
        myAddressesRecyclerView.setAdapter(addressesAdapter);
        ((SimpleItemAnimator)myAddressesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        addressesAdapter.notifyDataSetChanged();
    }

    public static void refreshItem(int deselect, int select) {
        addressesAdapter.notifyItemChanged(deselect);
        addressesAdapter.notifyItemChanged(select);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}