package edu.mirea.onebeattrue.znakomstva.ui.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.mirea.onebeattrue.znakomstva.R;
import edu.mirea.onebeattrue.znakomstva.databinding.ItemMessageBinding;

public class DataAdapter extends RecyclerView.Adapter<ViewHolder> {

    ArrayList<ChatMessage> messages;

    public DataAdapter(Context context, ArrayList<ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMessageBinding binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage msg = messages.get(position);
        holder.binding.messageTextView.setText(msg.getMessageText());
        // Получение экземпляра класса DateFormat
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        // Форматирование времени отправки сообщения в строку
        String msgTimeString = dateFormat.format(new Date(msg.getMessageTime()));
        holder.binding.dateTextView.setText(msgTimeString);
        holder.binding.userNameTextView.setText(msg.getMessageUser());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
