package com.example.vadim.books_sync.presenters.utils;


public enum Format {

    PDF("pdf"),

    DOCX("docx");

    final String format;

    Format(final String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

}
