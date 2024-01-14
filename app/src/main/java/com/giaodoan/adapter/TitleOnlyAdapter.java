package com.giaodoan.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.giaodoan.databinding.RvTitileOnlyBinding;
import com.giaodoan.model.ItemOrder;

import java.util.ArrayList;

public class TitleOnlyAdapter  extends RecyclerView.Adapter<TitleOnlyAdapter.ViewHolder> {
        private Context context;
        private ArrayList<ItemOrder> list;
        public TitleOnlyAdapter(Context context, ArrayList<ItemOrder> list) {
            this.context = context;
            this.list = list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            RvTitileOnlyBinding binding;
            public ViewHolder(RvTitileOnlyBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RvTitileOnlyBinding binding = RvTitileOnlyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ItemOrder currentItem = list.get(position);
            holder.binding.titleTitle.setText(currentItem.getName());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }


