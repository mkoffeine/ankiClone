package com.mkoffeine.ankiclone.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mkoffeine.ankiclone.db.DeckSqliteHelper;
import com.mkoffeine.ankiclone.model.DeckContract.DeckEntry;


/**
 * Created by mKoffeine on 07.01.2017.
 */

public class DeckDbHolder {
    private DeckSqliteHelper helper;

    public DeckDbHolder(Context context) {
        this.helper = new DeckSqliteHelper(context);
    }

    public Cursor loadDeckCursor() {
        SQLiteDatabase db = helper.getReadableDatabase();
        long t = System.currentTimeMillis();
        Cursor c = db.query(DeckEntry.TABLE_NAME, DeckEntry.PROJECTION, null, null, null, null, "name ASC");
        long t2 = System.currentTimeMillis();
        System.out.println("---------db.query time: " + (t2 - t));
        return c;
    }

    public long createNewDeck(Deck deck) {
        String name = deck.getName();
        if (name == null || name.length() == 0) {
            return -1;
        }
        ContentValues values = new ContentValues();
        values.put(DeckEntry.COLUMN_NAME, name);
        values.put(DeckEntry.COLUMN_DESCRIPTION, deck.getDescription());

        SQLiteDatabase db = helper.getWritableDatabase();
        long id = db.insert(DeckEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public int deleteDeck(Deck deck) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int delete = db.delete(DeckEntry.TABLE_NAME, DeckEntry._ID + "=?", new String[]{String.valueOf(deck.getId())});
        db.close();
        return delete;
    }

    public int updateDeck(Deck deck) {
        String name = deck.getName();
        if (name == null || name.length() == 0) {
            return 0;
        }
        ContentValues values = new ContentValues();
        values.put(DeckEntry.COLUMN_NAME, name);
        values.put(DeckEntry.COLUMN_DESCRIPTION, deck.getDescription());

        SQLiteDatabase db = helper.getWritableDatabase();
        int update = db.update(DeckEntry.TABLE_NAME, values, DeckEntry._ID + "=?", new String[]{String.valueOf(deck.getId())});
        db.close();
        return update;
    }
}
