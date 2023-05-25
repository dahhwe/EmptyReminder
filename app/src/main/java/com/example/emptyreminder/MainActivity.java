package com.example.emptyreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    private NotificationHelper notificationHelper;
    private AlarmHelper alarmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationHelper = new NotificationHelper(this);
        alarmHelper = new AlarmHelper(this);

        notificationHelper.createNotificationChannel();

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getReminders().observe(this, this::updateReminders);

        Button button = findViewById(R.id.btn_add_reminder);
        button.setOnClickListener(v -> openReminderAddActivity());
    }

    private void updateReminders(List<Reminder> reminders) {
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();

        TextView noRemindersTextView = findViewById(R.id.no_reminders_text_view);
        noRemindersTextView.setVisibility(reminders.isEmpty() ? View.VISIBLE : View.GONE);

        for (Reminder reminder : reminders) {

            ReminderView reminderView = new ReminderView(this);
            reminderView.setReminder(reminder);

            reminderView.setOnDeleteClickListener(v -> {
                viewModel.deleteReminder(reminder);
                Toast.makeText(this, "Reminder deleted", Toast.LENGTH_SHORT).show();
                alarmHelper.cancelAlarm(reminder);
            });

            reminderView.setOnCompleteClickListener(v -> {
                viewModel.toggleCompleted(reminder);
                Toast.makeText(this, "Reminder updated", Toast.LENGTH_SHORT).show();
                if (reminder.isCompleted()) {
                    alarmHelper.cancelAlarm(reminder);
                } else {
                    alarmHelper.scheduleAlarm(reminder);
                }
                reminderView.setReminder(reminder);
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

            if (!isOutOfDate(reminder) && !reminder.isCompleted()) {
                alarmHelper.scheduleAlarm(reminder);
            }
        }
    }

    private boolean isOutOfDate(Reminder reminder) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(reminder.getYear(), reminder.getMonth(), reminder.getDay(), reminder.getHour(), reminder.getMinute());
        return calendar.getTimeInMillis() < System.currentTimeMillis();
    }

    public void openReminderAddActivity() {
        Intent intent = new Intent(this, AddReminder.class);
        startActivity(intent);
    }
}

