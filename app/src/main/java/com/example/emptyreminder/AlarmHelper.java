package com.example.emptyreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;

public class AlarmHelper {
    private Context context;
    private AlarmManager alarmManager;

    public AlarmHelper(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void scheduleAlarm(Reminder reminder) {
        PendingIntent pendingIntent = createPendingIntent(reminder);

        Calendar calendar = Calendar.getInstance();
        calendar.set(reminder.getYear(), reminder.getMonth(), reminder.getDay(), reminder.getHour(), reminder.getMinute());

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public void cancelAlarm(Reminder reminder) {
        PendingIntent pendingIntent = createPendingIntent(reminder);

        alarmManager.cancel(pendingIntent);
    }

    private PendingIntent createPendingIntent(Reminder reminder) {
        Intent intent = new Intent(context, ReminderAlarmReceiver.class);
        intent.putExtra(ReminderAlarmReceiver.REMINDER_TITLE, reminder.getName());
        intent.putExtra(ReminderAlarmReceiver.REMINDER_TEXT, reminder.getInfo());

        return PendingIntent.getBroadcast(context, (int) reminder.getId(), intent, PendingIntent.FLAG_IMMUTABLE);
    }
}


