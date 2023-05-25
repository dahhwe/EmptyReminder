package com.example.emptyreminder;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;

public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<List<Reminder>> reminders;
    private ReminderRepository reminderRepository;

    public MainViewModel(Application application) {
        super(application);
        reminderRepository = new ReminderRepository(application.getApplicationContext());
        reminders = reminderRepository.getReminders();
    }

    public LiveData<List<Reminder>> getReminders() {
        return reminders;
    }

    public void deleteReminder(Reminder id) {
        reminderRepository.deleteReminder(id);
    }

    // add new updateReminder method
    public void updateReminder(Reminder reminder) {
        reminderRepository.updateReminder(reminder);
    }
}
