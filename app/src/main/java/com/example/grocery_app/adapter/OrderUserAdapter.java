package com.example.grocery_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocery_app.R;
import com.example.grocery_app.activities.OrderDetailsUsersActivity;
import com.example.grocery_app.databinding.RowUserOrderItemBinding;
import com.example.grocery_app.models.OrderUserModels;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class OrderUserAdapter extends RecyclerView.Adapter<OrderUserAdapter.ViewHolder> {

    RowUserOrderItemBinding binding;
    private Context context;
    private ArrayList<OrderUserModels> orderUserArrayList;

    public OrderUserAdapter(Context context, ArrayList<OrderUserModels> orderUserArrayList) {
        this.context = context;
        this.orderUserArrayList = orderUserArrayList;
    }

    @NonNull
    @Override
    public OrderUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowUserOrderItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull OrderUserAdapter.ViewHolder holder, int position) {
        OrderUserModels models = orderUserArrayList.get(position);

        loadShopInfo(models, holder);

        String orderId = models.getOrderId();
        String orderTime = models.getOrderTime();
        String orderCost = models.getOrderCost();
        String orderStatus = models.getOrderStatus();
        String orderTo = models.getOrderTo();
        String orderBy = models.getOrderBy();

        holder.orderIdTv.setText("OrderId :" + orderId);
        holder.statusTv.setText(orderStatus);
        if(orderStatus.equals("In Process")){
            holder.orderIdTv.setTextColor(context.getResources().getColor(R.color.green));
        }else if(orderStatus.equals("Completed")){
            holder.orderIdTv.setTextColor(context.getResources().getColor(R.color.blue));
        }else{
            holder.orderIdTv.setTextColor(context.getResources().getColor(R.color.red));
        }

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(orderTime));
        String date = DateFormat.format("dd/MM/yy",cal).toString();

        holder.dateTv.setText(date);
        holder.amountTv.setText("$"+orderCost);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailsUsersActivity.class);
                intent.putExtra("orderId",orderId);
                intent.putExtra("orderTo", orderTo);
                context.startActivity(intent);
            }
        });
    }

    private void loadShopInfo(OrderUserModels models, ViewHolder holder) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(""+models.getOrderTo())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String shopName =""+ snapshot.child("shopName").getValue();
                        holder.shopNameTv.setText(shopName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return orderUserArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTv, dateTv, amountTv, shopNameTv, statusTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTv = binding.orderIdTv;
            dateTv = binding.dateTv;
            amountTv = binding.amountTv;
            shopNameTv = binding.shopNameTv;
            statusTv = binding.statusTv;

        }
    }
}
