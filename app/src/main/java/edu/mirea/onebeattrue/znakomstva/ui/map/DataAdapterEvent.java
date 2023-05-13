package edu.mirea.onebeattrue.znakomstva.ui.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.mirea.onebeattrue.znakomstva.databinding.ItemEventBinding;

public class DataAdapterEvent extends RecyclerView.Adapter<ViewHolderEvent> {
    ArrayList<NewEvent> events;

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
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String eventTimeString = dateFormat.format(new Date(event.getEventTime()));
        holder.binding.eventTimeLocation.setText(eventTimeString);
        holder.binding.eventDescription.setText(event.getEventDescription());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}