package com.example.throwback;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.BlurMaskFilter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import com.lukelorusso.verticalseekbar.VerticalSeekBar;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class QuestionActivity extends AppCompatActivity {

    public static final String QUESTION_FIELD = "com.example.throwback.question";
    public static final String QUESTION_CORRECT_YEAR = "com.example.throwback.correctYear";
    public static final String QUESTION_GUESS_YEAR = "com.example.throwback.guessYear";

    public static final int NUMBER_MAXIMUM_HELP = 3;
    public static final Integer[] TEXT_FIELDS_QUESTIONS = {R.id.question1, R.id.question2,
            R.id.question3};

    public static int YEAR_OPTIONS = 17;

    public static int MIN_YEAR = 2001;
    public static int MAX_YEAR = 2019;


    public static int CORRECT_YEAR;
    private List<Headline> headlinesSelected;

    public int number_help = 0;
    public DatabaseHandler databaseHandler;


    public int yearGuess;
    public int startYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        databaseHandler = new DatabaseHandler(this);

        // Get random year between the min and max
        CORRECT_YEAR = MIN_YEAR + (int) ((MAX_YEAR - MIN_YEAR) * Math.random());

        headlinesSelected = databaseHandler.getNewsByYear(CORRECT_YEAR, NUMBER_MAXIMUM_HELP);

        // Fill all the questions
        for (int i = 0; i < NUMBER_MAXIMUM_HELP; i++) {

            TextView questionTextField = findViewById(TEXT_FIELDS_QUESTIONS[i]);

            // Set the text to autosize
            questionTextField.setText(headlinesSelected.get(i).getHeadline());
            TextViewCompat.setAutoSizeTextTypeWithDefaults(questionTextField,
                    TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

            // Blur all help questions except the first one
            if (i > 0){
                applyBlurMaskFilter(questionTextField, BlurMaskFilter.Blur.NORMAL);
            }
        }


        //fill year bar
        int randomInt = (int) (YEAR_OPTIONS * Math.random());
        startYear = CORRECT_YEAR - randomInt;

        // check if none in future
        if (startYear > MAX_YEAR - YEAR_OPTIONS) startYear = MAX_YEAR - YEAR_OPTIONS;

        // check if none before 2000
        if (startYear < MIN_YEAR) startYear = MIN_YEAR;

        // TODO: Solve this hardcoded rule. Now all TextViews are inside the parent, therefore one can have access to it bu index of the parent children.
        for (int i = 0; i < YEAR_OPTIONS; i++) {
            TextView year_plus = findViewById(getResources().getIdentifier("yearStart_plus" + Integer.toString(i), "id", getPackageName()));
            year_plus.setText(Integer.toString(startYear + i));
        }

        VerticalSeekBar yearBar = findViewById(R.id.yearBar);

        final int colorback = getResources().getColor(R.color.background);
        final int colorTry = getResources().getColor(R.color.tries);

        yearBar.setOnProgressChangeListener(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer integer) {

                int progress_to_date;

                if (integer == 0) progress_to_date = YEAR_OPTIONS - 1;
                else progress_to_date = (int) Math.floor((100.0 - integer) * YEAR_OPTIONS / 100.0);
                yearGuess = progress_to_date + startYear; // updates guess

                for (int i = 0; i < YEAR_OPTIONS; i++) {
                    TextView textBox = findViewById(getResources().getIdentifier("yearStart_plus" + Integer.toString(i), "id", getPackageName()));
                    if (i == progress_to_date) textBox.setBackgroundColor(colorTry);
                    else textBox.setBackgroundColor(colorback);
                }
                return null;
            }
        });
    }

    /**
     * Called when the user taps the guess button
     */
    public void guessYear(View view) {

        Button buttonGuessYear = findViewById(R.id.buttonGuessYear);
        buttonGuessYear.setVisibility(View.INVISIBLE);

        Button buttonNextQuestion = findViewById(R.id.buttonNextQuestion);
        buttonNextQuestion.setVisibility(View.VISIBLE);

        Button buttonHelp = findViewById(R.id.buttonHelp);
        buttonHelp.setVisibility(View.INVISIBLE);

        MainActivity.TOTAL_ANSWERS++;

//        Intent intent = new Intent(this, AnswerToQuestion.class);
//        intent.putExtra(QUESTION_FIELD, textView.getText().toString());
//        intent.putExtra(QUESTION_GUESS_YEAR, yearGuess);
//        intent.putExtra(QUESTION_CORRECT_YEAR, CORRECT_YEAR);
//
//        startActivity(intent);

        final int colorback = getResources().getColor(R.color.background);
        final int colorSigma1 = getResources().getColor(R.color.colorSigma1);
        final int colorSigma2 = getResources().getColor(R.color.colorSigma2);

        for (int i = 0; i < YEAR_OPTIONS; i++) {
            TextView textBox = findViewById(getResources().getIdentifier("yearStart_plus" + Integer.toString(i), "id", getPackageName()));
            int thisYear =  i + startYear;
            if (thisYear == yearGuess && yearGuess == CORRECT_YEAR) textBox.setBackgroundColor(Color.GREEN);
            else if (thisYear == yearGuess) textBox.setBackgroundColor(Color.RED);
            else if (thisYear == CORRECT_YEAR) textBox.setBackgroundColor(Color.GREEN);
            else if (thisYear < CORRECT_YEAR && thisYear > CORRECT_YEAR - 2) textBox.setBackgroundColor(colorSigma1);
            else if (thisYear > CORRECT_YEAR && thisYear < CORRECT_YEAR + 2) textBox.setBackgroundColor(colorSigma1);
            else if (thisYear < CORRECT_YEAR && thisYear > CORRECT_YEAR - 5) textBox.setBackgroundColor(colorSigma2);
            else if (thisYear > CORRECT_YEAR && thisYear < CORRECT_YEAR + 5) textBox.setBackgroundColor(colorSigma2);
            else textBox.setBackgroundColor(colorback);
        }
    }

    public void getNextQuestion(View view){
        Intent intent = new Intent(this, QuestionActivity.class);
        startActivity(intent);
    }

    public void getHelp(View view) {

        number_help++;
        TextView questionTextField = (TextView) findViewById(TEXT_FIELDS_QUESTIONS[number_help]);
        // FIXME: The line below should not exist
        questionTextField.setText(headlinesSelected.get(number_help).getHeadline());
        questionTextField.getPaint().setMaskFilter(null);

        if (number_help == NUMBER_MAXIMUM_HELP - 1) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    // Custom method to apply BlurMaskFilter to a TextView text
    // https://android--examples.blogspot.com/2015/11/how-to-use-blurmaskfilter-in-android.html
    protected void applyBlurMaskFilter(TextView tv, BlurMaskFilter.Blur style) {

        // Define the blur effect radius
        float radius = tv.getTextSize() / 3;

        // Initialize a new BlurMaskFilter instance
        BlurMaskFilter filter = new BlurMaskFilter(radius, style);

        // Set the TextView layer type
        tv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);


        // Finally, apply the blur effect on TextView text
        tv.getPaint().setMaskFilter(filter);

    }
}
