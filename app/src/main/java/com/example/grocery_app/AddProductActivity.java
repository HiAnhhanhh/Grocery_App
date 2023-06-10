package com.example.grocery_app;

import static androidx.constraintlayout.motion.widget.TransitionBuilder.validate;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.grocery_app.databinding.ActivityAddProductBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {

    ActivityAddProductBinding binding;
    String imageUri = null;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        binding.categoryEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialog();
            }
        });

        binding.addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               validate();
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
                }
            }
        });

        binding.productImageImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
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

    String des, category, price, quantity, discount, discountNote, title;
    boolean discountSwitch;

    private void validate() {

        title = binding.titleEt.getText().toString().trim();
        des = binding.descriptionEt.getText().toString().trim();
        price = binding.priceEt.getText().toString().trim();
        category = binding.categoryEt.getText().toString().trim();
        quantity = binding.quantityEt.getText().toString().trim();
        discount = binding.discountPriceEt.getText().toString().trim();
        discountNote = binding.discountNoteEt.getText().toString().trim();
        discountSwitch = binding.discountSwitch.isChecked();

        if(TextUtils.isEmpty(title)){
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show();
        }if(TextUtils.isEmpty(des)){
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show();
        }if(TextUtils.isEmpty(category)){
            Toast.makeText(this, "Pick Category ", Toast.LENGTH_SHORT).show();
        }if(TextUtils.isEmpty(price)){
            Toast.makeText(this, "Enter Price", Toast.LENGTH_SHORT).show();
        }if(TextUtils.isEmpty(quantity)){
            Toast.makeText(this, "Enter Quantity", Toast.LENGTH_SHORT).show();
        }if(discountSwitch){
            if (TextUtils.isEmpty(discount)){
                Toast.makeText(this, "Enter Discount", Toast.LENGTH_SHORT).show();
            }
            else{
                discount ="0";
                discountNote="";
            }
        }else{
            saveData();
        }

    }

    private void saveData() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String timestamp = String.valueOf(System.currentTimeMillis());

        if ( imageUri == null){
            HashMap<String, Object > hashMap = new HashMap<>();
            hashMap.put("title", ""+ title);
            hashMap.put("description","" + des);
            hashMap.put("category", ""+ category);
            hashMap.put("price", ""+ price);
            hashMap.put("quantity", ""+ quantity);
            hashMap.put("discount", ""+ discount);
            hashMap.put("discountNote", ""+ discountNote);
            hashMap.put("timestamp", ""+ timestamp);
            hashMap.put("productId", ""+ timestamp);
            hashMap.put("uid", ""+ firebaseAuth.getUid());
            hashMap.put("productImage", "");

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

            ref.child(firebaseAuth.getUid()).child("Product")
                    .setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AddProductActivity.this, "Product Added", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddProductActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

                            }
                        }
                    });
        }

    }

    String selectedCategory= "";

    private void categoryDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Product Category")
                .setItems(Constants.options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedCategory = Constants.options[which];
                        binding.categoryEt.setText(selectedCategory);
                    }
                })
                .show();

    }
}