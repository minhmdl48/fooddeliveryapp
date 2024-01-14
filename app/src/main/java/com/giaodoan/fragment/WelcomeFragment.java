package com.giaodoan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.giaodoan.Activity.HomePageActivity;
import com.giaodoan.R;
import com.giaodoan.databinding.WelcomeFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeFragment extends Fragment {

    private WelcomeFragmentBinding binding;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = WelcomeFragmentBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = WelcomeFragmentBinding.bind(view);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(getActivity(), HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            new Handler(Looper.getMainLooper()).postDelayed(() -> startActivity(intent), 1500);
        } else {
            new Handler(Looper.getMainLooper()).postDelayed(() -> NavHostFragment.findNavController(this).navigate(R.id.action_welcomeFragment_to_loginFragment2), 1500);
        }
    }
}

