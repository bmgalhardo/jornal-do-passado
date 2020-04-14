package com.example.throwback;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.BlurMaskFilter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import com.lukelorusso.verticalseekbar.VerticalSeekBar;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class GameActivity extends AppCompatActivity {

    public static final String QUESTION_FIELD = "com.example.throwback.question";
    public static final String QUESTION_CORRECT_YEAR = "com.example.throwback.correctYear";
    public static final String QUESTION_GUESS_YEAR = "com.example.throwback.guessYear";

    public static final int NUMBER_MAXIMUM_HELP = 3;
    public static final Integer[] TEXT_FIELDS_QUESTIONS = {R.id.question1, R.id.question2,
            R.id.question3};

    public static int MIN_YEAR = 2001;
    public static int MAX_YEAR = 2020;

    public static int NUMBER_CORRECT_ANSWERS;
    public static int TOTAL_ANSWERS;
    public static int CURRENT_LEVEL;
    public static int CURRENT_POINTS;

    public static int CORRECT_YEAR;
    private List<Headline> headlinesSelected;

    public int number_help;
    public DatabaseHandler databaseHandler;

    public int yearGuess;
    public int startYear;

    public int gameType = 1; // 0 normal game, 1 gauntlet
    public int gauntletLevel = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        NUMBER_CORRECT_ANSWERS = 0;
        TOTAL_ANSWERS = 0;
        CURRENT_LEVEL = 0;
        CURRENT_POINTS = 0;

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

        fillWithNewQuestion();

        if (gameType == 1) for (int i = 1; i <= 10; i++) {
            String level_i = "level" + i;
            View level_square = findViewById(getResources().getIdentifier(level_i, "id", getPackageName()));
            level_square.setBackgroundColor(getResources().getColor(R.color.g10));
        }
    }

    private void fillWithNewQuestion(){

        CURRENT_LEVEL++;

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

        TextView points = findViewById(R.id.points);
        points.setText("Score: " + Integer.toString(CURRENT_POINTS));

        // Fill all the questions
        for (int i = 0; i < numberQuestions; i++) {

            TextView questionTextField = findViewById(TEXT_FIELDS_QUESTIONS[i]);

            // Set the text to autosize
            questionTextField.setText(headlinesSelected.get(i).getHeadline());
            TextViewCompat.setAutoSizeTextTypeWithDefaults(questionTextField,
                    TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
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

    }

    /**
     * Called when the user taps the guess button
     */
    public void guessYear(View view) {

        Button buttonGuessYear = findViewById(R.id.buttonGuessYear);
        buttonGuessYear.setVisibility(View.INVISIBLE);

        Button buttonNextQuestion = findViewById(R.id.buttonNextQuestion);
        buttonNextQuestion.setVisibility(View.VISIBLE);

        TOTAL_ANSWERS++;

        // disable seekbar
        VerticalSeekBar yearBar = findViewById(R.id.yearBar);
        yearBar.setUseThumbToSetProgress(false);
        yearBar.setClickToSetProgress(false);

        // get year boxes
        final ViewGroup yearBoxes = findViewById(R.id.yearBoxes);
        final int yearOptions = yearBoxes.getChildCount();

        final int colorback = getResources().getColor(R.color.background);
        final int colorSigma0 = getResources().getColor(R.color.colorSigma0);
        final int colorSigma1 = getResources().getColor(R.color.colorSigma1);
        final int colorSigma2 = getResources().getColor(R.color.colorSigma2);
        final int colorSigma0_select = getResources().getColor(R.color.colorSigma0_select);
        final int colorSigma1_select = getResources().getColor(R.color.colorSigma1_select);
        final int colorSigma2_select = getResources().getColor(R.color.colorSigma2_select);
        final int tries = getResources().getColor(R.color.tries);
        int thisPoints = 0;
        int saveColor = tries;

        // Assign colors to boxes and points
        for (int i = 0; i < yearOptions; i++) {
            TextView thisYearBox = (TextView) yearBoxes.getChildAt(i);
            int thisYear =  i + startYear;
            if (thisYear == CORRECT_YEAR) {
                if (thisYear == yearGuess) {
                    thisPoints = 6;
                    thisYearBox.setBackgroundColor(colorSigma0_select);
                    saveColor = colorSigma0;
                } else thisYearBox.setBackgroundColor(colorSigma0);
            }
            else if ((thisYear < CORRECT_YEAR && thisYear > CORRECT_YEAR - 2) ||
                    (thisYear > CORRECT_YEAR && thisYear < CORRECT_YEAR + 2)) {
                if (thisYear == yearGuess) {
                    thisPoints = 3;
                    thisYearBox.setBackgroundColor(colorSigma1_select);
                    saveColor = colorSigma1;
                } else thisYearBox.setBackgroundColor(colorSigma1);
            }
            else if ((thisYear < CORRECT_YEAR && thisYear > CORRECT_YEAR - 5) ||
                    (thisYear > CORRECT_YEAR && thisYear < CORRECT_YEAR + 5)) {
                if (thisYear == yearGuess) {
                    thisPoints = 1;
                    thisYearBox.setBackgroundColor(colorSigma2_select);
                    saveColor = colorSigma2;
                } else thisYearBox.setBackgroundColor(colorSigma2);
            }
            else {
                if (thisYear == yearGuess) thisYearBox.setBackgroundColor(tries);
                else thisYearBox.setBackgroundColor(colorback);
            }
        }

        // updates level and points
        if (gameType == 0) {
            String level_i = "level" + CURRENT_LEVEL;
            View level_square = findViewById(getResources().getIdentifier(level_i, "id", getPackageName()));
            level_square.setBackgroundColor(saveColor);
        } else {
            if (thisPoints == 0) gauntletLevel -= 2;
            else gauntletLevel += thisPoints;

            if (gauntletLevel > 10) gauntletLevel = 10;

            int col;
            if (gauntletLevel == 10) col = getResources().getColor(R.color.g10);
            else if (gauntletLevel > 7) col = getResources().getColor(R.color.g8);
            else if (gauntletLevel > 5) col = getResources().getColor(R.color.g6);
            else if (gauntletLevel > 3) col = getResources().getColor(R.color.g4);
            else if (gauntletLevel > 1) col = getResources().getColor(R.color.g2);
            else col = getResources().getColor(R.color.g1);

            for (int i = 1; i <= 10; i++) {
                String level_i = "level" + i;
                View level_square = findViewById(getResources().getIdentifier(level_i, "id", getPackageName()));
                if (i > gauntletLevel) level_square.setBackgroundResource(R.drawable.rectangle_stroke);
                else level_square.setBackgroundColor(col);
            }
        }
        CURRENT_POINTS += thisPoints;

        if ((TOTAL_ANSWERS == 10 && gameType == 0) || (gauntletLevel < 1 && gameType == 1)) {

            buttonNextQuestion.setVisibility(View.INVISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(GameActivity.this, ScoreBoardActivity.class);
                    startActivity(i);
                    finish();
                }
            }, MainActivity.TIME_SHOW_FINAL_ANSWER);
        }
    }

    public void getNextQuestion(View view){
        number_help = 0;
        fillWithNewQuestion();
    }

    // Custom method to apply BlurMaskFilter to a TextView text
    // https://android--examples.blogspot.com/2015/11/how-to-use-blurmaskfilter-in-android.html
    protected void applyBlurMaskFilter(TextView tv, BlurMaskFilter.Blur style) {

        // Define the blur effect radius
        float radius = tv.getTextSize() / 2;

        // Initialize a new BlurMaskFilter instance
        BlurMaskFilter filter = new BlurMaskFilter(radius, style);

        // Set the TextView layer type
        tv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);


        // Finally, apply the blur effect on TextView text
        tv.getPaint().setMaskFilter(filter);

    }
}
