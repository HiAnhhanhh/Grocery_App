package com.example.grocery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
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

import java.util.HashMap;

public class MainSellerActivity extends AppCompatActivity {

    ActivityMainSellerBinding binding;
    FirebaseAuth firebaseAuth;

    private final static String TAG = "TAG_MAIN_SELLER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainSellerBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

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
            Log.d(TAG, "checkUser: ok");
            loadInfo();
        }

    }

    private void loadInfo() {
        Log.d(TAG, "loadInfo: Success");
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