package com.giaodoan.fragment;

import android.content.Intent;
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
import com.giaodoan.Activity.HomePageActivity;
import com.giaodoan.R;
import com.giaodoan.databinding.LoginFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginFragment extends Fragment {
    private LoginFragmentBinding binding;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LoginFragmentBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = LoginFragmentBinding.bind(view);
        auth = FirebaseAuth.getInstance();

        // Xử lý sự kiện click của nút Đăng nhập
        binding.loginButtonLogin.setOnClickListener(v -> {
            String email = binding.loginEmail .getText().toString().trim();
            String password = binding.loginPassword.getText().toString().trim();
            signinUser(email, password);
        });

        //Xử lý nút Đăng ký ngay
        binding.loginButtonRegister.setOnClickListener(v -> Navigation.findNavController(view)
                .navigate(R.id.action_loginFragment2_to_registerFragment2));

        //Xử lý nút Quên mật khẩu
        binding.loginForgotPassword.setOnClickListener(v -> {
            String mail= binding.loginEmail.getText().toString().trim();
            auth.sendPasswordResetEmail(mail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("LoginFragment", "Email sent."+mail);
                            Toast.makeText(requireActivity(), "Đã gửi link đặt lại mật khẩu tại: "+mail, Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void signinUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(requireActivity(), "Đăng nhập thành công!!", Toast.LENGTH_SHORT).show();

                        // For Activity
                        Intent intent = new Intent(getActivity(), HomePageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else {
                        Toast.makeText(requireActivity(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
