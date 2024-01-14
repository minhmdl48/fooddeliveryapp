package com.giaodoan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.giaodoan.databinding.RvSearchBinding;
import com.giaodoan.model.Item;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private List<Item> items = new ArrayList<>();
    private Context context;
    private ProductOnClickInterface productClickInterface;

    public SearchAdapter(Context context, ProductOnClickInterface productClickInterface) {
        this.context = context;
        this.productClickInterface = productClickInterface;
    }

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        compositeDisposable.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RvSearchBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item cheese = items.get(position);

        Glide.with(context)
                .load(cheese.getImageUrl())
                .into(holder.binding.searchImage);
        holder.binding.searchJudul.setText(cheese.getName());

        holder.itemView.setOnClickListener(view -> productClickInterface.onClickProduct(items.get(position)));
    }

    public interface ProductOnClickInterface {
        void onClickProduct(Item item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RvSearchBinding binding;

        ViewHolder(RvSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}


