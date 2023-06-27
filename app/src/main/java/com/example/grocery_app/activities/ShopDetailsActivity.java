package com.example.grocery_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.grocery_app.adapter.CartItemAdapter;
import com.example.grocery_app.adapter.ProductUserAdapter;
import com.example.grocery_app.databinding.ActivityShopDetailsBinding;
import com.example.grocery_app.databinding.DialogCartBinding;
import com.example.grocery_app.models.CartItemModels;
import com.example.grocery_app.models.ProductUserModels;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class ShopDetailsActivity extends AppCompatActivity {

    private ActivityShopDetailsBinding binding;
    private FirebaseAuth firebaseAuth;

    private DialogCartBinding bindingDialog;

    ProgressDialog progressDialog;

    public final  static String TAG ="CHECK_ORDER";
    private ArrayList<ProductUserModels> productUserModelsArrayList;
    private ArrayList<CartItemModels> cartItemModelsArrayList;

    private CartItemAdapter adapter1;
    private ProductUserAdapter adapter;

    String deliveryFee;
    String shopName;


    String shopUid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        shopUid = getIntent().getStringExtra("shopUid");
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");

        loadShopInfo();
        loadProductUser();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.orderDetailsImB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderDetail();
            }
        });

    }

    public double allTotalPrice =0.0;
    public double TotalPrice = 0.0;
    private void showOrderDetail() {


        cartItemModelsArrayList = new ArrayList<>();
        bindingDialog = DialogCartBinding.inflate(LayoutInflater.from(this));

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(bindingDialog.getRoot());

        AlertDialog dialog = alert.create();
        dialog.show();


        bindingDialog.shopNameTv.setText(shopName);

//        EasyDB easyDB = EasyDB.init(this,"ITEM_DB")
//                .setTableName("ITEM_TABLE")
//                .addColumn(new Column("Item_Id", new String[]{"text","unique"}))
//                .addColumn(new Column("Item_PId", new String[]{"text","not null"}))
//                .addColumn(new Column("Item_Name", new String[]{"text","not null"}))
//                .addColumn(new Column("Item_Price_Each", new String[]{"text","not null"}))
//                .addColumn(new Column("Item_Price", new String[]{"text","not null"}))
//                .addColumn(new Column("Item_Quantity", new String[]{"text","not null"}))
//                .doneTableColumn();
//
//        Cursor res = easyDB.getAllData();
//
//        while (res.moveToNext()){
//            String id = res.getString(1);
//            String pId = res.getString(2);
//            String name = res.getString(3);
//            String price = res.getString(4);
//            String cost = res.getString(5);
//            String quantity = res.getString(6);
//
//            allTotalPrice = allTotalPrice + Double.parseDouble(cost);
//
//            CartItemModels model = new CartItemModels(""+id,""+pId,
//                    ""+name, ""+price,""+ cost,""+quantity);
//
//            cartItemModelsArrayList.add(model);
//        }
//
//
//
//
////        adapter1 = new CartItemAdapter(ShopDetailsActivity.this, cartItemModelsArrayList);
////        bindingDialog.cartItemRec.setLayoutManager(new LinearLayoutManager(this));
////        bindingDialog.cartItemRec.setAdapter(adapter1);
//
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("AddToCart");
        ref.child(""+firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                cartItemModelsArrayList.clear();
                                for(DataSnapshot ds : snapshot.getChildren()){
                                    CartItemModels models = ds.getValue(CartItemModels.class);
                                    cartItemModelsArrayList.add(models);
                                    allTotalPrice = allTotalPrice + Double.parseDouble(models.getItemPrice().replace("$",""));
                                    TotalPrice = allTotalPrice;
                                }
                                adapter1 = new CartItemAdapter(ShopDetailsActivity.this, cartItemModelsArrayList);
                                bindingDialog.cartItemRec.setLayoutManager(new LinearLayoutManager(ShopDetailsActivity.this));
                                bindingDialog.cartItemRec.setAdapter(adapter1);
                                allTotalPrice = 0.0;

                                if(TotalPrice > 0){
                                    bindingDialog.deliveryFee.setText("$"+ deliveryFee);
                                    bindingDialog.subTotalPrice.setText("$"+ TotalPrice);
                                    bindingDialog.TotalPrice.setText("$"+(TotalPrice+ Double.parseDouble(deliveryFee.replace("$",""))));
                                    TotalPrice = 0.0;
                                }else {
                                    bindingDialog.subTotalPrice.setText("0.0");
                                    bindingDialog.deliveryFee.setText("0.0");
                                    bindingDialog.TotalPrice.setText("0.0");
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                allTotalPrice = 0.0;
            }
        });
        
        bindingDialog.confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOrders();
            }
        });

    }

    private void submitOrders() {

        progressDialog.setMessage("Placing Order...");
        progressDialog.show();


        String timestamp1 = String.valueOf(System.currentTimeMillis());
        String cost = "" + bindingDialog.TotalPrice.getText().toString().trim().replace("$","");

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("orderId", timestamp1);
        hashMap.put("orderTime",timestamp1);
        hashMap.put("orderStatus","In Process");
        hashMap.put("orderTo",""+ shopUid);
        hashMap.put("orderBy",""+ firebaseAuth.getUid());
        hashMap.put("orderCost",cost);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Orders").child(""+ shopUid).child(""+timestamp1);
        ref
            .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        for(int i=0; i<cartItemModelsArrayList.size();i++){
                            String productId = cartItemModelsArrayList.get(i).getItemProductId();
                            String quantity = cartItemModelsArrayList.get(i).getItemQuantity();
                            String name = cartItemModelsArrayList.get(i).getItemName();
                            String cost = cartItemModelsArrayList.get(i).getItemPrice();
                            String price = cartItemModelsArrayList.get(i).getItemPriceEach();


                            HashMap<String, String> hashMap1 = new HashMap<>();
                            hashMap1.put("productId",productId);
                            hashMap1.put("name", name);
                            hashMap1.put("quantity", quantity);
                            hashMap1.put("cost",cost);
                            hashMap1.put("price",price);

                            ref.child("Items").child(productId).setValue(hashMap1);
                        }
                        progressDialog.dismiss();
                        Toast.makeText(ShopDetailsActivity.this, "Order Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }

    private void loadProductUser() {
        productUserModelsArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(shopUid).child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productUserModelsArrayList.clear();
                        for(DataSnapshot ds : snapshot.getChildren()){
                            ProductUserModels models = ds.getValue(ProductUserModels.class);
                            Log.d(TAG, "onDataChange: "+ models.getProductId());
                            productUserModelsArrayList.add(models);
                        }

                        Log.d(TAG, "onDataChange: "+ productUserModelsArrayList.size());
                        binding.productUserRec.setLayoutManager(new LinearLayoutManager(ShopDetailsActivity.this));
                        adapter = new ProductUserAdapter(ShopDetailsActivity.this, productUserModelsArrayList);
                        binding.productUserRec.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void loadShopInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(shopUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String address = ""+ snapshot.child("address").getValue();
                            String phone = ""+ snapshot.child("phone").getValue();
                            String email = ""+ snapshot.child("email").getValue();
                            String open = ""+snapshot.child("open").getValue();
                            String imageShop = ""+ snapshot.child("imageProfile").getValue();
                            deliveryFee = ""+ snapshot.child("deliveryFee").getValue();
                            shopName = ""+ snapshot.child("shopName").getValue();

                            binding.addressTv.setText(address);
                            binding.phoneTv.setText(phone);
                            binding.emailTv.setText(email);
                            binding.shopNameTv.setText(shopName);
                            binding.deliveryFeeTv.setText("Delivery Fee : " + deliveryFee);

                            if(open.equals("open")){
                                binding.openTv.setText("Open");
                            }else{
                                binding.openTv.setText("Closed");
                            }

                            try{
                                Glide.with(ShopDetailsActivity.this).load(imageShop).into(binding.shopImv);
                            } catch( Exception e){

                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}