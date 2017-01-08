package com.mkoffeine.ankiclone.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mkoffeine.ankiclone.model.DeckContract.DeckEntry;


public class DeckSqliteHelper extends SQLiteOpenHelper {
    private static final String NAME_DB = "decks.db";
    private static final int VERSION = 1;

    private static final String SQL_CREATE_DECKS =
            "CREATE TABLE " + DeckEntry.TABLE_NAME + " (" +
                    DeckEntry._ID + " INTEGER PRIMARY KEY," +
                    DeckEntry.COLUMN_NAME + " TEXT NOT NULL," +
                    DeckEntry.COLUMN_DESCRIPTION + " TEXT)";


    public DeckSqliteHelper(Context context) {
        super(context, NAME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DECKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
