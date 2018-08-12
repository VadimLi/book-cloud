package com.example.vadim.books_sync.presenters.states_of_document;

import com.example.vadim.books_sync.presenters.MaterialPresenter;

public abstract class State {
    public abstract void doState(MaterialPresenter materialPresenter);
}
