package edu.mirea.onebeattrue.znakomstva.ui.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.mirea.onebeattrue.znakomstva.databinding.FragmentChatBinding;

public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://znakomstva3030-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference myRef = database.getReference("message");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ChatViewModel chatViewModel =
                new ViewModelProvider(this).get(ChatViewModel.class);

        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Обработка нажатия на кнопку
                String message = binding.messageEditText.getText().toString();
                sendMessage(message);
            }
        });

        return root;
    }

    private void sendMessage(String message) {
        // Проверка, что сообщение не пустое
        if (!TextUtils.isEmpty(message)) {
            // Отправка сообщения в Firebase
            String messageId = myRef.push().getKey();
            ChatMessage newMessage = new ChatMessage(message, FirebaseAuth.getInstance().getCurrentUser().getUid());
            myRef.child(messageId).setValue(newMessage);

            // Очистка поля ввода сообщения
            binding.messageEditText.setText("");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
