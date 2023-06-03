package com.example.grocery_app;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.grocery_app.databinding.ActivityRegisterUserBinding;

public class RegisterUserActivity extends AppCompatActivity {

    ActivityRegisterUserBinding binding;
    private String imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterUserBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.gpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

            }
        });
    }

    private void showImageAttachMenu() {

        PopupMenu popupMenu = new PopupMenu(this, binding.profileImv);
        popupMenu.getMenu().add(1,0,0,"Gallery");
        popupMenu.getMenu().add(2,0,0,"Camera");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int which = item.getGroupId();
                if(which == 1){
                    pickImageCamera();
                }else if (which == 2) {
                    pickImageGallery();
                }

                return false;
            }
        });

    }

    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("/image/.");
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