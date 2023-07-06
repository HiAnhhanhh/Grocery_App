package com.example.grocery_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.example.grocery_app.R;
import com.example.grocery_app.adapter.ReviewAdapter;
import com.example.grocery_app.databinding.ActivityShopReviewBinding;
import com.example.grocery_app.models.ReviewModels;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.xml.datatype.DatatypeConfigurationException;

public class ShopReviewActivity extends AppCompatActivity {

    private ActivityShopReviewBinding binding;
    private ArrayList<ReviewModels> reviewModelsArrayList;
    private ReviewAdapter adapter;
    private FirebaseAuth firebaseAuth;

    private String shopUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopReviewBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        shopUid = getIntent().getStringExtra("shopUid");
        firebaseAuth = FirebaseAuth.getInstance();
        loadShopDetails();
//        loadReviewsUser();
    }

    private void loadShopDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(shopUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String profileImage = ""+ snapshot.child("imageProfile").getValue();
                        String shopName = ""+ snapshot.child("shopName").getValue();

                        binding.shopNameTv.setText(shopName);
                        try{
                            Glide.with(ShopReviewActivity.this).load(profileImage).placeholder(R.drawable.baseline_store_24).into(binding.shopImageSiv);
                        } catch (Exception e){
                            Glide.with(ShopReviewActivity.this).load(R.drawable.baseline_store_24).into(binding.shopImageSiv);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadReviewsUser() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Review");
        ref.child(shopUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}