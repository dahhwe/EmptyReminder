package com.example.emptyreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddReminder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        ImageButton backButton = findViewById(R.id.imageButton2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(AddReminder.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Set default time to one minute ahead of current time
        TimePicker timePicker = (TimePicker) findViewById(R.id.time_new_reminder);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
    }


    public void onClick(View view) {
        EditText nameEditText = (EditText) findViewById(R.id.txt_new_reminder_name);
        String name = nameEditText.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        TimePicker timePicker = (TimePicker) findViewById(R.id.time_new_reminder);
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        DatePicker datePicker = (DatePicker) findViewById(R.id.date_new_reminder);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

        Calendar minCalendar = Calendar.getInstance();
        minCalendar.add(Calendar.MINUTE, 1);

        if (calendar.getTimeInMillis() < minCalendar.getTimeInMillis()) {
            Toast.makeText(this, "Date and time must be at least one minute in the future", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText infoEditText = (EditText) findViewById(R.id.txt_new_reminder_info);
        String info = infoEditText.getText().toString();

        ReminderDbHelper dbHelper = new ReminderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ReminderContract.ReminderEntry.COLUMN_NAME_NAME, name);
        values.put(ReminderContract.ReminderEntry.COLUMN_NAME_INFO, info);
        values.put(ReminderContract.ReminderEntry.COLUMN_NAME_DAY, day);
        values.put(ReminderContract.ReminderEntry.COLUMN_NAME_MONTH, month);
        values.put(ReminderContract.ReminderEntry.COLUMN_NAME_YEAR, year);
        values.put(ReminderContract.ReminderEntry.COLUMN_NAME_HOUR, hour);
        values.put(ReminderContract.ReminderEntry.COLUMN_NAME_MINUTE, minute);

        long newRowId = db.insert(ReminderContract.ReminderEntry.TABLE_NAME, null, values);

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();

        // Navigate back to MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
