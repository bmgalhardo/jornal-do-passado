package com.example.throwback;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.graphics.BlurMaskFilter;

import androidx.appcompat.app.AppCompatActivity;

import com.lukelorusso.verticalseekbar.VerticalSeekBar;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class QuestionActivity extends AppCompatActivity {

    public static final String QUESTION = "com.example.throwback.question";
    public static final String QUESTION_CORRECT_YEAR = "com.example.throwback.correctYear";
    public static final String QUESTION_GUESS_YEAR = "com.example.throwback.guessYear";

    public static int correctYear;
    public static String question;
    public int number_help;
    public DatabaseHandler databaseHandler;
    public static int YEAR_OPTIONS = 17;
    public static int CURRENT_YEAR = 2020;

    public String yearGuess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        TextView questionTextField = findViewById(R.id.question1);
        TextView questionTextField2 = findViewById(R.id.question2);
        TextView questionTextField3 = findViewById(R.id.question3);
//        findViewById(R.id.question2).setVisibility(View.INVISIBLE);
//        findViewById(R.id.question3).setVisibility(View.INVISIBLE);
//        findViewById(R.id.view2).setVisibility(View.INVISIBLE);

        applyBlurMaskFilter(questionTextField2, BlurMaskFilter.Blur.NORMAL);
        applyBlurMaskFilter(questionTextField3, BlurMaskFilter.Blur.NORMAL);

        databaseHandler = new DatabaseHandler(this);
        number_help = 0;

        Headline headline = databaseHandler.getRandomHeadlines().get(0);
        correctYear = Integer.parseInt(headline.getDate().split("-")[0]);
        question = headline.getHeadline();
        questionTextField.setText(headline.getHeadline());

        //fill year bar
        int randomInt = (int) (YEAR_OPTIONS * Math.random());
        int startyear = correctYear - randomInt;

        // check if none in future
        if (startyear > CURRENT_YEAR - YEAR_OPTIONS) startyear = CURRENT_YEAR - YEAR_OPTIONS;

        // fill question possible years
        for (int i = 0; i < YEAR_OPTIONS; i++) {
            TextView year_plus = findViewById(getResources().getIdentifier("yearStart_plus" + Integer.toString(i), "id", getPackageName()));
            year_plus.setText(Integer.toString(startyear + i));
        }

        VerticalSeekBar yearBar = findViewById(R.id.yearBar);


//        final EditText hintBox = findViewById(R.id.guessTheYear);
        final int colorSigma0 = getResources().getColor(R.color.colorSigma0);
        final int colorSigma1 = getResources().getColor(R.color.colorSigma1);
        final int colorSigma2 = getResources().getColor(R.color.colorSigma2);
        final int colorback = getResources().getColor(R.color.vintage1);

        final int finalStartyear = startyear;
        yearBar.setOnProgressChangeListener(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer integer) {
                int progress_to_date;
                if (integer == 0) progress_to_date = YEAR_OPTIONS - 1;
                else progress_to_date = (int) Math.floor((100.0 - integer) * YEAR_OPTIONS / 100.0);
                yearGuess = Integer.toString(progress_to_date + finalStartyear); // updates guess
//                hintBox.setHint(Integer.toString(progress_to_date + finalStartyear));
                // update text boxes
                for (int i = 0; i < YEAR_OPTIONS; i++) {
                    TextView textBox = findViewById(getResources().getIdentifier("yearStart_plus" + Integer.toString(i), "id", getPackageName()));
                    if (i == progress_to_date) textBox.setBackgroundColor(colorSigma0);
                    else if (i < progress_to_date && i > progress_to_date - 2)
                        textBox.setBackgroundColor(colorSigma1);
                    else if (i > progress_to_date && i < progress_to_date + 2)
                        textBox.setBackgroundColor(colorSigma1);
                    else if (i < progress_to_date && i > progress_to_date - 5)
                        textBox.setBackgroundColor(colorSigma2);
                    else if (i > progress_to_date && i < progress_to_date + 5)
                        textBox.setBackgroundColor(colorSigma2);
                    else textBox.setBackgroundColor(colorback);
                }
                return null;
            }
        });
    }

    /**
     * Called when the user taps the Start Game button
     */
    public void guessYear(View view) {

//        EditText editText = findViewById(R.id.guessTheYear);

        MainActivity.TOTAL_ANSWERS++;

        Intent intent = new Intent(this, AnswerToQuestion.class);
        intent.putExtra(QUESTION, question);
        intent.putExtra(QUESTION_GUESS_YEAR, yearGuess);
        intent.putExtra(QUESTION_CORRECT_YEAR, correctYear);

        startActivity(intent);

    }

    public void getHelp(View view) {

        number_help++;

        List<Headline> helpHeadlines = databaseHandler.getNewsByYear(correctYear, 1);
        // TODO mudar martelo (ver questao da question)
        if (number_help == 1) {
            TextView questionTextField = (TextView) findViewById(R.id.question2);
            questionTextField.setText(helpHeadlines.get(0).getHeadline());
            questionTextField.getPaint().setMaskFilter(null);
//            findViewById(R.id.question2).setVisibility(View.VISIBLE);
        } else if (number_help == 2) {
            TextView questionTextField = (TextView) findViewById(R.id.question3);
            questionTextField.setText(helpHeadlines.get(0).getHeadline());
            questionTextField.getPaint().setMaskFilter(null);
//            findViewById(R.id.question3).setVisibility(View.VISIBLE);
//            findViewById(R.id.view2).setVisibility(View.VISIBLE);
        }
        question = helpHeadlines.get(0).getHeadline();

        if (number_help == MainActivity.NUMBER_MAXIMUM_HELP) {
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
