package com.giaodoan.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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

    private boolean firstInit;

    private FirebaseAuth auth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        firstInit = true;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        BottomNavigationView bottomNavigation = requireActivity().findViewById(R.id.bottom_navigationView);
        setbottomNav(bottomNavigation);
        bottomNavigation.setVisibility(View.VISIBLE);

        itemList = new ArrayList<>();
        itemListPopular = new ArrayList<>();
        collectionReference = FirebaseFirestore.getInstance().collection("products");

        //Implement Items Recycle View All product
        GridLayoutManager productLayoutManager = new GridLayoutManager(getContext(), 2);
        setItemsData();
        itemAdapter = new ItemsShowAdapter(requireContext(), itemList, this);
        binding.rvAllItem.setLayoutManager(productLayoutManager);
        binding.rvAllItem.setAdapter(itemAdapter);

        setItemsPopular();
        LinearLayoutManager productLayoutManagerPop = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        itemAdapter2 = new ItemsPopularAdapter(requireContext(), itemListPopular, this);
        binding.rvPopular.setLayoutManager(productLayoutManagerPop);
        binding.rvPopular.setAdapter(itemAdapter2);

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
                itemListPopular.clear();
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Item item = document.toObject(Item.class);
                    itemListPopular.add(item);
                    Log.d("HomeFragment", "itemId: " + item.getId());

                }
                itemAdapter2.notifyDataSetChanged();
            } else {
                Toast.makeText(requireContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClickProduct(Item item) {

        // Create a new Bundle to pass the clicked item's ID to the ProductDetailFragment
        Bundle bundle = new Bundle();
        bundle.putString("productId", item.getId());

        // Use Navigation component to navigate to the ProductDetailFragment
        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_detailItemFragment2, bundle);
    }

    public void setbottomNav(BottomNavigationView bottomNavigation) {
        if(auth.getCurrentUser().getUid().equals("iouCxrzEPlea8hr2FYduSokzRbH3") && firstInit) {
            Menu menu = bottomNavigation.getMenu();
            menu.removeItem(R.id.orderFragment);
            menu.removeItem(R.id.profileFragment);
            menu.add(0,R.id.orderAdminFragment,Menu.NONE,"Đơn hàng").setIcon(R.drawable.ic_order);
            menu.add(0,R.id.profileFragment,Menu.NONE,"Profile").setIcon(R.drawable.ic_baseline_account_circle_24);
            Log.d("HomeFragment123", "dcmm");
            firstInit = false;
        }
        Log.d("HomeFragment123", auth.getCurrentUser().getUid());

    }
}
