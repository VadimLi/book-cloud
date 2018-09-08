package com.example.vadim.books_sync.presenters;

public interface StateOwnerProperties {
    void removeDocument();
    void renameDocument();
    void shareDocument();
    void addToFolderOrNewFolder(final String name);
    void dismiss();
}
