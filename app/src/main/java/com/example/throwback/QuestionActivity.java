package com.example.throwback;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    public static final String QUESTION = "com.example.throwback.question";
    public static final String QUESTION_CORRECT_YEAR = "com.example.throwback.correctYear";
    public static final String QUESTION_GUESS_YEAR = "com.example.throwback.guessYear";

    public static int correctYear;
    public static String question;
    public int number_help;
    public DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        TextView questionTextField = (TextView) findViewById(R.id.question);

        databaseHandler = new DatabaseHandler(this);
        number_help = 0;

        Headline headline = databaseHandler.getRandomHeadlines().get(0);
        correctYear = Integer.parseInt(headline.getDate().split("-")[0]);
        question = headline.getHeadline();
        questionTextField.setText(headline.getHeadline());
    }

    /** Called when the user taps the Start Game button */
    public void guessYear(View view) {

        EditText editText = findViewById(R.id.guessTheYear);
        int yearGuessed = Integer.parseInt(editText.getText().toString());

        MainActivity.TOTAL_ANSWERS++;

        if (yearGuessed == correctYear){
            MainActivity.NUMBER_CORRECT_ANSWERS++;
        }
        Intent intent = new Intent(this, AnswerToQuestion.class);
        intent.putExtra(QUESTION, question);
        intent.putExtra(QUESTION_GUESS_YEAR, yearGuessed);
        intent.putExtra(QUESTION_CORRECT_YEAR, correctYear);

        startActivity(intent);

    }

    public void getHelp(View view) {

        number_help++;

        List<Headline> helpHeadlines = databaseHandler.getNewsByYear(correctYear, 1);
        TextView questionTextField = (TextView) findViewById(R.id.question);
        questionTextField.setText(helpHeadlines.get(0).getHeadline());
        question = helpHeadlines.get(0).getHeadline();

        if (number_help == MainActivity.NUMBER_MAXIMUM_HELP) {
            view.setVisibility(View.GONE);
        }
    }
}
