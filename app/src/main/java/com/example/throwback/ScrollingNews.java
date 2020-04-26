package com.example.throwback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class ScrollingNews extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_news);

        LinearLayout layout =  findViewById(R.id.buttonLayout);

        // TODO button template in view is cumbersome, investigate templates
        // also make a template with two text views in a layout to allow differente colors for
        // headline and date
        Button btnTmp = findViewById(R.id.buttonTemplate);
        btnTmp.setText(concatText(GameActivity.headlineArray.get(0)));
        btnTmp.setTag(GameActivity.headlineArray.get(0).getUrl());

        for (int i = 1; i < GameActivity.headlineArray.size(); i++) {
            Button btn = new Button(this);
            ViewGroup.LayoutParams li = btnTmp.getLayoutParams();
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoURL(v);
                }
            });
            btn.setLayoutParams(li);
            btn.setText(concatText(GameActivity.headlineArray.get(i)));
            btn.setTag(GameActivity.headlineArray.get(i).getUrl());
            btn.setBackground(getResources().getDrawable(R.drawable.cards_round));
            btn.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            btn.setPadding(btnTmp.getPaddingLeft(),btnTmp.getPaddingTop(),btnTmp.getPaddingRight(),btnTmp.getPaddingBottom());
            layout.addView(btn);
        }


    }
    public void gotoURL(View view){
        Button b = (Button) view;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(b.getTag().toString()));
        startActivity(browserIntent);
    }

    public String concatText(Headline headline) {
        String hl =headline.getHeadline();
        String date = headline.getDate();

        return hl+"\n\n"+date;
    }
}
