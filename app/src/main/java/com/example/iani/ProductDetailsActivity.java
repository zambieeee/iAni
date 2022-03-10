package com.example.iani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.iani.MainActivity.showCart;
import static com.example.iani.RegisterActivity.setSignUpFragment;

public class ProductDetailsActivity extends AppCompatActivity {

    public static boolean running_wishlist_query = false;
    public static boolean running_rating_query = false;
    public static boolean running_cart_query = false;

    private ViewPager productImagesViewPager;
    private TextView productTitle;
    private TextView averageRatingMiniView;
    private TextView totalRatingMiniView;
    private TextView productPrice;
    private ImageView codIndicator;
    private TextView tvCodIndicator;
    private TabLayout viewpagerIndicator;

    // Product Description
    private ConstraintLayout productDetailsTabsContainer;
    private TabLayout productDetailsTablayout;
    private ViewPager productDetailsViewPager;

    private List<ProductOtherDetailsModel> productOtherDetailsModelList = new ArrayList<>();
    private String productDescription;
    // Product Description

    //rating layout
    public static int initialRating;
    public static LinearLayout rateContainer;
    private TextView totalRatings;
    private LinearLayout ratingsNoContainer;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBarContainer;
    private TextView averageRating;
    //rating layout

    private Button buyNowBtn;
    private LinearLayout addToCartBtn;
    public static MenuItem cartItem;

    public static boolean LIKED = false;
    public static boolean ALREADY_ADDED_TO_CART = false;
    public static FloatingActionButton likedBtn;

    private FirebaseFirestore firebaseFirestore;

    private Dialog signInDialog;
    private Dialog loadingDialog;
    private FirebaseUser currentUser;
    public static String productID;
    private TextView badgeCount;

    private DocumentSnapshot documentSnapshot;

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
        productTitle = findViewById(R.id.product_title);
        averageRatingMiniView = findViewById(R.id.tv_product_rating_miniview);
        totalRatingMiniView = findViewById(R.id.total_ratings_mini);
        productPrice = findViewById(R.id.product_price);
        tvCodIndicator = findViewById(R.id.tv_cod_indicator);
        codIndicator=findViewById(R.id.cod_indicator_imageview);
        productDetailsTabsContainer = findViewById(R.id.product_details_tabs_container);
        totalRatings = findViewById(R.id.total_ratings);
        ratingsNoContainer = findViewById(R.id.ratings_number_container);
        totalRatingsFigure = findViewById(R.id.total_ratings_figure);
        ratingsProgressBarContainer = findViewById(R.id.ratings_progressbar_container);
        averageRating = findViewById(R.id.average_rating);
        addToCartBtn = findViewById(R.id.add_to_cart_btn);

        initialRating = -1;

        ///loading dialog
        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        ///loading dialog

        firebaseFirestore = FirebaseFirestore.getInstance();

