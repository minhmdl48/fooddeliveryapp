package com.giaodoan.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.giaodoan.R;
import com.giaodoan.adapter.OrderAdapter;
import com.giaodoan.databinding.OrderFragmentBinding;
import com.giaodoan.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class OrderFragment  extends Fragment {
        private OrderFragmentBinding binding;
        private ArrayList<Order> orderList;
        private FirebaseAuth auth;
        private OrderAdapter adapter;

        private CollectionReference orderDatabaseReference = FirebaseFirestore.getInstance().collection("orders");

        public OrderFragment() {
            super(R.layout.order_fragment);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            binding = OrderFragmentBinding.inflate(inflater);
            return binding.getRoot();
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            binding = OrderFragmentBinding.bind(view);
            auth = FirebaseAuth.getInstance();

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            orderList = new ArrayList<>();

            retriveOrderList();
            adapter = new OrderAdapter(requireContext(), orderList);
            adapter.setOnItemClickListener(position -> {
                Order clickedOrder = orderList.get(position);
                String oid = clickedOrder.getOid();

                Bundle bundle = new Bundle();
                bundle.putString("oid", oid);

                DetailOrderFragment detailOrderFragment = new DetailOrderFragment();
                detailOrderFragment.setArguments(bundle);

                Navigation.findNavController(view).navigate(R.id.action_orderFragment_to_orderDetailFragment, bundle);
            });
            binding.rvOrder.setAdapter(adapter);
            binding.rvOrder.setLayoutManager(layoutManager);

        }

    private void retriveOrderList() {
        orderDatabaseReference
                .whereEqualTo("uid", Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot item : querySnapshot) {
                        Order order = item.toObject(Order.class);

                        orderList.add(order);
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
    }


