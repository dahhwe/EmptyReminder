package com.example.emptyreminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import com.example.emptyreminder.ReminderContract;

public class ReminderDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Reminders.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ReminderContract.ReminderEntry.TABLE_NAME + " (" +
                    ReminderContract.ReminderEntry._ID + " INTEGER PRIMARY KEY," +
                    ReminderContract.ReminderEntry.COLUMN_NAME_NAME + " TEXT," +
                    ReminderContract.ReminderEntry.COLUMN_NAME_INFO + " TEXT," +
                    ReminderContract.ReminderEntry.COLUMN_NAME_DAY + " INTEGER," +
                    ReminderContract.ReminderEntry.COLUMN_NAME_MONTH + " INTEGER," +
                    ReminderContract.ReminderEntry.COLUMN_NAME_YEAR + " INTEGER," +
                    ReminderContract.ReminderEntry.COLUMN_NAME_HOUR + " INTEGER," +
                    ReminderContract.ReminderEntry.COLUMN_NAME_MINUTE + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ReminderContract.ReminderEntry.TABLE_NAME;

    public ReminderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
