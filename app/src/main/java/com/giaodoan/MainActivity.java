package com.giaodoan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.giaodoan.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // AUTH
        auth = FirebaseAuth.getInstance();
    }
}
