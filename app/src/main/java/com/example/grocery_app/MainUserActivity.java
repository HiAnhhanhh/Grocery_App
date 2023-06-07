package com.example.grocery_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.grocery_app.databinding.ActivityMainUserBinding;

public class MainUserActivity extends AppCompatActivity {

    ActivityMainUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainUserBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());


    }
}