package com.giaodoan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.giaodoan.R;
import com.giaodoan.databinding.AllItemBinding;
import com.giaodoan.model.Item;

import java.util.List;

public class ItemsShowAdapter extends RecyclerView.Adapter<ItemsShowAdapter.ViewHolder> {
    private Context context;
    private List<Item> list;
    private ProductOnClickInterface productClickInterface;

    public ItemsShowAdapter(Context context, List<Item> list, ProductOnClickInterface productClickInterface) {
        this.context = context;
        this.list = list;
        this.productClickInterface = productClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AllItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = list.get(position);
        String image = item.getImageUrl();

        holder.binding.allJudul.setText(item.getName());
        holder.binding.allPrice.setText(context.getString(R.string.item_price, item.getPrice()));
        Glide.with(context).load(image).into(holder.binding.allImage);

        holder.itemView.setOnClickListener(view -> productClickInterface.onClickProduct(list.get(position)));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        AllItemBinding binding;

        ViewHolder(AllItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface ProductOnClickInterface {
        void onClickProduct(Item item);
    }
}




