package edu.mirea.onebeattrue.znakomstva.ui.chat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import edu.mirea.onebeattrue.znakomstva.databinding.FragmentChatBinding;
import edu.mirea.onebeattrue.znakomstva.ui.account.CurrentUser;

public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://znakomstva3030-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference myRef = database.getReference("messages");

    ArrayList<ChatMessage> messages = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ChatViewModel chatViewModel =
                new ViewModelProvider(this).get(ChatViewModel.class);

        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.messageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DataAdapter dataAdapter = new DataAdapter(getContext(), messages);
        binding.messageRecyclerView.setAdapter(dataAdapter);

        // Добавляем слушатель событий
        myRef.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Получаем новое сообщение
                ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                messages.add(message);
                dataAdapter.notifyDataSetChanged();
                binding.messageRecyclerView.smoothScrollToPosition(messages.size()); // вылет после отправки 1 сообщения
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Обработка изменения сообщения
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Обработка удаления сообщения
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Обработка перемещения сообщения
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Обработка ошибок
            }
        });

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
        if (!TextUtils.isEmpty(message) && message.trim().length() != 0) {
            // Отправка сообщения в Firebase
            String messageId = myRef.push().getKey();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            // TODO: 13.05.2023 передать информацию о имени пользователя из фрагмента Account 
            String username = user.getEmail();
            
            ChatMessage newMessage = new ChatMessage(message.trim(), username);
            assert messageId != null;
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
