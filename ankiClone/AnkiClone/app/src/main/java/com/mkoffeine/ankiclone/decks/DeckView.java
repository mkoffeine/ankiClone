package com.mkoffeine.ankiclone.decks;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.typeDeckName);

        final EditText input = new EditText(getContext());
        builder.setView(input);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                presenter.addDeck(name);
            }
        });
        builder.show();
    }

    @Override
    public boolean removeDeck(Deck deck) {
        return false;
    }

    @Override
    public void renameDeck(Deck deck) {

    }

    private class DeckItemHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public Deck deck;


        DeckItemHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.deckTextName);
            name.setClickable(false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getParent() != null && v.getParent().getParent() != null) {
                        DeckItemHolder vh = (DeckItemHolder) recyclerView.getChildViewHolder(v);
                        Toast.makeText(getContext(), "  " + vh.deck, Toast.LENGTH_LONG).show();
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    DeckItemHolder vh = (DeckItemHolder) recyclerView.getChildViewHolder(v);
                    presenter.removeDeck(vh.deck);
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
            viewHolder.deck = deck;
            viewHolder.name.setText(cursor.getPosition() + "  " + deck.getName());
        }
    }
}
