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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.giaodoan.R;
import com.giaodoan.adapter.ItemsPopularAdapter;
import com.giaodoan.adapter.ItemsShowAdapter;
import com.giaodoan.adapter.SearchAdapter;
import com.giaodoan.databinding.FragmentHomeBinding;
import com.giaodoan.model.Item;
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

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment implements SearchAdapter.ProductOnClickInterface, ItemsShowAdapter.ProductOnClickInterface, ItemsPopularAdapter.ProductOnClickInterface {
    private FragmentHomeBinding binding;
    private CollectionReference collectionReference;
    private ArrayList<Item> itemList;
    private ArrayList<Item> itemListPopular;
    private ItemsShowAdapter itemAdapter;
    private ItemsPopularAdapter itemAdapter2;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavigation = requireActivity().findViewById(R.id.bottom_navigationView);
        bottomNavigation.setVisibility(View.VISIBLE);

        itemList = new ArrayList<>();
        itemListPopular = new ArrayList<>();
        collectionReference = FirebaseFirestore.getInstance().collection("products");

        //Implement Items Recycle View All product
        GridLayoutManager productLayoutManager = new GridLayoutManager(getContext(), 2);

        itemAdapter = new ItemsShowAdapter(requireContext(), itemList, this);

        binding.rvAllItem.setLayoutManager(productLayoutManager);
        binding.rvAllItem.setAdapter(itemAdapter);
        setItemsData();



        //Implement Popular Items Recycle Items
        LinearLayoutManager productLayoutManagerPop = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        itemAdapter2 = new ItemsPopularAdapter(requireContext(), itemListPopular, this);
        binding.rvPopular.setLayoutManager(productLayoutManagerPop);
        binding.rvPopular.setAdapter(itemAdapter2);
        setItemsPopular();

        // got to search
        binding.searchBox.setOnClickListener(view1 -> Navigation.findNavController(requireView())
                .navigate(R.id.action_homeFragment_to_searchFragment));
    }








    private void setItemsData() {
        collectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                itemList.clear();
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Item item = document.toObject(Item.class);
                    itemList.add(item);
                    Log.d("HomeFragment","itemId: "+item.getId());
                    Log.d("HomeFragment","name: "+item.getName());
                    Log.d("HomeFragment","description: "+item.getDescription());
                }
                itemAdapter.notifyDataSetChanged();

            } else {
                Toast.makeText(requireContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setItemsPopular() {
        collectionReference.whereEqualTo("type", "popular").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                itemList.clear();
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Item item = document.toObject(Item.class);
                    itemList.add(item);
                    Log.d("HomeFragment","2imageurl: "+item.getImageUrl());
                }
                itemAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(requireContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onClickProduct(Item item) {
        // Handle the click event here
        // For example, you can show a Toast message

        Toast.makeText(getContext(), "Item clicked: " + item.getName(), Toast.LENGTH_SHORT).show();

        // Create a new Bundle to pass the clicked item's ID to the ProductDetailFragment
        Bundle bundle = new Bundle();
        bundle.putString("productId", item.getId());
        Log.d("HomeFragment","productId: "+item.getId());

        // Use Navigation component to navigate to the ProductDetailFragment
        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_detailItemFragment2, bundle);
    }


}
