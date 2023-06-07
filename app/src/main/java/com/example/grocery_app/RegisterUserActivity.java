package com.example.grocery_app;

import static android.content.ContentValues.TAG;

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
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.grocery_app.databinding.ActivityRegisterUserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class RegisterUserActivity extends AppCompatActivity {

    ActivityRegisterUserBinding binding;
    private String imageUri = null;
    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterUserBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Create Account");

        binding.registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterUserActivity.this, RegisterSellerActivity.class);
                startActivity(intent);
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.profileImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageAttachMenu();

            }
        });

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    String email, fullName, phone, country, state, city, address, password, confirmPassword;


    private void validate() {
        fullName = binding.fullNameEt.getText().toString().trim();
        phone = binding.phoneEt.getText().toString().trim();
        city = binding.cityEt.getText().toString().trim();
        country = binding.countryEt.getText().toString().trim();
        state = binding.stateEt.getText().toString().trim();
        address = binding.addressEt.getText().toString().trim();
        email = binding.emailEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();
        confirmPassword = binding.confirmPasswordEt.getText().toString().trim();

        if(TextUtils.isEmpty(fullName)){
            Toast.makeText(this, "Enter Full Name", Toast.LENGTH_SHORT).show();
            return;
        }if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Enter Phone", Toast.LENGTH_SHORT).show();
            return;
//        }if (latitude == 0.0 || longitude == 0.0 ){
//            Toast.makeText(this, "Enter City", Toast.LENGTH_SHORT).show();
//            return;
        } if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        } if (password.length()<6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        } if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
            return;
        }else{
            createAccount();
        }

    }

    private void createAccount() {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Log.d(TAG, "createAccount: email" + email);
        Log.d(TAG, "createAccount: password"+ password);



        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        saverFirebaseData();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void saverFirebaseData() {

        String uid = firebaseAuth.getUid();
        final String timestamp =""+ System.currentTimeMillis();
        progressDialog.setMessage("Saving Account Info ...");
        progressDialog.show();

        if(imageUri == null){
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("fullName",""+ fullName);
            hashMap.put("email",""+email);
            hashMap.put("uid",""+uid);
            hashMap.put("timestamp",""+ timestamp);
            hashMap.put("country",""+ country);
            hashMap.put("state",""+state);
            hashMap.put("city", ""+city);
            hashMap.put("address", ""+ address);
            hashMap.put("userType", "User");
            hashMap.put("online", "true");
            hashMap.put("shopOpen","open");
            hashMap.put("imageProfile","");

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(uid).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent intent = new Intent(RegisterUserActivity.this, LoginActivity.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterUserActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                hashMap.put("email",""+email);
                                hashMap.put("uid",""+uid);
                                hashMap.put("timestamp",""+ timestamp);
                                hashMap.put("country",""+ country);
                                hashMap.put("state",""+state);
                                hashMap.put("city", ""+city);
                                hashMap.put("address", ""+ address);
                                hashMap.put("userType", "Seller");
                                hashMap.put("online", "true");
                                hashMap.put("shopOpen","open");
                                hashMap.put("imageProfile",""+ downloadImageUri); //url of upload Image

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                ref.child(uid).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Intent intent = new Intent(RegisterUserActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                progressDialog.dismiss();
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisterUserActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

        PopupMenu popupMenu = new PopupMenu(this, binding.profileImv);
        popupMenu.getMenu().add(Menu.NONE,0,0,"Gallery");
        popupMenu.getMenu().add(Menu.NONE,1,1,"Camera");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int which = item.getItemId();
                if(which == 1){
                    pickImageCamera();
                }else if (which == 0) {
                    pickImageGallery();
                }

                return false;
            }
        });

    }

    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private void pickImageCamera() {
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK ){
                        Log.d(TAG, "onActivityResult: "+ imageUri);
                        Intent data = result.getData();
                        imageUri = String.valueOf(data.getData());
                        Log.d(TAG, "onActivityResult: "+ imageUri);
                        binding.profileImv.setImageURI(Uri.parse(imageUri));
                    }
                }
            });
}