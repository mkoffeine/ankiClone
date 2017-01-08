package com.mkoffeine.ankiclone.decks;

import android.database.Cursor;

import com.mkoffeine.ankiclone.model.Deck;
import com.mkoffeine.ankiclone.model.DeckDbHolder;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class DeckPresenter implements DecksContract.Presenter {

    private DecksContract.View decksView;
    private Disposable disposable;
    private DeckDbHolder deckDbHolder;

    public DeckPresenter(DecksContract.View decksView, DeckDbHolder deckDbHolder) {
        this.decksView = decksView;
        this.deckDbHolder = deckDbHolder;
    }

    @Override
    public void loadDecks() {
        disposable = Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> e) throws Exception {
                long t = System.currentTimeMillis();
                Cursor c = deckDbHolder.loadDeckCursor();
                long t2 = System.currentTimeMillis();
                e.onNext(c);
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Cursor>() {
                    @Override
                    public void accept(Cursor cursor) throws Exception {
                        decksView.showDecks(cursor);
                    }
                });
    }

    @Override
    public void addDeck(final String name) {
        long res = deckDbHolder.createNewDeck(new Deck(name));
        if (res != -1) {
            loadDecks();
        }
    }

    @Override
    public void removeDeck(Deck deck) {
        int deleted = deckDbHolder.deleteDeck(deck);
        if (deleted > 0) {
            loadDecks();
        }
    }

    @Override
    public void renameDeck(Deck deck, String name) {
        deck.setName(name);
        int updated = deckDbHolder.updateDeck(deck);
        if (updated > 0) {
            loadDecks();
        }
    }

    @Override
    public void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

 /*   private void addAlot(int size) {
        long t = System.currentTimeMillis();
        for (int i = 0; i<size;i++) {
            deckDbHolder.createNewDeck(new Deck("rand"+rand(10000)));
        }
        long t2 = System.currentTimeMillis();
        System.out.println("---------addAlot time: " + (t2-t));
    }
    Random random = new Random();
    private int rand(int max) {
        return random.nextInt(max);
    }*/
}
