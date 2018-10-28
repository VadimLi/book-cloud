package com.example.vadim.books_sync.views;

import android.view.View;

public interface ActivityView {
    void addQueryTextListener();
    void createAdapter();
    View getCustomActionBar();
    interface ActivityViewForFolders {
       void addClickListenerForCreatorFolder();
    }
}
