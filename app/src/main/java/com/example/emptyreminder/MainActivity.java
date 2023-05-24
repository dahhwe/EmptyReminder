package com.example.emptyreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateReminders();
            handler.sendEmptyMessageDelayed(0, 60000);
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.btn_add_reminder);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReminderAddActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateReminders();
        handler.sendEmptyMessageDelayed(0, 30000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeMessages(0);
    }

    // MainActivity.java
    @SuppressLint({"SetTextI18n", "RtlHardcoded"})
    private void updateReminders() {
        // Query the database and create views for each reminder
        ReminderDbHelper dbHelper = new ReminderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                ReminderContract.ReminderEntry._ID,
                ReminderContract.ReminderEntry.COLUMN_NAME_NAME,
                ReminderContract.ReminderEntry.COLUMN_NAME_INFO,
                ReminderContract.ReminderEntry.COLUMN_NAME_DAY,
                ReminderContract.ReminderEntry.COLUMN_NAME_MONTH,
                ReminderContract.ReminderEntry.COLUMN_NAME_YEAR,
                ReminderContract.ReminderEntry.COLUMN_NAME_HOUR,
                ReminderContract.ReminderEntry.COLUMN_NAME_MINUTE
        };

        Cursor cursor = db.query(
                ReminderContract.ReminderEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                ReminderContract.ReminderEntry.COLUMN_NAME_YEAR + " ASC, " +
                        ReminderContract.ReminderEntry.COLUMN_NAME_MONTH + " ASC, " +
                        ReminderContract.ReminderEntry.COLUMN_NAME_DAY + " ASC, " +
                        ReminderContract.ReminderEntry.COLUMN_NAME_HOUR + " ASC, " +
                        ReminderContract.ReminderEntry.COLUMN_NAME_MINUTE + " ASC"
        );

        TextView noRemindersTextView = (TextView) findViewById(R.id.no_reminders_text_view);
        noRemindersTextView.setVisibility(cursor.getCount() == 0 ? View.VISIBLE : View.GONE);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(ReminderContract.ReminderEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ReminderContract.ReminderEntry.COLUMN_NAME_NAME));
            String info = cursor.getString(cursor.getColumnIndexOrThrow(ReminderContract.ReminderEntry.COLUMN_NAME_INFO));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderContract.ReminderEntry.COLUMN_NAME_DAY));
            int month = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderContract.ReminderEntry.COLUMN_NAME_MONTH));
            int year = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderContract.ReminderEntry.COLUMN_NAME_YEAR));
            int hour = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderContract.ReminderEntry.COLUMN_NAME_HOUR));
            int minute = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderContract.ReminderEntry.COLUMN_NAME_MINUTE));

            // Check if the reminder is out of date
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day, hour, minute);
            boolean isOutOfDate = calendar.getTimeInMillis() < System.currentTimeMillis();

            // Create a view for this reminder and add it to the LinearLayout
            LinearLayout reminderLayout = new LinearLayout(this);
            reminderLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(16, 16, 16, 16);
            reminderLayout.setLayoutParams(layoutParams);
            reminderLayout.setBackgroundResource(isOutOfDate ? R.drawable.reminder_background_out_of_date : R.drawable.reminder_background);

            TextView nameTextView = new TextView(this);
            nameTextView.setText(name);
            nameTextView.setTextSize(35);
            nameTextView.setPadding(16, 16, 16, 0);
            reminderLayout.addView(nameTextView);

            TextView infoTextView = new TextView(this);
            infoTextView.setText(info);
            infoTextView.setPadding(16, 0, 16, 0);
            infoTextView.setTextSize(25);
            reminderLayout.addView(infoTextView);

            TextView dueLabelTextView = new TextView(this);
            dueLabelTextView.setText("Due:");
            infoTextView.setTextSize(25);
            dueLabelTextView.setPadding(16, 0, 16, 0);
            reminderLayout.addView(dueLabelTextView);

            TextView dateTimeTextView = new TextView(this);
            dateTimeTextView.setText(day + "/" + month + "/" + year + " " + hour + ":" + minute);
            dateTimeTextView.setTextSize(20);
            dateTimeTextView.setPadding(16, 0, 16, 16);
            reminderLayout.addView(dateTimeTextView);

            LinearLayout buttonsLayout = new LinearLayout(this);
            buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
            buttonsLayout.setGravity(Gravity.RIGHT);

            Button completeButton = new Button(this);
            completeButton.setText("✓");
            completeButton.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
            completeButton.setOnClickListener(v -> {
                // Mark the reminder as completed
            });
            buttonsLayout.addView(completeButton);

            Button deleteButton = new Button(this);
            deleteButton.setText("✗");
            deleteButton.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
            deleteButton.setOnClickListener(v -> {
                // Delete the reminder from the database
                String selection = ReminderContract.ReminderEntry._ID + " LIKE ?";
                String[] selectionArgs = {String.valueOf(id)};
                db.delete(ReminderContract.ReminderEntry.TABLE_NAME, selection, selectionArgs);

                // Update the list of reminders
                updateReminders();

                // Display a message
                Toast.makeText(MainActivity.this, "Reminder deleted", Toast.LENGTH_SHORT).show();
            });
            buttonsLayout.addView(deleteButton);

            reminderLayout.addView(buttonsLayout);

            linearLayout.addView(reminderLayout);
        }


        cursor.close();
    }


    public void openReminderAddActivity() {
        Intent intent = new Intent(this, AddReminder.class);
        startActivity(intent);
    }

    protected void add_reminder(View add_btn) {
        add_btn.setActivated(false);
    }
}
