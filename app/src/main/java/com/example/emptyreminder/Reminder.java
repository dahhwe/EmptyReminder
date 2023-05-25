package com.example.emptyreminder;

import android.annotation.SuppressLint;

public class Reminder {
    private long id;
    private String name;
    private String info;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;
    private boolean completed; // add new completed property

    public Reminder(long id, String name, String info, int day, int month, int year, int hour, int minute, boolean completed) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.completed = completed; // initialize completed property to false
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    @SuppressLint("DefaultLocale")
    public String getDate() {
        return String.format("%02d/%02d/%04d", day, month + 1, year);
    }

    @SuppressLint("DefaultLocale")
    public String getTime() {
        return String.format("%02d:%02d", hour, minute);
    }

    // add new isCompleted method
    public boolean isCompleted() {
        return completed;
    }

    // add new setCompleted method
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

