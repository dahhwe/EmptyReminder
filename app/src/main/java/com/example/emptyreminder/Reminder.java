package com.example.emptyreminder;


public interface Reminder {
    long getId();

    String getName();

    String getInfo();

    int getDay();

    int getMonth();

    int getYear();

    int getHour();

    int getMinute();

    boolean isCompleted();

    void setName(String name);

    void setInfo(String info);

    void setDay(int day);

    void setMonth(int month);

    void setYear(int year);

    void setHour(int hour);

    void setMinute(int minute);

    void setCompleted(boolean completed);

    String getDate();

    String getTime();
}


