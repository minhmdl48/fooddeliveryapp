package com.giaodoan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.giaodoan.databinding.RvCartBinding;
import com.giaodoan.model.Cart;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Cart> list;
    private OnLongClickRemove onLongClickRemove;

    public CartAdapter(Context context, ArrayList<Cart> list, OnLongClickRemove onLongClickRemove) {
        this.context = context;
        this.list = list;
        this.onLongClickRemove = onLongClickRemove;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RvCartBinding binding;

        public ViewHolder(RvCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvCartBinding binding = RvCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cart currentItem = list.get(position);

        Glide.with(context).load(currentItem.getImg_url()).into(holder.binding.cartImage);
        holder.binding.cartItemName.setText(currentItem.getName());
        holder.binding.cartItemPrice.setText("đ" + currentItem.getPrice());
        holder.binding.cartItemQuantity.setText(String.valueOf(currentItem.getQuantity()));

        holder.itemView.setOnLongClickListener(v -> {
            onLongClickRemove.onLongRemove(currentItem, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnLongClickRemove {
        void onLongRemove(Cart item, int position);
    }
}

