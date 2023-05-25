package com.example.emptyreminder;

import android.provider.BaseColumns;

public final class ReminderContract {
    private ReminderContract() {}

    public static class ReminderEntry implements BaseColumns {
        public static final String TABLE_NAME = "reminders";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_INFO = "info";
        public static final String COLUMN_NAME_DAY = "day";
        public static final String COLUMN_NAME_MONTH = "month";
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_HOUR = "hour";
        public static final String COLUMN_NAME_MINUTE = "minute";
        public static final String COLUMN_NAME_COMPLETED = "completed"; // add new constant for completed column
    }
}
