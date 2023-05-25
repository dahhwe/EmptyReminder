package com.example.emptyreminder;

import android.annotation.SuppressLint;

public class TextReminder implements Reminder {
    private long id;
    private String name;
    private String info;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;
    private boolean completed;

    public TextReminder(long id, String name, String info, int day,
                        int month, int year, int hour, int minute, boolean completed) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.completed = completed;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public int getDay() {
        return day;
    }

    @Override
    public int getMonth() {
        return month;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public int getHour() {
        return hour;
    }

    @Override
    public int getMinute() {
        return minute;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public void setHour(int hour) {
        this.hour = hour;
    }

    @Override
    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDate() {
        return String.format("%02d/%02d/%04d", day, month + 1, year);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getTime() {
        return String.format("%02d:%02d", hour, minute);
    }
}
