package com.example.grocery_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.grocery_app.databinding.RowShopBinding;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter <ShopAdapter.ViewHolder> {

    private RowShopBinding binding;
    private Context context;
    private ArrayList<ShopModels> shopModelsArrayList;

    public ShopAdapter(Context context, ArrayList<ShopModels> shopModelsArrayList) {
        this.context = context;
        this.shopModelsArrayList = shopModelsArrayList;
    }

    @NonNull
    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= RowShopBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ShopAdapter.ViewHolder holder, int position) {
        ShopModels models = shopModelsArrayList.get(position);

        String address = models.getAddress();
        String city = models.getCity();
        String state = models.getState();
        String shopOpen = models.getShopOpen();
        String uid = models.getUid();
        String timestamp = models.getTimestamp();
        String shopName = models.getShopName();
        String email = models.getEmail();
        String deliveryFee = models.getDeliveryFee();
        String country = models.getCountry();
        String imageProfile = models.getImageProfile();
        String online = models.getOnline();
        String accountType = models.getUserType();
        String fullName = models.getFullName();
        String phone = models.getPhone();


        holder.address.setText(address);
        holder.shopName.setText(shopName);
        holder.phone.setText(phone);

        if(online.equals("true")){
            holder.online.setVisibility(View.VISIBLE);
        }else{
            holder.online.setVisibility(View.GONE);
        }

        if(shopOpen.equals("open")){
            holder.open.setVisibility(View.VISIBLE);
            holder.shopClosed.setVisibility(View.GONE);
        }else{
            holder.open.setVisibility(View.GONE);
            holder.shopClosed.setVisibility(View.VISIBLE);
        }
        try {
            Glide.with(context).load(imageProfile).placeholder(R.drawable.baseline_account_circle_24).into(holder.imageProfile);
        } catch (Exception e){
            Glide.with(context).load(R.drawable.baseline_account_circle_24).into(holder.imageProfile);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShopDetailsActivity.class);
                intent.putExtra("shopUid", ""+uid);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopModelsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ShapeableImageView imageProfile;
        ImageView online;
        TextView address, shopName, shopClosed, phone, open;
        RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            online = binding.onlineImv;
            imageProfile = binding.shopImageView;
            address = binding.addressTv;
            shopName = binding.shopNameTv;
            shopClosed = binding.closedTv;
            phone = binding.phoneTv;
            ratingBar = binding.ratingBar;
            open = binding.openTv;
        }
    }
}
