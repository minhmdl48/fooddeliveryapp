package com.giaodoan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.giaodoan.R;
import com.giaodoan.databinding.PopularItemBinding;
import com.giaodoan.model.Item;

import java.util.List;

public class ItemsPopularAdapter extends RecyclerView.Adapter<ItemsPopularAdapter.ViewHolder> {
    private Context context;
    private List<Item> list;
    private ProductOnClickInterface productClickInterface;

    public ItemsPopularAdapter(Context context, List<Item> list, ProductOnClickInterface productClickInterface) {
        this.context = context;
        this.list = list;
        this.productClickInterface = productClickInterface;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PopularItemBinding binding;

        public ViewHolder(PopularItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PopularItemBinding binding = PopularItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = list.get(position);
        String image = item.getImageUrl();

        Glide.with(context).load(image).into(holder.binding.popImage);
        holder.binding.popTitle.setText(item.getName());
        holder.binding.popPrice.setText(context.getString(R.string.item_price, item.getPrice()));

        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                productClickInterface.onClickProduct(list.get(pos));
            }
        });
    }
    public interface ProductOnClickInterface {
        void onClickProduct(Item item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}



