package com.example.emptyreminder;

import android.content.Context;

public class ServiceLocator {
    private static ReminderRepository reminderRepository;

    public static ReminderRepository provideReminderRepository(Context context) {
        if (reminderRepository == null) {
            reminderRepository = (ReminderRepository) new ReminderRepositoryImpl(context);
        }
        return reminderRepository;
    }
}


