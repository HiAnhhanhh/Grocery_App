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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.grocery_app.R;
import com.example.grocery_app.databinding.ActivityEditProfileUserBinding;
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

public class EditProfileUserActivity extends AppCompatActivity {

    ActivityEditProfileUserBinding binding;

    private FirebaseAuth firebaseAuth;

    private String imageUri = null;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileUserBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");

        checkUser();

        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        binding.editProfileImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageAttachMenu();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    String  fullName, phone, country, state, city, address;

    private void validate() {
        fullName = binding.fullNameEt.getText().toString().trim();
        phone = binding.phoneEt.getText().toString().trim();
        city = binding.cityEt.getText().toString().trim();
        country = binding.countryEt.getText().toString().trim();
        state = binding.stateEt.getText().toString().trim();
        address = binding.addressEt.getText().toString().trim();


        if(TextUtils.isEmpty(fullName)){
            Toast.makeText(this, "Enter Full Name", Toast.LENGTH_SHORT).show();
            return;
        }if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Enter Phone", Toast.LENGTH_SHORT).show();
            return;
        }if (TextUtils.isEmpty(city)) {
            Toast.makeText(this, "Enter City", Toast.LENGTH_SHORT).show();
            return;
        }if (TextUtils.isEmpty(state)) {
            Toast.makeText(this, "Enter State", Toast.LENGTH_SHORT).show();
            return;
        }if (TextUtils.isEmpty(country)) {
            Toast.makeText(this, "Enter country", Toast.LENGTH_SHORT).show();
            return;
        }else{
            updateProfile();
        }
    }

    private void updateProfile() {

        String uid = ""+ firebaseAuth.getUid();
        progressDialog.setMessage("Updating profile ...");
        progressDialog.show();

        if(imageUri == null){
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("fullName",""+ fullName);
            hashMap.put("uid",""+uid);
            hashMap.put("country",""+ country);
            hashMap.put("state",""+state);
            hashMap.put("city", ""+city);
            hashMap.put("address", ""+ address);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(uid).updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent intent = new Intent(EditProfileUserActivity.this, MainSellerActivity.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfileUserActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                        }
                    });
        }else{

            String filePathName = "profile_image/"+ ""+uid;

            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathName);
            storageReference.putFile(Uri.parse(imageUri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                            while (!task.isSuccessful());
                            Uri downloadImageUri = task.getResult();

                            if (task.isSuccessful()){
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("fullName",""+ fullName);
                                hashMap.put("uid",""+uid);
                                hashMap.put("country",""+ country);
                                hashMap.put("state",""+state);
                                hashMap.put("city", ""+city);
                                hashMap.put("address", ""+ address);
                                hashMap.put("imageProfile",""+ downloadImageUri); //url of upload Image

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                ref.child(uid).updateChildren(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Intent intent = new Intent(EditProfileUserActivity.this, MainSellerActivity.class);
                                                startActivity(intent);
                                                progressDialog.dismiss();
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(EditProfileUserActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                finish();
                                            }
                                        });
                            }
                        }
                    });

        }





    }

    private void showImageAttachMenu() {
        PopupMenu menu = new PopupMenu(this,binding.editProfileImv);
        menu.getMenu().add(Menu.NONE,0,0,"Gallery");
        menu.getMenu().add(Menu.NONE,1,1,"Camera");
        menu.show();

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int which = item.getItemId();

                if(which == 0){
                    pickImageGallery();
                }else if(which==1){
                    pickImageCamera();
                }


                return false;
            }
        });

    }

    private void pickImageCamera() {
    }

    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        imageUri = String.valueOf(data.getData());
                        binding.editProfileImv.setImageURI(Uri.parse(imageUri));
                    }
                }
            });

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
                            String fullName = ""+ds.child("fullName").getValue();
                            String phone = ""+ds.child("phone").getValue();
                            String shopName = ""+ ds.child("shopName").getValue();
                            String address = ""+ds.child("address").getValue();
                            String city = ""+ds.child("city").getValue();
                            String country = ""+ds.child("country").getValue();
                            String state = ""+ds.child("state").getValue();
                            String shopOpen = ""+ ds.child("shopOpen").getValue();
                            String deliveryFee = ""+ds.child("deliveryFee").getValue();
                            String profileImage = ""+ds.child("imageProfile").getValue();

                            binding.fullNameEt.setText(fullName);
                            binding.phoneEt.setText(phone);
                            binding.addressEt.setText(address);
                            binding.cityEt.setText(city);
                            binding.countryEt.setText(country);
                            binding.stateEt.setText(state);



                            Glide.with(EditProfileUserActivity.this).load(profileImage).placeholder(R.drawable.baseline_account_circle_24).into(binding.editProfileImv);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}