package com.example.deaneryapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity {

    private EditText editPurchase;
    private TimePicker timePicker;
    private Button buttonReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        editPurchase = findViewById(R.id.editPurchase);
        timePicker = findViewById(R.id.timePicker);
        buttonReminder = findViewById(R.id.buttonReminder);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1
                );
            }
        }

        buttonReminder.setOnClickListener(v -> {
            String item = editPurchase.getText().toString();

            if (item.isEmpty()) {
                Toast.makeText(this, "Введите название покупки", Toast.LENGTH_SHORT).show();
                return;
            }

            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            Intent intent = new Intent(this, ReminderReceiver.class);
            intent.putExtra("item", item);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    100,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager =
                    (AlarmManager) getSystemService(ALARM_SERVICE);

            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );

            Toast.makeText(
                    this,
                    "Напоминание установлено",
                    Toast.LENGTH_LONG
            ).show();
        });
    }
}