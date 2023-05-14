package edu.mirea.onebeattrue.znakomstva.ui.account;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import edu.mirea.onebeattrue.znakomstva.databinding.FragmentAccountBinding;
import edu.mirea.onebeattrue.znakomstva.ui.auth.Login;
import edu.mirea.onebeattrue.znakomstva.ui.chat.ChatMessage;

public class AccountFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private FragmentAccountBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://znakomstva3030-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference myRef = database.getReference("users");

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

            // Добавляем слушатель событий
            myRef.orderByKey().equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()) {
                        //Key exists
                        for (DataSnapshot userID: dataSnapshot.getChildren()) {
                            if (Objects.equals(userID.getKey(), user.getUid()))
                            {
                                CurrentUser currentUser = userID.getValue(CurrentUser.class);
                                assert currentUser != null;
                                binding.userNameTextView.setText(currentUser.getUserName());
                            }

                        }
                    } else {
                        //Key does not exist
                        CurrentUser currentUser = new CurrentUser(user.getEmail(), "noname");
                        myRef.child(user.getUid()).setValue(currentUser);
                        binding.userNameTextView.setText(currentUser.getUserName());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        binding.editUserNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.userNameEditTextView.setText("");
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

                if (!username.trim().isEmpty()) {
                    username = username.trim();
                    CurrentUser currentUser = new CurrentUser(user.getEmail(), username);
                    myRef.child(user.getUid()).setValue(currentUser);
                    binding.userNameTextView.setText(currentUser.getUserName());

                    binding.userNameTextView.setText(username);
                    binding.userNameEditTextView.setVisibility(View.GONE);
                    binding.saveUserNameBtn.setVisibility(View.GONE);
                    binding.userNameTextView.setVisibility(View.VISIBLE);
                    binding.editUserNameBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Username changed successfully", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getContext(), "Enter username", Toast.LENGTH_SHORT).show();
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

        binding.changeAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        // Инициализация ActivityResultLauncher
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Uri imageUri = result.getData().getData();
                            // Обновление изображения аватарки
                            binding.avatar.setImageURI(imageUri);
                        }
                    }
                });



        View root = binding.getRoot();
        return root;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}