package com.example.grocery_app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.grocery_app.Constants;
import com.example.grocery_app.R;
import com.example.grocery_app.activities.EditProductActivity;
import com.example.grocery_app.databinding.ActivityProductDetailsSellerBinding;
import com.example.grocery_app.databinding.RowProductSellerBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    RowProductSellerBinding binding;
    ActivityProductDetailsSellerBinding bindingBottomSheet;
    final  static  String TAG ="TAG_IMAGE";

    Context context;

    FirebaseAuth firebaseAuth;
    public ArrayList<Constants.ProductModels> productModelsArrayList, filterList;
//    private FilterProduct filterProduct;

    public ProductAdapter(Context context, ArrayList<Constants.ProductModels> productModelsArrayList) {
        this.context = context;
        this.productModelsArrayList = productModelsArrayList;
//        this.filterList = productModelsArrayList;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowProductSellerBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Constants.ProductModels model = productModelsArrayList.get(position);
        String discountAvailable = model.getDiscountAvailable();
        String quantity = model.getQuantity();
        String discountedPrice = model.getDiscountedPrice();
        String discountedNote = model.getDiscountedNote();
        String originalPrice = model.getOriginalPrice();
        String title = model.getTitle();
        String productImage = model.getProductImage();


        holder.title.setText(title);
        holder.originalPrice.setText("$"+originalPrice);
        holder.discountedNote.setText(discountedNote);
        holder.discountedPrice.setText("$"+ discountedPrice);
        holder.quantity.setText(quantity);

        if(discountAvailable.equals("true")){
            holder.discountedPrice.setVisibility(View.VISIBLE);
            holder.discountedNote.setVisibility(View.VISIBLE);
            holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.discountedPrice.setVisibility(View.GONE);
            holder.discountedNote.setVisibility(View.GONE);
        }

        Log.d(TAG, "onBindViewHolder: "+ productImage);
        try{
            Glide.with(context).load(productImage).placeholder(R.drawable.baseline_add_shopping_cart_24_colorprimary).into(binding.productImage);
        }catch(Exception e){
            Glide.with(context).load(R.drawable.baseline_add_shopping_cart_24_colorprimary).into(binding.productImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsBottomSheet(model);
            }
        });

    }

    private void detailsBottomSheet(Constants.ProductModels model) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bindingBottomSheet = ActivityProductDetailsSellerBinding.inflate(LayoutInflater.from(context));
        bottomSheetDialog.setContentView(bindingBottomSheet.getRoot());

        String discountAvailable = model.getDiscountAvailable();
        String quantity = model.getQuantity();
        String discountedPrice = model.getDiscountedPrice();
        String discountedNote = model.getDiscountedNote();
        String originalPrice = model.getOriginalPrice();
        String des = model.getDescription();
        String title = model.getTitle();
        String productId = model.getProductId();
        String category = model.getCategory();
        String productImage = model.getProductImage();


        bindingBottomSheet.productTitle.setText(title);
        bindingBottomSheet.productCatrgory.setText(category);
        bindingBottomSheet.productDescription.setText(des);
        bindingBottomSheet.productQuantity.setText(quantity);
        bindingBottomSheet.originalPriceTv.setText(originalPrice);
        bindingBottomSheet.discountedNoteTv.setText(discountedPrice);
        bindingBottomSheet.discountedNoteTv.setText(discountedNote);

        if(discountAvailable.equals("true")){
            bindingBottomSheet.discountedNoteTv.setVisibility(View.VISIBLE);
            bindingBottomSheet.discountedPriceTv.setVisibility(View.VISIBLE);
            bindingBottomSheet.originalPriceTv.setPaintFlags(binding.originalPriceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            bindingBottomSheet.discountedPriceTv.setVisibility(View.GONE);
            bindingBottomSheet.discountedNoteTv.setVisibility(View.GONE);
        }

        try{
            Glide.with(context).load(productImage).placeholder(R.drawable.baseline_add_shopping_cart_24_white).into(bindingBottomSheet.productSiv);
        }catch (Exception e){
            Glide.with(context).load(R.drawable.baseline_add_shopping_cart_24_white).into(bindingBottomSheet.productSiv);
        }

        bottomSheetDialog.show();

        bindingBottomSheet.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bindingBottomSheet.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditProductActivity.class);
                intent.putExtra("productId", productId);
                context.startActivity(intent);
            }
        });

        bindingBottomSheet.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Delete")
                        .setMessage("Are you sure you want to delete product " + title+ " ?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteProduct(productId, bottomSheetDialog);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bottomSheetDialog.dismiss();
                            }
                        }).show();

            }
        });
    }

    private void deleteProduct(String productId, BottomSheetDialog bottomSheetDialog) {
        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(""+firebaseAuth.getUid()).child("Products").child(productId)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Product deleted...", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return productModelsArrayList.size();
    }
//    public Filter getFilter() {
//        if( filterProduct == null){
//            filterProduct = new FilterProduct(this, filterList);
//        }
//        return filterProduct;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, quantity, originalPrice, discountedPrice, discountedNote;
        ShapeableImageView productIv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = binding.titleTv;
            quantity = binding.quantityTv;
            originalPrice = binding.originalPriceTv;
            discountedNote = binding.discountedNoteTv;
            discountedPrice = binding.discountedPriceTv;
            productIv = binding.productImage;
        }
    }
}
