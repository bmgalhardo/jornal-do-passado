package com.example.throwback;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lukelorusso.verticalseekbar.VerticalSeekBar;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class GameActivity extends AppCompatActivity {

    public static final Integer[] TEXT_FIELDS_QUESTIONS = {R.id.question1, R.id.question2,
            R.id.question3};

    public static int MIN_YEAR = 2001;
    public static int MAX_YEAR = 2020;

    public static int CURRENT_LEVEL;
    final public static String CURRENT_POINTS_NAME = "CURRENT_POINTS_NAME";
    final public static String CORRECT_ANSWERS_NAME = "CORRECT_ANSWERS_NAME";
    final public static String CORRECT_ANSWERS_NAME_1 = "CORRECT_ANSWERS_NAME_1";
    final public static String CORRECT_ANSWERS_NAME_2 = "CORRECT_ANSWERS_NAME_2";
    final public static String WRONG_ANSWERS_NAME = "WRONG_ANSWERS_NAME";
    final public static String TOTAL_ANSWERS_NAME = "TOTAL_ANSWERS_NAME";

    final public static int TIME_TO_ANSWER = 15; // seconds

    public static int CORRECT_YEAR;

    private GameType gameType;
    private List<Headline> headlinesSelected;

    public int number_help;
    public DatabaseHandler databaseHandler;

    public int yearGuess;
    public int startYear;
    private int currentPoints;
    private int numberCorrectAnswers;
    private int numberCorrectAnswers_1;
    private int numberCorrectAnswers_2;
    private int wrong_answers;
    private int totalAnswers;

    public int gauntletLevel = 5;
    private boolean guessing;
    private CountDownTimer timer;
    public static List<Headline> headlineArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        headlineArray = new ArrayList<Headline>();

        Intent intent = getIntent();
        gameType = GameType.valueOf(intent.getStringExtra(MainActivity.EXTRA_GAME_TYPE));

        if (gameType == GameType.DEFAULT) getSupportActionBar().setTitle(R.string.start_game);
        else getSupportActionBar().setTitle(R.string.start_game_tower);

        numberCorrectAnswers = 0;
        numberCorrectAnswers_1 = 0;
        numberCorrectAnswers_2 = 0;
        wrong_answers = 0;
        totalAnswers = 0;
        CURRENT_LEVEL = 0;
        currentPoints = 0;

        databaseHandler = new DatabaseHandler(this);

        number_help = 0;

        final int colorTry = getResources().getColor(R.color.tries);

        VerticalSeekBar yearBar = findViewById(R.id.yearBar);
        // get year boxes
        final ViewGroup yearBoxes = findViewById(R.id.yearBoxes);
        final int yearOptions = yearBoxes.getChildCount();
        final int colorback = getResources().getColor(R.color.background);

        // Added listener to the bar
        yearBar.setOnProgressChangeListener(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer integer) {

                int progress_to_date;

                if (integer == 0) progress_to_date = yearOptions - 1;
                else progress_to_date = (int) Math.floor((100.0 - integer) * yearOptions / 100.0);
                yearGuess = progress_to_date + startYear; // updates guess

                for (int i = 0; i < yearOptions; i++) {
                    TextView thisYearBox = (TextView) yearBoxes.getChildAt(i);
                    if (i == progress_to_date) thisYearBox.setBackgroundColor(colorTry);
                    else thisYearBox.setBackgroundColor(colorback);
                }
                return null;
            }
        });

        final ProgressBar progressBar = findViewById(R.id.progressBar);
        int col = getResources().getColor(R.color.colorButtons);

        if (gameType == GameType.SUDDEN_DEATH) {
            col = getResources().getColor(R.color.g10);
            progressBar.setProgress(100);
        }

        progressBar.getProgressDrawable().setColorFilter(col, PorterDuff.Mode.SRC_IN);

        fillWithNewQuestion();

    }

    private void fillWithNewQuestion(){

        CURRENT_LEVEL++;
        yearGuess = 0;

        guessing = true;

        Button buttonGuessYear = findViewById(R.id.buttonGuessYear);
        buttonGuessYear.setVisibility(View.VISIBLE);

        Button buttonNextQuestion = findViewById(R.id.buttonNextQuestion);
        buttonNextQuestion.setVisibility(View.INVISIBLE);

        // disable seekbar
        VerticalSeekBar yearBar = findViewById(R.id.yearBar);
        yearBar.setUseThumbToSetProgress(true);
        yearBar.setClickToSetProgress(true);

        // get year boxes
        final ViewGroup yearBoxes = findViewById(R.id.yearBoxes);
        final int yearOptions = yearBoxes.getChildCount();

        final int colorback = getResources().getColor(R.color.background);

        for (int i = 0; i < yearOptions; i++) {
            yearBoxes.getChildAt(i).setBackgroundColor(colorback);
        }

        // Get random year between the min and max
        CORRECT_YEAR = MIN_YEAR + (int) ((MAX_YEAR - MIN_YEAR) * Math.random());

        int numberQuestions = TEXT_FIELDS_QUESTIONS.length;
        headlinesSelected = databaseHandler.getNewsByYear(CORRECT_YEAR, numberQuestions);

        // Fill all the questions
        for (int i = 0; i < numberQuestions; i++) {

            TextView questionTextField = findViewById(TEXT_FIELDS_QUESTIONS[i]);

            // Set the text to autosize
            questionTextField.setText(headlinesSelected.get(i).getHeadline());
            headlineArray.add(headlinesSelected.get(i));
        }

        //fill year bar
        int randomInt = (int) (yearOptions * Math.random());
        startYear = CORRECT_YEAR - randomInt;

        // check if none in future
        if (startYear > MAX_YEAR - yearOptions) startYear = MAX_YEAR - yearOptions;

        // check if none before 2000
        if (startYear < MIN_YEAR) startYear = MIN_YEAR;

        for (int i = 0; i < yearOptions; i++) {
            TextView thisYearBox = (TextView) yearBoxes.getChildAt(i);
            thisYearBox.setText(Integer.toString(startYear + i));
        }

        TextView tv = findViewById(R.id.tower_explaining_tv);

        if (gameType == GameType.DEFAULT) {
            final ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setProgress(CURRENT_LEVEL*10);
            tv.setText("Restantes Perguntas: " + CURRENT_LEVEL + "/10");
        } else tv.setText("Restantes Tentativas: " + Integer.toString(gauntletLevel));

        // start timer
        timer = new CountDownTimer(TIME_TO_ANSWER*1000, 1000) {

            TextView tv = findViewById(R.id.timer_tv);

            public void onTick(long millisUntilFinished) {
                tv.setText(Long.toString(millisUntilFinished / 1000));
            }

            public void onFinish() {
                tv.setText("0");
                Button btn = findViewById(R.id.buttonGuessYear);
                btn.performClick();
            }
        }.start();
    }

    /**
     * Called when the user taps the guess button
     */
    public void guessYear(View view) {

        timer.cancel();
        guessing = false;

        Button buttonGuessYear = findViewById(R.id.buttonGuessYear);
        buttonGuessYear.setVisibility(View.INVISIBLE);

        Button buttonNextQuestion = findViewById(R.id.buttonNextQuestion);
        buttonNextQuestion.setVisibility(View.VISIBLE);

        totalAnswers++;

        // disable seekbar
        VerticalSeekBar yearBar = findViewById(R.id.yearBar);
        yearBar.setUseThumbToSetProgress(false);
        yearBar.setClickToSetProgress(false);

        // get year boxes
        final ViewGroup yearBoxes = findViewById(R.id.yearBoxes);
        final int yearOptions = yearBoxes.getChildCount();

        final int tries = getResources().getColor(R.color.tries);
        int thisPoints = 0;
        int saveColor;

        // Assign colors to boxes and points
        for (int i = 0; i < yearOptions; i++) {
            TextView thisYearBox = (TextView) yearBoxes.getChildAt(i);
            int thisYear =  i + startYear;

            if (thisYear == CORRECT_YEAR) {
                saveColor = getResources().getColor(R.color.colorSigma0);
                if (thisYear == yearGuess) {
                    thisPoints = 6;
                    thisYearBox.setBackgroundColor(tries);
                    numberCorrectAnswers++;
                } else thisYearBox.setBackgroundColor(saveColor);
            }
            else if ((thisYear < CORRECT_YEAR && thisYear > CORRECT_YEAR - 2) ||
                    (thisYear > CORRECT_YEAR && thisYear < CORRECT_YEAR + 2)) {
                saveColor = getResources().getColor(R.color.colorSigma1);
                if (thisYear == yearGuess) {
                    thisPoints = 3;
                    thisYearBox.setBackgroundColor(tries);
                    numberCorrectAnswers_1++;
                } else thisYearBox.setBackgroundColor(saveColor);
            }
            else if ((thisYear < CORRECT_YEAR && thisYear > CORRECT_YEAR - 5) ||
                    (thisYear > CORRECT_YEAR && thisYear < CORRECT_YEAR + 5)) {
                saveColor = getResources().getColor(R.color.colorSigma2);
                if (thisYear == yearGuess) {
                    thisPoints = 1;
                    thisYearBox.setBackgroundColor(tries);
                    numberCorrectAnswers_2++;
                } else thisYearBox.setBackgroundColor(saveColor);
            }
            else {
                if (thisYear == yearGuess) thisYearBox.setBackgroundColor(tries);
                else thisYearBox.setBackgroundColor(getResources().getColor(R.color.background));
            }
        }

        if (thisPoints == 0) wrong_answers++;

        final ProgressBar progressBar = findViewById(R.id.progressBar);

        // updates level and points
        if (gameType == GameType.SUDDEN_DEATH) {
            if (thisPoints == 0) {
                gauntletLevel -= 1;
            }
            else if (thisPoints == 3) gauntletLevel++;
            else if (thisPoints == 6) gauntletLevel += 2;

            if (gauntletLevel > 10) gauntletLevel = 10;

            int col;
            if (gauntletLevel == 5) col = getResources().getColor(R.color.g10);
            else if (gauntletLevel == 4) col = getResources().getColor(R.color.g8);
            else if (gauntletLevel == 3) col = getResources().getColor(R.color.g4);
            else if (gauntletLevel == 2) col = getResources().getColor(R.color.g2);
            else if (gauntletLevel == 1) col = getResources().getColor(R.color.g1);
            else col = getResources().getColor(R.color.g1);

            progressBar.setProgress(gauntletLevel*20);
            progressBar.getProgressDrawable().setColorFilter(col, PorterDuff.Mode.SRC_IN);

            TextView tv = findViewById(R.id.tower_explaining_tv);
            tv.setText("Tentativas Restantes: " + gauntletLevel);

        }
        currentPoints += thisPoints;

        TextView points = findViewById(R.id.points);
        points.setText(Integer.toString(currentPoints));

        // endgame: triggers scores
        if ((totalAnswers == 10 && gameType == GameType.DEFAULT) ||
                (gauntletLevel < 1 && gameType == GameType.SUDDEN_DEATH)) buttonNextQuestion.setText("Ver Pontuação");
    }

    public void getNextQuestion(View view){
        number_help = 0;

        // endgame: triggers scores
        if ((totalAnswers == 10 && gameType == GameType.DEFAULT) ||
                (gauntletLevel < 1 && gameType == GameType.SUDDEN_DEATH)) {
            Intent i = new Intent(GameActivity.this, ScoreBoardActivity.class);
            i.putExtra(MainActivity.EXTRA_GAME_TYPE, gameType.name());
            i.putExtra(CURRENT_POINTS_NAME, Integer.toString(currentPoints));
            i.putExtra(TOTAL_ANSWERS_NAME, Integer.toString(totalAnswers));
            i.putExtra(CORRECT_ANSWERS_NAME, Integer.toString(numberCorrectAnswers));
            i.putExtra(CORRECT_ANSWERS_NAME_1, Integer.toString(numberCorrectAnswers_1));
            i.putExtra(CORRECT_ANSWERS_NAME_2, Integer.toString(numberCorrectAnswers_2));
            i.putExtra(WRONG_ANSWERS_NAME, Integer.toString(wrong_answers));
            startActivity(i);
            finish();
        } else fillWithNewQuestion();
    }

    // to detect if user left app during question
    @Override
    protected void onPause() {

        super.onPause();
        if (guessing) {
            Button btn = findViewById(R.id.buttonGuessYear);
            btn.performClick();
        }
    }
}
