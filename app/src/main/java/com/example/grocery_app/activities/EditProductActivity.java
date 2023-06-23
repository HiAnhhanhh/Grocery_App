package com.example.grocery_app.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.grocery_app.R;
import com.example.grocery_app.databinding.ActivityEditProductBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class EditProductActivity extends AppCompatActivity {

    private ActivityEditProductBinding binding;

    private ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    String imageUri = null;

    String productID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProductBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        productID = getIntent().getStringExtra("productId");

        binding.discountNoteEt.setVisibility(View.GONE);
        binding.discountPriceEt.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        loadDetailProduct();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait ...");


        binding.productImageImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        binding.discountSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.discountPriceEt.setVisibility(View.VISIBLE);
                    binding.discountNoteEt.setVisibility(View.VISIBLE);
                }else{
                    binding.discountPriceEt.setVisibility(View.GONE);
                    binding.discountNoteEt.setVisibility(View.GONE);
                    binding.discountNoteEt.setText("");
                    binding.discountPriceEt.setText("");
                }
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.updateProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });


    }

    String title, des, price, category, quantity, discount,discountNote;
    boolean discountAvailable;

    private void validate() {

        title = binding.titleEt.getText().toString().trim();
        des = binding.descriptionEt.getText().toString().trim();
        price = binding.priceEt.getText().toString().trim();
        category = binding.categoryEt.getText().toString().trim();
        quantity = binding.quantityEt.getText().toString().trim();
        discountAvailable  = binding.discountSwitch.isChecked();

        if(TextUtils.isEmpty(title)){
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show();
            return;
        }if(TextUtils.isEmpty(category)){
            Toast.makeText(this, "Pick Category ", Toast.LENGTH_SHORT).show();
            return;
        }if(TextUtils.isEmpty(price)){
            Toast.makeText(this, "Enter Price", Toast.LENGTH_SHORT).show();
            return;
        }if(TextUtils.isEmpty(quantity)){
            Toast.makeText(this, "Enter Quantity", Toast.LENGTH_SHORT).show();
            return;
        }if(discountAvailable){
            discount = binding.discountPriceEt.getText().toString().trim();
            discountNote = binding.discountNoteEt.getText().toString().trim();
            if (TextUtils.isEmpty(discount)){
                Toast.makeText(this, "Enter Discount", Toast.LENGTH_SHORT).show();
                return;
            }
        }else{
            discount="";
            discountNote="";
        }
        updateProduct();
    }

    private void updateProduct() {
        progressDialog.setMessage("Updating Product ...");
        progressDialog.show();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String timestamp = String.valueOf(System.currentTimeMillis());

        if ( imageUri == null){
            HashMap<String, Object > hashMap = new HashMap<>();
            hashMap.put("title", ""+ title);
            hashMap.put("description","" + des);
            hashMap.put("category", ""+ category);
            hashMap.put("originalPrice", ""+ price);
            hashMap.put("quantity", ""+ quantity);
            hashMap.put("discountedPrice", ""+ discount);
            hashMap.put("discountedNote", ""+ discountNote);
            hashMap.put("discountAvailable",""+discountAvailable);


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

            ref.child(""+firebaseAuth.getUid()).child("Products").child(productID)
                    .updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditProductActivity.this, "Product Updated", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProductActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                        }
                    });

        }else{
            String filePathName = "product_image/"+ ""+firebaseAuth.getUid();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathName);

            storageReference.putFile(Uri.parse(imageUri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                            while (!task.isSuccessful());
                            Uri downloadUri = task.getResult();

                            if(task.isSuccessful()){

                                HashMap<String, Object > hashMap = new HashMap<>();
                                hashMap.put("title", ""+ title);
                                hashMap.put("description","" + des);
                                hashMap.put("category", ""+ category);
                                hashMap.put("originalPrice", ""+ price);
                                hashMap.put("quantity", ""+ quantity);
                                hashMap.put("discountedPrice", ""+ discount);
                                hashMap.put("discountAvailable", ""+ discountAvailable);
                                hashMap.put("discountedNote", ""+ discountNote);


                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

                                ref.child(""+firebaseAuth.getUid()).child("Products").child(productID)
                                        .updateChildren(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(EditProductActivity.this, "Product Updated", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(EditProductActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                finish();
                                            }
                                        });

                            }
                        }
                    });
        }
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

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if( result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        imageUri = String.valueOf(data.getData());
                        binding.productImageImv.setImageURI(Uri.parse(imageUri));
                    }
                }
            });

}