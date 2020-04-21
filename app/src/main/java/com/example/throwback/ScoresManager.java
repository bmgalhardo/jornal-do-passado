package com.example.throwback;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class ScoresManager {

    private List<Score> scores;
    private final static String sep = "\t";

    public ScoresManager(List<Score> scores) {
        this.scores = scores;
    }

    public ScoresManager(Score score){
        scores = new ArrayList<>();
        scores.add(score);
    }

    public ScoresManager(String scoresStr){
        scores = parseObjectFromString(scoresStr);
    }

    @NonNull
    @Override
    public String toString() {

        StringBuilder st = new StringBuilder();
        for (int i = 0; i < scores.size(); i++) {
            st.append(scores.get(i).toString()).append(sep);
        }
        return st.toString();
    }

    public static List<Score> parseObjectFromString(String savedString){

        if (savedString == null) return new ArrayList<>();

        StringTokenizer st = new StringTokenizer(savedString, sep);
        List<Score> scoreList = new ArrayList<>();

        while (st.hasMoreTokens()){
            scoreList.add(Score.parseObjectFromString(st.nextToken()));
        }
        return scoreList;
    }

    public String addNewElement(Score score) {

        scores.add(score);

        Collections.sort(scores, new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                return o2.getScore().compareTo(o1.getScore());
            }
        });

        if (scores.size() < 5) return this.toString();
        else {
            scores = scores.subList(0, 5);
            return this.toString();
        }
    }
}
