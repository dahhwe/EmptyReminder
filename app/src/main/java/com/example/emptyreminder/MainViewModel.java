package com.example.emptyreminder;

import android.app.Application;
import android.icu.util.Calendar;

import androidx.lifecycle.LiveData;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Transformations;

public class MainViewModel extends AndroidViewModel {
    private ReminderRepository reminderRepository;
    private ReminderSorter reminderSorter;
    private LiveData<List<Reminder>> reminders;

    public MainViewModel(Application application) {
        super(application);
        reminderRepository = ServiceLocator.provideReminderRepository(application.getApplicationContext());
        reminderSorter = new DueDateReminderSorter();
        reminders = Transformations.map(reminderRepository.getReminders(), reminderSorter::sortRemindersByDueDate);
    }

    public LiveData<List<Reminder>> getReminders() {
        return reminders;
    }

    public void deleteReminder(Reminder reminder) {
        reminderRepository.deleteReminder(reminder);
    }

    public void toggleCompleted(Reminder reminder) {
        boolean completed = !reminder.isCompleted();
        reminder.setCompleted(completed);
        reminderRepository.updateReminder(reminder);
    }
}