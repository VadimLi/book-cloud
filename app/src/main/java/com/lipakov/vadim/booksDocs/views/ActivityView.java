package com.lipakov.vadim.booksDocs.views;

import android.view.View;

public interface ActivityView {
    void addQueryTextListener();
    void createAdapter();
    View getCustomActionBar();
    interface ActivityViewForFolders {
       void addClickListenerForCreatorFolder();
    }
}
