package com.giaodoan.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.giaodoan.R;
import com.giaodoan.databinding.ActivityHomepageBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class HomePageActivity extends AppCompatActivity {

    private NavController navController;
    private ActivityHomepageBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.mainContainer);
        navController = Objects.requireNonNull(navHostFragment).getNavController();

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

        // Testing User
        Toast.makeText(this, "Welcome " + Objects.requireNonNull(auth.getCurrentUser()).getEmail(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }
}

