package com.lipakov.vadim.booksDocs.presenters;

public enum Notification {

    REMOVE_FILE("Документ удален"),

    RENAME_FILE("Документ переименован"),

    SHARE_FILE("Документ отправлен"),

    ADD_TO_FOLDER("Документ добавлен в папку"),

    FILE_EXISTS("Документ уже существует в папке"),

    ADD_NEW_FOLDER("Папка добавлена"),

    REMOVE_FOLDER("Папка удалена"),

    RENAME_FOLDER("Папка переименована");

    final String notification;

    Notification(String notification) {
        this.notification = notification;
    }

    public String getNotification() {
        return notification;
    }

}
