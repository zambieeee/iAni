package com.example.iani;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.example.iani.RegisterActivity.setSignUpFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int HOME_FRAGMENT = 0;
    private static final int CART_FRAGMENT= 1;
    private static final int ORDER_FRAGMENT=2;
    private static final int ACCOUNT_FRAGMENT=3;
    private static final int BOOKMARK_FRAGMENT=4;
    public static Boolean showCart = false;

    private AppBarConfiguration mAppBarConfiguration;
    private FrameLayout frameLayout;
    private ImageView actionBarLogo;
    private int currentFragment = -1;
    private NavigationView navigationView;

    private Window window;
    private Toolbar toolbar;
    private Dialog signInDialog;
    private FirebaseUser currentUser;
    private TextView badgeCount;
    private int scrollFlags;
    private AppBarLayout.LayoutParams params;

    public static DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        actionBarLogo=findViewById(R.id.actionbar_logo);
        setSupportActionBar(toolbar);
        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        scrollFlags = params.getScrollFlags();

        drawer = findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        

        frameLayout=findViewById(R.id.main_framelayout);
        if (showCart) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            gotoFragment("My Cart", new MyCartFragment(), -2);
        } else {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            setFragment(new HomeFragment(), HOME_FRAGMENT);
        }

        signInDialog = new Dialog(MainActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.dialog_signin_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.dialog_signup_btn);
        Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);
        }else {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);
        }
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            if (currentFragment == HOME_FRAGMENT){
                currentFragment = -1;
                super.onBackPressed();
            }else {
                if(showCart){
                    showCart=false;
                    finish();
                }else {
                    actionBarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(),HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentFragment==HOME_FRAGMENT){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main, menu);

            MenuItem cartItem = menu.findItem(R.id.main_cart_icon);
                cartItem.setActionView(R.layout.badge_layout);
                ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
                badgeIcon.setImageResource(R.drawable.cart);
                badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);
                if(currentUser !=null){
                    if(DBqueries.cartList.size() == 0){
                        DBqueries.loadCartList(MainActivity.this, new Dialog(MainActivity.this), false, badgeCount, new TextView(MainActivity.this));
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
                    if (currentUser == null){
                        signInDialog.show();
                    }else {
                        gotoFragment("My Cart", new MyCartFragment(),CART_FRAGMENT);
                    }
                });

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.main_search_icon){
            //todo:search
            return true;
        }else if (id==R.id.main_notification_icon){
            //todo:notification
            return true;
        }else if (id==R.id.main_cart_icon){
            if (currentUser == null){
                signInDialog.show();
            }else {
                gotoFragment("My Cart", new MyCartFragment(),CART_FRAGMENT);
            }
            return true;
        }else if(id == android.R.id.home){
            if(showCart){
                showCart = false;
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void gotoFragment(String title, Fragment fragment, int fragmentNo) {
        actionBarLogo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();
        setFragment(fragment, fragmentNo);
        if (fragmentNo==CART_FRAGMENT || showCart){
            navigationView.getMenu().getItem(2).setChecked(true);
            params.setScrollFlags(0);
        }else {
            params.setScrollFlags(scrollFlags);
        }
    }

    MenuItem menuItem;
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        menuItem = item;

        if(currentUser!=null) {
            drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    int id = menuItem.getItemId();
                    if (id == R.id.nav_iani) {
                        actionBarLogo.setVisibility(View.VISIBLE);
                        invalidateOptionsMenu();
                        setFragment(new HomeFragment(), HOME_FRAGMENT);
                    } else if (id == R.id.nav_my_orders) {
                        gotoFragment("My Orders", new MyOrdersFragment(), ORDER_FRAGMENT);
                    } else if (id == R.id.nav_my_cart) {
                        gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                    } else if (id == R.id.nav_track_my_order) {

                    } else if (id == R.id.nav_nearby_shops) {

                    } else if (id == R.id.nav_products_analytics) {

                    } else if (id == R.id.nav_bookmarks) {
                        gotoFragment("My Bookmarks", new MyBookmarksFragment(), BOOKMARK_FRAGMENT);
                    } else if (id == R.id.nav_my_account) {
                        gotoFragment("My Account", new MyAccountFragment(), ACCOUNT_FRAGMENT);
                    } else if (id == R.id.nav_sign_out) {
                        FirebaseAuth.getInstance().signOut();
                        DBqueries.clearData();
                        Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(registerIntent);
                        finish();
                    }
                }
            });
            return true;
        }else {
            signInDialog.show();
            return false;
        }
    }

    private void setFragment(Fragment fragment, int fragmentNo){
        if (fragmentNo != currentFragment) {
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}