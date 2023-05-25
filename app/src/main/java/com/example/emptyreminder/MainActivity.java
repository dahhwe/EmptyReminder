package com.example.emptyreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the notification channel
        createNotificationChannel();

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // update UI with new reminders
        viewModel.getReminders().observe(this, this::updateReminders);

        Button button = findViewById(R.id.btn_add_reminder);
        button.setOnClickListener(v -> openReminderAddActivity());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("reminder_channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateReminders(List<Reminder> reminders) {
        // Sort reminders by due date
        reminders.sort((r1, r2) -> {
            Calendar c1 = Calendar.getInstance();
            c1.set(r1.getYear(), r1.getMonth(), r1.getDay(), r1.getHour(), r1.getMinute());

            Calendar c2 = Calendar.getInstance();
            c2.set(r2.getYear(), r2.getMonth(), r2.getDay(), r2.getHour(), r2.getMinute());

            return c1.compareTo(c2);
        });

        // update UI with new reminders
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();

        TextView noRemindersTextView = findViewById(R.id.no_reminders_text_view);
        noRemindersTextView.setVisibility(reminders.isEmpty() ? View.VISIBLE : View.GONE);

        for (Reminder reminder : reminders) {

            // Check if the reminder is out of date
            Calendar calendar = Calendar.getInstance();
            calendar.set(reminder.getYear(), reminder.getMonth(), reminder.getDay(), reminder.getHour(), reminder.getMinute());
            boolean isOutOfDate = calendar.getTimeInMillis() < System.currentTimeMillis();

            // create a view for this reminder and add it to the LinearLayout
            View reminderView = getLayoutInflater().inflate(R.layout.reminder_item, null);
            reminderView.setBackgroundResource(isOutOfDate ? R.drawable.reminder_background_out_of_date : R.drawable.reminder_background);

            TextView nameTextView = reminderView.findViewById(R.id.name_text_view);
            nameTextView.setText(reminder.getName());

            TextView infoTextView = reminderView.findViewById(R.id.info_text_view);
            infoTextView.setText(reminder.getInfo());

            // Set the text and color of the completedTextView based on whether the reminder is completed or not
            TextView completedTextView = reminderView.findViewById(R.id.completed_text_view);
            if (reminder.isCompleted()) {
                completedTextView.setText("Completed");
                completedTextView.setTextColor(Color.GREEN);
            } else {
                completedTextView.setText("Incomplete");
                completedTextView.setTextColor(Color.RED);
            }

            TextView timeTextView = reminderView.findViewById(R.id.time_text_view);
            timeTextView.setText(reminder.getTime());

            TextView dateTextView = reminderView.findViewById(R.id.date_text_view);
            dateTextView.setText(reminder.getDate());

            ImageButton deleteButton = reminderView.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(v -> {
                viewModel.deleteReminder(reminder);
                updateReminders(Objects.requireNonNull(viewModel.getReminders().getValue()));
                Toast.makeText(this, "Reminder deleted", Toast.LENGTH_SHORT).show();
                cancelAlarm(reminder); // Cancel the alarm when the reminder is deleted
            });

            // Add an OnClickListener for the complete button
            ImageButton completeButton = reminderView.findViewById(R.id.complete_button);
            completeButton.setOnClickListener(v -> {
                // Add your code here to handle when the complete button is clicked
                // For example, you could update the reminder's completed status in the database and refresh the UI
                reminder.setCompleted(!reminder.isCompleted());
                viewModel.updateReminder(reminder);
                updateReminders(Objects.requireNonNull(viewModel.getReminders().getValue()));
                Toast.makeText(this, "Reminder updated", Toast.LENGTH_SHORT).show();
                if (reminder.isCompleted()) {
                    cancelAlarm(reminder); // Cancel the alarm when the reminder is marked as completed
                } else {
                    scheduleAlarm(reminder); // Reschedule the alarm when the reminder is marked as incomplete again
                }
            });

            linearLayout.addView(reminderView);

            // Add space between each reminder box
            View space = new View(this);
            space.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    16
            ));
            space.setBackgroundColor(Color.TRANSPARENT);
            linearLayout.addView(space);

            if (!isOutOfDate && !reminder.isCompleted()) {
                scheduleAlarm(reminder); // Schedule an alarm for this reminder
            }
        }
    }

    private void scheduleAlarm(Reminder reminder) {
        // Create an Intent to trigger the ReminderAlarmReceiver
        Intent intent = new Intent(this, ReminderAlarmReceiver.class);
        intent.putExtra(ReminderAlarmReceiver.REMINDER_TITLE, reminder.getName());
        intent.putExtra(ReminderAlarmReceiver.REMINDER_TEXT, reminder.getInfo());

        // Create a PendingIntent to be triggered when the alarm goes off
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) reminder.getId(), intent, PendingIntent.FLAG_IMMUTABLE);

        // Schedule the alarm
        Calendar calendar = Calendar.getInstance();
        calendar.set(reminder.getYear(), reminder.getMonth(), reminder.getDay(), reminder.getHour(), reminder.getMinute());
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm(Reminder reminder) {
        Intent intent = new Intent(this, ReminderAlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) reminder.getId(), intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent); // Cancel any previously scheduled alarms for this reminder
    }

    public void openReminderAddActivity() {
        Intent intent = new Intent(this, AddReminder.class);
        startActivity(intent);
    }
}


