package com.mkoffeine.ankiclone.decks;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mkoffeine.ankiclone.R;
import com.mkoffeine.ankiclone.common.components.CursorRecyclerViewAdapter;
import com.mkoffeine.ankiclone.model.Deck;
import com.mkoffeine.ankiclone.model.DeckDbHolder;


public class DeckView extends Fragment implements DecksContract.View {

    private RecyclerView recyclerView;
    private DecksContract.Presenter presenter;
    private DeckCursorAdapter deckCursorAdapter;

    @Override
    public void setPresenter(DecksContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        System.out.println("---onCreate");

        DeckPresenter deckPresenter = new DeckPresenter(this, new DeckDbHolder(getContext().getApplicationContext()));
        setPresenter(deckPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.deck_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.deckRecycler);
        deckCursorAdapter = new DeckCursorAdapter(null);
        recyclerView.setAdapter(deckCursorAdapter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDeck();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.loadDecks();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void showDecks(Cursor cursor) {
        deckCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void addDeck() {
        buildDeckEdit(null);
    }

    @Override
    public void renameDeck(Deck deck) {
        buildDeckEdit(deck);
    }

    private void buildDeckEdit(final Deck deck) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.typeDeckName);

        final EditText input = new EditText(getContext());
        builder.setView(input);

        if (deck == null) {
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name = input.getText().toString();
                    presenter.addDeck(name);
                }
            });
        } else {
            input.setText(deck.getName());
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name = input.getText().toString();
                    presenter.renameDeck(deck, name);
                }
            });
        }
        builder.show();
    }

    @Override
    public void removeDeck(final Deck deck) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Do you want to remove deck: " + deck.getName());
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.removeDeck(deck);
            }
        });
        builder.show();
    }

    private class DeckItemHolder extends RecyclerView.ViewHolder {
        public TextView name;

        DeckItemHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.deckTextName);
            name.setClickable(false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Deck deck = (Deck) v.getTag();
                    Toast.makeText(getContext(), "  " + deck, Toast.LENGTH_LONG).show();
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    PopupMenu popupMenu = new PopupMenu(getContext(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Deck deck = (Deck) v.getTag();
                            switch (item.getItemId()) {
                                case R.id.menu_open:
                                    System.out.println("-------show " + deck);
                                    break;
                                case R.id.menu_delete:
                                    removeDeck(deck);
                                    break;
                                case R.id.menu_rename:
                                    renameDeck(deck);
                                    break;
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                    return true;
                }
            });
        }
    }

    private class DeckCursorAdapter extends CursorRecyclerViewAdapter<DeckItemHolder> {

        public DeckCursorAdapter(Cursor cursor) {
            super(cursor);
        }

        @Override
        public DeckItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.deck_item, parent, false);

            return new DeckItemHolder(v);
        }

        @Override
        public void onBindViewHolder(DeckItemHolder viewHolder, Cursor cursor) {
            Deck deck = Deck.fromCursor(cursor);
            viewHolder.itemView.setTag(deck);
            viewHolder.name.setText(cursor.getPosition() + "  " + deck.getName());
        }
    }
}
