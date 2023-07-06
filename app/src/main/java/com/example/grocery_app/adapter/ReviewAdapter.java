package com.example.grocery_app.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.grocery_app.R;
import com.example.grocery_app.databinding.RowUserReviewBinding;
import com.example.grocery_app.models.ReviewModels;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private RowUserReviewBinding binding;
    private Context context;
    private ArrayList<ReviewModels> reviewModelsArrayList;

    public ReviewAdapter(Context context, ArrayList<ReviewModels> reviewModelsArrayList) {
        this.context = context;
        this.reviewModelsArrayList = reviewModelsArrayList;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowUserReviewBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        ReviewModels models = reviewModelsArrayList.get(position);
         String rating = models.getRating();
         String reviewBy = models.getReviewBy();
         String reviewTo = models.getReviewTo();
         String timestamp = models.getTimestamp();
         String review = models.getReview();

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timestamp));
        String date = DateFormat.format("dd/MM/yy",cal).toString();

         holder.reviewTv.setText(review);
         holder.rating.setRating(Float.parseFloat(rating));
         holder.dateTv.setText(date);
         loadUserDetail(reviewBy, holder);
    }

    private void loadUserDetail(String reviewBy, ViewHolder holder) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(reviewBy)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String profileImage = ""+ snapshot.child("profileImage").getValue();
                String name = ""+ snapshot.child("fullName").getValue();

                holder.nameTv.setText(name);
                try{
                    Glide.with(context).load(profileImage).placeholder(R.drawable.baseline_account_circle_24).into(holder.imageUser);
                }catch(Exception e){
                    Glide.with(context).load(R.drawable.baseline_account_circle_24).into(holder.imageUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return reviewModelsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageUser;
        RatingBar rating;
        TextView nameTv, dateTv, reviewTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageUser = binding.profileImageSiv;
            rating = binding.ratingBar;
            nameTv = binding.nameTv;
            dateTv = binding.dateReviewTv;
            reviewTv = binding.reviewTv;
        }
    }
}
