package com.giaodoan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.giaodoan.R;
import com.giaodoan.databinding.DetailItemFragmentBinding;
import com.giaodoan.model.Item;
import com.giaodoan.model.ItemOrder;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class DetailItemFragment extends Fragment {

    private DetailItemFragmentBinding binding;
    private DatabaseReference productDatabaseReference;
    private FirebaseAuth auth;
    private String currentUID;
    private String orderImageUrl;
    private String orderName;
    private final int orderQuantity = 1;
    private String orderPrice;
    private CollectionReference orderDatabaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        productDatabaseReference = FirebaseDatabase.getInstance().getReference("products");
        orderDatabaseReference = FirebaseFirestore.getInstance().collection("orders");
        currentUID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DetailItemFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BottomNavigationView bottomNavigation = requireActivity().findViewById(R.id.bottom_navigationView);
        bottomNavigation.setVisibility(View.GONE);

        assert getArguments() != null;
        String productId = getArguments().getString("productId");

        binding.detailBackButton.setOnClickListener(v -> Navigation.findNavController(requireView()).popBackStack());

        ValueEventListener valueEvent = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Item products = dataSnapshot.getValue(Item.class);
                        assert products != null;
                        if (products.getId().equals(productId)) {
                            Glide.with(requireContext()).load(products.getImageUrl()).into(binding.detailImg);
                            orderImageUrl = products.getImageUrl();
                            orderName = products.getName();
                            orderPrice = products.getPrice();
                            binding.detailPrice.setText("Rp " + products.getPrice());
                            binding.detailItemname.setText(products.getName());
                            binding.detailDesc.setText(products.getDescription());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        productDatabaseReference.addValueEventListener(valueEvent);

        binding.detailAddbutton.setOnClickListener(v -> {
            ItemOrder orderedProduct = new ItemOrder(currentUID, productId, orderName, orderQuantity, orderPrice, orderImageUrl);
            addDataToOrdersDatabase(orderedProduct);
        });
    }

    private void addDataToOrdersDatabase(ItemOrder orderedProduct) {
        orderDatabaseReference.add(orderedProduct).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(requireContext(), "Thành công thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

