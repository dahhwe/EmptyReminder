package com.example.emptyreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class ReminderRepository {
    private ReminderDbHelper dbHelper;
    private MutableLiveData<List<Reminder>> reminders;

    public ReminderRepository(Context context) {
        dbHelper = new ReminderDbHelper(context);
        reminders = new MutableLiveData<>();
        loadReminders();
    }

    public MutableLiveData<List<Reminder>> getReminders() {
        return reminders;
    }

    public void addReminder(Reminder reminder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReminderContract.ReminderEntry.COLUMN_NAME_NAME, reminder.getName());
        values.put(ReminderContract.ReminderEntry.COLUMN_NAME_INFO, reminder.getInfo());
        values.put(ReminderContract.ReminderEntry.COLUMN_NAME_DAY, reminder.getDay());
        values.put(ReminderContract.ReminderEntry.COLUMN_NAME_MONTH, reminder.getMonth());
        values.put(ReminderContract.ReminderEntry.COLUMN_NAME_YEAR, reminder.getYear());
        values.put(ReminderContract.ReminderEntry.COLUMN_NAME_HOUR, reminder.getHour());
        values.put(ReminderContract.ReminderEntry.COLUMN_NAME_MINUTE, reminder.getMinute());
        values.put(ReminderContract.ReminderEntry.COLUMN_NAME_COMPLETED, reminder.isCompleted()); // add completed value

        long newRowId = db.insert(ReminderContract.ReminderEntry.TABLE_NAME, null, values);
        loadReminders();
    }

    public void deleteReminder(Reminder reminder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = ReminderContract.ReminderEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(reminder.getId())};
        db.delete(ReminderContract.ReminderEntry.TABLE_NAME, selection, selectionArgs);
        loadReminders();
    }

    // add new updateReminder method
    public void updateReminder(Reminder reminder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ReminderContract.ReminderEntry.COLUMN_NAME_COMPLETED, reminder.isCompleted());

        String selection = ReminderContract.ReminderEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(reminder.getId())};

        db.update(ReminderContract.ReminderEntry.TABLE_NAME, values, selection, selectionArgs);

        loadReminders();
    }

    private void loadReminders() {
        // load reminders from database
        List<Reminder> reminders = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                ReminderContract.ReminderEntry._ID,
                ReminderContract.ReminderEntry.COLUMN_NAME_NAME,
                ReminderContract.ReminderEntry.COLUMN_NAME_INFO,
                ReminderContract.ReminderEntry.COLUMN_NAME_DAY,
                ReminderContract.ReminderEntry.COLUMN_NAME_MONTH,
                ReminderContract.ReminderEntry.COLUMN_NAME_YEAR,
                ReminderContract.ReminderEntry.COLUMN_NAME_HOUR,
                ReminderContract.ReminderEntry.COLUMN_NAME_MINUTE,
                ReminderContract.ReminderEntry.COLUMN_NAME_COMPLETED // add completed column to projection
        };
        Cursor cursor = db.query(
                ReminderContract.ReminderEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(ReminderContract.ReminderEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ReminderContract.ReminderEntry.COLUMN_NAME_NAME));
            String info = cursor.getString(cursor.getColumnIndexOrThrow(ReminderContract.ReminderEntry.COLUMN_NAME_INFO));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderContract.ReminderEntry.COLUMN_NAME_DAY));
            int month = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderContract.ReminderEntry.COLUMN_NAME_MONTH));
            int year = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderContract.ReminderEntry.COLUMN_NAME_YEAR));
            int hour = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderContract.ReminderEntry.COLUMN_NAME_HOUR));
            int minute = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderContract.ReminderEntry.COLUMN_NAME_MINUTE));
            boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderContract.ReminderEntry.COLUMN_NAME_COMPLETED)) == 1; // get completed value

            reminders.add(new Reminder(id, name, info, day, month, year, hour, minute, completed)); // pass completed value to Reminder constructor
        }
        cursor.close();

        this.reminders.setValue(reminders);
    }
}