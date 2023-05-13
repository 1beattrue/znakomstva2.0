package edu.mirea.onebeattrue.znakomstva.ui.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.mirea.onebeattrue.znakomstva.databinding.ItemEventBinding;

public class DataAdapterEvent extends RecyclerView.Adapter<ViewHolderEvent> {
    ArrayList<NewEvent> events;
    FirebaseUser user;
    FirebaseAuth auth;

    public DataAdapterEvent(Context context, ArrayList<NewEvent> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolderEvent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEventBinding binding = ItemEventBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolderEvent(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEvent holder, int position) {
        NewEvent event = events.get(position);

        holder.binding.eventTitle.setText(event.getEventName());
        holder.binding.eventDescription.setText(event.getEventDescription());
        holder.binding.eventTime.setText(event.getEventTime());
        holder.binding.eventLocation.setText(event.getEventPlace());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        assert user != null;
        if (user.getUid().equals(event.getUser())) {
            holder.binding.editEventButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}