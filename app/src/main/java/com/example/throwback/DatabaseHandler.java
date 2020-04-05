package com.example.throwback;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.*;

public class DatabaseHandler extends SQLiteOpenHelper {


    //Context
    private Context context;

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Version
    private static final String DATABASE_NAME = "arquivopt.db";

    //Table Name
    private static final String TABLE_NAME = "news_data";

    //Table Fields
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_HEADLINE = "headline";
    private static final String COLUMN_ULR = "url";
    private static final String COLUMN_DATE = "date";

    private static String CSV_PATH;
    private static final String CSV_FILE = "all_news_extracted.csv";
    private static final String CSV_SPLIT_BY = "\t";

    SQLiteDatabase database;

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        database = getWritableDatabase();
        /*
        List<Headline> headlines = get_all_headlines();
        int id = 1;
        for (Headline headline: headlines){
            ContentValues values = new ContentValues();
            values.put(COLUMN_HEADLINE, headline.getHeadline());
            values.put(COLUMN_ULR, headline.getUrl());
            values.put(COLUMN_DATE, headline.getDate());
            values.put(COLUMN_ID, id);
            id++;

            // Insert the new row, returning the primary key value of the new row
            long newRowId = database.insert(TABLE_NAME, null, values);
        }*/
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( " +
                COLUMN_ID + " int NOT NULL, " +
                COLUMN_HEADLINE + " TEXT NOT NULL, " +
                COLUMN_ULR + " TEXT NOT NULL, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                " PRIMARY KEY(" + COLUMN_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private List<Headline> get_all_headlines() {

        List<Headline> headlineCollection = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(this.context.getAssets().open(CSV_FILE)))) {

            String line;
            while ((line = br.readLine()) != null) {

                String[] lineNewsInfo = line.split(CSV_SPLIT_BY);

                String headline = lineNewsInfo[3];
                String url = lineNewsInfo[5];
                String date = lineNewsInfo[4];
                headlineCollection.add(new Headline(headline, url, date));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return headlineCollection;
    }

    public List<Headline> getRandomHeadlines(){
        //String query = String.format("Select %s, %s, %s from %s ORDER BY RANDOM() LIMIT 1;",
        //COLUMN_HEADLINE, COLUMN_ULR, COLUMN_DATE, TABLE_NAME);

        Cursor cursor = database.query(TABLE_NAME,
                new String[]{COLUMN_HEADLINE, COLUMN_ULR, COLUMN_DATE}, null,
                null, null,null, "RANDOM()", "1");

        List<Headline> headlines = new ArrayList<>();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Headline headline = new Headline(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2));
               headlines.add(headline);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return headlines;
    }

    public List<Headline> getNewsByYear(int year, int limit) {
        //String query = String.format("Select %s, %s, %s from %s ORDER BY RANDOM() LIMIT 1;",
        //COLUMN_HEADLINE, COLUMN_ULR, COLUMN_DATE, TABLE_NAME);
        String whereClause = COLUMN_DATE + " LIKE ?";
        String[] whereArgs = new String[] {
                Integer.toString(year)  + "%"
        };

        @SuppressLint("DefaultLocale") Cursor cursor = database.query(TABLE_NAME,
                new String[]{COLUMN_HEADLINE, COLUMN_ULR, COLUMN_DATE}, whereClause, whereArgs,
                null, null, "RANDOM()", format("%d", limit));

        List<Headline> headlines = new ArrayList<>();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Headline headline = new Headline(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2));
                headlines.add(headline);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return headlines;
    }
}
