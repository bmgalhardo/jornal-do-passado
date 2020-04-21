package com.example.throwback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class TopScoreBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_score_board);

        SharedPreferences sharedPreferences = getSharedPreferences("throwback", MODE_PRIVATE);
        String scoreString = sharedPreferences.getString("scores", null);
        TableLayout table = findViewById(R.id.normalTab);
        List<Score> scoreList = ScoresManager.parseObjectFromString(scoreString);

        final int headerColor = getResources().getColor(R.color.colorButtons);
        final int alternateColor = getResources().getColor(R.color.tries);
        String[] elements;

        for (int i = 0; i < scoreList.size() + 1; i++) {
            TableRow row = new TableRow(this);
            if (i == 0) row.setBackgroundColor(headerColor);
            else if (i % 2 == 0) row.setBackgroundColor(alternateColor);

            if (i == 0) elements = new String[] {"Pontuação", "No Perguntas", "Data"};
            else elements = scoreList.get(i - 1).getArray();

            for (int j = 0; j < 3; j++) {
                TextView tv = new TextView(this);
                tv.setText(elements[j]);
                if (i == 0) tv.setTextColor(Color.WHITE);
                row.addView(tv);
            }
            table.addView(row);

        }
    }
}
