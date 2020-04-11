package com.example.throwback;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_MONTH = "month";
    private static final String COLUMN_DAY = "day";


    private static String CSV_PATH;
    private static final String CSV_FILE = "all_news_extracted.csv";
    private static final String CSV_SPLIT_BY = "\t";

    SQLiteDatabase database;

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        boolean dbexist = checkdatabase();
        if (dbexist) {
            opendatabase();
        } else {
            System.out.println("Database doesn't exist");

            try {
                createdatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkdatabase() {

        boolean checkdb = false;
        try {
            String myPath = context.getDatabasePath(DATABASE_NAME).toString();
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch(SQLiteException e) {
            System.out.println("Database doesn't exist");
        }
        return checkdb;
    }

    private void copydatabase() throws IOException {
        //Open your local db as the input stream
        InputStream myinput = context.getAssets().open(DATABASE_NAME);

        File outfile = new File(context.getDatabasePath(DATABASE_NAME).toString());
        outfile.setWritable(true);

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outfile);

        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer))>0) {
            myOutput.write(buffer,0,length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myinput.close();
    }

    public void opendatabase() throws SQLException {
        //Open the database
        String mypath = context.getDatabasePath(DATABASE_NAME).toString();
        database = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void close() {
        if(database != null) {
            database.close();
        }
        super.close();
    }


    public void createdatabase() throws IOException {
        boolean dbexist = checkdatabase();
        if(!dbexist) {
            this.getReadableDatabase();
            try {
                copydatabase();
            } catch(IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Headline> getNewsByYear(int year, int limit) {

        String whereClause = COLUMN_YEAR + "=?";
        String[] whereArgs = new String[] {
                Integer.toString(year)
        };

        @SuppressLint("DefaultLocale") Cursor cursor = database.query(TABLE_NAME,
                new String[]{COLUMN_HEADLINE, COLUMN_ULR, COLUMN_DATE, COLUMN_YEAR, COLUMN_MONTH,
                        COLUMN_DAY}, whereClause, whereArgs,
                null, null, "RANDOM()", format("%d", limit));

        List<Headline> headlines = new ArrayList<>();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Headline headline = new Headline(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getInt(3),
                        cursor.getInt(4), cursor.getInt(5));
                headlines.add(headline);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return headlines;
    }

    public List<Headline> getRandomHeadlines(int numberHeadlines){

        Cursor cursor = database.query(TABLE_NAME,
                new String[]{COLUMN_HEADLINE, COLUMN_ULR, COLUMN_DATE, COLUMN_YEAR,
                        COLUMN_MONTH, COLUMN_DAY}, null, null, null,
                null, "RANDOM()", Integer.toString(numberHeadlines));

        List<Headline> headlines = new ArrayList<>();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Headline headline = new Headline(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getInt(3),
                        cursor.getInt(4), cursor.getInt(5));
                headlines.add(headline);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return headlines;
    }


    private void insertDataDatabaseFromCSV(){

        List<Headline> headlines = get_all_headlines();

        int id = 1;
        for (Headline headline: headlines){
            ContentValues values = new ContentValues();
            values.put(COLUMN_HEADLINE, headline.getHeadline());
            values.put(COLUMN_ULR, headline.getUrl());
            values.put(COLUMN_DATE, headline.getDate());


            values.put(COLUMN_YEAR, headline.getYear());
            values.put(COLUMN_MONTH, headline.getMonth());
            values.put(COLUMN_DAY, headline.getDay());

            values.put(COLUMN_ID, id);
            id++;

            // Insert the new row, returning the primary key value of the new row
            long newRowId = database.insert(TABLE_NAME, null, values);
        }

        /*

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " ( " +
                    COLUMN_ID + " int NOT NULL, " +
                    COLUMN_HEADLINE + " TEXT NOT NULL, " +
                    COLUMN_ULR + " TEXT NOT NULL, " +
                    COLUMN_DATE + " TEXT NOT NULL, " +
                    COLUMN_YEAR + " INT NOT NULL, " +
                    COLUMN_MONTH + " INT NOT NULL, " +
                    COLUMN_DAY + " INT NOT NULL, " +
                    " PRIMARY KEY(" + COLUMN_ID + "))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
         */
    }


    private List<Headline> get_all_headlines() {

        List<Headline> headlineCollection = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(this.context.getAssets().open(CSV_FILE)))) {

            String line;

            int lineNumber = 0;

            while ((line = br.readLine()) != null) {

                lineNumber++;

                // Skip the header
                if (lineNumber == 1) {
                continue;
                }

                String[] lineNewsInfo = line.split(CSV_SPLIT_BY);

                String headline = lineNewsInfo[0];
                String url = lineNewsInfo[2];
                String date = lineNewsInfo[1];

                int year = Integer.parseInt(date.split("/")[2]);
                int month = Integer.parseInt(date.split("/")[1]);
                int day = Integer.parseInt(date.split("/")[0]);

                headlineCollection.add(new Headline(headline, url, date, year, month, day));


            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return headlineCollection;
    }
}
