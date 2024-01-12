package com.giaodoan.fragment;

import android.app.AlertDialog;
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

import com.giaodoan.adapter.TitleOnlyAdapter;
import com.giaodoan.databinding.CheckoutFragmentBinding;
import com.giaodoan.model.ItemOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CheckoutFragment extends Fragment {
    private CheckoutFragmentBinding binding;
    private ArrayList<ItemOrder> cartList;
    private FirebaseAuth auth;
    private TitleOnlyAdapter adapter;

    private int totalPrice = 0;

    private DatabaseReference cartDatabase= FirebaseDatabase.getInstance("https://acofee-order-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("carts");

    private AlertDialog.Builder builder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = CheckoutFragmentBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = CheckoutFragmentBinding.bind(view);
        auth = FirebaseAuth.getInstance();

        // Initialize cartList
        cartList = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView
        adapter = new TitleOnlyAdapter(getContext(), cartList);
        binding.rvTitle.setAdapter(adapter);
        binding.rvTitle.setLayoutManager(new LinearLayoutManager(getContext()));

        // Call retrieveCartItems
        retrieveCartItems();
        Log.d("CheckoutFragment", "onViewCreated: " + totalPrice);

        binding.checkoutButton.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Mã nhận hàng")
                    .setMessage("Vui lòng đưa mã cho nhân viên tại quầy để nhận đồ uống")
                    .setPositiveButton("OK", (dialog, id) -> dialog.cancel())
                    .show();
            Toast.makeText(getActivity(), "Đã thanh toán thành công" + totalPrice, Toast.LENGTH_LONG).show();

            if (!cartList.isEmpty()) {
                deleteCart();
                cartList.clear();
                adapter.notifyDataSetChanged();
            }

            Navigation.findNavController(v).popBackStack();
        });
    }

    private void retrieveCartItems() {
        cartDatabase.child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            ItemOrder cartProduct = itemSnapshot.getValue(ItemOrder.class);

                            cartList.add(cartProduct);

                            totalPrice += Integer.parseInt(Objects.requireNonNull(cartProduct).getPrice());

                            adapter.notifyDataSetChanged();
                        }
                        binding.txtViewPriceTotal.setText("đ "+totalPrice );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteCart() {
        cartDatabase.child(auth.getCurrentUser().getUid())
                .removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Xóa giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

}
