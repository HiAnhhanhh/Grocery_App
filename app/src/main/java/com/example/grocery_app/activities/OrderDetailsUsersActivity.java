package com.example.grocery_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.grocery_app.R;
import com.example.grocery_app.adapter.OrderItemsAdapter;
import com.example.grocery_app.databinding.ActivityOrderDetailsUsersBinding;
import com.example.grocery_app.models.OrderItemsModels;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class OrderDetailsUsersActivity extends AppCompatActivity {

    String orderId, orderTo;

    public static final String TAG = "CHECK_ORDER";

    private ActivityOrderDetailsUsersBinding binding;

    private OrderItemsAdapter adapter;
    private ArrayList<OrderItemsModels> orderItemsModelsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailsUsersBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        orderId = getIntent().getStringExtra("orderId");
        orderTo = getIntent().getStringExtra("orderTo");
        Log.d(TAG, "onCreate: "+ orderId + " " + orderTo);

        loadOrderDetails();
        loadOrderItems();


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadOrderItems() {
        orderItemsModelsArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Orders");
        ref.child(orderTo).child(orderId).child("Items")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderItemsModelsArrayList.clear();
                        for(DataSnapshot ds : snapshot.getChildren()){
                            OrderItemsModels models = ds.getValue(OrderItemsModels.class);
                            orderItemsModelsArrayList.add(models);
                        }
                        adapter = new OrderItemsAdapter(OrderDetailsUsersActivity.this,orderItemsModelsArrayList);
                        binding.orderDetailsRec.setLayoutManager(new LinearLayoutManager(OrderDetailsUsersActivity.this));
                        binding.orderDetailsRec.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadOrderDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Orders").child(orderTo).child(orderId);
            ref
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String orderId = ""+snapshot.child("orderId").getValue();
                        String orderTo = ""+snapshot.child("orderTo").getValue();
                        String orderBy = ""+snapshot.child("orderBy").getValue();
                        String orderStatus = ""+snapshot.child("orderStatus").getValue();
                        String orderTime = ""+snapshot.child("orderId").getValue();
                        String orderCost = ""+snapshot.child("orderCost").getValue();
                        String address = ""+snapshot.child("shopAddress").getValue();
                        String shopName = ""+snapshot.child("orderByShop").getValue();

                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(Long.parseLong(orderTime));
                        String date = DateFormat.format("dd/MM/yy",cal).toString();


                        binding.shopNameTv.setText(shopName);
                        binding.deliveryAddress.setText(address);
                        binding.orderIdTv.setText(orderId);
                        binding.dateTv.setText(date);
                        if(orderStatus.equals("In Process")){
                            binding.statusTv.setTextColor(OrderDetailsUsersActivity.this.getResources().getColor(R.color.green));
                        }else if(orderStatus.equals("Completed")){
                            binding.statusTv.setTextColor(OrderDetailsUsersActivity.this.getResources().getColor(R.color.blue));
                        }else{
                            binding.statusTv.setTextColor(OrderDetailsUsersActivity.this.getResources().getColor(R.color.red));
                        }
                        binding.totalPriceTv.setText("$"+orderCost);

                        ref.child("Items")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String items =""+snapshot.getChildrenCount();
                                        binding.itemTv.setText(items);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}