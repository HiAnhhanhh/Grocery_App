package com.example.grocery_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.grocery_app.R;
import com.example.grocery_app.adapter.OrderUserAdapter;
import com.example.grocery_app.adapter.ShopAdapter;
import com.example.grocery_app.databinding.ActivityMainUserBinding;
import com.example.grocery_app.models.OrderUserModels;
import com.example.grocery_app.models.ShopModels;
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

public class MainUserActivity extends AppCompatActivity {

    private ActivityMainUserBinding binding;

    private FirebaseAuth firebaseAuth;
    private static final String TAG = "CHECK";
    private TextView productTv,ordersTv;

    private ArrayList<ShopModels> shopModelsArrayList;
    private ShopAdapter adapter;

    private ArrayList<OrderUserModels> orderUserList;
    private OrderUserAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainUserBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());


        ordersTv = binding.ordersTv;
        productTv = binding.productTv;


        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadShopInfo();

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeOffline();
            }
        });

        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainUserActivity.this, EditProfileUserActivity.class);
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

    }

    private void loadShopInfo() {

        shopModelsArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("userType").equalTo("Seller")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        shopModelsArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ShopModels shopModels = ds.getValue(ShopModels.class);
                            shopModelsArrayList.add(shopModels);
                        }
                        adapter = new ShopAdapter(MainUserActivity.this, shopModelsArrayList);
                        binding.shopRec.setLayoutManager(new LinearLayoutManager(MainUserActivity.this));
                        binding.shopRec.setAdapter(adapter);
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

        loadOrderInfo();
    }

    private void loadOrderInfo() {
        orderUserList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    String uId = ds.getRef().getKey();
                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Orders");
                    ref1.child(uId).orderByChild("orderBy").equalTo(""+ firebaseAuth.getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        for(DataSnapshot ds : snapshot.getChildren()){
                                            OrderUserModels model = ds.getValue(OrderUserModels.class);
                                            orderUserList.add(model);
                                        }
                                        adapter1 = new OrderUserAdapter(MainUserActivity.this,orderUserList);
                                        binding.orderRec.setLayoutManager(new LinearLayoutManager(MainUserActivity.this));
                                        binding.orderRec.setAdapter(adapter1);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                        startActivity(new Intent(MainUserActivity.this, LoginActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if( user == null){
            Intent intent = new Intent(MainUserActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            loadUserInfo();
        }

    }

    private void loadUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(""+firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for( DataSnapshot ds : snapshot.getChildren()){
                            String fullName = ""+ds.child("fullName").getValue();
                            String userType = ""+ ds.child("userType").getValue();
                            String email = ""+ ds.child("email").getValue();
                            String phone = ""+ ds.child("phone").getValue();
                            String imageProfile = ""+ ds.child("imageProfile").getValue();

                            binding.fullNameTv.setText(fullName);
                            binding.phoneNumberTv.setText(phone);
                            binding.emailTv.setText(email);

                            try{
                                Glide.with(MainUserActivity.this).load(imageProfile).placeholder(R.drawable.baseline_account_circle_24).into(binding.imageProfile);
                            } catch (Exception e){
                                Glide.with(MainUserActivity.this).load(R.drawable.baseline_account_circle_24).into(binding.imageProfile);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}