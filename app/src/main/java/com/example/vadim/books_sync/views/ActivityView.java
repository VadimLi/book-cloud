package com.example.vadim.books_sync.views;

public interface ActivityView {
    void addQueryTextListener();
    void createAdapter();
    interface ActivityViewForFolders {
       void addClickListenerForCreatorFolder();
    }
}
