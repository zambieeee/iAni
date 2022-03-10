package com.example.iani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;


public class AddAddressActivity extends AppCompatActivity {

    private EditText city;
    private EditText locality;
    private EditText unitNo;
    private EditText zipcode;
    private EditText landmark;
    private EditText name;
    private EditText mobileNo;
    private EditText alternateMobileNo;
    private Spinner provinceSpinner;
    private Button saveBtn;

    private String [] provinceList;
    private String selectedProvince;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add a new address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ///loading dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ///loading dialog

        provinceList = getResources().getStringArray(R.array.ph_provinces);

        city = findViewById(R.id.city);
        locality = findViewById(R.id.locality);
        unitNo = findViewById(R.id.unitnumber);
        zipcode = findViewById(R.id.zipcode);
        landmark = findViewById(R.id.landmark);
        name = findViewById(R.id.name);
        mobileNo = findViewById(R.id.mobile_no);
        alternateMobileNo = findViewById(R.id.alternate_mobile_no);
        provinceSpinner = findViewById(R.id.province_spinner);
        saveBtn = findViewById(R.id.save_btn);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,provinceList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        provinceSpinner.setAdapter(spinnerAdapter);

        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProvince = provinceList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if(!TextUtils.isEmpty(city.getText())){
                    if(!TextUtils.isEmpty(locality.getText())){
                        if(!TextUtils.isEmpty(unitNo.getText())){
                            if(!TextUtils.isEmpty(zipcode.getText()) && zipcode.getText().length() == 4){
                                if(!TextUtils.isEmpty(name.getText())){
                                    if(!TextUtils.isEmpty(mobileNo.getText()) && mobileNo.getText().length() == 11){

                                        loadingDialog.show();

                                        String fullAddress = unitNo.getText().toString()+" "+locality.getText().toString()+" "+landmark.getText().toString() + " " + city.getText().toString()+ " " + selectedProvince;

                                        Map<String, Object> addAddress = new HashMap();
                                        addAddress.put("list_size",(long)DBqueries.addressesModelList.size()+1);
                                        if(TextUtils.isEmpty(alternateMobileNo.getText())) {
                                            addAddress.put("fullname_" + String.valueOf((long) DBqueries.addressesModelList.size() + 1), name.getText().toString() + " - " + mobileNo.getText().toString());
                                        }else {
                                            addAddress.put("fullname_" + String.valueOf((long) DBqueries.addressesModelList.size() + 1), name.getText().toString() + " - " + mobileNo.getText().toString() + " or " + alternateMobileNo.getText().toString());
                                        }
                                        addAddress.put("address_"+ String.valueOf((long)DBqueries.addressesModelList.size()+1), fullAddress);
                                        addAddress.put("zipcode_"+ String.valueOf((long)DBqueries.addressesModelList.size()+1), zipcode.getText().toString());
                                        addAddress.put("selected_"+ String.valueOf((long)DBqueries.addressesModelList.size()+1), true);
                                        if (DBqueries.addressesModelList.size()>0) {
                                            addAddress.put("selected_" + (DBqueries.selectAddress + 1), false);
                                        }

                                        FirebaseFirestore.getInstance().collection("USERS")
                                                .document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                                .document("MY_ADDRESSES")
                                                .update(addAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    if (DBqueries.addressesModelList.size()>0) {
                                                        DBqueries.addressesModelList.get(DBqueries.selectAddress).setSelected(false);
                                                    }
                                                    if (TextUtils.isEmpty(alternateMobileNo.getText())) {
                                                        DBqueries.addressesModelList.add(new AddressesModel(name.getText().toString() + " - " + mobileNo.getText().toString(), fullAddress, zipcode.getText().toString(), true));
                                                    }else {
                                                        DBqueries.addressesModelList.add(new AddressesModel(name.getText().toString() + " - " + mobileNo.getText().toString()+ " or "+alternateMobileNo.getText().toString(), fullAddress, zipcode.getText().toString(), true));
                                                    }
                                                    if (getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                                        Intent deliveryIntent = new Intent(AddAddressActivity.this, DeliveryActivity.class);
                                                        startActivity(deliveryIntent);
                                                    }else{
                                                        MyAddressesActivity.refreshItem(DBqueries.selectAddress, DBqueries.addressesModelList.size()-1);
                                                    }
                                                    DBqueries.selectAddress = DBqueries.addressesModelList.size() - 1;
                                                    finish();
                                                }else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(AddAddressActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                                loadingDialog.dismiss();
                                            }
                                        });
                                    }else{
                                        mobileNo.requestFocus();
                                        Toast.makeText(AddAddressActivity.this, "Please provide valid mobile number.", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    name.requestFocus();
                                }
                            }else {
                                zipcode.requestFocus();
                                Toast.makeText(AddAddressActivity.this, "Please provide valid zipcode", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            unitNo.requestFocus();
                        }
                    }else {
                        locality.requestFocus();
                    }
                }else {
                    city.requestFocus();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}