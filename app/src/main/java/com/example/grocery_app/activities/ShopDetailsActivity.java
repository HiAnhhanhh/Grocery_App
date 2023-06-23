package com.example.grocery_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.grocery_app.databinding.ActivityShopDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShopDetailsActivity extends AppCompatActivity {

    ActivityShopDetailsBinding binding;
    FirebaseAuth firebaseAuth;

    String shopUid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        shopUid = getIntent().getStringExtra("shopUid");
        firebaseAuth = FirebaseAuth.getInstance();

        loadShopInfo();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void loadShopInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(shopUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String shopName = ""+ snapshot.child("shopName").getValue();
                            String address = ""+ snapshot.child("address").getValue();
                            String phone = ""+ snapshot.child("phone").getValue();
                            String email = ""+ snapshot.child("email").getValue();
                            String open = ""+snapshot.child("open").getValue();
                            String imageShop = ""+ snapshot.child("imageProfile").getValue();

                            binding.addressTv.setText(address);
                            binding.phoneTv.setText(phone);
                            binding.emailTv.setText(email);
                            binding.shopNameTv.setText(shopName);

                            if(open.equals("open")){
                                binding.openTv.setText("Open");
                            }else{
                                binding.openTv.setText("Closed");
                            }

                            try{
                                Glide.with(ShopDetailsActivity.this).load(imageShop).into(binding.shopImv);
                            } catch( Exception e){

                            }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}