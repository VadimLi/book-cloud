package com.example.vadim.books_sync.presenters.services;

import java.util.Arrays;
import java.util.List;

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

    public static boolean checkNameOfFormat(final String formatName) {
        final List<Format> formats = Arrays.asList(Format.values());
        for (Format format : formats) {
            if ( format.getFormat().equals(formatName) ) {
                return false;
            }
        }
        return true;
    }

}
