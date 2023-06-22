package com.example.grocery_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.grocery_app.databinding.ActivityProductDetailsSellerBinding;

public class ProductDetailsSellerActivity extends AppCompatActivity {

    ActivityProductDetailsSellerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsSellerBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
    }
}