        List<String> productImages = new ArrayList<>();
        productID = getIntent().getStringExtra("PRODUCT_ID");
        firebaseFirestore.collection("PRODUCTS").document(productID)
                .get().addOnCompleteListener((OnCompleteListener<DocumentSnapshot>) task -> {
                    if(task.isSuccessful()){
                        documentSnapshot = task.getResult();

                        for(long x = 1;x < (long)documentSnapshot.get("no_of_product_images") + 1; x++) {
                            productImages.add(documentSnapshot.get("product_image_"+x).toString());
                        }
                        ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                        productImagesViewPager.setAdapter(productImagesAdapter);

                        productTitle.setText(documentSnapshot.get("product_title").toString());
                        averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
                        totalRatingMiniView.setText("("+(long)documentSnapshot.get("total_ratings")+") ratings");
                        productPrice.setText("PHP "+documentSnapshot.get("product_price").toString());
                        if((boolean)documentSnapshot.get("COD")){
                            codIndicator.setVisibility(View.VISIBLE);
                            tvCodIndicator.setVisibility(View.VISIBLE);
                        }else {
                            codIndicator.setVisibility(View.INVISIBLE);
                            tvCodIndicator.setVisibility(View.INVISIBLE);
                        }

                        if((boolean)documentSnapshot.get("use_tab_layout")){
                            productDetailsTabsContainer.setVisibility(View.VISIBLE);
                            productDescription = documentSnapshot.get("product_description").toString();

                            for (long x = 1;x<(long)documentSnapshot.get("total_other_title_fields")+1;x++){
                                productOtherDetailsModelList.add(new ProductOtherDetailsModel(1, documentSnapshot.get("other_title_"+x+"_field").toString(), documentSnapshot.get("other_title_"+x+"_value").toString()));
                            }

                        }else {
                            productDetailsTabsContainer.setVisibility(View.GONE);
                        }

                        totalRatings.setText((long)documentSnapshot.get("total_ratings")+" ratings");

                        for (int x = 0; x < 5 ; x++){
                            TextView rating = (TextView) ratingsNoContainer.getChildAt(x);
                            rating.setText(String.valueOf((long)documentSnapshot.get(5-x+"_star")));

                            ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                            int maxProgress = Integer.parseInt(String.valueOf((long)documentSnapshot.get("total_ratings")));
                            progressBar.setMax(maxProgress);
                            progressBar.setProgress(Integer.parseInt(String.valueOf((long)documentSnapshot.get((5-x)+"_star"))));
                        }
                        totalRatingsFigure.setText(String.valueOf((long)documentSnapshot.get("total_ratings")));
                        averageRating.setText(documentSnapshot.get("average_rating").toString());
                        productDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(),productDetailsTablayout.getTabCount(), productDescription, productOtherDetailsModelList));

                        if(currentUser !=null) {
                            if(DBqueries.myRating.size() == 0){
                                DBqueries.loadRatingList(ProductDetailsActivity.this);
                            }
                            if(DBqueries.cartList.size() == 0){
                                DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog, false,badgeCount, new TextView(ProductDetailsActivity.this));
                            }
                            if (DBqueries.bookmarks.size() == 0) {
                                DBqueries.loadBookmarks(ProductDetailsActivity.this, loadingDialog, false);
                            } else {
                                loadingDialog.dismiss();
                            }
                        }else {
                            loadingDialog.dismiss();
                        }


