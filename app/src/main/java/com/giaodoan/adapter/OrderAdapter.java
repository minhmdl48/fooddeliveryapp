package com.giaodoan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.giaodoan.R;
import com.giaodoan.databinding.RvOrderBinding;
import com.giaodoan.model.Order;

import java.util.ArrayList;

public class OrderAdapter  extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
        private Context context;
        private ArrayList<Order> list;

        public OrderAdapter(Context context, ArrayList<Order> list) {
            this.context = context;
            this.list = list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private RvOrderBinding binding;

            public ViewHolder(RvOrderBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(RvOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Order currentItem = list.get(position);

            holder.binding.orderTitle.setText(context.getString(R.string.order_title) + currentItem.getOid());
            holder.binding.orderItemQuantity.setText(R.string.order_item_quantity);
            holder.binding.orderTvItemQuantity.setText(currentItem.getQuantity());
            holder.binding.orderStatus.setText("Trạng thái : " + currentItem.getStatus());

            holder.binding.orderTvTotalPrice.setText(R.string.item_price + currentItem.getPrice());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

