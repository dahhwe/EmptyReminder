package com.example.emptyreminder;

import android.icu.util.Calendar;

import java.util.List;

interface ReminderSorter {
    List<Reminder> sortRemindersByDueDate(List<Reminder> reminders);
}

class DueDateReminderSorter implements ReminderSorter {
    @Override
    public List<Reminder> sortRemindersByDueDate(List<Reminder> reminders) {
        reminders.sort((r1, r2) -> {
            Calendar c1 = Calendar.getInstance();
            c1.set(r1.getYear(), r1.getMonth(), r1.getDay(), r1.getHour(), r1.getMinute());

            Calendar c2 = Calendar.getInstance();
            c2.set(r2.getYear(), r2.getMonth(), r2.getDay(), r2.getHour(), r2.getMinute());

            return c1.compareTo(c2);
        });

        return reminders;
    }
}
