package com.example.throwback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class ScoreBoardActivity extends AppCompatActivity {

    private GameType gameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_board);

        TextView viewTotalAnswers = findViewById(R.id.finalTotalAnswers);
        viewTotalAnswers.setText(Integer.toString(GameActivity.TOTAL_ANSWERS));

        TextView viewCorrectAnswers = findViewById(R.id.finalCrrectAnswers);
        viewCorrectAnswers.setText(Integer.toString(GameActivity.NUMBER_CORRECT_ANSWERS));

        Intent intent = getIntent();
        gameType = GameType.valueOf(intent.getStringExtra(MainActivity.EXTRA_GAME_TYPE));

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
