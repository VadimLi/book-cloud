package com.example.vadim.books_sync.presenters;

public enum Notifications {

    REMOVE("Документ удален"),

    RENAME("Документ переименован"),

    SHARE("Документ отправлен"),

    ADD_TO_FOLDER("Документ добавлен");

    final String notification;

    Notifications(String notification) {
        this.notification = notification;
    }

    public String getNotification() {
        return notification;
    }

}
