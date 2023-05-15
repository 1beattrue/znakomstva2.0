package edu.mirea.onebeattrue.znakomstva.ui.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.mirea.onebeattrue.znakomstva.MainActivity;
import edu.mirea.onebeattrue.znakomstva.databinding.ItemEventBinding;
import edu.mirea.onebeattrue.znakomstva.ui.auth.Login;

public class DataAdapterEvent extends RecyclerView.Adapter<ViewHolderEvent> {
    private boolean editButtonInEditMode = true; // установка кнопки в значение редактирования

    ArrayList<NewEvent> events;
    FirebaseUser user;
    FirebaseAuth auth;

    DatabaseReference usersRef = FirebaseDatabase.getInstance("https://znakomstva3030-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference()
            .child("users");

    DatabaseReference eventsRef = FirebaseDatabase.getInstance("https://znakomstva3030-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference()
            .child("events");

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
    public void onBindViewHolder(@NonNull ViewHolderEvent holder, @SuppressLint("RecyclerView") int position) {
        NewEvent event = events.get(position);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        DatabaseReference interestsRef = usersRef.child(user.getUid()).child("interests");

        // установка цвета мероприятия в зависимости от интересов пользователя
        String eventCategory = event.getEventCategory();

        interestsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Получение данных интересов пользователя
                    boolean greenColor = Boolean.TRUE.equals(dataSnapshot.child(eventCategory).getValue(Boolean.class));
                    // Установка цвета заднего фона в зависимости от переменной greenColor
                    if (greenColor) {
                        holder.itemView.setBackgroundColor(Color.GREEN);
                    } else {
                        holder.itemView.setBackgroundColor(Color.WHITE);
                    }
                } else {
                    holder.itemView.setBackgroundColor(Color.WHITE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок чтения из базы данных
            }
        });

        holder.binding.eventTitle.setText(event.getEventName());
        holder.binding.eventDescription.setText(event.getEventDescription());
        holder.binding.eventTime.setText(event.getEventTime());
        holder.binding.eventLocation.setText(event.getEventPlace());

        // Установка слушателя кликов для кнопки удаления
        holder.binding.deleteEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                events.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, events.size());

                // Удаление мероприятия из базы данных
                eventsRef.child(event.getEventId()).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Успешно удалено
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Обработка ошибки удаления
                            }
                        });
            }
        });

        // Установка слушателя кликов для кнопки удаления

        if (editButtonInEditMode) {
            holder.binding.editEventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // включение возможности редактирования event'a
                    editButtonInEditMode = false;
                    holder.binding.eventTitle.setFocusableInTouchMode(true);
                    holder.binding.eventDescription.setFocusableInTouchMode(true);
                    holder.binding.eventTime.setFocusableInTouchMode(true);
                    holder.binding.eventLocation.setFocusableInTouchMode(true);
                    holder.binding.editEventButton.setText("Save");
                    notifyDataSetChanged();
                }
            });
        } else {
            holder.binding.editEventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // выключение возможности редактирования event'a
                    editButtonInEditMode = true;
                    holder.binding.eventTitle.setFocusable(false);
                    holder.binding.eventDescription.setFocusable(false);
                    holder.binding.eventTime.setFocusable(false);
                    holder.binding.eventLocation.setFocusable(false);
                    holder.binding.editEventButton.setText("Edit");

                    // редактирование информации в бд
                    // Проверка, что поле названия мероприятия не пустое
                    if (!(holder.binding.eventTitle.getText().toString().trim().length() == 0)) {
                        eventsRef.child(event.getEventId()).child("eventName").setValue(holder.binding.eventTitle.getText().toString().trim());
                    }

                    // Проверка, что поле описания мероприятия не пустое
                    if (!(holder.binding.eventDescription.getText().toString().trim().length() == 0)) {
                        eventsRef.child(event.getEventId()).child("eventDescription").setValue(holder.binding.eventDescription.getText().toString().trim());
                    }

                    // Проверка, что поле времени мероприятия не пустое
                    if (!(holder.binding.eventTime.getText().toString().trim().length() == 0)) {
                        eventsRef.child(event.getEventId()).child("eventTime").setValue(holder.binding.eventTime.getText().toString().trim());
                    }

                    // Проверка, что поле места мероприятия не пустое
                    if (!(holder.binding.eventLocation.getText().toString().trim().length() == 0)) {
                        eventsRef.child(event.getEventId()).child("eventPlace").setValue(holder.binding.eventLocation.getText().toString().trim());
                    }

                    notifyDataSetChanged();
                }
            });
        }

        if (user.getUid().equals(event.getEventUser())) {
            holder.binding.editEventButton.setVisibility(View.VISIBLE);
            holder.binding.deleteEventButton.setVisibility(View.VISIBLE);
        }
        else {
            holder.binding.editEventButton.setVisibility(View.GONE);
            holder.binding.deleteEventButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}