package com.example.throwback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        String totalAnswers = intent.getStringExtra(GameActivity.TOTAL_ANSWERS_NAME);

        TextView textPoints = findViewById(R.id.pontos);
        TextView textCorrectAnswers = findViewById(R.id.finalCorrectAnswers);
        TextView textTotalAnswers = findViewById(R.id.valueTotalAnswers);

        textPoints.setText(gamePoints);
        textCorrectAnswers.setText(correctAnswers);
        textTotalAnswers.setText(totalAnswers);
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

}
