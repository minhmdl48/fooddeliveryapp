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
import com.giaodoan.adapter.DetailOrderAdapter;
import com.giaodoan.databinding.DetailOrderFragmentBinding;
import com.giaodoan.model.ItemOrder;
import com.giaodoan.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class DetailOrderFragment extends Fragment {
    private DetailOrderFragmentBinding binding;
    private ArrayList<ItemOrder> itemorderList;
    private FirebaseAuth auth;
    private String oid;
    private String orderTime;
    private String totalPrice;
    private DetailOrderAdapter adapter;

    private final CollectionReference orderDatabaseReference = FirebaseFirestore.getInstance().collection("orders");
    private final DatabaseReference orderedDatabase= FirebaseDatabase.getInstance("https://acofee-order-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("itemordered");

    public DetailOrderFragment() {
        super(R.layout.detail_order_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DetailOrderFragmentBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = DetailOrderFragmentBinding.bind(view);
        auth = FirebaseAuth.getInstance();
        oid = requireArguments().getString("oid");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        itemorderList = new ArrayList<>();

        retrieveOrderList();
        adapter = new DetailOrderAdapter(requireContext(), itemorderList);
        binding.orderTvCode.setText("Mã đơn hàng: "+oid);
        retrieveOrderInfo();

        binding.orderBackButton.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.rvOrderDetail.setAdapter(adapter);
        binding.rvOrderDetail.setLayoutManager(layoutManager);
    }
    private void retrieveOrderInfo() {
        orderDatabaseReference
                .whereEqualTo("oid", oid)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        Order order = documentSnapshot.toObject(Order.class);
                        orderTime = order.getOrdertime();
                        totalPrice =order.getPrice();
                        // Use orderTime as needed
                        Log.d("DetailOrderFragment", "time: "+orderTime);
                        binding.orderTvTime.setText("Thời gian đặt hàng: "+orderTime);
                        binding.tvTotal.setText("Tổng tiền: "+"đ"+totalPrice);

                    }
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }
    private void retrieveOrderList() {
        orderedDatabase.child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).child(oid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            ItemOrder itemorder = itemSnapshot.getValue(ItemOrder.class);
                            itemorderList.add(itemorder);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(requireContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}



