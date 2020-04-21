package com.example.throwback;

import android.content.Intent;
import android.graphics.BlurMaskFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

public class MainActivity extends AppCompatActivity {

    public static final int TOTAL_NUMBER_QUESTIONS_PER_GAME = 4;
    public static final int TIME_SHOW_FINAL_ANSWER = 4000;
    public static final int TIME_SHOW_FINAL_STATISTICS = 4000;

    public static final String EXTRA_GAME_TYPE = "GAME_TYPE";
    public static final GameType EXTRA_GAME_TYPE_DEFAULT_VALUE = GameType.DEFAULT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        // It is here to create the database for the first time
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = findViewById(R.id.gameButtons);
        view.bringToFront();

        TextView textCenter = findViewById(R.id.journalTextCenter);
        TextView textRight = findViewById(R.id.journalTextRight);
        TextView textLeft = findViewById(R.id.journalTextLeft);

        applyBlurMaskFilter(textCenter, BlurMaskFilter.Blur.NORMAL);
        applyBlurMaskFilter(textRight, BlurMaskFilter.Blur.NORMAL);
        applyBlurMaskFilter(textLeft, BlurMaskFilter.Blur.NORMAL);



        TextView dateView = findViewById(R.id.mainMenuDate);

        dateView.setText(DateUtils.getToday(true));

        // Set the text to autosize
        TextViewCompat.setAutoSizeTextTypeWithDefaults((TextView) findViewById(R.id.journalTextLeft),
                TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeWithDefaults((TextView) findViewById(R.id.journalTextCenter),
                TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeWithDefaults((TextView) findViewById(R.id.journalTextRight),
                TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
    }

    /** Called when the user taps the Start Game button */
    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        String gametype = view.getTag().toString();
        intent.putExtra(EXTRA_GAME_TYPE, gametype);

        startActivity(intent);
    }

    public void topScores(View view) {
        Intent intent = new Intent(this, TopScoreBoard.class);
        startActivity(intent);
    }

    // Custom method to apply BlurMaskFilter to a TextView text
    // https://android--examples.blogspot.com/2015/11/how-to-use-blurmaskfilter-in-android.html
    protected static void applyBlurMaskFilter(TextView tv, BlurMaskFilter.Blur style) {

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
