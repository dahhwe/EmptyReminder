package com.example.emptyreminder;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface ReminderRepository {
    LiveData<List<Reminder>> getReminders();
    void addReminder(Reminder reminder);
    void updateReminder(Reminder reminder);
    void deleteReminder(Reminder reminder);
}

