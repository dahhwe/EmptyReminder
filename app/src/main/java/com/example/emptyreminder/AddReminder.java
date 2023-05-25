package com.example.emptyreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddReminder extends AppCompatActivity {

    private ReminderRepository reminderRepository;
    private Reminder reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        reminderRepository = ServiceLocator.provideReminderRepository(getApplicationContext());
        reminder = new ReminderImpl();

        ImageButton backButton = findViewById(R.id.imageButton2);
        backButton.setOnClickListener(v -> navigateBack());

        setDefaultTime();
    }

    private void setDefaultTime() {
        TimePicker timePicker = findViewById(R.id.time_new_reminder);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
    }

    private void navigateBack() {
        Intent intent = new Intent(AddReminder.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClick(View view) {
        if (!isNameValid()) return;
        if (!isDateTimeValid()) return;

        EditText infoEditText = findViewById(R.id.txt_new_reminder_info);
        String info = infoEditText.getText().toString();

        reminder.setInfo(info);

        reminderRepository.addReminder(reminder);

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();

        navigateBack();
    }

    private boolean isNameValid() {
        EditText nameEditText = findViewById(R.id.txt_new_reminder_name);
        String name = nameEditText.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        reminder.setName(name);
        return true;
    }

    private boolean isDateTimeValid() {
        TimePicker timePicker = findViewById(R.id.time_new_reminder);
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        DatePicker datePicker = findViewById(R.id.date_new_reminder);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

        Calendar minCalendar = Calendar.getInstance();
        minCalendar.add(Calendar.MINUTE, 1);

        if (calendar.getTimeInMillis() < minCalendar.getTimeInMillis()) {
            Toast.makeText(this, "Date and time must be at least one minute in the future", Toast.LENGTH_SHORT).show();
            return false;
        }

        reminder.setDay(day);
        reminder.setMonth(month);
        reminder.setYear(year);
        reminder.setHour(hour);
        reminder.setMinute(minute);

        return true;
    }
}

