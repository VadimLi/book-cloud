package com.example.vadim.books_sync.presenters.services;

import java.util.Arrays;
import java.util.List;

public class Formats {

    public static final String PDF = "pdf";

    public static final String DOCX = "docx";

    public static final String DOC = "doc";

    public static final String DJVU = "djvu";

    public static final String EPUB = "epub";

    public enum Format {

        PDF(Formats.PDF),

        DOCX(Formats.DOCX),

        DOC(Formats.DOC),

        DJVU(Formats.DJVU),

        EPUB(Formats.EPUB);

        final String format;

        Format(final String format) {
            this.format = format;
        }

        public String getFormat() {
            return format;
        }

    }

    public static boolean notCheckNameOfFormat(final String formatName) {
        final List<Format> formats = Arrays.asList(Format.values());
        for (Format format : formats) {
            if ( format.getFormat().equals(formatName) ) {
                return false;
            }
        }
        return true;
    }

}
