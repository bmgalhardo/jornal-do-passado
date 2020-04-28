package com.example.throwback;

public class Headline {

    private int id;
    private String headline;
    private String url;
    private String date;
    private int order;
    private int numberWords;
    private String newspaper;
    private int year;
    private int month;
    private int day;

    public Headline(int id, String headline, String url, String date, int order, int numberWords, String newspaper, int year, int month, int day) {
        this.id = id;
        this.headline = headline;
        this.url = url;
        this.date = date;
        this.order = order;
        this.numberWords = numberWords;
        this.newspaper = newspaper;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Headline(String headline, String url, String date, int order, int numberWords, String newspaper, int year, int month, int day) {
        this.headline = headline;
        this.url = url;
        this.date = date;
        this.order = order;
        this.numberWords = numberWords;
        this.newspaper = newspaper;
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

    public int getOrder() {
        return order;
    }

    public int getNumberWords() {
        return numberWords;
    }

    public String getNewspaper() {
        return newspaper;
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
