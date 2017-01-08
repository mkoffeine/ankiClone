package com.mkoffeine.ankiclone.model;


import android.database.Cursor;

import com.mkoffeine.ankiclone.model.DeckContract.DeckEntry;

/**
 * Created by mKoffeine on 03.12.2016.
 */

public class Deck {

    //    @PrimaryKey
    private long id;
    private String name;
    private String description;

    public Deck() {
    }

    public Deck(String name) {
        this.name = name;
    }

    public Deck(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Deck(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Deck(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Deck{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static Deck fromCursor(Cursor c) {
        int id = c.getInt(DeckEntry.COLUMN_INDEX_ID);
        String name = c.getString(DeckEntry.COLUMN_INDEX_NAME);
        String desc = c.getString(DeckEntry.COLUMN_INDEX_DECRIPTION);
        return new Deck(id, name, desc);
    }
}
