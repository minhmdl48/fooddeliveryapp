package com.giaodoan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
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
        private OnItemClickListener onItemClickListener;
        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public OrderAdapter(Context context, ArrayList<Order> list) {
            this.context = context;
            this.list = list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private RvOrderBinding binding;

            public ViewHolder(RvOrderBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                // Set the click listener for the item view
                itemView.setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                });
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

            holder.binding.orderTitle.setText(context.getString(R.string.order_title)+"  " + currentItem.getOid());
            holder.binding.orderTime.setText(R.string.order_time);
            holder.binding.orderTvTime.setText(currentItem.getOrdertime());
            holder.binding.orderStatus.setText("Trạng thái : " + currentItem.getStatus());

            String totalPriceText = context.getString(R.string.item_price, currentItem.getPrice());
            holder.binding.orderTvTotalPrice.setText(totalPriceText);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
        public interface OnItemClickListener {
            void onItemClick(int position);
        }

    }


