package com.example.throwback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class TopScoreBoard extends AppCompatActivity {

    int NUMBER_OF_ROWS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_score_board);

        getSupportActionBar().setTitle(R.string.scores);

        SharedPreferences sharedPreferences = getSharedPreferences("throwback", MODE_PRIVATE);
        String scoresDefault = sharedPreferences.getString("scoresDefault", null);
        String scoresSuddenDeath = sharedPreferences.getString("scoresSuddenDeath", null);
        TableLayout table = findViewById(R.id.normalTab);
        TableLayout table2 = findViewById(R.id.suddenDeathTab);

        fillTable(table, scoresDefault);
        fillTable(table2, scoresSuddenDeath);
    }

    public void fillTable(TableLayout table, String scoreString){

        final int headerColor = getResources().getColor(R.color.colorButtons);
        final int alternateColor = getResources().getColor(R.color.light_color);

        String[] elements;

        List<Score> scoreList = ScoresManager.parseObjectFromString(scoreString);

        for (int i = 0; i < NUMBER_OF_ROWS + 1; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
            if (i == 0) row.setBackgroundColor(headerColor);
            else if (i % 2 == 0) row.setBackgroundColor(alternateColor);

            if (i == 0) elements = new String[] {"Pontos", "Perguntas", "Data"};
            else if (i < scoreList.size() + 1) elements = scoreList.get(i - 1).getArray();
            else elements = new String[] {"---", "---", "---"};

            for (int j = 0; j < 3; j++) {
                // text
                TextView tv = new TextView(this);
                tv.setText(elements[j]);
                tv.setGravity(Gravity.CENTER);
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,1));
                tv.setTextSize(14);
//                TextViewCompat.setAutoSizeTextTypeWithDefaults(tv, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                tv.setPadding(1, 1, 1, 1);
                if (i == 0) {
                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setTextColor(Color.WHITE);
                }
                row.addView(tv);
            }
            table.addView(row);
        }
        table.setStretchAllColumns(true);
    }
}
