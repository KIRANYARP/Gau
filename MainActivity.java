package com.example.travelbookingapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText addressEditText;
    private DatePicker datePicker;
    private Button geocodeButton;
    private Button setReminderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addressEditText = findViewById(R.id.addressEditText);
        datePicker = findViewById(R.id.datePicker);
        geocodeButton = findViewById(R.id.geocodeButton);
        setReminderButton = findViewById(R.id.setReminderButton);

        geocodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geocodeAddress();
            }
        });

        setReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleReminder();
            }
        });
    }

    private void geocodeAddress() {
        String address = addressEditText.getText().toString();
        if (address.isEmpty()) {
            Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show();
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses.size() > 0) {
                Address location = addresses.get(0);
                String message = "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude();
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Geocoding failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleReminder() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), 8, 0, 0);

        Intent intent = new Intent(this, ReminderBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "Reminder set for: " + calendar.getTime().toString(), Toast.LENGTH_LONG).show();
    }
}