                        if(DBqueries.myRatedIds.contains(productID)){
                            int index = DBqueries.myRatedIds.indexOf(productID);
                            initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) - 1;
                            setRating(initialRating);
                        }

                        if(DBqueries.cartList.contains(productID)){
                            ALREADY_ADDED_TO_CART = true;
                        }else {
                            ALREADY_ADDED_TO_CART = false;
                        }

                        if(DBqueries.bookmarks.contains(productID)){
                            LIKED = true;
                            likedBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                        }else {
                            likedBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                            LIKED = false;
                        }
                        if((boolean)documentSnapshot.get("in_stock")){

                            addToCartBtn.setOnClickListener(v -> {
                                if(currentUser == null){
                                    signInDialog.show();
                                }else {
                                    if (!running_cart_query) {
                                        running_cart_query = true;
                                        if (ALREADY_ADDED_TO_CART) {
                                            running_cart_query = false;
                                            Toast.makeText(ProductDetailsActivity.this, "Added to cart!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Map<String, Object> addProduct = new HashMap<>();
                                            addProduct.put("product_ID_" + String.valueOf(DBqueries.cartList.size()), productID);
                                            addProduct.put("list_size", (long) (DBqueries.cartList.size() + 1));

                                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_CART")
                                                    .update(addProduct).addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {

                                                    if (DBqueries.cartItemModelList.size() != 0) {
                                                        DBqueries.cartItemModelList.add(0, new CartItemModel(CartItemModel.CART_ITEM, productID, documentSnapshot.get("product_image_1").toString()
                                                                , documentSnapshot.get("product_title").toString()
                                                                , documentSnapshot.get("product_price").toString()
                                                                , (long) 1
                                                                , (boolean) documentSnapshot.get("in_stock")));
                                                    }

                                                    ALREADY_ADDED_TO_CART = true;
                                                    DBqueries.cartList.add(productID);
                                                    Toast.makeText(ProductDetailsActivity.this, "Added to Cart Successfully!", Toast.LENGTH_SHORT).show();
                                                    invalidateOptionsMenu();
                                                    running_cart_query = false;
                                                }
                                                else {
                                                    running_cart_query =false;
                                                    String error = task1.getException().getMessage();
                                                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                }
                            });

                        }else {
                            buyNowBtn.setVisibility(View.GONE);
                            TextView outOfStock = (TextView) addToCartBtn.getChildAt(0);
                            outOfStock.setText("Out of Stock");
                            outOfStock.setTextColor(getResources().getColor(R.color.colorPrimary));
                            outOfStock.setCompoundDrawables(null, null, null, null);
                        }
                    }else {
                        loadingDialog.dismiss();
                        String error = task.getException().getMessage();
                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });

        viewpagerIndicator.setupWithViewPager(productImagesViewPager, true);

        likedBtn.setOnClickListener(v -> {
            if(currentUser == null){
                signInDialog.show();
            }else {
                //likedBtn.setEnabled(false);
                if (!running_wishlist_query) {
                    running_wishlist_query = true;
                    if (LIKED) {
                        int index = DBqueries.bookmarks.indexOf(productID);
                        DBqueries.removeFromBookmarks(index, ProductDetailsActivity.this);
                        likedBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                    } else {
                        likedBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                        Map<String, Object> addProduct = new HashMap<>();
                        addProduct.put("product_ID_" + String.valueOf(DBqueries.bookmarks.size()), productID);
                        addProduct.put("list_size", (long) (DBqueries.bookmarks.size() + 1));
                        firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_BOOKMARKS")
                                .update(addProduct).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                        if (DBqueries.bookmarkModelList.size() != 0) {
                                            DBqueries.bookmarkModelList.add(new BookmarkModel(productID, documentSnapshot.get("product_image_1").toString()
                                                    , documentSnapshot.get("product_full_title").toString()
                                                    , documentSnapshot.get("average_rating").toString()
                                                    , (long) documentSnapshot.get("total_ratings")
                                                    , documentSnapshot.get("product_price").toString()
                                                    , (boolean) documentSnapshot.get("COD")));
                                        }

                                        LIKED = true;
                                        likedBtn.setSupportImageTintList(getResources().getColorStateList(R.color.Red));
                                        DBqueries.bookmarks.add(productID);
                                        Toast.makeText(ProductDetailsActivity.this, "Added to bookmarks.", Toast.LENGTH_SHORT).show();

                            } else {
                                likedBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                String error = task.getException().getMessage();
                                Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                            running_wishlist_query =false;
                        });
                    }
                }
            }
        });

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
                    if(currentUser == null) {
                        signInDialog.show();
                    }else {
                        if (starPosition != initialRating){
                        if (!running_rating_query) {
                            running_rating_query = true;

                            setRating(starPosition);
                            Map<String, Object> updateRating = new HashMap<>();
                            if (DBqueries.myRatedIds.contains(productID)) {

                                TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);

                                updateRating.put(initialRating + 1 + "_star", Long.parseLong(oldRating.getText().toString()) - 1);
                                updateRating.put(starPosition + 1 + "_star", Long.parseLong(finalRating.getText().toString()) + 1);
                                updateRating.put("average_rating", calculateAverageRating((long) starPosition - initialRating, true));
                            } else {

                                updateRating.put(starPosition + 1 + "_star", (long) documentSnapshot.get(starPosition + 1 + "_star") + 1);
                                updateRating.put("average_rating", calculateAverageRating((long) starPosition + 1, false));
                                updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);

                                firebaseFirestore.collection("PRODUCTS").document(productID)
                                        .update(updateRating).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {

                                        Map<String, Object> myRating = new HashMap<>();
                                        if (DBqueries.myRatedIds.contains(productID)) {
                                            myRating.put("rating_" + DBqueries.myRatedIds.indexOf(productID), (long) starPosition + 1);
                                        } else {

                                            myRating.put("list_size", (long) DBqueries.myRatedIds.size() + 1);
                                            myRating.put("product_ID_" + DBqueries.myRatedIds.size(), productID);
                                            myRating.put("rating_" + DBqueries.myRatedIds.size(), (long) starPosition + 1);

                                        }

                                        firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                .update(myRating).addOnCompleteListener(task12 -> {
                                            if (task12.isSuccessful()) {

                                                if (DBqueries.myRatedIds.contains(productID)) {
                                                    DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(productID), (long) starPosition + 1);

                                                    TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                                    TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                    oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));
                                                    finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));
                                                } else {
                                                    DBqueries.myRatedIds.add(productID);
                                                    DBqueries.myRating.add((long) starPosition + 1);

                                                    TextView rating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                    rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));

                                                    totalRatingMiniView.setText("(" + ((long) documentSnapshot.get("total_ratings") + 1) + ") ratings");
                                                    totalRatings.setText((long) documentSnapshot.get("total_ratings") + 1 + " ratings");
                                                    totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));
                                                    Toast.makeText(ProductDetailsActivity.this, "Rated Successfully!", Toast.LENGTH_SHORT).show();
                                                }

                                                for (int x = 0; x < 5; x++) {
                                                    TextView ratingfigures = (TextView) ratingsNoContainer.getChildAt(x);

                                                    ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                                                    int maxProgress = Integer.parseInt(totalRatingsFigure.getText().toString());
                                                    progressBar.setMax(maxProgress);
                                                    progressBar.setProgress(Integer.parseInt(ratingfigures.getText().toString()));

                                                }
                                                initialRating = starPosition;
                                                averageRating.setText(calculateAverageRating(0, true));
                                                averageRatingMiniView.setText(calculateAverageRating(0, true));

                                                if (DBqueries.bookmarks.contains(productID) && DBqueries.bookmarkModelList.size() != 0) {
                                                    int index = DBqueries.bookmarks.indexOf(productID);
                                                    DBqueries.bookmarkModelList.get(index).setRating(averageRating.getText().toString());
                                                    DBqueries.bookmarkModelList.get(index).setTotalRatings(Long.parseLong(totalRatingsFigure.getText().toString()));
                                                }

                                            } else {
                                                setRating(initialRating);
                                                String error = task12.getException().getMessage();
                                                Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                            }
                                            running_rating_query = false;
                                        });

                                    } else {
                                        running_rating_query = false;
                                        setRating(initialRating);
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        }
                    }
                }
            });
        }
        //rating layout

        buyNowBtn.setOnClickListener(v -> {
            loadingDialog.show();
            if(currentUser == null){
                signInDialog.show();
            }else {
                DeliveryActivity.cartItemModelList.clear();
                DeliveryActivity.cartItemModelList = new ArrayList<>();
                DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.CART_ITEM, productID, documentSnapshot.get("product_image_1").toString()
                        , documentSnapshot.get("product_title").toString()
                        , documentSnapshot.get("product_price").toString()
                        , (long) 1
                        , (boolean) documentSnapshot.get("in_stock")));
                DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));

                if(DBqueries.addressesModelList.size() == 0) {
                    DBqueries.loadAddresses(ProductDetailsActivity.this, loadingDialog);
                }else{
                    loadingDialog.dismiss();
                    Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                    startActivity(deliveryIntent);
                }
            }
        });

        addToCartBtn.setOnClickListener(v -> {
            if(currentUser == null){
                signInDialog.show();
            }else {
                if (!running_cart_query) {
                    running_cart_query = true;
                    if (ALREADY_ADDED_TO_CART) {
                        running_cart_query = false;
                        Toast.makeText(ProductDetailsActivity.this, "Added to cart!", Toast.LENGTH_SHORT).show();
                    } else {
                        Map<String, Object> addProduct = new HashMap<>();
                        addProduct.put("product_ID_" + String.valueOf(DBqueries.cartList.size()), productID);
                        addProduct.put("list_size", (long) (DBqueries.cartList.size() + 1));

                        firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_CART")
                                .update(addProduct).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                        if (DBqueries.cartItemModelList.size() != 0) {
                                            DBqueries.cartItemModelList.add(new CartItemModel(CartItemModel.CART_ITEM, productID, documentSnapshot.get("product_image_1").toString()
                                                    , documentSnapshot.get("product_title").toString()
                                                    , documentSnapshot.get("product_price").toString()
                                                    , (long) 1
                                                    , (boolean) documentSnapshot.get("in_stock")));
                                        }

                                        ALREADY_ADDED_TO_CART = true;
                                        DBqueries.cartList.add(productID);
                                        Toast.makeText(ProductDetailsActivity.this, "Added to Cart Successfully!", Toast.LENGTH_SHORT).show();
                                        invalidateOptionsMenu();
                                        running_cart_query = false;
                                }
                             else {
                                running_cart_query =false;
                                String error = task.getException().getMessage();
                                Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

        /// sign dialog
        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.dialog_signin_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.dialog_signup_btn);
        Intent registerIntent = new Intent(ProductDetailsActivity.this, RegisterActivity.class);

        dialogSignInBtn.setOnClickListener(v -> {
            SignInFragment.disableCloseBtn=true;
            SignUpFragment.disableCloseBtn=true;
            signInDialog.dismiss();
            setSignUpFragment = false;
            startActivity(registerIntent);
        });

        dialogSignUpBtn.setOnClickListener(v -> {
            SignInFragment.disableCloseBtn=true;
            SignUpFragment.disableCloseBtn=true;
            signInDialog.dismiss();
            setSignUpFragment = true;
            startActivity(registerIntent);
        });
        /// sign dialog
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser !=null) {
            if(DBqueries.myRating.size() == 0){
                DBqueries.loadRatingList(ProductDetailsActivity.this);
            }

            if (DBqueries.bookmarks.size() == 0) {
                DBqueries.loadBookmarks(ProductDetailsActivity.this, loadingDialog,false);
            } else {
                loadingDialog.dismiss();
            }
        }else {
            loadingDialog.dismiss();
        }

        if(DBqueries.myRatedIds.contains(productID)){
            int index = DBqueries.myRatedIds.indexOf(productID);
            initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index)))-1;
            setRating(initialRating);
        }

        if(DBqueries.cartList.contains(productID)){
            ALREADY_ADDED_TO_CART = true;
        }else {
            ALREADY_ADDED_TO_CART = false;
        }

        if(DBqueries.bookmarks.contains(productID)){
            LIKED = true;
            likedBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
        }else {
            likedBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
            LIKED = false;
        }
        invalidateOptionsMenu();
    }

    public static void setRating(int starPosition) {
            for (int x = 0; x < rateContainer.getChildCount(); x++) {
                ImageView starBtn = (ImageView) rateContainer.getChildAt(x);
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
                if (x <= starPosition) {
                    starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#65bc60")));
                }
            }
    }

    private String calculateAverageRating(long currentUserRating, boolean update){
        Double totalStars = Double.valueOf(0);
        for (int x = 1;x < 6;x++){
            TextView ratingNo = (TextView) ratingsNoContainer.getChildAt(5 - x);
            totalStars = totalStars + (Long.parseLong(ratingNo.getText().toString())*x);
        }
        totalStars = totalStars + currentUserRating;

        if (update){
            return String.valueOf(totalStars / Long.parseLong(totalRatingsFigure.getText().toString())).substring(0, 3);
        }else {
            return String.valueOf(totalStars / (Long.parseLong(totalRatingsFigure.getText().toString()) + 1)).substring(0, 3);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);

        cartItem = menu.findItem(R.id.main_cart_icon);
            cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.drawable.cart);
            badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);

            if(currentUser !=null){
                if(DBqueries.cartList.size() == 0){
                    DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog, false, badgeCount, new TextView(ProductDetailsActivity.this));
                }else {
                    badgeCount.setVisibility(View.VISIBLE);
                    if(DBqueries.cartList.size()<99){
                        badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                    }else {
                        badgeCount.setText("99");
                    }
                }
            }
            cartItem.getActionView().setOnClickListener(v -> {
                if (currentUser == null) {
                    signInDialog.show();
                }else {
                    Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                    showCart = true;
                    startActivity(cartIntent);

                }
            });
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
            if (currentUser == null) {
                signInDialog.show();
            }else {
                Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                showCart = true;
                startActivity(cartIntent);

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

}