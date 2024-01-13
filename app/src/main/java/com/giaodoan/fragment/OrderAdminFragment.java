package com.giaodoan.fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.giaodoan.Controller.OrderController;
import com.giaodoan.R;
import com.giaodoan.adapter.OrderAdminAdapter;
import com.giaodoan.databinding.AdminHistoryBinding;
import com.giaodoan.model.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OrderAdminFragment  extends Fragment {
    private AdminHistoryBinding binding;
    private ArrayList<Order> orderList;
    private FirebaseAuth auth;
    private OrderAdminAdapter adapter;

    private CollectionReference orderDatabaseReference = FirebaseFirestore.getInstance().collection("orders");

    public OrderAdminFragment() {
        super(R.layout.order_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AdminHistoryBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = AdminHistoryBinding.bind(view);
        auth = FirebaseAuth.getInstance();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        Log.d("OrderFragment" , "chan vl");
        orderList = new ArrayList<>();
//        orderList = new OrderController().AdminGetOrders(orderDatabaseReference);
        AdminGetOrders();
        Log.d("OrderFragmentSize" , String.valueOf(orderList.size()));
        adapter = new OrderAdminAdapter(requireContext(), orderList);
        Log.d("OrderFragment" , String.valueOf(adapter.getItemCount()));
        binding.viewListAdmin.setAdapter(adapter);
        binding.viewListAdmin.setLayoutManager(layoutManager);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void AdminGetOrders() {
        orderDatabaseReference.get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot item : querySnapshot) {
                        Order order = item.toObject(Order.class);

                        orderList.add(order);
                        Log.d("OrderAdminFragment" , String.valueOf(orderList.size()));
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());

    }
}


