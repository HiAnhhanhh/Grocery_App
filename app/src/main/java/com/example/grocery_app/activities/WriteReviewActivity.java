package com.example.grocery_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.grocery_app.R;
import com.example.grocery_app.databinding.ActivityWriteReviewBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class WriteReviewActivity extends AppCompatActivity {

    private ActivityWriteReviewBinding binding;
    private String shopId;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWriteReviewBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        shopId = getIntent().getStringExtra("shopId");
        loadShopInfo();

        binding.submitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void loadShopInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(""+shopId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String profileImageShop = ""+snapshot.child("profileImage").getValue();
                        String shopName = ""+snapshot.child("shopName").getValue();

                        binding.shopNameTv.setText(shopName);
                        try {
                            Glide.with(WriteReviewActivity.this).load(profileImageShop).placeholder(R.drawable.baseline_store_24).into(binding.shopImageSiv);
                        } catch (Exception e){
                            Glide.with(WriteReviewActivity.this).load(R.drawable.baseline_store_24).into(binding.shopImageSiv);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void inputData() {
        String reviewEt = binding.textReview.getText().toString().trim();
        String ratingBar = ""+ binding.ratingBar.getRating();

        String timestamp = ""+System.currentTimeMillis();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", timestamp);
        hashMap.put("review",reviewEt);
        hashMap.put("rating",ratingBar);
        hashMap.put("reviewTo",shopId);
        hashMap.put("reviewBy",""+ firebaseAuth.getUid());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Review");
        ref.child(""+ firebaseAuth.getUid()).child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(WriteReviewActivity.this, "Reviewed", Toast.LENGTH_SHORT).show();
                    }
                });
        
    }
}