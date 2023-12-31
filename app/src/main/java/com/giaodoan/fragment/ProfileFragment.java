package com.giaodoan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.giaodoan.MainActivity;
import com.giaodoan.databinding.ProfileFragmentBinding;
import com.giaodoan.model.User;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.Objects;

public class ProfileFragment extends Fragment {
    private ProfileFragmentBinding binding;
    private FirebaseAuth auth;
    private User userz;


    private final CollectionReference userDatabaseReference = FirebaseFirestore.getInstance().collection("users");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ProfileFragmentBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = ProfileFragmentBinding.bind(view);

        auth = FirebaseAuth.getInstance();
        userz = new User();
        getUserInfo();

        binding.profileEditProfile.setOnClickListener(v -> Toast.makeText(requireActivity(), "Tính năng chưa sẵn sàng", Toast.LENGTH_SHORT).show());

        binding.profileQuestion.setOnClickListener(v -> Toast.makeText(requireActivity(), "Tính năng chưa sẵn sàng", Toast.LENGTH_SHORT).show());

        ;


        binding.profileLogout.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            Toast.makeText(requireActivity(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });
    }

    private void getUserInfo() {
        userDatabaseReference
                .whereEqualTo("email", Objects.requireNonNull(auth.getCurrentUser()).getEmail())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        userz = documentSnapshot.toObject(User.class);

                        binding.profileFullname.setText(userz.getFull_name());
                        binding.profileEmail.setText(userz.getEmail());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }
}

