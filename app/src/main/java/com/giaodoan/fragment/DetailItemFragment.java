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

import com.bumptech.glide.Glide;
import com.giaodoan.R;
import com.giaodoan.databinding.DetailItemFragmentBinding;
import com.giaodoan.model.Item;
import com.giaodoan.model.ItemOrder;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DetailItemFragment extends Fragment {

    private DetailItemFragmentBinding binding;
    private CollectionReference collectionReference;
    private DatabaseReference cartDatabase;
    private FirebaseAuth auth;
   private ArrayList<Item> itemList = new ArrayList<>();
    private String currentUID;
    private String orderImageUrl;
    private String orderName;
    private int orderQuantity;
    private String orderPrice;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        collectionReference = FirebaseFirestore.getInstance().collection("products");
        cartDatabase = FirebaseDatabase.getInstance("https://acofee-order-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("carts");

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
        Log.d("DetailItemFragment","productId: "+productId);

        binding.detailBackButton.setOnClickListener(v -> Navigation.findNavController(requireView()).popBackStack());

        collectionReference.whereEqualTo("id",productId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Item item = document.toObject(Item.class);
                    itemList.add(item);

                    Log.d("DetailItemFragment","description: "+item.getDescription());
                }
                if (!itemList.isEmpty()) {
                    Item item = itemList.get(0);

                    orderImageUrl = item.getImageUrl();
                    orderName = item.getName();

                    binding.detailItemquantity.setText("0");
                    binding.detailItemname.setText(item.getName());
                    binding.detailPrice.setText("đ"+item.getPrice());
                    binding.detailDesc.setText(item.getDescription());
                    binding.addItem.setOnClickListener(v -> {
                        // Handle the click event here
                        // For example, you can increase the quantity of the item
                        int currentQuantity = Integer.parseInt(binding.detailItemquantity.getText().toString());
                        currentQuantity++;
                        binding.detailItemquantity.setText(String.valueOf(currentQuantity));
                    });

                    binding.removeItem.setOnClickListener(v -> {
                        // Handle the click event here
                        // Decrease the quantity of the item
                        int currentQuantity = Integer.parseInt(binding.detailItemquantity.getText().toString());
                        currentQuantity = Math.max(0, currentQuantity - 1); // Ensure the quantity doesn't go below 0
                        binding.detailItemquantity.setText(String.valueOf(currentQuantity));
                    });


                    // Load the image using Glide
                    Glide.with(requireContext())
                            .load(item.getImageUrl())
                            .into(binding.detailImg);
                }
            } else {
                Toast.makeText(requireContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.detailAddbutton.setOnClickListener(v -> {
            // Get the quantity from the TextView and convert it to an integer
            orderQuantity = Integer.parseInt(binding.detailItemquantity.getText().toString());

            // Get the price from the TextView, remove the currency symbol, and convert it to a double
            String priceText = binding.detailPrice.getText().toString().replace("đ", "");
            int price = Integer.parseInt(priceText);

            // Calculate the total price for the quantity ordered
            orderPrice = String.valueOf(price * orderQuantity);

            // Create the orderedProduct object and add it to the database
            ItemOrder orderedProduct = new ItemOrder(currentUID, productId, orderName, orderQuantity, orderPrice, orderImageUrl);
            addDataToCartDatabase(orderedProduct);

            Navigation.findNavController(v).navigate(R.id.action_detailItemFragment2_to_homeFragment);

        });
    }

    private void addDataToCartDatabase(ItemOrder orderedProduct) {
        Map<String, Object> product = new HashMap<>();
        product.put("uid", orderedProduct.getUid());
        product.put("pid", orderedProduct.getPid());
        product.put("name", orderedProduct.getName());
        product.put("quantity", orderedProduct.getQuantity());
        product.put("price", orderedProduct.getPrice());
        product.put("imageUrl", orderedProduct.getImageUrl());
        String time = String.valueOf(System.currentTimeMillis());
        cartDatabase.child(currentUID).child(orderedProduct.getPid()).updateChildren(product)
                .addOnSuccessListener(aVoid ->{
                    Log.d("DetailItemFragment","addDataToCartDatabase: "+product);
                    Toast.makeText(requireContext(), "Thành công thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), Objects.requireNonNull(e.getMessage()), Toast.LENGTH_SHORT).show());
        cartDatabase.child(currentUID).child(orderedProduct.getPid()).setValue(product)
                .addOnSuccessListener(aVoid -> Toast.makeText(requireContext(), "Thành công thêm vào giỏ hàng", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(requireContext(), Objects.requireNonNull(e.getMessage()), Toast.LENGTH_SHORT).show());

    }
}

