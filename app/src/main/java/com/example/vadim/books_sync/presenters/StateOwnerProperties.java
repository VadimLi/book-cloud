package com.example.vadim.books_sync.presenters;

public interface StateOwnerProperties {
    void removeDocument();
    void renameDocument();
    void shareDocument();
    void addToFolder(final String materialName);
    void addNewFolder(final String name);
    void dismiss();
}
