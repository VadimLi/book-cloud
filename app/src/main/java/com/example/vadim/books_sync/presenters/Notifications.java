package com.example.vadim.books_sync.presenters;

public enum Notifications {

    REMOVE_FILE("Документ удален"),

    RENAME_FILE("Документ переименован"),

    SHARE_FILE("Документ отправлен"),

    ADD_TO_FOLDER("Документ добавлен"),

    ADD_NEW_FOLDER("Папка добавлена"),

    REMOVE_FOLDER("Папка удалена"),

    RENAME_FOLDER("Папка переименована");

    final String notification;

    Notifications(String notification) {
        this.notification = notification;
    }

    public String getNotification() {
        return notification;
    }

}
