package com.example.iani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static com.example.iani.MainActivity.showCart;

public class ProductDetailsActivity extends AppCompatActivity {

    private ViewPager productImagesViewPager;
    private TabLayout viewpagerIndicator;

    private ViewPager productDetailsViewPager;
    private TabLayout productDetailsTablayout;

    //rating layout
    private LinearLayout rateContainer;
    //rating layout

    private Button buyNowBtn;

    private static boolean LIKED = false;
    private FloatingActionButton likedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImagesViewPager=findViewById(R.id.product_images_viewpager);
        viewpagerIndicator=findViewById(R.id.viewpager_indicator);
        likedBtn = findViewById(R.id.add_to_wishlist_btn);
        productDetailsViewPager = findViewById(R.id.product_details_viewpager);
        productDetailsTablayout=findViewById(R.id.product_details_tablayout);
        buyNowBtn = findViewById(R.id.buy_now_btn);

        List<Integer> productImages = new ArrayList<>();
        productImages.add(R.mipmap.potatoes);
        productImages.add(R.mipmap.vegetable);
        productImages.add(R.mipmap.banner);
        productImages.add(R.mipmap.banner1);

        ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
        productImagesViewPager.setAdapter(productImagesAdapter);

        viewpagerIndicator.setupWithViewPager(productImagesViewPager, true);

        likedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LIKED){
                    LIKED=false;
                    likedBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                }else {
                    LIKED=true;
                    likedBtn.setSupportImageTintList(getResources().getColorStateList(R.color.Red));
                }
            }
        });

        productDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(),productDetailsTablayout.getTabCount()));

        productDetailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTablayout));
        productDetailsTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //rating layout
        rateContainer = findViewById(R.id.rate_now_container);
        for (int x = 0;x<rateContainer.getChildCount();x++){
            final int starPosition = x;
            rateContainer.getChildAt(x).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    setRating(starPosition);
                }
            });
        }
        //rating layout

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent deliveryIntent = new Intent(ProductDetailsActivity.this,DeliveryActivity.class);
                startActivity(deliveryIntent);
            }
        });
    }

    private void setRating(int starPosition) {
        for (int x=0;x<rateContainer.getChildCount();x++){
            ImageView starBtn = (ImageView)rateContainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x<=starPosition){
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#65bc60")));
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if(id==android.R.id.home){
            //todo:search
            finish();
            return true;
        }else if (id==R.id.main_search_icon){
            //todo:notification
            return true;
        }else if (id==R.id.main_cart_icon){
            Intent cartIntent = new Intent(ProductDetailsActivity.this,MainActivity.class);
            showCart = true;
            startActivity(cartIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}