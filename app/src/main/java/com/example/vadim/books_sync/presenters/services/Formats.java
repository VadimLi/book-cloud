package com.example.vadim.books_sync.presenters.services;

public class Formats {

    public static final String PDF = "pdf";

    public static final String DOCX = "docx";

    public enum Format {

        PDF(Formats.PDF),

        DOCX(Formats.DOCX);

        final String format;

        Format(final String format) {
            this.format = format;
        }

        public String getFormat() {
            return format;
        }

    }

}
