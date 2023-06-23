package com.example.grocery_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.grocery_app.Constants;
import com.example.grocery_app.adapter.ProductAdapter;
import com.example.grocery_app.R;
import com.example.grocery_app.databinding.ActivityMainSellerBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainSellerActivity extends AppCompatActivity {

    ActivityMainSellerBinding binding;
    FirebaseAuth firebaseAuth;

    TextView productTv, ordersTv;

    private ProductAdapter productAdapter;
    private ArrayList<Constants.ProductModels> productModelsArrayList;

    private final static String TAG = "TAG_MAIN_SELLER";
    String selectedCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainSellerBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        productTv = binding.productTv;
        ordersTv = binding.ordersTv;

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadAllProduct();


        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeOffline();
            }
        });

        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainSellerActivity.this, EditProfileSellerActivity.class);
                startActivity(intent);
            }
        });

        binding.addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainSellerActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });
        
        binding.productTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductList();
            }
        });
        
        binding.ordersTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrdersList();
            }
        });

        binding.filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainSellerActivity.this);
                builder.setTitle("Chose Category")
                        .setItems(Constants.optionsCategories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedCategory = Constants.optionsCategories[which];
                        binding.filteredTv.setText(selectedCategory);
                        if(selectedCategory.equals("All")){
                            binding.filteredTv.setText("Showing All");
                            loadAllProduct();
                        }else{
                            loadFilterList(selectedCategory);
                        }
                    }
                }).show();
            }
        });

//        binding.searchEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                try {
//                    productAdapter.getFilter().filter(s);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }

    private void loadFilterList(String selectedCategory) {
        productModelsArrayList= new ArrayList<>();
        Log.d(TAG, "loadAllProduct: ok");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Products");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productModelsArrayList.clear();
                        for( DataSnapshot ds : snapshot.getChildren()){

                            String category =""+ ds.child("category").getValue();

                            if(selectedCategory.equals(category)){
                                Constants.ProductModels model = ds.getValue(Constants.ProductModels.class);
                                productModelsArrayList.add(model);
                            }
                        }
                        productAdapter = new ProductAdapter(MainSellerActivity.this, productModelsArrayList);
                        binding.productRec.setLayoutManager(new LinearLayoutManager(MainSellerActivity.this));
                        binding.productRec.setAdapter(productAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void loadAllProduct() {
        productModelsArrayList= new ArrayList<>();
        Log.d(TAG, "loadAllProduct: ok");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(""+firebaseAuth.getUid()).child("Products")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productModelsArrayList.clear();
                        for( DataSnapshot ds : snapshot.getChildren()){
                            Constants.ProductModels model = ds.getValue(Constants.ProductModels.class);
                            productModelsArrayList.add(model);
                            Log.d(TAG, "onDataChange: "+ productModelsArrayList);
                        }
                        productAdapter = new ProductAdapter(MainSellerActivity.this, productModelsArrayList);
                        binding.productRec.setLayoutManager(new LinearLayoutManager(MainSellerActivity.this));
                        binding.productRec.setAdapter(productAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void showOrdersList() {

        binding.productListRl.setVisibility(View.GONE);
        binding.ordersListRl.setVisibility(View.VISIBLE);

        ordersTv.setBackgroundResource(R.drawable.shape_rect_04);
        ordersTv.setTextColor(getResources().getColor(R.color.black));

        productTv.setTextColor(getResources().getColor(R.color.white));
        productTv.setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    private void showProductList() {
        binding.productListRl.setVisibility(View.VISIBLE);
        binding.ordersListRl.setVisibility(View.GONE);

        productTv.setBackgroundResource(R.drawable.shape_rect_04);
        productTv.setTextColor(getResources().getColor(R.color.black));

        ordersTv.setTextColor(getResources().getColor(R.color.white));
        ordersTv.setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    private void makeOffline() {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("online","false");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        firebaseAuth.signOut();
                        startActivity(new Intent(MainSellerActivity.this, LoginActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void checkUser() {
        Log.d(TAG, "checkUser: 11");
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if( user == null){
            Intent intent = new Intent(MainSellerActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            loadInfo();
        }

    }

    private void loadInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for( DataSnapshot ds : snapshot.getChildren()){
                            String fullName = ""+ds.child("fullName").getValue();
                            String email = ""+ds.child("email").getValue();
                            String shopName = ""+ds.child("shopName").getValue();
                            String imageProfile = ""+ ds.child("imageProfile").getValue();
                            binding.fullNameTv.setText(fullName);
                            binding.emailTv.setText(email);
                            binding.shopNameTv.setText(shopName);

                            Glide.with(MainSellerActivity.this).load(imageProfile).placeholder(R.drawable.baseline_account_circle_24).into(binding.imageProfile);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}