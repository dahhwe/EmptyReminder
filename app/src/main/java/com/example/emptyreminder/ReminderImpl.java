package com.example.emptyreminder;

import android.annotation.SuppressLint;

public class ReminderImpl implements Reminder {
    private long id;
    private String name;
    private String info;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;
    private boolean completed;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDate() {
        return String.format("%02d/%02d/%04d", getDay(), getMonth() + 1, getYear());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getTime() {
        return String.format("%02d:%02d", getHour(), getMinute());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}

