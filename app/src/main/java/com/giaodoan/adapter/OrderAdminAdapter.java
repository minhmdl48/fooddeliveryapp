package com.giaodoan.adapter;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.giaodoan.Controller.OrderController;
import com.giaodoan.R;
import com.giaodoan.databinding.ItemAdminOrderBinding;
import com.giaodoan.model.ItemOrder;
import com.giaodoan.model.Order;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class OrderAdminAdapter  extends RecyclerView.Adapter<OrderAdminAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Order> list;

    private CollectionReference orderRef;

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
        orderRef = FirebaseFirestore.getInstance().collection("orders");
        String id = currentItem.getOid();
        String price = String.valueOf(currentItem.getPrice());
        String time = currentItem.getOrdertime();
        String status = String.valueOf(currentItem.getStatus());

        if(id != null) {
            holder.binding.orderIdAdmin.setText(id);
        }
        if(time != null) {
            holder.binding.orderTvTimeAdmin.setText(time);
        }
        if (price != null) {
            holder.binding.orderTvTotalPriceAdmin.setText(price + "đ");
        }
        else {
            holder.binding.orderTvTotalPriceAdmin.setText("Không xác định");
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
        else {
            holder.binding.orderTvStatusAdmin.setText("Không xác định");
        }
        boolean isNull = status.equals("null");
        Log.d("StatusAdapter", String.valueOf(isNull));
        holder.binding.adminAccept.setOnClickListener(v -> {
            if(status.equals("1") || isNull){
                Log.d("StatusAdapter",id);
                new OrderController().AdminAcceptOrder(id,context,orderRef);

            }
            else {
                Toast.makeText(context, "Chỉ có thể chấp nhận đơn hàng ở trạng thái mới tạo", Toast.LENGTH_SHORT).show();
            }
        });
        holder.binding.adminCancel.setOnClickListener(v -> {
            if(status.equals("1") || isNull){
                new OrderController().AdminCancelOrder(id,context,orderRef);
            }
            else {
                Toast.makeText(context, "Chỉ có thể hủy đơn hàng ở trạng thái mới tạo", Toast.LENGTH_SHORT).show();

            }
        });
        holder.binding.adminFinish.setOnClickListener(v -> {
            if(status.equals("2") || isNull){
                new OrderController().AdminFinishOrder(id,context,orderRef);

            }
            else  {
                Toast.makeText(context, "Chỉ có thể hoàn thành đơn hàng ở trạng thái đã chấp nhận", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}


