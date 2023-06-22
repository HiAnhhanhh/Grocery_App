package com.example.grocery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.example.grocery_app.databinding.ActivityEditProductBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProductActivity extends AppCompatActivity {

    private ActivityEditProductBinding binding;


    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProductBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        String productID = getIntent().getStringExtra("productId");

        binding.discountNoteEt.setVisibility(View.GONE);
        binding.discountPriceEt.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        loadDetailProduct();


        binding.discountSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.discountPriceEt.setVisibility(View.VISIBLE);
                    binding.discountNoteEt.setVisibility(View.VISIBLE);
                }else{
                    binding.discountPriceEt.setVisibility(View.GONE);
                    binding.discountNoteEt.setVisibility(View.GONE);
                }
            }
        });



    }

    private void loadDetailProduct() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            String title = ""+ds.child("title").getValue();
                            String category = ""+ds.child("category").getValue();
                            String productId = ""+ ds.child("productId").getValue();
                            String discountedPrice = ""+ds.child("discountedPrice").getValue();
                            String discountedNote = ""+ds.child("discountedNote").getValue();
                            String originalPrice= ""+ds.child("originalPrice").getValue();
                            String discountAvailable = ""+ds.child("discountAvailable").getValue();
                            String timestamp = ""+ ds.child("timestamp").getValue();
                            String uid = ""+ds.child("uid").getValue();
                            String productImage = ""+ds.child("productImage").getValue();
                            String des = ""+ds.child("description").getValue();
                            String quantity = ""+ds.child("quantity").getValue();

                            if(discountAvailable.equals("true")){
                                binding.discountSwitch.setChecked(true);
                                binding.discountPriceEt.setVisibility(View.VISIBLE);
                                binding.discountNoteEt.setVisibility(View.VISIBLE);
                            }else{
                                binding.discountSwitch.setChecked(false);
                                binding.discountPriceEt.setVisibility(View.GONE);
                                binding.discountNoteEt.setVisibility(View.GONE);
                            }

                            binding.titleEt.setText(title);
                            binding.priceEt.setText(originalPrice);
                            binding.discountPriceEt.setText(discountedPrice);
                            binding.discountNoteEt.setText(discountedNote);
                            binding.quantityEt.setText(quantity);
                            binding.categoryEt.setText(category);

                            Glide.with(EditProductActivity.this).load(productImage).placeholder(R.drawable.baseline_add_shopping_cart_24_colorprimary).into(binding.productImageImv);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}