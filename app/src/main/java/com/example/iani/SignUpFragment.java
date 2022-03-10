package com.example.iani;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignUpFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {

    }

    private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;
    String[] items= {"Consumer","Producer"};
    AutoCompleteTextView signup_usertype;
    ArrayAdapter<String> adapterItems;

    private EditText email;
    private EditText fullname;
    private EditText password;
    private EditText confirmpassword;

    private ImageButton closeBtn;
    private Button signUpBtn;

    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String emailPattern="[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public static boolean disableCloseBtn = false;

    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sign_up, container, false);

        signup_usertype=view.findViewById(R.id.sign_up_user_type);
        adapterItems=new ArrayAdapter<String>(getActivity(),R.layout.sign_up_dropdown,items);
        signup_usertype.setAdapter(adapterItems);

        alreadyHaveAnAccount=view.findViewById(R.id.already_have_an_account);
        email=view.findViewById(R.id.sign_up_email);
        fullname=view.findViewById(R.id.sign_up_full_name);
        password=view.findViewById(R.id.sign_up_password);
        confirmpassword=view.findViewById(R.id.sign_up_confirm_password);

        closeBtn=view.findViewById(R.id.sign_up_clost_btn);
        signUpBtn=view.findViewById(R.id.sign_up_btn);

        progressBar=view.findViewById(R.id.sign_up_progressbar);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        parentFrameLayout=getActivity().findViewById(R.id.register_framelayout);

        if(disableCloseBtn){
            closeBtn.setVisibility(View.GONE);
        }else {
            closeBtn.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
    });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signup_usertype.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailandPassword();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs(){
        if (!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(fullname.getText())){
                if (!TextUtils.isEmpty(password.getText()) && password.length() >= 8){
                    if (!TextUtils.isEmpty(confirmpassword.getText())){
                        if(!TextUtils.isEmpty(signup_usertype.getText())) {
                            signUpBtn.setEnabled(true);
                            signUpBtn.setTextColor(Color.rgb(255,255,255));
                        }else {
                            signUpBtn.setEnabled(false);
                            signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
                        }
                    }else {
                        signUpBtn.setEnabled(false);
                        signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
                    }
                }else {
                    signUpBtn.setEnabled(false);
                    signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));

                }
            }else {
                signUpBtn.setEnabled(false);
                signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
            }
        }else {
            signUpBtn.setEnabled(false);
            signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }

    private void checkEmailandPassword(){

        Drawable customErrorIcon = getResources().getDrawable(R.drawable.custom_error_icon);
        customErrorIcon.setBounds(0,0,customErrorIcon.getIntrinsicWidth(),customErrorIcon.getIntrinsicHeight());

        if (email.getText().toString().matches(emailPattern)){
            if(password.getText().toString().equals(confirmpassword.getText().toString())){

                progressBar.setVisibility(View.VISIBLE);
                signUpBtn.setEnabled(false);
                signUpBtn.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            Map<String, Object> userdata = new HashMap<>();
                            userdata.put("fullname", fullname.getText().toString());
                            userdata.put("userType", signup_usertype.getText().toString());

                            firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                    .set(userdata)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()){

                                            CollectionReference userDataReference = firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");

                                            //MAPS
                                            Map<String, Object> bookmarksMap = new HashMap<>();
                                            bookmarksMap.put("list_size",(long) 0);

                                            Map<String,Object>ratingsMap = new HashMap<>();
                                            ratingsMap.put("list_size",(long) 0);

                                            Map<String, Object> cartMap = new HashMap<>();
                                            cartMap.put("list_size",(long)0);

                                            Map<String, Object> myAddressesMap = new HashMap<>();
                                            myAddressesMap.put("list_size",(long) 0);
                                            //MAPS
                                            List<String> documentNames = new ArrayList<>();
                                            documentNames.add("MY_BOOKMARKS");
                                            documentNames.add("MY_RATINGS");
                                            documentNames.add("MY_CART");
                                            documentNames.add("MY_ADDRESSES");

                                            List<Map<String, Object>> documentFields = new ArrayList<>();
                                            documentFields.add(bookmarksMap);
                                            documentFields.add(ratingsMap);
                                            documentFields.add(cartMap);
                                            documentFields.add(myAddressesMap);

                                            for (int x = 0;x<documentNames.size();x++){

                                                final int finalX = x;
                                                userDataReference.document(documentNames.get(x))
                                                        .set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            if (finalX == documentNames.size()-1) {
                                                                mainIntent();
                                                            }
                                                        }else {
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            signUpBtn.setEnabled(true);
                                                            signUpBtn.setTextColor(Color.rgb(255,255, 255));
                                                            String error = task.getException().getMessage();
                                                            Toast.makeText(getActivity(), error,Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }

                                        } else{
                                            String error = task1.getException().getMessage();
                                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }else {
                            progressBar.setVisibility(View.INVISIBLE);
                            signUpBtn.setEnabled(true);
                            signUpBtn.setTextColor(Color.rgb(255,255,255));
                            String error = task.getException().getMessage();
                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                confirmpassword.setError("Password doesn't match.",customErrorIcon);
            }
        }else {
            email.setError("Invalid Email",customErrorIcon);
        }
    }

    private void mainIntent(){
        if(disableCloseBtn){
            disableCloseBtn = false;
        }else {
            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(mainIntent);
        }
        getActivity().finish();
    }
}