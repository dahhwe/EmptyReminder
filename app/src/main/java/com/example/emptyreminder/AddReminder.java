package com.example.emptyreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddReminder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
    }

    public void onClick(View view) {
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();

        EditText nameEditText = (EditText) findViewById(R.id.txt_new_reminder_name);
        String name = nameEditText.getText().toString();

        EditText infoEditText = (EditText) findViewById(R.id.txt_new_reminder_info);
        String info = infoEditText.getText().toString();

        TimePicker timePicker = (TimePicker) findViewById(R.id.time_new_reminder);
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        DatePicker datePicker = (DatePicker) findViewById(R.id.date_new_reminder);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

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

        // show a message to the user indicating that the data has been saved
    }
}
