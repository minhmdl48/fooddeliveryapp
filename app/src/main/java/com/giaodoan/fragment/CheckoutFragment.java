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

import com.giaodoan.R;
import com.giaodoan.adapter.TitleOnlyAdapter;
import com.giaodoan.databinding.CheckoutFragmentBinding;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CheckoutFragment extends Fragment {
    private CheckoutFragmentBinding binding;
    private CollectionReference orderDatabaseReference = FirebaseFirestore.getInstance().collection("orders");
    private ArrayList<ItemOrder> cartList;
    private FirebaseAuth auth;
    private TitleOnlyAdapter adapter;
    private int totalPrice = 0;
    private DatabaseReference cartDatabase = FirebaseDatabase.getInstance("https://acofee-order-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("carts");
    private DatabaseReference itemorderedDatabase = FirebaseDatabase.getInstance("https://acofee-order-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("itemordered");
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

        cartList = new ArrayList<>();

        adapter = new TitleOnlyAdapter(getContext(), cartList);
        binding.rvTitle.setAdapter(adapter);
        binding.rvTitle.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.checkoutBackButton.setOnClickListener(v -> Navigation.findNavController(requireView())
                .navigate(R.id.action_checkoutFragment_to_cartFragment));
        retrieveCartItems();
        Log.d("CheckoutFragment", "onViewCreated: " + totalPrice);

        binding.checkoutButton.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Thanh toán thành công")
                    .setMessage("Vui lòng đưa mã cho nhân viên tại quầy để nhận đồ uống")
                    .setPositiveButton("OK", (dialog, id) -> dialog.cancel())
                    .show();
            Toast.makeText(getActivity(), "Đã thanh toán thành công" + totalPrice, Toast.LENGTH_LONG).show();

            if (!cartList.isEmpty()) {
                // Generate a unique string based on current time
                String timeString = String.valueOf(System.currentTimeMillis());
                String base64Table = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

                StringBuilder oidBuilder = new StringBuilder();
                for (int i = 0; i < timeString.length() - 2; i += 2) {
                    String chunk = timeString.substring(i, i + 2);
                    int index = Integer.parseInt(chunk);
                    oidBuilder.append(base64Table.charAt(index % 64)); // Use modulo 64 to ensure index is within base64 table range
                }

                // Append the last two characters without translation
                oidBuilder.append(timeString.substring(timeString.length() - 2));

                String oid = oidBuilder.toString();

                // Create an Order object
                Order order = new Order();
                order.setUid(Objects.requireNonNull(auth.getCurrentUser()).getUid());
                order.setPrice(String.valueOf(totalPrice));
                Date now = new Date();

                SimpleDateFormat sdf = new SimpleDateFormat("EEEEE hh:mm dd-MM-yyyy", Locale.getDefault());

                String formattedDate = sdf.format(now);
                order.setOrdertime(formattedDate);
                order.setOid(oid);

                // Add the order to the database
                orderDatabaseReference.document(oid).set(order)
                        .addOnSuccessListener(aVoid -> Log.d("CheckoutFragment", "Order placed successfully"))
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to place order", Toast.LENGTH_SHORT).show());
                //set item ordered to database
                itemorderedDatabase.child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).child(oid).setValue(cartList)
                        .addOnSuccessListener(aVoid -> Log.d("CheckoutFragment", "Order placed successfully"))
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to place order", Toast.LENGTH_SHORT).show());

                deleteCart(); //delete cart in carts database after checkout
                cartList.clear();
                adapter.notifyDataSetChanged();
            }

            Navigation.findNavController(requireView())
                    .navigate(R.id.action_checkoutFragment_to_homeFragment);
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
                        binding.txtViewPriceTotal.setText("đ " + totalPrice);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteCart() {
        cartDatabase.child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Xóa giỏ hàng thành công", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show());
    }

}
