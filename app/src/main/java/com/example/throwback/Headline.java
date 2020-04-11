package com.example.throwback;

public class Headline {

    private int id;
    private String headline;
    private String url;
    private String date;
    private int year;
    private int month;
    private int day;

    public Headline(int id, String headline, String url, String date, int year, int month, int day) {
        this.id = id;
        this.headline = headline;
        this.url = url;
        this.date = date;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Headline(String headline, String url, String date, int year, int month, int day) {
        this.headline = headline;
        this.url = url;
        this.date = date;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public String getHeadline() {
        return headline;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}
