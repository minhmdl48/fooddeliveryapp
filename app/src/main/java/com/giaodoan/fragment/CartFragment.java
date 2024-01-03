package com.giaodoan.fragment;

import android.os.Bundle;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class CartFragment extends Fragment implements CartAdapter.OnLongClickRemove {

    private CartFragmentBinding binding;
    private ArrayList<Cart> cartList;
    private FirebaseAuth auth;
    private CartAdapter adapter;

    private int totalPrice = 0;


    private CollectionReference cartDatabaseReference = FirebaseFirestore.getInstance().collection("carts");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CartFragmentBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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
        cartDatabaseReference
                .whereEqualTo("uid", Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Cart cartProduct = documentSnapshot.toObject(Cart.class);

                        cartList.add(cartProduct);

                        totalPrice += Integer.parseInt(cartProduct.getPrice());


                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onLongRemove(Cart item, int position) {
        cartDatabaseReference
                .whereEqualTo("uid", item.getUid())
                .whereEqualTo("pid", item.getPid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        cartDatabaseReference.document(documentSnapshot.getId()).delete();
                        cartList.remove(position);
                        adapter.notifyItemRemoved(position);
                        Toast.makeText(getContext(), "Berhasil Menghapus", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Gagal Menghapus Barang", Toast.LENGTH_SHORT).show());
    }


    private void deleteCart(Cart cart) {
        Task<QuerySnapshot> personQuery = cartDatabaseReference
                .whereEqualTo("uid", cart.getUid())
                .get();

        personQuery.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    cartDatabaseReference.document(document.getId()).delete()
                            .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
                }
            } else {
                Toast.makeText(getContext(), "Không tìm thấy giỏ hàng trong database ", Toast.LENGTH_SHORT).show();
            }
        });
    }



}