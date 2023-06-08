package com.example.grocery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.grocery_app.databinding.ActivityMainUserBinding;
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

public class MainUserActivity extends AppCompatActivity {

    private ActivityMainUserBinding binding;

    private FirebaseAuth firebaseAuth;
    private static final String TAG = "CHECK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainUserBinding.inflate(LayoutInflater.from(this));
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
                Intent intent = new Intent(MainUserActivity.this, EditProfileUserActivity.class);
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
                        startActivity(new Intent(MainUserActivity.this, LoginActivity.class));
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
            Intent intent = new Intent(MainUserActivity.this, LoginActivity.class);
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
                            String userType = ""+ ds.child("userType").getValue();
                            Log.d(TAG, "onDataChange: "+ fullName + userType);
                            binding.fullNameTv.setText(fullName);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}