package com.giaodoan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.giaodoan.R;
import com.giaodoan.databinding.RegisterFragmentBinding;
import com.giaodoan.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";
    private RegisterFragmentBinding binding;
    private FirebaseAuth auth;
    private CollectionReference userDatabaseReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RegisterFragmentBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = RegisterFragmentBinding.bind(view);
        auth = FirebaseAuth.getInstance();
        userDatabaseReference = FirebaseFirestore.getInstance().collection("users");

        binding.registerButtonRegister.setOnClickListener(v -> {
            String email = binding.registerEmail .getText().toString().trim();
            String password = binding.registerPassword.getText().toString().trim();
            if (!email.isEmpty() && !password.isEmpty()) {
                User user = new User(
                        "",
                        binding.registerFullName.getText().toString().trim(),
                        binding.registerUsername.getText().toString().trim(),
                        email,
                        password
                );
                registerUser(user);
            }
        });
    }

    private void addDataToUserModel(User user) {
        userDatabaseReference.add(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(requireContext(), "Thành công thêm User vào collection", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser(User user) {

            auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireActivity(), "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                            addDataToUserModel(user);
                            Navigation.findNavController(requireView())
                                    .navigate(R.id.action_registerFragment2_to_loginFragment2);
                        } else {
                            Toast.makeText(requireActivity(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }






