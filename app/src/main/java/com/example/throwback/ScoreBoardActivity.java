package com.example.throwback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class ScoreBoardActivity extends AppCompatActivity {

    private GameType gameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_board);

        Intent intent = getIntent();
        gameType = GameType.valueOf(intent.getStringExtra(MainActivity.EXTRA_GAME_TYPE));

        String gamePoints = intent.getStringExtra(GameActivity.CURRENT_POINTS_NAME);
        String correctAnswers = intent.getStringExtra(GameActivity.CORRECT_ANSWERS_NAME);
        String correctAnswers_1 = intent.getStringExtra(GameActivity.CORRECT_ANSWERS_NAME_1);
        String correctAnswers_2 = intent.getStringExtra(GameActivity.CORRECT_ANSWERS_NAME_2);
        String wrong_answers = intent.getStringExtra(GameActivity.WRONG_ANSWERS_NAME);
        String totalAnswers = intent.getStringExtra(GameActivity.TOTAL_ANSWERS_NAME);

        TextView textPoints = findViewById(R.id.pontos);

        textPoints.setText(gamePoints);

        Score score = new Score(gamePoints, DateUtils.getToday(false), totalAnswers);

        if (gameType == GameType.DEFAULT) saveToDb("scoresDefault", score);
        else if (gameType == GameType.SUDDEN_DEATH) saveToDb("scoresSuddenDeath", score);

        // Fill game stats
        TableLayout table = findViewById(R.id.statsTab);

        final int alternateColor = getResources().getColor(R.color.light_color);

        for (int i = 0; i < 5; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.FILL_PARENT, 1.0f));

            if (i % 2 == 0) row.setBackgroundColor(alternateColor);

            String key;
            String score_item;

            if (i == 0) {
                key = "Respostas certeiras";
                score_item = correctAnswers;
            } else if (i == 1) {
                key = "Respostas quase certas";
                score_item = correctAnswers_1;
            } else if (i == 2) {
                key = "Respostas ao lado";
                score_item = correctAnswers_2;
            } else if (i == 3)  {
                key = "Respostas erradas";
                score_item = wrong_answers;
            } else {
                key = "Total de Perguntas";
                score_item = totalAnswers;
            }
            for (int j = 0; j < 2; j++) {
                // text
                TextView tv = new TextView(this);
                if (j == 0) tv.setText(key);
                else tv.setText(score_item);

                tv.setGravity(Gravity.CENTER);
                TextViewCompat.setAutoSizeTextTypeWithDefaults(tv, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                tv.setPadding(1, 1, 1, 1);

                row.addView(tv);
            }
            table.addView(row);
        }
        table.setStretchAllColumns(true);

    }

    private void saveToDb(String keyString, Score score) {
        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("throwback", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        String currentString = sharedPreferences.getString(keyString, null);
        String newString =  new ScoresManager(currentString).addNewElement(score);
        myEdit.putString(keyString, newString.toString());
        myEdit.apply();
    }

    public void startNewGame(View view){
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra(MainActivity.EXTRA_GAME_TYPE, gameType.toString());
        startActivity(i);
    }
    public void backToMenu(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    public void showNews(View view){
        Intent i = new Intent(this, ScrollingNews.class);
        startActivity(i);
    }
}
