package com.mkoffeine.ankiclone.model;

import android.provider.BaseColumns;

/**
 * Created by mKoffeine on 07.01.2017.
 */

public class DeckContract {

    public static class DeckEntry implements BaseColumns {
        public static final String TABLE_NAME = "deck";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";

        public static final String[] PROJECTION = {_ID, COLUMN_NAME, COLUMN_DESCRIPTION};
        public static final int COLUMN_INDEX_ID = 0;
        public static final int COLUMN_INDEX_NAME = 1;
        public static final int COLUMN_INDEX_DECRIPTION = 2;

    }
}
