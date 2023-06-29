package com.example.grocery_app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocery_app.databinding.RowOrderDetailsBinding;
import com.example.grocery_app.databinding.RowUserOrderItemBinding;
import com.example.grocery_app.models.OrderItemsModels;
import com.example.grocery_app.models.OrderUserModels;

import java.util.ArrayList;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<OrderItemsModels> orderItemsModelsArrayList;

    private RowOrderDetailsBinding binding;
    public static final String  TAG = "CHECK_OB" ;

    public OrderItemsAdapter(Context context, ArrayList<OrderItemsModels> orderItemsModelsArrayList) {
        this.context = context;
        this.orderItemsModelsArrayList = orderItemsModelsArrayList;
    }

    @NonNull
    @Override
    public OrderItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowOrderDetailsBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding.getRoot());
    }


    @Override
    public void onBindViewHolder(@NonNull OrderItemsAdapter.ViewHolder holder, int position) {
        OrderItemsModels models = orderItemsModelsArrayList.get(position);
        String title = models.getName();
        String cost = models.getCost();
        String priceEach = models.getPrice();
        String quantity = models.getQuantity();
        String unit = models.getUnit();

        holder.quantity.setText(quantity);
        holder.cost.setText(cost);
        holder.priceEach.setText(priceEach +" / " + unit );
        holder.quantity.setText("["+quantity+"]");
        holder.title.setText(title);
    }

    @Override
    public int getItemCount() {
        return orderItemsModelsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, cost, quantity, priceEach;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = binding.titleItemTv;
            cost = binding.itemPriceTv;
            priceEach = binding.itemPriceEachTv;
            quantity = binding.itemQuantityTv;
        }
    }
}
