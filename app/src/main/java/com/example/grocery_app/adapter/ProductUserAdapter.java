package com.example.grocery_app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.grocery_app.R;
import com.example.grocery_app.databinding.DialogQuantityBinding;
import com.example.grocery_app.databinding.RowProductUserBinding;
import com.example.grocery_app.models.ProductUserModels;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductUserAdapter extends RecyclerView.Adapter<ProductUserAdapter.ViewHolder> {

    private RowProductUserBinding binding;

    private DialogQuantityBinding quantityBinding;
    private Context context;
    FirebaseAuth firebaseAuth;
    private ArrayList<ProductUserModels> productUserModelsArrayList;


    public ProductUserAdapter(Context context, ArrayList<ProductUserModels> productUserModelsArrayList) {
        this.context = context;
        this.productUserModelsArrayList = productUserModelsArrayList;
    }

    @NonNull
    @Override
    public ProductUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowProductUserBinding.inflate(LayoutInflater.from(context), parent, false);
         return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ProductUserAdapter.ViewHolder holder, int position) {
            ProductUserModels models = productUserModelsArrayList.get(position);

            String title = models.getTitle();
            String des = models.getDescription();
            String originalPrice = models.getOriginalPrice();
            String discountedPrice = models.getDiscountedPrice();
            String discountedNote = models.getDiscountedNote();
            String imageProduct = models.getProductImage();
            String discountAvailable = models.getDiscountAvailable();


            binding.productTitle.setText(title);
            binding.productDes.setText(des);
            binding.originalPriceTv.setText(originalPrice +"$");
            binding.discountedNoteTv.setText(discountedNote);
            binding.discountedPriceTv.setText(discountedPrice+"$");

        if(discountAvailable.equals("true")){
            holder.discountedPrice.setVisibility(View.VISIBLE);
            holder.discountedNote.setVisibility(View.VISIBLE);
            holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.discountedPrice.setVisibility(View.GONE);
            holder.discountedNote.setVisibility(View.GONE);
        }

        try{
            Glide.with(context).load(imageProduct).placeholder(R.drawable.baseline_add_shopping_cart_24_colorprimary).into(binding.productSiv);
        }catch (Exception e){
            Glide.with(context).load(R.drawable.baseline_add_shopping_cart_24_colorprimary).into(binding.productSiv);
        }

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuantityDialog(models);
            }
        });
    }

    int count = 0;
    double cost = 0;
    double finalCost = 0;

    private void showQuantityDialog(ProductUserModels models) {
        quantityBinding = DialogQuantityBinding.inflate(LayoutInflater.from(context));

        AlertDialog.Builder quantityDialog =  new AlertDialog.Builder(context);
        quantityDialog.setView(quantityBinding.getRoot());

        String title = models.getTitle();
        String des = models.getDescription();
        String originalPrice = models.getOriginalPrice();
        String discountedPrice = models.getDiscountedPrice();
        String discountedNote = models.getDiscountedNote();
        String imageProduct = models.getProductImage();
        String quantity = models.getQuantity();
        String discountAvailable = models.getDiscountAvailable();
        String productId = models.getProductId();

        quantityBinding.productTitleTv.setText(title);
        quantityBinding.descriptionTv.setText(des);
        quantityBinding.quantityTv.setText(quantity);
        quantityBinding.originalPriceTv.setText("$"+originalPrice);
        quantityBinding.discountedPriceTv.setText("$"+discountedPrice);
        quantityBinding.discountedNoteTv.setText("$"+ discountedNote);
        if(discountAvailable.equals("true")){
            quantityBinding.finalPriceTv.setText("$"+discountedPrice);
        }else{
            quantityBinding.finalPriceTv.setText("$"+originalPrice);
        }

        try{
            Glide.with(context).load(imageProduct).placeholder(R.drawable.baseline_add_shopping_cart_24_white).into(quantityBinding.imageProductSiv);
        }catch (Exception e){
            Glide.with(context).load(R.drawable.baseline_add_shopping_cart_24_colorprimary).into(binding.productSiv);
        }
        String price;
        if(discountAvailable.equals("true")){
            price = models.getDiscountedPrice();
            quantityBinding.discountedPriceTv.setVisibility(View.VISIBLE);
            quantityBinding.discountedNoteTv.setVisibility(View.VISIBLE);
            quantityBinding.originalPriceTv.setPaintFlags(quantityBinding.originalPriceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            quantityBinding.discountedPriceTv.setVisibility(View.GONE);
            quantityBinding.discountedNoteTv.setVisibility(View.GONE);
            price=models.getOriginalPrice();
        }


        cost = Double.parseDouble(price.replaceAll("$",""));
        finalCost = Double.parseDouble(price.replaceAll("$",""));
        count = 1;

        AlertDialog dialog = quantityDialog.create();
        dialog.show();

        quantityBinding.decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count +=1;
                finalCost = finalCost + cost;
                quantityBinding.countTv.setText(""+count);
                quantityBinding.finalPriceTv.setText("$"+finalCost);
            }
        });

        quantityBinding.incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count > 1){
                    count -=1;
                    finalCost = finalCost - cost;
                    quantityBinding.countTv.setText(""+count);
                    quantityBinding.finalPriceTv.setText("$"+finalCost);
                }
            }
        });

        quantityBinding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = quantityBinding.productTitleTv.getText().toString().trim();
                String quantity = quantityBinding.countTv.getText().toString().trim();
                String priceEach = quantityBinding.originalPriceTv.getText().toString().trim().replace("$","");
                String price = quantityBinding.finalPriceTv.getText().toString().trim().replace("S","");
                String unit = quantityBinding.quantityTv.getText().toString().trim();

                addToCart(productId,title,quantity,priceEach,price, unit);
                dialog.dismiss();
            }
        });

    }

    private void addToCart(String productId, String title, String quantity, String priceEach, String price, String unit) {
//        EasyDB easyDB = EasyDB.init(context,"ITEM_DB")
//                .setTableName("ITEM_TABLE")
//                .addColumn(new Column("Item_Id", new String[]{"text","unique"}))
//                .addColumn(new Column("Item_PId", new String[]{"text","not null"}))
//                .addColumn(new Column("Item_Name", new String[]{"text","not null"}))
//                .addColumn(new Column("Item_Price_Each", new String[]{"text","not null"}))
//                .addColumn(new Column("Item_Price", new String[]{"text","not null"}))
//                .addColumn(new Column("Item_Quantity", new String[]{"text","not null"}))
//                .doneTableColumn();
//
//        boolean b = easyDB.addData("Item_Id", itemId)
//                .addData("Item_Pid",productId)
//                .addData("Item_Name", title)
//                .addData("Item_Price_Each",priceEach )
//                .addData("Item_Price",price)
//                .addData("Item_Quantity", quantity)
//                .doneDataAdding();
        firebaseAuth = FirebaseAuth.getInstance();

        String timestamp = String.valueOf(System.currentTimeMillis());
        String itemId = timestamp;
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("itemId", itemId);
        hashMap.put("itemName", title);
        hashMap.put("itemPriceEach", priceEach);
        hashMap.put("itemPrice",price);
        hashMap.put("itemQuantity", quantity);
        hashMap.put("itemProductId", productId);
        hashMap.put("timestamp", timestamp);
        hashMap.put("unit", unit);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("AddToCart");
        ref.child(""+ firebaseAuth.getUid()).child(itemId).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });



    }

    @Override
    public int getItemCount() {
        return productUserModelsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ShapeableImageView productImage;

        TextView title, des, discountedPrice, discountedNote, originalPrice, addToCart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = binding.productSiv;
            title = binding.productTitle;
            des = binding.productDes;
            discountedNote = binding.discountedNoteTv;
            discountedPrice = binding.discountedPriceTv;
            originalPrice = binding.originalPriceTv;
            addToCart = binding.addToCart;
        }
    }
}
