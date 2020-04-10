package com.example.throwback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class AnswerToQuestion extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_to_question);

        Intent intent = getIntent();

        String guessYearStr = intent.getStringExtra(QuestionActivity.QUESTION_GUESS_YEAR);
        int correctYear = intent.getIntExtra(QuestionActivity.QUESTION_CORRECT_YEAR, 0);
        String question = intent.getStringExtra(QuestionActivity.QUESTION);

        Integer guessYear = null;
        if (guessYearStr != null && guessYearStr.length() > 0) {
            guessYear = Integer.parseInt(guessYearStr);
            if (guessYear == correctYear) {
                MainActivity.NUMBER_CORRECT_ANSWERS++;
            }
        }
        TextView viewQuestion = findViewById(R.id.question_repeated);
        viewQuestion.setText(question);

        TextView viewGuessYear = findViewById(R.id.yearAnswered);

        if (guessYear != null) {
            viewGuessYear.setText(guessYearStr);
        }

        TextView viewCorrectYear = findViewById(R.id.yearCorrect);
        viewCorrectYear.setText(Integer.toString(correctYear));

        TextView viewTotalAnswers = findViewById(R.id.totalAnswers);
        viewTotalAnswers.setText(Integer.toString(MainActivity.TOTAL_ANSWERS));

        TextView viewCorrectAnswers = findViewById(R.id.correctAnswers);
        viewCorrectAnswers.setText(Integer.toString(MainActivity.NUMBER_CORRECT_ANSWERS));


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if (MainActivity.TOTAL_ANSWERS < MainActivity.TOTAL_NUMBER_QUESTIONS_PER_GAME) {
                    i = new Intent(AnswerToQuestion.this, QuestionActivity.class);
                }
                else {
                    i = new Intent(AnswerToQuestion.this, FinalGameStatistics.class);
                }
                startActivity(i);
                finish();
            }
        }, MainActivity.TIME_SHOW_CORRECT_ANSWER);

    }
}
