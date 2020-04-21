package com.example.throwback;

import androidx.annotation.NonNull;

import java.util.StringTokenizer;

public class Score {

    private final static String sep = ";";

    private GameType gameType;

    private String score;
    private String date;
    private String numberOfQuestions;

    public Score(String score, String date, String numberOfQuestions) {
        this.score = score;
        this.date = date;
        this.numberOfQuestions = numberOfQuestions;
    }

    @NonNull
    @Override
    public String toString() {
        return score + sep + date + sep + numberOfQuestions;
    }

    public static Score parseObjectFromString(String savedString){

        StringTokenizer st = new StringTokenizer(savedString, sep);

        String score = st.nextToken();
        String date = st.nextToken();
        String numberQuestions = st.nextToken();

        return new Score(score, date, numberQuestions);
    }

    public static String getSep() {
        return sep;
    }

    public String getScore() {
        return score;
    }

    public String getDate() {
        return date;
    }

    public String getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public String[] getArray() {
        return new String[]{score, numberOfQuestions, date};
    }
}
