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
import com.giaodoan.model.Cart;
import com.giaodoan.model.ItemOrder;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class CartFragment extends Fragment implements CartAdapter.OnLongClickRemove {

    private CartFragmentBinding binding;
    private ArrayList<ItemOrder> cartList;
    private FirebaseAuth auth;
    private CartAdapter adapter;

    private int totalPrice = 0;
    private DatabaseReference cartDatabase= FirebaseDatabase.getInstance("https://acofee-order-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("carts");


    private CollectionReference cartDatabaseReference = FirebaseFirestore.getInstance().collection("carts");


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

        adapter = new CartAdapter(requireContext(), cartList, this);
        binding.rvCarts.setAdapter(adapter);

        binding.rvCarts.setLayoutManager(layoutManager);


        binding.cartCheckout.setOnClickListener(v -> {
            if (cartList.isEmpty()) {
                Toast.makeText(requireActivity(), "Giỏ hàng của bạn đang trống", Toast.LENGTH_LONG).show();
            } else {
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_cartFragment_to_checkoutFragment);
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
                    Toast.makeText(getContext(), "Berhasil Menghapus", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Gagal Menghapus Barang", Toast.LENGTH_SHORT).show());
    }






}