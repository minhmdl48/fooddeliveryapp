package com.giaodoan.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.giaodoan.databinding.RvCartBinding;
import com.giaodoan.model.ItemOrder;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ItemOrder> list;
    private String itemprice;
    private OnLongClickRemove onLongClickRemove;
    private CollectionReference collectionReference= FirebaseFirestore.getInstance().collection("products");
    private OnQuantityChangeListener onQuantityChangeListener;

    public CartAdapter(Context context, ArrayList<ItemOrder> list, OnLongClickRemove onLongClickRemove, OnQuantityChangeListener onQuantityChangeListener) {
        this.context = context;
        this.list = list;
        this.onLongClickRemove = onLongClickRemove;
        this.onQuantityChangeListener = onQuantityChangeListener;
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
        ItemOrder currentItem = list.get(position);
        collectionReference.whereEqualTo("id", currentItem.getPid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            itemprice = document.getString("price");
                            if (itemprice != null && !itemprice.isEmpty()) {
                                int x = safeParseInt(itemprice, 0);
                            Log.d("CartAdapter", "itemprice: " + itemprice);
                            holder.binding.cartItemPrice.setText(itemprice);
                            // rest of your code that depends on itemprice
                            holder.binding.addButton.setOnClickListener(v -> {
                                currentItem.setQuantity(currentItem.getQuantity() + 1);
                                holder.binding.cartItemQuantity.setText(String.valueOf(currentItem.getQuantity()));
                                currentItem.setPrice(String.valueOf(x*currentItem.getQuantity()));
                                onQuantityChangeListener.onQuantityChange();
                            });
                            holder.binding.removeButton.setOnClickListener(v -> {
                                if (currentItem.getQuantity() > 1) {

                                    currentItem.setQuantity(currentItem.getQuantity() - 1);
                                    currentItem.setPrice(String.valueOf(x*currentItem.getQuantity()));
                                    holder.binding.cartItemQuantity.setText(String.valueOf(currentItem.getQuantity()));
                                    onQuantityChangeListener.onQuantityChange();
                                }
                            });
                        }}
                    } else {
                        Log.d("CartAdapter", "Error getting documents: ", task.getException());
                    }
                });

        Glide.with(context).load(currentItem.getImageUrl()).into(holder.binding.cartImage);
        holder.binding.cartItemName.setText(currentItem.getName());
        holder.binding.cartItemQuantity.setText(String.valueOf(currentItem.getQuantity()));

        holder.itemView.setOnLongClickListener(v -> {
            onLongClickRemove.onLongRemove(currentItem, position);
            return true;
        });
    }
    public ArrayList<ItemOrder> getUpdatedList(){
        return list;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnLongClickRemove {
        void onLongRemove(ItemOrder item, int position);
    }
    public interface OnQuantityChangeListener{
        void onQuantityChange();
    }
    private int safeParseInt(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}