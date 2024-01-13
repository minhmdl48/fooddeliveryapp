package com.giaodoan.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.giaodoan.R;
import com.giaodoan.databinding.ItemAdminOrderBinding;
import com.giaodoan.model.Order;

import java.util.ArrayList;

public class OrderAdminAdapter  extends RecyclerView.Adapter<OrderAdminAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Order> list;

    public OrderAdminAdapter(Context context, ArrayList<Order> list) {
        this.context = context;
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemAdminOrderBinding binding;

        public ViewHolder(ItemAdminOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemAdminOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order currentItem = list.get(position);

        String id = currentItem.getOid();
        String quantity = currentItem.getQuantity();
        String time = currentItem.getOrdertime();
        String status = currentItem.getStatus();

        if(id != null) {
            holder.binding.orderIdAdmin.setText(currentItem.getOid());
        }
        if(time != null) {
            holder.binding.orderTvTimeAdmin.setText(currentItem.getOrdertime());
        }
        if (quantity != null) {
            holder.binding.orderTvTotalPriceAdmin.setText(currentItem.getPrice());
        }
        if (status != null) {
            String strStatus = "";
            switch (status) {
                case "1":
                    strStatus = "Đơn mới";
                    break;
                case "2":
                    strStatus = "Đơn đã chấp nhận";
                    break;
                case "3":
                    strStatus = "Đơn đã hủy";
                    break;
                case "4":
                    strStatus = "Đơn hoàn thành";
                    break;
                default:
                    strStatus = "Không xác định";
                    break;
            }
            holder.binding.orderTvStatusAdmin.setText(strStatus);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}


