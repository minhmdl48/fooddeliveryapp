package com.giaodoan.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.giaodoan.R;
import com.giaodoan.databinding.RvOrderDetailBinding;
import com.giaodoan.model.ItemOrder;

import java.util.ArrayList;

public class DetailOrderAdapter extends RecyclerView.Adapter<com.giaodoan.adapter.DetailOrderAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ItemOrder> list;

    public DetailOrderAdapter(Context context, ArrayList<ItemOrder> list) {
        this.context = context;
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RvOrderDetailBinding binding;

        public ViewHolder(RvOrderDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RvOrderDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemOrder currentItem = list.get(position);

        holder.binding.orderItemName.setText(currentItem.getName());
        Log.d("DetailOrderAdapter", "onBindViewHolder: "+currentItem.getName());
        holder.binding.orderItemQuantity.setText("x"+String.valueOf(currentItem.getQuantity()));
        holder.binding.orderItemPrice.setText(context.getString(R.string.item_price, currentItem.getPrice()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}


