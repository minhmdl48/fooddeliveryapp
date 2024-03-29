package com.giaodoan.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.giaodoan.R;
import com.giaodoan.adapter.CartAdapter;
import com.giaodoan.databinding.CartFragmentBinding;
import com.giaodoan.model.ItemOrder;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CartFragment extends Fragment implements CartAdapter.OnLongClickRemove, CartAdapter.OnQuantityChangeListener {

    private CartFragmentBinding binding;
    private ArrayList<ItemOrder> cartList;
    private FirebaseAuth auth;
    private CartAdapter adapter;

    private int totalPrice = 0;
    private DatabaseReference cartDatabase= FirebaseDatabase.getInstance("https://acofee-order-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("carts");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CartFragmentBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = CartFragmentBinding.bind(view);
        auth = FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigation = requireActivity().findViewById(R.id.bottom_navigationView);
        bottomNavigation.setVisibility(View.VISIBLE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        cartList = new ArrayList<>();

        retrieveCartItems();

        adapter = new CartAdapter(requireContext(), cartList, this,this);
        binding.rvCarts.setAdapter(adapter);
        binding.rvCarts.setLayoutManager(layoutManager);
        binding.deleteCart.setOnClickListener(v -> cartDatabase.child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .removeValue()
                .addOnSuccessListener(aVoid -> {
                    cartList.clear();
                    binding.tvTotalPrice.setText("đ0");
                    adapter.notifyDataSetChanged();

                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show()));
        binding.cartCheckout.setOnClickListener(v -> {
            if (auth.getCurrentUser() != null) {
                if (cartList.isEmpty()) {
                    Toast.makeText(requireActivity(), "Giỏ hàng của bạn đang trống", Toast.LENGTH_LONG).show();
                } else {
                    for (ItemOrder item : cartList) {
                        cartDatabase.child(auth.getCurrentUser().getUid())
                                .child(item.getPid())
                                .setValue(item)
                                .addOnSuccessListener(aVoid -> Log.d("CartFragment", "Cập nhật giỏ hàng thành công"))
                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Cập nhật giỏ hàng thất bại", Toast.LENGTH_SHORT).show());
                    }
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_cartFragment_to_checkoutFragment);
                }
            } else {
                Toast.makeText(requireActivity(), "Bạn cần đăng nhập để đặt món", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void retrieveCartItems() {
        cartDatabase.child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ItemOrder cartProduct = snapshot.getValue(ItemOrder.class);

                            cartList.add(cartProduct);
                            totalPrice += Integer.parseInt(Objects.requireNonNull(cartProduct).getPrice());

                        }
                        adapter.notifyDataSetChanged();
                        Log.d("CartFragment", "totalPrice: " + totalPrice);
                        binding.tvTotalPrice.setText("đ"+ totalPrice);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onLongRemove(ItemOrder item, int position) {
        cartDatabase.child(item.getUid()).child(item.getPid())
                .removeValue()
                .addOnSuccessListener(aVoid -> {
                    cartList.remove(position);
                    adapter.notifyItemRemoved(position);

                    // Recalculate total price after removing an item
                    totalPrice = 0;
                    for (ItemOrder itemOrder : cartList) {
                        totalPrice += Integer.parseInt(itemOrder.getPrice());
                    }

                    // Update the total price TextView
                    binding.tvTotalPrice.setText("đ"+ totalPrice);
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show());
    }
    @Override
    public void onQuantityChange() {
        cartList= adapter.getUpdatedList();
        totalPrice = 0;
        for (ItemOrder item : cartList) {
            totalPrice += Integer.parseInt(item.getPrice());
        }
        binding.tvTotalPrice.setText("đ"+ totalPrice);
    }

}