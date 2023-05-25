package com.example.emptyreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class ReminderRepositoryImpl implements ReminderRepository {
    private ReminderDbHelper dbHelper;
    private MutableLiveData<List<Reminder>> reminders;

    public ReminderRepositoryImpl(Context context) {
        dbHelper = new ReminderDbHelper(context);
        reminders = new MutableLiveData<>();
        loadReminders();
    }

    @Override
    public MutableLiveData<List<Reminder>> getReminders() {
        return reminders;
    }

    @Override
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
        values.put(ReminderContract.ReminderEntry.COLUMN_NAME_COMPLETED, reminder.isCompleted());

        long newRowId = db.insert(ReminderContract.ReminderEntry.TABLE_NAME, null, values);
        loadReminders();
    }

    @Override
    public void deleteReminder(Reminder reminder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = ReminderContract.ReminderEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(reminder.getId())};
        db.delete(ReminderContract.ReminderEntry.TABLE_NAME, selection, selectionArgs);
        loadReminders();
    }

    @Override
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
                ReminderContract.ReminderEntry.COLUMN_NAME_COMPLETED
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
            boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderContract.ReminderEntry.COLUMN_NAME_COMPLETED)) == 1;

            reminders.add(new TextReminder(id, name, info, day, month, year, hour, minute, completed));
        }
        cursor.close();

        this.reminders.setValue(reminders);
    }
}

