package com.mkoffeine.ankiclone.decks;

import android.database.Cursor;

import com.mkoffeine.ankiclone.common.BaseView;
import com.mkoffeine.ankiclone.model.Deck;

/**
 * Created by mKoffeine on 03.12.2016.
 */

public interface DecksContract {
    interface View extends BaseView<Presenter> {
        void showDecks(Cursor cursor);

        void addDeck();

        boolean removeDeck(Deck deck);

        void renameDeck(Deck deck);
    }

    interface Presenter {
        void loadDecks();

        void addDeck(String name);

        void removeDeck(Deck deck);

        void renameDeck(Deck deck, String name);

        void onDestroy();


    }
}
