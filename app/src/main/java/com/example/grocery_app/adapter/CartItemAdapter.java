package com.example.grocery_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocery_app.databinding.RowCartItemBinding;
import com.example.grocery_app.models.CartItemModels;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {

    RowCartItemBinding binding;
    private Context context;
    private ArrayList<CartItemModels> cartItemModelsArrayList;

    private FirebaseAuth firebaseAuth;

    public CartItemAdapter(Context context, ArrayList<CartItemModels> cartItemModelsArrayList) {
        this.context = context;
        this.cartItemModelsArrayList = cartItemModelsArrayList;
    }

    @NonNull
    @Override
    public CartItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowCartItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.ViewHolder holder, int position) {
        CartItemModels models = cartItemModelsArrayList.get(holder.getAdapterPosition());

        String id = models.getItemId();
        String price = models.getItemPrice();
        String name = models.getItemName();
        String productId = models.getItemProductId();
        String quantity = models.getItemQuantity();
        String priceEach = models.getItemPriceEach();
        String timestamp = models.getTimestamp();

        holder.titleTv.setText(name);
        holder.priceTv.setText(price);
        holder.quantityTv.setText(" [ "+quantity+" ]");
        holder.priceEachTv.setText("$"+priceEach);

        holder.removeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("AddToCart");
                ref.child(""+firebaseAuth.getUid()).child(id)
                        .removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(context, "Delete Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

    }

    @Override
    public int getItemCount() {
        return cartItemModelsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTv, priceEachTv, priceTv, quantityTv, removeTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = binding.titleItemTv;
            priceEachTv = binding.itemPriceEachTv;
            priceTv = binding.itemPriceTv;
            quantityTv = binding.itemQuantityTv;
            removeTv = binding.removeItemTv;
        }
    }
}
