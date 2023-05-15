package edu.mirea.onebeattrue.znakomstva.ui.map;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import edu.mirea.onebeattrue.znakomstva.MainActivity;
import edu.mirea.onebeattrue.znakomstva.R;
import edu.mirea.onebeattrue.znakomstva.databinding.ActivityEventBinding;
import edu.mirea.onebeattrue.znakomstva.ui.auth.Login;
import edu.mirea.onebeattrue.znakomstva.ui.chat.ChatMessage;

public class EventActivity extends AppCompatActivity {
    String selectedEvent; // Изначально ни одно мероприятие не выбрано

    ActivityEventBinding binding;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://znakomstva3030-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference eventsRef = database.getReference("events");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        // получение даты мероприятия
        binding.editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Обработка выбранной даты
                        String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%02d", dayOfMonth, month + 1, year % 100);
                        binding.editTextDate.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

                datePickerDialog.show();
            }
        });

        // получение времени мероприятия
        binding.editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(EventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Обработка выбранного времени
                        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        binding.editTextTime.setText(selectedTime);
                    }
                }, hour, minute, true);

                timePickerDialog.show();
            }
        });

        // Получение ссылки на RadioGroup из привязки
        RadioGroup radioGroup = binding.radioGroup;

        // установка значения переменной по умолчанию
        selectedEvent = "music";

        // выбор категории мероприятия
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button1:
                        selectedEvent = "music";
                        break;
                    case R.id.radio_button2:
                        selectedEvent = "sport";
                        break;
                    case R.id.radio_button3:
                        selectedEvent = "art";
                        break;
                    case R.id.radio_button4:
                        selectedEvent = "movies";
                        break;
                    case R.id.radio_button5:
                        selectedEvent = "education";
                        break;
                    case R.id.radio_button6:
                        selectedEvent = "social";
                        break;
                    case R.id.radio_button7:
                        selectedEvent = "culinary";
                        break;
                    case R.id.radio_button8:
                        selectedEvent = "technology";
                        break;
                }
            }
        });

        binding.addNewEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Отправка мероприятия в Firebase
                String eventId = eventsRef.push().getKey();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String eventUser, eventName, eventDescription, eventTime, eventPlace, eventCategory;

                // Проверка, что поле названия мероприятия не пустое
                if (binding.editTextName.getText().toString().isEmpty() || binding.editTextName.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter event name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Проверка, что поле описания мероприятия не пустое
                if (binding.editTextDescription.getText().toString().isEmpty() || binding.editTextDescription.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter event description", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Проверка, что поле времени мероприятия не пустое
                if (binding.editTextTime.getText().toString().equals("Set event time")) {
                    Toast.makeText(getApplicationContext(), "Set event time", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Проверка, что поле даты мероприятия не пустое
                if (binding.editTextDate.getText().toString().equals("Set event date")) {
                    Toast.makeText(getApplicationContext(), "Set event date", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Проверка, что поле места мероприятия не пустое
                if (binding.editTextPlace.getText().toString().isEmpty() || binding.editTextPlace.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter event place", Toast.LENGTH_SHORT).show();
                    return;
                }

                eventUser = user.getUid();
                eventName = binding.editTextName.getText().toString();
                eventDescription = binding.editTextDescription.getText().toString();
                eventTime = binding.editTextTime.getText().toString() + " " + binding.editTextDate.getText().toString();
                eventPlace = binding.editTextPlace.getText().toString();
                eventCategory = selectedEvent;

                NewEvent newEvent = new NewEvent(eventName.trim(), eventDescription.trim(), eventTime.trim(), eventPlace.trim());
                newEvent.setEventId(eventId);
                newEvent.setEventUser(eventUser);
                newEvent.setEventCategory(eventCategory);

                eventsRef.child(eventId).setValue(newEvent);
                Toast.makeText(EventActivity.this, "Event successfully added",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
