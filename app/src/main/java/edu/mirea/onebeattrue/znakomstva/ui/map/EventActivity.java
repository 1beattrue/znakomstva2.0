package edu.mirea.onebeattrue.znakomstva.ui.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.mirea.onebeattrue.znakomstva.MainActivity;
import edu.mirea.onebeattrue.znakomstva.databinding.ActivityEventBinding;
import edu.mirea.onebeattrue.znakomstva.ui.chat.ChatMessage;

public class EventActivity extends AppCompatActivity {

    ActivityEventBinding binding;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://znakomstva3030-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference myRef = database.getReference("events");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        binding.addNewEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Отправка мероприятия в Firebase
                String eventId = myRef.push().getKey();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String eventUser, eventName, eventDescription, eventTime, eventPlace;

                eventUser = user.getUid();
                eventName = binding.editTextName.getText().toString();
                eventDescription = binding.editTextDescription.getText().toString();
                eventTime = binding.editTextTime.getText().toString();
                eventPlace = binding.editTextPlace.getText().toString();

                NewEvent newEvent = new NewEvent(eventUser, eventName, eventDescription, eventTime, eventPlace);
                myRef.child(eventId).setValue(newEvent);

                Intent intent = new Intent(getApplicationContext(), MapFragment.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
