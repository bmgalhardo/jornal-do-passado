package com.example.throwback;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final int TOTAL_NUMBER_QUESTIONS_PER_GAME = 4;
    public static final int TIME_SHOW_FINAL_ANSWER = 4000;
    public static final int TIME_SHOW_FINAL_STATISTICS = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // It is here to create the database for the first time
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Start Game button */
    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}
