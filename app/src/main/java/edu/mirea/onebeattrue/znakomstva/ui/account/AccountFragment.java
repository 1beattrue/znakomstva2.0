package edu.mirea.onebeattrue.znakomstva.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

import edu.mirea.onebeattrue.znakomstva.databinding.FragmentAccountBinding;
import edu.mirea.onebeattrue.znakomstva.ui.auth.Login;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccountViewModel homeViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);
            getActivity().finish();
        }
        else {
            binding.userDetails.setText(user.getEmail());
        }

        binding.userNameTextView.setText("UnknownUser");
        binding.userNameEditTextView.setText("");
        if (!Objects.equals(user.getDisplayName(), "")) {
            binding.userNameTextView.setText(user.getDisplayName());
            binding.userNameEditTextView.setText(user.getDisplayName());
        }

        binding.editUserNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.userNameTextView.setVisibility(View.GONE);
                binding.editUserNameBtn.setVisibility(View.GONE);
                binding.userNameEditTextView.setVisibility(View.VISIBLE);
                binding.saveUserNameBtn.setVisibility(View.VISIBLE);
            }
        });

        binding.saveUserNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username;
                username = String.valueOf(binding.userNameEditTextView.getText());

                if (!username.isEmpty()) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();

                    assert user != null;
                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Username updated successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    binding.userNameTextView.setText(username);
                    binding.userNameEditTextView.setVisibility(View.GONE);
                    binding.saveUserNameBtn.setVisibility(View.GONE);
                    binding.userNameTextView.setVisibility(View.VISIBLE);
                    binding.editUserNameBtn.setVisibility(View.VISIBLE);
                }
                else Toast.makeText(getContext(), "Enter username", Toast.LENGTH_SHORT).show();
            }
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}