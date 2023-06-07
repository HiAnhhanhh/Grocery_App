package com.example.grocery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.grocery_app.databinding.ActivityEditProfileBinding;
import com.example.grocery_app.databinding.ActivityMainUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding binding;
    private FirebaseAuth firebaseAuth;

    private String imageUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        checkUser();

        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });


    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }else{
            loadInfo();
        }

    }

    private void loadInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            String fullName = ""+snapshot.child("fullName").getValue();
                            String phone = ""+snapshot.child("phone").getValue();
                            String address = ""+snapshot.child("address").getValue();
                            String email = ""+snapshot.child("email").getValue();
                            String city = ""+snapshot.child("City").getValue();
                            String country = ""+snapshot.child("country").getValue();
                            String state = ""+snapshot.child("state").getValue();
                            String shopOpen = ""+ snapshot.child("shopOpen").getValue();
                            String deliveryFee = ""+snapshot.child("deliveryFee").getValue();
                            String profileImage = ""+snapshot.child("profileImage").getValue();

                            binding.fullNameEt.setText(fullName);
                            binding.phoneEt.setText(phone);
                            binding.countryEt.setText(email);
                            binding.addressEt.setText(address);
                            binding.cityEt.setText(city);
                            binding.countryEt.setText(country);
                            binding.stateEt.setText(state);
                            binding.deliveryFeeEt.setText(deliveryFee);


                            if(shopOpen.equals("true")){
                                binding.shopOpenSwitch.setChecked(true);
                            }else{
                                binding.shopOpenSwitch.setChecked(false);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void validate() {

    }
}